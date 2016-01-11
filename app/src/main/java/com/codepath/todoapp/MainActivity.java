package com.codepath.todoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int TODOTASK_EDIT_REQUEST_CODE = 10;

    private static final String TODOTASK_FILENAME="todoTask.txt";

    private ArrayList<TaskData> alTodoItems;
    private TaskArrayAdapter aaTodoAdapter;
//    private ArrayList<String> alTodoItems;
//    private ArrayAdapter<String> aaTodoAdapter;
    private ListView lvItems;

//    private Intent editItemIntent = new Intent(this, EditItemActivity.class);

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


        readToDoTasks();
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
                alTodoItems.remove(position);
                aaTodoAdapter.notifyDataSetChanged();
                writeToDoTasks();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //System.out.println(">>> ID:" + id);
                //System.out.println(">>> ID:" + view.getId());
                //System.out.println(">>> ID:" + view.getTag());
                TaskData td = alTodoItems.get(position);
                Intent editItemIntent = new Intent(MainActivity.this, EditItemActivity.class);
                editItemIntent.putExtra("taskIndex", position);
                editItemIntent.putExtra("todoTask", td.getTask());
                editItemIntent.putExtra("taskCompleteStat", td.isTaskDone());
                //startActivity(editItemIntent);
                startActivityForResult(editItemIntent, TODOTASK_EDIT_REQUEST_CODE);

                /*
                td.setTaskDone(!td.isTaskDone());
                aaTodoAdapter.notifyDataSetChanged();
                writeToDoTasks();
                */
            }
        });

        Button btnAddItem = (Button)findViewById(R.id.btnAddItem);
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etTmp = (EditText) findViewById(R.id.etEditText);
                aaTodoAdapter.add(new TaskData(etTmp.getText().toString()));
                aaTodoAdapter.notifyDataSetChanged();
                etTmp.setText("");
                writeToDoTasks();
            }
        });

    }

    protected void readToDoTasks() {
        /*
        ///alTodoItems.add("ToDo note 1");
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todoTask.txt");
        try {
            alTodoItems = new ArrayList<String>(FileUtils.readLines(todoFile));
            aaTodoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alTodoItems);
        }catch (IOException ioe) {
            ioe.printStackTrace();
        }
        */


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

    }

    /**
     * Write ToDoTask tasks into flat file.
     */
    protected void writeToDoTasks() {
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


        if (resultCode == RESULT_OK && requestCode == TODOTASK_EDIT_REQUEST_CODE) {
            // Extract name value from result extras
            String task = data.getExtras().getString("updatedTask");

            // Toast the name to display temporarily on screen
            Toast.makeText(this, task, Toast.LENGTH_SHORT).show();

            int index = data.getExtras().getInt("taskIndex", 0);
            TaskData td = alTodoItems.get(index);
            td.setTask(task);

            String completeStat = data.getExtras().getString("taskCompleteStat");
            //System.out.println("Task Status-1 >"+ completeStat+"<");
            //System.out.println("Task Status-2 " + Boolean.getBoolean(completeStat));
            if(completeStat != null && completeStat.equalsIgnoreCase("true")) {
                td.setTaskDone(true);
            }else{
                td.setTaskDone(false);
            }
            System.out.println("Task Status-3 " + td.isTaskDone());
            aaTodoAdapter.notifyDataSetChanged();
            writeToDoTasks();

        }
    }
}
