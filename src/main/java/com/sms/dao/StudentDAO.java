package com.sms.dao;

import com.sms.model.Student;
import com.sms.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for the Student entity.
 *
 * Every query is a PreparedStatement, which (a) protects against SQL
 * injection and (b) lets MySQL cache/reuse the query execution plan
 * instead of re-parsing raw SQL on every call.
 */
public class StudentDAO {

    // ---------- CREATE ----------

    public boolean addStudent(Student student) {
        String sql = "INSERT INTO students (name, age, course, email, phone) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, student.getName());
            ps.setInt(2, student.getAge());
            ps.setString(3, student.getCourse());
            ps.setString(4, student.getEmail());
            ps.setString(5, student.getPhone());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error adding student: " + e.getMessage());
            return false;
        }
    }

    // ---------- READ ----------

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        // Explicit column list instead of SELECT * avoids pulling columns
        // the app doesn't need and stays correct if columns are reordered.
        String sql = "SELECT id, name, age, course, email, phone FROM students ORDER BY id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                students.add(mapRow(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching students: " + e.getMessage());
        }
        return students;
    }

    public Student getStudentById(int id) {
        // Lookup by primary key -> uses the clustered index, O(log n).
        String sql = "SELECT id, name, age, course, email, phone FROM students WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error fetching student: " + e.getMessage());
        }
        return null;
    }

    public List<Student> searchStudentsByName(String keyword) {
        List<Student> students = new ArrayList<>();
        // idx_name (see sql/student_db.sql) speeds up this lookup on large tables.
        String sql = "SELECT id, name, age, course, email, phone FROM students WHERE name LIKE ? ORDER BY name";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    students.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error searching students: " + e.getMessage());
        }
        return students;
    }

    // ---------- UPDATE ----------

    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET name = ?, age = ?, course = ?, email = ?, phone = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, student.getName());
            ps.setInt(2, student.getAge());
            ps.setString(3, student.getCourse());
            ps.setString(4, student.getEmail());
            ps.setString(5, student.getPhone());
            ps.setInt(6, student.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating student: " + e.getMessage());
            return false;
        }
    }

    // ---------- DELETE ----------

    public boolean deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting student: " + e.getMessage());
            return false;
        }
    }

    // ---------- helpers ----------

    private Student mapRow(ResultSet rs) throws SQLException {
        return new Student(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("age"),
                rs.getString("course"),
                rs.getString("email"),
                rs.getString("phone")
        );
    }
}
