package com.example.recyclerview.adapter;

import android.view.View;
import android.widget.Toast;

import com.example.recyclerview.R;
import com.example.recyclerview.bean.MultiMainBean;
import com.example.recyclerview.recycler.provider.BaseItemProvider;
import com.example.recyclerview.recycler.BaseViewHolder;

public class TextItemProvider extends BaseItemProvider<MultiMainBean> {
    @Override
    public int getItemViewType() {
        return R.layout.item_main2;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_main2;
    }

    @Override
    public void convert(BaseViewHolder holder, MultiMainBean item, int position) {
        holder.setText(R.id.tv_name, item.getName());
        addChildClickViewIds(R.id.tv_name);
        addChildLongClickViewIds(R.id.tv_name);
    }

    @Override
    public void onClick(BaseViewHolder helper, View view, MultiMainBean data, int position) {
        super.onClick(helper, view, data, position);
    }

    @Override
    public boolean onLongClick(BaseViewHolder helper, View view, MultiMainBean data, int position) {
        return true;
    }

    @Override
    public void onChildClick(BaseViewHolder helper, View view, MultiMainBean data, int position) {
        super.onChildClick(helper, view, data, position);
        Toast.makeText(context,"TextItemProvider子View点击",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onChildLongClick(BaseViewHolder helper, View view, MultiMainBean data, int position) {
        Toast.makeText(context,"TextItemProvider子View长按",Toast.LENGTH_SHORT).show();
        return true;
    }
}
