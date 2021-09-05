package ap.streaksaver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SpringFoxConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.any())
                .build();
    }

    @Bean
    RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Streaksaver")
                .contact(new Contact("Alessandro Pinna", null, "alessandro.pinna95@gmail.com"))
                .license("MIT")
                .licenseUrl("https://opensource.org/licenses/MIT")
                .build();
    }
}