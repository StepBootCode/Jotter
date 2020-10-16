/*
 *
 *  Created by Sergey Stepchenkov on 16.10.20 17:37
 *  Copyright (c) 2020. All rights reserved.
 *  More info on www.bootcode.ru
 *  Last modified 16.10.20 17:23
 *
 */

package ru.bootcode.jotter;


import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.view.View;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import java.util.List;
import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import ru.bootcode.jotter.database.JotterDatabase;
import ru.bootcode.jotter.database.ListNotesAdapter;
import ru.bootcode.jotter.database.Note;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    static final int EDIT_NOTE_REQUEST = 2;               // Код обратки редактора записи Notes

    // Это то что нужно получить от дагера
    @Inject
    JotterDatabase jdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // объявлен как финальный, т.к. необходимло вытаскивать адаптер для получаения текущей
        // позиции при нажатии и соответсвующего Note, пожно переопределить onItemClick
        // после чего можно избавиться от этого модификатора
        // доп. создан RecyclerClickListener
        final RecyclerView rvMain = findViewById(R.id.rvMain);
        RecyclerView.LayoutManager couponLayoutManager = new LinearLayoutManager(this);
        rvMain.setLayoutManager(couponLayoutManager);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
                intent.putExtra("id",       "");
                startActivityForResult(intent,EDIT_NOTE_REQUEST);

            }
        });
        // Получаем от дагера необходимые ссылки
        ((App) getApplication()).mAppComponent.inject(this);

        // С помошью RX запросим данные и заполним адаптер
        Disposable d = jdb.notesDao().getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Note>>() {
                    @Override
                    public void accept(List<Note> notes) throws Exception {
                        ListNotesAdapter adapter = new ListNotesAdapter(notes);
                        rvMain.setAdapter(adapter);
                    }
                });

        // Обработка нажатия, доп. смотри иницилизацию RecycleView
        // в новое Активити передаем id выбранной записи
        rvMain.addOnItemTouchListener(
                new RecyclerClickListener(this, new RecyclerClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Note nt = ((ListNotesAdapter)rvMain.getAdapter()).getItemNote(position);
                        Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
                        intent.putExtra("id",  nt.getId());
                        startActivityForResult(intent,EDIT_NOTE_REQUEST);
                    }
                }));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_NOTE_REQUEST) {
            if (resultCode == RESULT_OK) {
            }
        }

    }


}
