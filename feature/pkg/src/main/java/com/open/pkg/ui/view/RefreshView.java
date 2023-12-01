package com.open.pkg.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

public class RefreshView extends SmartRefreshLayout {
    public RefreshView(Context context) {
        this(context, null);
    }

    public RefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
