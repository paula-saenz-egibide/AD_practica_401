package dao;

import idao.IStudentDao;
import models.Student;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class StudentDao extends Dao<Student, Integer> implements IStudentDao {
    @Override
    public Student find(Integer id) {
        return em.find(Student.class, id);
    }

    @Override
    public List<Student> listStudentsByCourseId(Integer courseId) {
        TypedQuery<Student> query = em.createQuery(
                "SELECT s FROM Student s JOIN s.courses c WHERE c.id = :courseId", Student.class);
        query.setParameter("courseId", courseId);
        return query.getResultList();
    }
}