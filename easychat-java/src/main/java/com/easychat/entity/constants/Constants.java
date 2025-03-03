package com.easychat.entity.constants;

import com.easychat.entity.enums.UserContactTypeEnum;

public class Constants {
    public static final String REDIS_KEY_CHECK_CODE = "easychat:checkCode:";

    public static final String REDIS_KEY_WS_USER_HEART_BEAT = "easychat:ws:user:HeartBeat:";

    public static final String REDIS_KEY_WS_TOKEN = "easychat:ws:token:";

    public static final String REDIS_KEY_WS_TOKEN_USERID = "easychat:ws:token:userID:";

    public static final String ROBOT_UID = UserContactTypeEnum.USER.getPrefix() + "robot";

    public static final String REDIS_KEY_SYS_SETTING = "easychat:syssetting";

    public static final Integer REDIS_TIME_1MIN = 60;

    public static final Integer REDIS_KEY_EXPIRE_DAY = 60 * 60 * 24;

    public static final Integer REDIS_KEY_EXPIRE_HEART_BEAT = 6;

    public static final Integer LENGTH_11 = 11;

    public static final Integer LENTH_20 = 20;

    public static final String FILE_FOLDER_FILE = "file/";

    public static final String FILE_FOLDER_AVATAR_NAME = "avatar/";

    public static final String IMAGE_SUFFIX = ".png";

    public static final String COVER_IMAGE_SUFFIX = "_cover.png";

    public static final String APPLY_INFO_TEMPLATE = "我是%s";

    public static final String REGEX_PASSWORD = "^(?=.*\\d)(?=.*[a-zA-Z])[\\da-zA-Z~!@#$%^&*_]{8,18}$";

    public static final String APP_UPDATE_FOLDER = "/app/";

    public static final String APP_UPDATE_FILE_SUFFIX = ".exe";

    public static final String APP_NAME = "EasyChatSetup.";

    public static final String REDIS_KEY_USER_CONTACT = "easychat:ws:user:contact:";

    public static final Long MILLISECONDS_3DAYS_AGO = 3 * 24 * 60 * 60 * 1000L;

    public static final String[] IMAGE_SUFFIX_LIST = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp"};

    public static final String[] VIDEO_SUFFIX_LIST = {".mp4", ".avi", ".mov", ".rmvb", ".flv", ".3gp", ".wmv", ".mkv"};

    public static final Long FILE_SIZE_MB = 1024 * 1024L;
}
