package com.myrobot.org.base.superadapter;

import android.view.View;

/**
 * OnItemLongClickListener for RecyclerView.
 * <p>
 * Created by Lixingxing on 16/2/24.
 */
public interface OnItemLongClickListener {
    void onItemLongClick(View itemView, int viewType, int position);
}
