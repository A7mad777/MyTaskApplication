package com.example.future.mytask.Model;

import android.renderscript.BaseObj;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataModel {
    private String note_id;
    private String name;
    private String full_name;
    private String description;

    public DataModel(String note_id, String name, String full_name, String description) {
        this.note_id = note_id;
        this.name = name;
        this.full_name = full_name;
        this.description = description;
    }

    public String getNote_id() {
        return note_id;
    }

    public void setNote_id(String note_id) {
        this.note_id = note_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}