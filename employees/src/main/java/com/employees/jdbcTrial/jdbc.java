package com.employees.jdbcTrial;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Properties;
import java.util.Scanner;

public class jdbc {
	public static void jdbc() {
		Properties property=new Properties();
		Scanner sc=new Scanner(System.in);
		try(InputStream input=new FileInputStream("src/main/resources/Appilication.properties")){
			property.load(input);
			
			String url=property.getProperty("db.url");
			String user=property.getProperty("db.username");
			String password=property.getProperty("db.password");
			Connection conn=DriverManager.getConnection(url,user,password);
			
			conn.setAutoCommit(false);
			Statement stmt=conn.createStatement();
			String sql="select *from student";
			
			ResultSet rs=stmt.executeQuery(sql);
			while(rs.next()) {
				System.out.println("id:"+rs.getInt("id"));
				System.out.println("name:"+rs.getString("name"));
				System.out.println("email:"+rs.getString("email"));
				System.out.println("dob:"+rs.getDate("dob"));
				System.out.println("cgpa:"+rs.getFloat("cgpa"));
			}
			PreparedStatement psmt=conn.prepareStatement("update student set name=? where id=?");
			System.out.println("Enter name");
			String name=sc.next();
			psmt.setString(1, name);
			psmt.setInt(2, 1);
			
			
			int rows=psmt.executeUpdate();
			System.out.println("rows:"+rows);
			rs=stmt.executeQuery(sql);
			while(rs.next()) {
				System.out.println("id:"+rs.getInt("id"));
				System.out.println("name:"+rs.getString("name"));
				System.out.println("email:"+rs.getString("email"));
				System.out.println("dob:"+rs.getDate("dob"));
				System.out.println("cgpa:"+rs.getFloat("cgpa"));
			}
			System.out.println("insert--------");
			String query="INSERT INTO student (id, name, email, phnNo, dob, CGPA)"
					+ "VALUES (?, ?, ?, ?, ?, ?)";
			LocalDate date=LocalDate.of(2025, 12, 01);
			Date sqlDate=Date.valueOf(date);
			PreparedStatement psmt1=conn.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
//			psmt1.setInt(1, 2);
//			psmt1.setString(2, "sunny");
//			psmt1.setString(3, "sunny@gmail.com");
//			psmt1.setString(4, "9876543210");
//			psmt1.setDate(5,sqlDate);
//			psmt1.setDouble(6,8.0);
			
			psmt1.setInt(1, 5);
			psmt1.setString(2, "tharun");
			psmt1.setString(3, "tharun@gmail.com");
			psmt1.setString(4, "9876543210");
			psmt1.setDate(5,sqlDate);
			psmt1.setDouble(6,8.0);
			
			int r=psmt1.executeUpdate();
			
		    rs=stmt.executeQuery(sql);
			
			while(rs.next()) {
				System.out.println("id:"+rs.getInt("id"));
				System.out.println("name:"+rs.getString("name"));
				System.out.println("email:"+rs.getString("email"));
				System.out.println("dob:"+rs.getDate("dob"));
				System.out.println("cgpa:"+rs.getFloat("cgpa"));
			}
			conn.commit();
			rs.close();
			stmt.close();
			conn.close();
			
		}catch(IOException e) {
			System.out.println(e.getMessage());
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
