package jp.co.crypton.spinach.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huy on 9/1/2016.
 */
public class TopInfo {
    public List<Information> information;
    public class Information implements Serializable{

        public int id;
        public String title_ja;
        public String url_ja;
        public String title_en;
        public String url_en;
        public String date;
        public int show_as;

        public Information(int id, String title_ja, String url_ja, String title_en, String url_en, String date, int show_as) {
            this.id = id;
            this.title_ja = title_ja;
            this.url_ja = url_ja;
            this.title_en = title_en;
            this.url_en = url_en;
            this.date = date;
            this.show_as = show_as;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle_ja() {
            return title_ja;
        }

        public void setTitle_ja(String title_ja) {
            this.title_ja = title_ja;
        }

        public String getUrl_ja() {
            return url_ja;
        }

        public void setUrl_ja(String url_ja) {
            this.url_ja = url_ja;
        }

        public String getTitle_en() {
            return title_en;
        }

        public void setTitle_en(String title_en) {
            this.title_en = title_en;
        }

        public String getUrl_en() {
            return url_en;
        }

        public void setUrl_en(String url_en) {
            this.url_en = url_en;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getShow_as() {
            return show_as;
        }

        public void setShow_as(int show_as) {
            this.show_as = show_as;
        }

        @Override
        public String toString() {
            return "Information [id : "+id+" title : "+title_ja+" url_ja "+url_ja+" url_en "+url_en+" date "+date+" show_as "+show_as+"]";
        }
    }
}
