package jp.co.crypton.spinach.Helpers;

import android.util.Log;
import android.webkit.CookieManager;

/**
 * Created by huy on 9/8/2016.
 */
public class GetValueCookies {
    public String getCookie(String url) {
        String CookieName = "TAPCM";
        String CookieValue = "";
        CookieManager cookieManager = CookieManager.getInstance();
        String cookies = cookieManager.getCookie(url);
        Log.d("aaa", "getCookie: " + cookies);
        if (cookies != null) {
            String[] temp = cookies.split(";");
            if (temp.length > 0) {
                for (String ar1 : temp) {
                    if (ar1.contains(CookieName)) {
                        String[] temp1 = ar1.split("=");
                        if (temp.length > 0) {
                            CookieValue = temp1[1];
                        }
                    }
                }
                return CookieValue;
            }
        }
        return CookieValue;
    }
}
