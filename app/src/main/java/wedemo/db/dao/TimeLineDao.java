package wedemo.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.Date;
import java.util.List;

import wedemo.db.entity.TimeLineEntity;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface TimeLineDao{

    @Query("select * from TimeLineEntity")
    List<TimeLineEntity> getAllTimeLine();

    @Query("select * from TimeLineEntity where time_id = :id")
    TimeLineEntity getItembyId(String id);

    @Query("select * from TimeLineEntity where time_save = :date")
    TimeLineEntity getItembyDate(Date date);

    @Insert(onConflict = REPLACE)
    void addTimeLine(TimeLineEntity borrowModel);

    @Insert(onConflict = REPLACE)
    void addTimeLineList(List<TimeLineEntity> timeLineEntities);

    @Update
    public void updateTimeLine(TimeLineEntity timeLineEntity);

    @Delete
    void deleteTimeLine(TimeLineEntity timeLineEntity);
}
