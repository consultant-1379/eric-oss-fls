package eric.oss.fls.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import eric.oss.fls.controller.NodeInfoRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@ComponentScan("eric.oss")
@EnableAutoConfiguration
@EnableJpaRepositories(basePackageClasses = { NodeInfoRepository.class })
@EntityScan("eric.oss")
@OpenAPIDefinition(info = @Info(title = "File Lookup Service", version = "1.0", description = "File Lookup service"))
public class FileLookupServiceApplication {
    
    
    public static void main(String[] args) {
        final Logger logger = LogManager.getLogger(FileLookupServiceApplication.class);
        SpringApplication.run(FileLookupServiceApplication.class, args);
        logger.info("file-lookup-service Instance started.");
    }
}