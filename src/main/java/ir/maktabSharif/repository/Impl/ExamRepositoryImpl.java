package ir.maktabSharif.repository.Impl;


import ir.maktabSharif.Exception.ExamNotFoundException;
import ir.maktabSharif.model.Exam;
import ir.maktabSharif.repository.ExamRepository;
import ir.maktabSharif.util.EntityManagerProvider;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import java.util.List;
import java.util.Optional;

public class ExamRepositoryImpl implements ExamRepository {

    private EntityManagerProvider entityManagerProvider;

    public ExamRepositoryImpl(EntityManagerProvider entityManagerProvider) {
        this.entityManagerProvider = entityManagerProvider;
    }

    @Override
    public void saveOrUpdate(Exam object) {
        if (object.getId() == null) {
            saveExam(object);
        } else {
            updateExam(object);
        }
    }

    private void updateExam(Exam object) {
        EntityManager entityManager = entityManagerProvider.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(object);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
        }
    }


    private void saveExam(Exam object) {
        EntityManager entityManager = entityManagerProvider.getEntityManager();
        EntityTransaction transaction = entityManagerProvider.getEntityManager().getTransaction();
        try {
            transaction.begin();
            entityManager.persist(object);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void delete(Integer id) throws ExamNotFoundException {
        EntityManager entityManager = entityManagerProvider.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Optional<Exam> optionalExam = findById(id);
        if (this.findById(id).isPresent()) {
            try {
                transaction.begin();
                entityManager.remove(optionalExam);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            } finally {
                entityManager.close();
            }
        } else {
            throw new ExamNotFoundException("exam not found");
        }

    }

    @Override
    public Optional<Exam> findById(Integer id) {
        EntityManager entityManager = entityManagerProvider.getEntityManager();
        Optional<Exam> optionalExam = Optional.empty();
        Exam exam = entityManager.find(Exam.class, id);
        optionalExam = Optional.of(exam);


        return optionalExam;
    }

    @Override
    public List<Exam> getAll() throws ExamNotFoundException {
        EntityManager entityManager = entityManagerProvider.getEntityManager();
        Query query = null;
        try {

            query = entityManager.createQuery("select c from Exam c");

        } catch (Exception e) {
            throw new ExamNotFoundException("exams not found");
        }
        return query.getResultList();
    }
}
