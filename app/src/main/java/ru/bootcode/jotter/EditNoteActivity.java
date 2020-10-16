/*
 *
 *  Created by Sergey Stepchenkov on 16.10.20 17:37
 *  Copyright (c) 2020. All rights reserved.
 *  More info on www.bootcode.ru
 *  Last modified 16.10.20 17:23
 *
 */

package ru.bootcode.jotter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.jaredrummler.android.colorpicker.ColorShape;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import ru.bootcode.jotter.database.JotterDatabase;
import ru.bootcode.jotter.database.Note;

import static java.util.Calendar.*;

public class EditNoteActivity extends AppCompatActivity implements ColorPickerDialogListener {
    public final static String INTENT_RESULT = "ru.bootcode.jotter.EditNoteActivity";

    // Это то что нужно получить от дагера
    @Inject
    JotterDatabase jdb;

    // поля в которые мы запишем то что нам передали при создании Активити
    String sID;
    Note tempNote;

    // Вспомогательные переменные
    Calendar dateAndTime = getInstance();

    // Элементы текущего layout
    ImageButton imgbtnLock;
    EditText teNote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        // Элементы текущего layout
        teNote = findViewById(R.id.teNote);
        imgbtnLock= findViewById(R.id.imgbtnLock);

        // Отобразим кнопку назад (по ней же и будем записывать или обновлять данные
        ActionBar actionBar =getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Получаем от дагера необходимые ссылки
        ((App) getApplication()).mAppComponent.inject(this);

        // Получим переданные данные в текушее Активити
        // если id = пустой строке то значит мы создаем новую запись в БД,
        // иначе будем обновлять запись по id и нам требуеться запросить
        // запись из базы данных
        Intent intent = getIntent();
        sID  = intent.getStringExtra("id");
        if (sID == null) {sID = "";}
        if (!sID.isEmpty()) {
            Disposable d = jdb.notesDao().getById(sID)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Note>() {
                        @Override
                        public void accept(Note note) throws Exception {
                            // запишем полученные данные во временную переменную, которую и будем
                            // править если потребуеться пользователю и ее же и обновим
                            tempNote = note;

                            // Преобразим наши элементы на layout
                            teNote.setText(note.getNote());
                            if (tempNote.isCrypto()) {
                                imgbtnLock.setImageResource(R.drawable.ic_act_unlock);
                            }
                        }
                    });
        } else {
            // новая запись = новый Note, настроим его по дефолту
            tempNote = new Note("", new Date(), "", 1, 1, false, false, new Date());
            tempNote.setColor(Color.WHITE);
            setInitialDateTime();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Обработка нажатия кнопки назад, по сути запись в базу данных
        // и возвращение к основной Активити
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!(tempNote == null)) {
                    tempNote.setNote(teNote.getText().toString());
                    // Проверку на пустую запись не делаем, пользователь мог и пустую
                    // захотеть создать
                    //if (!tempNote.getNote().isEmpty()) {
                        if (sID.isEmpty()) {
                            jdb.notesDao().insert(tempNote);
                        }else{
                            jdb.notesDao().update(tempNote);
                        }
                        Intent answerIntent = new Intent();
                        answerIntent.putExtra(INTENT_RESULT, tempNote.getId());
                        setResult(RESULT_OK, answerIntent);
                    //}
                }
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Создание типового диалога для выбора цвета, два метода которого были заимплеменчены ниже ----
    private void createColorPickerDialog() {
        ColorPickerDialog.newBuilder()
                .setColor(Color.RED)
                .setDialogType(ColorPickerDialog.TYPE_PRESETS)
                .setAllowCustom(true)
                .setAllowPresets(true)
                .setColorShape(ColorShape.SQUARE)
                .setDialogId(0)
                .show(this);
    }

    @Override
    public void onColorSelected ( int dialogId, int color){
        if (dialogId == 0) {
            tempNote.setColor(color);
            teNote.setBackgroundColor(color);
        }
    }

    @Override
    public void onDialogDismissed ( int dialogId){
        Toast.makeText(this, "Dialog dismissed", Toast.LENGTH_SHORT).show();
    }

    public void setColor(View v) {
        createColorPickerDialog();
    }
    //----------------------------------------------------------------------------------------------

    // установка даты и времени, используется для выбора времени под будильник
    // поочереди вызываются диалоги выбора даты потом времени --------------------------------------
    // обработчик выбора даты
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(YEAR, year);
            dateAndTime.set(MONTH, monthOfYear);
            dateAndTime.set(DAY_OF_MONTH, dayOfMonth);
            setTime();
        }
    };

    // обработчика выбора времени
    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            dateAndTime.set(HOUR, i);
            dateAndTime.set(MINUTE, i1);
            setInitialDateTime();
        }
    };

    // отображаем диалоговое окно для выбора даты
    public void setDate(View v) {
        new DatePickerDialog(this, d,
                dateAndTime.get(YEAR),
                dateAndTime.get(MONTH),
                dateAndTime.get(DAY_OF_MONTH))
                .show();
    }

    // отображаем диалоговое окно для выбора времени
    public void setTime() {
        new TimePickerDialog(this, t,
                dateAndTime.get(HOUR),
                dateAndTime.get(MINUTE),
                true)
                .show();
    }
    // И в итоге инициализируем время которое выбрал пользователь
    private void setInitialDateTime() {
        Date dateTo = new Date();
        dateTo.setTime(dateAndTime.getTimeInMillis());
        tempNote.setTodate(dateTo);
        Toast.makeText(getApplicationContext(),
                DateUtils.formatDateTime(EditNoteActivity.this,
                       dateAndTime.getTimeInMillis(),
                       DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME),
                Toast.LENGTH_SHORT).show();
    }
    //----------------------------------------------------------------------------------------------

    // попап меню для остальных действий с записью -------------------------------------------------
    public void openPopupMenu(View v) {
        showPopupMenu(v);
    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.edit_note_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menuClear:
                                tempNote = new Note("", new Date(), "", 1, 1, false, false, new Date());
                                tempNote.setColor(Color.WHITE);
                                teNote.setBackgroundColor(tempNote.getColor());
                                teNote.setText("");
                                return true;
                            case R.id.menuSave:
                                if (!(tempNote == null)) {
                                    tempNote.setNote(teNote.getText().toString());
                                    tempNote.setDate(dateAndTime.getTime());
                                    // Проверку на пустую запись не делаем, пользователь мог и пустую
                                    // захотеть создать
                                    //if (!tempNote.getNote().isEmpty()) {
                                    if (sID.isEmpty()) {
                                        jdb.notesDao().insert(tempNote);
                                    }else{
                                        jdb.notesDao().update(tempNote);
                                    }
                                    Intent answerIntent = new Intent();
                                    answerIntent.putExtra(INTENT_RESULT, tempNote.getId());
                                    setResult(RESULT_OK, answerIntent);
                                    //}
                                }
                                return true;
                            default:
                                return false;
                        }
                    }
                });

        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                Toast.makeText(getApplicationContext(), "onDismiss",
                        Toast.LENGTH_SHORT).show();
            }
        });
        popupMenu.show();
    }
    //----------------------------------------------------------------------------------------------

    public void setPassword(View v) {
        // отмечаем или снимаем отметку что запись приватная
        if (tempNote.isCrypto()) {
            tempNote.setCrypto(false);
            imgbtnLock.setImageResource(R.drawable.ic_act_lock);
            Toast.makeText(getApplicationContext(), "unlock",
                    Toast.LENGTH_SHORT).show();
        } else {
            tempNote.setCrypto(true);
            imgbtnLock.setImageResource(R.drawable.ic_act_unlock);
            Toast.makeText(getApplicationContext(), "lock",
                    Toast.LENGTH_SHORT).show();
        }
    }
}