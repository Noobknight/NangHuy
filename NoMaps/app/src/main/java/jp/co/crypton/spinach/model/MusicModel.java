package jp.co.crypton.spinach.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MusicModel {

    @SerializedName("music")
    @Expose
    private List<Music> music = new ArrayList<Music>();

    /**
     *
     * @return
     * The music
     */
    public List<Music> getMusic() {
        return music;
    }

    /**
     *
     * @param music
     * The music
     */
    public void setMusic(List<Music> music) {
        this.music = music;
    }

}