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

    // Создаем БД, прописывает тут же миграции (обновления базы данных)
    // Для каждого обновления БД свое имя MIGRATION_1_2, MIGRATION_2_3
    // Порядок обновления прописан в основном классе JotterDatabase
    // Более подробно как действуют правила можно увидеть там же
    public DatabaseModule(Application mApplication) {
        // allowMainThreadQueries - затычка, необходимо переделать на RX
        jotterDatabase = Room.databaseBuilder(mApplication, JotterDatabase.class, "jotter-db")
                .addMigrations(JotterDatabase.MIGRATION_1_2)
                .allowMainThreadQueries().build();
    }

    // Опять же тут все стандартно для дагера, можно смотреть документацию к нему
    // В модуле БД будет только один провайдер, укажем что JotterDatabase будет единственным
    @Singleton
    @Provides
    JotterDatabase provideJotterDatabase() {
        return jotterDatabase;
    }



}
