package com.IW.model.dao;

import com.IW.interfaces.IBeans.IAuthor;
import com.IW.interfaces.SQL.IAuthorDAO;
import com.IW.model.objects.Author;
import com.IW.utils.PersistenceUnit;
import com.IW.utils.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class AuthorDAO extends Author implements IAuthorDAO {

    private static final Logger logger = LoggerFactory.getLogger(AuthorDAO.class);

    public AuthorDAO(long id) {
        Author a = null;
        EntityManager em = PersistenceUnit.createEM();
        em.getTransaction().begin();
        try {
            a = em.find(Author.class, id);
            if (a != null) {
                this.setId(a.getId());
                this.setPassword(a.getPassword());
                this.setName(a.getName());
                this.setPhoto(a.getPhoto());
                this.setBooks(a.getBooks());
            }
        } catch (NoResultException e) {
            logger.error("Autor con id: " + id + "\nNo encontrado en la base de datos");
        }
        PersistenceUnit.closeEM();
    }

    public AuthorDAO() {
        super();
    }

    @Override
    public void persist() {
        EntityManager em = PersistenceUnit.createEM();
        em.getTransaction().begin();
        setPassword(Tools.encryptSHA256(getPassword()));
        Author a = em.merge(this);
        em.persist(a);
        if (getId() == -1) {
            em.flush();
            setId(a.getId());
        }
        em.getTransaction().commit();
        PersistenceUnit.closeEM();
    }

    @Override
    public void remove() {
        EntityManager em = PersistenceUnit.createEM();
        em.getTransaction().begin();
        Author a = em.merge(this);
        em.remove(a);
        em.getTransaction().commit();
        PersistenceUnit.closeEM();
    }

    /**
     * Este método sirve para validar que las credenciales de un usuario sean correctas, pues la contraseña está encriptada
     * en la base de datos
     * @return true si es correcta la contraseña, false si no coinciden.
     */
    @Override
    public boolean checkUser() {
        EntityManager em = PersistenceUnit.createEM();
        String encryptedUPwd = Tools.encryptSHA256(getPassword());
        String encryptedDBPwd = "";
        Author a;
        em.getTransaction().begin();
        if (getId() != -1) {
            a = new AuthorDAO(getId());
            encryptedDBPwd = a.getPassword();
            em.getTransaction().commit();
        } else {
            TypedQuery<Author> q = em.createNamedQuery("findByName", Author.class);
            q.setParameter("name", getName());
            try {
                a = q.getSingleResult();
            } catch (NoResultException e) {
                logger.error("Autor con nombre: "+getName()+ "\nNo encontrado");
                PersistenceUnit.closeEM();
                return false;
            }
            encryptedDBPwd = a.getPassword();
            setPassword(a.getPassword());
            setId(a.getId());
            setName(a.getName());
            setPhoto(a.getPhoto());
            if(a.getBooks()!=null)
                setBooks(a.getBooks());
            em.getTransaction().commit();
        }
        PersistenceUnit.closeEM();
        return encryptedUPwd.equals(encryptedDBPwd);
    }

    //UTILIDADES

    public static List<IAuthor> listAll() {
        EntityManager em = PersistenceUnit.createEM();
        em.getTransaction().begin();
        TypedQuery<Author> q = em.createNamedQuery("listAll", Author.class);
        List<IAuthor> result = new ArrayList<>(q.getResultList());
        em.getTransaction().commit();
        PersistenceUnit.closeEM();
        return result;
    }
}