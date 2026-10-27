package idao;

import models.Student;
import java.util.List;

public interface IStudentDao {
    Student find(Integer id);
    List<Student> listStudentsByCourseId(Integer courseId);
}