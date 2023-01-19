package com.amiel.moviecenter.DB.AsyncTask;

import android.os.AsyncTask;

import com.amiel.moviecenter.DB.DAO.BaseDao;

public class InsertAsyncTask<T> extends AsyncTask<T, Void, Void> {

    private final BaseDao<T> baseDao;

    public InsertAsyncTask(BaseDao<T> dao) {
        baseDao = dao;
    }

    @SafeVarargs
    @Override
    protected final Void doInBackground(T... obj) {
        baseDao.insert(obj);
        return null;
    }
}
