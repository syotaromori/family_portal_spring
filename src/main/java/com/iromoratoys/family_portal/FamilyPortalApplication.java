package com.iromoratoys.family_portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FamilyPortalApplication {

	public static void main(String[] args) {
		// Windowsではウイルス対策ソフト(Norton等)がSSL通信を検査するため、その証明書はWindowsの
		// 証明書ストアにしか入っていない。Javaにもそれを参照させないとGmail(SMTP)へのTLS接続に失敗する。
		if (System.getProperty("os.name", "").startsWith("Windows")
				&& System.getProperty("javax.net.ssl.trustStoreType") == null) {
			System.setProperty("javax.net.ssl.trustStoreType", "Windows-ROOT");
		}
		SpringApplication.run(FamilyPortalApplication.class, args);
	}
}


