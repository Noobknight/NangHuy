package jp.co.crypton.spinach.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import jp.co.crypton.spinach.R;
import jp.co.crypton.spinach.abstracts.BaseActivity;
import jp.co.crypton.spinach.constants.Constants;
import jp.co.crypton.spinach.utils.Utils;

/**
 * Created by huy on 9/23/2016.
 */
public class PinchImageActivity extends BaseActivity{
    private static final String TAG = "PinchImageActivity";
    ScaleGestureDetector scaleGestureDetector;
    TouchImageView imageView;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    protected int layoutById() {
        return R.layout.activity_pinch;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        scaleGestureDetector = new ScaleGestureDetector(this, new MyOnScaleGestureListener());
        imageView = (TouchImageView) findViewById(R.id.image);
        Intent intent = getIntent();
        String name = intent.getStringExtra("schudule");
        Bitmap todayBm = Utils.getLocalFile(this, name);
        if (todayBm != null) {
            imageView.setImageBitmap(todayBm);
        }
    }

    @Override
    protected void setEvents() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void initData() {


    }

    @Override
    protected String TAG() {
        return null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    public class MyOnScaleGestureListener extends
            ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            float scaleFactor = detector.getScaleFactor();

            if (scaleFactor > 1) {
                imageView.setMaxZoom(4f);

            } else {
                imageView.setMaxZoom(0.4f);

            }
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                finish();
                break;
        }
        return true;
    }

}
