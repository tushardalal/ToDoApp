package com.codepath.todoapp;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
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

    private static final int DISPLAY_LENGTH=27;
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

        //Get the Row Data.
        TaskData tData = lTaskData.get(position);
        //View
        View rView = inflater.inflate(R.layout.task_details, parent, false);

        ImageView ivTaskStat = (ImageView) rView.findViewById(R.id.row_taskStat);
        //ivTaskStat.setTag("Image");
        if(tData.isTaskDone()) {
            ivTaskStat.setImageResource(R.drawable.done);
        }else {
            ivTaskStat.setImageResource(R.drawable.notdone);
        }

        //TextView Desc
        TextView tvTaskDesc = (TextView) rView.findViewById(R.id.row_taskDesc);
        //
        //TODO - Need to add the support to show the all text by expanding the height of the ROW in ListView
        //OR
        //By calculating the proper display width according to the screen resolution
        //
        if(tData.getTaskDesc().length() > DISPLAY_LENGTH) {
            tvTaskDesc.setText(tData.getTaskDesc().substring(0,DISPLAY_LENGTH) + "...");
        }else {
            tvTaskDesc.setText(tData.getTaskDesc());
        }
        //TextView Due Date
        TextView tvDue = (TextView) rView.findViewById(R.id.row_taskDue);
        tvDue.setText(tData.getFormattedComplitionDate());
        tvDue.setTextColor(rView.getResources().getColor(R.color.colorPrimary));
        //
        //TODO - Once the Time is implemented - open the Date Time version of method call.
        //
        //tvDue.setText(tData.getFormattedComplitionDateTime());
        //TextView Priority
        TextView tvPriority = (TextView) rView.findViewById(R.id.row_priority);
        tvPriority.setText(tData.getPriority());
        //TODO - Take the Color based on the PRIORITY
        //TODO - Introduce the Setting Activity and introduce the color.
        tvPriority.setTextColor(rView.getResources().getColor(R.color.colorRed));
        //tvPriority.setText(tData.getPriorityCode());

        //ImageView - Audio Task
        ImageView ivAudioTask = (ImageView) rView.findViewById(R.id.row_audio);
        if(tData.isTaskTypeAudio()) {
            ivAudioTask.setImageResource(R.drawable.audioplay);
        }
        /*
        //Idea was to make check box 'Checked/UnChecked' by just clicking on checkbox image.
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("On Click happened on Image---");
            }
        });
        */

        //Set Background and Foreground based on Task Done or Not
        //rView.setBackgroundColor(Color.parseColor("#FFFEC7"));
        return rView;
    }

}
