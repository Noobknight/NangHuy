package jp.co.crypton.spinach.Helpers;

import android.util.Log;

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
        return URI;
    }

    public String checkCharspecial(String url)
    {
//        Log.d("hello", "checkCharspecial: "+Integer.toHexString(Integer.parseInt(url.substring(url.length()-1))));
        String s = "��";
        if (url.contains(s))
        {
            return url.substring(0,url.length()-1);
        }
        else {
            return url;
        }
    }
}
