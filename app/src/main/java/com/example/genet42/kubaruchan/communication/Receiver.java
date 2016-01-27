package com.example.genet42.kubaruchan.communication;

import java.io.IOException;

/**
 * Created by genet42 on 2015/11/19.
 */
public interface Receiver {
    /**
     * 数バイトを受信し，それをバッファ配列 b に格納する．
     *
     * @param b データの読み込み先のバッファ
     * @return バッファに読み込まれたバイトの合計数．データがない場合は -1．
     */
    int receive(byte[] b) throws IOException;
}
