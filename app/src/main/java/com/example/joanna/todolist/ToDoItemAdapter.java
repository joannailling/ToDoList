package com.example.joanna.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by joanna on 8/29/15.
 */
public class ToDoItemAdapter extends ArrayAdapter<ToDoItem> {

    private static class ViewHolder {
        TextView toDoItem;
        TextView dueDate;
    }

    public ToDoItemAdapter(Context context, ArrayList<ToDoItem> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ToDoItem tdi = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_todo, parent, false);
            viewHolder.toDoItem = (TextView)convertView.findViewById(R.id.tvToDoItem);
            viewHolder.dueDate = (EditText)convertView.findViewById(R.id.etToDoDate);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        // Lookup view for data population
        //TextView tvName = (TextView) convertView.findViewById(R.id.tvToDoTask);
        //TextView tvHome = (TextView) convertView.findViewById(R.id.tvToDoDate);

        // Populate the data into the template view using the data object
        viewHolder.toDoItem.setText(tdi.getItem());
        viewHolder.dueDate.setText(tdi.getDueDate());

        // Return the completed view to render on screen
        return convertView;
    }

}

