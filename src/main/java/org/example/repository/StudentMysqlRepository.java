package org.example.repository;

import org.example.domain.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentMysqlRepository implements StudentRepository {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/tetiana";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "*****";
    private static final String SELECT_FROM_STUDENTS = "SELECT * FROM students";
    private static final String SELECT_BY_ID = "SELECT * FROM students WHERE id = ?";
    private static final String INSERT = "INSERT INTO students (name, age, group_id) VALUES (?,?,?)";


    @Override
    public void save(Student student) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(INSERT);
            ps.setString(1, student.getName());
            ps.setInt(2, student.getAge());
            ps.setInt(3, student.getGroupId());
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            try {
                assert conn != null;
                conn.rollback();
            } catch (SQLException ex) {

            }
            e.printStackTrace();
        } finally {
            try {
                assert conn != null;
                conn.close();
                assert ps != null;
                ps.close();
            } catch (SQLException e) {

            }
        }
    }

    @Override
    public List<Student> findAll() {
        List<Student> result = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_FROM_STUDENTS);) {
            while (rs.next()) {
                Student student = Student.builder()
                        .id(rs.getInt("id"))
                        .age(rs.getInt("age"))
                        .groupId(rs.getInt("group_id"))
                        .name(rs.getString("name"))
                        .build();
                result.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Student findById(int id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);) {
            ps = conn.prepareStatement(SELECT_BY_ID);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            rs.next();
            return Student.builder()
                    .id(rs.getInt("id"))
                    .age(rs.getInt("age"))
                    .groupId(rs.getInt("group_id"))
                    .name(rs.getString("name"))
                    .build();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                ps.close();
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }
}
