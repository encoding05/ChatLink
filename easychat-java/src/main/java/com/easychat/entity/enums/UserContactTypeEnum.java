package com.easychat.entity.enums;

import com.easychat.utils.StringTools;

public enum UserContactTypeEnum {
    USER(0, "U", "好友"),
    GROUP(1, "G", "群组");

    private Integer type;
    private String prefix;
    private String desc;

    UserContactTypeEnum(Integer type, String prefix, String desc) {
        this.type = type;
        this.prefix = prefix;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getDesc() {
        return desc;
    }

    // getByName
    public static UserContactTypeEnum getByName(String name) {
        try {
            if (StringTools.isEmpty(name)) {
                return null;
            }
            return UserContactTypeEnum.valueOf(name.toUpperCase());
        } catch (Exception e){
            return null;
        }
    }

    // getByPrefix
    public static UserContactTypeEnum getByPrefix(String prefix) {
        try {
            if (StringTools.isEmpty(prefix) || prefix.trim().isEmpty()) {
                return null;
            }
            prefix = prefix.substring(0, 1);
            for (UserContactTypeEnum userContactTypeEnum : UserContactTypeEnum.values()) {
                if (userContactTypeEnum.getPrefix().equals(prefix)) {
                    return userContactTypeEnum;
                }
            }
            return null;
        } catch (Exception e){
            return null;
        }
    }
}
