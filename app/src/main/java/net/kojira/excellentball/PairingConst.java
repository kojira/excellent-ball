package net.kojira.excellentball;


import java.util.Calendar;

public class PairingConst {

    public static final String INTENT_EXTRA_SENSOR_ID = "sensor_id";

    /**
     * 開発者ボードのセンサ情報を取得するIDです。
     * 他のデバイスのセンサ情報取得時に使用するセンサタイプのIDは
     * デバイス仕様書に従った値を使用してください。
     */
    public static final int SENSOR_ID_ANGULAR = 0;
    public static final int SENSOR_ID_ACCEL = 1;
    public static final int SENSOR_ID_ORIENT = 2;

    public static final String SENSOR_TAG_ACCEL = "加速度センサ";
    public static final String SENSOR_TAG_ANGULAR = "ジャイロ（角速度）センサ";
    public static final String SENSOR_TAG_ORIENT = "方位（地磁気センサ）";

    /**
     * 渡されたセンサタイプのIDから対応したセンサ名を返す
     * @param id センサタイプのID
     * @return 上記で定義しているセンサ名
     */
    public static String getSensorName(int id) {

        String name = "";

        switch(id) {
            case SENSOR_ID_ACCEL:
                name = SENSOR_TAG_ACCEL;
                break;
            case SENSOR_ID_ANGULAR:
                name = SENSOR_TAG_ANGULAR;
                break;
            case SENSOR_ID_ORIENT:
                name = SENSOR_TAG_ORIENT;
                break;
        }

        return name;

    }


    /**
     * デバイスからサービスアプリへの通知を受け取るBroadcastReceiverのActionフィルター
     */
    public static final String ACTION_NOTIFY = "com.nttdocomo.android.smartdeviceagent.action.NOTIFICATION";

    /**
     * デバイス側のボタンが押下された時に通知を受けるBroadcastReceiverにて、
     * ボタンIDを受け取るためのExtraキー
     **/
    public static final String EXTRA_DEVICE_NAME = "com.nttdocomo.android.smartdeviceagent.extra.APP_NAME";
    public static final String EXTRA_DEVICE_URI_1 = "com.nttdocomo.android.smartdeviceagent.extra.CONTENT_URI_1";
    public static final String EXTRA_DEVICE_URI_2 = "com.nttdocomo.android.smartdeviceagent.extra.CONTENT_URI_2";
    public static final String EXTRA_DEVICE_URI_3 = "com.nttdocomo.android.smartdeviceagent.extra.CONTENT_URI_3";
    public static final String EXTRA_DEVICE_URI_4 = "com.nttdocomo.android.smartdeviceagent.extra.CONTENT_URI_4";
    public static final String EXTRA_DEVICE_URI_5 = "com.nttdocomo.android.smartdeviceagent.extra.CONTENT_URI_5";
    public static final String EXTRA_DEVICE_URI_6 = "com.nttdocomo.android.smartdeviceagent.extra.CONTENT_URI_6";
    public static final String EXTRA_DEVICE_URI_7 = "com.nttdocomo.android.smartdeviceagent.extra.CONTENT_URI_7";
    public static final String EXTRA_DEVICE_URI_8 = "com.nttdocomo.android.smartdeviceagent.extra.CONTENT_URI_8";
    public static final String EXTRA_DEVICE_URI_9 = "com.nttdocomo.android.smartdeviceagent.extra.CONTENT_URI_9";
    public static final String EXTRA_DEVICE_URI_10 = "com.nttdocomo.android.smartdeviceagent.extra.CONTENT_URI_10";
    public static final String EXTRA_DEVICE_IMAGE = "com.nttdocomo.android.smartdeviceagent.extra.IMAGE_URI";
    public static final String EXTRA_DEVICE_IMAGE_TYPE = "com.nttdocomo.android.smartdeviceagent.extra.IMAGE_TYPE";
    public static final String EXTRA_DEVICE_MEDIA = "com.nttdocomo.android.smartdeviceagent.extra.MEDIA_URI";
    public static final String EXTRA_DEVICE_MEDIA_TYEP = "com.nttdocomo.android.smartdeviceagent.extra.MEDIA_TYPE";
    public static final String EXTRA_DEVICE_NOTIF = "com.nttdocomo.android.smartdeviceagent.extra.NOTIFICATION_ID";
    public static final String EXTRA_DEVICE_NOTIF_ID = "com.nttdocomo.android.smartdeviceagent.extra.NOTIFICATION_CATEGORY_ID";
    public static final String EXTRA_DEVICE_ID = "com.nttdocomo.android.smartdeviceagent.extra.DEVICE_ID";
    public static final String EXTRA_DEVICE_UID = "com.nttdocomo.android.smartdeviceagent.extra.DEVICE_UID";
    public static final String EXTRA_BUTTON_ID = "com.nttdocomo.android.smartdeviceagent.extra.NOTIFICATION_DEVICE_BUTTON_ID";


    /**
     * ミリ秒表示で与えられた時間を見やすくフォーマットする
     * @param data System.currentTimeMillis()などで取得された値
     * @return 引数で与えられた時間を[MM-DD hh:mm:ss]に変換した文字列
     */
    public static String timeLogFormat(long data) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(data);
        String time = String.format("%02d-%02d %02d:%02d:%02d",
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND));
        return time;
    }

}
