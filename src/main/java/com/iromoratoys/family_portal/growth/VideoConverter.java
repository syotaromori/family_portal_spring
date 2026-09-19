package com.iromoratoys.family_portal.growth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 動画をffmpegでブラウザ再生しやすい形式(H.264 + AAC の MP4)に変換する。
 *
 * iPhoneの動画は既定でHEVC(H.265)のため、Windowsのブラウザでは再生できないことがある。
 * ffmpegが見つからない・変換に失敗した場合は false を返すだけで、呼び出し側は元の動画をそのまま使う。
 */
@Component
public class VideoConverter {

    private static final Logger log = LoggerFactory.getLogger(VideoConverter.class);

    private static final Pattern VIDEO_CODEC = Pattern.compile("Video: (\\w+)");

    // iPhoneのHDR動画(HLG / PQ)。SDRのH.264にそのまま変換すると色が白っぽくなるためトーンマッピングする
    private static final Pattern HDR_TRANSFER = Pattern.compile("arib-std-b67|smpte2084");

    private static final String HDR_TO_SDR_FILTER =
            "zscale=t=linear:npl=100,format=gbrpf32le,zscale=p=bt709,"
                    + "tonemap=tonemap=hable:desat=0,zscale=t=bt709:m=bt709:r=tv,format=yuv420p";

    private static final long PROBE_TIMEOUT_SECONDS = 30;

    @Value("${app.ffmpeg.path:ffmpeg}")
    private String ffmpegPath;

    @Value("${app.ffmpeg.timeout-minutes:8}")
    private long timeoutMinutes;

    /**
     * input を H.264/AAC の MP4 に変換して output に書き出す。
     * 映像がすでにH.264なら再エンコードせず、MP4への詰め替えのみ行う(高速・無劣化)。
     *
     * @return 変換に成功したら true。ffmpeg未導入・失敗・タイムアウト時は false(output は残さない)
     */
    public boolean convertToMp4(Path input, Path output) {
        boolean success = false;
        try {
            Result probe = run(List.of(ffmpegPath, "-hide_banner", "-nostdin", "-i", abs(input)),
                    PROBE_TIMEOUT_SECONDS);
            // 出力先を指定していないため終了コードは常に非0になる。ここでは標準エラーの情報だけを使う
            Matcher codec = VIDEO_CODEC.matcher(probe.output());
            if (!codec.find()) {
                log.warn("動画の映像ストリームを検出できなかったため変換しません: {}", tail(probe.output()));
                return false;
            }
            String videoCodec = codec.group(1);
            boolean hdr = HDR_TRANSFER.matcher(probe.output()).find();

            List<String> cmd = new ArrayList<>(List.of(
                    ffmpegPath, "-y", "-nostdin", "-hide_banner", "-loglevel", "error",
                    "-i", abs(input),
                    // iPhoneのMOVにはタイムコード等のメタデータトラックがありMP4に入れられないため、映像と音声だけ取り出す
                    "-map", "0:v:0", "-map", "0:a:0?"));

            if ("h264".equals(videoCodec)) {
                cmd.addAll(List.of("-c", "copy"));
            } else {
                cmd.addAll(List.of("-c:v", "libx264", "-preset", "veryfast", "-crf", "23",
                        "-pix_fmt", "yuv420p"));
                if (hdr) {
                    cmd.addAll(List.of("-vf", HDR_TO_SDR_FILTER,
                            "-color_primaries", "bt709", "-color_trc", "bt709", "-colorspace", "bt709"));
                }
                cmd.addAll(List.of("-c:a", "aac", "-b:a", "128k"));
            }
            // 全体のダウンロード完了前でも再生を始められるようにする
            cmd.addAll(List.of("-movflags", "+faststart", "-f", "mp4", abs(output)));

            log.info("動画変換を開始します: codec={}, hdr={}, mode={}", videoCodec, hdr,
                    "h264".equals(videoCodec) ? "remux" : "transcode");
            long started = System.currentTimeMillis();

            Result result = run(cmd, TimeUnit.MINUTES.toSeconds(timeoutMinutes));
            if (result.exitCode() != 0 || !Files.exists(output) || Files.size(output) == 0) {
                log.warn("動画変換に失敗したため元の動画を保存します(exit={}): {}",
                        result.exitCode(), tail(result.output()));
                return false;
            }

            log.info("動画変換が完了しました({}秒)", (System.currentTimeMillis() - started) / 1000);
            success = true;
            return true;

        } catch (IOException e) {
            // ffmpegが見つからない場合など
            log.warn("ffmpegを実行できないため動画を変換しません({}): {}", ffmpegPath, e.getMessage());
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } finally {
            if (!success) {
                // 失敗・タイムアウト時に中途半端な出力ファイルが残らないようにする
                try {
                    Files.deleteIfExists(output);
                } catch (IOException ignored) {
                }
            }
        }
    }

    private record Result(int exitCode, String output) {}

    private Result run(List<String> command, long timeoutSeconds) throws IOException, InterruptedException {
        // 標準出力/エラーをパイプで読むと、タイムアウトを掛けられずブロックするためファイルに逃がす
        Path logFile = Files.createTempFile("ffmpeg-", ".log");
        try {
            Process process = new ProcessBuilder(command)
                    .redirectErrorStream(true)
                    .redirectOutput(logFile.toFile())
                    .start();

            if (!process.waitFor(timeoutSeconds, TimeUnit.SECONDS)) {
                process.destroyForcibly();
                process.waitFor();
                return new Result(-1, "タイムアウト(" + timeoutSeconds + "秒)");
            }
            return new Result(process.exitValue(),
                    new String(Files.readAllBytes(logFile), StandardCharsets.UTF_8));
        } finally {
            Files.deleteIfExists(logFile);
        }
    }

    private static String abs(Path path) {
        return path.toAbsolutePath().toString();
    }

    private static String tail(String text) {
        String trimmed = text.strip();
        return trimmed.length() <= 1000 ? trimmed : "..." + trimmed.substring(trimmed.length() - 1000);
    }
}
