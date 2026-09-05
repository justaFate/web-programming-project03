package dao.impl;

import java.util.List;
import config.JPAConfig;
import dao.IUserDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import model.User;

public class UserDaoImpl implements IUserDao {

    @Override
    public void insert(User user) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(user);
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            trans.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void update(User user) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(user);
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            trans.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(int id) throws Exception {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            User user = em.find(User.class, id);
            if (user != null) {
                em.remove(user);
            } else {
                throw new Exception("Không tìm thấy User với ID: " + id);
            }
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            trans.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public User findById(int id) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public User findByUsername(String username) {
        EntityManager em = JPAConfig.getEntityManager();
        String jpql = "SELECT u FROM User u WHERE u.username = :username";
        try {
            TypedQuery<User> query = em.createQuery(jpql, User.class);
            query.setParameter("username", username);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<User> findAll() {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<User> query = em.createNamedQuery("User.findAll", User.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public boolean checkExistUsername(String username) {
        return findByUsername(username) != null;
    }

    @Override
    public boolean checkExistPhone(String phone, int excludeUserId) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        EntityManager em = JPAConfig.getEntityManager();
        String jpql = "SELECT u FROM User u WHERE u.phone = :phone AND u.id != :excludeId";
        try {
            TypedQuery<User> query = em.createQuery(jpql, User.class);
            query.setParameter("phone", phone);
            query.setParameter("excludeId", excludeUserId);
            List<User> list = query.getResultList();
            return !list.isEmpty();
        } finally {
            em.close();
        }
    }
}

