package com.example.recyclerview.adapter;

import androidx.annotation.NonNull;

import com.example.recyclerview.R;
import com.example.recyclerview.bean.MultiMainBean;
import com.example.recyclerview.recycler.BaseMultiItemAdapter;
import com.example.recyclerview.recycler.BaseViewHolder;

import java.util.List;

public class MultiMainAdapter extends BaseMultiItemAdapter<MultiMainBean, BaseViewHolder> {

    public MultiMainAdapter(List<MultiMainBean> data) {
        super(data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, MultiMainBean item, int position) {
        holder.setText(R.id.tv_name, item.getName());
    }
}
