package com.easychat.entity.enums;

import com.easychat.utils.StringTools;

public enum UserContactStatusEnum {
    NOT_FRIEND(0, "非好友"),
    FRIEND(1, "好友"),
    DEL(2, "已删除"),
    DEL_BE(3, "被好友删除"),
    BLACKLIST(4, "已拉黑好友"),
    BLACKLIST_BE(5, "被好友拉黑");

    private Integer status;

    private String desc;

    UserContactStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static UserContactStatusEnum getByStatus(String status) {
        try {
            if (StringTools.isEmpty(status)){
                return null;
            }
            return UserContactStatusEnum.valueOf(status);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static UserContactStatusEnum getByStatus(Integer status) {
        for (UserContactStatusEnum userContactStatusEnum : UserContactStatusEnum.values()) {
            if (userContactStatusEnum.getStatus().equals(status)) {
                return userContactStatusEnum;
            }
        }
        return null;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc(){
        return desc;
    }
}
