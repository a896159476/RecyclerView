package com.example.recyclerview.adapter;

import com.example.recyclerview.bean.MultiMainBean;
import com.example.recyclerview.recycler.provider.BaseProviderMultiAdapter;

import java.util.List;

public class MainProviderMultiAdapter extends BaseProviderMultiAdapter<MultiMainBean> {

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
