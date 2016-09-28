package jp.co.crypton.spinach.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelDownload {

    @SerializedName("doc")
    @Expose
    private List<DocDownload> doc = new ArrayList<DocDownload>();

    /**
     *
     * @return
     * The doc
     */
    public List<DocDownload> getDoc() {
        return doc;
    }

    /**
     *
     * @param doc
     * The doc
     */
    public void setDoc(List<DocDownload> doc) {
        this.doc = doc;
    }

}