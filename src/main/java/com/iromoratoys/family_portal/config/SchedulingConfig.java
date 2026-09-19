package com.iromoratoys.family_portal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

// 予定のリマインド(定期実行)とメール送信(非同期)を有効にする
@Configuration
@EnableScheduling
@EnableAsync
public class SchedulingConfig {
}
