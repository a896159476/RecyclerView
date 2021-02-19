package com.example.recyclerview.mian;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.example.recyclerview.bean.MainBean;

public class DiffItemCallBack extends DiffUtil.ItemCallback<MainBean> {

    /**
     * 判断是否是同一个item
     * True表示一致，False表示不一致
     */
    @Override
    public boolean areItemsTheSame(@NonNull MainBean oldItem, @NonNull MainBean newItem) {
        return oldItem.getId() == newItem.getId();
    }

    /**
     * 当是同一个item时，再判断内容是否发生改变
     * True表示一致，False表示不一致
     */
    @Override
    public boolean areContentsTheSame(@NonNull MainBean oldItem, @NonNull MainBean newItem) {
        return oldItem.getName().equals(newItem.getName());
    }

    /**
     * 可选实现
     * 如果需要精确修改某一个view中的内容，请实现此方法。
     * 如果不实现此方法，或者返回null，将会直接刷新整个item。
     */
    @Nullable
    @Override
    public Object getChangePayload(@NonNull MainBean oldItem, @NonNull MainBean newItem) {
        if (!oldItem.getName().equals(newItem.getName())) {
            return newItem;
        }
        return null;
    }

}
