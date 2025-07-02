package com.example.qbankapi.dao;

import com.example.qbankapi.entity.Teacher;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class TeacherDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<Teacher> findById(Long id) {
        List<Teacher> teacherList = entityManager.createQuery("SELECT u FROM Teacher u WHERE u.id = :id", Teacher.class).setParameter("id", id).getResultList();
        return teacherList.isEmpty() ? Optional.empty() : Optional.of(teacherList.getFirst());
    }

    public void update(Teacher teacher) {
        entityManager.merge(teacher);
    }

}
