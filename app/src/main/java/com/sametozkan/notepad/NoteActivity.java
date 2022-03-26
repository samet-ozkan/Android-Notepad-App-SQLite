package com.sametozkan.notepad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class NoteActivity extends AppCompatActivity {

    private EditText title, text;
    private TextView label, date;
    private DatabaseHelper databaseHelper;
    private int favorite = 0;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        init();
        if (getIntent().getStringExtra("note").equals("editNote")) {
            editNote();
        }
        else if (getIntent().getStringExtra("note").equals("newNote")) {
            newNote();
        }
        else {
            Log.e("intent", "Intent fail!");
        }
    }

    private void editNote() {
        Intent intent = getIntent();
        title.setText(intent.getStringExtra("title"));
        text.setText(intent.getStringExtra("text"));
        label.setText(intent.getStringExtra("label"));
        date.setText(intent.getStringExtra("date"));
    }

    private void newNote() {
        date.setText(R.string.new_note);
    }

    private void isFavorite(Intent intent) {
        if (intent.getIntExtra("favorite", 0) == 0) {
            favorite = 0;
            menu.findItem(R.id.favorite_menu).setIcon(R.drawable.ic_baseline_favorite_border_32);
        }
        else {
            favorite = 1;
            menu.findItem(R.id.favorite_menu).setIcon(R.drawable.ic_baseline_favorite_32);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getMenuInflater().inflate(R.menu.menu_note, menu);
        this.menu = menu;
        if (getIntent().getStringExtra("note").equals("editNote")) {
            isFavorite(getIntent());
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.save_menu) {
            Log.i("save", "Save has been clicked.");
            if (getIntent().getStringExtra("note").equals("editNote")) {
                Intent intent = getIntent();
                int id = intent.getIntExtra("id", -1);
                ContentValues note = new ContentValues();
                note.put("title", title.getText().toString().trim());
                note.put("label", label.getText().toString().trim());
                note.put("favorite", favorite);
                note.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
                note.put("text", text.getText().toString().trim());
                SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
                sqLiteDatabase.update(DatabaseHelper.TABLE_NOTE, note, "id = " + id, null);
                sqLiteDatabase.close();
                finish();
            }
            else if (getIntent().getStringExtra("note").equals("newNote")) {
                databaseHelper.addNote(title.getText().toString().trim(),
                        text.getText().toString().trim(),
                        favorite,
                        label.getText().toString().trim());
                finish();
            }
            else {
                Log.e("intent", "Intent fail!");
            }
        }
        else if (itemId == R.id.delete_menu) {
            if (getIntent().getStringExtra("note").equals("editNote")) {
                deleteAlertDialog((dialogInterface, i) -> {
                    Intent intent = getIntent();
                    int id = intent.getIntExtra("id", -1);
                    SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
                    sqLiteDatabase.delete(DatabaseHelper.TABLE_NOTE, "id = " + id, null);
                    sqLiteDatabase.close();
                    finish();
                });
            }
            else if (getIntent().getStringExtra("note").equals("newNote")) {
                deleteAlertDialog((dialogInterface, i) -> finish());
            }
            else {
                Log.e("intent", "Intent fail!");
            }
        }
        else if (itemId == R.id.label_menu) {
            Log.d("label", "Label has been clicked.");
            View view = View.inflate(this, R.layout.activity_note, null);

            showLabelPopup(view, databaseHelper.getLabels());
        }
        else if (itemId == R.id.favorite_menu) {
            if (favorite == 0) {
                favorite = 1;
                item.setIcon(R.drawable.ic_baseline_favorite_32);
                Log.i("favorite", "Favorite variable is true now!");
            }
            else {
                favorite = 0;
                item.setIcon(R.drawable.ic_baseline_favorite_border_32);
                Log.i("favorite", "Favorite variable is false now!");
            }
        }
        else if (itemId == android.R.id.home) {
            finish();
        }
        else {
            Log.i("itemId", "Item id couldn't be paired!");
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAlertDialog(DialogInterface.OnClickListener listener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(NoteActivity.this);
        alertDialog.setTitle("Delete");
        alertDialog.setMessage("Are you sure you want to delete?");
        alertDialog.setPositiveButton("Delete", listener);
        alertDialog.setNegativeButton("Cancel", null);
        alertDialog.show();
    }

    private void init() {
        databaseHelper = new DatabaseHelper(NoteActivity.this, null);
        title = findViewById(R.id.title);
        text = findViewById(R.id.text);
        label = findViewById(R.id.label_menu);
        date = findViewById(R.id.date);
    }

    public void showLabelPopup(@NonNull View view, @NonNull ArrayList<String> labels) {
        View popupView = View.inflate(this, R.layout.popup_label, null);
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        AutoCompleteTextView enterLabel = popupView.findViewById(R.id.enterLabel);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(NoteActivity.this, android.R.layout.simple_list_item_1, labels);
        enterLabel.setAdapter(adapter);
        Button close = popupView.findViewById(R.id.close);
        close.setOnClickListener(view1 -> popupWindow.dismiss());
        Button selectLabel = popupView.findViewById(R.id.showLabels);
        selectLabel.setOnClickListener(view12 -> {
            PopupMenu popupMenu = new PopupMenu(NoteActivity.this, selectLabel);
            Menu menu = popupMenu.getMenu();
            for (int i = 0; i < labels.size(); i++) {
                menu.add(labels.get(i));
            }
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                enterLabel.setText(menuItem.getTitle().toString());
                return false;
            });
            popupMenu.show();
        });
        Button save = popupView.findViewById(R.id.save);
        save.setOnClickListener(view13 -> {
            label.setText(enterLabel.getText());
            popupWindow.dismiss();
        });

    }

}