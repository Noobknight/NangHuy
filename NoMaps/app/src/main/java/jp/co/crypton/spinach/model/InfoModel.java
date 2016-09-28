package jp.co.crypton.spinach.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InfoModel {

    @SerializedName("information")
    @Expose
    private List<Information> information = new ArrayList<Information>();

    /**
     *
     * @return
     * The information
     */
    public List<Information> getInformation() {
        return information;
    }

    /**
     *
     * @param information
     * The information
     */
    public void setInformation(List<Information> information) {
        this.information = information;
    }

}