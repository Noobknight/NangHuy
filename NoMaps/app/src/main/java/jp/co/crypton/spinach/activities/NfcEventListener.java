package jp.co.crypton.spinach.activities;

import java.util.EventListener;

/**
 * Created by huy on 9/1/2016.
 */
public interface NfcEventListener extends EventListener {

    public void foundUri(String foundUrl);
}
