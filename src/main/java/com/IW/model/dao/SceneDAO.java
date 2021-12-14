package com.IW.model.dao;

import com.IW.interfaces.SQL.ISceneDAO;
import com.IW.model.objects.Scene;
import com.IW.utils.PersistenceUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class SceneDAO extends Scene implements ISceneDAO {

    private static final Logger logger = LoggerFactory.getLogger(SceneDAO.class);

    public SceneDAO(long id) {
        Scene s = null;
        EntityManager em = PersistenceUnit.createEM();
        em.getTransaction().begin();
        try {
            s = em.find(Scene.class, id);
            if (s != null) {
                this.setId(s.getId());
                this.setTitle(s.getTitle());
                this.setPhoto(s.getPhoto());
                this.setDescription(s.getDescription());
            }
        } catch (NoResultException e) {
            logger.error("Autor con id: " + id + "\nNo encontrado en la base de datos");
        }
        PersistenceUnit.closeEM();
    }

    public SceneDAO() {
        super();
    }

    @Override
    public void persist() {
        EntityManager em = PersistenceUnit.createEM();
        em.getTransaction().begin();
        Scene s = em.merge(this);
        em.persist(s);
        if (getId() == -1) {
            em.flush();
            setId(s.getId());
        }
        em.getTransaction().commit();
        PersistenceUnit.closeEM();
    }

    @Override
    public void remove() {
        EntityManager em = PersistenceUnit.createEM();
        em.getTransaction().begin();
        Scene s = em.merge(this);
        em.remove(s);
        em.getTransaction().commit();
        PersistenceUnit.closeEM();
    }
}
