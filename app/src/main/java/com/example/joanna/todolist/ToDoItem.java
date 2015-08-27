package com.example.joanna.todolist;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by joanna on 8/26/15.
 */
public class ToDoItem implements Serializable{
    private long id;
    private String item;
    private String dueDate;
    private int priority;

    public ToDoItem(String item, String dueDate, int priority) {
        this.item = item;
        this.dueDate = dueDate;
        this.priority = priority;
    }

    public ToDoItem() {
        this.item = "";
        this.dueDate = "";
        this.priority = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }


}
