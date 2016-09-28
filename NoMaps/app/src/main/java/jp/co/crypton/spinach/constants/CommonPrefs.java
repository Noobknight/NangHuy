/**
 * CommonPrefs.java
 */
package jp.co.crypton.spinach.constants;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * プリファレンスヘルパクラス
 * 
 * @author aso
 */
public class CommonPrefs extends AppPrefs {

	/** UUID */
	private static final String KEY_UUID = "uuid";
	/** handsetID */
	private static final String KEY_HANDSET_ID = "hanset_id";
	/** isAgree */
	private static final String KEY_IS_AGREE = "is_agree";
	/** cookie */
	private static final String KEY_COOKIE_TAPCM = "cookie_tapcm";

	private static final String KEY_NOTIFY = "notify";

	/**
	 * UUIDを取得
	 * @return UUID
	 */
	public static String getUUID() {
		return get(KEY_UUID);
	}
	
	/**
	 * UUIDを設定
	 * @param uuid UUID
	 */
	public static void setUUID(String uuid) {
		putAsync(KEY_UUID, uuid);
	}
	
	/**
	 * handsetIDを取得
	 * @return handsetID
	 */
	public static String getHandsetId() {
		return get(KEY_HANDSET_ID);
	}
	
	/**
	 * handsetIDを設定
	 * @param handsetId
	 */
	public static void setHandsetId(String handsetId) {
		putAsync(KEY_HANDSET_ID, handsetId);
	}

	/**
	 * 利用許諾同意状態を取得
	 * @return isAgree
	 */
	public static boolean isAgree() {
		return getBoolean(KEY_IS_AGREE);
	}

	/**
	 * 利用許諾同意状態を設定
	 * @param agree
	 */
	public static void setAgree(boolean agree) {
		putAsync(KEY_IS_AGREE, agree);
	}

	/**
	 * クッキーのTAPCMを取得
	 * @return tapcm
	 */
	public static String getCookieTapcm() {
		return get(KEY_COOKIE_TAPCM);
	}

	/**
	 * クッキーのTAPCMを設定
	 * @param tapcm
	 */
	public static void setCookieTapcm(String tapcm) {
		putAsync(KEY_COOKIE_TAPCM, tapcm);
	}

	/**
	 * カスタムデータを取得
	 * @return para
	 */
	public static String getCustomeData(String key) {
		return get(key);
	}

	/**
	 * 利用許諾同意状態を設定
	 * @param key
	 * @param val
	 */
	public static void setCustomData(String key, String val) {
		putAsync(key, val);
	}
	
	//-------------------------------
	// private members
	//-------------------------------
	/**
	 * 非同期書き込み
	 * @param key プリファレンスのキー
	 * @param value 書き込む値
	 */
	private static void putAsync(String key, Object value) {
		putValue(getEditor(), key, value).apply();
	}
	
	/**
	 * 設定されている値を削除
	 * @param key 削除するプリファレンスのキー
	 */
	public static void deleteAsync(String key) {
		putValue(getEditor(), key, null).apply();
	}

	
	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
	    try {
	        PackageInfo packageInfo = context.getPackageManager()
	                .getPackageInfo(context.getPackageName(), 0);
	        return packageInfo.versionCode;
	    } catch (NameNotFoundException e) {
	        // should never happen
	        throw new RuntimeException("Could not get package name: " + e);
	    }
	}

	public  static void setKeyNotify(boolean key){
		put(KEY_NOTIFY, key);
	}
	public static boolean getKeyNotify() {
		return getBoolean(KEY_NOTIFY);
	}
}
