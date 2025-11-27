/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.student.controller;

import com.student.dao.StudentDAO;
import com.student.model.Student;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/student")
public class StudentController extends HttpServlet {

    private StudentDAO studentDAO;

    @Override
    public void init() {
        studentDAO = new StudentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "search":
                searchStudents(request, response);
                break;
            case "new":
                showNewForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteStudent(request, response);
                break;
            default:
                listStudents(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        switch (action) {
            case "insert":
                insertStudent(request, response);
                break;
            case "update":
                updateStudent(request, response);
                break;
        }
    }

    // Validate student
    private boolean validateStudent(Student student, HttpServletRequest request) {
        boolean isValid = true;

        String studentCode = student.getStudentCode();
        String fullName = student.getFullName();
        String email = student.getEmail();
        String major = student.getMajor();

        String codePattern = "[A-Z]{2}[0-9]{3,}";
        String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";

        // Validate student code
        if (studentCode == null || studentCode.isBlank()) {
            request.setAttribute("errorCode", "Student code is required");
            isValid = false;
        } else if (!studentCode.matches(codePattern)) {
            request.setAttribute("errorCode", "Invalid format. Use 2 letters + 3+ digits (e.g., SV001)");
            isValid = false;
        }

        // Validate full name
        if (fullName == null || fullName.isBlank()) {
            request.setAttribute("errorName", "Student full name is required");
            isValid = false;
        } else if (fullName.length() < 2) {
            request.setAttribute("errorName", "Full name is at least 2 characters");
            isValid = false;
        }

        // Validate email (only if provided)
        if (email != null && !email.isBlank()) {
            if (!email.matches(emailPattern)) {
                request.setAttribute("errorEmail", "Invalid format. Example: example@example.com");
                isValid = false;
            }
        }

        // Validate major
        if (major == null || major.isBlank()) {
            request.setAttribute("errorMajor", "Student major is required");
            isValid = false;
        }

        return isValid;
    }

    // Search students
    private void searchStudents(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        if (keyword == null) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/student");
            dispatcher.forward(request, response);
        } else {
            List<Student> students = studentDAO.searchStudents(keyword);
            request.setAttribute("students", students);
            request.setAttribute("keyword", keyword);

            RequestDispatcher dispatcher = request
                    .getRequestDispatcher("/views/student-list.jsp?message=Search results for: " + keyword);
            dispatcher.forward(request, response);
        }
    }

    // List all students
    private void listStudents(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String major = request.getParameter("major");
        String sortBy = request.getParameter("sortBy");
        String order = request.getParameter("order");
        String pageParam = request.getParameter("page");
        List<Student> students = null;

        // Page variables
        int currentPage = pageParam != null ? Integer.parseInt(pageParam) : 1;
        int recordsPerPage = 10;
        int offset = (currentPage - 1) * recordsPerPage;
        int totalRecords = 0;

        // Sort order check
        if ("desc".equalsIgnoreCase(order)) {
            order = "desc";
        } else {
            order = "asc";
        }

        if (sortBy != null) {
            String[] validColumns = { "id", "student_code", "full_name", "email", "major" };
            for (int i = 0; i < validColumns.length; i++) {
                if (validColumns[i].equals(sortBy)) {
                    students = studentDAO.getStudentsFiltered(major, sortBy, order, recordsPerPage, offset);
                    totalRecords = studentDAO.getTotalStudents(major, sortBy, order);
                    break;
                }
            }
            if (students == null) {
                sortBy = "id";
                students = studentDAO.getStudentsFiltered(major, sortBy, order, recordsPerPage, offset);
                totalRecords = studentDAO.getTotalStudents(major, sortBy, order);

            }

        } else if (major != null && !major.isBlank()) {
            students = studentDAO.getStudentsFiltered(major, sortBy, order, recordsPerPage, offset);
            totalRecords = studentDAO.getTotalStudents(major, sortBy, order);

        } else {
            students = studentDAO.getAllStudents(recordsPerPage, offset);
            totalRecords = studentDAO.getTotalStudents();
        }
        System.err.println(totalRecords);

        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("sortBy", sortBy);
        request.setAttribute("order", order);
        request.setAttribute("major", major);
        request.setAttribute("students", students);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-list.jsp");
        dispatcher.forward(request, response);
    }

    // Show form for new student
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
        dispatcher.forward(request, response);
    }

    // Show form for editing student
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Student existingStudent = studentDAO.getStudentById(id);

        request.setAttribute("student", existingStudent);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
        dispatcher.forward(request, response);
    }

    // Insert new student
    private void insertStudent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String studentCode = request.getParameter("studentCode");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String major = request.getParameter("major");

        Student newStudent = new Student(studentCode, fullName, email, major);

        if (!validateStudent(newStudent, request)) {
            // Set student as attribute (to preserve entered data)
            request.setAttribute("student", newStudent);
            request.setAttribute("valid", validateStudent(newStudent, request));
            // Forward back to form
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
            dispatcher.forward(request, response);
            return; // STOP here
        }

        if (studentDAO.addStudent(newStudent)) {
            response.sendRedirect("student?action=list&message=Student added successfully");
        } else {
            response.sendRedirect("student?action=list&error=Failed to add student");
        }
    }

    // Update student
    private void updateStudent(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        String studentCode = request.getParameter("studentCode");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String major = request.getParameter("major");

        Student student = new Student(studentCode, fullName, email, major);
        student.setId(id);

        if (studentDAO.updateStudent(student)) {
            response.sendRedirect("student?action=list&message=Student updated successfully");
        } else {
            response.sendRedirect("student?action=list&error=Failed to update student");
        }
    }

    // Delete student
    private void deleteStudent(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        if (studentDAO.deleteStudent(id)) {
            response.sendRedirect("student?action=list&message=Student deleted successfully");
        } else {
            response.sendRedirect("student?action=list&error=Failed to delete student");
        }
    }
}
