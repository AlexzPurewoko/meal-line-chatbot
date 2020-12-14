package com.apwdevs.submission.chatbot.mealrecipe.config;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.LineMessagingClientBuilder;
import com.linecorp.bot.client.LineSignatureValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:application.properties")
public class BotConfig {

    @Autowired
    private Environment botEnv;

    @Bean(name = "bot_channel_secret")
    public String getBotChannelSecret() {
        return botEnv.getProperty("bot_channel_secret");
    }

    @Bean(name = "bot_channel_access_token")
    public String getBotChannelAccessToken() {
        return botEnv.getProperty("bot_channel_access_token");
    }

    @Bean(name = "lineSignatureValidator")
    public LineSignatureValidator getSignatureValidator() {
        return new LineSignatureValidator(getBotChannelSecret().getBytes());
    }

    @Bean(name = "lineMessagingClient")
    public LineMessagingClient getMessagingClient() {
        return LineMessagingClient
                .builder(getBotChannelAccessToken())
                .apiEndPoint(LineMessagingClientBuilder.DEFAULT_API_END_POINT)
                .connectTimeout(LineMessagingClientBuilder.DEFAULT_CONNECT_TIMEOUT)
                .readTimeout(LineMessagingClientBuilder.DEFAULT_READ_TIMEOUT)
                .writeTimeout(LineMessagingClientBuilder.DEFAULT_WRITE_TIMEOUT)
                .build();
    }
}
