package com.tvo.nomaps.Helpers;

/**
 * Created by huy on 9/6/2016.
 */

public class CheckURLVali {
    public Boolean checkURL(String url) {
        String str1 = "http://plate.id/";
        if (url.contains(str1)) {
            return true;
        }
        return false;
    }

    public String slipURI(String URI) {
        String check = "=http://plate.id/";
        if (URI.contains(check)){
            String arr[] = URI.split("=");
            return arr[arr.length - 1];
        }
        return URI.substring(0,URI.length()-1);
    }

    public String addHttp(String url){
        String http = "http://";
        if (url.contains(http))
        {
            return url;
        }
        else {
            return http+url;
        }
    }
}
