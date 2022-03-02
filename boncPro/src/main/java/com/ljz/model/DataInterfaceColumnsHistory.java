package com.ljz.model;

import java.io.Serializable;
import java.time.LocalDate;

public class DataInterfaceColumnsHistory extends DataInterfaceColumnsHistoryKey implements Serializable {
    private String columnName;

    private String dataType;

    private String dataFormat;

    private Integer nullable;

    private Integer replacenull;

    private String comma;

    private String columnComment;

    private String isbucket;

    private LocalDate sDate;

    private LocalDate eDate;

    private static final long serialVersionUID = 1L;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName == null ? null : columnName.trim();
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType == null ? null : dataType.trim();
    }

    public String getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat == null ? null : dataFormat.trim();
    }

    public Integer getNullable() {
        return nullable;
    }

    public void setNullable(Integer nullable) {
        this.nullable = nullable;
    }

    public Integer getReplacenull() {
        return replacenull;
    }

    public void setReplacenull(Integer replacenull) {
        this.replacenull = replacenull;
    }

    public String getComma() {
        return comma;
    }

    public void setComma(String comma) {
        this.comma = comma == null ? null : comma.trim();
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment == null ? null : columnComment.trim();
    }

    public String getIsbucket() {
        return isbucket;
    }

    public void setIsbucket(String isbucket) {
        this.isbucket = isbucket == null ? null : isbucket.trim();
    }

    public LocalDate getsDate() {
        return sDate;
    }

    public void setsDate(LocalDate sDate) {
        this.sDate = sDate;
    }

    public LocalDate geteDate() {
        return eDate;
    }

    public void seteDate(LocalDate eDate) {
        this.eDate = eDate;
    }
}