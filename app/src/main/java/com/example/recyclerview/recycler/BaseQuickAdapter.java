package com.example.recyclerview.recycler;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.IntRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

public abstract class BaseQuickAdapter<T, VH extends BaseViewHolder> extends RecyclerView.Adapter<VH> {

    protected abstract void convert(@NonNull VH holder, T item, int position);

    /**
     * 可选实现，如果你是用了[payloads]刷新item，请实现此方法，进行局部刷新
     */
    protected void update(@NonNull VH holder, T item, List<Object> payloads) {

    }

    private Context context;
    //点击位置
    private int clickLocation = 0;

    public List<T> data;
    //单布局id
    private int layoutResId;
    //是否是多布局
    protected boolean isMultiItem;
    //多布局所有的id集合
    protected SparseIntArray layouts;

    /**
     * 单布局构造函数
     */
    public BaseQuickAdapter(@LayoutRes int layoutResId, List<T> data) {
        isMultiItem = false;
        this.data = data;
        this.layoutResId = layoutResId;
    }

    /**
     * 多布局
     */
    public BaseQuickAdapter(List<BaseMultiItemEntity> data) {
        isMultiItem = true;
        this.data = (List<T>) data;
        layouts = new SparseIntArray();
        for (BaseMultiItemEntity t : data) {
            layouts.put(t.getItemType(), t.getItemType());
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();

        //获取布局View
        View view;
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        if (isMultiItem) {
            view = mLayoutInflater.inflate(getLayoutId(viewType), viewGroup, false);
        } else {
            view = mLayoutInflater.inflate(layoutResId, viewGroup, false);
        }
        //获取ViewHolder
        return createBaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        T item = null;
        if (position >= 0 && position < data.size()) {
            item = data.get(position);
        }
        //设置item的点击事件/长按事件
        bindViewClickListener(holder, position);
        convert(holder, item, position);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
            return;
        }
        T item = null;
        if (position >= 0 && position < data.size()) {
            item = data.get(position);
        }
        //设置item的点击事件/长按事件
        bindViewClickListener(holder, position);
        update(holder, item, payloads);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * 获取多布局中指定多布局
     */
    protected int getLayoutId(@LayoutRes int viewType) {
        return layouts.get(viewType);
    }

    /**
     * 如果要在适配器中使用BaseViewHolder的子类，
     * 必须覆盖该方法以创建新的ViewHolder。
     */
    protected VH createBaseViewHolder(View view) {
        Class<?> temp = getClass();
        Class<?> z = null;
        while (z == null && null != temp) {
            z = getInstancedGenericKClass(temp);
            temp = temp.getSuperclass();
        }
        VH vh;
        // 泛型擦除会导致z为null
        if (z == null) {
            vh = (VH) new BaseViewHolder(view);
        } else {
            vh = createGenericKInstance(z, view);
        }
        return vh != null ? vh : (VH) new BaseViewHolder(view);
    }

    /**
     * 得到通用参数.
     */
    private Class<?> getInstancedGenericKClass(Class<?> z) {
        Type type = z.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            for (Type temp : types) {
                if (temp instanceof Class) {
                    Class<?> tempClass = (Class<?>) temp;
                    if (BaseViewHolder.class.isAssignableFrom(tempClass)) {
                        return tempClass;
                    }
                } else if (temp instanceof ParameterizedType) {
                    Type rawType = ((ParameterizedType) temp).getRawType();
                    if (rawType instanceof Class && BaseViewHolder.class.isAssignableFrom((Class<?>) rawType)) {
                        return (Class<?>) rawType;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 创建新的ViewHolder
     */
    private VH createGenericKInstance(Class<?> z, View view) {
        try {
            Constructor<?> constructor;
            if (z.isMemberClass() && !Modifier.isStatic(z.getModifiers())) {
                constructor = z.getDeclaredConstructor(getClass(), View.class);
                constructor.setAccessible(true);
                return (VH) constructor.newInstance(this, view);
            } else {
                constructor = z.getDeclaredConstructor(View.class);
                constructor.setAccessible(true);
                return (VH) constructor.newInstance(view);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * item的点击事件/长按事件
     */
    private void bindViewClickListener(final VH baseViewHolder, final int position) {
        if (baseViewHolder == null) {
            return;
        }
        final View view = baseViewHolder.itemView;
        //事件
        if (onItemClickListener != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(view, position);
                }
            });
        }
        if (onItemLongClickListener != null) {
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return onItemLongClickListener.onItemLongClick(view, position);
                }
            });
        }
        if (mOnItemChildClickListener != null) {
            for (int id : childClickViewIds) {
                View childView = view.findViewById(id);
                if (!childView.isClickable()) {
                    childView.setClickable(true);
                }
                childView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemChildClickListener.onItemChildClick(BaseQuickAdapter.this, v, position);
                    }
                });
            }
        }
        if (onItemChildLongClickListener != null) {
            for (int id : childLongClickViewIds) {
                View childView = view.findViewById(id);
                if (!childView.isLongClickable()) {
                    childView.setLongClickable(true);
                }
                childView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return onItemChildLongClickListener.onItemChildLongClick(BaseQuickAdapter.this, v, position);
                    }
                });
            }
        }
    }

    /**
     * item 点击事件
     */
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * item 长按事件
     */
    private OnItemLongClickListener onItemLongClickListener;

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    /**
     * 用于保存需要设置点击事件的 item
     */
    private final LinkedHashSet<Integer> childClickViewIds = new LinkedHashSet<>();

    public LinkedHashSet<Integer> getChildClickViewIds() {
        return childClickViewIds;
    }

    /**
     * 设置需要点击事件的子view
     */
    public void addChildClickViewIds(@IdRes int... viewIds) {
        for (int viewId : viewIds) {
            childClickViewIds.add(viewId);
        }
    }

    private OnItemChildClickListener mOnItemChildClickListener;

    public interface OnItemChildClickListener {
        void onItemChildClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position);
    }

    public void setOnItemChildClickListener(OnItemChildClickListener listener) {
        mOnItemChildClickListener = listener;
    }

    /**
     * 用于保存需要设置长按点击事件的 item
     */
    private final LinkedHashSet<Integer> childLongClickViewIds = new LinkedHashSet<>();

    public LinkedHashSet<Integer> getChildLongClickViewIds() {
        return childLongClickViewIds;
    }

    /**
     * 设置需要长按点击事件的子view
     */
    public void addChildLongClickViewIds(@IdRes int... viewIds) {
        for (int viewId : viewIds) {
            childLongClickViewIds.add(viewId);
        }
    }

    private OnItemChildLongClickListener onItemChildLongClickListener;

    public interface OnItemChildLongClickListener {
        boolean onItemChildLongClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position);
    }

    public void setOnItemChildLongClickListener(OnItemChildLongClickListener listener) {
        onItemChildLongClickListener = listener;
    }

    public Context getContext() {
        return context;
    }

    /**
     * 设置点击位置
     */
    public void setClickLocation(int clickLocation) {
        if (this.clickLocation != clickLocation) {
            //获取前一次点击的position
            int beforePosition = this.clickLocation;
            //获取当前点击position
            this.clickLocation = clickLocation;
            //刷新点击项
            notifyItemChanged(clickLocation);
            if (beforePosition >= 0) {
                //刷新前一次点击位置
                notifyItemChanged(beforePosition);
            }
        }
    }

    /**
     * 获取点击位置
     */
    public int getClickLocation() {
        return clickLocation;
    }

    /**
     * 获取默认位置
     */
    public void setDefaultPosition(int position) {
        clickLocation = position;
    }

    /**
     * 获取指定位置数据
     */
    public T getItem(@IntRange(from = 0) int position) {
        return data.get(position);
    }

    /**
     * 获取指定元素的位置
     * 如果返回 -1，表示不存在
     */
    public int getItemPosition(T item) {
        if (item != null && !data.isEmpty()) {
            return data.indexOf(item);
        }
        return -1;
    }

    /**
     * 在列表最后添加一个item
     *
     * @param element item的数据
     */
    public void addNewItem(@NonNull T element) {
        data.add(element);
        notifyItemInserted(data.size());
        compatibilityDataSizeChanged(1);
    }

    /**
     * 在指定位置添加一个item，如果position越界则添加到列表最后
     *
     * @param position item添加的位置
     * @param element  item的数据
     */
    public void addNewItem(@IntRange(from = 0) int position, @NonNull T element) {
        if (position < data.size()) {
            data.add(position, element);
            //更新数据集用notifyItemInserted(position)与notifyItemRemoved(position) 否则没有动画效果。
            //首个Item位置做增加操作
            notifyItemInserted(position);
            compatibilityDataSizeChanged(1);
        } else {
            addNewItem(element);
        }
    }

    /**
     * 在指定位置添加多个item，如果position越界则添加到列表最后
     * 示例：mAdapter.addData(1, listOf(Person("xin","1"),Person("xin","2")))
     *
     * @param position 插入位置
     * @param newData  多个item的集合
     */
    public void addNewData(@IntRange(from = 0) int position, @NonNull Collection<? extends T> newData) {
        if (position < data.size()) {
            data.addAll(position, newData);
            notifyItemRangeInserted(position, newData.size());
            compatibilityDataSizeChanged(newData.size());
        } else {
            addNewData(newData);
        }
    }

    /**
     * 在列表最后添加多个item
     * 示例：mAdapter.addData(listOf(Person("xin","1"),Person("xin","2")))
     *
     * @param newData 多个item的集合
     */
    public void addNewData(@NonNull Collection<? extends T> newData) {
        data.addAll(newData);
        notifyItemRangeInserted(data.size() - newData.size(), newData.size());
        compatibilityDataSizeChanged(newData.size());
    }

    /**
     * 删除一个指定位置的item，如果position越界则无效
     *
     * @param position 指定item的位置
     */
    public void removeItem(@IntRange(from = 0) int position) {
        if (position < data.size()) {
            data.remove(position);
            notifyItemRemoved(position);
            compatibilityDataSizeChanged(0);
            notifyItemRangeChanged(position, data.size() - position);
        }
    }

    /**
     * 删除一个指定位置的元素，如果position越界则无效
     */
    public void removeItem(T element) {
        int index = data.indexOf(element);
        if (index == -1) {
            return;
        }
        removeItem(index);
    }

    /**
     * 修改某个item的数据，如果position越界则无效
     *
     * @param position item位置
     * @param element  要修改的item数据
     */
    public void replaceItem(@IntRange(from = 0) int position, @NonNull T element) {
        if (position < data.size()) {
            data.set(position, element);
            notifyItemChanged(position);
        }
    }

    /**
     * 修改所有item的数据
     *
     * @param newData 更新item数据的集合
     */
    public void replaceData(Collection<? extends T> newData) {
        if (newData == null || newData.size() == 0) {
            data.clear();
            notifyDataSetChanged();
            return;
        }
        // 不是同一个引用才清空列表
        if (newData != data) {
            data.clear();
            data.addAll(newData);
        }
        notifyDataSetChanged();
    }

    private void compatibilityDataSizeChanged(int size) {
        int dataSize = data == null ? 0 : data.size();
        if (dataSize == size) {
            notifyDataSetChanged();
        }
    }

    public void setDiffData(DiffUtil.DiffResult result, List<T> newList) {
        data.clear();
        data.addAll(newList);
        result.dispatchUpdatesTo(this);
    }
}
