package com.example.userloginlogout.app;

import android.content.Context;
import androidx.room.Room;

public  class DatabaseClient {

    private Context mContext;
    private static DatabaseClient mInstance;
    private AppDatabase appDatabase;

    private DatabaseClient(Context mContext) {
        this.mContext = mContext;

        appDatabase = Room.databaseBuilder(mContext, AppDatabase.class, "UserInformation").build();
    }

    public static DatabaseClient getInstance(Context mContext) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(mContext);
        }
        return mInstance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}