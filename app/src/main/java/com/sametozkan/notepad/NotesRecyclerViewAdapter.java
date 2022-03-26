package com.sametozkan.notepad;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotesRecyclerViewAdapter extends RecyclerView.Adapter<NotesRecyclerViewAdapter.MyViewHolder> {

    ArrayList<NoteModel> notesList;
    Context context;
    SQLiteDatabase sqLiteDatabase;
    Fragment fragment;

    public NotesRecyclerViewAdapter(Context context, Fragment fragment, ArrayList<NoteModel> notesList) {
        this.context = context;
        this.fragment = fragment;
        this.notesList = notesList;
        DatabaseHelper databaseHelper = new DatabaseHelper(context, null);
        sqLiteDatabase = databaseHelper.getWritableDatabase();
    }

    private void deleteAlertDialog(DialogInterface.OnClickListener listener) {
        AlertDialog.Builder deleteAlertDialog = new AlertDialog.Builder(context);
        deleteAlertDialog.setTitle("Delete");
        deleteAlertDialog.setMessage("Are you sure you want to delete?");
        deleteAlertDialog.setPositiveButton("Delete", listener);
        deleteAlertDialog.setNegativeButton("Cancel", null);
        deleteAlertDialog.show();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (fragment instanceof NotesFragment) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notes, parent, false);
        }
        else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorites, parent, false);
        }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        NoteModel noteModel = notesList.get(position);
        holder.title.setText(noteModel.getTitle());
        holder.text.setText(noteModel.getText());
        holder.date.setText(noteModel.getDate());
        holder.favoriteBorder.setOnClickListener(view -> {
            if (noteModel.getFavorite() == 0) {
                holder.favoriteBorder.setImageResource(R.drawable.ic_baseline_favorite_32);
            }
            else {
                holder.favoriteBorder.setImageResource(R.drawable.ic_baseline_favorite_border_32);
            }
            ContentValues note = new ContentValues();
            int favorite = (noteModel.getFavorite() == 0) ? 1 : 0;
            note.put(DatabaseHelper.NOTE_FAV, favorite);
            sqLiteDatabase.update(DatabaseHelper.TABLE_NOTE, note, "id = " + noteModel.getId(), null);
            fragment.onResume();

        });
        holder.delete.setOnClickListener(view -> {
            deleteAlertDialog((dialogInterface, i) -> {
                sqLiteDatabase.delete(DatabaseHelper.TABLE_NOTE, "id = " + noteModel.getId(), null);
                sqLiteDatabase.close();
            });
            fragment.onResume();
        });
        if (noteModel.getFavorite() == 1) {
            holder.favoriteBorder.setImageResource(R.drawable.ic_baseline_favorite_32);
        }
        holder.itemView.setOnClickListener(view -> MainActivity.noteActivityIntent(noteModel, context));
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, text, label, date;
        ImageView favoriteBorder, delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            text = itemView.findViewById(R.id.text);
            label = itemView.findViewById(R.id.label_menu);
            date = itemView.findViewById(R.id.date);
            favoriteBorder = itemView.findViewById(R.id.favoriteBorder);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
