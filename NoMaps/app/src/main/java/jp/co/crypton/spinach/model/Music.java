package jp.co.crypton.spinach.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Music {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title_ja")
    @Expose
    private String titleJa;
    @SerializedName("url_ja")
    @Expose
    private String urlJa;
    @SerializedName("icon")
    @Expose
    private String icon;

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The titleJa
     */
    public String getTitleJa() {
        return titleJa;
    }

    /**
     *
     * @param titleJa
     * The title_ja
     */
    public void setTitleJa(String titleJa) {
        this.titleJa = titleJa;
    }

    /**
     *
     * @return
     * The urlJa
     */
    public String getUrlJa() {
        return urlJa;
    }

    /**
     *
     * @param urlJa
     * The url_ja
     */
    public void setUrlJa(String urlJa) {
        this.urlJa = urlJa;
    }

    /**
     *
     * @return
     * The icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     *
     * @param icon
     * The icon
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

}