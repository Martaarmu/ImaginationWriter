package com.IW.interfaces;

import com.IW.interfaces.IBeans.IAuthor;

public interface SQL extends IDAO{
    interface IAuthorDAO extends SQL{
        boolean checkUser();
    }
    interface IBookDAO extends SQL{
        void addAuthor(IAuthor author);
        void deleteAuthor(IAuthor author);
    }
    interface IChapterDAO extends SQL{

    }
    interface ICharacterDAO extends SQL{

    }
    interface ISceneDAO extends SQL{

    }
    interface IPartDAO extends SQL{

    }
}
