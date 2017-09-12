package com.android.acadgild.todolist.model;

/**
 * Created by Jal on 28-07-2017.
 * BEAN POJO class for getting and setting TODODATA DETAILS
 */

public class ToDoData {

    String keyTitle;
    int keyId;
    String keyDescription;
    String keyDate;
    int keyStatus;
    byte[] keyPhotoImagae;
    String id;

    public int getKeyId() {
        return keyId;
    }

    public void setKeyId(int keyId) {
        this.keyId = keyId;
    }

    public String getKeyTitle() {
        return keyTitle;
    }

    public void setKeyTitle(String keyTitle) {
        this.keyTitle = keyTitle;
    }

    public String getKeyDate() {
        return keyDate;
    }

    public void setKeyDate(String keyDate) {
        this.keyDate = keyDate;
    }
    public int getKeyStatus() {
        return keyStatus;
    }

    public void setKeyStatus(int keyStatus) {
        this.keyStatus = keyStatus;
    }

    public String getKeyDescription() {
        return keyDescription;
    }

    public void setKeyDescription(String keyDescription) {
        this.keyDescription = keyDescription;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getKeyPhotoImage() {
        return keyPhotoImagae;
    }

    public void setKeyPhotoImage(byte[] keyPhotoImagae) {
        this.keyPhotoImagae = keyPhotoImagae;
    }
}
