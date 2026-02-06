package com.employees.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.employees.enums.Roles;
import com.employees.exceptions.DataAccessException;
import com.employees.exceptions.EmployeeNotFoundException;
import com.employees.exceptions.ValidationException;
import com.employees.model.Employee;
import com.employees.model.LoginResult;
import com.employees.utils.DatabaseConfig;
import com.employees.utils.Util;

public class JdbcEmployeeDaoImpl implements EmployeeDao {

	public LoginResult validateUser(String id, String password) {

		String authQuery = "select a.password from emp_auth a join"
				+ " employees e on a.emp_id=e.emp_id where a.emp_id=? and e.is_active= true";
		String roleQuery = "select r.roles from emp_roles r join employees e on r.emp_id=e.emp_id where r.emp_id=?"
				+ " and e.is_active=true";

		try (Connection conn = DatabaseConfig.getConnection();) {
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
				return new LoginResult(id, roles);

			}
		} catch (SQLException e) {
			throw new DataAccessException(" DB error while validating user" + e);
		}

	}

	private void checkActiveEmployee(Connection conn, String id) {
		String sql = "select 1 from employees where emp_id=? and is_active=true";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, id);
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new EmployeeNotFoundException("employee not found");
				}
			}
		} catch (SQLException e) {
			throw new DataAccessException("DB error while checking employee" + e);
		}
	}

	private Employee mapToEmployee(ResultSet rs, PreparedStatement roleStmt) throws SQLException {

		String id = rs.getString("emp_id");
		String name = rs.getString("emp_name");
		String dept = rs.getString("dept");
		String email = rs.getString("email");
		String phnNo = rs.getString("phnNo");

		Set<Roles> roles = new HashSet<>();
		roleStmt.setString(1, id);
		ResultSet roleRs = roleStmt.executeQuery();
		while (roleRs.next()) {
			roles.add(Roles.valueOf(roleRs.getString("roles")));
		}

		Employee emp = new Employee(id, name, dept, email, phnNo, roles);
		return emp;
	}

	public void addEmployee(Employee emp) {

		String insertEmployees = "insert into employees (emp_name, dept, email, phnNo) values (?, ?, ?, ?)";
		String insertPassword = "insert into emp_auth (emp_id, password) values (?, ?)";
		String insertRoles = "insert into emp_roles (emp_id, roles) values (?, ?)";

		Connection conn = null;

		try {
			conn = DatabaseConfig.getConnection();
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
						throw new DataAccessException("Employee ID not generated");
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
				System.out.println("rollback error:" + ex);
			}
			throw new DataAccessException("failed to add employee" + e);

		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw new DataAccessException("error during closing connection" + e);
			}
		}
	}

	public List<Employee> fetchEmployee() {
		String fetchQuery = "select * from employees where is_active=true";
		String fetchRoles = "select r.roles from emp_roles r join employees e on r.emp_id="
				+ " e.emp_id where r.emp_id=? and  e.is_active=true";
		List<Employee> empList = new ArrayList<>();
		try (Connection conn = DatabaseConfig.getConnection();) {
			try (Statement stmt = conn.createStatement(); PreparedStatement psmt = conn.prepareStatement(fetchRoles)) {

				ResultSet rs = stmt.executeQuery(fetchQuery);

				while (rs.next()) {
					Employee emp = mapToEmployee(rs, psmt);
					empList.add(emp);
				}
			}
		} catch (SQLException e) {
			throw new DataAccessException("DB error during fetch employees" + e);
		}
		return empList;
	}

	public Employee fetchEmployeeById(String id) {
		String fetchQuery = "select * from employees where emp_id=? and is_active=true";
		String fetchRoles = "select r.roles from emp_roles r join employees e on r.emp_id="
				+ " e.emp_id where r.emp_id=? and e.is_active=true";
		try (Connection conn = DatabaseConfig.getConnection();) {
			try (PreparedStatement stmt = conn.prepareStatement(fetchQuery);
					PreparedStatement psmt = conn.prepareStatement(fetchRoles)) {
				stmt.setString(1, id);
				ResultSet rs = stmt.executeQuery();

				while (rs.next()) {
					return mapToEmployee(rs, psmt);
				}

			}
		} catch (SQLException e) {
			throw new DataAccessException("DB error during fetch employee by id" + e);
		}
		return null;
	}

	public void deleteEmployee(String id) {
		String query = "update employees set is_active=false, deleted_at=now() where" + " emp_id =? and is_active=true";

		try (Connection conn = DatabaseConfig.getConnection();) {
			try (PreparedStatement stmt = conn.prepareStatement(query)) {
				stmt.setString(1, id);
				int row = stmt.executeUpdate();
				if (row == 0) {
					throw new EmployeeNotFoundException("employee not found");
				}
			}
		} catch (SQLException e) {
			throw new DataAccessException("DB error during delete" + e);
		}

	}

	public void updateEmployee(Employee emp, Roles role) {
		String adminUpdate = "update employees set emp_name=?,dept=?,email=?,phnNo=? where emp_id=? and is_active=true";
		String empUpdate = "update employees set phnNo=?,email=? where emp_id=? and is_active=true";

		try (Connection conn = DatabaseConfig.getConnection();) {
			String sql = (role == Roles.ADMIN) ? adminUpdate : empUpdate;
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				if (role == Roles.ADMIN) {
					stmt.setString(1, emp.getName());
					stmt.setString(2, emp.getDept());
					stmt.setString(3, emp.getEmail());
					stmt.setString(4, emp.getPhnNo());
					stmt.setString(5, emp.getId());
					int row = stmt.executeUpdate();
					if (row == 0) {
						throw new EmployeeNotFoundException("employee not found");
					}

				} else {
					stmt.setString(1, emp.getPhnNo());
					stmt.setString(2, emp.getEmail());
					stmt.setString(3, emp.getId());
					int row = stmt.executeUpdate();
					if (row == 0) {
						throw new EmployeeNotFoundException("employee not found");
					}
				}

			}
		} catch (SQLException e) {
			throw new DataAccessException("DB error during update employee" + e);
		}

	}

	public void resetPassword(String id, String password) {
		String query = "update emp_auth set password = ? where emp_id = ?";
		try (Connection conn = DatabaseConfig.getConnection();) {
			checkActiveEmployee(conn, id);
			try (PreparedStatement stmt = conn.prepareStatement(query)) {
				stmt.setString(1, password);
				stmt.setString(2, id);

				stmt.executeUpdate();

			}
		} catch (SQLException e) {
			throw new DataAccessException("DB error during reset password " + e);
		}

	}

	public void changePassword(String id, String password) {

		resetPassword(id, password);
	}

	public void assignRole(String id, Roles role) {

		String query = "insert into emp_roles (emp_id, roles) values (?,?)";

		try (Connection conn = DatabaseConfig.getConnection();) {
			checkActiveEmployee(conn, id);
			try (PreparedStatement stmt = conn.prepareStatement(query)) {
				stmt.setString(1, id);
				stmt.setObject(2, role.name(), java.sql.Types.OTHER);

				stmt.executeUpdate();

			}
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new ValidationException("duplicate role assigned" + e);
		} catch (SQLException e) {
			throw new DataAccessException("DB error while assign role " + e);
		}

	}

	public void revokeRole(String id, Roles role) {
		String query = "delete from emp_roles where emp_id = ? and roles = ?";

		try (Connection conn = DatabaseConfig.getConnection();) {
			checkActiveEmployee(conn, id);
			try (PreparedStatement stmt = conn.prepareStatement(query)) {
				stmt.setString(1, id);
				stmt.setObject(2, role.name(), java.sql.Types.OTHER);

				int row = stmt.executeUpdate();
				if (row == 0) {
					throw new ValidationException("role not assigned");
				}
			}
		} catch (SQLException e) {
			throw new DataAccessException("DB error while revoke role " + e);
		}

	}

	public List<Employee> fetchInActiveEmployees() {
		String fetchQuery = "select * from employees where is_active=false";
		String fetchRoles = "select r.roles from emp_roles r join employees e on r.emp_id="
				+ " e.emp_id where r.emp_id=? and  e.is_active=false";
		List<Employee> empList = new ArrayList<>();
		try (Connection conn = DatabaseConfig.getConnection();) {
			try (Statement stmt = conn.createStatement(); PreparedStatement psmt = conn.prepareStatement(fetchRoles)) {

				ResultSet rs = stmt.executeQuery(fetchQuery);

				while (rs.next()) {
					Employee emp = mapToEmployee(rs, psmt);
					empList.add(emp);
				}
			}
		} catch (SQLException e) {
			throw new DataAccessException("DB error during fetch employees" + e);
		}
		return empList;
	}
}
