package com.example.genet42.kubaruchan.communication;

import android.os.AsyncTask;
import android.util.Log;

import com.example.genet42.kubaruchan.statistics.Evaluation;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * わいぽーとに模型車の有効/無効を設定して評価値と模型車が緊急かどうかを取得するためのリクエスト
 */
public class WiPortRequest {

    public static final int CP_ACTIVE = 0;
    public static final int CP_EMERGENCY = 1;
    public static final int CP_EVALUATION_0 = 2;
    public static final int CP_EVALUATION_1 = 3;
    public static final int CP_LED_TEST = 8;

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
     * テスト用LEDの点灯/滅灯
     */
    private boolean ledTest = false;

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


    private class AsyncRequestTask extends AsyncTask<Void, Void, IOException> {
        @Override
        protected IOException doInBackground(Void... params) {
            try {
                // 送信と確認
                final Socket socket = new Socket(address, port);
                // 指示を送信
                Log.d("TCP", "sending...");
                command.sendTo(new Sender() {
                    @Override
                    public void send(byte[] b) throws IOException {
                        socket.getOutputStream().write(b);
                    }
                });
                Log.d("TCP", "sent");
                // 返信を確認
                Log.d("TCP", "receiving...");
                command.checkReply(new Receiver() {
                    @Override
                    public int receive(byte[] b) throws IOException {
                        return socket.getInputStream().read(b);
                    }
                });
                Log.d("TCP", "received");
                socket.close();
            } catch (IOException e) {
                return e;
            }
            return null;
        }

        public void execute(int timeout)
                throws IOException, InterruptedException, ExecutionException, TimeoutException {
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            IOException e = get(timeout, TimeUnit.MILLISECONDS);
            if (e != null) {
                throw e;
            }
        }
    }

    /**
     * WiPortのIPアドレスとリモートアドレスを指定して作成する.
     *
     * @param address IPアドレス.
     * @param port ポート番号.
     * @throws IOException 接続時にエラーが発生した場合
     */
    public WiPortRequest(InetAddress address, int port) {
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

    public void setLEDTest(boolean ledTest) {
        if (done) {
            throw new IllegalStateException("this request has already done.");
        }
        this.ledTest = ledTest;
    }

    /**
     * リクエストを送信する
     *
     * @param timeout タイムアウト [ms]
     */
    public void send(int timeout) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        if (done) {
            throw new IllegalStateException("this request has already done.");
        }
        // コマンド作成
        command.set(CP_ACTIVE, vehicleActive);
        command.set(CP_LED_TEST, ledTest);
        // 送信と確認 (失敗すると例外が投げられてこのメソッドを抜ける)
        new AsyncRequestTask().execute(timeout);
        // 値を更新 (送信と確認の成功時)
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
