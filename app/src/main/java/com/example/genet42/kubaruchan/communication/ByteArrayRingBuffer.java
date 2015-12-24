package com.example.genet42.kubaruchan.communication;

import android.util.Log;

import java.util.Arrays;

/**
 * 配列を用いたバイトリングバッファ
 */
public class ByteArrayRingBuffer {
    /**
     * 要素の配列
     */
    private byte[] elements;

    /**
     * バッファの容量
     */
    private int capacity;

    /**
     * データの範囲が配列の端をまたぐとき true
     */
    private boolean wrappedAround = false;

    /**
     * 次に要素を追加する位置
     */
    private int tail = 0;

    /**
     * バッファの要領を指定してリングバッファを作成する．
     *
     * @param capacity バッファの容量
     */
    public ByteArrayRingBuffer(int capacity) {
        this.capacity = capacity;
        elements = new byte[capacity];
    }

    /**
     * 与えられた byte 配列をバッファに追加する
     *
     * @param elements 追加元の配列
     */
    public synchronized void offer(byte[] elements) {
        offer(elements, 0, elements.length);
    }

    /**
     * 与えられた byte 配列の先頭からから指定された数の要素をバッファに追加する
     *
     * @param elements 追加元の配列
     * @param toIndex 範囲の終わり（このインデックスの要素は追加されない）
     */
    public synchronized void offer(byte[] elements, int toIndex) {
        offer(elements, 0, toIndex);
    }

    /**
     * 与えられた byte 配列の指定された範囲をバッファに追加する
     *
     * @param elements 追加元の配列
     * @param fromIndex 範囲の始まり
     * @param toIndex 範囲の終わり（このインデックスの要素は追加されない）
     */
    public synchronized void offer(byte[] elements, int fromIndex, int toIndex) {
        int length = toIndex - fromIndex;
        // Log.i("ByteArrayRingBuffer", "offered length: " + length);
        // Log.i("ByteArrayRingBuffer", "stored: " + (wrappedAround ? capacity + " (wrapped)" : tail) + " (" + 100 * (wrappedAround ? capacity : tail) / capacity + " %)");
        // length <= capacity を保証
        if (length > capacity) {
            offer(elements, toIndex - capacity, toIndex);
            return;
        }
        int capacityTail = capacity - tail;
        // Log.i("ByteArrayRingBuffer", String.format("arraycopy: [%d, %d) -> [%d, %d)", fromIndex, fromIndex + Math.min(toIndex, capacityTail), tail, tail + Math.min(toIndex, capacityTail)));
        try {
            System.arraycopy(elements, fromIndex, this.elements, tail, Math.min(toIndex, capacityTail));
        } catch (Exception e) {
            Log.i("ByteArrayRingBuffer", "toIndex: " + toIndex);
            throw e;
        }
        tail += length;
        if (length > capacityTail) {
            System.arraycopy(elements, fromIndex + capacityTail, this.elements, 0, length - capacityTail);
            tail = length - capacityTail;
            wrappedAround = true;
        }
    }

    /**
     * バッファのすべての内容を取得する．バッファの内容は消去される．
     *
     * @return バッファすべてのの内容
     */
    public synchronized byte[] pollAll() {
        // Log.i("ByteArrayRingBuffer", "polled length: " + (wrappedAround ? capacity + " (wrapped)" : tail));
        if (!wrappedAround) {
            byte[] result = Arrays.copyOf(elements, tail);
            reset();
            return result;
        }
        byte[] result = new byte[capacity];
        System.arraycopy(elements, tail, result, 0, capacity - tail);
        System.arraycopy(elements, 0, result, capacity - tail, tail);
        reset();
        return result;
    }

    /**
     * バッファの内容を消去して初期状態にする
     */
    private void reset() {
        elements = new byte[capacity];
        tail = 0;
        wrappedAround = false;
    }
}
