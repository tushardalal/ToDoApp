package com.codepath.todoapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.todoapp.db.TaskDBHelper;
import com.codepath.todoapp.util.SpeechParser;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public static final int TODOTASK_EDIT_REQUEST_CODE = 10;
    public static final int TODOTASK_AUDIO_CAPTURE_REQUEST_CODE = 20;
    public static final String TASK_ROW_INDEX = "TASK_ROW_INDEX";
    public static final String TASK_OBJ = "TASK_OBJ";
    public static final String TASK_ACTION = "TASK_ACTION";
    public static final String TASK_ACTION_DELETE = "TASK_ACTION_DELETE";
    public static final String TASK_ACTION_UPDATE = "TASK_ACTION_UPDATE";
    private static final String TODOTASK_FILENAME="todoTask.txt";

    private ArrayList<TaskData> alTodoItems;
    private TaskArrayAdapter aaTodoAdapter;
    private ListView lvItems;
    //private TaskDBHelper taskHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        //taskHelper = new TaskDBHelper(this);
        //loadAllTaskFromFile();
        loadAllTask();
        if(alTodoItems == null) {
            //alTodoItems = new ArrayList<String>();
            alTodoItems = new ArrayList<TaskData>();
            //aaTodoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alTodoItems);
            aaTodoAdapter = new TaskArrayAdapter(this, R.layout.task_details, alTodoItems);
        }

        lvItems = (ListView)findViewById(R.id.lvItems);
        lvItems.setAdapter(aaTodoAdapter);
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteTaskByConfirmation(alTodoItems.get(position));

//                TaskData tdToDelete = alTodoItems.remove(position);
//                aaTodoAdapter.notifyDataSetChanged();
//                //writeToDoTasksToFile(null); //Delete TASK.
//                deleteTask(tdToDelete);
//                return true;
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addOrEditTask(position);
            }
        });

        Button btnAddTask = (Button)findViewById(R.id.btnAddTask);
        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                EditText etTmp = (EditText) findViewById(R.id.etEditText);
                TaskData td = new TaskData(etTmp.getText().toString());
                aaTodoAdapter.add(td);
                aaTodoAdapter.notifyDataSetChanged();
                etTmp.setText("");
                //writeToDoTasksToFile(td);
                insertTask(td);
                */
                addOrEditTask(-1);
            }
        });

        final Button btnAddAudioTask = (Button)findViewById(R.id.btnAddAudioTask);
        btnAddAudioTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrEditAudioTask(-1);
            }
        });
    }

    protected void loadAllTaskFromFile() {

        //--[Start]--File Based Persistence -------------------
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, TODOTASK_FILENAME);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(todoFile));
            alTodoItems = new ArrayList<TaskData>();
            while(true) {
                String task = reader.readLine();
                if(task == null) {
                    break;
                }
                TaskData td = new TaskData();
                td.setProperties(task);
                alTodoItems.add(td);
            }
            aaTodoAdapter = new TaskArrayAdapter(this,R.layout.task_details,alTodoItems);
        }catch (IOException ioe) {
            ioe.printStackTrace();
        }
        //--[ End ]--File Based Persistence -------------------
    }

    /**
     * Write ToDoTask tasks into flat file.
     */
    protected void writeToDoTasksToFile(TaskData td) {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, TODOTASK_FILENAME);
        try {
            FileUtils.writeLines(todoFile, alTodoItems);
        }catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TD", "RESULT_CODE: " + resultCode + "  REQUEST_CODE: " + requestCode);
        if (resultCode == RESULT_OK && requestCode == TODOTASK_EDIT_REQUEST_CODE) {
            // Extract the Index & TaskData object from Response
            int index = data.getExtras().getInt(TASK_ROW_INDEX, 0);
            TaskData td = (TaskData)data.getSerializableExtra(TASK_OBJ);

            String action = data.getExtras().getString(TASK_ACTION);
            if(TASK_ACTION_DELETE.equals(action)) {
                if (index >= 0 && td.getTaskId() >= 0) {
                    deleteTaskByConfirmation(alTodoItems.get(index));
                }else{
                    Log.d("TD","Invalid request for delete.");
                }
            }else if(TASK_ACTION_UPDATE.equals(action)) {
                // Toast the name to display temporarily on screen
                Toast.makeText(this, td.getTaskDesc(), Toast.LENGTH_SHORT).show();

                //File Based persistent.
                //writeToDoTasksToFile(td);

                if (index == -1 || td.getTaskId() == -1) {
                    //ADD the Data into ArrayList (List Model)
                    aaTodoAdapter.add(td);
                    insertTask(td);
                } else {
                    //Update the Data into ArrayList (List Model)
                    alTodoItems.set(index, td);
                    updateTask(td);
                }
            }
            //Notify ListView for Data Changed
            aaTodoAdapter.notifyDataSetChanged();
        }else if(resultCode == RESULT_OK && requestCode == TODOTASK_AUDIO_CAPTURE_REQUEST_CODE) {
            if(data != null) {
                ArrayList<String> alAudioToTextTask = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                TaskData taskData = processAudioInput(alAudioToTextTask);
                //taskData.setTaskDesc(alAudioToTextTask.get(0));
                Log.d("TD", "Audio Data FULL: " + alAudioToTextTask);
                Log.d("TD", "Audio Data: " + alAudioToTextTask.get(0) );

                //Call the Add Task activity for other details to capture.
                Intent editItemIntent = new Intent(MainActivity.this, EditItemActivity.class);
                editItemIntent.putExtra(TASK_ROW_INDEX, -1);
                editItemIntent.putExtra(TASK_OBJ, taskData);
                startActivityForResult(editItemIntent, TODOTASK_EDIT_REQUEST_CODE);
            }
        }
    }

    protected TaskData processAudioInput(ArrayList<String> alAudioToTextTask) {
        String text = alAudioToTextTask.get(0);
        Log.d("TD","Text:>"+text+"<");
        TaskData taskData = SpeechParser.processAudioInput(text);
        taskData.setTaskId(-1);
        return taskData;
    }

    protected void loadAllTask() {
        //Write Task in DB as well.
        TaskDBHelper  taskHelper = new TaskDBHelper(this);
        alTodoItems = taskHelper.getAll();
        taskHelper.close();
        aaTodoAdapter = new TaskArrayAdapter(this,R.layout.task_details,alTodoItems);
    }

    protected void insertTask(TaskData td) {
        //Write Task in DB as well.
        TaskDBHelper  taskHelper = new TaskDBHelper(this);
        long stat = taskHelper.insert(td);
        taskHelper.close();
    }

    protected void updateTask(TaskData td) {
        //Delete the Task from database
        TaskDBHelper  taskHelper = new TaskDBHelper(this);
        taskHelper.update(td);
        taskHelper.close();
    }

    protected void addOrEditTask(int position) {
        //If the record position is -1 then ADD Mode OR it is EDIT Mode
        Intent editItemIntent = new Intent(MainActivity.this, EditItemActivity.class);
        editItemIntent.putExtra(TASK_ROW_INDEX, position);

        if(position >= 0) {
            TaskData td = alTodoItems.get(position);
            if (td != null) {
                editItemIntent.putExtra(TASK_OBJ, td);
            }
        }
        //startActivity(editItemIntent);
        startActivityForResult(editItemIntent, TODOTASK_EDIT_REQUEST_CODE);
    }

    protected void addOrEditAudioTask(int position) {
        //Toast.makeText(this, "Add the new Task by talking", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
        try {
            startActivityForResult(intent, TODOTASK_AUDIO_CAPTURE_REQUEST_CODE);
            //txtText.setText("");
        } catch (ActivityNotFoundException a) {
            Toast t = Toast.makeText(getApplicationContext(), "Opps! Your device doesn't support Speech to Text", Toast.LENGTH_SHORT);
            t.show();
        }
    }

    protected void deleteTaskByConfirmation(TaskData td) {
        final TaskData tdToDelete = td;

        AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(this);
        dlgBuilder.setTitle(R.string.deleteTaskTitle);
        dlgBuilder.setMessage(R.string.deleteTaskMsg);
        dlgBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteTask(tdToDelete);
                dialogInterface.dismiss();
            }
        });
        dlgBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dlgBuilder.show();

    }

    protected void deleteTask(TaskData td) {
        //Remove Task from UI
        alTodoItems.remove(td);
        aaTodoAdapter.notifyDataSetChanged();

        //Delete the Task from database
        TaskDBHelper  taskHelper = new TaskDBHelper(this);
        taskHelper.delete(td.getTaskId());
        taskHelper.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //if( taskHelper != null ) {
        //    taskHelper.close();
        //}
    }
}
