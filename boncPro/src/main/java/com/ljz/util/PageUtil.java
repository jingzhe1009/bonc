package com.ljz.util;


import java.util.List;

/**
 * @Author: ld
 * @Description:
 * @Dtate:Create in 22:122019/3/21
 */
public class PageUtil<T> {

    private int draw; // 第几次请求
    private int start = 0;// 起止位置
    private int length = 100; // 数据长度
    private String search;
    private boolean is_search;
    private int[] order;
    private String order_dir;
    private String columns_data;
    private String columns_name;
    private String columns_searchable;
    private boolean is_orderable;
    private String columns_search_value;
    private boolean is_search_regex;
    private int recordsTotal;
    private int recordsFiltered;
    private List<T> data;
    private String error;
    private String dt_rowId;
    private String dt_rowClass;
    private Object dt_rowData;
    private Object dt_rowAttr;
    private int page_num = 1;
    private int page_size = 10;


    public PageUtil() {

    }

    public PageUtil(Integer start, Integer length) {
        //开始的数据行数
        //每页的数据数
        //DT传递的draw:

        this.setStart(start);
        this.setLength(length);
        //计算页码
        this.page_num = (start / length) + 1;

    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public boolean isIs_search() {
        return is_search;
    }

    public void setIs_search(boolean is_search) {
        this.is_search = is_search;
    }

    public int[] getOrder() {
        return order;
    }

    public void setOrder(int[] order) {
        this.order = order;
    }

    public String getOrder_dir() {
        return order_dir;
    }

    public void setOrder_dir(String order_dir) {
        this.order_dir = order_dir;
    }

    public String getColumns_data() {
        return columns_data;
    }

    public void setColumns_data(String columns_data) {
        this.columns_data = columns_data;
    }

    public String getColumns_name() {
        return columns_name;
    }

    public void setColumns_name(String columns_name) {
        this.columns_name = columns_name;
    }

    public String getColumns_searchable() {
        return columns_searchable;
    }

    public void setColumns_searchable(String columns_searchable) {
        this.columns_searchable = columns_searchable;
    }

    public boolean isIs_orderable() {
        return is_orderable;
    }

    public void setIs_orderable(boolean is_orderable) {
        this.is_orderable = is_orderable;
    }

    public String getColumns_search_value() {
        return columns_search_value;
    }

    public void setColumns_search_value(String columns_search_value) {
        this.columns_search_value = columns_search_value;
    }

    public boolean isIs_search_regex() {
        return is_search_regex;
    }

    public void setIs_search_regex(boolean is_search_regex) {
        this.is_search_regex = is_search_regex;
    }

    public int getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(int recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public int getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(int recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getDt_rowId() {
        return dt_rowId;
    }

    public void setDt_rowId(String dt_rowId) {
        this.dt_rowId = dt_rowId;
    }

    public String getDt_rowClass() {
        return dt_rowClass;
    }

    public void setDt_rowClass(String dt_rowClass) {
        this.dt_rowClass = dt_rowClass;
    }

    public Object getDt_rowData() {
        return dt_rowData;
    }

    public void setDt_rowData(Object dt_rowData) {
        this.dt_rowData = dt_rowData;
    }

    public Object getDt_rowAttr() {
        return dt_rowAttr;
    }

    public void setDt_rowAttr(Object dt_rowAttr) {
        this.dt_rowAttr = dt_rowAttr;
    }

    public int getPage_num() {
        return page_num;
    }

    public void setPage_num(int page_num) {
        this.page_num = page_num;
    }

    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }
}

