package com.amiel.moviecenter.DB.AsyncTask;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.amiel.moviecenter.DB.DAO.BaseDao;

public class InsertAsyncTask<T> extends AsyncTask<T, Void, long[]> {

    private final BaseDao<T> baseDao;
    private final MutableLiveData<long[]> insertedId;

    public InsertAsyncTask(MutableLiveData<long[]> liveData, BaseDao<T> dao) {
        insertedId = liveData;
        baseDao = dao;
    }

    @SafeVarargs
    @Override
    protected final long[] doInBackground(T... obj) {
        return baseDao.insert(obj);
    }

    @Override
    protected void onPostExecute(long[] insertId) {
        insertedId.postValue(insertId);
        super.onPostExecute(insertId);
    }

    @Override
    protected void onCancelled() {
        insertedId.postValue(new long[]{});
        super.onCancelled();
    }
}
