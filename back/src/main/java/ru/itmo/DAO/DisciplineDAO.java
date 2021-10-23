package ru.itmo.DAO;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.itmo.entity.Discipline;
import ru.itmo.entity.LabWork;
import ru.itmo.utils.DisciplineResult;
import ru.itmo.utils.HibernateUtil;

import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class DisciplineDAO {

    public DisciplineResult getAllDisciplines(){
        List<Discipline> disciplines;
        DisciplineResult result;
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Discipline> cq = cb.createQuery(Discipline.class);
            Root<Discipline> rootEntry = cq.from(Discipline.class);
            CriteriaQuery<Discipline> all = cq.select(rootEntry);

            TypedQuery<Discipline> allQuery = session.createQuery(all);
            disciplines = allQuery.getResultList();
            result = new DisciplineResult(disciplines.size(), disciplines);
        } catch (Exception e){
            if (transaction != null) transaction.rollback();
            throw e;
        }
        return result;
    }

    public Long createDiscipline(Discipline discipline){
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Long id = (Long) session.save(discipline);
            transaction.commit();
            return id;
        } catch (PersistenceException e){
            if (transaction != null)
                transaction.rollback();
            throw e;
        } catch (Exception e){
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public Optional<Discipline> getDiscipline(Long id){
        Transaction transaction;
        Discipline discipline = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            discipline = session.find(Discipline.class, id);
            transaction.commit();
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
        return Optional.ofNullable(discipline);
    }

    public void addLabWork(Long id, LabWork labWork){
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            Discipline discipline = session.find(Discipline.class, id);
            discipline.getLabWorks().add(labWork);
            labWork.setDiscipline(discipline);
            session.update(discipline);
            transaction.commit();
        } catch (Exception e){
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }


}
