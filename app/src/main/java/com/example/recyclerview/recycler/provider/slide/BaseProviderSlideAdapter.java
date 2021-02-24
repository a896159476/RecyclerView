package com.example.recyclerview.recycler.provider.slide;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.recyclerview.recycler.BaseViewHolder;
import com.example.recyclerview.recycler.slide.BaseSlideAdapter;

import java.util.List;

public abstract class BaseProviderSlideAdapter<T, VH extends BaseViewHolder> extends BaseSlideAdapter<T, VH> {

    public BaseProviderSlideAdapter(List<T> data) {
        super(0, data);
    }

    private final SparseArray<BaseItemSlideProvider<T, VH>> mItemProviders = new SparseArray<>();

    /**
     * 返回 item 类型
     */
    protected abstract int getItemType(List<T> data, int position);

    /**
     * 必须通过此方法，添加 provider
     *
     * @param provider BaseItemSlideProvider
     */
    public void addItemProvider(BaseItemSlideProvider<T, VH> provider) {
        provider.setAdapter(this);
        mItemProviders.put(provider.itemViewType, provider);
    }

    @Override
    protected VH onCreateDefViewHolder(ViewGroup parent, int viewType) {
        BaseItemSlideProvider<T, VH> provider = getItemProvider(viewType);
        provider.context = parent.getContext();
        VH baseViewHolder = provider.onCreateViewHolder(parent, viewType);
        provider.onViewHolderCreated(baseViewHolder, viewType);
        return baseViewHolder;
    }

    public int getDefItemViewType(int position) {
        return getItemType(data, position);
    }

    public void convert(VH holder, T item, int position) {
        getItemProvider(holder.getItemViewType()).convert(holder, item, position);
    }

    public void update(VH holder, T item, List<Object> payloads) {
        getItemProvider(holder.getItemViewType()).update(holder, item, payloads);
    }

    public void bindViewClickListener(VH viewHolder, int position) {
        super.bindViewClickListener(viewHolder, position);
        bindClick(viewHolder);
        bindChildClick(viewHolder);
    }

    protected void bindClick(final VH viewHolder) {
        if (getOnItemClickListener() == null) {
            //如果没有设置点击监听，则回调给 itemProvider
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = viewHolder.getAdapterPosition();
                    int itemViewType = viewHolder.getItemViewType();
                    BaseItemSlideProvider<T, VH> provider = mItemProviders.get(itemViewType);
                    provider.onClick(viewHolder, v, data.get(position), position);
                }
            });
        }
        if (getOnItemLongClickListener() == null) {
            //如果没有设置长按监听，则回调给itemProvider
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = viewHolder.getAdapterPosition();
                    int itemViewType = viewHolder.getItemViewType();
                    BaseItemSlideProvider<T, VH> provider = mItemProviders.get(itemViewType);
                    return provider.onLongClick(viewHolder, v, data.get(position), position);
                }
            });
        }
    }

    protected void bindChildClick(final VH viewHolder) {
        if (getOnItemChildClickListener() == null) {
            int itemViewType = viewHolder.getItemViewType();
            final BaseItemSlideProvider<T, VH> provider = mItemProviders.get(itemViewType);
            if (provider == null) {
                return;
            }
            List<Integer> ids = provider.getChildClickViewIds();
            for (int id : ids) {
                View childView = viewHolder.itemView.findViewById(id);
                if (!childView.isClickable()) {
                    childView.setClickable(true);
                }
                childView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = viewHolder.getAdapterPosition();
                        provider.onChildClick(viewHolder, v, data.get(position), position);
                    }
                });
            }
        }
        if (getOnItemChildLongClickListener() == null) {
            int itemViewType = viewHolder.getItemViewType();
            final BaseItemSlideProvider<T, VH> provider = mItemProviders.get(itemViewType);
            if (provider == null) {
                return;
            }
            List<Integer> ids = provider.getChildLongClickViewIds();
            for (int id : ids) {
                View childView = viewHolder.itemView.findViewById(id);
                if (!childView.isLongClickable()) {
                    childView.setLongClickable(true);
                }
                childView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int position = viewHolder.getAdapterPosition();
                        return provider.onChildLongClick(viewHolder, v, data.get(position), position);
                    }
                });
            }
        }
    }

    /**
     * 通过 ViewType 获取 BaseItemSlideProvider
     * 例如：如果ViewType经过特殊处理，可以重写此方法，获取正确的Provider
     * （比如 ViewType 通过位运算进行的组合的）
     *
     * @param viewType Int
     * @return BaseItemSlideProvider
     */
    protected BaseItemSlideProvider<T, VH> getItemProvider(int viewType) {
        return mItemProviders.get(viewType);
    }

    public void onViewAttachedToWindow(@NonNull VH holder) {
        super.onViewAttachedToWindow(holder);
        getItemProvider(holder.getItemViewType()).onViewAttachedToWindow(holder);
    }

    public void onViewDetachedFromWindow(@NonNull VH holder) {
        super.onViewDetachedFromWindow(holder);
        getItemProvider(holder.getItemViewType()).onViewDetachedFromWindow(holder);
    }
}
