package com.IW.utils;

import com.IW.interfaces.IBeans.IAuthor;
import com.IW.model.objects.Character;
import com.IW.model.objects.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class PersistenceUnit {

    private static final Logger logger = LoggerFactory.getLogger(PersistenceUnit.class);
    private static EntityManagerFactory emf = null;
    private static EntityManager manager = null;

    /**
     * Solo se llama una vez para conectar con la base de datos
     */
    public static void init() {
        try {
            emf = Persistence.createEntityManagerFactory("ApplicationH2");
        } catch (PersistenceException e) {
            logger.error("Sin nombre de proveedor de persistencia para EntityManager con H2");
        }
    }

    /**
     * Este método es para crear unidades de persistencia
     *
     * @return entitymanager para la unidad de persistencia
     */
    public static EntityManager createEM() {
        if (emf == null) {
            try {
                emf = Persistence.createEntityManagerFactory("ApplicationH2");
            } catch (PersistenceException e) {
                logger.error("Sin nombre de proveedor de persistencia para EntityManager con H2");
            }
        }
        if (manager == null)
            manager = emf.createEntityManager();
        return manager;
    }

    /**
     * Cerramos la conexión con la base de datos
     */
    private static void closeEntityManagerFactory() {
        if (emf != null) {
            emf.close();
            emf = null;
        }
    }

    /**
     * Cerramos el EntityManager
     */
    public static void closeEM() {
        if (manager != null) {
            manager.close();
            manager = null;
        }
    }

    /**
     * Este método sólo se llama una vez al final del programa
     */
    public static void closeAllConnections() {
        closeEM();
        closeEntityManagerFactory();
    }

    /**
     * Copia la base de datos de H2 a MariaDB
     * @throws IllegalStateException en caso de que haya un error con la conexión a la base de datos de MariaDB
     */
    public static void copyH2toMariaDB() throws IllegalStateException {
        if (emf != null) {
            EntityManagerFactory temp = null;
            try {
                temp = Persistence.createEntityManagerFactory("ApplicationMariaDB");
            } catch (PersistenceException e) {
                throw new IllegalStateException(e.getMessage());
            }
            if (temp != null) {
                EntityManager t = temp.createEntityManager();
                t.getTransaction().begin();
                for (Author a : getAllAuthors(emf)) {
                    a = t.merge(a);
                    t.persist(a);
                }
                for (Book b : getAllBooks(emf)) {
                    List<IAuthor> a = new ArrayList<>();
                    for (IAuthor au : b.getEditors()) {
                        au = t.merge(au);
                        a.add(au);
                    }
                    b = t.merge(b);
                    b.setEditors(a);
                    t.persist(b);
                }
                for (Chapter c : getAllChapters(emf)) {
                    c = t.merge(c);
                    t.persist(c);
                }
                for (Character c : getAllCharacters(emf)) {
                    c = t.merge(c);
                    t.persist(c);
                }
                for (Part p : getAllParts(emf)) {
                    p = t.merge(p);
                    t.persist(p);
                }
                for (Scene s : getAllScenes(emf)) {
                    s = t.merge(s);
                    t.persist(s);
                }
                t.getTransaction().commit();
                t.close();
                temp.close();
            }
        }
    }
    /**
     * Copia la base de datos MariaDB a H2
     * @throws IllegalStateException en caso de que haya un error con la conexión a la base de datos de MariaDB
     */
    public static void copyMariaDBToH2() throws IllegalStateException {
        if (emf != null) {
            EntityManagerFactory temp = null;
            try {
                temp = Persistence.createEntityManagerFactory("ApplicationMariaDB");
            } catch (PersistenceException e) {
                throw new IllegalStateException(e.getMessage());
            }
            if (temp != null) {
                EntityManager t = emf.createEntityManager();
                t.getTransaction().begin();
                for (Author a : getAllAuthors(temp)) {
                    a = t.merge(a);
                    t.persist(a);
                }
                for (Book b : getAllBooks(temp)) {
                    List<IAuthor> a = new ArrayList<>();
                    for (IAuthor au : b.getEditors()) {
                        au = t.merge(au);
                        a.add(au);
                    }
                    b = t.merge(b);
                    b.setEditors(a);
                    t.persist(b);
                }
                for (Chapter c : getAllChapters(temp)) {
                    c = t.merge(c);
                    t.persist(c);
                }
                for (Character c : getAllCharacters(temp)) {
                    c = t.merge(c);
                    t.persist(c);
                }
                for (Part p : getAllParts(temp)) {
                    p = t.merge(p);
                    t.persist(p);
                }
                for (Scene s : getAllScenes(temp)) {
                    s = t.merge(s);
                    t.persist(s);
                }
                t.getTransaction().commit();
                t.close();
                temp.close();
            }
        }
    }

    private static List<Author> getAllAuthors(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        TypedQuery<Author> q = em.createNamedQuery("listAll", Author.class);
        List<Author> result = new ArrayList<>(q.getResultList());
        em.getTransaction().commit();
        closeEM();
        return result;
    }

    private static List<Book> getAllBooks(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        TypedQuery<Book> q = em.createQuery("SELECT b FROM Book b", Book.class);
        List<Book> result = new ArrayList<>(q.getResultList());
        em.getTransaction().commit();
        closeEM();
        return result;
    }

    private static List<Chapter> getAllChapters(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        TypedQuery<Chapter> q = em.createQuery("SELECT c FROM Chapter c", Chapter.class);
        List<Chapter> result = new ArrayList<>(q.getResultList());
        em.getTransaction().commit();
        closeEM();
        return result;
    }

    private static List<Character> getAllCharacters(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        TypedQuery<Character> q = em.createQuery("SELECT c FROM Character c", Character.class);
        List<Character> result = new ArrayList<>(q.getResultList());
        em.getTransaction().commit();
        closeEM();
        return result;
    }

    private static List<Part> getAllParts(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        TypedQuery<Part> q = em.createQuery("SELECT p FROM Part p", Part.class);
        List<Part> result = new ArrayList<>(q.getResultList());
        em.getTransaction().commit();
        closeEM();
        return result;
    }

    private static List<Scene> getAllScenes(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        TypedQuery<Scene> q = em.createQuery("SELECT s FROM Scene s", Scene.class);
        List<Scene> result = new ArrayList<>(q.getResultList());
        em.getTransaction().commit();
        closeEM();
        return result;
    }

}
