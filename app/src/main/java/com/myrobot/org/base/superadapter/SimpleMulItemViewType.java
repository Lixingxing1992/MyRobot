package com.myrobot.org.base.superadapter;

/**
 * Convenient class for RecyclerView.Adapter.
 * <p>
 * Created by Lixingxing on 16/4/5.
 */
public abstract class SimpleMulItemViewType<T> implements IMulItemViewType<T> {

    @Override
    public int getViewTypeCount() {
        return 1;
    }

}
