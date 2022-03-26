package com.sametozkan.notepad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FavoritesFragment extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseHelper helper;

    public FavoritesFragment() {
        //
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        init(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.setAdapter(new NotesRecyclerViewAdapter(getActivity(), this, helper.getNotes(null, 1)));
    }

    private void init(View view) {
        helper = new DatabaseHelper(getContext(), null);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        NotesRecyclerViewAdapter notesAdapter = new NotesRecyclerViewAdapter(getActivity(), this, helper.getNotes(null, 1));
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(notesAdapter);
    }


}
