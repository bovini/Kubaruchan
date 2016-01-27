package com.example.genet42.kubaruchan.communication;

import android.util.Log;

import com.example.genet42.kubaruchan.statistics.Evaluation;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * わいぽーとに模型車の有効/無効を設定して評価値と模型車が緊急かどうかを取得するためのリクエスト
 */
public class WiPortRequest {

    public static final int CP_EMERGENCY = 0;
    public static final int CP_EVALUATION_0 = 1;
    public static final int CP_EVALUATION_1 = 2;
    public static final int CP_ACTIVE = 3;


    /**
     * WiPortのIPアドレス．
     */
    private InetAddress address;

    /**
     * WiPortのポート番号．
     */
    private int port;

    /**
     * 模型車の動作の有効/無効
     */
    private boolean vehicleActive = false;

    /**
     * このリクエストが終了しているかどうか
     */
    private boolean done = false;

    /**
     * 評価値
     */
    private Evaluation evaluation;

    /**
     * 模型車が緊急かどうか
     */
    private boolean emergency;

    /**
     * WiPortのCPに対するSet current statesのためのデータ生成器
     */
    private WiPortCommand command = new WiPortCommand();

    /**
     * WiPortのIPアドレスとリモートアドレスを指定して作成する.
     *
     * @param address IPアドレス.
     * @param port ポート番号.
     * @throws IOException 接続時にエラーが発生した場合
     */
    public WiPortRequest(InetAddress address, int port) throws IOException {
        this.address = address;
        this.port = port;
    }

    /**
     * 模型車の動作の有効/無効を指定する．
     *
     * @param vehicleActive 模型車の動作の有効/無効
     */
    public void setVehicleActive(boolean vehicleActive) {
        if (done) {
            throw new IllegalStateException("this request has already done.");
        }
        this.vehicleActive = vehicleActive;
    }

    /**
     * リクエストを送信する
     */
    public void send() throws IOException {
        if (done) {
            throw new IllegalStateException("this request has already done.");
        }
        // コマンド作成
        if (vehicleActive) {
            command.setActive(CP_ACTIVE);
        } else {
            command.setInactive(CP_ACTIVE);
        }
        // 送信と確認
        final Socket socket = new Socket(address, port);
        // 指示を送信
        Log.i("TCP", "sending...");
        command.sendTo(new Sender() {
            @Override
            public void send(byte[] b) throws IOException {
                socket.getOutputStream().write(b);
            }
        });
        Log.i("TCP", "sent");
        // 返信を確認
        Log.i("TCP", "receiving...");
        command.checkReply(new Receiver() {
            @Override
            public int receive(byte[] b) throws IOException {
                return socket.getInputStream().read(b);
            }
        });
        Log.i("TCP", "received");
        socket.close();
        emergency = command.valueAt(CP_EMERGENCY) == 1;
        int raw_eval = command.valueAt(CP_EVALUATION_0) + (command.valueAt(CP_EVALUATION_1) << 1);
        evaluation = Evaluation.toEvaluation(raw_eval);
        done = true;
    }

    /**
     * 模型車が緊急かどうかを取得
     *
     * @return 模型車が緊急かどうか
     */
    public boolean isEmergency() {
        if (!done) {
            throw new IllegalStateException("this request has not sent yet.");
        }
        return emergency;
    }

    /**
     * 評価値を取得
     *
     * @return 評価値
     */
    public Evaluation getEvaluation() {
        if (!done) {
            throw new IllegalStateException("this request has not sent yet.");
        }
        return evaluation;
    }
}
