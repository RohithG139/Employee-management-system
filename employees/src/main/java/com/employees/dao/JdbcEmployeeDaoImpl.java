package com.employees.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
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

		try (Connection conn = DbConnection.getConnection()) {
			try (PreparedStatement ps = conn.prepareStatement(authQuery);
					PreparedStatement ps2 = conn.prepareStatement(roleQuery)) {
				ps.setString(1, id);
				ResultSet rs = ps.executeQuery();

				if (!rs.next()) {
					System.out.println("Invalid id");
					return new LoginResult(false, null, null);
				}

				String dbPass = rs.getString("password");

				if (!dbPass.equals(Util.hashPassword(password))) {
					System.out.println("Invalid password");
					return new LoginResult(false, null, null);
				}

				Set<Roles> roles = new HashSet<>();

				ps2.setString(1, id);
				ResultSet rs2 = ps2.executeQuery();

				while (rs2.next()) {
					roles.add(Roles.valueOf(rs2.getString("roles")));
				}

				return new LoginResult(true, id, roles);

			} catch (SQLException e) {
				System.out.println("Statement error:" + e.getMessage());
			}
		} catch (SQLException e1) {

			System.out.println("connection error:" + e1.getMessage());
		}

		return new LoginResult(false, null, null);
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
			System.out.println("statement error" + e.getMessage());
		}
		return false;
	}

	private boolean checkEmp(String id, Connection conn) {
		String query = "select emp_id from employees where emp_id=?";
		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setString(1, id);
			ResultSet rs = stmt.executeQuery();
			if (!rs.next()) {
				return false;
			}
		} catch (SQLException e) {
			System.out.println("Statement error:" + e.getMessage());
		}
		return true;
	}

	public void addEmployee(Employee emp) {

		String insertEmployees = "insert into employees (emp_name,dept,email,phnNo) values(?,?,?,?)";
		String insertRoles = "insert into emp_roles(emp_id,roles) values(?,?)";
		String insertPassword = "insert into emp_auth(emp_id,password) values(?,?)";
		try (Connection conn = DbConnection.getConnection()) {
			conn.setAutoCommit(false);
			try (PreparedStatement pstmt = conn.prepareStatement(insertEmployees, new String[] { "emp_id" });
					PreparedStatement pstmt1 = conn.prepareStatement(insertPassword);
					PreparedStatement pstmt2 = conn.prepareStatement(insertRoles)) {
				pstmt.setString(1, emp.getName());
				pstmt.setString(2, emp.getDept());
				pstmt.setString(3, emp.getEmail());
				pstmt.setString(4, emp.getPhnNo());
				pstmt.executeUpdate();

				ResultSet rs = pstmt.getGeneratedKeys();
				if (!rs.next()) {
					System.out.println("No id is generated");
					return;

				}
				String generatedId = rs.getString(1);

				pstmt1.setString(1, generatedId);
				pstmt1.setString(2, emp.getPassword());
				pstmt1.executeUpdate();

				for (Roles role : emp.getRole()) {
					pstmt2.setString(1, generatedId);
					pstmt2.setObject(2, role.name(), java.sql.Types.OTHER);
					pstmt2.addBatch();
				}

				pstmt2.executeBatch();

				conn.commit();
				System.out.println("Employee added succesfully");
			} catch (SQLException e) {
				System.out.println("Statement error:" + e.getMessage());
			}
		} catch (SQLException e1) {

			System.out.println("connection error:" + e1.getMessage());
		}
	}

	public void fetchEmployee() {
		String fetchQuery = "select e1.emp_id, e1.emp_name, e1.dept, e1.email, e1.phnNo, "
				+ "string_agg(e2.roles::text,',' ) as all_roles "
				+ "from employees e1 join emp_roles e2 on e1.emp_id = e2.emp_id "
				+ "group by e1.emp_id order by e1.emp_id";

		try (Connection conn = DbConnection.getConnection()) {
			try (Statement stmt = conn.createStatement()) {
				ResultSet rs = stmt.executeQuery(fetchQuery);
				if (!rs.next()) {
					System.out.println("No Employees exist");
					return;
				}
				do {
					Set<String> rolesList = new HashSet<>();
					String roles = rs.getString("all_roles");
					rolesList.addAll(Arrays.asList(roles.split(",")));
					System.out.print("id:" + rs.getString("emp_id") + " | " + "name:" + rs.getString("emp_name") + "|"
							+ "dept:" + rs.getString("dept") + "|" + "email:" + rs.getString("email") + "|" + "phnNo:"
							+ rs.getString("phnNO") + "|" + "roles:" + rolesList);
					System.out.println();
				} while (rs.next());

			} catch (SQLException e) {
				System.out.println("Statement error:" + e.getMessage());
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void fetchEmployeeById(String id) {
		String query = "select e1.emp_id, e1.emp_name, e1.dept, e1.email, e1.phnNo, "
				+ "string_agg(e2.roles::text,',' ) as all_roles "
				+ "from employees e1 join emp_roles e2 on e1.emp_id = e2.emp_id where e1.emp_id=? "
				+ "group by e1.emp_id";
		try (Connection conn = DbConnection.getConnection()) {
			if (!checkEmp(id, conn)) {
				System.out.println("Employee doesnt exist");
				return;
			}
			try (PreparedStatement stmt = conn.prepareStatement(query)) {
				stmt.setString(1, id);
				ResultSet rs = stmt.executeQuery();
				if (!rs.next()) {
					System.out.println("No employee exist with id");
					return;
				}
				Set<String> rolesList = new HashSet<>();
				String roles = rs.getString("all_roles");
				rolesList.addAll(Arrays.asList(roles.split(",")));
				System.out.print("id:" + rs.getString("emp_id") + " | " + "name:" + rs.getString("emp_name") + "|"
						+ "dept:" + rs.getString("dept") + "|" + "email:" + rs.getString("email") + "|" + "phnNo:"
						+ rs.getString("phnNO") + "|" + "roles:" + rolesList);
				System.out.println();

			} catch (SQLException e) {
				System.out.println("Statement error:" + e.getMessage());
			}
		} catch (SQLException e) {
			System.out.println("Connection error:" + e.getMessage());
		}

	}

	public void deleteEmployee(String id) {
		String query = "delete from employees where emp_id=?";

		try (Connection conn = DbConnection.getConnection()) {
			if (!checkEmp(id, conn)) {
				System.out.println("Employee doesnt exist");
				return;
			}
			try (PreparedStatement stmt = conn.prepareStatement(query)) {
				stmt.setString(1, id);
				int row = stmt.executeUpdate();
				if (row != 0) {
					System.out.println("Employee deleted succesfully");
				}

			} catch (SQLException e) {
				System.out.println("Statement error:" + e.getMessage());
			}
		} catch (SQLException e) {
			System.out.println("Connection error:" + e.getMessage());
		}
	}

	public void updateEmployee(Employee emp, Roles role) {
		String adminUpdate = "update employees set emp_name=?,dept=?,email=?,phnNo=? where emp_id=?";
		String empUpdate = "update employees set phnNo=? where emp_id=?";

		try (Connection conn = DbConnection.getConnection()) {
			if (!checkEmp(emp.getId(), conn)) {
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

					stmt.setString(2, emp.getId());
					int row = stmt.executeUpdate();
					if (row != 0) {
						System.out.println("updated succesfully");
						return;
					}
				}
			} catch (SQLException e) {
				System.out.println("Statement error:" + e.getMessage());
			}
		} catch (SQLException e) {
			System.out.println("Connection error:" + e.getMessage());
		}

	}

	public void resetPassword(String id, String password) {
		String query = "update emp_auth set password=? where emp_id=?";
		try (Connection conn = DbConnection.getConnection()) {
			if (!checkEmp(id, conn)) {
				System.out.println("Employee doesnt exist");
				return;
			}
			try (PreparedStatement stmt = conn.prepareStatement(query)) {
				stmt.setString(1, password);
				stmt.setString(2, id);
				int row = stmt.executeUpdate();
				if (row != 0) {
					System.out.println("reset password succesfully");
					return;
				}
			} catch (SQLException e) {
				System.out.println("Statement error:" + e.getMessage());
			}
		} catch (SQLException e) {
			System.out.println("Connection error:" + e.getMessage());
		}

	}

	public void changePassword(String id, String password) {
		String query = "update emp_auth set password=? where emp_id=?";
		try (Connection conn = DbConnection.getConnection()) {
			if (!checkEmp(id, conn)) {
				System.out.println("Employee doesnt exist");
				return;
			}
			try (PreparedStatement stmt = conn.prepareStatement(query)) {
				stmt.setString(1, password);
				stmt.setString(2, id);
				int row = stmt.executeUpdate();
				if (row != 0) {
					System.out.println("password  changed succesfully");
					return;
				}
			} catch (SQLException e) {
				System.out.println("Statement error:" + e.getMessage());
			}
		} catch (SQLException e) {
			System.out.println("Connection error:" + e.getMessage());
		}

	}

	public void assignRole(String id, Roles role) {
		String query = "insert into emp_roles (emp_id,roles) values (?,?)";
		try (Connection conn = DbConnection.getConnection()) {
			if (!checkEmp(id, conn)) {
				System.out.println("Employee doesnt exist");
				return;
			}
			if(checkRoleExists(id,role,conn)) {
				System.out.println("Role already exist");
				return;
			}
			try (PreparedStatement stmt = conn.prepareStatement(query)) {
				stmt.setString(1, id);
				stmt.setObject(2, role.name(), java.sql.Types.OTHER);
				int row=stmt.executeUpdate();
				if (row != 0) {
					System.out.println("role is assigned succesfully");
					return;
				}
				
			} catch (SQLException e) {
				System.out.println("Statement error:" + e.getMessage());
			}
		} catch (SQLException e) {
			System.out.println("Connection error:" + e.getMessage());
		}

	}

	public void revokeRole(String id, Roles role) {
		String query="delete  from emp_roles where emp_id=? and roles=?";
		try (Connection conn = DbConnection.getConnection()) {
			if (!checkEmp(id, conn)) {
				System.out.println("Employee doesnt exist");
				return;
			}
			if(!checkRoleExists(id,role,conn)) {
				System.out.println("Role not exist");
				return;
			}
			try (PreparedStatement stmt = conn.prepareStatement(query)) {
				stmt.setString(1, id);
				stmt.setObject(2, role.name(), java.sql.Types.OTHER);
				int row=stmt.executeUpdate();
				if (row != 0) {
					System.out.println("role is revoked succesfully");
					return;
				}
				
			} catch (SQLException e) {
				System.out.println("Statement error:" + e.getMessage());
			}
		} catch (SQLException e) {
			System.out.println("Connection error:" + e.getMessage());
		}
	}
}
