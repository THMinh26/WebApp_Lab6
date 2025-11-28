/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.student.dao;

import com.student.model.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // Database configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/student_management";
    private static final String DB_USER = "root";
    // private static final String DB_USER = "minh";
    private static final String DB_PASSWORD = "minhmatmong";
    // private static final String DB_PASSWORD = "minhmatmong26";

    // Get database connection
    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found", e);
        }
    }

    // Get total students
    public int getTotalStudents() {
        int result = 0;
        String sql = "select count(*) from students";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    // Get total students
    public int getTotalStudents(String major, String sortBy, String order) {
        int result = 0;
        if (sortBy != null) {
            String sql = null;
            if (major == null || major.isBlank()) {
                sql = "select count(*) from students order by " + sortBy + " " + order;
            } else {
                sql = "select count(*) from students where major = ? order by " + sortBy + " " + order;
            }

            try (Connection conn = getConnection();
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {
                if (major != null && !major.isBlank()) {
                    pstmt.setString(1, major);
                }
                System.err.println("PSTMT: " + pstmt);

                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    result = rs.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (major != null) {
            String sql = "select count(*) from students where major = ? ORDER BY id DESC";

            try (Connection conn = getConnection();
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, major);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    result = rs.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    // Get paginated results
    public List<Student> getStudentsPaginated(int offset, int limit) {
        String sql = "SELECT * FROM students ORDER BY id DESC LIMIT ? OFFSET ?";
        List<Student> students = new ArrayList<>();
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            pstmt.setInt(2, offset);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setStudentCode(rs.getString("student_code"));
                student.setFullName(rs.getString("full_name"));
                student.setEmail(rs.getString("email"));
                student.setMajor(rs.getString("major"));
                student.setCreatedAt(rs.getTimestamp("created_at"));
                students.add(student);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    // Get student sorted
    public List<Student> getStudentsFiltered(String major, String sortBy, String order, int limit,
            int offset) {
        List<Student> students = new ArrayList<>();
        // Index for pstmt parameters
        int paramIndex = 0;

        if (sortBy != null) {
            String sql = null;
            if (major == null || major.isBlank()) {
                sql = "select * from students order by " + sortBy + " " + order + " limit ? offset ?";
            } else {
                sql = "select * from students where major = ? order by " + sortBy + " " + order + " limit ? offset ?";
            }
            try (Connection conn = getConnection();
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {
                if (major != null && !major.isBlank()) {
                    pstmt.setString(++paramIndex, major);
                }
                pstmt.setInt(++paramIndex, limit);
                pstmt.setInt(++paramIndex, offset);
                System.err.println("PSTMT: " + pstmt);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    Student student = new Student();
                    student.setId(rs.getInt("id"));
                    student.setStudentCode(rs.getString("student_code"));
                    student.setFullName(rs.getString("full_name"));
                    student.setEmail(rs.getString("email"));
                    student.setMajor(rs.getString("major"));
                    student.setCreatedAt(rs.getTimestamp("created_at"));
                    students.add(student);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (major != null) {
            String sql = "select * from students where major = ? ORDER BY id DESC limit ? offset ?";

            try (Connection conn = getConnection();
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(++paramIndex, major);
                pstmt.setInt(++paramIndex, limit);
                pstmt.setInt(++paramIndex, offset);
                System.err.println("PSTMT: " + pstmt);

                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    Student student = new Student();
                    student.setId(rs.getInt("id"));
                    student.setStudentCode(rs.getString("student_code"));
                    student.setFullName(rs.getString("full_name"));
                    student.setEmail(rs.getString("email"));
                    student.setMajor(rs.getString("major"));
                    student.setCreatedAt(rs.getTimestamp("created_at"));
                    students.add(student);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return students;
    }

    // Search students
    public List<Student> searchStudents(String keyword) {
        String sql = "SELECT * FROM students WHERE student_code LIKE ? OR full_name LIKE ? OR email LIKE ? ORDER BY id DESC";
        String searchPattern = "%" + keyword + "%";
        List<Student> students = new ArrayList<>();

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setStudentCode(rs.getString("student_code"));
                student.setFullName(rs.getString("full_name"));
                student.setEmail(rs.getString("email"));
                student.setMajor(rs.getString("major"));
                student.setCreatedAt(rs.getTimestamp("created_at"));
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    // Get all students
    public List<Student> getAllStudents(int limit, int offset) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY id DESC limit ? offset ?";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            pstmt.setInt(2, offset);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setStudentCode(rs.getString("student_code"));
                student.setFullName(rs.getString("full_name"));
                student.setEmail(rs.getString("email"));
                student.setMajor(rs.getString("major"));
                student.setCreatedAt(rs.getTimestamp("created_at"));
                students.add(student);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    // Get student by ID
    public Student getStudentById(int id) {
        String sql = "SELECT * FROM students WHERE id = ?";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setStudentCode(rs.getString("student_code"));
                student.setFullName(rs.getString("full_name"));
                student.setEmail(rs.getString("email"));
                student.setMajor(rs.getString("major"));
                student.setCreatedAt(rs.getTimestamp("created_at"));
                return student;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Add new student
    public boolean addStudent(Student student) {
        String sql = "INSERT INTO students (student_code, full_name, email, major) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getStudentCode());
            pstmt.setString(2, student.getFullName());
            pstmt.setString(3, student.getEmail());
            pstmt.setString(4, student.getMajor());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update student
    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET student_code = ?, full_name = ?, email = ?, major = ? WHERE id = ?";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getStudentCode());
            pstmt.setString(2, student.getFullName());
            pstmt.setString(3, student.getEmail());
            pstmt.setString(4, student.getMajor());
            pstmt.setInt(5, student.getId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete student
    public boolean deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
