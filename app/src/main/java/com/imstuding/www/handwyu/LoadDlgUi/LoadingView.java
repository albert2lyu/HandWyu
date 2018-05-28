package com.imstuding.www.handwyu.LoadDlgUi;


import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.imstuding.www.handwyu.R;


public class LoadingView extends LinearLayout{
    public static final int LOADING = 0;
    public static final int GONE = 4;
    private ImageView imageView;
    private AnimationDrawable mAni;
    private View mView;

    public LoadingView(Context context) {
        super(context);
        init(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.loading_layout, this);
        imageView = (ImageView) mView.findViewById(R.id.iv_loading);
        mAni = (AnimationDrawable) imageView.getBackground();
        setStatue(GONE);
    }

    public void setStatue(int status) {
        setVisibility(View.VISIBLE);
        try {
            if (status == LOADING)
                mAni.start();
        } catch (OutOfMemoryError e) {

        }
    }


}
