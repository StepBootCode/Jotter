/*
 *
 *  Created by Sergey Stepchenkov on 02.10.20 17:10
 *  Copyright (c) 2020. All rights reserved.
 *  More info on www.bootcode.ru
 *  Last modified 02.10.20 17:10
 *
 */

package ru.bootcode.jotter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import ru.bootcode.jotter.database.JotterDatabase;
import ru.bootcode.jotter.database.ListNotesAdapter;
import ru.bootcode.jotter.database.Note;

import static java.util.Calendar.*;

public class EditNoteActivity extends AppCompatActivity {
    public final static String INTENT_RESULT = "ru.bootcode.jotter.EditNoteActivity";

    @Inject
    JotterDatabase jdb;
    String sID;
    Note tempNote;

    ImageButton imgbtnLock;
    EditText teNote;
    Calendar dateAndTime;
    Date dateTo = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        Intent intent = getIntent();
        sID  = intent.getStringExtra("id");// Если пусто то новый, иначе это редактирование

        ActionBar actionBar =getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        dateAndTime= getInstance();

        ((App) getApplication()).mAppComponent.inject(this);

        teNote = findViewById(R.id.teNote);
        imgbtnLock= findViewById(R.id.imgbtnLock);

        if (!sID.isEmpty()) {
            Disposable d = jdb.notesDao().getById(sID)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Note>() {
                        @Override
                        public void accept(Note note) throws Exception {
                            tempNote = note;
                            teNote.setText(note.getNote());
                            dateTo.setTime(note.getTodate().getTime());
                            if (tempNote.isCrypto()) {
                                imgbtnLock.setImageResource(R.drawable.ic_act_unlock);
                            }
                        }
                    });
        } else {
            tempNote = new Note("", new Date(), "", 1, 1, false, false, dateTo);
            tempNote.setColor(Color.WHITE);
            setInitialDateTime();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!(tempNote == null)) {
                    if (!teNote.getText().toString().trim().isEmpty()) {
                        if (!sID.isEmpty()) {
                            jdb.notesDao().insert(tempNote);
                        }else{
                            jdb.notesDao().update(tempNote);
                        }
                        Intent answerIntent = new Intent();
                        answerIntent.putExtra(INTENT_RESULT, tempNote.getId());
                        setResult(RESULT_OK, answerIntent);
                    }
                }
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // установка начальных даты и времени
    private void setInitialDateTime() {
        dateTo.setTime(dateAndTime.getTimeInMillis());
        Toast.makeText(getApplicationContext(),
                DateUtils.formatDateTime(EditNoteActivity.this,
                       dateAndTime.getTimeInMillis(),
                       DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME),
                Toast.LENGTH_SHORT).show();
    }

    // отображаем диалоговое окно для выбора даты
    public void setDate(View v) {
        new DatePickerDialog(this, d,
                dateAndTime.get(YEAR),
                dateAndTime.get(MONTH),
                dateAndTime.get(DAY_OF_MONTH))
                .show();
    }

    // отображаем диалоговое окно для выбора даты
    public void setTime() {
        new TimePickerDialog(this, t,
                dateAndTime.get(HOUR),
                dateAndTime.get(MINUTE),
                true)
                .show();
    }

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(YEAR, year);
            dateAndTime.set(MONTH, monthOfYear);
            dateAndTime.set(DAY_OF_MONTH, dayOfMonth);
            //setInitialDateTime();
            setTime();
        }
    };

    // установка обработчика выбора даты
    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            dateAndTime.set(HOUR, i);
            dateAndTime.set(MINUTE, i1);
            setInitialDateTime();
        }
    };

    public void setPassword(View v) {
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

    public void setColor(View v) {
        showPopupMenuColor(v);
    }

    public void openPopupMenu(View v) {

    }

    private void showPopupMenuColor(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.color_select_menu);

        popupMenu
                .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menuYellow:
                                tempNote.setColor(Color.parseColor("#FFF5C3"));
                                teNote.setBackgroundColor(tempNote.getColor());

                                return true;
                            case R.id.menuGreen:
                                tempNote.setColor(Color.parseColor("#C9FFC9"));
                                teNote.setBackgroundColor(tempNote.getColor());

                                return true;
                            case R.id.menuBlue:
                                tempNote.setColor(Color.parseColor("#C8FFFF"));
                                teNote.setBackgroundColor(tempNote.getColor());

                                return true;
                            case R.id.menuGray:
                                tempNote.setColor(Color.parseColor("#DFDFDF"));
                                teNote.setBackgroundColor(tempNote.getColor());

                                return true;
                            case R.id.menuOrange:
                                tempNote.setColor(Color.parseColor("#FFE1B5"));
                                teNote.setBackgroundColor(tempNote.getColor());

                                return true;
                            case R.id.menuWhite:
                                tempNote.setColor(Color.parseColor("#FFFFFF"));
                                teNote.setBackgroundColor(tempNote.getColor());

                                return true;
                            case R.id.menuSelect:
                                tempNote.setColor(Color.parseColor("#FFFFFF"));
                                teNote.setBackgroundColor(tempNote.getColor());

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
}