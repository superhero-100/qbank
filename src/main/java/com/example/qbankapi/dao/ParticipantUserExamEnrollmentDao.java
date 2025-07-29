package com.example.qbankapi.dao;

import com.example.qbankapi.entity.ParticipantUserExamEnrollment;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.crypto.spec.OAEPParameterSpec;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ParticipantUserExamEnrollmentDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<ParticipantUserExamEnrollment> findAllByParticipantUserIdAndNowIsBetweenExamStartEnd(Long participantUserId, ZonedDateTime nowUtc) {
        return entityManager.createQuery("SELECT DISTINCT puee FROM ParticipantUserExamEnrollment puee JOIN FETCH puee.exam e JOIN FETCH e.subject s WHERE puee.participantUser.id = :participantUserId AND puee.exam.examStartDate < :nowUtc AND puee.exam.examEndDate > :nowUtc AND puee.examAttemptStatus = :examAttemptStatus", ParticipantUserExamEnrollment.class).setParameter("participantUserId", participantUserId).setParameter("nowUtc", nowUtc).setParameter("examAttemptStatus", ParticipantUserExamEnrollment.ExamAttemptStatus.NOT_ATTEMPTED).getResultList();
    }

    public void save(ParticipantUserExamEnrollment participantUserExamEnrollment) {
        entityManager.persist(participantUserExamEnrollment);
    }

    public Optional<ParticipantUserExamEnrollment> findById(Long examEnrollmentId) {
        List<ParticipantUserExamEnrollment> resultList = entityManager.createQuery("SELECT puee FROM ParticipantUserExamEnrollment puee WHERE puee.id = :examEnrollmentId", ParticipantUserExamEnrollment.class).setParameter("examEnrollmentId", examEnrollmentId).getResultList();
        return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList.getFirst());
    }

    public void update(ParticipantUserExamEnrollment participantUserExamEnrollment) {
        entityManager.merge(participantUserExamEnrollment);
    }

    public List<ParticipantUserExamEnrollment> findAllByParticipantUserIdAndNowIsAfterExamEnd(Long participantUserId,ZonedDateTime nowUtc) {
        return entityManager.createQuery("SELECT puee FROM ParticipantUserExamEnrollment puee JOIN FETCH puee.exam e JOIN FETCH e.subject s WHERE puee.participantUser.id = :participantUserId AND puee.exam.examEndDate < :nowUtc", ParticipantUserExamEnrollment.class).setParameter("participantUserId", participantUserId).setParameter("nowUtc",nowUtc).getResultList();
    }

}
