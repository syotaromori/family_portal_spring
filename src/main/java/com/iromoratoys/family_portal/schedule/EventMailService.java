package com.iromoratoys.family_portal.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class EventMailService {

    private static final Logger log = LoggerFactory.getLogger(EventMailService.class);

    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("M月d日(E)", Locale.JAPANESE);
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm");

    private final ObjectProvider<JavaMailSender> senderProvider;
    private final String to;
    private final String from;
    private final String fromName;

    public EventMailService(ObjectProvider<JavaMailSender> senderProvider,
                            @Value("${app.notification.to:}") String to,
                            @Value("${spring.mail.username:}") String from,
                            @Value("${app.notification.from-name:family_portal}") String fromName) {
        this.senderProvider = senderProvider;
        this.to = to;
        this.from = from;
        this.fromName = fromName;
    }

    // 宛先とSMTPが設定されているときだけ通知する(未設定でもアプリ自体は普通に動く)
    public boolean isEnabled() {
        return !to.isBlank() && senderProvider.getIfAvailable() != null;
    }

    // 予定の登録通知。画面の応答を待たせないよう非同期で送る
    @Async
    public void notifyCreated(Event event) {
        if (!isEnabled()) {
            return;
        }
        send("【予定追加】" + event.getTitle() + "(" + event.getStartDateTime().format(DATE) + ")",
                "予定が追加されました。\n\n" + describe(event));
    }

    // 前日リマインド。送れたら true(失敗時は呼び出し側が次回再試行できるよう false)
    public boolean sendReminder(Event event) {
        return send("【明日の予定】" + event.getTitle() + "(" + event.getStartDateTime().format(DATE) + ")",
                "明日の予定のリマインドです。\n\n" + describe(event));
    }

    private boolean send(String subject, String body) {
        JavaMailSender sender = senderProvider.getIfAvailable();
        if (sender == null || to.isBlank()) {
            return false;
        }
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            if (!from.isBlank()) {
                // 表示名つきの差出人(「family_portal <admin@...>」)にする
                if (fromName.isBlank()) {
                    helper.setFrom(from);
                } else {
                    helper.setFrom(from, fromName);
                }
            }
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body);
            sender.send(message);
            return true;
        } catch (Exception e) {
            // メール送信の失敗で予定の登録自体を失敗させない
            log.warn("メール送信に失敗しました: {} ({})", subject, e.getMessage());
            return false;
        }
    }

    private String describe(Event event) {
        StringBuilder sb = new StringBuilder();
        sb.append("タイトル: ").append(event.getTitle()).append('\n');
        sb.append("日時: ").append(formatPeriod(event)).append('\n');
        if (event.getLocation() != null && !event.getLocation().isBlank()) {
            sb.append("場所: ").append(event.getLocation()).append('\n');
        }
        if (!event.getAssignees().isEmpty()) {
            sb.append("担当者: ").append(String.join("、", event.getAssignees())).append('\n');
        }
        if (event.getMemo() != null && !event.getMemo().isBlank()) {
            sb.append("メモ: ").append(event.getMemo()).append('\n');
        }
        return sb.toString();
    }

    private String formatPeriod(Event event) {
        LocalDateTime start = event.getStartDateTime();
        LocalDateTime end = event.getEndDateTime();
        boolean sameDay = start.toLocalDate().equals(end.toLocalDate());

        if (event.isAllDay()) {
            return sameDay
                    ? start.format(DATE) + " 終日"
                    : start.format(DATE) + " ～ " + end.format(DATE) + " 終日";
        }
        return sameDay
                ? start.format(DATE) + " " + start.format(TIME) + " ～ " + end.format(TIME)
                : start.format(DATE) + " " + start.format(TIME) + " ～ " + end.format(DATE) + " " + end.format(TIME);
    }
}
