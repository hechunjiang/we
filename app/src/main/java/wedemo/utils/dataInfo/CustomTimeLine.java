package wedemo.utils.dataInfo;

import com.meicam.sdk.NvsAudioTrack;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsVideoTrack;
import com.sven.huinews.international.utils.cache.ACache;

public class CustomTimeLine {

    private TimelineDataSingle timeData;
    private NvsTimeline timeline;

    public CustomTimeLine() {
        timeData = new TimelineDataSingle();
    }

    public TimelineDataSingle getTimeData() {
        if(timeData == null){
            return new TimelineDataSingle();
        }
        return timeData;
    }

    public void setTimeData(TimelineDataSingle timeData) {
        this.timeData = timeData;
    }

    public void setTimeData(CustomTimeLine customTimeLine) {
        this.timeData = customTimeLine.getTimeData().clone();
    }

    public NvsTimeline getTimeline() {
        return timeline;
    }

    public void setTimeline(NvsTimeline timeline) {
        this.timeline = timeline;
    }

    public NvsAudioTrack getAudioTrackByIndex(int index) {
        return timeline.getAudioTrackByIndex(index);
    }

    public NvsVideoTrack getVideoTrackByIndex(int index) {
        return timeline.getVideoTrackByIndex(index);
    }
}
