package com.example.danmudemobz.BZDanMu;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import com.example.danmudemobz.R;
import java.util.Map;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.android.AndroidDisplayer;
import master.flame.danmaku.danmaku.model.android.BaseCacheStuffer;

/**
 * author  : Liushuai
 * time    : 2020/9/5 17:16
 * desc    :B站自定义样式
 */
public class MyCacheStuffer extends BaseCacheStuffer {

    /**
     * 文字和头像间距
     */
    private float LEFTMARGE;
    /**
     * 文字和右边线距离
     */
    private int TEXT_RIGHT_PADDING;
    /**
     * 文字大小
     */
    private float TEXT_SIZE;
    /**
     * 头像的大小
     */
    private float IMAGEHEIGHT;
    /**
     * 文字右边间距
     */
    private float RIGHTMARGE;
    public MyCacheStuffer(Activity activity) {
        LEFTMARGE = activity.getResources().getDimension(R.dimen.DIMEN_13PX);
        RIGHTMARGE = activity.getResources().getDimension(R.dimen.DIMEN_22PX);
        IMAGEHEIGHT = activity.getResources().getDimension(R.dimen.DIMEN_60PX);
        TEXT_SIZE = activity.getResources().getDimension(R.dimen.DIMEN_24PX);

    }

    @Override
    public void measure(BaseDanmaku danmaku, TextPaint paint, boolean fromWorkerThread) {
        Map<String,Object> map= (Map<String, Object>) danmaku.tag;
        String content = (String) map.get("content");

        Bitmap bitmap = (Bitmap) map.get("bitmap");

        paint.setTextSize(TEXT_SIZE);

        float contentWidth = paint.measureText(content);

        danmaku.paintWidth=contentWidth+IMAGEHEIGHT+LEFTMARGE+RIGHTMARGE;

        danmaku.paintHeight=IMAGEHEIGHT;



    }

    @Override
    public void clearCaches() {

    }

    @Override
    public void drawDanmaku(BaseDanmaku danmaku, Canvas canvas, float left, float top, boolean fromWorkerThread, AndroidDisplayer.DisplayerConfig displayerConfig) {
        Map<String, Object> map = (Map<String, Object>) danmaku.tag;
        String content = (String) map.get("content");
        Bitmap bitmap = (Bitmap) map.get("bitmap");
        String color = (String) map.get("color");

        //
        Paint paint = new Paint();
        paint.setTextSize(TEXT_SIZE);

        int textLength = (int) paint.measureText(content);

        paint.setColor(Color.parseColor(color));

        float  rectBgLeft=left;
        float  rectBgTop=top;

        float rectBgRight=left+IMAGEHEIGHT+textLength+LEFTMARGE+RIGHTMARGE;
        float rectBgBottom=top+IMAGEHEIGHT;

        canvas.drawRoundRect(new RectF(rectBgLeft, rectBgTop, rectBgRight, rectBgBottom), IMAGEHEIGHT / 2, IMAGEHEIGHT / 2, paint);


        float avatorRight=left+IMAGEHEIGHT;
        float avatorBottom=top+IMAGEHEIGHT;
        canvas.drawBitmap(bitmap,null,new RectF(left,top,avatorRight,avatorBottom),paint);

        paint.setColor(Color.WHITE);
        float contentLeft=left+IMAGEHEIGHT+LEFTMARGE;
        Paint.FontMetrics fontMetrics=paint.getFontMetrics();
        float textTop = fontMetrics.top;

        float textBottom=fontMetrics.bottom;
        float contentBottom=top+IMAGEHEIGHT/2;

        int baseLineY = (int) (contentBottom - textTop / 2 - textBottom / 2);
        //文本内容
        canvas.drawText(content,contentLeft,baseLineY,paint);

    }
}
