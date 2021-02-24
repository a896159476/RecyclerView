package com.example.recyclerview.recycler.provider.slide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;

import com.example.recyclerview.recycler.BaseViewHolder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseItemSlideProvider<T, VH extends BaseViewHolder> {
    public Context context;
    private WeakReference<BaseProviderSlideAdapter<T, VH>> weakAdapter;
    private final List<Integer> clickViewIds = new ArrayList<>();
    private final List<Integer> longClickViewIds = new ArrayList<>();

    protected void setAdapter(BaseProviderSlideAdapter<T, VH> adapter) {
        weakAdapter = new WeakReference<>(adapter);
    }

    public BaseProviderSlideAdapter<T, VH> getAdapter() {
        return weakAdapter.get();
    }

    public int itemViewType = getItemViewType();

    public abstract int getItemViewType();

    private final int layoutId = getLayoutId();

    public abstract int getLayoutId();

    public abstract void convert(VH holder, T item, int position);

    public void update(VH holder, T item, List<Object> payloads) {
    }

    /**
     * （可选重写）创建 ViewHolder。
     * 默认实现返回[BaseViewHolder]，可重写返回自定义 ViewHolder
     */
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return (VH) new BaseViewHolder(getItemView(layoutId, parent));
    }

    public View getItemView(@LayoutRes int layoutResId, ViewGroup parent) {
        return LayoutInflater.from(this.context).inflate(layoutResId, parent, false);
    }

    /**
     * （可选重写）ViewHolder创建完毕以后的回掉方法。
     *
     * @param viewHolder VH
     */
    public void onViewHolderCreated(VH viewHolder, int viewType) {
    }

    /**
     * Called when a view created by this [BaseItemProvider] has been attached to a window.
     * 当此[BaseItemProvider]出现在屏幕上的时候，会调用此方法
     * <p>
     * This can be used as a reasonable signal that the view is about to be seen
     * by the user. If the [BaseItemProvider] previously freed any resources in
     * [onViewDetachedFromWindow][.onViewDetachedFromWindow]
     * those resources should be restored here.
     *
     * @param holder Holder of the view being attached
     */
    public void onViewAttachedToWindow(VH holder) {
    }

    /**
     * Called when a view created by this [BaseItemProvider] has been detached from its
     * window.
     * 当此[BaseItemProvider]从屏幕上移除的时候，会调用此方法
     * <p>
     * Becoming detached from the window is not necessarily a permanent condition;
     * the consumer of an Adapter's views may choose to cache views offscreen while they
     * are not visible, attaching and detaching them as appropriate.
     *
     * @param holder Holder of the view being detached
     */
    public void onViewDetachedFromWindow(VH holder) {
    }

    /**
     * item 若想实现条目点击事件则重写该方法
     *
     * @param helper   VH
     * @param data     T
     * @param position Int
     */
    public void onClick(VH helper, View view, T data, int position) {
    }

    /**
     * item 若想实现条目长按事件则重写该方法
     *
     * @param helper   VH
     * @param data     T
     * @param position Int
     * @return Boolean
     */
    public boolean onLongClick(VH helper, View view, T data, int position) {
        return false;
    }

    /**
     * 子View 若想实现点击事件则重写该方法
     */
    public void onChildClick(VH helper, View view, T data, int position) {
    }

    /**
     * 子View 若想实现长按事件则重写该方法
     */
    public boolean onChildLongClick(VH helper, View view, T data, int position) {
        return false;
    }

    public void addChildClickViewIds(@IdRes int... ids) {
        for (int id : ids) {
            clickViewIds.add(id);
        }
    }

    public List<Integer> getChildClickViewIds() {
        return clickViewIds;
    }

    public void addChildLongClickViewIds(@IdRes int... ids) {
        for (int id : ids) {
            longClickViewIds.add(id);
        }
    }

    public List<Integer> getChildLongClickViewIds() {
        return longClickViewIds;
    }
}

