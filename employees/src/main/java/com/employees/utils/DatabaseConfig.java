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
	    String file="";
		switch(storage) {
		case DB:
			file="Application.properties";
			break;
		case SUPABASE:
			file="postgres.properties";
			break;
		}
		try {
			Properties props = new Properties();
			InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream(file);

			if (input == null) {
				throw new DataAccessException("Application.properties not found");
			}
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
			throw new RuntimeException("Error initializing HikariCP pool: " + e.getMessage());
		}
	}

	public static Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

}
