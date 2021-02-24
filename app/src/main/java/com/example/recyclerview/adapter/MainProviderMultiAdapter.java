package com.example.recyclerview.adapter;

import com.example.recyclerview.bean.MultiMainBean;
import com.example.recyclerview.recycler.BaseViewHolder;
import com.example.recyclerview.recycler.provider.BaseProviderMultiAdapter;
import com.example.recyclerview.recycler.provider.slide.BaseProviderSlideAdapter;

import java.util.List;

public class MainProviderMultiAdapter extends BaseProviderSlideAdapter<MultiMainBean, BaseViewHolder> {

    public MainProviderMultiAdapter(List<MultiMainBean> data) {
        super(data);
        addItemProvider(new ImgItemProvider());
        addItemProvider(new TextItemProvider());
    }

    @Override
    protected int getItemType(List<MultiMainBean> data, int position) {
        MultiMainBean multiMainBean = data.get(position);
        //根据标记分类
        return multiMainBean.getItemType();
    }

}
