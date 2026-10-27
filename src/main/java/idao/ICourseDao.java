package idao;

import models.Course;
import java.util.List;

public interface ICourseDao {
    Course find(Integer id);
    List<Course> listCoursesByTeacherLastName(String lastName);
}