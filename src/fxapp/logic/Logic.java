package fxapp.logic;

import fxapp.db.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Logic {
    private static Logic instance;
    private Connection connection;
    private Logic(){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mainAcad", "Lemetriss", "123");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public static Logic getInstance(){
        if(instance == null) {
            instance = new Logic();
        }
        return instance;
    }

    public List<Student> findAllStudents(){
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("SELECT * FROM mainacad.student;")) {
            List<Student> students = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String lastName = rs.getString("lastName");
                int age = rs.getInt("age");
                students.add(new Student(id, name, lastName, age));
            }
            return students;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public void addStudent(String name, String lastName, int age) {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO student (name, lastName, age) VALUES (?,?,?)")){
            ps.setString(1, name);
            ps.setString(2, lastName);
            ps.setInt(3, age);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteStudent(Student student) {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM student where id = ?")){
            ps.setInt(1, student.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void updateStudent(Student student) {
        try (PreparedStatement ps = connection.prepareStatement("UPDATE student SET name = ?, lastName = ?, age = ? WHERE id = ?")){
            ps.setInt(4, student.getId());
            ps.setInt(3, student.getAge());
            ps.setString(1, student.getName());
            ps.setString(2, student.getLastName());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}