package wedemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.meicam.sdk.NvsTimeline;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sven.huinews.international.R;
import com.sven.huinews.international.utils.ToastUtils;
import com.sven.huinews.international.view.MyRefreshLayout;
import com.sven.huinews.international.view.dialog.ShowProgressDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

import wedemo.base.BaseActivity;
import wedemo.config.Constant;
import wedemo.music.MusicCategroyAdapter;
import wedemo.music.MusicContract;
import wedemo.music.MusicListAdapter;
import wedemo.music.MusicListRequest;
import wedemo.music.MusicListResponse;
import wedemo.music.MusicModel;
import wedemo.music.MusicPresenter;
import wedemo.music.MusicSearchRequest;
import wedemo.music.MusicTypeResponse;
import wedemo.utils.Constants;
import wedemo.utils.LogUtil;
import wedemo.utils.MediaPlayerUtil;
import wedemo.utils.MusicUtils;
import wedemo.utils.PathUtils;
import wedemo.utils.dataInfo.MusicInfo;
import wedemo.utils.dataInfo.TimelineData;

public class MusicActivity extends BaseActivity<MusicPresenter, MusicModel> implements MusicContract.View {
    private MyRefreshLayout refresh_view;
    private ImageButton btn_back;
    private RecyclerView music_cate_rv;
    private RecyclerView music_rv;
    private MusicCategroyAdapter musicCategroyAdapter;
    private MusicListAdapter musicListAdapter;
    private int musciTypeId;

    private MusicInfo musicInfo;
    private MediaPlayer mediaPlayer;
    private int musicPosition;
    private int lastSelectPos = -1;
    private MusicListResponse.DataBean mMusicData;
    private EditText et_search;
    private ImageView btn_search;
    private boolean isSearch;
    private NvsTimeline m_timeLine;
    private int type = 0;
    private TextView tv_shot;

    private ShowProgressDialog showProgressDialog;

    @Override
    public int getLayoutId() {
        // Eyes.setStatusBarColor(this, getResources().getColor(R.color.c262626));
        return R.layout.activity_music_s;
    }

    @Override
    public void initView() {
        btn_back = findViewById(R.id.btn_back);
        music_cate_rv = findViewById(R.id.music_cate_rv);
        music_rv = findViewById(R.id.music_rv);
        et_search = findViewById(R.id.et_search);
        btn_search = findViewById(R.id.btn_search);
        refresh_view = findViewById(R.id.refresh_view);
        tv_shot = findViewById(R.id.tv_shot);

        showProgressDialog = ShowProgressDialog.initGrayDialog(this);
        showProgressDialog.setMessage("");
        showProgressDialog.setCancelable(false);


        type = getIntent().getIntExtra("intype", 0);

        if(type == 3){
            tv_shot.setVisibility(View.VISIBLE);
        }else{
            tv_shot.setVisibility(View.GONE);
        }

        refresh_view.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pauseMusic();
                isRefresh = true;
                PAGE = 1;

                if(musciTypeId == Constant.MUSIC_STORE){
                    List<MusicListResponse.DataBean> musicData = MusicUtils.getMusicData(MusicActivity.this, Constant.MUSIC_STORE);
                    setMusicList(musicData);
                }else {
                    if (isSearch) {
                        mPresenter.onMusicSearch(mContext);
                    } else {
                        mPresenter.onMusicList(mContext);
                    }
                }
            }
        });

        refresh_view.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pauseMusic();
                isRefresh = false;

                if(musciTypeId == Constant.MUSIC_STORE){
                    refresh_view.finishLoadmore();
                }else {
                    PAGE += 1;
                    if (isSearch) {
                        mPresenter.onMusicSearch(mContext);
                    } else {
                        mPresenter.onMusicList(mContext);
                    }
                }
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    isSearch = false;
                    isRefresh = true;
                    PAGE = 1;
                    mPresenter.onMusicList(mContext);
                }
            }
        });
    }

    @Override
    public void initEvents() {
        btn_back.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        tv_shot.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        if (v == btn_back) {
            finish();
        } else if (v == btn_search) {
            search();
        } else if( v == tv_shot){

            ShotActivity.toThis(MusicActivity.this, 1);
            stopMusic();
            finish();
        }
    }

    @Override
    public void initObject() {
        mediaPlayer = new MediaPlayer();
        setMVP();
        initCategroy();
        initMusicList();
        // m_timeLine = TimelineUtil.createTimeline();
        showProgressDialog.show();
        mPresenter.onMusic(this);
    }


    private void initCategroy() {
        musicCategroyAdapter = new MusicCategroyAdapter(mContext);
        music_cate_rv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        music_cate_rv.setAdapter(musicCategroyAdapter);
        musicCategroyAdapter.setmOnItemClickLisenter(new MusicCategroyAdapter.OnItemClickLisenter() {
            @Override
            public void onItemClicl(MusicTypeResponse.DataBean mData) {
                PAGE = 1;
                isRefresh = true;
                musciTypeId = mData.getId();
                isSearch = false;
                stopMusic();

                if(mData.getId() == Constant.MUSIC_STORE){
                    List<MusicListResponse.DataBean> musicData = MusicUtils.getMusicData(MusicActivity.this, Constant.MUSIC_STORE);
                    setMusicList(musicData);
                }else {
                    mPresenter.onMusicList(MusicActivity.this);

                }
            }
        });
    }


    private void initMusicList() {
        musicListAdapter = new MusicListAdapter(mContext);
        music_rv.setLayoutManager(new LinearLayoutManager(mContext));
        music_rv.setAdapter(musicListAdapter);

        musicListAdapter.onPlayMusicLisenter(new MusicListAdapter.onPlayMusicLisenter() {
            @Override
            public void onPlayMusic(int position, final MusicListResponse.DataBean mData) {
                musicPosition = position;
                mMusicData = mData;
                musicInfo = new MusicInfo();
                musicInfo.setFilePath(mData.getMusic_url());
                //   stopMusic();
                String path = null;
                if(mData.getId() == Constant.MUSIC_STORE){
                    path = mData.getMusic_path();
                }else {
                    path = PathUtils.getMusicPath(mData.getTitle());
                }
                boolean tmp = PathUtils.fileIsExists(path);
                if (tmp) {
                    //  playMusic(path);
                    Log.e("weiwei", "存在");
                    if (lastSelectPos != -1 && lastSelectPos == position) {
                        if (player != null) {
                            if (player.isPlaying()) {
                                pauseMusic();
                            } else {
                                player.play();
                            }
                            return;
                        }
                    }

                    mData.setMusic_path(path);
                    playMusic(mData);
                } else {
                    Log.e("weiwei", "不存在");
                    showProgressDialog.show();
                    mPresenter.getMusic(mData, false);
                }
                lastSelectPos = position;
            }

            @Override
            public void onSelectMusic(MusicListResponse.DataBean mData) {
                Log.e("weiwei", "使用");
                stopMusic();
                mPresenter.getMusic(mData, true);

            }
        });
    }

    @Override
    public void setMusicType(List<MusicTypeResponse.DataBean> mMusicTypes) {
        showProgressDialog.dismiss();
        isSearch = false;

        MusicTypeResponse.DataBean current = new MusicTypeResponse.DataBean();
        current.setName("本地音乐");
        current.setSelected(false);
        current.setType_cover("");
        current.setId(Constant.MUSIC_STORE);

        mMusicTypes.add(current);

        musicCategroyAdapter.setDatas(mMusicTypes);
        musicCategroyAdapter.setDefSelected();
        musciTypeId = mMusicTypes.get(0).getId();

        if(mMusicTypes.size() == 1){
            List<MusicListResponse.DataBean> musicData = MusicUtils.getMusicData(this, Constant.MUSIC_STORE);
            setMusicList(musicData);
        }else {
            mPresenter.onMusicList(this);
        }
    }

    @Override
    public MusicListRequest getMusicListRequest() {
        MusicListRequest request = new MusicListRequest();
        request.setId(musciTypeId);
        request.setPage(PAGE);
        request.setPage_size(LIMIT);
        return request;
    }

    @Override
    public void setMusicList(List<MusicListResponse.DataBean> mDatas) {
        music_cate_rv.setVisibility(isSearch ? View.GONE : View.VISIBLE);
        if (isRefresh) {
            refresh_view.finishRefresh();
        } else {
            refresh_view.finishLoadmore();
        }
        musicListAdapter.setDatas(mDatas, isRefresh);
    }

    @Override
    public MusicSearchRequest getMusicSearchRequest() {
        MusicSearchRequest request = new MusicSearchRequest();
        request.setKeyword(et_search.getText().toString().trim());
        request.setPage(PAGE);
        request.setPage_size(LIMIT);
        return request;
    }

    @Override
    public void setMusicPath(final String path, boolean isUse) {
        Log.e("weiwei", "下载完毕");
        mMusicData.setMusic_path(path);
        showProgressDialog.dismiss();
        if (isUse) {
            if (mMusicData != null) {
                MusicInfo mMusicInfo = new MusicInfo();
                mMusicInfo.setDuration(mMusicData.getMusic_duration() * 1000 * 1000);
                mMusicInfo.setTitle(mMusicData.getTitle());
                mMusicInfo.setPlay(true);
                mMusicInfo.setFilePath(mMusicData.getMusic_path());
                mMusicInfo.setImagePath(mMusicData.getMusic_cover());
                mMusicInfo.setTrimIn(0);
                mMusicInfo.setTrimOut(mMusicData.getMusic_duration() * 1000 * 1000);
                mMusicInfo.setId(mMusicData.getId());

                if (type == 1) {
                    EventBus.getDefault().post(new MessageEvent("select_music", mMusicInfo));
                } else if(type == 0){
                    EventBus.getDefault().post(new MessageEvent("music", mMusicInfo));
                }else if(type == 3){
                    TimelineData.instance().setMasterMusic(mMusicInfo);
                    ShotActivity.toThis(MusicActivity.this, 1);
                }
            }

            stopMusic();
            finish();

        } else {
            musicListAdapter.insertData(musicPosition, mMusicData);
            playMusic(path);
            scanFile(MusicActivity.this,path);
        }

    }

    private void search() {
        isSearch = true;
        PAGE = 1;
        isRefresh = true;
        pauseMusic();
        if (TextUtils.isEmpty(et_search.getText().toString())) {
            ToastUtils.show(mContext, getResources().getString(R.string.input_keywords), 1);
            et_search.requestFocus();
            return;
        }
        mPresenter.onMusicSearch(mContext);
    }


    MediaPlayerUtil player;

    private void playMusic(String path) {
        stopMusic();
        player = new MediaPlayerUtil();
        player.setUrl(path);
        player.play();
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showErrorTip(int code, String msg) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showProgressDialog.dismiss();
        stopMusic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopMusic();

        if(Constants.UMENG_TEST){
            MobclickAgent.onPageEnd("MusicActivity");
            MobclickAgent.onPause(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Constants.UMENG_TEST){
            MobclickAgent.onPageStart("MusicActivity");
            MobclickAgent.onResume(this);
        }
    }

    private void pauseMusic() {
        // AudioPlayer.getInstance(this).destroyPlayer();
        if (player != null) {
            player.pause();
        }
    }


    private void stopMusic() {
        // AudioPlayer.getInstance(this).destroyPlayer();
        if (player != null) {
            player.stop();
        }
    }

    public void playMusic(MusicListResponse.DataBean musicData) {
        if (musicData == null) {
            return;
        }

        MusicInfo musicInfo = new MusicInfo();
        musicInfo.setDuration(musicData.getMusic_duration() * 1000 * 1000);
        musicInfo.setTitle(musicData.getTitle());
        musicInfo.setPlay(true);
        musicInfo.setIsHttpMusic(true);
        musicInfo.setFileUrl(musicData.getMusic_url());
        musicInfo.setFilePath(musicData.getMusic_path());
        musicInfo.setImagePath(musicData.getMusic_cover());
        musicInfo.setTrimIn(0);
        musicInfo.setTrimOut(musicData.getMusic_duration() * 1000 * 1000);
//        AudioPlayer.getInstance(this).setCurrentMusic(musicInfo);
//        AudioPlayer.getInstance(this).startPlay();
        playMusic(musicInfo.getFilePath());
    }

    //更新媒体库
    /**
     * 通知媒体库更新文件
     * @param context
     * @param filePath 文件全路径
     *
     * */
    public void scanFile(Context context, String filePath) {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(Uri.fromFile(new File(filePath)));
        context.sendBroadcast(scanIntent);
    }


}
