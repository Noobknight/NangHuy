package jp.co.crypton.spinach.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BootModel {

    @SerializedName("boot")
    @Expose
    private List<Boot> boot = new ArrayList<Boot>();

    /**
     *
     * @return
     * The boot
     */
    public List<Boot> getBoot() {
        return boot;
    }

    /**
     *
     * @param boot
     * The boot
     */
    public void setBoot(List<Boot> boot) {
        this.boot = boot;
    }

}