/*
 *
 *  Created by Sergey Stepchenkov on 16.10.20 13:23
 *  Copyright (c) 2020. All rights reserved.
 *  More info on www.bootcode.ru
 *  Last modified 09.10.20 13:20
 *
 */

package ru.bootcode.jotter.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import io.reactivex.Flowable;


@Dao
public interface NotesDao {
    // Запрос всех записей из базы данных с сортировкой по дате (новые записи - первые)
    // возвращает Flowable - изменяемый, тоесть при внесении данных запрос будет выполнен автоматом
    // в том же потоке где он выполнялся ранее
    @Query("SELECT * FROM Note ORDER BY todate DESC")
    Flowable<List<Note>> getAll();

    // Запрос 10 последних записей из базы данных с сортировкой по дате (новые записи - первые)
    // возвращает Flowable - изменяемый, тоесть при внесении данных запрос будет выполнен автоматом
    // в том же потоке где он выполнялся ранее
    @Query("SELECT * FROM Note  ORDER BY todate DESC LIMIT 10")
    Flowable<List<Note>> get10();

    // Запрос записи из базы данных по идентификатору возвращает Flowable - изменяемый,
    // тоесть при внесении данных запрос будет выполнен автоматом
    // в том же потоке где он выполнялся ранее
    @Query("SELECT * FROM Note WHERE id = :id")
    Flowable<Note> getById(String id);

    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);
}




