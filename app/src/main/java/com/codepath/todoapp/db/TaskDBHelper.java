package com.codepath.todoapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.codepath.todoapp.TaskData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tushar on 1/13/2016.
 */
public class TaskDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tododb.db";
    private static final String TABLE_TASK = "TASKS";

    private static final String COL_TASK_ID = "TASK_ID";                         // Task ID
    private static final String COL_TASK_TASK_DESC = "TASK";                     // Task Description
    private static final String COL_TASK_COMPLITION_DATE = "COMPLITION_DATE";    // Date Time To Complete Task
    private static final String COL_TASK_PRIORITY = "PRIORITY";                  // H - High, M - Medium, L - Low
    private static final String COL_TASK_STATUS = "STATUS";                      // D - Done, T - TODO
    private static final String COL_TASK_REMINDER = "REMINDER";                  // Y - Yes, N - No
    private static final String COL_TASK_TYPE = "TASK_TYPE";                     // A - Audio, T - Text
    private static final String COL_TASK_AUDIO_FILE = "TASK_AUDIO_FILE";         // Audio File name

    private static final String CREATE_TASK_TABLE_SQL =
            " CREATE TABLE " +TABLE_TASK+
            "( "+COL_TASK_ID+" INTEGER PRIMARY KEY, "+
                COL_TASK_TASK_DESC+" TEXT, "+
                COL_TASK_COMPLITION_DATE+" TEXT, "+
                COL_TASK_PRIORITY+" CHAR(1), "+
                COL_TASK_STATUS+" CHAR(1), "+
                COL_TASK_REMINDER+" CHAR(1), "+
                COL_TASK_TYPE+" CHAR(1), "+
                COL_TASK_AUDIO_FILE+" CHAR(1) "+
                ")";

    private static final String DROP_TASK_TABLE_SQL = "DROP TABLE IF EXISTS "+TABLE_TASK;

    private static final String COLUMN_LIST =
            COL_TASK_ID+", "+
            COL_TASK_TASK_DESC+", "+
            COL_TASK_COMPLITION_DATE+", "+
            COL_TASK_PRIORITY+", "+
            COL_TASK_STATUS+", "+
            COL_TASK_REMINDER+", "+
            COL_TASK_TYPE+", "+
            COL_TASK_AUDIO_FILE;

    private static final String SELECT_ALL_SQL = "SELECT "+COLUMN_LIST+" FROM "+TABLE_TASK;
    private static final String SELECT_BY_ID_SQL = "SELECT "+COLUMN_LIST+" FROM "+TABLE_TASK+" WHERE "+COL_TASK_ID+ "= ?";
    private static final String DELETE_BY_ID_SQL = "DELETE FROM "+TABLE_TASK+" WHERE "+COL_TASK_ID+ "= ?";

    public TaskDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqDB) {
        Log.d("[TD]","Going to create the DATABAE TABLE");
        Log.d("[TD]", "SQL=" + CREATE_TASK_TABLE_SQL);
        sqDB.execSQL(CREATE_TASK_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqDB, int i, int i1) {
        Log.d("[TD]","Going to RE-CREATE the DATABAE TABLE");
        Log.d("[TD]","SQL="+DROP_TASK_TABLE_SQL);
        sqDB.execSQL(DROP_TASK_TABLE_SQL);
        onCreate(sqDB);
    }

    public long insert(TaskData td) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = createContentValue(td);
        long rowId = db.insert(TABLE_TASK, null, cv);

        Log.d("[TD]", "Inserted "+td.toString());
        Log.d("[TD]", "Inserted Stat:" + rowId);
        Log.d("[TD]", ""+getById(1));
        td.setTaskId(rowId);
        return rowId;
    }

    public boolean update(TaskData td) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = createContentValue(td);
        String args[] = {""+td.getTaskId()};
        int updateRowCount = db.update(TABLE_TASK, cv, COL_TASK_ID + " = ?", args);
        Log.d("[TD]", "Inserted " + td.toString());
        Log.d("[TD]", "Inserted Stat:" + updateRowCount);
        Log.d("[TD]", ""+getById(td.getTaskId()));
        return true;
    }

    public boolean delete(long taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String args[] = {""+taskId};
        int deleteRowCount = db.delete(TABLE_TASK, COL_TASK_ID + " = ?", args);
//        int deleteRowCount = db.delete(TABLE_TASK, "", null);
        Log.d("[TD]", "Delete CALLED for: " + taskId);
        Log.d("[TD]", "Deleted RowCount : " + deleteRowCount);
        Log.d("[TD]", ""+getById(taskId));
        return true;
    }


    public TaskData getById(long id){
        TaskData td = null;
        SQLiteDatabase db = this.getReadableDatabase();

        //String SQL = SELECT_ALL_SQL+" WHERE "+COL_TASK_ID+ " = "+id+"";
        Log.d("[TD]",SELECT_BY_ID_SQL+ " -> "+id);
        //Cursor rs =  db.rawQuery( "select * from "+TASK_TABLE_NAME+" WHERE "+COL_TASK_ID+ " = "+id+"", null );
        String args[] = {""+id};
        Cursor cursor =  db.rawQuery( SELECT_BY_ID_SQL, args );


        /*
        Log.d("[TD]", "TOTAL RECORDS:" + cursor.getCount());
        Log.d("[TD]", "TOTAL COLS:" + cursor.getColumnCount());
        for(String s: cursor.getColumnNames()) {
            Log.d("TD", "COLS:" + s);
        }
        Log.d("[TD]", "COL_TASK_ID:" + cursor.getColumnIndex(COL_TASK_ID));
        Log.d("[TD]", "COL_TASK_TASK_DESC:" + cursor.getColumnIndex(COL_TASK_TASK_DESC));
        Log.d("[TD]", "COL_TASK_COMPLITION_DATE:" + cursor.getColumnIndex(COL_TASK_COMPLITION_DATE));
        Log.d("[TD]", "COL_TASK_PRIORITY:" + cursor.getColumnIndex(COL_TASK_PRIORITY));
        Log.d("[TD]", "COL_TASK_STATUS:" + cursor.getColumnIndex(COL_TASK_STATUS));
        Log.d("[TD]", "COL_TASK_REMINDER:" + cursor.getColumnIndex(COL_TASK_REMINDER));
        Log.d("[TD]", "COL_TASK_TYPE:" + cursor.getColumnIndex(COL_TASK_TYPE));
        Log.d("[TD]", "COL_TASK_AUDIO_FILE:" + cursor.getColumnIndex(COL_TASK_AUDIO_FILE));
        */

        if(cursor.moveToFirst()) {
            td = createTaskData(cursor);
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
        Log.d("[TD]", "TaskData:" + td);
        return td;
    }

    public ArrayList<TaskData> getAll() {
        ArrayList<TaskData> alRetData = new ArrayList<TaskData>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( SELECT_ALL_SQL, null );

        cursor.moveToFirst();
        while(cursor.isAfterLast() == false){
            alRetData.add(createTaskData(cursor));
            cursor.moveToNext();
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return alRetData;
    }

    protected TaskData createTaskData(Cursor cursor) {
        TaskData td = new TaskData();
        td.setTaskId(cursor.getLong(0));
        td.setTaskDesc(cursor.getString(1));
        td.setFormattedComplitionDateTime(cursor.getString(2));
        td.setPriority(cursor.getString(3));
        td.setTaskStatus(cursor.getString(4));
        td.setReminder(cursor.getString(5));
        td.setTaskType(cursor.getString(6));
        td.setTaskAudioFileName(cursor.getString(7));
        return td;
    }
    protected ContentValues createContentValue(TaskData td) {
        ContentValues cv = new ContentValues();
        cv.put(COL_TASK_TASK_DESC, td.getTaskDesc());
        cv.put(COL_TASK_COMPLITION_DATE, td.getFormattedComplitionDateTime());
        cv.put(COL_TASK_PRIORITY, td.getPriority());
        cv.put(COL_TASK_STATUS, td.getTaskStatus());
        cv.put(COL_TASK_REMINDER, td.getReminder());
        cv.put(COL_TASK_TYPE, td.getTaskType());
        cv.put(COL_TASK_AUDIO_FILE, td.getTaskAudioFileName());
        return cv;
    }
}
