package com.example.recyclerview.recycler.slide;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerview.recycler.BaseQuickAdapter;
import com.example.recyclerview.recycler.BaseViewHolder;

import java.util.Collections;
import java.util.List;

public abstract class BaseSlideAdapter<T, VH extends BaseViewHolder> extends BaseQuickAdapter<T, VH> {

    //会报The key must be an application-specific resource id.修改成唯一
    public static final int TAG = 0x10000000;
    private static final int NO_TOGGLE_VIEW = 0;
    private int mToggleViewId = NO_TOGGLE_VIEW;
    private ItemTouchHelper mItemTouchHelper;
    private boolean itemDragEnabled = false;
    private boolean itemSwipeEnabled = false;
    private OnItemDragListener mOnItemDragListener;
    private OnItemSwipeListener mOnItemSwipeListener;
    private boolean mDragOnLongPress = true;

    private View.OnTouchListener mOnToggleViewTouchListener;
    private View.OnLongClickListener mOnToggleViewLongClickListener;

    public BaseSlideAdapter(int layoutResId, List<T> data) {
        super(layoutResId, data);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        super.onBindViewHolder(holder, position);
        if (mItemTouchHelper != null && itemDragEnabled) {
            if (mToggleViewId != NO_TOGGLE_VIEW) {
                View toggleView = holder.getView(mToggleViewId);
                if (toggleView != null) {
                    toggleView.setTag(TAG, holder);
                    if (mDragOnLongPress) {
                        toggleView.setOnLongClickListener(mOnToggleViewLongClickListener);
                    } else {
                        toggleView.setOnTouchListener(mOnToggleViewTouchListener);
                    }
                }
            } else {
                holder.itemView.setTag(TAG, holder);
                holder.itemView.setOnLongClickListener(mOnToggleViewLongClickListener);
            }
        }
    }

    public int getViewHolderPosition(RecyclerView.ViewHolder viewHolder) {
        return viewHolder.getAdapterPosition();
    }

    /*******************************************************拖动*****************************************************************/

    /**
     * 开启拖动item功能
     * 长按时使用itemView作为toggleView。
     *
     * @param itemTouchHelper {@link ItemTouchHelper}
     */
    public void enableDragItem(@NonNull ItemTouchHelper itemTouchHelper) {
        enableDragItem(itemTouchHelper, NO_TOGGLE_VIEW, true);
    }

    /**
     * 禁用拖动item功能
     */
    public void disableDragItem() {
        itemDragEnabled = false;
        mItemTouchHelper = null;
    }

    /**
     * 开启拖动item的子view的功能
     * 长按时使用传入的 toggleViewId 作为toggleView。
     *
     * @param itemTouchHelper {@link ItemTouchHelper}
     * @param toggleViewId    被拖动的itemView的子view的id
     * @param dragOnLongPress 如果为true，则拖动事件将在长按时触发，否则将在点击时触发。
     */
    public void enableDragItem(@NonNull ItemTouchHelper itemTouchHelper, int toggleViewId, boolean dragOnLongPress) {
        itemDragEnabled = true;
        mItemTouchHelper = itemTouchHelper;
        setToggleViewId(toggleViewId);
        setToggleDragOnLongPress(dragOnLongPress);
    }

    /**
     * 设置长按子View：mToggleViewId触发拖动事件
     * 如果未设置mToggleViewId，则在长按item时触发拖动事件
     */
    public void setToggleViewId(int toggleViewId) {
        mToggleViewId = toggleViewId;
    }

    public boolean isItemDraggable() {
        return itemDragEnabled;
    }

    /**
     * 如果为true，则拖动事件将在长按时触发，否则将在点击时触发。
     *
     * @param longPress 默认为true
     */
    public void setToggleDragOnLongPress(boolean longPress) {
        mDragOnLongPress = longPress;
        if (mDragOnLongPress) {
            mOnToggleViewTouchListener = null;
            mOnToggleViewLongClickListener = new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mItemTouchHelper != null && itemDragEnabled) {
                        mItemTouchHelper.startDrag((RecyclerView.ViewHolder) v.getTag(TAG));
                    }
                    return true;
                }
            };
        } else {
            mOnToggleViewTouchListener = new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN
                            && !mDragOnLongPress) {
                        if (mItemTouchHelper != null && itemDragEnabled) {
                            mItemTouchHelper.startDrag((RecyclerView.ViewHolder) v.getTag(TAG));
                        }
                        return true;
                    } else {
                        return false;
                    }
                }
            };
            mOnToggleViewLongClickListener = null;
        }
    }

    /**
     * 拖动事件，在Activity中调用mAdapter.setOnItemDragListener(onItemDragListener)
     *
     * @param onItemDragListener 拖动事件的回调。
     */
    public void setOnItemDragListener(OnItemDragListener onItemDragListener) {
        mOnItemDragListener = onItemDragListener;
    }

    /**
     * 拖动接口
     */
    public interface OnItemDragListener {
        //拖动开始
        void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos);

        void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to);

        //拖动结束
        void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos);
    }

    /**
     * 拖动开始
     */
    public void onItemDragStart(RecyclerView.ViewHolder viewHolder) {
        if (mOnItemDragListener != null && itemDragEnabled) {
            mOnItemDragListener.onItemDragStart(viewHolder, getViewHolderPosition(viewHolder));
        }
    }

    /**
     * 拖动中
     */
    public void onItemDragMoving(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        int from = getViewHolderPosition(source);
        int to = getViewHolderPosition(target);

        if (inRange(from) && inRange(to)) {
            if (from < to) {
                for (int i = from; i < to; i++) {
                    Collections.swap(data, i, i + 1);
                }
            } else {
                for (int i = from; i > to; i--) {
                    Collections.swap(data, i, i - 1);
                }
            }
            notifyItemMoved(source.getAdapterPosition(), target.getAdapterPosition());
        }

        if (mOnItemDragListener != null && itemDragEnabled) {
            mOnItemDragListener.onItemDragMoving(source, from, target, to);
        }
    }

    /**
     * 拖动结束
     */
    public void onItemDragEnd(RecyclerView.ViewHolder viewHolder) {
        if (mOnItemDragListener != null && itemDragEnabled) {
            mOnItemDragListener.onItemDragEnd(viewHolder, getViewHolderPosition(viewHolder));
        }
    }

    private boolean inRange(int position) {
        return position >= 0 && position < data.size();
    }

    /*******************************************************滑动*****************************************************************/
    /**
     * <p>启用滑动item功能</p>
     */
    public void enableSwipeItem() {
        itemSwipeEnabled = true;
    }

    public void disableSwipeItem() {
        itemSwipeEnabled = false;
    }

    public boolean isItemSwipeEnable() {
        return itemSwipeEnabled;
    }

    /**
     * 滑动接口
     */
    public interface OnItemSwipeListener {
        //滑动开始
        void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos);

        /**
         * 滑动结束
         * 如果在onItemSwipeStart中更改视图该方法要重置
         *
         * @param pos 如果视图被刷过，则pos将为负数。
         */
        void clearView(RecyclerView.ViewHolder viewHolder, int pos);

        //当Item被滑动的时候回调
        void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos);
        //自定义侧滑删除动画
        void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive);
    }

    /**
     * 滑动事件,在Activity中调用mAdapter.setOnItemSwipeListener(OnItemSwipeListener)
     *
     * @param listener 滑动事件的回调
     */
    public void setOnItemSwipeListener(OnItemSwipeListener listener) {
        mOnItemSwipeListener = listener;
    }

    /**
     * 滑动开始
     */
    public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder) {
        if (mOnItemSwipeListener != null && itemSwipeEnabled) {
            mOnItemSwipeListener.onItemSwipeStart(viewHolder, getViewHolderPosition(viewHolder));
        }
    }

    /**
     * 滑动结束
     */
    public void onItemSwipeClear(RecyclerView.ViewHolder viewHolder) {
        if (mOnItemSwipeListener != null && itemSwipeEnabled) {
            mOnItemSwipeListener.clearView(viewHolder, getViewHolderPosition(viewHolder));
        }
    }

    /**
     * 滑动刷新项目时调用，视图将从适配器中删除。
     */
    public void onItemSwiped(RecyclerView.ViewHolder viewHolder) {
        if (mOnItemSwipeListener != null && itemSwipeEnabled) {
            mOnItemSwipeListener.onItemSwiped(viewHolder, getViewHolderPosition(viewHolder));
        }
    }

    public void removeItem(int pos){
        data.remove(pos);
        notifyItemRemoved(pos);
    }

    /**
     * 自定义侧滑删除动画
     *
     * @param canvas     空白的画布
     * @param viewHolder 正在与用户交互的ViewHolder
     * @param dX         侧滑水平位移量
     * @param dY         侧滑垂直位移量
     */
    public void onItemSwiping(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
        if (mOnItemSwipeListener != null && itemSwipeEnabled) {
            mOnItemSwipeListener.onItemSwipeMoving(canvas, viewHolder, dX, dY, isCurrentlyActive);
        }
    }
}
