package com.IW.model.objects;

import com.IW.interfaces.IBeans.IBook;
import com.IW.interfaces.IBeans.ICharacter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "_Character")
public class Character implements Serializable, ICharacter {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected long id;
    @Column(name = "name")
    protected String name;
    @Column(name = "description")
    protected String description;
    @Column(name = "photo")
    protected String photo;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = Book.class)
    protected Book book;

    public Character(long id, String name, String description, String photo, Book book) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.photo = photo;
        this.book = book;
    }

    public Character() {
        this.id = -1;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getPhoto() {
        return photo;
    }

    @Override
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public IBook getBook() {
        return book;
    }

    @Override
    public void setBook(IBook book) {
        this.book = (Book) book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Character character = (Character) o;
        return id == character.id;
    }

}
