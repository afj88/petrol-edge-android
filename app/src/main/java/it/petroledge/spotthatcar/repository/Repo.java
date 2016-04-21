package it.petroledge.spotthatcar.repository;

import android.database.sqlite.SQLiteDatabase;

import it.petroledge.spotthatcar.App;
import it.petroledge.spotthatcar.repository.schema.CarDao;
import it.petroledge.spotthatcar.repository.schema.DaoMaster;
import it.petroledge.spotthatcar.repository.schema.DaoSession;

/**
 * Created by friz on 10/04/16.
 */
public abstract class Repo {

    private DaoMaster.DevOpenHelper mOpenHelper;
    SQLiteDatabase mDb;

    protected Repo() {
        mOpenHelper = new DaoMaster.DevOpenHelper(App.getAppContext(), "stc-db", null);
    }

    protected DaoSession openSession() {
        mDb = mOpenHelper.getWritableDatabase();

        DaoMaster daoMaster = new DaoMaster(mDb);
        DaoSession daoSession = daoMaster.newSession();

        return daoSession;
    }

    protected void closeSession (DaoSession session) {
        if (mDb.isOpen()) {
            mDb.close();
        }

        mDb = null;
    }
}
