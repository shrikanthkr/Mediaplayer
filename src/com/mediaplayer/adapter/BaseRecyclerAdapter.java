package com.mediaplayer.adapter;

import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.mediaplayer.interfaces.RecyclerClickHelper;

/**
 * Created by shrikanth on 1/21/17.
 */

public  class BaseRecyclerAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    RecyclerClickHelper clickHelper;

    public BaseRecyclerAdapter(RecyclerClickHelper clickHelper) {
        this.clickHelper = clickHelper;
    }

    @CallSuper
    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return null;
    }

    @CallSuper
    @Override
    public void onBindViewHolder(final T holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickHelper != null) clickHelper.onItemClickListener( holder.itemView, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

}
