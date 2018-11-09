package wedemo.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.meicam.sdk.NvsVideoResolution;
import com.sven.huinews.international.AppConfig;
import com.sven.huinews.international.utils.cache.ACache;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import wedemo.MessageEvent;
import wedemo.db.AppDatabase;
import wedemo.db.entity.TimeLineEntity;
import wedemo.utils.dataInfo.ClipInfo;
import wedemo.utils.dataInfo.CustomTimeLine;
import wedemo.utils.dataInfo.TimeDataCache;
import wedemo.utils.dataInfo.TimelineData;
import wedemo.utils.dataInfo.TimelineDataSingle;

public class TimelineManager {
    private List<CustomTimeLine> singleTimelineList;
    private static TimelineManager instance = new TimelineManager();
    private CustomTimeLine masterTimeline;
    private int selectBranch = 0;
    private int cacheId = -1;
    private Date saveDate = new Date();

    public void setCacheId(int cacheId){
        this.cacheId = cacheId;
        saveDate = new Date();
    }

    public void save() {
        if(cacheId == -1) {
            Observable.just(saveDate)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(Schedulers.io())
                    .subscribe(new Action1<Date>() {
                        @Override
                        public void call(Date date) {
                            AppDatabase database = AppDatabase.getDatabase(AppConfig.getAppContext());
                            TimeLineEntity itembyDate = database.timeLineDao().getItembyDate(date);

                            if (itembyDate != null) {
                                cacheId = itembyDate.getId();
                                LogUtil.showLog("数据库有cacheId==" + cacheId);
                            }
                            saveTimeLine();
                        }
                    });
        }else{
            saveTimeLine();
        }
    }

    private void saveTimeLine(){
        Observable.create(new Observable.OnSubscribe<TimeLineEntity>() {
            @Override
            public void call(Subscriber<? super TimeLineEntity> subscriber) {
                TimeDataCache timeDataCache = new TimeDataCache();

                List<TimelineDataSingle> timelineDataSingles = new ArrayList<>();
                for (CustomTimeLine customTimeLine : singleTimelineList) {
                    TimelineDataSingle timeData = customTimeLine.getTimeData();
                    timelineDataSingles.add(timeData);
                }

                ArrayList<ClipInfo> videoClipArray = TimelineData.instance().getClipInfoData();

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
                Date date = new Date(System.currentTimeMillis());
                saveDate = date;
                String format = simpleDateFormat.format(date);

                timeDataCache.setData(timelineDataSingles);
                timeDataCache.setM_clipInfoArray(videoClipArray);
                timeDataCache.setM_makeRatio(TimelineData.instance().getMakeRatio());
                timeDataCache.setmMusicData(TimelineData.instance().getMasterMusic());
                timeDataCache.setDuration(masterTimeline.getTimeline().getDuration());
                timeDataCache.setDate(format);
                timeDataCache.setTransitionInfo(TimelineData.instance().getTransitionData());

                Gson gson = new Gson();
                String result = gson.toJson(timeDataCache);
                TimeLineEntity entity = new TimeLineEntity();
                entity.setJson(result);
                entity.setDate(date);

                subscriber.onNext(entity);
            }
        })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .doOnNext(new Action1<TimeLineEntity>() {
                    @Override
                    public void call(TimeLineEntity timeLineEntity) {
                        AppDatabase database = AppDatabase.getDatabase(AppConfig.getAppContext());

                        if (cacheId == -1) {
                            LogUtil.showLog("插入数据库");

                            database.timeLineDao().addTimeLine(timeLineEntity);
                        } else {
                            LogUtil.showLog("更新数据库");
                            timeLineEntity.setId(cacheId);
                            database.timeLineDao().updateTimeLine(timeLineEntity);
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<TimeLineEntity>() {
                    @Override
                    public void call(TimeLineEntity timeLineEntity) {
                        //EventBus.getDefault().post(new MessageEvent(Constants.REFRESH_DRATS));
                    }
                });

    }

    /**
     * 数据汇源
     */
    private void initCache(TimeDataCache timeDataCache) {

        NvsVideoResolution videoEditRes = new NvsVideoResolution();
        videoEditRes.imageWidth = Constants.IMAGE_WIDTH;      /*视频分辨率的宽*/
        videoEditRes.imageHeight = Constants.IMAGE_HEIGHT;
        TimelineData.instance().setVideoResolution(videoEditRes);

        TimelineData.instance().setClipInfoData(timeDataCache.getM_clipInfoArray());
        TimelineData.instance().setMakeRatio(timeDataCache.getM_makeRatio());
        TimelineData.instance().setMasterMusic(timeDataCache.getmMusicData());
        TimelineData.instance().setTransitionData(timeDataCache.getTransitionInfo());
    }

    public void createDraftsTimeLine(TimeDataCache timeDataCache, Context context,int id) {
        cacheId = id;
        initCache(timeDataCache);

        //创建单线
        singleTimelineList = CustomTimelineUtil.createDraftsSingleTimelineList(timeDataCache);
        //音乐重置
        CustomTimelineUtil.createSingleMusic();

        //创建总线
        createMasterTimeline(context);
        masterTimeline.getTimeData().setTransitionData(TimelineData.instance().getTransitionData());
    }


    private TimelineManager() {

    }

    public static TimelineManager getInstance() {
        return instance;
    }

    public void clear() {
        if (singleTimelineList != null) {
            singleTimelineList.clear();
        }

        masterTimeline = null;
        selectBranch = 0;
    }

    /**
     * 设置当前选中的时间线
     *
     * @param selectBranch
     */
    public void setBranch(int selectBranch) {
        this.selectBranch = selectBranch;
    }

    public int getBranch() {
        return selectBranch;
    }


    /**
     * 获取时间线列表
     *
     * @return
     */
    public List<CustomTimeLine> getSingleTimelineList() {
        return singleTimelineList;
    }

    /**
     * 创建总线
     *
     * @return
     */
    public boolean createMasterTimeline(Context context) {
        masterTimeline = CustomTimelineUtil.createTimeline(context);
        if (masterTimeline == null)
            return false;
//        NvsVideoTrack mVideoTrack = masterTimeline.getTimeline().getVideoTrackByIndex(0);
//        if(mVideoTrack == null)
//            return false;
        return true;
    }

    /**
     * 创建所有单时间线
     *
     * @return
     */
    public boolean createBranchTimeline() {
        //为每个视频创建时间线
        singleTimelineList = CustomTimelineUtil.createSingleTimelineList();

        //添加音乐线
        CustomTimelineUtil.createSingleMusic();
        return TimelineUtil.checkSingleTimeLine(singleTimelineList);
    }

    /**
     * 获取前面时间线的时间
     *
     * @param pos
     * @return
     */
    public long getPreviousAllTime(int pos) {
        long preTotal = 0;

        if (pos == 0 || singleTimelineList == null || singleTimelineList.size() == 0) {
            return preTotal;
        }
        for (int i = 0; i < singleTimelineList.size(); i++) {
            if (pos == i) {
                break;
            }
            preTotal += singleTimelineList.get(i).getTimeline().getDuration();
        }

        return preTotal;
    }

    public long getCurrentPreviousAllTime() {
        return getPreviousAllTime(selectBranch);
    }


    /**
     * 追加时间线
     *
     * @param pos 追加时间线位置
     * @return
     */

    public boolean addBranchTimeline(ArrayList<ClipInfo> videoClipArray, int pos) {

        List<CustomTimeLine> addSingleTimelineList = CustomTimelineUtil.addSingleTimelineList(videoClipArray);
        singleTimelineList.addAll(pos, addSingleTimelineList);

        //追加音乐线
        CustomTimelineUtil.createSingleMusic();
        return TimelineUtil.checkSingleTimeLine(singleTimelineList);
    }

    public boolean removeBranchTimeline(int pos) {
        CustomTimeLine remove = singleTimelineList.remove(pos);

        //添加音乐线
        CustomTimelineUtil.createSingleMusic();
        return CustomTimelineUtil.removeTimeline(remove);
    }


    public CustomTimeLine getMasterTimeline() {
        return masterTimeline;
    }

    public CustomTimeLine getBranchTimeline(int pos) {
        if (singleTimelineList == null || singleTimelineList.size() == 0) {
            return null;
        }
        return singleTimelineList.get(pos);
    }

    public CustomTimeLine getCurrenTimeline() {

        return getBranchTimeline(selectBranch);
    }
}
