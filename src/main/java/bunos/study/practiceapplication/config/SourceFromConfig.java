package bunos.study.practiceapplication.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class SourceFromConfig {
    @Primary
    @Bean(name = "fromDataProperties")
    @ConfigurationProperties(prefix = "sources.from-datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = "fromDataSource")
    public HikariDataSource dataSource(@Qualifier("fromDataProperties") DataSourceProperties dataProperties) {
        return dataProperties.initializeDataSourceBuilder().type(HikariDataSource.class)
                .build();
    }

    @Primary
    @Bean(name = "fromEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("fromDataSource") DataSource dataSource
    ) {
        return builder
                .dataSource(dataSource)
                .packages("bunos.study.practiceapplication.models.migration")
                .persistenceUnit("from")
                .build();
    }

    @Primary
    @Bean(name = "fromTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("fromEntityManagerFactory") EntityManagerFactory
                    entityManagerFactory
    ) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
