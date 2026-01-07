package com.employees.main;
import java.io.IOException;

import org.json.simple.parser.ParseException;

import com.employees.jdbcTrial.jdbc;
import com.employees.view.Menu;
public class EmployeeApp {
	public static void main(String args[]) throws IOException,ParseException {
		//jdbc.jdbc();
		Menu.showMenu();
	}
}
