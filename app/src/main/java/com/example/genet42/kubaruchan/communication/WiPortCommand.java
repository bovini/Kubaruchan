package com.example.genet42.kubaruchan.communication;

import java.io.IOException;

/**
 * WiPortのシリアルポートに対する指示
 */
public class WiPortCommand {
    /**
     * 模型車の走行時間および一時停止時間のビット幅
     */
    private static final int BITS_DURATION = 9;

    /**
     * 模型車の走行時間の設定の位置
     */
    private static final int POSITION_DURATION_RUNNING = 0;

    /**
     * 模型車の一時停止時間の設定の位置
     */
    private static final int POSITION_DURATION_PAUSING = 9;

    /**
     * 模型車の全体動作のON/OFFの設定の位置
     */
    private static final int POSITION_ACTIVE = 18;

    /**
     * 送信データのビット幅
     */
    private static final int BITS_DATA = 1 + BITS_DURATION * 2;

    /**
     * 送信データのバイト数
     */
    private static final int LENGTH_DATA = (int) Math.ceil((double) BITS_DATA / Byte.SIZE);

    /**
     * 送信データ
     */
    private byte[] data = new byte[LENGTH_DATA];

    /**
     * 模型車の全体動作のON/OFFを設定する．
     *
     * @param active trueでON
     */
    public void setActive(boolean active) {
        setValue(POSITION_ACTIVE, active);
    }

    /**
     * 模型車の走行時間を設定する．
     * デフォルトで無効な値が設定される．
     *
     * @param duration 模型車の走行時間 [s]
     */
    public void setDurationRunning(int duration) {
        setDuration(POSITION_DURATION_RUNNING, duration);
    }

    /**
     * 模型車の一時停止時間を設定する．
     * デフォルトで無効な値が設定される．
     *
     * @param duration 模型車の一時停止時間 [s]
     */
    public void setDurationPausing(int duration) {
        setDuration(POSITION_DURATION_PAUSING, duration);
    }

    /**
     * 指定された位置に値を設定する．
     *
     * @param position 値を設定する位置
     * @param active true で 1，false で 0
     */
    private void setValue(int position, boolean active) {
        if (active) {
            data[position / Byte.SIZE] &= Integer.reverse(1 << position % Byte.SIZE);
        } else {
            data[position / Byte.SIZE] |= 1 << position % Byte.SIZE;
        }
    }

    /**
     * 指定された位置を先頭に値を設定する．
     *
     * @param positionBase 値を設定する先頭位置
     * @param duration 値
     */
    private void setDuration(int positionBase, int duration) {
        for (int i = 0; i < BITS_DURATION; i++) {
            setValue(positionBase + i, ((duration >>> i) & 1) == 1);
        }
    }

    /**
     * この指示を与えられたSenderに書き込む．
     *
     * @param sender 書き込み先
     * @throws IOException 入出力エラーが発生した場合
     */
    public void sendTo(Sender sender) throws IOException {
        sender.send(data);
    }
}
