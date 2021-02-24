package com.example.recyclerview.recycler.slide;

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class BaseItemTouchHelpCallback extends ItemTouchHelper.Callback {

    private BaseSlideAdapter mAdapter;

    public BaseItemTouchHelpCallback(BaseSlideAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    /**
     * 设置拖拽或滑动方向
     * 当用户拖拽或者滑动Item的时候需要告诉系统拖拽或者滑动的方向
     */
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(mDragMoveFlags, mSwipeMoveFlags);
    }

    /**
     * 滑动或拖动开始时调用
     *
     * @param viewHolder  滑动或拖动的ViewHolder
     * @param actionState ViewHolder的状态，ACTION_STATE_DRAG拖拽,ACTION_STATE_SWIPE滑动
     */
    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                mAdapter.onItemDragStart(viewHolder);
            } else if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                mAdapter.onItemSwipeStart(viewHolder);
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    /**
     * 滑动或拖动结束时调用
     *
     * @param recyclerView recyclerView
     * @param viewHolder   滑动或拖动的ViewHolder
     */
    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        //重置改变，防止由于复用而导致的显示问题
        viewHolder.itemView.setScrollX(0);

        if (viewHolder.itemView.getTag(BaseSlideAdapter.TAG) != null) {
            mAdapter.onItemDragEnd(viewHolder);
        }
        if (viewHolder.itemView.getTag(BaseSlideAdapter.TAG) != null) {
            mAdapter.onItemSwipeClear(viewHolder);
        }
    }

    /********************************************拖拽**************************************************/
    /**
     * 拖拽方向：上、下、左、右
     */
    private int mDragMoveFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;

    public void setDragMoveFlags(int dragMoveFlags) {
        mDragMoveFlags = dragMoveFlags;
    }

    /**
     * 是否可以通过长按来触发拖动操作，
     *
     * @return 默认返回true，如果返回false，那么可以通过startDrag(ViewHolder)方法来触发某个特定Item的拖动的机制
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    /**
     * 拖拽的时候移动多少距离就判定为可以交换了，默认0.5F
     */
    private float mMoveThreshold = 0.5f;

    public void setMoveThreshold(float moveThreshold) {
        mMoveThreshold = moveThreshold;
    }

    @Override
    public float getMoveThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return mMoveThreshold;
    }

    /**
     * 当Item被拖拽的时候回调
     *
     * @param recyclerView recyclerView
     * @param source       拖拽的viewHolder
     * @param target       目的拖拽viewHolder
     * @return true: ViewType相同调用onMoved；设置source.getItemViewType() == target.getItemViewType();不同 ViewType 之间不能进行拖拽
     */
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder source, @NonNull RecyclerView.ViewHolder target) {
        return true;
    }

    /**
     * 当onMove返回true时回调,刷新列表
     *
     * @param recyclerView recyclerView
     * @param source       拖拽的viewHolder
     * @param fromPos
     * @param target       目的拖拽viewHolder
     * @param toPos
     * @param x
     * @param y
     */
    @Override
    public void onMoved(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder source, int fromPos, @NonNull RecyclerView.ViewHolder target, int toPos, int x, int y) {
        super.onMoved(recyclerView, source, fromPos, target, toPos, x, y);
        mAdapter.onItemDragMoving(source, target);
    }

    /********************************************滑动**************************************************/
    /**
     * 滑动方向：右滑
     */
    private int mSwipeMoveFlags = ItemTouchHelper.START;

    public void setSwipeMoveFlags(int swipeMoveFlags) {
        mSwipeMoveFlags = swipeMoveFlags;
    }

    /**
     * 是否启动在任意位置滑动
     *
     * @return true:在任意位置发生触摸事件时启用滑动操作
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return mAdapter.isItemSwipeEnable();
    }

    /**
     * 滑动的时候移动多少距离就判定为可以交换了
     */
    private float mSwipeThreshold = 0.7f;

    public void setSwipeThreshold(float swipeThreshold) {
        mSwipeThreshold = swipeThreshold;
    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return mSwipeThreshold;
    }

    /**
     * 当Item被滑动的时候回调
     *
     * @param viewHolder 要删除的item
     * @param direction  表示滑动的方向
     */
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemSwiped(viewHolder);
    }

    /**
     * 自定义侧滑删除动画
     *
     * @param c
     * @param recyclerView      recyclerView
     * @param viewHolder        正在与用户交互的ViewHolder
     * @param dX                侧滑水平位移量
     * @param dY                侧滑垂直位移量
     * @param actionState       状态
     * @param isCurrentlyActive
     */
    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            View itemView = viewHolder.itemView;

            c.save();
            if (dX > 0) {
                c.clipRect(itemView.getLeft(), itemView.getTop(),
                        itemView.getLeft() + dX, itemView.getBottom());
                c.translate(itemView.getLeft(), itemView.getTop());
            } else {
                c.clipRect(itemView.getRight() + dX, itemView.getTop(),
                        itemView.getRight(), itemView.getBottom());
                c.translate(itemView.getRight() + dX, itemView.getTop());
            }

            mAdapter.onItemSwiping(c, viewHolder, dX, dY, isCurrentlyActive);
            c.restore();
        }
    }
}
