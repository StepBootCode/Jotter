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
import dagger.Component;
import ru.bootcode.jotter.EditNoteActivity;
import ru.bootcode.jotter.MainActivity;
import ru.bootcode.jotter.database.JotterDatabase;
import ru.bootcode.jotter.database.NotesDao;

@Singleton
@Component(dependencies = {}, modules = {AppModule.class, DatabaseModule.class})
public interface AppComponent {

    void inject(MainActivity mainActivity);
    void inject(EditNoteActivity editNoteActivity);
    //AppComponent inject(MainActivity mainActivity);

    //NotesDao notesDao();

    JotterDatabase jotterDatabase();

    //ProductRepository productRepository();

    Application application();

}