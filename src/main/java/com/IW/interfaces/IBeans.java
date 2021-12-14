package com.IW.interfaces;

import java.util.List;

public interface IBeans {
    interface IAuthor{
        void setId(long id);
        void setName(String name);
        void setPassword(String password);
        void setBooks(List<IBook> books);
        void setPhoto(String photo);

        long getId();
        String getPassword();
        List<IBook> getBooks();
        String getName();
        String getPhoto();
    }

    interface IBook{
        void setId(long id);
        void setTitle(String title);
        void setParts(List<IPart> parts);
        void setCharacters(List<ICharacter> characters);
        void setScenes(List<IScene> scenes);
        void setEditors(List<IAuthor> authors);
        void setCover(String cover);
        void setNPages(int nPages);
        void setCreator(IAuthor creator);

        long getId();
        String getTitle();
        List<IPart> getParts();
        List<ICharacter> getCharacters();
        List<IScene> getScenes();
        List<IAuthor> getEditors();
        String getCover();
        int getNPages();
        IAuthor getCreator();
    }

    interface IChapter{
        void setId(long id);
        void setNPage(int nPage);
        void setPart(IPart part);
        void setResume(String resume);
        void setNote(String note);
        void setText(String text);

        long getId();
        int getNPage();
        IPart getPart();
        String getResume();
        String getNote();
        String getText();
    }

    interface ICharacter{
        void setId(long id);
        void setName(String name);
        void setDescription(String description);
        void setPhoto(String photo);
        void setBook(IBook book);

        long getId();
        String getName();
        String getDescription();
        String getPhoto();
        IBook getBook();
    }

    interface IScene{
        long getId();
        void setTitle(String title);
        void setDescription(String description);
        void setPhoto(String photo);
        void setId(long id);
        void setBook(IBook book);

        String getTitle();
        String getDescription();
        String getPhoto();
        IBook getBook();
    }

    interface IPart{
        void setId(long id);
        void setNPart(int nPart);
        void setChapters(List<IChapter> chapters);
        void setBook(IBook book);

        long getId();
        int getNPart();
        List<IChapter> getChapters();
        IBook getBook();
    }
}
