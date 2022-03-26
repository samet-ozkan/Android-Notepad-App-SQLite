package com.sametozkan.notepad;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MainViewPagerAdapter extends FragmentStateAdapter {

    private final int tabItemCount;

    public MainViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, int tabItemCount) {
        super(fragmentManager, lifecycle);
        this.tabItemCount = tabItemCount;

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                Log.i("fragment", "NotesFragment has created.");
                return new NotesFragment();
            case 1:
                Log.i("fragment", "LabelsFragment has created.");
                return new LabelsFragment();
            default:
                return new FavoritesFragment();
        }
    }

    @Override
    public int getItemCount() {
        return tabItemCount;
    }

}
