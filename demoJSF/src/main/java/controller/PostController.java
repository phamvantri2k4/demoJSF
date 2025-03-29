package controller;

import dao.PostDAO;
import model.Post;
import model.User;
import utils.DatabaseConnection;

import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Named("postController")
@SessionScoped
public class PostController implements Serializable {
    private Post post = new Post();

    @Inject
    private PostDAO postDAO;

    public String savePost() {
        // Lấy người dùng đang đăng nhập từ session
        User loggedInUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("loggedInUser");
        if (loggedInUser == null) {
            // Nếu không có người dùng đăng nhập, chuyển hướng về trang đăng nhập
            return "login.xhtml?faces-redirect=true";
        }

        // Gán user_id cho bài viết
        post.setUserId(loggedInUser.getId());

        // Đảm bảo status không null
        if (post.getStatus() == null || post.getStatus().trim().isEmpty()) {
            post.setStatus("PUBLISHED");
        }

        // Sử dụng PostDAO để lưu bài viết (thay vì JDBC trực tiếp)
        postDAO.insertPost(post);

        return "dashboard.xhtml?faces-redirect=true";
    }

    public List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM posts ORDER BY created_at DESC")) {
            while (rs.next()) {
                Post p = new Post();
                p.setId(rs.getInt("id"));
                p.setTitle(rs.getString("title"));
                p.setBody(rs.getString("body"));
                p.setUserId(rs.getInt("user_id"));
                p.setStatus(rs.getString("status"));
                p.setCreatedAt(rs.getTimestamp("created_at"));
                posts.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}