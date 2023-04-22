package projects.dao;

import projects.exception.DbException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection
{
    private static String HOST = "localhost";
    private static String PASSWORD = "";
    private static int PORT = 3306;
    private static String SCHEMA = "projects";
    private static String USER = "projects";
    public static java.sql.Connection getConnection()
    {
        String uri = String.format("jdbc:mysql://" + HOST + ":" + PORT + "/" + SCHEMA + "?user=" + USER + "&password=" + PASSWORD);
        try
        {
            Connection connection = DriverManager.getConnection(uri);
            System.out.println("Connection successful.");
            return connection;
        }
        catch (SQLException exception)
        {
            System.out.println("Connection unsuccessful.");
            throw new DbException("failed to acquire connection at " + uri);
        }
    }
}
