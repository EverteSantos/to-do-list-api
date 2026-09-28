package br.com.everte.todolistapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "To-Do List API",
                description = "API REST para gerenciamento de tarefas.",
                version = "1.0.0"
        )
)
public class OpenApiConfig {
}