
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
        "jdbc:mysql://localhost:3306/planetverse_java";

    private static final String USER =
        System.getenv("DB_USER");

    private static final String PASSWORD =
        System.getenv("DB_PASSWORD");

    public static Connection getConnection() {

        try {
            if (USER == null || PASSWORD == null) {
                throw new SQLException(
                    "Database environment variables are missing."
                );
            }

            return DriverManager.getConnection(
                URL, USER, PASSWORD
            );

        } catch (SQLException e) {

            System.out.println(
                "Database connection failed: " + e.getMessage()
            );

            return null;
        }
    }
}
