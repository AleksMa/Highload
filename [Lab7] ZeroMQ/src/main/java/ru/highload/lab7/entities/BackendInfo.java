package ru.highload.lab7.entities;

public class BackendInfo {
    private String address;
    private int min;
    private int max;

    public BackendInfo(String address, int min, int max) {
        this.address = address;
        this.min = min;
        this.max = max;
    }

    public String getAddress() {
        return address;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
