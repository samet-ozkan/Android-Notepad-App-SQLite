package com.sametozkan.notepad;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class LabelsFragment extends Fragment {

    private ExpandableListView expandableListView;
    private DatabaseHelper databaseHelper;
    private List<String> labels;
    private HashMap<String, List<NoteModel>> map;

    public LabelsFragment() {
        //
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_labels, container);
        init(view);
        return view;
    }

    @Override
    public void onResume() {
        Log.i("onResume", "onResume has been invoked.");
        super.onResume();
        expandableListView.setAdapter(createAdapter());
    }

    private LabelsExpandableListAdapter createAdapter() {
        Log.i("labels", "createAdapter has been invoked.");
        labels = databaseHelper.getLabels();
        map = new HashMap<>();
        for (String label : labels) {
            map.put(label, databaseHelper.getNotes(label, 0));
        }
        LabelsExpandableListAdapter expandableListAdapter;
        expandableListAdapter = new LabelsExpandableListAdapter(getContext(), map, labels);
        return expandableListAdapter;
    }

    private void init(View view) {
        databaseHelper = new DatabaseHelper(getContext(), null);
        expandableListView = view.findViewById(R.id.expandableListView);
        expandableListView.setAdapter(createAdapter());
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int lastExpandedPosition = -1;

            @Override
            public void onGroupExpand(int i) {
                if (lastExpandedPosition != -1 && i != lastExpandedPosition) {
                    expandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = i;
            }
        });
        expandableListView.setOnChildClickListener((expandableListView, view1, i, i1, l) -> {
            NoteModel noteModel = Objects.requireNonNull(map.get(labels.get(i))).get(i1);
            MainActivity.noteActivityIntent(noteModel, getContext());
            Log.i("listener", "onChildClickListener");
            return true;
        });
    }


}
