package com.sven.huinews.international.main.me;

import android.Manifest;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sven.huinews.international.AppConfig;
import com.sven.huinews.international.R;
import com.sven.huinews.international.main.me.adapter.DraftsAdapter;
import com.sven.huinews.international.utils.ToastUtils;
import com.sven.huinews.international.utils.statusbar.Eyes;
import com.sven.huinews.international.view.ShotDialog;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import wedemo.DraftsVideoEditActivity;
import wedemo.MessageEvent;
import wedemo.MusicActivity;
import wedemo.ShotActivity;
import wedemo.base.BaseActivity;
import wedemo.db.AppDatabase;
import wedemo.db.dao.TimeLineDao;
import wedemo.db.entity.TimeLineEntity;
import wedemo.utils.Constants;
import wedemo.utils.LogUtil;
import wedemo.utils.TimelineManager;
import wedemo.utils.dataInfo.TimeDataCache;

/**
 * TODO:草稿箱 下版本开放
 * [MeFragment]
 */
public class DraftsActivity extends BaseActivity {
    private RecyclerView rv_drafts;
    private ImageView back_iv;
    private TextView tv_tip_no;
    private TextView tv_shot;

    private DraftsAdapter adapter;
    private List<TimeDataCache> timeDataCacheList;
    private List<TimeLineEntity> timeLineEntityList;
    private TimeLineDao dao;

    private ShotDialog mShotDialog;

    @Override
    public int getLayoutId() {
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.bg_white));
        EventBus.getDefault().register(this);
        return R.layout.activity_drafts;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void refreshEvent(MessageEvent event){
        if(event.getMessage().equals(Constants.REFRESH_DRATS)){
            LogUtil.showLog("刷新");
            getDrafts();
        }
    }

    @Override
    public void initView() {
        rv_drafts =  findViewById(R.id.rv_drafts);
        back_iv = findViewById(R.id.back_iv);
        tv_tip_no = findViewById(R.id.tv_tip_no);
        tv_shot = findViewById(R.id.tv_shot);

        adapter = new DraftsAdapter();
        rv_drafts.setLayoutManager(new LinearLayoutManager(this));
        rv_drafts.setHasFixedSize(true);
        rv_drafts.setAdapter(adapter);

        mShotDialog = new ShotDialog();
    }

    @Override
    public void initEvents() {
        back_iv.setOnClickListener(this);
        tv_shot.setOnClickListener(this);

        mShotDialog.onPublishDilaogClickLisenter(new ShotDialog.onPublishDilaogClickLisenter() {
            @Override
            public void dialogCancleClick(final int index) {

                if (Constants.UPLOAD_TEST) {
                    EventBus.getDefault().post(new MessageEvent("test", "finish"));
                }

                new RxPermissions(DraftsActivity.this)
                        .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA)
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean aBoolean) {
                                if (aBoolean) {
                                    if (index == 1) {

                                        Intent intent = new Intent(mContext, MusicActivity.class);
                                        intent.putExtra("intype", 3);
                                        startActivity(intent);

                                    } else if (index == 2) {
                                        ShotActivity.toThis(DraftsActivity.this, 2);
                                    }
                                    finish();
                                } else {
                                    ToastUtils.showShort(DraftsActivity.this, R.string.camera_permission);
                                }
                            }
                        });

            }
        });

        adapter.setOnItemClickListener(new DraftsAdapter.OnItemClickListener() {
            @Override
            public void onDelet(int pos, TimeDataCache timeDataCache) {
                if(timeLineEntityList != null){
                    delTimeline(timeLineEntityList.get(pos),pos);
                }
            }

            @Override
            public void onEdit(int pos, TimeDataCache timeDataCache) {
                if(timeLineEntityList != null){
                    TimeLineEntity timeLineEntity = timeLineEntityList.get(pos);

                    Intent intent = new Intent(DraftsActivity.this,DraftsVideoEditActivity.class);
                    intent.putExtra("timeline",timeLineEntity);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onClickEvent(View v) {
        if(v == back_iv){
            finish();
        }else if(v == tv_shot){
            mShotDialog.dialogShow(getFragmentManager(), "");
        }
    }

    @Override
    public void initObject() {

        AppDatabase database = AppDatabase.getDatabase(AppConfig.getAppContext());
        dao = database.timeLineDao();

        getDrafts();
    }

    /**
     * 查询
     */
    private void getDrafts(){
        Observable.create(new Observable.OnSubscribe<List<TimeLineEntity>>() {
            @Override
            public void call(Subscriber<? super List<TimeLineEntity>> subscriber) {
                List<TimeLineEntity> allTimeLine = dao.getAllTimeLine();
                subscriber.onNext(allTimeLine);
            }
        }).map(new Func1<List<TimeLineEntity>, List<TimeDataCache>>() {
            @Override
            public List<TimeDataCache> call(List<TimeLineEntity> timeLineEntities) {

                timeLineEntityList = timeLineEntities;

                List<TimeDataCache> timeDataCacheList = new ArrayList<>();

                for(TimeLineEntity timeLineEntity:timeLineEntities){
                    Gson gson = new Gson();
                    TimeDataCache timeDataCache = gson.fromJson(timeLineEntity.getJson(), TimeDataCache.class);
                    timeDataCache.setId(timeLineEntity.getId());
                    timeDataCacheList.add(timeDataCache);
                }
                return timeDataCacheList;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<TimeDataCache>>() {
                    @Override
                    public void call(List<TimeDataCache> timeDataCacheList) {
                        LogUtil.showLog(timeDataCacheList.toString());
                        setDraftsData(timeDataCacheList);
                    }
                });
    }

    /**
     * 删除
     * @param timeLineEntity
     * @param pos
     */
    private void delTimeline(TimeLineEntity timeLineEntity,final int pos){
        Observable.just(timeLineEntity)
                .map(new Func1<TimeLineEntity, Boolean>() {
                    @Override
                    public Boolean call(TimeLineEntity timeLineEntity) {
                        dao.deleteTimeLine(timeLineEntity);
                        return true;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                timeDataCacheList.remove(pos);
                adapter.notifyItemRemoved(pos);
                adapter.notifyItemRangeChanged(pos,timeDataCacheList.size());

                if(timeDataCacheList.size() == 0){
                    setDraftsData(timeDataCacheList);
                }
            }
        });

    }

    private void setDraftsData(List<TimeDataCache> timeDataCacheList){
        this.timeDataCacheList = timeDataCacheList;
        adapter.setData(timeDataCacheList);

        if(timeDataCacheList == null || timeDataCacheList.size() == 0){
            tv_shot.setVisibility(View.VISIBLE);
            tv_tip_no.setVisibility(View.VISIBLE);
        }else{
            tv_shot.setVisibility(View.GONE);
            tv_tip_no.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        TimelineManager.getInstance().setCacheId(-1);
        getDrafts();

        if(Constants.UMENG_TEST){
            MobclickAgent.onPageStart("DraftsActivity");
            MobclickAgent.onResume(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(Constants.UMENG_TEST){
            MobclickAgent.onPageEnd("DraftsActivity");
            MobclickAgent.onPause(this);
        }
    }
}
