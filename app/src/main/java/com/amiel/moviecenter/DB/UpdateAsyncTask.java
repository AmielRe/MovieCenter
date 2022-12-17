package com.amiel.moviecenter.DB;

import android.os.AsyncTask;

import com.amiel.moviecenter.DB.DAO.BaseDao;

public class UpdateAsyncTask<T> extends AsyncTask<T, Void, Void> {

    private final BaseDao<T> baseDao;

    public UpdateAsyncTask(BaseDao<T> dao) {
        baseDao = dao;
    }

    @SafeVarargs
    @Override
    protected final Void doInBackground(T... obj) {
        baseDao.update(obj);
        return null;
    }
}
