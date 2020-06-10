package com.example.pbs_mobile.Overview;

public class OverviewStatusDataModel {

    private String status, countPercentage;
    private int progressPercentage;

    public OverviewStatusDataModel() {}

    public OverviewStatusDataModel(String status, String countPercentage, int progressPercentage) {
        this.status = status;
        this.countPercentage = countPercentage;
        this.progressPercentage = progressPercentage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCountPercentage() {
        return countPercentage;
    }

    public void setCountPercentage(String countPercentage) {
        this.countPercentage = countPercentage;
    }

    public int getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(int progressPercentage) {
        this.progressPercentage = progressPercentage;
    }
}
