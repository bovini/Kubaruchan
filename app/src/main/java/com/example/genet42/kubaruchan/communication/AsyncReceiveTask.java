package com.example.genet42.kubaruchan.communication;

/**
 * 音声を受信する非同期タスク
 */
public abstract class AsyncReceiveTask extends StoppableAsyncTask {
    public abstract byte[] getData();
}
