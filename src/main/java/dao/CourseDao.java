package dao;

import idao.ICourseDao;
import models.Course;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class CourseDao extends Dao<Course, Integer> implements ICourseDao {
    @Override
    public Course find(Integer id) {
        return em.find(Course.class, id);
    }

    @Override
    public List<Course> listCoursesByTeacherLastName(String lastName) {
        TypedQuery<Course> query = em.createQuery(
                "SELECT c FROM Course c WHERE c.teacher.lastName = :lastName", Course.class);
        query.setParameter("lastName", lastName);
        return query.getResultList();
    }
}
