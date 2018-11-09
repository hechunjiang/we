package wedemo.utils.dataInfo;


import android.graphics.Bitmap;

import java.io.Serializable;

public class MusicInfo implements Serializable {
    private String m_title;
    private String m_artist;
    private String m_imagePath;
    private String m_filePath;
    private String m_fileUrl;
    private String m_assetPath;
    private Bitmap m_image;
    private long inPoint;
    private long m_duration;
    private long m_trimIn;
    private long m_trimOut;
    private int m_mimeType;
    private boolean m_prepare;
    private boolean m_isHttpMusic;
    private boolean m_isAsset;
    private boolean m_play;
    private long changem_trimIn;
    private long changem_trimOut;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MusicInfo clone() {
        MusicInfo musicInfo = new MusicInfo();
        musicInfo.setTitle(this.getTitle());
        musicInfo.setArtist(this.getArtist());
        musicInfo.setImagePath(this.getImagePath());
        musicInfo.setFilePath(this.getFilePath());
        musicInfo.setFileUrl(this.getFileUrl());
        musicInfo.setAssetPath(this.getAssetPath());
        musicInfo.setImage(this.getImage());
        musicInfo.setInPoint(this.getInPoint());
        musicInfo.setDuration(this.getDuration());
        musicInfo.setTrimIn(this.getTrimIn());
        musicInfo.setTrimOut(this.getTrimOut());
        musicInfo.setMimeType(this.getMimeType());
        musicInfo.setChangem_trimIn(this.getChangem_trimIn());
        musicInfo.setChangem_trimOut(this.getChangem_trimOut());
        musicInfo.setId(this.getId());
        return musicInfo;
    }

    public long getChangem_trimIn() {
        return changem_trimIn;
    }

    public void setChangem_trimIn(long changem_trimIn) {
        this.changem_trimIn = changem_trimIn;
    }

    public long getChangem_trimOut() {
        return changem_trimOut;
    }

    public void setChangem_trimOut(long changem_trimOut) {
        this.changem_trimOut = changem_trimOut;
    }

    public MusicInfo() {
        m_title = null;
        m_artist = null;
        m_imagePath = null;
        m_filePath = null;
        m_fileUrl = null;
        m_image = null;
        m_duration = 0;
        m_trimIn = 0;
        m_trimOut = 0;
        m_prepare = false;
        m_isHttpMusic = false;
        m_isAsset = false;
        m_play = false;
        changem_trimIn = -1;
        changem_trimOut = -1;
        id = 0;
    }

    public long getInPoint() {
        return inPoint;
    }

    public void setInPoint(long inPoint) {
        this.inPoint = inPoint;
    }

    public void setTitle(String title) {
        m_title = title;
    }

    public String getTitle() {
        return m_title;
    }

    public void setArtist(String artist) {
        m_artist = artist;
    }

    public String getArtist() {
        return m_artist;
    }

    public void setImagePath(String filePath) {
        m_imagePath = filePath;
    }

    public String getImagePath() {
        return m_imagePath;
    }

    public void setFilePath(String filePath) {
        m_filePath = filePath;
    }

    public String getFilePath() {
        return m_filePath;
    }

    public void setFileUrl(String url) {
        m_fileUrl = url;
    }

    public String getFileUrl() {
        return m_fileUrl;
    }

    public String getAssetPath() {
        return m_assetPath;
    }

    public void setAssetPath(String m_assetPath) {
        this.m_assetPath = m_assetPath;
    }

    public void setImage(Bitmap bitmap) {
        m_image = bitmap;
    }

    public Bitmap getImage() {
        return m_image;
    }

    public void setDuration(long time) {
        m_duration = time;
    }

    public long getDuration() {
        return m_duration;
    }

    public void setTrimIn(long time) {
        m_trimIn = time;
    }

    public long getTrimIn() {
        return m_trimIn;
    }

    public void setTrimOut(long time) {
        m_trimOut = time;
    }

    public long getTrimOut() {
        return m_trimOut;
    }

    public int getMimeType() {
        return m_mimeType;
    }

    public void setMimeType(int m_mimeType) {
        this.m_mimeType = m_mimeType;
    }

    public boolean isPrepare() {
        return m_prepare;
    }

    public void setPrepare(boolean m_prepare) {
        this.m_prepare = m_prepare;
    }

    public boolean isHttpMusic() {
        return m_isHttpMusic;
    }

    public void setIsHttpMusic(boolean m_isHttpMusic) {
        this.m_isHttpMusic = m_isHttpMusic;
    }

    public boolean isAsset() {
        return m_isAsset;
    }

    public void setIsAsset(boolean m_isAsset) {
        this.m_isAsset = m_isAsset;
    }

    public boolean isPlay() {
        return m_play;
    }

    public void setPlay(boolean m_play) {
        this.m_play = m_play;
    }
}
