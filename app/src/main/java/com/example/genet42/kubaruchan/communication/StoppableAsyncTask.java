package com.example.genet42.kubaruchan.communication;

import android.os.AsyncTask;

/**
 * Created by genet42 on 2015/12/07.
 */
public abstract class StoppableAsyncTask extends AsyncTask<Void, Void, Void> {
    /**
     * true で処理が有効
     */
    private boolean isActive = true;

    /**
     * 処理が有効かどうかを返す
     *
     * @return 処理が有効なら true
     */
    protected boolean isActive() {
        return isActive;
    }

    /**
     * 処理を停止する．
     */
    public void stop() {
        isActive = false;
    }
}
