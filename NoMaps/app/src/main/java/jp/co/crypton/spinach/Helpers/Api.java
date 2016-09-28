package jp.co.crypton.spinach.Helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;

import jp.co.crypton.spinach.constants.Constants;
import jp.co.crypton.spinach.model.TopInfo;

/**
 * データ同期API
 *
 * @author aso
 */
public class Api extends AsyncTask<String, Void, String[]> {

    /**
     * タスク
     */
    private int task;
    /**
     * タグ
     */
  private static final String TAG = "Api";

    /**
     * コンテキスト
     */
    public Context context;
    /**
     * コールバック
     */
    private ApiCallback callback;
    /**
     * リクエストコード
     */
    public int requestCode;
    /**
     * オブジェクト
     */
    public Object obj;
    /**
     * レスポンスコード
     */
    public int respCode;
    /**
     * 通信時タイムアウト
     */
    public static final int CONNECTION_TIMEOUT = 10000;

    /**
     * UUID
     */
    public String uuid;
    /**
     * 言語
     */
    public String language;
    /**
     * トークン
     */
    public String token;
    /**
     * アプリ識別
     */
    public String app;
    /**
     * バージョン
     */
    public String ver;

    /**
     * キャンセルフラグ
     */
    public boolean cancel = false;
    /**
     * パラメーター
     */
    private ApiParams params;

    /**
     * このクラスで使うデータ
     */
    public static class ApiParams {

        /**
         * TOPお知らせ取得用
         */
        public void setDataGetTopInfomation() {
        }
    }

    /**
     * コンストラクタ
     *
     * @param con
     * @param task
     * @param callback
     * @param requestCode
     * @param apiParams
     */
    public Api(Context con, int task, final ApiCallback callback, final int requestCode, ApiParams apiParams) {
        this.task = task;
        context = con;
        this.callback = callback;
        this.requestCode = requestCode;
        respCode = ApiCallback.ERROR;
        this.params = apiParams;

    }

    /**
     * 非同期処理開始
     */
    @Override
    protected String[] doInBackground(String... params) {
        synchronized (context) {
            if (this.isCancelled()) {
                return null;
            }
            switch (task) {
                case Constants.API_GET_TOP_INFO:
                    getTopInfo();
                    break;
            }
        }
        return null;
    }

    private void getTopInfo() {
        obj = null;
        try {
            String cUrl = Constants.URL_TOP_INFO;
            obj = getJsonDataGet(cUrl, new TopInfo());
            Log.d(TAG, "getTopInfo: "+obj.toString());
        } catch (Exception e) {
            obj = null;
        }
    }

    /**
     * HTTP通信 GET版
     *
     * @param obj
     * @return
     */
    public Object getJsonDataGet(String url, Object obj) {
        HttpURLConnection connection = null;
        try {
            URL u = new URL(url);
            connection = (HttpURLConnection) u.openConnection();
            // optional default is GET
            connection.setRequestMethod("GET");
            connection.setReadTimeout(Constants.CONNECTION_TIMEOUT);
            connection.setConnectTimeout(Constants.CONNECTION_TIMEOUT);
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream objStream = connection.getInputStream();
                InputStreamReader objReader = new InputStreamReader(objStream);
                JsonReader jsonReader = new JsonReader(objReader);
                GsonBuilder gb = new GsonBuilder().excludeFieldsWithModifiers(
                        Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
                Gson gson = gb.create();
                obj = gson.fromJson(jsonReader, checkInstans(obj));
                objStream.close();
            }
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return obj;
    }

    /**
     * Jsonの戻りの型を判定
     *
     * @param obj
     * @return
     */
    private Type checkInstans(Object obj) {
        if (obj instanceof TopInfo) {
            return TopInfo.class;
        }
        return null;
    }

    @Override
    protected void onCancelled() {
        cancel = true;
    }

    @Override
    protected void onPostExecute(String[] result) {
        Log.d(TAG, "onPostExecute: "+result);
        callback.callback(respCode, requestCode, obj);
    }

    public void forceCancel() {
        try {
            cancel(true);
        } catch (Exception e) {

        }
    }
}
