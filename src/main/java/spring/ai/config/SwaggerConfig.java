package spring.ai.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Spring AI Chatbot API")
                .description("Spring AI(OpenAI, Gemini)를 활용한 챗봇 서비스 API 명세서입니다.")
                .version("v1.0.0"));
    }
}