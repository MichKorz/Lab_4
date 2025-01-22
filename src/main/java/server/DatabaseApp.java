package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseApp {

    // MySQL connection details
    private static final String URL = "jdbc:mysql://localhost:3306/chinese_checkers"; // Replace with your DB URL
    private static final String USER = "root"; // Replace with your MySQL username
    private static final String PASSWORD = "3213"; // Replace with your MySQL password


    public void insertMove(int game_id, String move)
    {
        String query = "INSERT INTO moves (game_id, move) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query))
        {

            stmt.setInt(1, game_id);
            stmt.setString(2, move);
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new move was inserted successfully!");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public ArrayList<String> readMoves(int game_id)
    {
        ArrayList<String> moves = new ArrayList<>();

        String query = "SELECT * FROM moves WHERE game_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query))
        {
            stmt.setInt(1, game_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
            {
                System.out.println("Something found");
                String move = rs.getString("move");
                moves.add(move);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return moves;
    }

    public void insertGame(String name)
    {
        String query = "INSERT INTO games (name) VALUES (?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query))
        {

            stmt.setString(1, name);
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0)
            {
                System.out.println("A new game was inserted successfully!");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public int getHighestId()
    {
        String query = "SELECT * FROM games ORDER BY id DESC LIMIT 1";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery())
        {

            while (rs.next())
            {
                return rs.getInt("id");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    public int findGameId(String name)
    {
        int id = -1;
        String query = "SELECT * FROM games WHERE name = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query))
        {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                id =  rs.getInt("id");
                System.out.println(id);
                return id;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return id;
    }
}
