/*
 *
 *  Created by Sergey Stepchenkov on 05.10.20 14:12
 *  Copyright (c) 2020. All rights reserved.
 *  More info on www.bootcode.ru
 *  Last modified 05.10.20 14:11
 *
 */

package ru.bootcode.jotter.database;

import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;
import java.util.UUID;

@Entity
public class Note {

    @NonNull
    @PrimaryKey
    private  String id;                           //уникальный идентификатор записи UUID
    private  String name;                         //Наименование записи
    @TypeConverters({DataConverter.class})
    private  Date date;                           //Датаи время записи
    private  String note;                         //сама запись
    private  int img_id;                          // Ссылка на картинку или null
    private  int type_id;                         // Ссылка на TYPE
    private  boolean check;                       //Истина если есть срок
    private  boolean crypto;                      // Истина если шифруется/требуется пароль
    @TypeConverters({DataConverter.class})
    private  Date todate;                         //срок исполнения
    private  int   color;                         //срок исполнения


    @Ignore
    public Note(String nameIn, Date dateIn, String noteIn, int img_idIn, int type_idIn, boolean checkIn, boolean cryptoIn, Date todateIn) {
        id      = UUID.randomUUID().toString();
        name    = nameIn;
        date    = dateIn;
        note    = noteIn;
        img_id  = img_idIn;
        type_id = type_idIn;
        check   = checkIn;
        crypto  = cryptoIn;
        todate  = todateIn;
        color = Color.YELLOW;
    }

    public Note(@NonNull String id, String name, Date date, String note, int img_id, int type_id, boolean check, boolean crypto, Date todate) {
        this.id         = id;
        this.name       = name;
        this.date       = date;
        this.note       = note;
        this.img_id     = img_id;
        this.type_id    = type_id;
        this.check      = check;
        this.crypto     = crypto;
        this.todate     = todate;
        this.color = Color.YELLOW;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Note tmp = (Note) o;

        if (this.img_id != tmp.img_id || this.type_id != tmp.type_id ||
                this.check != tmp.check || this.crypto != tmp.crypto ) {
            return false;
        }

        if (!this.date.equals(tmp.date) || !this.todate.equals(tmp.todate)) {
            return false;
        }

        return (this.id.equals(tmp.id) && this.name.equals(tmp.name) && this.note.equals(tmp.note));
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public String getNote() {
        return note;
    }

    public int getImg_id() {
        return img_id;
    }

    public int getType_id() {
        return type_id;
    }

    public boolean isCheck() {
        return check;
    }

    public boolean isCrypto() {
        return crypto;
    }

    public Date getTodate() {
        return todate;
    }


    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

}
