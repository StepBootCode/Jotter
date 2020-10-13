/*
 *
 *  Created by Sergey Stepchenkov on 07.10.20 15:18
 *  Copyright (c) 2020. All rights reserved.
 *  More info on www.bootcode.ru
 *  Last modified 07.10.20 15:18
 *
 */

package ru.bootcode.jotter.database;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.bootcode.jotter.R;

public class ListNotesAdapter  extends RecyclerView.Adapter<ListNotesAdapter.ViewHolder>{
    private List<Note> notesLists;

    public ListNotesAdapter(List<Note> myDataLists) {
        this.notesLists = myDataLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_item,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Note md=notesLists.get(i);

       // viewHolder.tvName.setText(md.getName());
        viewHolder.tvNote.setText(md.getNote());
        viewHolder.tvNote.setBackgroundColor(md.getColor());
        GradientDrawable bgShape = (GradientDrawable) viewHolder.vwCircle.getBackground();
        bgShape.setColor(Color.parseColor("#ffbb33"));

    }

    @Override
    public int getItemCount() {
        return notesLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
       // private TextView tvName;
        private TextView tvNote;
        private View vwCircle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNote=(TextView)itemView.findViewById(R.id.tvNote);
            vwCircle = itemView.findViewById(R.id.vwCircle);
        }
    }
}