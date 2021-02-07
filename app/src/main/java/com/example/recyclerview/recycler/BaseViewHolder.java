package com.example.recyclerview.recycler;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BaseViewHolder extends RecyclerView.ViewHolder {

    private BaseQuickAdapter<?, ?> adapter;
    private final SparseArray<View> views = new SparseArray<>();

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void setAdapter(BaseQuickAdapter<?, ?> adapter) {
        this.adapter = adapter;
    }

    /**
     * 获取item的view
     *
     * @param viewId view的id
     */
    public <T extends View> T getView(@IdRes int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 设置EditText的文字
     *
     * @param viewId TextView的id
     * @param value  文本内容
     */
    public BaseViewHolder setEditText(@IdRes int viewId, CharSequence value) {
        EditText view = getView(viewId);
        view.setText(value);
        return this;
    }

    /**
     * 设置TextView的文字
     *
     * @param viewId TextView的id
     * @param value  文本内容
     */
    public BaseViewHolder setText(@IdRes int viewId, CharSequence value) {
        TextView view = getView(viewId);
        view.setText(value);
        return this;
    }

    /**
     * 设置TextView的文字颜色
     *
     * @param viewId    TextView的id
     * @param textColor 文字颜色
     */
    public BaseViewHolder setTextColor(@IdRes int viewId, @ColorInt int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    /**
     * 设置TextView的文字颜色
     *
     * @param viewId   TextView的id
     * @param colorRes 文字颜色
     */
    public BaseViewHolder setTextColorRes(@IdRes int viewId, @ColorRes int colorRes) {
        TextView view = getView(viewId);
        view.setTextColor(itemView.getResources().getColor(colorRes));
        return this;
    }

    /**
     * 为TextView创建超链接
     *
     * @param viewId TextView的id
     */
    public BaseViewHolder addLinks(@IdRes int viewId) {
        TextView view = getView(viewId);
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    /**
     * 设置TextView的字体，并启用子像素渲染。
     *
     * @param viewId   TextView的id
     * @param typeface 字体
     */
    public BaseViewHolder setTypeface(@IdRes int viewId, Typeface typeface) {
        TextView view = getView(viewId);
        view.setTypeface(typeface);
        view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        return this;
    }

    /**
     * 批量设置TextView的字体，并启用子像素渲染。
     *
     * @param typeface 字体
     * @param viewIds  TextView的id
     */
    public BaseViewHolder setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = getView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }

    /**
     * 设置ImageView的图片
     *
     * @param viewId     ImageView的id
     * @param imageResId 资源文件id
     */
    public BaseViewHolder setImageResource(@IdRes int viewId, @DrawableRes int imageResId) {
        ImageView view = getView(viewId);
        view.setImageResource(imageResId);
        return this;
    }

    /**
     * 设置ImageView的图片
     *
     * @param viewId ImageView的id
     * @param bitmap bitmap
     */
    public BaseViewHolder setImageBitmap(@IdRes int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    public BaseViewHolder setBackgroundColor(@IdRes int viewId, @ColorInt int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public BaseViewHolder setBackgroundResource(@IdRes int viewId, @DrawableRes int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    public BaseViewHolder setVisible(@IdRes int... viewIds) {
        for (int viewId : viewIds) {
            View view = getView(viewId);
            view.setVisibility(View.VISIBLE);
        }
        return this;
    }

    public BaseViewHolder setGone(@IdRes int... viewIds) {
        for (int viewId : viewIds) {
            View view = getView(viewId);
            view.setVisibility(View.GONE);
        }
        return this;
    }

    public BaseViewHolder setInVisible(@IdRes int... viewIds) {
        for (int viewId : viewIds) {
            View view = getView(viewId);
            view.setVisibility(View.INVISIBLE);
        }
        return this;
    }

    public BaseViewHolder setEnabled(@IdRes int viewId, boolean enable) {
        View view = getView(viewId);
        view.setEnabled(enable);
        return this;
    }

    /**
     * 设置view的标签
     *
     * @param viewId view的id
     * @param tag    标签
     */
    public BaseViewHolder setTag(@IdRes int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    /**
     * 设置view的标签
     *
     * @param viewId view的id
     * @param key    key
     * @param tag    标签
     */
    public BaseViewHolder setTag(@IdRes int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }
}
