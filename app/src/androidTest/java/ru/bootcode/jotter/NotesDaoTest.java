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

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import io.reactivex.functions.Predicate;
import ru.bootcode.jotter.database.JotterDatabase;
import ru.bootcode.jotter.database.Note;
import ru.bootcode.jotter.database.NotesDao;

@RunWith(AndroidJUnit4ClassRunner.class)
public class NotesDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule =
            new InstantTaskExecutorRule();

    private NotesDao notesDao;
    private JotterDatabase jotterDBTest;
    private static Date currDate = Calendar.getInstance().getTime();
    private static final Note note = new Note("Тест", currDate,
            "Тестирование записи /n необходимо все тщательно /n проверять и перепроверять",
            1, 1,true, true, currDate);

    @Before
    public void createDb() {

        Context context = ApplicationProvider.getApplicationContext();
        jotterDBTest = Room.inMemoryDatabaseBuilder(context, JotterDatabase.class)
                .allowMainThreadQueries()
                .build();
        notesDao = jotterDBTest.notesDao();
    }

    @After
    public void closeDb() throws IOException {
        jotterDBTest.close();
    }

    @Test
    public void writeNoteAndReadNote() throws Exception {
        // Простейший тест, в котором мы производим запись в базу данных и
        // дальнейшее считывание, и сравниваем записываемый и прочитаный объекты
        // тест за одно проверяет возможность записи/чтения и возврат не нулевого результата
        notesDao.insert(note);
        notesDao.getById(note.getId())
                .test()
                .assertValue(new Predicate<Note>() {
                    @Override
                    public boolean test(Note byNote) throws Exception {
                            return byNote.equals(note);

                    }
                });
    }

    @Test
    public void updateNoteAndReadNote() throws Exception {

        notesDao.insert(note);

        Note updatedNote = new Note(note.getId(), "new Тест", currDate,
                "Тестирование записи /n необходимо все тщательно /n проверять и перепроверять",
                1, 1,true, true, currDate);

        notesDao.update(updatedNote);

        notesDao.getById(note.getId())
                .test()
                .assertValue(new Predicate<Note>() {
                    @Override
                    public boolean test(Note byNote) throws Exception {
                        return byNote != null && byNote.getId().equals(note.getId()) &&
                                byNote.getName().equals("new Тест");
                    }
                });

    }

    @Test
    public void deleteNoteAndReadNote() throws Exception{

        notesDao.insert(note);
        notesDao.delete(note);
        notesDao.getById(note.getId())
                .test()
                .assertNoValues();
    }

}
