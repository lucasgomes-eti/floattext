package com.lucas.fabtext;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class ObservableScrollView extends ScrollView {

    public interface ScrollViewListener {
        void onScrollViewTouchEvent(MotionEvent event);
    }

    private ScrollViewListener scrollViewListener = null;

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (scrollViewListener != null) {
            scrollViewListener.onScrollViewTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }
}
