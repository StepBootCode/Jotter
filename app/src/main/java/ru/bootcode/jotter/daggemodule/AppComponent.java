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

@Singleton
@Component(dependencies = {}, modules = {AppModule.class, DatabaseModule.class})
public interface AppComponent {

    // Интерфейс который мы скарливем Дагеру
    // в анотации Components не забываем прописывать наши модули

    // Область в которой прописываются инжекты, т.е.
    // Активити(классы) куда мы будем транслировать наши задоккереные классы ---
    void inject(MainActivity mainActivity);
    void inject(EditNoteActivity editNoteActivity);
    //--------------------------------------------------------------------------

    Application application();
    JotterDatabase jotterDatabase();
    //NotesDao notesDao();
}