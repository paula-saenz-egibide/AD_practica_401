package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.function.Supplier;

public abstract class Dao<T, K> implements AutoCloseable {
    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY =
            Persistence.createEntityManagerFactory("default");

    protected final EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();

    public EntityManager getEntityManager() {
        return this.em;
    }

    public abstract T find(K id);

    public T create(T t) {
        return inTransaction(() -> {
            em.persist(t);
            return t;
        });
    }

    public T update(T t) {
        return inTransaction(() -> em.merge(t));
    }

    public void delete(T t) {
        inTransaction(() -> {
            em.remove(em.contains(t) ? t : em.merge(t));
            return null;
        });
    }

    private <R> R inTransaction(Supplier<R> operation) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            R result = operation.get();
            transaction.commit();
            return result;
        } catch (RuntimeException | Error exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;
        }
    }

    @Override
    public void close() {
        if (em.isOpen()) {
            em.close();
        }
    }

    public static void closeEntityManagerFactory() {
        if (ENTITY_MANAGER_FACTORY.isOpen()) {
            ENTITY_MANAGER_FACTORY.close();
        }
    }
}
