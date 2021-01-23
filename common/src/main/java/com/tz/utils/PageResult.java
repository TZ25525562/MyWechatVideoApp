package com.tz.utils;

import java.util.List;

/**
 * 封装pageInfo里的数据
 */
public class PageResult {
//    当前页数
    private int page;
//    每页记录数
    private int pageSize;
//    总记录数
    private long records;
//    每行显示的内容
    private List<?> rows;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getRecords() {
        return records;
    }

    public void setRecords(long records) {
        this.records = records;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }
}
