package live.tikgik.expenses.account.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "userHeaderAuth",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = "X-User-Id"
)
@SecurityScheme(
        name = "userNameHeaderAuth",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = "X-Username"
)
@SecurityScheme(
        name = "userEmailHeaderAuth",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = "X-Email"
)
@SecurityScheme(
        name = "userRolesHeaderAuth",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = "X-Roles"
)
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("My Spring Boot API")
                        .version("1.0.0"))
                .addSecurityItem(new SecurityRequirement()
                        .addList("userHeaderAuth")
                        .addList("userNameHeaderAuth")
                        .addList("userEmailHeaderAuth")
                        .addList("userRolesHeaderAuth"));
    }
}
