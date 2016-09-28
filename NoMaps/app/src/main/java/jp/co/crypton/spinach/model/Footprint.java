package jp.co.crypton.spinach.model;

import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Footprint {

    @SerializedName("status")
    @Expose
    private Status status;
    @SerializedName("count")
    @Expose
    private Integer count;

    @SerializedName("list")
    @Expose
    private java.util.List<ListData> list = new ArrayList<ListData>();

    /**
     *
     * @return
     * The status
     */
    public Status getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     *
     * @return
     * The count
     */
    public Integer getCount() {
        return count;
    }

    /**
     *
     * @param count
     * The count
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     *
     * @return
     * The list
     */
    public java.util.List<ListData> getList() {
        return list;
    }

    /**
     *
     * @param list
     * The list
     */
    public void setList(java.util.List<ListData> list) {
        this.list = list;
    }

}