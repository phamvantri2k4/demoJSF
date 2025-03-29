package controller;

import dao.UserDAO;
import model.User;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;

@Named("userController")
@SessionScoped
public class UserController implements Serializable {
    private String searchTerm;
    private User foundUser;
    private int postCount;

    @Inject
    private UserDAO userDAO;

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public User getFoundUser() {
        return foundUser;
    }

    public void setFoundUser(User foundUser) {
        this.foundUser = foundUser;
    }

    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    public String search() {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Vui lòng nhập tên người dùng để tìm kiếm!", null));
            foundUser = null;
            postCount = 0;
            return null;
        }

        foundUser = userDAO.searchUserByUsername(searchTerm);
        if (foundUser == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Không tìm thấy người dùng!", null));
            postCount = 0;
        } else {
            postCount = userDAO.countPostsByUserId(foundUser.getId());
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Người dùng " + foundUser.getUsername() + " có " + postCount + " bài viết", null));
        }
        return null;
    }

    public String goToDashboard() {
        return "dashboard.xhtml?faces-redirect=true";
    }
}