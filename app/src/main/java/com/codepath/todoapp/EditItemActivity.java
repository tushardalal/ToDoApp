package com.codepath.todoapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class EditItemActivity extends AppCompatActivity {

    private int rowIndex = 0;
    private TaskData taskData;
    private DatePickerFragment datePicker = null;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            submitUpdatedDeleteTask(MainActivity.TASK_ACTION_UPDATE);
            return true;
        }else if (id == R.id.action_delete) {
            Log.d("TD","Delete Action is called.....");
            submitUpdatedDeleteTask(MainActivity.TASK_ACTION_DELETE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

//        activity_edit_item create_item_label

        taskData = (TaskData)getIntent().getSerializableExtra(MainActivity.TASK_OBJ);
        rowIndex = getIntent().getIntExtra(MainActivity.TASK_ROW_INDEX,-1);
        //
        //NOTE:
        //Here index is Row Index of in-memory list of task (array list index)
        //Where as TaskId refers to the ID in the Database
        //
        TextView task_label = (TextView)findViewById(R.id.task_label);

        if(taskData == null) {
            //Treat it as a NEW Task
            taskData = new TaskData();
            taskData.setTaskId(-1);
        }
        if(rowIndex == -1 ) {
            task_label.setText(R.string.create_item_label);
        }else{
            task_label.setText(R.string.edit_item_label);
        }

        EditText etTmp = (EditText) findViewById(R.id.etChangeText);
        etTmp.setText(taskData.getTaskDesc());

        final CheckBox taskStat = (CheckBox) findViewById(R.id.cbTaskCompleteStat);
        taskStat.setChecked(taskData.isTaskDone());

        EditText etTaskCompleteDate = (EditText) findViewById(R.id.etTaskCompleteDate);
        etTaskCompleteDate.setText(taskData.getFormattedComplitionDate());
        etTaskCompleteDate.setCursorVisible(false);
        etTaskCompleteDate.setKeyListener(null);
        etTaskCompleteDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focusGained) {
                if(focusGained) {
                    showDatePickerDialog();
                }
            }
        });
        etTaskCompleteDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        Spinner sPriority = (Spinner)findViewById(R.id.spPriority);
        //TODO - Need to find the solution for correct implementation - to set the item selected based on 'Item Value' not based on index.
        //-------------------------------------------------------
        SpinnerAdapter sa = sPriority.getAdapter();
        for(int index=0; index < sa.getCount(); index++) {
            String itm = sa.getItem(index).toString();
            if(itm.equalsIgnoreCase(taskData.getPriority())) {
                sPriority.setSelection(index);
                break;
            }
        }
        //-------------------------------------------------------
        sPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                taskData.setPriority(adapterView.getItemAtPosition(position).toString());
                //Log.d("TD","AdapterView:" + taskData.getPriorityCode() + " - " + taskData.getPriority() );
                //Log.d("TD","AdapterView:   i:" + position + "  long:"+ id);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        /*
        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitUpdatedDeleteTask(MainActivity.TASK_ACTION_UPDATE);
            }
        });
        */
    }

    private void submitUpdatedDeleteTask(String action) {
        Intent intent = new Intent();

        //Update the TaskData object with current modified values.
        EditText etTmp = (EditText) findViewById(R.id.etChangeText);
        taskData.setTaskDesc(etTmp.getText().toString());
        CheckBox taskStat = (CheckBox) findViewById(R.id.cbTaskCompleteStat);
        if(taskStat.isChecked()) {
            taskData.setTaskStatus(TaskData.TASK_STAT_DONE);
        }else{
            taskData.setTaskStatus(TaskData.TASK_STAT_TODO);
        }

        //Set the Index and Modified TaskData into Intent.
        intent.putExtra(MainActivity.TASK_ROW_INDEX, rowIndex); // pass index position back
        intent.putExtra(MainActivity.TASK_OBJ, taskData);
        intent.putExtra(MainActivity.TASK_ACTION, action);

        setResult(RESULT_OK, intent); // set result code and bundle data for response
        finish();

    }

    protected void showDatePickerDialog() {

        if(datePicker == null) {
            DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener(){
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    Log.d("TD", "onDateSet is called.......");
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR, year);
                    cal.set(Calendar.MONTH, month);
                    cal.set(Calendar.DAY_OF_MONTH, day);
                    setSelectedDate(cal.getTime());
                }
            };
            datePicker = new DatePickerFragment();
            datePicker.setOnDateSetListener(dateListener);
        }
        datePicker.setDate(taskData.getComplitionDateTime());
        datePicker.show(getFragmentManager(), "datePicker");
    }

    /**
     * Date Picker Fragment.
     */
    public static class DatePickerFragment extends DialogFragment {

        private DatePickerDialog.OnDateSetListener dateListener;
        private Date date;
        public void setOnDateSetListener(DatePickerDialog.OnDateSetListener dateListener) {
            this.dateListener = dateListener;
        }

        public void setDate(Date date) {
            Log.d("TD","Set Date is called...." + date);
            this.date = date;
            if(date == null) {
                this.date = new Date();
                Log.d("TD", "Set Date is called....WITH NULL - Setting Date: " + date);
            }
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            Log.d("TD","onCreateDialog is called....." + cal.getTime());
            return new DatePickerDialog(getActivity(), dateListener, year, month, day);
        }
    }

    protected void setSelectedDate(Date date) {
        taskData.setComplitionDate(date);
        EditText etTaskCompleteDate = (EditText) findViewById(R.id.etTaskCompleteDate);
        etTaskCompleteDate.setText(taskData.getFormattedComplitionDate());
    }
}
