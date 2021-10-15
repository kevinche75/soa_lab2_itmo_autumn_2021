package ru.itmo.DAO;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.itmo.converter.FieldConverter;
import ru.itmo.entity.Coordinates;
import ru.itmo.entity.LabWork;
import ru.itmo.entity.Location;
import ru.itmo.entity.Person;
import ru.itmo.utils.HibernateUtil;
import ru.itmo.utils.LabWorkParams;
import ru.itmo.utils.LabWorksResult;
import ru.itmo.validator.ValidatorResult;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LabWorksDAO {

    private void applyPagination(TypedQuery<LabWork> labWorkQuery, LabWorkParams params){
        int startIndex = (params.getPageIdx() - 1) * params.getPageSize();
        labWorkQuery.setFirstResult(startIndex);
        labWorkQuery.setMaxResults(params.getPageSize());
    }

    public LabWorksResult getAllLabWorks(LabWorkParams params){
        List<LabWork> labWorks = null;
        LabWorksResult result = null;
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<LabWork> criteriaQuery = criteriaBuilder.createQuery(LabWork.class);
            Root<LabWork> root = criteriaQuery.from(LabWork.class);
            Join<LabWork, Coordinates> coordinatesJoin = root.join("coordinates");
            Join<LabWork, Person> personJoin = root.join("author");
            Join<Person, Location> locationJoin = personJoin.join("location");
            List<Predicate> predicates;

            predicates = params.getPredicates(criteriaBuilder, root, coordinatesJoin, personJoin, locationJoin);

            if (params.getSortField() != null){
                if (params.getSortField().startsWith("coordinates")){
                    criteriaQuery.orderBy(criteriaBuilder.asc(coordinatesJoin.get(FieldConverter.removePrefixFieldConvert(params.getSortField(), "coordinates"))));
                } else if (params.getSortField().startsWith("author")){
                    criteriaQuery.orderBy(criteriaBuilder.asc(personJoin.get(FieldConverter.removePrefixFieldConvert(params.getSortField(), "author"))));
                } else if (params.getSortField().startsWith("location")){
                    criteriaQuery.orderBy(criteriaBuilder.asc(locationJoin.get(FieldConverter.removePrefixFieldConvert(params.getSortField(), "location"))));
                } else {
                    criteriaQuery.orderBy(criteriaBuilder.asc(root.get(params.getSortField())));
                }
            }

            CriteriaQuery<LabWork> query = criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
            TypedQuery<LabWork> typedQuery = session.createQuery(query);
            applyPagination(typedQuery, params);

            labWorks = typedQuery.getResultList();

            query = criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
            typedQuery = session.createQuery(query);
            long count = typedQuery.getResultList().size();

            result = new LabWorksResult(count, labWorks);
        } catch (Exception e){
            if (transaction != null) transaction.rollback();
            throw e;
        }
        return result;
    }

    public Optional<LabWork> getLabWork(Long id){
        Transaction transaction;
        LabWork labWork = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            labWork = session.find(LabWork.class, id);
            transaction.commit();
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
        return Optional.ofNullable(labWork);
    }

    public long createLabWork(LabWork labWork){
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            Long id = (Long) session.save(labWork);
            transaction.commit();
            return id;
        } catch (Exception e){
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public void updateLabWork(LabWork labWork){
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.update(labWork);
            transaction.commit();
        } catch (Exception e){
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public boolean deleteLabWork(Long id, ValidatorResult validatorResult){
        Transaction transaction = null;
        boolean successful = false;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            LabWork lab = session.find(LabWork.class, id);
            if (lab != null) {
                session.delete(lab);
                session.flush();
                successful = true;
            } else {
                validatorResult.addMessage("No LabWork with such Id: " + id);
                validatorResult.setCode(404);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            validatorResult.addMessage("Server error, try again");
            validatorResult.setCode(500);
        }
        return successful;
    }

    public LabWork getMinName(){
        List<LabWork> labWorks;
        LabWork result = null;
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<LabWork> criteriaQuery = criteriaBuilder.createQuery(LabWork.class);
            Root<LabWork> root = criteriaQuery.from(LabWork.class);
            criteriaQuery.orderBy(criteriaBuilder.asc(root.get("name")));

            CriteriaQuery<LabWork> query = criteriaQuery.select(root);
            TypedQuery<LabWork> typedQuery = session.createQuery(query);
            typedQuery.setFirstResult(0);
            typedQuery.setMaxResults(1);

            labWorks = typedQuery.getResultList();

            if (labWorks.size() > 0){
                result = labWorks.get(0);
            }
        } catch (Exception e){
            if (transaction != null) transaction.rollback();
            throw e;
        }
        return result;
    }
    
    public Long countPQM(Long pqm){
        Transaction transaction = null;
        long count;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<LabWork> criteriaQuery = criteriaBuilder.createQuery(LabWork.class);
            Root<LabWork> root = criteriaQuery.from(LabWork.class);

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("personalQualitiesMaximum"), pqm));

            CriteriaQuery<LabWork> query = criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
            TypedQuery<LabWork> typedQuery = session.createQuery(query);
            count = typedQuery.getResultList().size();

        } catch (Exception e){
            if (transaction != null) transaction.rollback();
            throw e;
        }
        return count;
    }
}
