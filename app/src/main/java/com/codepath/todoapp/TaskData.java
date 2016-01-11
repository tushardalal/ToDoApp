package com.codepath.todoapp;

import java.util.StringTokenizer;

/**
 * Created by tushardalal on 1/10/16.
 */
public class TaskData {

    private String taskStatusImg;
    private String background;
    private String foreground;
    private String task;
    private boolean taskDone = false;

    public TaskData() {
    }

    public TaskData(String task) {
        setTask(task);
    }

    public TaskData(String sTask, String sTaskStatusImg, String sBackground) {
        this.task = sTask;
        this.taskStatusImg = sTaskStatusImg;
        this.background = sBackground;
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

    public String getTask() {
        if(task == null) {
            return "Something Wrong happened.";
        }
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public boolean isTaskDone() {
        return taskDone;
    }

    public void setTaskDone(boolean taskDone) {
        this.taskDone = taskDone;
    }

    public void setProperties(String propList) {
        if(propList != null && propList.trim().length() > 0 ) {
            StringTokenizer st = new StringTokenizer(propList.trim(), ",");
            int index = 1;
            //System.out.println("PropList:" + propList+"<");
            while(st.hasMoreTokens()) {
                String prop = st.nextToken();
                //First Value is Task.
                if(index == 1) {
                    setTask(prop);
                }else if (index == 2) {
                    //setTaskDone(Boolean.getBoolean(prop));
                    if("TRUE".equalsIgnoreCase(prop)) {
                        setTaskDone(true);
                    }else {
                        setTaskDone(false);
                    }
                }else if (index == 3) {
                    setTaskStatusImg(prop);
                }else if (index == 4) {
                    setBackground(prop);
                }
                index++;
            }
        }
    }

    public String toString() {

        return getTask()+","+((isTaskDone())?"TRUE":"FALSE")+","+getTaskStatusImg()+","+getBackground();
        /*
        return  "task="+getTask()+","+
                "taskdone="+isTaskDone()+","+
                "taskStatImg="+getTaskStatusImg()+","+
                "background="+getBackground();
        */
    }


}
