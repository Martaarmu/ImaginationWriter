package com.IW.model.dao;

import com.IW.interfaces.SQL.IChapterDAO;
import com.IW.model.objects.Chapter;
import com.IW.utils.PersistenceUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class ChapterDAO extends Chapter implements IChapterDAO {

    private static final Logger logger = LoggerFactory.getLogger(ChapterDAO.class);

    public ChapterDAO(long id) {
        Chapter c = null;
        EntityManager em = PersistenceUnit.createEM();
        em.getTransaction().begin();
        try {
            c = em.find(Chapter.class, id);
            if (c != null) {
                this.setId(c.getId());
                this.setNPage(c.getNPage());
                this.setPart(c.getPart());
                this.setResume(c.getResume());
                this.setNote(c.getNote());
                this.setText(c.getText());
            }
        } catch (NoResultException e) {
            logger.error("Capítulo con id: " + id + "\nNo encontrado en la base de datos");
        }
        PersistenceUnit.closeEM();
    }

    public ChapterDAO() {
        super();
    }

    @Override
    public void persist() {
        EntityManager em = PersistenceUnit.createEM();
        em.getTransaction().begin();
        Chapter c = em.merge(this);
        em.persist(c);
        if (getId() == -1) {
            em.flush();
            setId(c.getId());
        }
        em.getTransaction().commit();
        PersistenceUnit.closeEM();
    }

    @Override
    public void remove() {
        EntityManager em = PersistenceUnit.createEM();
        em.getTransaction().begin();
        Chapter c = em.merge(this);
        em.remove(c);
        em.getTransaction().commit();
        PersistenceUnit.closeEM();
    }
}
