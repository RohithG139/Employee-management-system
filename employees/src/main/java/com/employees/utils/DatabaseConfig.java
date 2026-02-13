package com.employees.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.employees.enums.Storage;
import com.employees.exceptions.DataAccessException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseConfig {
	private static HikariDataSource dataSource;

	public static void init(Storage storage) {
		String file = "";
		switch (storage) {
		case DB:
			file = "postgres.properties";
			break;
		case SUPABASE:
			file = "supabase.properties";
			break;
		}
		try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream(file);) {

			if (input == null) {
				throw new DataAccessException("properties file not found");
			}
			Properties props = new Properties();
			props.load(input);

			HikariConfig config = new HikariConfig();

			config.setJdbcUrl(props.getProperty("db.url"));
			config.setUsername(props.getProperty("db.username"));
			config.setPassword(props.getProperty("db.password"));
			config.setDriverClassName(props.getProperty("db.driver"));

			config.setMaximumPoolSize(10);
			config.setMinimumIdle(2);
			config.setIdleTimeout(30000);
			config.setConnectionTimeout(30000);

			dataSource = new HikariDataSource(config);
		} catch (Exception e) {
			throw new DataAccessException("Error initializing HikariCP pool: " + e.getMessage());
		}
	}

	public static Connection getConnection() throws SQLException {
		if (dataSource == null) {
			throw new IllegalStateException("Database not initialized Call init() first");
		}
		return dataSource.getConnection();
	}

	public static void shutdown() {
		if (dataSource != null && !dataSource.isClosed()) {
			dataSource.close();
		}
	}
}
