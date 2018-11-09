package com.sven.huinews.international.utils.update;

/**
 * Created by sfy. on 2018/4/3 0003.
 */

public class FileLoadEvent {
    long total;
    long bytesLoaded;

    public long getBytesLoaded() {
        return bytesLoaded;
    }

    public long getTotal() {
        return total;
    }

    public FileLoadEvent(long total, long bytesLoaded) {
        this.total = total;
        this.bytesLoaded = bytesLoaded;
    }
}
