package dao;

import model.Post;
import utils.DatabaseConnection;
import java.io.Serializable; // Thêm import này
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PostDAO implements Serializable { // Thêm implements Serializable
    public void insertPost(Post post) {
        String sql = "INSERT INTO posts (title, body, user_id, status, created_at) VALUES (?, ?, ?, ?, NOW())";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, post.getTitle());
            stmt.setString(2, post.getBody());
            stmt.setInt(3, post.getUserId());
            stmt.setString(4, post.getStatus());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}