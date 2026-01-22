package com.employees.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import com.employees.enums.Roles;
import com.employees.exceptions.EmployeeNotFoundException;
import com.employees.exceptions.ValidationException;
import com.employees.model.Employee;
import com.employees.model.LoginResult;
import com.employees.utils.Util;

public class JdbcEmployeeDaoImpl implements EmployeeDao {

	public LoginResult validateUser(String id, String password) {

		String authQuery = "select password from emp_auth where emp_id=?";
		String roleQuery = "select roles from emp_roles where emp_id=?";

		try (Connection conn = Util.getConnection();) {
			try (PreparedStatement ps = conn.prepareStatement(authQuery);
					PreparedStatement ps2 = conn.prepareStatement(roleQuery)) {
				ps.setString(1, id);
				ResultSet rs = ps.executeQuery();

				if (!rs.next()) {
					return null;
				}

				String dbPass = rs.getString("password");

				if (!dbPass.equals(password)) {
					
					return null;
				}

				Set<Roles> roles = new HashSet<>();

				ps2.setString(1, id);
				ResultSet rs2 = ps2.executeQuery();

				while (rs2.next()) {
					roles.add(Roles.valueOf(rs2.getString("roles")));
				}
				return new LoginResult( id, roles);

			}
		} catch (SQLException e) {
			System.out.println("login validating error:" + e.getMessage());
		}
		return  null;
	}

	private boolean checkRoleExists(String id, Roles role, Connection conn) {
		String query = "select roles from emp_roles where emp_id = ?";
		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setString(1, id);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				if (rs.getString(1).equals(role.toString())) {
					return true;
				}
			}

		} catch (SQLException e) {
			System.out.println("checking role error" + e.getMessage());
		}
		return false;
	}

	private boolean checkEmpExists(String id, Connection conn) {
		String query = "select emp_id from employees where emp_id=?";
		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setString(1, id);
			ResultSet rs = stmt.executeQuery();
			if (!rs.next()) {
				return false;
			}

		} catch (SQLException e) {
			System.out.println("checking employee error:" + e.getMessage());
		}
		return true;
	}

	public void addEmployee(Employee emp) {

		String insertEmployees = "insert into employees (emp_name, dept, email, phnNo) values (?, ?, ?, ?)";
		String insertPassword = "insert into emp_auth (emp_id, password) values (?, ?)";
		String insertRoles = "insert into emp_roles (emp_id, roles) values (?, ?)";

		Connection conn = null;

		try {
			conn = Util.getConnection();
			conn.setAutoCommit(false);

			try (PreparedStatement empStmt = conn.prepareStatement(insertEmployees, new String[] { "emp_id" });
					PreparedStatement authStmt = conn.prepareStatement(insertPassword);
					PreparedStatement roleStmt = conn.prepareStatement(insertRoles)) {

				empStmt.setString(1, emp.getName());
				empStmt.setString(2, emp.getDept());
				empStmt.setString(3, emp.getEmail());
				empStmt.setString(4, emp.getPhnNo());
				empStmt.executeUpdate();

				String empId;
				try (ResultSet rs = empStmt.getGeneratedKeys()) {
					if (!rs.next()) {
						throw new SQLException("Employee ID not generated");
					}
					empId = rs.getString(1);
				}

				authStmt.setString(1, empId);
				authStmt.setString(2, emp.getPassword());
				authStmt.executeUpdate();

				for (Roles role : emp.getRole()) {
					roleStmt.setString(1, empId);
					roleStmt.setObject(2, role.name(), java.sql.Types.OTHER);
					roleStmt.addBatch();
				}
				roleStmt.executeBatch();

				conn.commit();
			}

		} catch (SQLException e) {
			try {
				if (conn != null)
					conn.rollback();
			} catch (SQLException ex) {
				System.out.println("error during rollback" + e.getMessage());
			}
			System.out.println("failed to add employee:" + e.getMessage());

		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				System.out.println("error during closing" + e.getMessage());
			}
		}
	}

	public void fetchEmployee() {
		String fetchQuery = "select * from employees";
		String fetchRoles = "select roles from emp_roles where emp_id=?";

		try (Connection conn = Util.getConnection();) {
			try (Statement stmt = conn.createStatement(); PreparedStatement psmt = conn.prepareStatement(fetchRoles)) {

				ResultSet rs = stmt.executeQuery(fetchQuery);

				while (rs.next()) {
					String id = rs.getString("emp_id");
					psmt.setString(1, id);
					ResultSet rs1 = psmt.executeQuery();

					System.out.print("id:" + rs.getString("emp_id") + " | " + "name:" + rs.getString("emp_name") + "|"
							+ "dept:" + rs.getString("dept") + "|" + "email:" + rs.getString("email") + "|" + "phnNo:"
							+ rs.getString("phnNO") + "|" + "role: [");
					while (rs1.next()) {
						System.out.print(rs1.getString(1) + " ");

					}
					System.out.print("]");
					System.out.println();
				}

			}
		} catch (SQLException e) {
			System.out.println("error during fetching:" + e.getMessage());
		}
	}

	public void fetchEmployeeById(String id) {
		String fetchQuery = "select * from employees where emp_id=?";
		String fetchRoles = "select roles from emp_roles where emp_id=?";

		try (Connection conn =Util.getConnection();) {
			try (PreparedStatement stmt = conn.prepareStatement(fetchQuery);
					PreparedStatement psmt = conn.prepareStatement(fetchRoles)) {
				stmt.setString(1, id);
				ResultSet rs = stmt.executeQuery();

				while (rs.next()) {

					psmt.setString(1, id);
					ResultSet rs1 = psmt.executeQuery();

					System.out.print("id:" + rs.getString("emp_id") + " | " + "name:" + rs.getString("emp_name") + "|"
							+ "dept:" + rs.getString("dept") + "|" + "email:" + rs.getString("email") + "|" + "phnNo:"
							+ rs.getString("phnNO") + "|" + "role: [");
					while (rs1.next()) {
						System.out.print(rs1.getString(1) + " ");

					}
					System.out.print("]");
					System.out.println();
				}

			}
		} catch (SQLException e) {
			System.out.println("error during fetching:" + e.getMessage());
		}
	}

	public void deleteEmployee(String id) {
		String query = "delete from employees where emp_id=?";

		try (Connection conn = Util.getConnection();) {
			if (!checkEmpExists(id, conn)) {
				System.out.println("Employee doesnt exist");
				return;
			}
			try (PreparedStatement stmt = conn.prepareStatement(query)) {
				stmt.setString(1, id);
				int row = stmt.executeUpdate();
				if (row != 0) {
					System.out.println("Employee deleted succesfully");
				}
				
			}
		} catch (SQLException e) {
			System.out.println("error during delete:" + e.getMessage());
		}
	}

	public void updateEmployee(Employee emp, Roles role) {
		String adminUpdate = "update employees set emp_name=?,dept=?,email=?,phnNo=? where emp_id=?";
		String empUpdate = "update employees set phnNo=?,email=? where emp_id=?";

		try (Connection conn = Util.getConnection();) {

			if (!checkEmpExists(emp.getId(), conn)) {
				System.out.println("Employee doesnt exist");
				return;
			}
			String sql = (role == Roles.ADMIN) ? adminUpdate : empUpdate;
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				if (role == Roles.ADMIN) {
					stmt.setString(1, emp.getName());
					stmt.setString(2, emp.getDept());
					stmt.setString(3, emp.getEmail());
					stmt.setString(4, emp.getPhnNo());
					stmt.setString(5, emp.getId());
					int row = stmt.executeUpdate();
					if (row != 0) {
						System.out.println("updated succesfully");
						return;
					}

				} else {
					stmt.setString(1, emp.getPhnNo());
					stmt.setString(2, emp.getEmail());
					stmt.setString(3, emp.getId());
					int row = stmt.executeUpdate();
					if (row != 0) {
						System.out.println("updated succesfully");
						return;
					}
				}
				
			}
		} catch (SQLException e) {
			System.out.println("error during update:" + e.getMessage());
		}
	}

	public boolean resetPassword(String id, String password) {
		String query = "update emp_auth set password=? where emp_id=?";
		try (Connection conn = Util.getConnection();) {
			
			if (!checkEmpExists(id, conn)) {
				return false;
			}
			try (PreparedStatement stmt = conn.prepareStatement(query)) {
				stmt.setString(1, password);
				stmt.setString(2, id);
				int row = stmt.executeUpdate();
				if (row != 0) {
					return true;
				}
				
			}
		} catch (SQLException e) {
			System.out.println("error during updating password:" + e.getMessage());
		}
		return false;
	}

	public boolean changePassword(String id, String password) {

		return resetPassword(id, password);
	}

	public void assignRole(String id, Roles role) {
		String query = "insert into emp_roles (emp_id,roles) values (?,?)";
		try (Connection conn = Util.getConnection();) {
			
			if (!checkEmpExists(id, conn)) {
				System.out.println("Employee doesnt exist");
				return;
			}
			if (checkRoleExists(id, role, conn)) {
				System.out.println("Role already exist");
				return;
			}
			try (PreparedStatement stmt = conn.prepareStatement(query)) {
				stmt.setString(1, id);
				stmt.setObject(2, role.name(), java.sql.Types.OTHER);
				int row = stmt.executeUpdate();
				if (row != 0) {
					System.out.println("role is assigned succesfully");
					return;
				}
				
			}
		} catch (SQLException e) {
			System.out.println("error during assign role:" + e.getMessage());
		}
	}

	public void revokeRole(String id, Roles role) {
		String query = "delete  from emp_roles where emp_id=? and roles=?";
		try (Connection conn = Util.getConnection();) {
			
			if (!checkEmpExists(id, conn)) {
				System.out.println("Employee doesnt exist");
				return;
			}
			if (!checkRoleExists(id, role, conn)) {
				System.out.println("Role not exist");
				return;
			}
			try (PreparedStatement stmt = conn.prepareStatement(query)) {
				stmt.setString(1, id);
				stmt.setObject(2, role.name(), java.sql.Types.OTHER);
				int row = stmt.executeUpdate();
				if (row != 0) {
					System.out.println("role is revoked succesfully");
					return;
				}
				
			}
		} catch (SQLException e) {
			System.out.println("error during revoke role:" + e.getMessage());
		}
	}
}
