package com.sametozkan.notepad;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LabelsExpandableListAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private final Map<String, List<NoteModel>> collection;
    private final List<String> labels;

    public LabelsExpandableListAdapter(Context context, Map<String, List<NoteModel>> collection, List<String> labels) {
        this.context = context;
        this.collection = collection;
        this.labels = labels;
    }

    @Override
    public int getGroupCount() {
        return labels.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return Objects.requireNonNull(collection.get(labels.get(i))).size();
    }

    @Override
    public Object getGroup(int i) {
        return labels.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return Objects.requireNonNull(collection.get(labels.get(i))).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        Log.i("labels", "getGroupView has been invoked.");
        String label = labels.get(i) + " (" + Objects.requireNonNull(collection.get(labels.get(i))).size() + ")";

        if (view == null) {
            view = View.inflate(context, R.layout.item_labels_group, null);
        }
        TextView labelView = view.findViewById(R.id.label);
        labelView.setText(label);

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        Log.i("labels", "getChildView has been invoked.");

        if (view == null) {
            view = View.inflate(context, R.layout.item_labels_child, null);
        }

        NoteModel noteModel = Objects.requireNonNull(collection.get(labels.get(i))).get(i1);
        TextView title = view.findViewById(R.id.title);
        TextView date = view.findViewById(R.id.date);

        title.setText(noteModel.getTitle());
        date.setText(noteModel.getDate());

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
