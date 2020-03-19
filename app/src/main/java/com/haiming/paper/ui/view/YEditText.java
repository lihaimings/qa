package com.haiming.paper.ui.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

public class YEditText extends EditText {

    private String hint;

    public YEditText(Context context) {
        super(context);
        init();
    }

    public YEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 获得hint值
     *
     * @author admin 2016-9-5 下午4:32:19
     */
    private void init() {
        hint = getHint().toString();
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        if (focused) {
            setHint("");
        } else {
            setHint(hint);
        }
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }
}

