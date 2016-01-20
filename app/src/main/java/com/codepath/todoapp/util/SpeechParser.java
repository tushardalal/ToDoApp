package com.codepath.todoapp.util;

import com.codepath.todoapp.TaskData;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by tushar on 1/19/2016.
 */
public class SpeechParser {

    public static TaskData processAudioInput(String taskMsg) {

        TaskData taskData = new TaskData();
        taskData.setTaskId(-1);

        String text = null;
        boolean dateFound = false;

        //---[Start]-Date Check------------------------------------------------
        //
        //NOTE:
        //TODO
        //This temporary logic - not fully optimized
        //If this idea is going to be correct - need to develop OPTIMIZED logic for this.
        //
        Calendar cal = Calendar.getInstance();
        int todaysDay = cal.get(Calendar.DAY_OF_WEEK);    //SUNDAY = 1, ... SATURDAY = 7
        int index = -1;
        int daysToAdd = 0;
        if(taskMsg == null) {
            text = "";
        }else{
            text = taskMsg.toUpperCase();
        }
        System.out.println("text:>" + text +"<");

        if(text.indexOf("TODAY") >= 0) {
            dateFound = true;
        }else if( ( index = text.indexOf("DAY AFTER TOMORROW") ) >= 0) {
            dateFound = true;
            cal.add(Calendar.DAY_OF_MONTH, 2);
        }else if( ( index = text.indexOf("TOMORROW") ) >= 0) {
            dateFound = true;
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }else if( ( index = text.indexOf("SUNDAY") ) >= 0) {
            dateFound = true;
            daysToAdd = Calendar.SUNDAY - todaysDay;
        }else if( ( index = text.indexOf("MONDAY") ) >= 0) {
            dateFound = true;
            daysToAdd = Calendar.MONDAY - todaysDay;
        }else if( ( index = text.indexOf("TUESDAY") ) >= 0) {
            dateFound = true;
            daysToAdd = Calendar.TUESDAY - todaysDay;
        }else if( ( index = text.indexOf("WEDNESDAY") ) >= 0) {
            dateFound = true;
            daysToAdd = Calendar.WEDNESDAY - todaysDay;
        }else if( ( index = text.indexOf("THURSDAY") ) >= 0) {
            dateFound = true;
            daysToAdd = Calendar.THURSDAY - todaysDay;
        }else if( ( index = text.indexOf("FRIDAY") ) >= 0) {
            dateFound = true;
            daysToAdd = Calendar.FRIDAY - todaysDay;
        }else if( ( index = text.indexOf("SATURDAY") ) >= 0) {
            dateFound = true;
            daysToAdd = Calendar.SATURDAY - todaysDay;
        }

        if(dateFound) {
            if(daysToAdd == 0 ) {
                //Today - is same day in the text - So if the next word found then ADD one Week (7 Days)
                if(index > 0 && text.substring(0,index).indexOf("NEXT") >= 0 ) {
                    daysToAdd+=7;
                }
            } else if(daysToAdd < 0 ) {
                //day already passed - so
                daysToAdd+=7;
            }
            cal.add(Calendar.DAY_OF_MONTH, daysToAdd);
            taskData.setComplitionDate(cal.getTime());
        }
        //---[ End ]-Date Check------------------------------------------------

        if(text.indexOf("PRIORITY") >= 0) {
            if(text.indexOf("HIGH") >= 0 ) {
                taskData.setPriority("High");
            }else if(text.indexOf("MEDIUM") >= 0 ) {
                taskData.setPriority("Medium");
            }else if(text.indexOf("LOW") >= 0 ) {
                taskData.setPriority("Low");
            }
        }
        taskData.setTaskDesc(text);
        return taskData;
    }

}
