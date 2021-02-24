package com.example.recyclerview.mian;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;

import com.example.recyclerview.R;
import com.example.recyclerview.adapter.MainAdapter;
import com.example.recyclerview.adapter.MainProviderMultiAdapter;
import com.example.recyclerview.bean.MainBean;
import com.example.recyclerview.bean.MultiMainBean;
import com.example.recyclerview.recycler.BaseQuickAdapter;
import com.example.recyclerview.recycler.slide.BaseItemTouchHelpCallback;
import com.example.recyclerview.recycler.slide.BaseSlideAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<MainBean> newList = new ArrayList<>();

    private MainAdapter mainAdapter;
    private List<MainBean> mainBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainBeanList = new ArrayList<>();
        mainBeanList.add(new MainBean(0, "100"));
        mainBeanList.add(new MainBean(1, "200"));
        mainBeanList.add(new MainBean(2, "300"));
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        mainAdapter = new MainAdapter(R.layout.item_main, mainBeanList);
        //拖动or滑动事件回调
        BaseItemTouchHelpCallback baseItemTouchHelpCallback = new BaseItemTouchHelpCallback(mainAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(baseItemTouchHelpCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mainAdapter);

        //开启item拖拽
        mainAdapter.enableDragItem(itemTouchHelper);

        //开启item中子view的拖拽，开启后textView将被占用
        //mainAdapter.enableDragItem(itemTouchHelper, R.id.textView, true);
        //拖拽回调事件，非必要
        //mainAdapter.setOnItemDragListener(onItemDragListener);

        // 开启滑动删除
        mainAdapter.enableSwipeItem();
        //滑动回调事件，非必要
        mainAdapter.setOnItemSwipeListener(new BaseSlideAdapter.OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
                mainAdapter.removeItem(pos);
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
                canvas.drawColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
            }
        });


        mainAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MainBean mainBean = new MainBean(3, "500");
                mainAdapter.addNewItem(mainBean);
            }
        });
        mainAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                newList.clear();
                newList.add(new MainBean(0, "500"));
                newList.add(new MainBean(1, "200"));
                newList.add(new MainBean(3, "500"));
                mainAdapter.setDiffData(newList);
                return true;
            }
        });

        List<MultiMainBean> multiMainList = new ArrayList<>();
        multiMainList.add(new MultiMainBean(R.layout.item_main, 0, "父0"));
        multiMainList.add(new MultiMainBean(R.layout.item_main2, 1, "子1"));
        multiMainList.add(new MultiMainBean(R.layout.item_main2, 2, "子2"));
        multiMainList.add(new MultiMainBean(R.layout.item_main, 3, "父1"));
        multiMainList.add(new MultiMainBean(R.layout.item_main2, 4, "子1"));
        multiMainList.add(new MultiMainBean(R.layout.item_main2, 5, "子2"));
        RecyclerView multiRecyclerView = findViewById(R.id.multiRecyclerView);

        final MainProviderMultiAdapter mainProviderMultiAdapter = new MainProviderMultiAdapter(multiMainList);

        //拖动or滑动事件回调
        BaseItemTouchHelpCallback callback = new BaseItemTouchHelpCallback(mainProviderMultiAdapter);
        ItemTouchHelper itemHelper = new ItemTouchHelper(callback);
        itemHelper.attachToRecyclerView(multiRecyclerView);

        multiRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        multiRecyclerView.setAdapter(mainProviderMultiAdapter);

        // 开启滑动删除
        mainProviderMultiAdapter.enableSwipeItem();
        //滑动回调事件，非必要
        mainProviderMultiAdapter.setOnItemSwipeListener(new BaseSlideAdapter.OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
                mainProviderMultiAdapter.removeItem(pos);
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
                canvas.drawColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
            }
        });

//        final MultiMainAdapter multiMainAdapter = new MultiMainAdapter(multiMainList);
//        multiRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        multiRecyclerView.setAdapter(multiMainAdapter);
//        //必须
//        multiMainAdapter.setLayouts(R.layout.item_main);
//        multiMainAdapter.setLayouts(R.layout.item_main2);
//
//        multiMainAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                MultiMainBean multiMainBean = new MultiMainBean(R.layout.item_main, 5, "父2");
//                multiMainAdapter.addNewItem(multiMainBean);
//            }
//        });
//
//        multiMainAdapter.setOnLoadMoreListener(multiRecyclerView, new BaseQuickAdapter.OnLoadMoreListener() {
//            @Override
//            public void onLoadMoreListener() {
//                //加载中
//                multiMainAdapter.setLoadingView(R.layout.loading_view);
//                //模拟加载失败
//                multiMainAdapter.setLoadFailedView(R.layout.load_failed_view);
//            }
//
//            @Override
//            public void onLoadFailedListener() {
//                //模拟加载成功
//                MultiMainBean multiMainBean = new MultiMainBean(R.layout.item_main, 4, "父3");
//                multiMainAdapter.addNewItem(multiMainBean);
//                multiMainAdapter.setLoadSuccessView();
//                //模拟没有更多
////              mainAdapter.setLoadEndView(R.layout.load_end_view);
//            }
//        });
    }
}