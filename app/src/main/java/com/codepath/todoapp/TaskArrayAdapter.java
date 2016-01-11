package com.codepath.todoapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tushardalal on 1/10/16.
 */
public class TaskArrayAdapter extends ArrayAdapter<TaskData> {

    private List<TaskData> lTaskData;
    private Context context;
    public TaskArrayAdapter(Context context, int resource, List<TaskData> objects) {
        super(context, resource, objects);
        this.context = context;
        this.lTaskData = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rView = inflater.inflate(R.layout.task_details, parent, false);
        TextView textView = (TextView) rView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rView.findViewById(R.id.logo);
        imageView.setTag("Image");
        TaskData tData = lTaskData.get(position);
        textView.setText(tData.getTask());
        /*
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("On Click happened on Image---");
            }
        });
        */

        if(tData.isTaskDone()) {
            imageView.setImageResource(R.drawable.done);
        }else {
            imageView.setImageResource(R.drawable.notdone);
        }
        //Set Background and Foreground based on Task Done or Not
        //rView.setBackgroundColor(Color.parseColor("#FFFEC7"));
        return rView;
    }

}
