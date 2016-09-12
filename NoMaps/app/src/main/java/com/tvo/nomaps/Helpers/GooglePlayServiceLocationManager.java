package com.tvo.nomaps.Helpers;

import io.tangerine.beaconreceiver.android.sdk.application.LocationManager;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class GooglePlayServiceLocationManager implements LocationManager,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    // 位置情報リクエスト設定
    private final int LOCATION_REQUEST_INTERVAL = 750; // 0.75秒間隔
    private final int LOCATION_REQUEST_PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY; // 高精度。できる限り正確な位置。
//  private final int LOCATION_REQUEST_PRIORITY = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY; //高精度と省電力。誤差約100m以内の精度。
//  private final int LOCATION_REQUEST_PRIORITY = LocationRequest.PRIORITY_LOW_POWER; //省電力。誤差約10km以内の精度。電力消費を抑えられる。
//  private final int LOCATION_REQUEST_PRIORITY = LocationRequest.PRIORITY_NO_POWER; //消費電力なしで可能な限り最高の精度。

    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;


    /**
     * コンストラクタ
     * <p>
     * LocationClientのインスタンスと初期ロケーションの取得
     * </p>
     *
     * @param context
     */
    public GooglePlayServiceLocationManager(Context context) {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    /**
     * LocationListenerメンバー変数 定義
     */
    private final LocationListener mLocationListener = new LocationListener() {
        /**
         * ローケーション変更時の通知イベント受信
         *
         * @param location
         */
        public void onLocationChanged(final Location location) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mLocation = location;
                }
            }).start();
        }
    };

    /**
     * 位置情報の取得
     */
	/*
	public void getCurrentLocation() {
		if (mLocationClient != null) {
			mLocation = mLocationClient.getLastLocation();
		} else {
			Log.d("LocationManager", "LocationClient is not connected.");
		}
	}
	*/


    /**
     * 緯度 取得
     *
     * @return メンバー変数mLocationより緯度を取得
     */
    public double getLatitude()
    {
        if (mLocation == null)
        {
            return 0;
        }
        return mLocation.getLatitude();
    }

    /**
     * 経度 取得
     *
     * @return メンバー変数mLocationより経度を取得
     */
    public double getLongitude()
    {
        if (mLocation == null)
        {
            return 0;
        }
        return mLocation.getLongitude();
    }

    /**
     * 接続確認
     *
     * @return
     */
    public boolean isConnected()
    {
        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            return false;
        }
        return true;
    }

    /**
     * 終了
     */
    void finalysed()
    {
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        mGoogleApiClient = null;

    }

    @Override
    public void onConnected(Bundle connectionHint) {
        LocationRequest request = LocationRequest.create().setPriority(LOCATION_REQUEST_PRIORITY).setInterval(LOCATION_REQUEST_INTERVAL);

    }

    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient = null;

    }
}
