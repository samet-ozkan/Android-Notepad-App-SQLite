package com.sametozkan.notepad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private MainViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setViewPager();
    }

    private void setViewPager() {
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                Log.i("tabLayout", tab.getText() + " has been selected.");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.i("tab", "Unselected tab: " + tab.getText());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.i("tab", "Reselected tab: " + tab.getText());
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }

    public static void noteActivityIntent(@NonNull NoteModel noteModel, Context context) {
        int id = noteModel.getId();
        String title = noteModel.getTitle();
        String text = noteModel.getText();
        String label = noteModel.getLabel();
        String date = noteModel.getDate();
        int favorite = noteModel.getFavorite();
        Intent intent = new Intent(context, NoteActivity.class).putExtra("note", "editNote");
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        intent.putExtra("text", text);
        intent.putExtra("label", label);
        intent.putExtra("date", date);
        intent.putExtra("favorite", favorite);
        context.startActivity(intent);
    }

    private void init() {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        viewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), getLifecycle(), tabLayout.getTabCount());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.addNote) {
            startActivity(new Intent(this, NoteActivity.class).putExtra("note", "newNote"));
            Log.i("addNote", "addNote has been clicked.");
        }
        return super.onOptionsItemSelected(item);
    }
}