package com.example.danmudemobz.CustomDanmu;

import android.view.View;

import java.util.HashMap;
import java.util.Stack;

/**
 * author  : Liushuai
 * time    : 2020/9/5 10:30
 * desc    :
 */
public abstract class DMCacheAdapter<M> {
    private HashMap<Integer, Stack<View>> cacheViews;

    public DMCacheAdapter() {
       cacheViews = new HashMap<>();
       int typeArray[]=getViewTypeArray();
        for (int i = 0; i < typeArray.length; i++) {
            Stack<View> stack=new Stack<>();
            cacheViews.put(typeArray[i],stack);
        }
    }

    public abstract View getView(M danmuEntity, View convertView);
    //获取弹幕类型数据
    public abstract int[] getViewTypeArray();
    public abstract  int getSingleLineHeight();

    synchronized public View removeFromCacheViews(int type){
        if (cacheViews.get(type).size() > 0) {
            return cacheViews.get(type).pop();
        }else{
            return null;
        }
    }

    synchronized public void addToCacheViews(int type, View view) {
        if (cacheViews.containsKey(type)) {
            cacheViews.get(type).push(view);
        }else{
            throw new Error("undefined type view");
        }
    }

    synchronized public void shrinkCacheSize(){
        int typeArray[]=getViewTypeArray();
        for (int i = 0; i < typeArray.length; i++) {
            int type = typeArray[i];
            Stack<View> typeStack = cacheViews.get(type);
            int length=typeStack.size();
            while (typeStack.size() > (length / 2.0 + 0.5)) {
                typeStack.pop();
            }
            cacheViews.put(type, typeStack);
        }
    }

    public int getCacheSize(){
        int totalSize=0;
        int typeArray[] =getViewTypeArray();
        Stack typeStack=null;
        for (int i = 0; i < typeArray.length; i++) {
            int type = typeArray[i];
            typeStack = cacheViews.get(type);
            totalSize+=typeStack.size();
        }
        return totalSize;
    }


}
