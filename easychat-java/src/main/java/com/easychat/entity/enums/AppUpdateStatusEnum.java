package com.easychat.entity.enums;

public enum AppUpdateStatusEnum {
    INIT(0, "未发布"), GRAYSCALE(1, "灰度发布"), ALL(2, "已发布");

    private Integer status;
    private String desc;

    AppUpdateStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

    public static AppUpdateStatusEnum getEnumByStatus(Integer status) {
        for (AppUpdateStatusEnum appUpdateStatusEnum : AppUpdateStatusEnum.values()) {
            if (appUpdateStatusEnum.getStatus().equals(status)) {
                return appUpdateStatusEnum;
            }
        }
        return null;
    }
}
