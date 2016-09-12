package com.tvo.nomaps.Helpers;

import android.webkit.CookieManager;

/**
 * Created by huy on 9/8/2016.
 */
public class GetValueCookies {
    public String getCookie(String url){
        String CookieName = "TAPCM";
        String CookieValue = null;
        CookieManager cookieManager = CookieManager.getInstance();
        String cookies = cookieManager.getCookie(url);
        String[] temp=cookies.split(";");
        for (String ar1 : temp ){
            if(ar1.contains(CookieName)){
                String[] temp1=ar1.split("=");
                CookieValue = temp1[1];
            }
        }
        return CookieValue;
    }
}
