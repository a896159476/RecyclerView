package com.example.recyclerview.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.recyclerview.R;
import com.example.recyclerview.bean.MainBean;
import com.example.recyclerview.mian.DiffCallBack;
import com.example.recyclerview.recycler.BaseQuickAdapter;
import com.example.recyclerview.recycler.BaseViewHolder;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainAdapter extends BaseQuickAdapter<MainBean, BaseViewHolder> {

    public MainAdapter(int layoutResId, List<MainBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, MainBean item, int position) {
        holder.setText(R.id.tv_name, item.getName());
    }

    //局部更新
    @Override
    protected void update(@NonNull BaseViewHolder holder, MainBean item, List<Object> payloads) {
        MainBean mainBean = (MainBean) payloads.get(0);
        if (mainBean.getName() != null) {
            holder.setText(R.id.tv_name, item.getName());
        }
    }

    //更新数据
    public void setDiffData(final List<MainBean> newList) {
        Observable.create(new ObservableOnSubscribe<DiffUtil.DiffResult>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<DiffUtil.DiffResult> emitter) throws Exception {
                //这一步如果数据量大会阻塞主线程，建议异步处理
                DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffCallBack(data, newList));
                emitter.onNext(result);
            }
        }).subscribeOn(Schedulers.io())// 将被观察者切换到子线程
                .observeOn(AndroidSchedulers.mainThread())// 将观察者切换到主线程
                .subscribe(new Observer<DiffUtil.DiffResult>() {
                    private Disposable mDisposable;

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(@NonNull DiffUtil.DiffResult result) {
                        setDiffData(result, newList);
                        mDisposable.dispose();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
