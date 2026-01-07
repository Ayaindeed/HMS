package org.hsc.hospital_management_system.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.hsc.hospital_management_system.config.JpaConfig;
import java.util.List;

/**
 * Generic DAO class providing CRUD operations for all entities
 */
public abstract class GenericDAO<T> {
    private Class<T> entityClass;

    public GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected EntityManager getEntityManager() {
        return JpaConfig.getEntityManagerFactory().createEntityManager();
    }

    /**
     * Create a new entity
     */
    public T create(T entity) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(entity);
            tx.commit();
            return entity;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error creating entity: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * Read entity by ID
     */
    public T read(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(entityClass, id);
        } finally {
            em.close();
        }
    }

    /**
     * Update an entity
     */
    public T update(T entity) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T mergedEntity = em.merge(entity);
            tx.commit();
            return mergedEntity;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error updating entity: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * Delete an entity
     */
    public void delete(Long id) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T entity = em.find(entityClass, id);
            if (entity != null) {
                em.remove(entity);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error deleting entity: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * Get all entities
     */
    public List<T> findAll() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<T> query = em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Check if entity exists
     */
    public boolean exists(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(entityClass, id) != null;
        } finally {
            em.close();
        }
    }

    /**
     * Get count of entities
     */
    public long count() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e", Long.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }
}
