package com.sven.huinews.international.main.task.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseActivity;
import com.sven.huinews.international.entity.TimeLineBean;
import com.sven.huinews.international.main.task.TaskTest;
import com.sven.huinews.international.main.task.adapter.TaskAdapter;
import com.sven.huinews.international.main.task.adapter.TimeLineAdapter;
import com.sven.huinews.international.view.MyRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends BaseActivity {
    private RecyclerView rv_task;
    private RecyclerView rv_time_line;
    private MyRefreshLayout refresh_view;
    private TaskAdapter adapter;
    private TimeLineAdapter timeLineAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_task;
    }

    @Override
    public void initView() {
        rv_task = findViewById(R.id.rv_task);
        refresh_view = findViewById(R.id.refresh_view);

        adapter = new TaskAdapter(getTest());
        rv_task.setLayoutManager(new LinearLayoutManager(this));
        rv_task.setAdapter(adapter);
        adapter.addHeaderView(getHeader());
    }

    private List<TaskTest> getTest(){
        List<TaskTest> list = new ArrayList<>();
//        for(int i = 0;i<20;i++){
//            if(i == 0 || i == 5 || i == 15) {
//                TaskTest taskTest = new TaskTest(true, "s");
//                list.add(taskTest);
//            }else{
//                TaskTest taskTest = new TaskTest("s");
//                list.add(taskTest);
//            }
//        }


        return list;

    }

    @Override
    public void initEvents() {

    }

    @Override
    public void onClickEvent(View v) {

    }

    @Override
    public void initObject() {
       timeLineAdapter.setDay(5 - 1);
    }

    private View getHeader(){
        View view = getLayoutInflater().inflate(R.layout.item_task_header, (ViewGroup) rv_task.getParent(), false);

//        rv_time_line = view.findViewById(R.id.tv_time_line);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,7);
//        rv_time_line.setLayoutManager(gridLayoutManager);
//        List<TimeLineBean> lineBeans = initTimeData();
//        timeLineAdapter = new TimeLineAdapter(lineBeans);
//        rv_time_line.setAdapter(timeLineAdapter);


        return view;
    }

    private List<TimeLineBean> initTimeData(){
        List<TimeLineBean> lineBeans = new ArrayList<>();
        for(int i = 0; i< 7; i++){
            TimeLineBean timeLineBean = new TimeLineBean();
            timeLineBean.setDate("Day"+(i+1));
            timeLineBean.setNum("100");
            timeLineBean.setDay(i+1);
            lineBeans.add(timeLineBean);
        }
        return lineBeans;
    }
}
