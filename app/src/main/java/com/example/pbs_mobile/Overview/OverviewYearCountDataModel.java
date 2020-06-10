package com.example.pbs_mobile.Overview;

public class OverviewYearCountDataModel {

    private String year, count;

    public OverviewYearCountDataModel() {}

    public OverviewYearCountDataModel(String year, String count) {
        this.year = year;
        this.count = count;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
