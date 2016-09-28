package jp.co.crypton.spinach.Helpers;

/**
 * API実行後のコールバッククラス
 * @author aso
 *
 */
public interface ApiCallback {

	/** API結果：成功(0) */
    public static final int SUCCESS = 0;  
	/** API結果：失敗(-1) */
    public static final int ERROR = -1;  
	/** コールバック */
    public void callback(final int responseCode, final int requestCode, final Object obj);
}