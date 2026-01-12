package com.employees.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.employees.model.Employee;
import com.employees.model.LoginResult;
import com.employees.security.Roles;
import com.employees.utils.DbConnection;
import com.employees.utils.Util;

public class JdbcEmployeeDaoImpl implements EmployeeDao {

	public LoginResult validateUser(String id, String password) {

		String authQuery = "select password from emp_auth where emp_id=?";
		String roleQuery = "select roles from emp_roles where emp_id=?";

		try (Connection conn = DbConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(authQuery)) {

			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();

			if (!rs.next())
				return new LoginResult(false, null, null);

			String dbPass = rs.getString("password");

			if (!dbPass.equals(Util.hashPassword(password)))
				return new LoginResult(false, null, null);

			Set<Roles> roles = new HashSet<>();
			try (PreparedStatement ps2 = conn.prepareStatement(roleQuery)) {
				ps2.setString(1, id);
				ResultSet rs2 = ps2.executeQuery();

				while (rs2.next()) {
					roles.add(Roles.valueOf(rs2.getString("roles")));
				}
			}

			return new LoginResult(true, id, roles);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return new LoginResult(false, null, null);
	}

	public void addEmployee(Employee emp) {

		String insertEmployees = "insert into employees (emp_name,dept,email,phnNo) values(?,?,?,?)";
		String insertRoles = "insert into emp_roles(emp_id,roles) values(?,?)";
		String insertPassword = "insert into emp_auth(emp_id,password) values(?,?)";
		try (Connection conn = DbConnection.getConnection()) {
			conn.setAutoCommit(false);
			try (PreparedStatement pstmt = conn.prepareStatement(insertEmployees, new String[] { "emp_id" })) {
				pstmt.setString(1, emp.getName());
				pstmt.setString(2, emp.getDept());
				pstmt.setString(3, emp.getEmail());
				pstmt.setString(4, emp.getPhnNo());
				pstmt.executeUpdate();

				ResultSet rs = pstmt.getGeneratedKeys();
				if (!rs.next()) {
					throw new SQLException("ID not generated");

				}
				String generatedId = rs.getString(1);
				try (PreparedStatement pstmt1 = conn.prepareStatement(insertPassword)) {
					pstmt1.setString(1, generatedId);
					pstmt1.setString(2, emp.getPassword());
					pstmt1.executeUpdate();
				}
				try (PreparedStatement pstmt2 = conn.prepareStatement(insertRoles)) {

					for (Roles role : emp.getRole()) {
						pstmt2.setString(1, generatedId);
						pstmt2.setObject(2, role.name(), java.sql.Types.OTHER);
						pstmt2.addBatch();
					}

					pstmt2.executeBatch();
				}
				conn.commit();
			}
		} catch (SQLException e) {

			System.out.println(e.getMessage());
		}
	}

	public void fetchEmployee() {
		String fetchQuery =
				 "select e1.emp_id, e1.emp_name, e1.dept, e1.email, e1.phnNo, e2.roles " +
				 "from employees e1 join emp_roles e2 on e1.emp_id = e2.emp_id";


		try (Connection conn = DbConnection.getConnection()) {
			conn.setAutoCommit(false);
			try (Statement stmt = conn.createStatement()) {
				ResultSet rs = stmt.executeQuery(fetchQuery);
				while (rs.next()) {
					System.out.print("id:" + rs.getString("emp_id") + " | " + "name:" + rs.getString("emp_name") + "|"
							+ "dept:" + rs.getString("dept") + "|" + "email:" + rs.getString("email") + "|" + "phnNo:"
							+ rs.getString("phnNO") + "|" + "roles:" + rs.getString("roles"));
					System.out.println();
				}
				
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void fetchEmployeeById(String id) {

	}

	public void deleteEmployee(String id) {

	}

	public void updateEmployee(String id, Map<String, String> map) {

	}

	public void resetPassword(String id, String password) {

	}

	public void changePassword(String id, String password) {

	}

}
