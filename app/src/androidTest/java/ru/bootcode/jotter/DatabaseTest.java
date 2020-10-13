/*
 *
 *  Created by Sergey Stepchenkov on 02.10.20 17:10
 *  Copyright (c) 2020. All rights reserved.
 *  More info on www.bootcode.ru
 *  Last modified 02.10.20 17:10
 *
 */

package ru.bootcode.jotter;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import ru.bootcode.jotter.database.JotterDatabase;
import ru.bootcode.jotter.database.Note;
import ru.bootcode.jotter.database.NotesDao;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4ClassRunner.class)
public class DatabaseTest {
    private JotterDatabase jotterDBTest;

    @Before
    public void createDb() {

        Context context = ApplicationProvider.getApplicationContext();
        jotterDBTest = Room.inMemoryDatabaseBuilder(context, JotterDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() throws IOException {
        jotterDBTest.close();
    }

}