package jp.co.crypton.spinach.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpClient;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.SimpleDateFormat;
import java.util.Date;

import jp.co.crypton.spinach.Helpers.CheckURLVali;
import jp.co.crypton.spinach.R;
import jp.co.crypton.spinach.abstracts.BaseActivity;
import jp.co.crypton.spinach.constants.Constants;
import jp.co.crypton.spinach.utils.Utils;

/**
 * Created by huy on 9/14/2016.
 */
public class ScheduleActivity extends BaseActivity {
    private final String TAG = ScheduleActivity.class.getSimpleName();
    private CheckURLVali checkURLVali = new CheckURLVali();
    private ImageView todayTtIv;

    private String today, tomorrow;
    private Button btnToday,btnOffical;

    @Override
    protected int layoutById() {
        return R.layout.activity_timetable;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        todayTtIv = (ImageView) findViewById(R.id.today_tt_iv);
        btnToday = (Button) findViewById(R.id.btnToday);
        btnToday.setEnabled(false);
        btnOffical = (Button) findViewById(R.id.btnOfficialWebsiteSchedule);


    }

    @Override
    protected void setEvents() {

        // ツールバーをアクションバーとしてセット
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        prepareNfc(new NfcEventListener() {
            @Override
            public void foundUri(String uri) {
                showWebview(uri, Constants.WEBVIEW_TYPE_SP);
            }
        });
        btnToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imageUri1 = Constants.SCHEDULE_IMAGE_DIR + today + ".png";
                gotoPinch("imageSchedule");
            }
        });
        btnOffical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoOfficial();
            }
        });
    }

    private void gotoOfficial() {
        String url = "https://no-maps.jp/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void gotoPinch(String value) {
        Intent i = new Intent(this, PinchImageActivity.class);
        i.putExtra("schudule", value);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private void showWebview(String url, int type) {
        Intent newActivity = new Intent(this, WebViewActivity.class);
        newActivity.putExtra(Constants.INTENT_KEY_WEBVIEW_URL, checkURLVali.slipURI(url));
        newActivity.putExtra(Constants.INTENT_KEY_WEBVIEW_TYPE, type);
        newActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(newActivity);
    }

    @Override
    protected void initData() {
        today = Utils.getTodayStr();
        tomorrow = Utils.getTomorowStr();
        Log.d(TAG, today + ":" + tomorrow);
        createTimetableLayout();
    }

    private void createTimetableLayout() {
        someMethod();
    }
    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            todayTtIv.setImageBitmap(bitmap);
            Utils.saveLocalFile(ScheduleActivity.this, "imageSchedule", bitmap);
            btnToday.setEnabled(true);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };

    private void someMethod() {
        String valid_until_end = "10/10/2016";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date strDateEnd = null;
        try {
            strDateEnd = sdf.parse(valid_until_end);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (new Date().before(strDateEnd)) {
            Picasso.with(this).load("https://s3-ap-northeast-1.amazonaws.com/aquabit/nomaps/schedule/20161010.png").into(target);
        }
        else {
            String imageUri1 = Constants.SCHEDULE_IMAGE_DIR + today + ".png";
            Picasso.with(this).load(imageUri1).into(target);
        }
    }
    @Override
    protected String TAG() {
        return TAG;
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
