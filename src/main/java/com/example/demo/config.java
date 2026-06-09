package com.example.demo;



import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class config implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Aplica as regras de CORS a todas as rotas da API
                .allowedOrigins("http://localhost:3000") // Permite requisições do seu frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos HTTP permitidos
                .allowedHeaders("*") // Permite todos os cabeçalhos
                .allowCredentials(true) // Permite o envio de cookies e cabeçalhos de autorização
                .maxAge(3600); // Tempo em segundos que o resultado do preflight request pode ser cacheado
    }
}
