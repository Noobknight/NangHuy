package jp.co.crypton.spinach.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Boot {

    @SerializedName("state")
    @Expose
    private Integer state;
    @SerializedName("msg_ja")
    @Expose
    private String msgJa;
    @SerializedName("msg_en")
    @Expose
    private String msgEn;

    /**
     *
     * @return
     * The state
     */
    public Integer getState() {
        return state;
    }

    /**
     *
     * @param state
     * The state
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     *
     * @return
     * The msgJa
     */
    public String getMsgJa() {
        return msgJa;
    }

    /**
     *
     * @param msgJa
     * The msg_ja
     */
    public void setMsgJa(String msgJa) {
        this.msgJa = msgJa;
    }

    /**
     *
     * @return
     * The msgEn
     */
    public String getMsgEn() {
        return msgEn;
    }

    /**
     *
     * @param msgEn
     * The msg_en
     */
    public void setMsgEn(String msgEn) {
        this.msgEn = msgEn;
    }

}