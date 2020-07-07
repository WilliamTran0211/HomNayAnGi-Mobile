package com.cuongtt.homnayangi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FileResults {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("fileUrls")
    @Expose
    private ArrayList<String> fileUrls;

    public FileResults() {
    }

    public FileResults(String message, ArrayList<String> fileUrls) {
        this.message = message;
        this.fileUrls = fileUrls;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<String> getFileUrls() {
        return fileUrls;
    }

    public void setFileUrls(ArrayList<String> fileUrls) {
        this.fileUrls = fileUrls;
    }

    @Override
    public String toString() {
        return "FileResults{" +
                "message='" + message + '\'' +
                ", fileUrls=" + fileUrls +
                '}';
    }
}
