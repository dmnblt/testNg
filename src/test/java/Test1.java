import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Test1 {
    Connection connection;

    @BeforeTest
    public void setup() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/testng", "root", "");
    }

    @AfterTest
    public void quit() throws SQLException {
        connection.close();
    }

    @Test
    public void testInsertToDb() throws SQLException {
        String sql = "INSERT INTO user (username, password) VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);

        for(int i=0;i<1000; i++) {
            statement.setString(1, RandomStringUtils.random(5));
            statement.setString(2, RandomStringUtils.random(7));
            statement.executeUpdate();
        }

        statement.close();
    }
}
