package koala.com.koalaapp;

import java.util.Date;
/**
 * Created by Elgun on 10-Nov-16.
 */

public class Task {

    String taskOwner;
    String taskName;
    Date taskDue;


    public Task(String taskowner, String taskName, Date taskDue){
        this.taskDue = taskDue;
        this.taskOwner  = taskOwner;
        this.taskName = taskName;
    }
    public String getTaskOwner(){
        return  this.taskOwner;
    }
    public void setTaskOwner(String taskOwner){
        this.taskOwner = taskOwner;
    }
    public Date getTaskDue() {
        return taskDue;
    }

    public void setTaskDue(Date taskDue) {

        this.taskDue = taskDue;
    }

    public String getTaskName() {

        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public String toString() {
        return "Task name: " +  taskName + "/n Task Due: " + taskDue;
    }
}
