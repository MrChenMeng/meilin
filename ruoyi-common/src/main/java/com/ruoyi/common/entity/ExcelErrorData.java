package com.ruoyi.common.entity;


/**
 * @author chenm
 * @create 2019-08-23 20:32
 */
public class ExcelErrorData {

    private static final long serialVersionUID = 1L;

    private int rowId;

    private int cellId;

    private String reason;

    private Object data;

    public ExcelErrorData() {

    }

    public ExcelErrorData(int rowId, int cellId, String reason) {
        this.rowId = rowId;
        this.cellId = cellId;
        this.reason = reason;
    }

    public ExcelErrorData(int rowId, int cellId, String reason, Object data) {
        this.rowId = rowId;
        this.cellId = cellId;
        this.reason = reason;
        this.data = data;
    }

    public static ExcelErrorData getExcelErrorData(int rowId, int cellId, String reason) {
        return new ExcelErrorData(rowId, cellId, reason);
    }

    public static ExcelErrorData getExcelErrorData(int rowId, int cellId, String reason, Object data) {
        return new ExcelErrorData(rowId, cellId, reason, data);
    }

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public int getCellId() {
        return cellId;
    }

    public void setCellId(int cellId) {
        this.cellId = cellId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
