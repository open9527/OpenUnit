package com.open.pkg.ui.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.zhpan.bannerview.BannerViewPager;
import com.zhpan.bannerview.BaseBannerAdapter;
import com.zhpan.bannerview.BaseViewHolder;

public class BannerView extends BannerViewPager {
    public BannerView(Context context) {
        this(context, null, -1);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public static class BannerViewAdapter<T> extends BaseBannerAdapter<T> {

        @Override
        protected void bindData(BaseViewHolder<T> holder, T t, int position, int pageSize) {
            ViewDataBinding binding = DataBindingUtil.bind(holder.itemView);
            assert binding != null;

        }

        @Override
        public int getLayoutId(int viewType) {
            return 0;
        }
    }
}
