package com.eduardodomingues.promptvault.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI promptVaultOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("PromptVault API")
                .version("1.0.0")
                .description("Gerenciador de Prompts para Times de IA. Versionamento semântico, tags de contexto e endpoint de teste com integração simulada (Azure OpenAI / Ollama).")
                .contact(new Contact()
                        .name("Eduardo Domingues")
                        .email("contato.eduardocd@gmail.com")));
    }
}
