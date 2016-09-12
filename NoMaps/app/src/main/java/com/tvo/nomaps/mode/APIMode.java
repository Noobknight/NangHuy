package com.tvo.nomaps.mode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by huy on 9/1/2016.
 */
public class APIMode {
    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("stat")
    @Expose
    private Integer stat;
    @SerializedName("reserved1")
    @Expose
    private String reserved1;
    @SerializedName("reserved2")
    @Expose
    private String reserved2;
    @SerializedName("error")
    @Expose
    private String error;

    /**
     *
     * @return
     * The kind
     */
    public String getKind() {
        return kind;
    }

    /**
     *
     * @param kind
     * The kind
     */
    public void setKind(String kind) {
        this.kind = kind;
    }

    /**
     *
     * @return
     * The stat
     */
    public Integer getStat() {
        return stat;
    }

    /**
     *
     * @param stat
     * The stat
     */
    public void setStat(Integer stat) {
        this.stat = stat;
    }

    /**
     *
     * @return
     * The reserved1
     */
    public String getReserved1() {
        return reserved1;
    }

    /**
     *
     * @param reserved1
     * The reserved1
     */
    public void setReserved1(String reserved1) {
        this.reserved1 = reserved1;
    }

    /**
     *
     * @return
     * The reserved2
     */
    public String getReserved2() {
        return reserved2;
    }

    /**
     *
     * @param reserved2
     * The reserved2
     */
    public void setReserved2(String reserved2) {
        this.reserved2 = reserved2;
    }

    /**
     *
     * @return
     * The error
     */
    public String getError() {
        return error;
    }

    /**
     *
     * @param error
     * The error
     */
    public void setError(String error) {
        this.error = error;
    }


}
