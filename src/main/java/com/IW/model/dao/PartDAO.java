package com.IW.model.dao;

import com.IW.interfaces.SQL.IPartDAO;
import com.IW.model.objects.Part;
import com.IW.utils.PersistenceUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class PartDAO extends Part implements IPartDAO {

    private static final Logger logger = LoggerFactory.getLogger(PartDAO.class);

    public PartDAO(long id) {
        Part p = null;
        EntityManager em = PersistenceUnit.createEM();
        em.getTransaction().begin();
        try {
            p = em.find(Part.class, id);
            if (p != null) {
                this.setId(p.getId());
                this.setNPart(p.getNPart());
                this.setChapters(p.getChapters());
                this.setBook(p.getBook());
            }
        } catch (NoResultException e) {
            logger.error("Parte con id: " + id + "\nNo encontrado en la base de datos");
        }
        PersistenceUnit.closeEM();
    }

    public PartDAO() {
        super();
    }

    @Override
    public void persist() {
        EntityManager em = PersistenceUnit.createEM();
        em.getTransaction().begin();
        Part p = em.merge(this);
        em.persist(p);
        if (getId() == -1) {
            em.flush();
            setId(p.getId());
        }
        em.getTransaction().commit();
        PersistenceUnit.closeEM();
    }

    @Override
    public void remove() {
        EntityManager em = PersistenceUnit.createEM();
        em.getTransaction().begin();
        Part p = em.merge(this);
        em.remove(p);
        em.getTransaction().commit();
        PersistenceUnit.closeEM();
    }
}
