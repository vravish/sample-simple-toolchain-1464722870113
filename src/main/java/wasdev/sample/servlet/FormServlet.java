package wasdev.sample.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/FormServlet")
public class FormServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://169.44.112.10/enterpriseregistrydb";

	static final String USER = "root";
	static final String PASS = "rootpass";

	static Connection conn = null;
	static PreparedStatement searchStmt = null, addStmt = null;
	
	static void initializeSql() throws ServletException {
		try {
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			searchStmt = conn.prepareStatement("SELECT * FROM Names_And_IDs");
			addStmt = conn.prepareStatement("INSERT INTO `Names_And_IDs` (`id`, `name`) VALUES (?, ?)");
		} catch (SQLException e) {
			throw new ServletException(e);
		}
	}

	static {
		try {
			Class.forName(JDBC_DRIVER);
			initializeSql();
		} catch (ClassNotFoundException | ServletException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			ResultSet rs = searchStmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(1);
				String name = rs.getString(2);
				resp.getWriter().println("ID: " + id + ", name: " + name + "<br>");
			}
		} catch (SQLException e) {
			initializeSql();
			throw new ServletException(e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			int id = Integer.parseInt(req.getParameter("id"));
			String name = req.getParameter("name");
			
			addStmt.setInt(1, id);
			addStmt.setString(2, name);
			addStmt.executeUpdate();
			
			resp.getWriter().println("Created new record with id: " + id + " and name: " + name + "<br>");
		} catch (SQLException e) {
			initializeSql();
			throw new ServletException(e);
		}
	}

}
