package com.example.recyclerview.mian;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.recyclerview.R;
import com.example.recyclerview.adapter.MainAdapter;
import com.example.recyclerview.adapter.MultiMainAdapter;
import com.example.recyclerview.bean.MainBean;
import com.example.recyclerview.bean.MultiMainBean;
import com.example.recyclerview.recycler.BaseQuickAdapter;

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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mainAdapter);

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
        MultiMainAdapter multiMainAdapter = new MultiMainAdapter(multiMainList);
        multiRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        multiRecyclerView.setAdapter(multiMainAdapter);
        //必须
        multiMainAdapter.setLayouts(R.layout.item_main);
        multiMainAdapter.setLayouts(R.layout.item_main2);

        multiMainAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getApplicationContext(), "点击" + position, Toast.LENGTH_SHORT).show();
            }
        });
//        multiMainAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
//            @Override
//            public void onItemLongClick(View view, int position) {
//                Toast.makeText(getApplicationContext(), "长按" + position, Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}