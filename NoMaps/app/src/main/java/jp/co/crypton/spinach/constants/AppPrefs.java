/**
 * AppPrefs.java
 */
package jp.co.crypton.spinach.constants;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import jp.co.crypton.spinach.NoMapsApp;

/**
 * プリファレンスアクセスクラス
 * @author aso
 */
public class AppPrefs {

	/**
	 * 値が設定されているかどうか調べる
	 * @param key 調べるプリファレンスのキー
	 */
	public static boolean contains(String key) {
		return getPreference().contains(key);
	}

	/**
	 * 文字列を取得
	 * @param key 取得するプリファレンスのキー
	 * @return 取得した文字列
	 */
	public static String get(String key) {
		return get(key, null);
	}
	
	/**
	 * 文字列を取得
	 * @param key 取得するプリファレンスのキー
	 * @param defaultValue キーが存在しない場合に返す文字列
	 * @return 取得した文字列
	 */
	public static String get(String key, String defaultValue) {
		return getPreference().getString(key, defaultValue);
	}

	/**
	 * boolean値を取得
	 * @param key 取得するプリファレンスのキー
	 * @return 取得したboolean値
	 */
	public static boolean getBoolean(String key) {
		return getBoolean(key, false);
	}
	
	/**
	 * boolean値を取得
	 * @param key 取得するプリファレンスのキー
	 * @param defaultValue キーが存在しない場合に返すboolean値
	 * @return 取得したboolean値
	 */
	public static boolean getBoolean(String key, boolean defaultValue) {
		return getPreference().getBoolean(key, defaultValue);
	}

	/**
	 * int値を取得
	 * @param key 取得するプリファレンスのキー
	 * @return 取得したint値
	 */
	public static int getInt(String key) {
		return getInt(key, 0);
	}
	
	/**
	 * int値を取得
	 * @param key 取得するプリファレンスのキー
	 * @param defaultValue キーが存在しない場合に返すint値
	 * @return 取得したint値
	 */
	public static int getInt(String key, int defaultValue) {
		return getPreference().getInt(key, defaultValue);
	}

	/**
	 * long値を取得
	 * @param key 取得するプリファレンスのキー
	 * @return 取得したlong値
	 */
	public static long getLong(String key) {
		return getLong(key, 0);
	}

	/**
	 * long値を取得
	 * @param key 取得するプリファレンスのキー
	 * @param defaultValue キーが存在しない場合に返すlong値
	 * @return 取得したlong値
	 */
	public static long getLong(String key, long defaultValue) {
		return getPreference().getLong(key, defaultValue);
	}

	/**
	 * float値を取得
	 * @param key 取得するプリファレンスのキー
	 * @return 取得したfloat値
	 */
	public static float getFloat(String key) {
		return getFloat(key, 0);
	}

	/**
	 * float値を取得
	 * @param key 取得するプリファレンスのキー
	 * @param defaultValue キーが存在しない場合に返すfloat値
	 * @return 取得したfloat値
	 */
	public static float getFloat(String key, float defaultValue) {
		return getPreference().getFloat(key, defaultValue);
	}

	/**
	 * 値を設定
	 * @param key 設定するプリファレンスのキー
	 * @param value キーに設定する値
	 */
	public static void put(String key, Object value) {
		putValue(getEditor(), key, value).commit();
	}

	/**
	 * 設定されている値を削除
	 * @param key 削除するプリファレンスのキー
	 */
	public static void delete(String key) {
		putValue(getEditor(), key, null).commit();
	}
	
	/**
	 * プリファレンスをリセット
	 */
	public static void reset() {
		getEditor().clear().commit();
	}

	
	/**
	 * プリファレンスの取得
	 * @return プリファレンス
	 */
	protected static SharedPreferences getPreference() {
		return NoMapsApp.getPreference();
	}
	
	/**
	 * プリファレンスエディタの取得
	 * @return プリファレンスエディタ
	 */
	protected static Editor getEditor() {
		return NoMapsApp.getPreference().edit();
	}
	
	/**
	 * 文字列を設定
	 * @param editor プリファレンスエディタ
	 * @param key 設定するプリファレンスのキー
	 * @param value キーに設定する文字列
	 * @return プリファレンスエディタ
	 */
	protected static Editor putValue(Editor editor, String key, Object value) {
		if (value instanceof Boolean) {
			editor.putBoolean(key, (Boolean)value);
		}
		else if (value instanceof Integer) {
			editor.putInt(key, (Integer)value);
		}
		else if (value instanceof Long) {
			editor.putLong(key, (Long)value);
		}
		else if (value instanceof Float) {
			editor.putFloat(key, (Float)value);
		}
		else if (value instanceof String) {
			editor.putString(key, (String)value);
		}
		else if (value != null) {
			editor.putString(key, value.toString());
		}
		else {
			editor.remove(key);
		}
		return editor;
	}
	
}
