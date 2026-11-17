package repository;

import dao.StudentDAO;
import model.Student;

import java.util.List;

public class StudentRepository
        implements Repository<Student, Integer> {

    private final StudentDAO studentDAO = new StudentDAO();

    @Override
    public boolean save(Student student) {
        return studentDAO.addStudent(student);
    }

    @Override
    public Student findById(Integer id) {
        return studentDAO.getStudentById(id);
    }

    @Override
    public List<Student> findAll() {
        return studentDAO.getAllStudents();
    }

    @Override
    public boolean delete(Integer id) {

        if (id == null) {
            return false;
        }

        return studentDAO.deleteStudent(id);
    }
}