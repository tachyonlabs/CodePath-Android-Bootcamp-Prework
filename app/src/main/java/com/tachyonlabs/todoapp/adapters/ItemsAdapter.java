package com.tachyonlabs.todoapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tachyonlabs.todoapp.R;
import com.tachyonlabs.todoapp.models.Item;

import java.util.ArrayList;

public class ItemsAdapter extends ArrayAdapter<Item> {
    // adapted from http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView
    public ItemsAdapter(Context context, ArrayList<Item> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Item item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_main_listview_row, parent, false);
        }

        // Lookup view for data population
        TextView tvTodoItem = (TextView) convertView.findViewById(R.id.tvTodoItem);
        TextView tvTodoItemDate = (TextView) convertView.findViewById(R.id.tvTodoItemDate);

        // Populate the data into the template view using the data object
        tvTodoItem.setText(item.text);
        tvTodoItemDate.setText(item.date);

        // Return the completed view to render on screen
        return convertView;
    }
}
