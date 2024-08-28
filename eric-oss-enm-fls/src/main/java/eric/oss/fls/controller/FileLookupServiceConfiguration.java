package eric.oss.fls.controller;

import java.util.stream.Stream;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.DatabaseStartupValidator;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UrlPathHelper;

import eric.oss.fls.repository.model.NodeInfo;

/**
 * @author xjaimah
 *
 */
@Configuration
public class FileLookupServiceConfiguration implements WebMvcConfigurer {

    /**
     * This method will allow ";" in request url
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        urlPathHelper.setRemoveSemicolonContent(false);
        configurer.setUrlPathHelper(urlPathHelper);
    }
    

    @Bean
    public DatabaseStartupValidator databaseStartupValidator(DataSource dataSource) {
        DatabaseStartupValidator dsv = new DatabaseStartupValidator();
        dsv.setDataSource(dataSource);
        dsv.setValidationQuery(DatabaseDriver.POSTGRESQL.getValidationQuery());
        dsv.setTimeout(10000);
        return dsv;
    }
    
    @Bean
    public static BeanFactoryPostProcessor dependsOnPostProcessor() {
        return bf -> {
            String[] flyway = bf.getBeanNamesForType(NodeInfo.class);
            Stream.of(flyway)
                    .map(bf::getBeanDefinition)
                    .forEach(it -> it.setDependsOn("databaseStartupValidator"));

            String[] jpa = bf.getBeanNamesForType(EntityManagerFactory.class);
            Stream.of(jpa)
                    .map(bf::getBeanDefinition)
                    .forEach(it -> it.setDependsOn("databaseStartupValidator"));
        };
    }
}