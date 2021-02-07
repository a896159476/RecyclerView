package com.example.recyclerview.adapter;

import androidx.annotation.NonNull;

import com.example.recyclerview.R;
import com.example.recyclerview.bean.MainBean;
import com.example.recyclerview.recycler.BaseQuickAdapter;
import com.example.recyclerview.recycler.BaseViewHolder;

import java.util.List;

public class MainAdapter extends BaseQuickAdapter<MainBean, BaseViewHolder> {

    public MainAdapter(int layoutResId, List<MainBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, MainBean item, int position) {
        holder.setText(R.id.tv_name, item.getName());
    }
}
