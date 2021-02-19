package com.example.recyclerview.mian;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.example.recyclerview.bean.MainBean;

import java.util.List;

public class DiffCallBack extends DiffUtil.Callback {

    private final List<MainBean> mNewData;
    private final List<MainBean> mOldData;

    public DiffCallBack(List<MainBean> mOldData, List<MainBean> mNewData) {
        this.mNewData = mNewData;
        this.mOldData = mOldData;
    }

    /**
     * 返回老数据列表的大小
     */
    @Override
    public int getOldListSize() {
        return mOldData.size();
    }

    /**
     * 返回新数据列表的大小
     */
    @Override
    public int getNewListSize() {
        return mNewData.size();
    }

    /**
     * 在DiffUtil检测两个对象是否表示相同的Item时被调用，True代表两个对象对应相同的Item
     */
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldData.get(oldItemPosition).getId() == mNewData.get(newItemPosition).getId();
    }

    /**
     * 在DiffUtil想去检测两个Items是否有一样的数据时调用，True表示一致，False表示不一致
     */
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldData.get(oldItemPosition).getName().equals(mNewData.get(newItemPosition).getName());
    }

    /**
     * 在areItemsTheSame返回True时，areContentsTheSame返回false时，也就是一个Item的内容发生了变化（例如微博的点赞，我们只需要刷新图标而不是整个Item）。
     * 所以可以在getChangePayload()中封装一个Object来告诉RecyclerView进行局部的刷新。
     * 使用该功能时我们需要重写Adapter的public void onBindViewHolder(DiffVH holder, int position, List payloads) 回调，
     * payloads为我们返回的Object，我们可以通过bundle进行数据的传递
     */
    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        MainBean oldBean = mOldData.get(oldItemPosition);
        MainBean newBean = mNewData.get(newItemPosition);
        if (!oldBean.getName().equals(newBean.getName())) {
            return newBean;
        }
        return null;
    }
}
