package com.codepath.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    int index = 0;
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


        index = getIntent().getIntExtra("taskIndex",-2);

        String task = getIntent().getStringExtra("todoTask");
        EditText etTmp = (EditText) findViewById(R.id.etChangeText);
        etTmp.setText(task);

        boolean taskCompleteStat = getIntent().getBooleanExtra("taskCompleteStat", false);
        CheckBox taskStat = (CheckBox) findViewById(R.id.cbTaskCompleteStat);
        taskStat.setChecked(taskCompleteStat);

        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                EditText etTmp = (EditText) findViewById(R.id.etChangeText);
                CheckBox taskStat = (CheckBox) findViewById(R.id.cbTaskCompleteStat);

                i.putExtra("updatedTask", etTmp.getText().toString()); // pass updated Task
                i.putExtra("taskIndex", index); // pass index position back
                //System.out.println("Ret TaskComolete Stat:"+ taskStat.isChecked());
                i.putExtra("taskCompleteStat", ""+taskStat.isChecked()); // pass Task Status.

                setResult(RESULT_OK, i); // set result code and bundle data for response
                finish();

            }
        });
    }

}
