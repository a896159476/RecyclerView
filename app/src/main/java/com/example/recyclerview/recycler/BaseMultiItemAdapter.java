package com.example.recyclerview.recycler;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import java.util.Collection;
import java.util.List;

public abstract class BaseMultiItemAdapter<T extends BaseMultiItemEntity, VH extends BaseViewHolder> extends BaseQuickAdapter<T, VH> {

    public BaseMultiItemAdapter(List<T> data) {
        super((List<BaseMultiItemEntity>) data);
    }

    @Override
    protected int getDefItemViewType(int position) {
        return data.get(position).getItemType();
    }

    /**
     * 添加新布局
     *
     * @param viewType 新布局
     */
    public void setLayouts(@LayoutRes int viewType) {
        layouts.put(viewType, viewType);
    }

    /**
     * 是否是新布局
     *
     * @param element BaseMultiItemEntity
     */
    private void isNewLayout(@NonNull T element) {
        if (isMultiItem) {
            int itemType = element.getItemType();
            if (getLayoutId(itemType) == 0) {
                layouts.put(itemType, itemType);
            }
        }
    }

    /**
     * 是否是新布局
     *
     * @param newData BaseMultiItemEntity集合
     */
    private void isNewLayout(@NonNull Collection<? extends T> newData) {
        if (isMultiItem) {
            for (T element : newData) {
                int itemType = element.getItemType();
                if (getLayoutId(itemType) == 0) {
                    layouts.put(itemType, itemType);
                }
            }
        }
    }

    @Override
    public void addNewItem(@NonNull T element) {
        isNewLayout(element);
        super.addNewItem(element);
    }

    @Override
    public void addNewItem(int position, @NonNull T element) {
        isNewLayout(element);
        super.addNewItem(position, element);
    }

    @Override
    public void addNewData(@NonNull Collection<? extends T> newData) {
        isNewLayout(newData);
        super.addNewData(newData);
    }

    @Override
    public void addNewData(int position, @NonNull Collection<? extends T> newData) {
        isNewLayout(newData);
        super.addNewData(position, newData);
    }

    @Override
    public void removeItem(int position) {
        super.removeItem(position);
    }

    @Override
    public void replaceItem(int position, @NonNull T element) {
        isNewLayout(element);
        super.replaceItem(position, element);
    }

    @Override
    public void replaceData(Collection<? extends T> newData) {
        isNewLayout(newData);
        super.replaceData(newData);
    }
}
