package com.IW.model.dao;

import com.IW.interfaces.SQL.ICharacterDAO;
import com.IW.model.objects.Character;
import com.IW.utils.PersistenceUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class CharacterDAO extends Character implements ICharacterDAO {

    private static final Logger logger = LoggerFactory.getLogger(CharacterDAO.class);

    public CharacterDAO(long id) {
        Character c = null;
        EntityManager em = PersistenceUnit.createEM();
        em.getTransaction().begin();
        try {
            c = em.find(Character.class, id);
            if (c != null) {
                this.setId(c.getId());
                this.setName(c.getName());
                this.setPhoto(c.getPhoto());
                this.setDescription(c.getDescription());
            }
        } catch (NoResultException e) {
            logger.error("Autor con id: " + id + "\nNo encontrado en la base de datos");
        }
        PersistenceUnit.closeEM();
    }

    public CharacterDAO() {
        super();
    }

    @Override
    public void persist() {
        EntityManager em = PersistenceUnit.createEM();
        em.getTransaction().begin();
        Character c = em.merge(this);
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
        Character c = em.merge(this);
        em.remove(c);
        em.getTransaction().commit();
        PersistenceUnit.closeEM();
    }
}
