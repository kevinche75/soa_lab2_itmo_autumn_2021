package ru.itmo.DAO;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.itmo.entity.Discipline;
import ru.itmo.entity.LabWork;
import ru.itmo.utils.DisciplineResult;
import ru.itmo.utils.HibernateUtil;
import ru.itmo.utils.LabWorksResult;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;
import java.util.Optional;

public class DisciplineDAO {

    private static final String backFirst = "http://localhost:50432/lab2";
    private static final String LIMIT_PARAM = "limit";

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

    public LabWorksResult getDisciplineLabWorks(Long id) throws EntityNotFoundException{
        Transaction transaction = null;
        LabWorksResult labWorksResult = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            Discipline discipline = session.find(Discipline.class, id);
            if (discipline != null) {
                labWorksResult = new LabWorksResult(discipline.getLabWorks().size(), discipline.getLabWorks());
            } else {
                throw new EntityNotFoundException(String.format("Discipline with id %s wasn't found", id));
            }
            transaction.commit();
        } catch (Exception e){
            if (transaction != null)
                transaction.rollback();
            e.printStackTrace();
            throw e;
        }
        return labWorksResult;
    }

    public void addLabWork(Long id, Long labWorkId) throws EntityNotFoundException{
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Discipline discipline = session.find(Discipline.class, id);
            if (discipline == null) {
                throw new EntityNotFoundException(String.format("Discipline with id %s wasn't found", id));
            }
            LabWork labWork = session.find(LabWork.class, labWorkId);
            if (labWork == null) {
                throw new EntityNotFoundException(String.format("LabWork with id %s wasn't found", labWorkId));
            }
            Discipline labDiscipline = session.createNamedQuery("getLabWorkDiscipline", Discipline.class).setParameter("labWorkId", labWorkId).getSingleResult();
            if (labDiscipline != null) {
                throw new EntityExistsException(String.format("LabWork has already belonged to %s", labDiscipline.getName()));
            }
            discipline.getLabWorks().add(labWork);
            session.update(discipline);
            transaction.commit();
        } catch (EntityExistsException e){
            throw e;
        } catch (Exception e){
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public boolean deleteLabWorkFromDiscipline(Long id, Long labWorkId){
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            LabWork labWork = session.find(LabWork.class, labWorkId);
            Discipline searchDiscipline = session.find(Discipline.class, id);

            if (searchDiscipline == null){
                throw new EntityNotFoundException(String.format("Discipline with id %s wasn't found", id));
            }

            if (labWork != null) {
                Discipline discipline = session.createNamedQuery("getLabWorkDiscipline", Discipline.class).setParameter("labWorkId", labWorkId).getSingleResult();

                if (discipline != null && discipline.getId().equals(id)){
                    discipline.getLabWorks().remove(labWork);
                } else {
                    throw new EntityNotFoundException(String.format("Discipline %s has no labWork with id %s", searchDiscipline.getName(), labWorkId));
                }
            } else {
                throw new EntityNotFoundException(String.format("LabWork with id %s wasn't found", labWorkId));
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
        return true;
    }

    public WebTarget getTarget() {
        URI uri = UriBuilder.fromUri(backFirst).build();
        Client client = ClientBuilder.newClient();
        return client.target(uri).path("api").path("labworks").queryParam(LIMIT_PARAM, Integer.MAX_VALUE);
    }
}