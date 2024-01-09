package bunos.study.practiceapplication.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;

@Configuration
public class AppSourceConfig {
    @Bean(name = "dataProperties")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "dataSource")
    public HikariDataSource dataSource(@Qualifier("dataProperties") DataSourceProperties toDataProperties) {
        return toDataProperties.initializeDataSourceBuilder().type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("dataSource") DataSource dataSource
    ) {
        return
                builder
                        .dataSource(dataSource)
                        .packages("bunos.study.practiceapplication.models")
                        .persistenceUnit("to")
                        .build();
    }
}
