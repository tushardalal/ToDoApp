package com.codepath.todoapp;

import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.logging.Logger;

/**
 * Created by tushardalal on 1/10/16.
 */
public class TaskData implements Serializable {

    public static final String TASK_STAT_DONE = "D";
    public static final String TASK_STAT_TODO = "T";
    public static final String TASK_PRIORITY_HIGH = "H";
    public static final String TASK_PRIORITY_MEDIUM = "M";
    public static final String TASK_PRIORITY_LOW = "L";
    public static final String TASK_PRIORITY_HIGH_DESC = "High";
    public static final String TASK_PRIORITY_MEDIUM_DESC = "Medium";
    public static final String TASK_PRIORITY_LOW_DESC = "Low";
    public static final String TASK_TYPE_AUDIO = "A";
    public static final String TASK_TYPE_TEXT = "T";
    public static final String YES = "Y";
    public static final String NO = "N";
    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");

    private long taskId;
    private String taskDesc;
    private Date complitionDateTime;
    private String priority = TASK_PRIORITY_LOW;
    private String reminder = NO;
    private String taskType = TASK_TYPE_TEXT;
    private String taskAudioFileName;
    private String taskStatus = TASK_STAT_TODO;
    private String taskStatusImg;
    private String background;
    private String foreground;

    public TaskData() {
    }

    public TaskData(String task) {
        setTaskDesc(task);
    }

    public TaskData(String sTask, String sTaskStatusImg, String sBackground) {
        this.taskDesc = sTask;
        this.taskStatusImg = sTaskStatusImg;
        this.background = sBackground;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getTaskDesc() {
        if(taskDesc == null) {
            return "";
        }
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public boolean isTaskDone() {
        if(TASK_STAT_DONE.equals(taskStatus)) {
            return true;
        }
        return false;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public String getPriority() {
        return priority;
    }

    public String getPriorityCode() {
        if(TASK_PRIORITY_HIGH_DESC.equalsIgnoreCase(priority)) {
            return TASK_PRIORITY_HIGH;
        }else if(TASK_PRIORITY_MEDIUM_DESC.equalsIgnoreCase(priority)) {
            return TASK_PRIORITY_MEDIUM;
        }
        return TASK_PRIORITY_LOW;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getFormattedComplitionDateTime() {
        if(complitionDateTime != null) {
            return dateTimeFormat.format(complitionDateTime);
        }
        return "";
    }

    public void setFormattedComplitionDateTime(String formattedComplitionDate) {
        if(formattedComplitionDate != null && formattedComplitionDate.trim().length() > 0 ) {
            try {
                complitionDateTime = dateTimeFormat.parse(formattedComplitionDate);
            }catch (ParseException e) {
                complitionDateTime = Calendar.getInstance().getTime();
            }
        }
    }

    public Date getComplitionDateTime() {
        return complitionDateTime;
    }

    public void setComplitionDateTime(Date complitionDateTime) {
        this.complitionDateTime = complitionDateTime;
    }

    public void setComplitionTime(Date complitionTime) {
        if(complitionDateTime != null) {
            complitionDateTime = Calendar.getInstance().getTime();
        }
        Calendar calTime = Calendar.getInstance();
        calTime.setTime(complitionTime);

        Calendar calDateTime = Calendar.getInstance();
        calDateTime.setTime(complitionDateTime);
        calDateTime.set(Calendar.HOUR_OF_DAY, calTime.get(Calendar.HOUR_OF_DAY));
        calDateTime.set(Calendar.MINUTE, calTime.get(Calendar.MINUTE));
        complitionDateTime = calDateTime.getTime();
    }

    public String getFormattedComplitionTime() {
        if(complitionDateTime != null) {
            return timeFormat.format(complitionDateTime);
        }
        return "";
    }

    public void setComplitionDate(Date complitionDate) {
        if(complitionDateTime == null) {
            complitionDateTime = Calendar.getInstance().getTime();
        }
        Calendar calDate = Calendar.getInstance();
        calDate.setTime(complitionDate);

        Calendar calDateTime = Calendar.getInstance();
        calDateTime.setTime(complitionDateTime);
        calDateTime.set(Calendar.YEAR, calDate.get(Calendar.YEAR));
        calDateTime.set(Calendar.MONTH, calDate.get(Calendar.MONTH));
        calDateTime.set(Calendar.DAY_OF_MONTH, calDate.get(Calendar.DAY_OF_MONTH));
        complitionDateTime = calDateTime.getTime();
    }

    public String getFormattedComplitionDate() {
        if(complitionDateTime != null) {
            return dateFormat.format(complitionDateTime);
        }
        return "";
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public boolean isTaskTypeAudio() {
        if(TASK_TYPE_AUDIO.equalsIgnoreCase(taskType)) {
            return true;
        }
        return false;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskAudioFileName() {
        return taskAudioFileName;
    }

    public void setTaskAudioFileName(String taskAudioFileName) {
        this.taskAudioFileName = taskAudioFileName;
    }
    public String getTaskStatusImg() {
        return taskStatusImg;
    }

    public void setTaskStatusImg(String taskStatusImg) {
        this.taskStatusImg = taskStatusImg;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getForeground() {
        return foreground;
    }

    public void setForeground(String foreground) {
        this.foreground = foreground;
    }

    /**
     * This expects the propList string as a comma seperated filed values in certain order.
     * @param propList - comma seperated string. e.g. TaskDesc,ComplitionDate,Priority,TaskStatus,Reminder,TaskType,TaskAudioFileName
     */
    public void setProperties(String propList) {
        if(propList != null && propList.trim().length() > 0 ) {
            StringTokenizer st = new StringTokenizer(propList.trim(), ",");
            int index = 1;
            //System.out.println("PropList:" + propList+"<");
            while(st.hasMoreTokens()) {
                String prop = st.nextToken();
                //First Value is Task.
                if(index == 1) {
                    setTaskDesc(prop);
                }else if (index == 2) {
                    setFormattedComplitionDateTime(prop);
                }else if (index == 3) {
                    setPriority(prop);
                }else if (index == 4) {
                    setTaskStatus(prop);
                }else if (index == 5) {
                    setReminder(prop);
                }else if (index == 6) {
                    setTaskType(prop);
                }else if (index == 7) {
                    setTaskAudioFileName(prop);
                }else if (index == 8) {
                    setTaskStatusImg(prop);
                }else if (index == 9) {
                    setBackground(prop);
                }
                index++;
            }
        }
    }

    public String toString() {
        Log.d("TD", getTaskDesc()+","+getFormattedComplitionDateTime()+","+getPriority()+","+getTaskStatus()+","+getReminder()+","+getTaskType()+","+getTaskAudioFileName());
        return getTaskDesc()+","+getFormattedComplitionDateTime()+","+getPriority()+","+getTaskStatus()+","+getReminder()+","+getTaskType()+","+getTaskAudioFileName();
        /*
        return  "task="+getTask()+","+
                "taskdone="+isTaskDone()+","+
                "taskStatImg="+getTaskStatusImg()+","+
                "background="+getBackground();
        */
    }


}
