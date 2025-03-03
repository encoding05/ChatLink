package com.easychat.entity.enums;

public enum ResponseCodeEnum {
    CODE_200(200, "请求成功"),
    CODE_404(404, "请求资源不存在"),
    CODE_600(600, "请求参数错误"),
    CODE_601(601, "信息已经存在"),
    CODE_602(602, "文件不存在"),
    CODE_901(901, "登录超时"),
    CODE_902(902, "您不是对方好友，请先向好友发送验证申请"),
    CODE_903(903, "您已经不在群聊，请重新加入群聊"),
    CODE_500(500, "服务器返回错误");

    private Integer code;

    private String message;

    ResponseCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return message;
    }
}
