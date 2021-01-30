package pl.indexpz.db_manager_2.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {
    private static final String URL = "jdbc:mysql://localhost:3306";
    private static final String USER = "root";
    private static final String PASSWORD = "coderslab";
    private static final String PARAMS = "?characterEncoding=utf8";

    public static Connection conn(String dbName) throws SQLException {
        String sql = URL + (dbName != null ? "/" + dbName : "")+PARAMS;
        Connection connection = DriverManager.getConnection(sql, USER, PASSWORD);
        return connection;
    }
}
