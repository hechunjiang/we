package com.sven.huinews.international.main.task;

import com.chad.library.adapter.base.entity.SectionEntity;
import com.sven.huinews.international.entity.TaskWapperBean;
import com.sven.huinews.international.entity.response.TaskListNewResponse;

public class TaskTest extends SectionEntity<TaskListNewResponse.DataBean.ListBean> {

    public TaskTest(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public TaskTest(TaskListNewResponse.DataBean.ListBean s) {
        super(s);
    }
}
