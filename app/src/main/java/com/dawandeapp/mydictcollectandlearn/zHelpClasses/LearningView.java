package com.dawandeapp.mydictcollectandlearn.zHelpClasses;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class LearningView extends FrameLayout {
    private LearningAdapter mAdapter = null;


    //Конструкторы
    public LearningView(@NonNull Context context) {
        super(context);
    }
    public LearningView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }
    public LearningView(@NonNull Context context, @Nullable AttributeSet attrs,
                       @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr, 0);
    }
    public LearningView(@NonNull Context context, @Nullable AttributeSet attrs,
                       @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean setAdapter(LearningAdapter adapter) {
        if (mAdapter != null) {
            return false;
        }
        this.mAdapter = adapter;
        return true;
    }

    public boolean start() {
        if (mAdapter == null) {
            return false;
        }

        return true;
    }


}
