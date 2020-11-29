package com.example.aplikasibatiktes.roomdatabase;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.loader.content.AsyncTaskLoader;

import com.example.aplikasibatiktes.models.Batik;
import com.example.aplikasibatiktes.models.BatikSlide;

import java.nio.channels.AsynchronousChannelGroup;
import java.util.List;

public class BatikRepository {

    private BatikDao mBatikDao;
    private LiveData<List<Batik>> mAllBatik;
    private LiveData<List<BatikSlide>> mAllBatikPopular;
    private LiveData<List<Batik>> mSearchBatik;

    public BatikRepository(Application application) {
        BatikRoomDatabase db = BatikRoomDatabase.getDatabase(application);
        mBatikDao = db.batikDao();
        mAllBatik = mBatikDao.getAllBatik();
        mAllBatikPopular = mBatikDao.getAllBatikPopular();
    }

    LiveData<List<Batik>> getAllBatik() {
        return mAllBatik;
    }

    public void insert(List<Batik> batiks) {
        /*new insertAsynTask(mBatikDao).execute(batiks);*/
        BatikRoomDatabase.databaseWriteExecutor.execute(() -> {
            mBatikDao.insertAllBatik(batiks);
        });
    }

    LiveData<List<BatikSlide>> getAllBatikPopular() {
        return mAllBatikPopular;
    }

    public void insertPopular(List<BatikSlide> batikslide) {
        BatikRoomDatabase.databaseWriteExecutor.execute(() -> {
            mBatikDao.insertAllBatikPopular(batikslide);
        });
    }

    LiveData<List<Batik>> searchBatik(String search) {
        return mBatikDao.searchBatik(search);
    }

    /*static class insertAsynTask extends AsyncTask<List<Batik>,Void,Void>{

        private BatikDao batikDao;

        public insertAsynTask(BatikDao dao) {
            batikDao = dao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(final List<Batik>... lists) {
            batikDao.insertAllBatik(lists[0]);
            return null;
        }
    }*/

}
