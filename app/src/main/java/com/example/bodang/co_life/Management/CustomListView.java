package com.example.bodang.co_life.Management;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Bodang on 17/11/2016.
 */

//This is a listview which can use in a scrollview or swipereashlayout.
//By setting the static height of the list, the list view can be used in some
//specific situations
public class CustomListView extends ListView {
    public CustomListView(Context context) {
        super(context);
    }

    public CustomListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}

