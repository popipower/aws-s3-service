package com.checkpro.extractor.model;

public class BucketData {
    private String name;
    private Boolean alreadyExist;
    private Boolean created;
    private Boolean isError;

    public BucketData(String name){
        this.name = name;
        this.alreadyExist = false;
        this.created = false;
        this.isError = false;
    }
    public BucketData(String name, Boolean alreadyExist, Boolean created , Boolean isError) {
        this.name = name;
        this.alreadyExist = alreadyExist;
        this.created = created;
        this.isError = isError;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getAlreadyExist() {
        return alreadyExist;
    }

    public void setAlreadyExist(Boolean alreadyExist) {
        this.alreadyExist = alreadyExist;
    }

    public Boolean getCreated() {
        return created;
    }

    public void setCreated(Boolean created) {
        this.created = created;
    }

    public Boolean getError() {
        return isError;
    }

    public void setError(Boolean error) {
        isError = error;
    }
}
