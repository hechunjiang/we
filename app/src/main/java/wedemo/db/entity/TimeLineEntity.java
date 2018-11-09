package wedemo.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity
public class TimeLineEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "time_id")
    private int id;

    @ColumnInfo(name = "time_value")
    private String json;

    @ColumnInfo(name = "time_save")
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

}
