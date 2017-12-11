package camt.cbsd.lab05.dao;

import camt.cbsd.lab05.entity.Student;

import java.util.List;

/**
 * Created by Dto on 3/15/2017.
 */
public interface StudentDao {
    List<Student> getStudents();
    Student findById(long id);
    Student addStudent(Student student);
    Integer size();
    Student findByUsername(String username);
    List<Student> getStudents(String searchText);
}
