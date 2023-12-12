import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connectionDatabase {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/reservationVol";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private static  final Connection connection;

    static {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
