package com.example.danmudemobz.CustomDanmu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.danmudemobz.R;

/**
 * author  : Liushuai
 * time    : 2020/9/6 9:58
 * desc    :
 */
public class DanMuAdapter extends DMCacheAdapter<DanmuModel> {
    private Context context;


    public DanMuAdapter(Context context) {
        this.context = context;
    }

    @Override
    public View getView(DanmuModel danmuEntity, View convertView) {
        ViewHolder viewHolder=null;
        if (convertView == null) {
            switch (danmuEntity.getType()) {
                case 1:
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_danmu_layout_type_1, null);
                    viewHolder=new ViewHolder();
                    viewHolder.content = convertView.findViewById(R.id.content);

                    viewHolder.image = convertView.findViewById(R.id.image);
                    convertView.setTag(viewHolder);
                    break;
            }
        }else{
            switch (danmuEntity.getType()) {
                case 1:
                    viewHolder= (ViewHolder) convertView.getTag();
                    viewHolder.content.setText(danmuEntity.getContent());
                    break;
            }
        }
        return convertView;

    }

    @Override
    public int[] getViewTypeArray() {
        return new int[0];
    }

    @Override
    public int getSingleLineHeight() {
       //高度 多个取最高
       View  view = LayoutInflater.from(context).inflate(R.layout.item_danmu_layout_type_1, null);
       view.measure(0,0);
       return view.getMeasuredHeight();
    }

    class ViewHolder{
        public TextView content;
        public ImageView image;

    }
}
