package com.example.danmudemobz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.danmudemobz.BZDanMu.BitmapUtils;
import com.example.danmudemobz.BZDanMu.MyCacheStuffer;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.BaseCacheStuffer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.util.SystemClock;
import master.flame.danmaku.ui.widget.DanmakuView;

public class MainActivity extends AppCompatActivity {

    private DanmakuView mDanmakuView;
    private DanmakuContext mContext;
    private BaseDanmakuParser mParser;

    private EditText editText;
    private Button   mbt;

    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDanmakuView=findViewById(R.id.dv);
        editText=findViewById(R.id.et_damu);
        mbt=findViewById(R.id.bt_send);
        initDanMuView();

        mbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText.getText().toString().isEmpty()) {
//                    addDanmaku(true,editText.getText().toString());
                    addWithHeadText(editText.getText().toString());
                }
            }
        });
    }


    private void initDanMuView(){
        // 设置最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<Integer, Integer>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 5); // 滚动弹幕最大显示5行
        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<Integer, Boolean>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);

        mContext = DanmakuContext.create();
        mContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3).setDuplicateMergingEnabled(false).setScrollSpeedFactor(1.2f).setScaleTextSize(1.2f)
//                .setCacheStuffer(new SpannedCacheStuffer(), mCacheStufferAdapter) // 图文混排使用SpannedCacheStuffer
        .setCacheStuffer(new MyCacheStuffer(this),mBackgroundCacheStuffer)  // 绘制背景使用BackgroundCacheStuffer
                .setMaximumLines(maxLinesPair)
                .preventOverlapping(overlappingEnablePair).setDanmakuMargin(40);

        if (mDanmakuView != null) {
            mParser = getDefaultDanmakuParser();
            mDanmakuView.setCallback(new master.flame.danmaku.controller.DrawHandler.Callback() {
                @Override
                public void updateTimer(DanmakuTimer timer) {
                }

                @Override
                public void drawingFinished() {

                }

                @Override
                public void danmakuShown(BaseDanmaku danmaku) {
//                    Log.d("DFM", "danmakuShown(): text=" + danmaku.text);
                }

                @Override
                public void prepared() {
                    mDanmakuView.start();
                }
            });
            mDanmakuView.setOnDanmakuClickListener(new IDanmakuView.OnDanmakuClickListener() {

                @Override
                public boolean onDanmakuClick(IDanmakus danmakus) {
                    Log.d("DFM", "onDanmakuClick: danmakus size:" + danmakus.size());
                    BaseDanmaku latest = danmakus.last();
                    if (null != latest) {
                        Log.d("DFM", "onDanmakuClick: text of latest danmaku:" + latest.text);
                        return true;
                    }
                    return false;
                }

                @Override
                public boolean onDanmakuLongClick(IDanmakus danmakus) {
                    return false;
                }

                @Override
                public boolean onViewClick(IDanmakuView view) {
                    return false;
                }
            });
            mDanmakuView.prepare(mParser, mContext);
            mDanmakuView.showFPS(true);
            mDanmakuView.enableDanmakuDrawingCache(true);
        }
    }

    class AsyncAddTask extends TimerTask {

        @Override
        public void run() {
            for (int i = 0; i < 20; i++) {
                addDanmaku(true,"");
                SystemClock.sleep(20);
            }
        }
    };

    private BaseCacheStuffer.Proxy mBackgroundCacheStuffer = new BaseCacheStuffer.Proxy() {
        @Override
        public void prepareDrawing(BaseDanmaku danmaku, boolean fromWorkerThread) {
            // 根据你的条件检查是否需要需要更新弹幕
        }

        @Override
        public void releaseResource(BaseDanmaku danmaku) {
            //清理额外的字段
            danmaku.tag = null;
        }
    };



    private void addDanmaku(boolean islive,String content) {
        //创建一个弹幕对象，这里后面的属性是设置滚动方向的！
        BaseDanmaku danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        if (danmaku == null || mDanmakuView == null) {
            return;
        }
        //弹幕显示的文字
        danmaku.text = content + System.nanoTime();
        //设置相应的边距，这个设置的是四周的边距
        danmaku.padding = 5;
        // 可能会被各种过滤器过滤并隐藏显示，若果是本机发送的弹幕，建议设置成1；
        danmaku.priority = 0;
        //是否是直播弹幕
        danmaku.isLive = islive;
        danmaku.setTime(mDanmakuView.getCurrentTime() + 1200);
        //设置文字大小
        danmaku.textSize = 25f;
        //设置文字颜色
        danmaku.textColor = Color.RED;
        //设置阴影的颜色
        danmaku.textShadowColor = Color.WHITE;
        // danmaku.underlineColor = Color.GREEN;
        //设置背景颜色
        danmaku.borderColor = Color.GREEN;
        //添加这条弹幕，也就相当于发送
        mDanmakuView.addDanmaku(danmaku);
    }

    public void addWithHeadText(String content) {
        BaseDanmaku danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);

        if (danmaku == null || mDanmakuView == null) {
            return;
        }

        //设置相应的数据
        Bitmap showBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon);
        showBitmap = BitmapUtils.getShowPicture(showBitmap);
        Map<String, Object> map = new HashMap<>(16);
        map.put("content",content);
        map.put("bitmap", showBitmap);
        map.put("color", "#0099ff");
        danmaku.tag = map;
        danmaku.textSize = 0;
        danmaku.padding = 10;
        danmaku.text = "";
        // 一定会显示, 一般用于本机发送的弹幕
        danmaku.priority = 1;
        danmaku.isLive = false;
        danmaku.setTime(mDanmakuView.getCurrentTime());
        danmaku.textColor = Color.WHITE;
        // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
        danmaku.textShadowColor = 0;
        mDanmakuView.addDanmaku(danmaku);
    }



    public static BaseDanmakuParser getDefaultDanmakuParser() {
        return new BaseDanmakuParser() {
            @Override
            protected IDanmakus parse() {
                return new Danmakus();
            }
        };
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mDanmakuView != null && mDanmakuView.isPrepared()) {
            mDanmakuView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDanmakuView != null && mDanmakuView.isPrepared() && mDanmakuView.isPaused()) {
            mDanmakuView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDanmakuView != null) {
            // dont forget release!
            mDanmakuView.release();
            mDanmakuView = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mDanmakuView != null) {
            // dont forget release!
            mDanmakuView.release();
            mDanmakuView = null;
        }
    }
}
