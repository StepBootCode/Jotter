/*
 *
 *  Created by Sergey Stepchenkov on 02.10.20 17:10
 *  Copyright (c) 2020. All rights reserved.
 *  More info on www.bootcode.ru
 *  Last modified 02.10.20 17:10
 *
 */

package ru.bootcode.jotter.daggemodule;

import android.app.Application;

import androidx.room.Room;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import ru.bootcode.jotter.database.JotterDatabase;

@Module
public class DatabaseModule {
    private JotterDatabase jotterDatabase;

    public DatabaseModule(Application mApplication) {
        // allowMainThreadQueries - затычка, необходимо переделать на RX
        jotterDatabase = Room.databaseBuilder(mApplication, JotterDatabase.class, "jotter-db")
                .addMigrations(JotterDatabase.MIGRATION_1_2)
                .allowMainThreadQueries().build();
    }

    @Singleton
    @Provides
    JotterDatabase provideJotterDatabase() {
        return jotterDatabase;
    }



}
