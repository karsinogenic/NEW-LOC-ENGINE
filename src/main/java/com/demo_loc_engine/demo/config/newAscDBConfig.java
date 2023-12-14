package com.demo_loc_engine.demo.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
public class newAscDBConfig {

    // @Bean(name = "sqlServerDataSource")
    // @ConfigurationProperties(prefix = "spring.datasource.sqlserver")
    // public DataSource sqlServerDataSource() {
    // return DataSourceBuilder.create().build();
    // }

    // @Bean(name = "sqlServerJdbcTemplate")
    // public JdbcTemplate sqlServerJdbcTemplate(@Qualifier("sqlServerDataSource")
    // DataSource dataSource) {
    // return new JdbcTemplate(dataSource);
    // }
}
