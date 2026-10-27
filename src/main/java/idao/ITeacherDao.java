package idao;

import models.Teacher;

public interface ITeacherDao {
    Teacher find(Integer id);
}