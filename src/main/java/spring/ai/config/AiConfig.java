package spring.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    /*
        * ChatClient Bean 등록
        * @RequiredArgsConstructor를 사용하면 단순히 this.chatClient = chatClient가 되므로
        * 이를 해결하려면 ChatClient 자체를 미리 빈(Bean)으로 등록해야함.
     */
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder.build();
    }
}