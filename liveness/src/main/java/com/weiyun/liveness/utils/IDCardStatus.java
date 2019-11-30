package com.weiyun.liveness.utils;


import com.weiyun.liveness.R;

/**
 * 摄像头识别状态
 */
public class IDCardStatus {

    private static final String a = IDCardStatus.class.getSimpleName();
    public static final int SUCCESS = 0;
    public static final int STATUS1 = 1;
    public static final int STATUS2 = 2;
    public static final int STATUS3 = 3;
    public static final int STATUS4 = 4;
    public static final int STATUS5 = 5;
    public static final int STATUS6 = 6;
    public static final int STATUS7 = 7;
    public static final int STATUS8 = 8;
    public static final int STATUS9 = 9;
    public static final int STATUS10 = 10;
    public static final int STATUS11 = 11;

    public IDCardStatus() {
    }

    public static int getHintFromStatus(int var0) {
        switch (var0) {
            case 0:
                return R.string.id_card_camera_hint0;
            case 1:
                return R.string.id_card_camera_hint1;
            case 2:
                return R.string.id_card_camera_hint2;
            case 3:
                return R.string.id_card_camera_hint3;
            case 4:
                return R.string.id_card_camera_hint4;
            case 5:
                return R.string.id_card_camera_hint5;
            case 6:
                return R.string.id_card_camera_hint6;
            case 7:
                return R.string.id_card_camera_hint7;
            case 8:
                return R.string.id_card_camera_hint8;
            case 9:
                return R.string.id_card_camera_hint9;
            case 10:
                return R.string.id_card_camera_hint10;
            case 11:
                return R.string.id_card_camera_hint11;
            default:
                return R.string.id_card_camera_hint_defalut;
        }
    }
}
