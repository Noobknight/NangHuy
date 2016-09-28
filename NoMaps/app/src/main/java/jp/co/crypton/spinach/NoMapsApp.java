package jp.co.crypton.spinach;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import jp.co.crypton.spinach.constants.CommonPrefs;
import jp.co.crypton.spinach.utils.Utils;

import io.tangerine.beaconreceiver.android.sdk.application.HandsetId;

/**
 * NoMapsApp
 * @author aso
 *
 */
public class NoMapsApp extends Application {

	/** UUID */
	public static String UUID;
	/** HANDSET_ID */
	public static String HANDSET_ID;
	/** コンテキスト */
	static private Context context = null;
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
		init();
	}

	/**
	 * アプリ起動時の処理
	 * IMEI取得とUUIDを生成してプリファレンスにセット
	 */
	public void init() {
		getPreference();
		UUID = CommonPrefs.getUUID();
		if (UUID == null || "".equals(UUID)) {
			UUID = Utils.getUUID();
			CommonPrefs.setUUID(UUID);
		}

		HANDSET_ID = CommonPrefs.getHandsetId();
		if (HANDSET_ID == null || "".equals(HANDSET_ID)) {
			HANDSET_ID = HandsetId.getGuid(HandsetId.Type.Guid, this);
			CommonPrefs.setHandsetId(HANDSET_ID);
		}

		initImageLoader(getApplicationContext());
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.denyCacheImageMultipleSizesInMemory();
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		config.writeDebugLogs(); // Remove for release app

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config.build());
	}

	/**
	 * シェアードプリファレンス
	 * @return
	 */
	static public SharedPreferences getPreference() {
		return context.getSharedPreferences("app_prefs", Activity.MODE_PRIVATE);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}
}