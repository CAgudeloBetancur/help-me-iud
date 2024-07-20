package co.edu.iudigital.helpmeiud.configs;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SecurityScheme(
        name = "Authorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI apiDocConfig() {
        return new OpenAPI()
            .info(new Info()
                .title("HelpMeIUD API")
                .description("API REST HelpMe IUD")
                .version("1.0.0")
                .contact(new Contact()
                    .name("--> Camilo Agudelo | Laura Blandón | Justin Cardona")
                    .email("camilo.agudelob@est.iudigital.edu.co")
                )
            )
            .externalDocs(new ExternalDocumentation()
                .description("Institución Universitaria Digital de Antioquia")
                .url("https://iudigital.edu.co")
            );
    }
}
