package com.checkpro.extractor.model;

public class ObjectData {

    private String signedUrl;
    private String fileName;
    private String transactionId;
    private String userId;

    public ObjectData(){

    }
    public ObjectData(String signedUrl, String fileName, String transactionId, String userId) {
        this.signedUrl = signedUrl;
        this.fileName = fileName;
        this.transactionId = transactionId;
        this.userId = userId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSignedUrl() {
        return signedUrl;
    }

    public void setSignedUrl(String signedUrl) {
        this.signedUrl = signedUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}

