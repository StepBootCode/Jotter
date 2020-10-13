/*
 *
 *  Created by Sergey Stepchenkov on 07.10.20 14:11
 *  Copyright (c) 2020. All rights reserved.
 *  More info on www.bootcode.ru
 *  Last modified 07.10.20 13:25
 *
 */

package ru.bootcode.jotter.RichEdit;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.style.ReplacementSpan;


public class SelectionTrackingSpan extends ReplacementSpan {

    @Override
    public int getSize(Paint paint, CharSequence charSequence, int i, int i1, Paint.FontMetricsInt fontMetricsInt) {
        //        // размер будет достаточный для того чтобы нарисовать буквы + расстояния между ними
        //        return (int)(paint.measureText(text, start, end) + tracking * (end - start));
        return 0;
    }

    @Override
    public void draw(Canvas canvas, CharSequence charSequence, int i, int i1, float v, int i2, int i3, int i4, Paint paint) {
        //        float dx = x;
        //        for (int i = start; i < end; i++) {
        //            // если символ не попадает в выделенную часть, будем рисовать его просто черным
        //            if (i < selectionBegin || i >= (selectionEnd != NO_SELECTION ? selectionEnd + 1 : selectionBegin + 1)) paint.setColor(Color.BLACK);
        //            else paint.setColor(selectionColor);
        //            canvas.drawText(text, i, i + 1, dx, y, paint);
        //            dx += paint.measureText(text, i, i + 1) + tracking;
        //        }
    }

}