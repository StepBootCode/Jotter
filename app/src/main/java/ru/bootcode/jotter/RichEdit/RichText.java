/*
 *
 *  Created by Sergey Stepchenkov on 07.10.20 14:11
 *  Copyright (c) 2020. All rights reserved.
 *  More info on www.bootcode.ru
 *  Last modified 07.10.20 14:09
 *
 */

package ru.bootcode.jotter.RichEdit;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class RichText extends androidx.appcompat.widget.AppCompatEditText  {


    // тут будем хранить исходную строку
    private final String originalText = null;

    // это число определяет, сколько пикселей отведено на трекинг (расстояние между буквами)
    //private float tracking = convertDpToPx(16);
    // цвет выделения
    private int selectionColor = Color.parseColor("#5591F6");

    // специальное значение для отсуттсвия выделения
    private static final int NO_SELECTION = -1;
    // начало и конец выделения (индексы символов в строке)
    private int selectionBegin = NO_SELECTION,
            selectionEnd = NO_SELECTION;

    // та самая штука, которая отвечает за отрисовку трекинга и выделения
    private SelectionTrackingSpan selectionTrackingSpan = new SelectionTrackingSpan();

    // понадобится позже, чтобы определять, на какую букву был клик
    private int baseWidth;



    public RichText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //super(context, attrs, defStyleAttr, defStyleRes);
        /*
        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_UP:
                        // считаем индекс символа, на который кликнули
                        int index = (int)(event.getX() / baseWidth);
                        // и устанавливаем границы выделения согласно описанным выше правилам
                        if (selectionBegin == index && selectionEnd == NO_SELECTION) {
                            selectionBegin = NO_SELECTION;
                            selectionEnd = NO_SELECTION;
                            v.invalidate();
                            break;
                        }

                        if (selectionBegin == NO_SELECTION) {
                            selectionBegin = index;
                        }
                        else if (selectionEnd == NO_SELECTION) {
                            selectionEnd = index;
                            if (selectionBegin > selectionEnd) {
                                int tmp = selectionBegin;
                                selectionBegin = selectionEnd;
                                selectionEnd = tmp;
                            }
                        } else {
                            selectionBegin = index;
                            selectionEnd = NO_SELECTION;
                        }
                        v.invalidate();
                        break;
                }
                return false;
            }
            */
/*
            protected void onSizeChanged(int w, int h, int oldw, int oldh) {
                super.onSizeChanged(w, h, oldw, oldh);
                baseWidth = w / originalText.length();
            }
            */
        //});
    }

}


