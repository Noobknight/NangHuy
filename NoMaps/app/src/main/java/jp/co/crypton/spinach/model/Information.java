package jp.co.crypton.spinach.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Information {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title_ja")
    @Expose
    private String titleJa;
    @SerializedName("url_ja")
    @Expose
    private String urlJa;
    @SerializedName("title_en")
    @Expose
    private String titleEn;
    @SerializedName("url_en")
    @Expose
    private String urlEn;
    @SerializedName("show_as")
    @Expose
    private Integer showAs;
    @SerializedName("date")
    @Expose
    private String date;

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
     * The titleEn
     */
    public String getTitleEn() {
        return titleEn;
    }

    /**
     *
     * @param titleEn
     * The title_en
     */
    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    /**
     *
     * @return
     * The urlEn
     */
    public String getUrlEn() {
        return urlEn;
    }

    /**
     *
     * @param urlEn
     * The url_en
     */
    public void setUrlEn(String urlEn) {
        this.urlEn = urlEn;
    }

    /**
     *
     * @return
     * The showAs
     */
    public Integer getShowAs() {
        return showAs;
    }

    /**
     *
     * @param showAs
     * The show_as
     */
    public void setShowAs(Integer showAs) {
        this.showAs = showAs;
    }

    /**
     *
     * @return
     * The date
     */
    public String getDate() {
        return date;
    }

    /**
     *
     * @param date
     * The date
     */
    public void setDate(String date) {
        this.date = date;
    }

}