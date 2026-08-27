package dao.impl;

import java.util.List;
import config.JPAConfig;
import dao.IVideoDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.Query;
import model.Video;

public class VideoDaoImpl implements IVideoDao {

    @Override
    public void insert(Video video) {
        EntityManager enma = JPAConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            enma.persist(video);
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            trans.rollback();
            throw e;
        } finally {
            enma.close();
        }
    }

    @Override
    public void update(Video video) {
        EntityManager enma = JPAConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            enma.merge(video);
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            trans.rollback();
            throw e;
        } finally {
            enma.close();
        }
    }

    @Override
    public void delete(String videoId) throws Exception {
        EntityManager enma = JPAConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            Video video = enma.find(Video.class, videoId);
            if (video != null) {
                enma.remove(video);
            } else {
                throw new Exception("Không tìm thấy Video với ID: " + videoId);
            }
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            trans.rollback();
            throw e;
        } finally {
            enma.close();
        }
    }

    @Override
    public Video findById(String videoId) {
        EntityManager enma = JPAConfig.getEntityManager();
        try {
            return enma.find(Video.class, videoId);
        } finally {
            enma.close();
        }
    }

    @Override
    public List<Video> findAll() {
        EntityManager enma = JPAConfig.getEntityManager();
        try {
            TypedQuery<Video> query = enma.createNamedQuery("Video.findAll", Video.class);
            return query.getResultList();
        } finally {
            enma.close();
        }
    }

    @Override
    public List<Video> findAll(int page, int pagesize) {
        EntityManager enma = JPAConfig.getEntityManager();
        try {
            TypedQuery<Video> query = enma.createNamedQuery("Video.findAll", Video.class);
            query.setFirstResult(page * pagesize);
            query.setMaxResults(pagesize);
            return query.getResultList();
        } finally {
            enma.close();
        }
    }

    @Override
    public List<Video> searchByTitle(String title) {
        EntityManager enma = JPAConfig.getEntityManager();
        String jpql = "SELECT v FROM Video v WHERE v.title LIKE :title";
        try {
            TypedQuery<Video> query = enma.createQuery(jpql, Video.class);
            query.setParameter("title", "%" + title + "%");
            return query.getResultList();
        } finally {
            enma.close();
        }
    }

    @Override
    public List<Video> findByCategoryId(int categoryId) {
        EntityManager enma = JPAConfig.getEntityManager();
        String jpql = "SELECT v FROM Video v WHERE v.category.categoryid = :categoryId";
        try {
            TypedQuery<Video> query = enma.createQuery(jpql, Video.class);
            query.setParameter("categoryId", categoryId);
            return query.getResultList();
        } finally {
            enma.close();
        }
    }

    @Override
    public int count() {
        EntityManager enma = JPAConfig.getEntityManager();
        String jpql = "SELECT count(v) FROM Video v";
        try {
            Query query = enma.createQuery(jpql);
            return ((Long) query.getSingleResult()).intValue();
        } finally {
            enma.close();
        }
    }
}