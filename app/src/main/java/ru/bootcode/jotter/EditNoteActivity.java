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
import androidx.appcompat.widget.PopupMenu;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Date;
import javax.inject.Inject;

import ru.bootcode.jotter.database.JotterDatabase;
import ru.bootcode.jotter.database.Note;

import static java.util.Calendar.*;

public class EditNoteActivity extends AppCompatActivity {
    public final static String INTENT_RESULT = "ru.bootcode.jotter.EditNoteActivity";

    @Inject
    JotterDatabase jdb;

    int noteColor = Color.WHITE;
    int img_id = 0;
    int type_id = 0;
    boolean check = false;
    boolean crypto = false;
    Date dateTo = new Date();
    EditText teNote;
    TextView tvDate;
    Button btDate;
    Calendar dateAndTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        ActionBar actionBar =getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ((App) getApplication()).mAppComponent.inject(this);



        teNote = findViewById(R.id.teNote);

        dateAndTime= getInstance();

        setInitialDateTime();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!teNote.getText().toString().trim().isEmpty()) {
                    Note note = new Note("", dateTo, teNote.getText().toString(), img_id, type_id, check, crypto, dateTo);
                    jdb.notesDao().insert(note);
                    Intent answerIntent = new Intent();
                    answerIntent.putExtra(INTENT_RESULT, note.getId());
                    setResult(RESULT_OK, answerIntent);
                }
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // установка начальных даты и времени
    private void setInitialDateTime() {
        //dateTo.setTime(dateAndTime.getTimeInMillis());
        //tvDate.setText(DateUtils.formatDateTime(EditNoteActivity.this,
        //        dateAndTime.getTimeInMillis(),
         //       DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
         //               | DateUtils.FORMAT_SHOW_TIME));
    }

    // отображаем диалоговое окно для выбора даты
    public void setDate(View v) {
        new DatePickerDialog(this, d,
                dateAndTime.get(YEAR),
                dateAndTime.get(MONTH),
                dateAndTime.get(DAY_OF_MONTH))
                .show();
    }

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(YEAR, year);
            dateAndTime.set(MONTH, monthOfYear);
            dateAndTime.set(DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };

    public void setPassword(View v) {

    }

    public void setColor(View v) {
        showPopupMenu(v);
    }

    public void openPopupMenu(View v) {

    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.color_select_menu);

        popupMenu
                .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                noteColor = Color.RED;
                                teNote.setBackgroundColor(noteColor);
                                Toast.makeText(getApplicationContext(),
                                        "Вы выбрали  1",
                                        Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.menu2:
                                Toast.makeText(getApplicationContext(),
                                        "Вы выбрали  2",
                                        Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.menu3:
                                Toast.makeText(getApplicationContext(),
                                        "Вы выбрали  3",
                                        Toast.LENGTH_SHORT).show();
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