package dao;

import models.CourseMaterial;

public class CourseMaterialDao extends Dao<CourseMaterial, Integer> {
    @Override
    public CourseMaterial find(Integer id) {
        return em.find(CourseMaterial.class, id);
    }
}