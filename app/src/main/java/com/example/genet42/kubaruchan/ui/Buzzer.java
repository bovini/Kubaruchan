package com.example.genet42.kubaruchan.ui;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.example.genet42.kubaruchan.R;

/**
 * 模型車がやばいときにならす端末側ブザー
 * 端末がAPI 16なのでSoundPoolのコンストラクタを使用している．
 */
public class Buzzer {
    /**
     * アプリケーションのコンテキスト
     */
    private Context context;

    /**
     * 再生器
     */
    private SoundPool soundPool;

    /**
     * ブザー音声をロードしたときにもらえるID
     */
    private int idSound;

    /**
     * ブザー音声を再生したときにもらえるID
     */
    private int idStream;

    /**
     * アプリケーションのコンテキストを指定して初期化．
     *
     * @param context アプリケーションのコンテキスト
     */
    public Buzzer(Context context) {
        this.context = context;
    }

    /**
     * 音声データを読み込む
     */
    public void prepare() {
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        idSound = soundPool.load(context, R.raw.buzzer, 0);
    }

    /**
     * 音声データを解放
     */
    public void release() {
        soundPool.release();
    }

    /**
     * ブザーを鳴らす
     */
    public void play() {
        stop();
        idStream = soundPool.play(idSound, 1, 1, 0, /* loop forever */ 0, 1);
    }

    /**
     * ブザーを止める
     */
    public void stop() {
        soundPool.stop(idStream);
    }
}
