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
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    // Опять же тут все стандартно для дагера, можно смотреть документацию к нему
    // В модуле приложения будет только один провайдер, укажем что Application будет единственным
    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }
}
