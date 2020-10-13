/*
 *
 *  Created by Sergey Stepchenkov on 08.10.20 9:20
 *  Copyright (c) 2020. All rights reserved.
 *  More info on www.bootcode.ru
 *  Last modified 08.10.20 9:20
 *
 */

package ru.bootcode.jotter;

import android.app.Application;

import ru.bootcode.jotter.daggemodule.AppModule;
import ru.bootcode.jotter.daggemodule.DaggerAppComponent;
import ru.bootcode.jotter.daggemodule.DatabaseModule;

public class App extends Application {

    DaggerAppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppComponent = (DaggerAppComponent) DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .databaseModule(new DatabaseModule(this))
                .build();
    }

}
