package com.IW.model.objects;

import com.IW.interfaces.IBeans.IAuthor;
import com.IW.interfaces.IBeans.IBook;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "Author")
@NamedQueries({
        @NamedQuery(name = "findByName", query = "SELECT a FROM Author a WHERE a.name=:name"),
        @NamedQuery(name = "listAll", query = "SELECT a FROM Author a")
})
public class Author implements Serializable, IAuthor {
    //TODO desactivar la cuenta de Autor con un booleano?
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected long id;
    @Column(name = "name", unique = true, nullable = false)
    protected String name;
    @Column(name = "password", nullable = false)
    protected String password;
    @Column(name = "photo")
    protected String photo;
    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.LAZY)
    protected List<Book> books;

    public Author(long id, String name, String password, String photo) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.photo = photo;
    }

    public Author() {
        this.id = -1;
        this.photo = "";
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
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
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
    public List<IBook> getBooks() {
        return (List<IBook>) this._getBooks();
    }

    private List<? extends IBook> _getBooks() {
        return books;
    }

    @Override
    public void setBooks(List<IBook> books) {
        _setBooks(books);
    }

    private void _setBooks(List<? extends IBook> books) {
        this.books = (List<Book>) books;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return id == author.id;
    }
}
