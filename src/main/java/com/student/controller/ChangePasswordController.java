package com.student.controller;

import java.io.IOException;

import org.mindrot.jbcrypt.BCrypt;

import com.student.dao.UserDAO;
import com.student.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/change-password")
public class ChangePasswordController extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Show change password form
        request.getRequestDispatcher("/views/change-password.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        if (session == null || user == null) {
            response.sendRedirect("login");
            return;
        }

        String currentPassword = request.getParameter("currentPassword");

        if (!BCrypt.checkpw(currentPassword, user.getPassword())) {
            request.setAttribute("errorCurrent", "Wrong password");
            request.getRequestDispatcher("/views/change-password.jsp").forward(request, response);
            ;
            return;
        }

        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        if (newPassword.isBlank() || newPassword == null) {
            request.setAttribute("errorNew", "Invalid password");
            request.getRequestDispatcher("/views/change-password.jsp").forward(request, response);
            ;
            return;
        }

        if (confirmPassword == null || !confirmPassword.equals(newPassword)) {

            request.setAttribute("errorConfirm", "Invalid");
            request.getRequestDispatcher("/views/change-password.jsp").forward(request, response);
            ;
            return;
        }

        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        if (userDAO.updatePassword(user.getId(), hashedPassword)) {
            response.sendRedirect("logout");
            return;
        } else {
            request.setAttribute("error", "Failed to update password");
            request.getRequestDispatcher("/views/change-password.jsp");
            return;
        }
    }
}