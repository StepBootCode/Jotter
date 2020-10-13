/*
 *
 *  Created by Sergey Stepchenkov on 02.10.20 17:10
 *  Copyright (c) 2020. All rights reserved.
 *  More info on www.bootcode.ru
 *  Last modified 02.10.20 17:10
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
import io.reactivex.Maybe;

@Dao
public interface NotesDao {

    @Query("SELECT * FROM Note ORDER BY todate DESC")
    Flowable<List<Note>> getAll();

    @Query("SELECT * FROM Note  ORDER BY todate DESC LIMIT 10")
    List<Note> get10();

    @Query("SELECT * FROM Note WHERE id = :id")
    Flowable<Note> getById(String id);

    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);



}




