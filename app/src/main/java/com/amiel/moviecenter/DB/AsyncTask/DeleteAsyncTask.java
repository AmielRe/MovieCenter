package com.amiel.moviecenter.DB.AsyncTask;

import android.os.AsyncTask;

import com.amiel.moviecenter.DB.DAO.BaseDao;

public class DeleteAsyncTask<T> extends AsyncTask<T, Void, Void> {

    private final BaseDao<T> baseDao;

    public DeleteAsyncTask(BaseDao<T> dao) {
        baseDao = dao;
    }

    @SafeVarargs
    @Override
    protected final Void doInBackground(T... obj) {
        baseDao.delete(obj);
        return null;
    }
}