package jp.co.crypton.spinach.constants;

/**
 * ABS
 */
public class Constants {
    public static final String API_DOMAIN = "http://smartplate.kokope.li/access_history.php";
    public static final String NOMAPS_URL_PREFIX = "http://plate.id/";
    public static final String SMARTPLATE_PREFIX = "http://plate.id/";
    public static final String SMARTPLATE_PREFIX_NEXTPAGE = "http://plate.id/tiles/tiles.php";
    public static final String QR_CODE_URL_PREFIX = "http://plate.id/NM";
//    public static final String SCHEDULE_IMAGE_DIR = "http://mt.aquabit.net/nomaps/images/";
    public static final String SCHEDULE_IMAGE_DIR = "https://s3-ap-northeast-1.amazonaws.com/aquabit/nomaps/schedule/";
    public static final String URL_LICENSE = "https://s3-ap-northeast-1.amazonaws.com/aquabit/nomaps/agreement.txt";
    public static final String URL_BOOT = "https://s3-ap-northeast-1.amazonaws.com/aquabit/nomaps/boot.json";


    public static final String NOMAPS_PROJECT_ID = "2852";

    public static final String URL_TOP_INFO = "https://s3-ap-northeast-1.amazonaws.com/aquabit/nomaps/nomaps_top.json";

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int API_GET_TOP_INFO = 100;

    public static final String INTENT_KEY_NFC_URL = "NFC_URL";
    public static final String INTENT_KEY_WEBVIEW_TYPE = "WEBVIEW_TYPE";
    public static final String INTENT_KEY_WEBVIEW_URL = "WEBVIEW_URL";
    public static final String INTENT_KEY_FOOTPRINT = "FOOTPRINT";


    public static final int WEBVIEW_TYPE_SP = 0;
    public static final int WEBVIEW_TYPE_NORMAL = 1;

    public static final String FILE_NAME_JSON = "jsonFootprint";
    public static final String FILE_NAME_JSON_TOP = "jsonInfoTop";
    public static final String API_DOMAIN_JSON_DOWNLOAD = "http://smartplate.kokope.li/access_history_category";
    public static final String FILE_NAME_JSON_DOWNLOAD = "jsonDownload";
    public static final String FILE_NAME_JSON_MUSIC = "jsonMusic";
    public static final String FILE_NAME_JSON_LICENCE = "jsonLicense";



}
