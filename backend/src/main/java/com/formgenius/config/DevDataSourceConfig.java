package com.formgenius.config;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.net.InetSocketAddress;
import java.net.Socket;

@Configuration
@Profile("dev")
public class DevDataSourceConfig {

    private static final Logger log = LoggerFactory.getLogger(DevDataSourceConfig.class);

    @Bean
    @Primary
    public DataSource dataSource() {
        boolean postgresReachable = false;
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("127.0.0.1", 5432), 800);
            postgresReachable = true;
        } catch (Exception ignored) {
            postgresReachable = false;
        }

        HikariDataSource ds = new HikariDataSource();
        if (postgresReachable) {
            log.info("Detected active PostgreSQL on localhost:5432 - Connecting dev to PostgreSQL datasource...");
            ds.setDriverClassName("org.postgresql.Driver");
            ds.setJdbcUrl("jdbc:postgresql://localhost:5432/formgenius");
            ds.setUsername("formgenius");
            ds.setPassword("formgenius_secret");
        } else {
            log.info("Local PostgreSQL not running on port 5432 - Using zero-configuration in-memory H2 database (PostgreSQL compatibility mode)...");
            ds.setDriverClassName("org.h2.Driver");
            ds.setJdbcUrl("jdbc:h2:mem:formgenius_dev;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;CASE_INSENSITIVE_IDENTIFIERS=TRUE;DB_CLOSE_DELAY=-1");
            ds.setUsername("sa");
            ds.setPassword("");
        }
        return ds;
    }
}
