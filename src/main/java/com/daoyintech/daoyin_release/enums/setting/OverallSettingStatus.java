package com.daoyintech.daoyin_release.enums.setting;

public enum OverallSettingStatus {
    off,
    on;
    public static OverallSettingStatus getStatus(int status){
        switch (status){
            case 0:
                return OverallSettingStatus.off;
            case 1:
                return OverallSettingStatus.on;
            default:
                return OverallSettingStatus.off;
        }
    }
}
