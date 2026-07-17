package com.ordermanager.common;

import java.util.Collections;
import java.util.List;

/**
 * Pagination result wrapper
 */
public class PageResult<T> {
    private List<T> list;
    private long total;

    public PageResult() {
        this.list = Collections.emptyList();
        this.total = 0;
    }

    public PageResult(List<T> list, long total) {
        this.list = list;
        this.total = total;
    }

    public List<T> getList() { return list; }
    public void setList(List<T> list) { this.list = list; }
    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }
}
