package com.example.genet42.kubaruchan.statistics;

import android.content.Context;

/**
 * Created by genet42 on 2016/01/21.
 */
public class Statistics {
    private int[] good = new int[CSVManager.MAX_DAYS];
    private int[] soso = new int[CSVManager.MAX_DAYS];
    private int[] ummm = new int[CSVManager.MAX_DAYS];

    public Statistics(){
    }


    public void addGood(int day, int umai) {
        this.good[day]= umai;
    }

    public void addSoso(int day, int soso) {
        this.soso[day]= soso;
    }

    public void addUmmm(int day, int ummm) {
        this.ummm[day]= ummm;
    }

    public int[] getGood() {
        return good;
    }

    public int[] getSoso() {
        return soso;
    }

    public int[] getUmmm() {
        return ummm;
    }
}
