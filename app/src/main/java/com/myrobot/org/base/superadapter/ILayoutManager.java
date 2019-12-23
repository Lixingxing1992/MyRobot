package com.myrobot.org.base.superadapter;

import android.support.v7.widget.RecyclerView;

/**
 * Methods about layout manager.
 * <p>
 * Created by Lixingxing on 16/1/18.
 */
interface ILayoutManager {
    boolean hasLayoutManager();

    RecyclerView.LayoutManager getLayoutManager();
}
