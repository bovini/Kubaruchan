package com.example.genet42.kubaruchan.statistics;

import android.content.Context;

/**
 * Created by genet42 on 2016/01/21.
 */
public class Statistics {
    private int[] good = new int[CSVManager.MAX_DAYS];
    private int[] soso = new int[CSVManager.MAX_DAYS];
    private int[] ummm = new int[CSVManager.MAX_DAYS];
    public static final int MAX_DAYS = CSVManager.MAX_DAYS;

    public Statistics(){
    }

    public void setUmmm(int i, int ummm) {
        this.ummm[i] = ummm;
    }

    public void setGood(int i, int  good) {
        this.good[i] = good;
    }

    public void setSoso(int i, int soso) {
        this.soso[i] = soso;
    }

    public void addGood(int day) {
        this.good[day-1]++;
    }

    public void addSoso(int day) {
        this.soso[day-1]++;
    }

    public void addUmmm(int day) {
        this.ummm[day-1]++;
    }

    public int getGood(int day) {
        return good[day-1];
    }

    public int getSoso(int day) {
        return soso[day-1];
    }

    public int getUmmm(int day) {
        return ummm[day-1];
    }
}
