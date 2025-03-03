package com.easychat.redis;


import com.easychat.entity.constants.Constants;
import com.easychat.entity.dto.SysSettingDto;
import com.easychat.entity.dto.TokenUserInfoDto;
import com.easychat.utils.StringTools;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.channels.Channel;
import java.util.List;

@Component("redisComponent")
public class RedisComponent {

    @Resource
    private RedisUtils redisUtils;

    public Long getUserHeartBeat(String userId) {
        return (Long) redisUtils.get(Constants.REDIS_KEY_WS_USER_HEART_BEAT + userId);
    }

    public void saveUserHeartBeat(String userId) {
        redisUtils.setex(Constants.REDIS_KEY_WS_USER_HEART_BEAT + userId, System.currentTimeMillis(), Constants.REDIS_KEY_EXPIRE_HEART_BEAT);
    }

    public void removeHeartBeat(String userId) {
        redisUtils.delete(Constants.REDIS_KEY_WS_USER_HEART_BEAT + userId);
    }


    public void saveTokenUserInfoDto(TokenUserInfoDto tokenUserInfoDto) {
        redisUtils.setex(Constants.REDIS_KEY_WS_TOKEN + tokenUserInfoDto.getToken(), tokenUserInfoDto, Constants.REDIS_KEY_EXPIRE_DAY);
        redisUtils.setex(Constants.REDIS_KEY_WS_TOKEN_USERID + tokenUserInfoDto.getUserId(), tokenUserInfoDto.getToken(), Constants.REDIS_KEY_EXPIRE_DAY * 2);
    }

    public TokenUserInfoDto getTokenUserInfoDto(String token) {
        return (TokenUserInfoDto) redisUtils.get(Constants.REDIS_KEY_WS_TOKEN + token);
    }

    public TokenUserInfoDto getTokenUserInfoDtoByUserId(String userId) {
        String token = (String) redisUtils.get(Constants.REDIS_KEY_WS_TOKEN_USERID + userId);
        return getTokenUserInfoDto(token);
    }

    public void cleanUserTokenByUserId(String userId) {
        String token = (String) redisUtils.get(Constants.REDIS_KEY_WS_TOKEN_USERID + userId);
        if (StringTools.isEmpty(token)) {
            return;
        }
        redisUtils.delete(Constants.REDIS_KEY_WS_TOKEN + token);
    }

    public SysSettingDto getSysSetting() {
        SysSettingDto sysSettingDto = (SysSettingDto) redisUtils.get(Constants.REDIS_KEY_SYS_SETTING);
        sysSettingDto = sysSettingDto == null ? new SysSettingDto() : sysSettingDto;
        return sysSettingDto;
    }

    public void saveSysSetting(SysSettingDto sysSettingDto) {
        redisUtils.set(Constants.REDIS_KEY_SYS_SETTING, sysSettingDto);
    }

    public void saveChannel(String userId, Channel channel) {
        redisUtils.set(Constants.REDIS_KEY_WS_TOKEN + userId, channel);
    }

    // 清空联系人
    public void clearUserContact(String userId) {
        redisUtils.delete(Constants.REDIS_KEY_USER_CONTACT + userId);
    }
    // 批量添加联系人
    public void addUserContactBatch(String userId, List<String> contactList) {
        redisUtils.lpushAll(Constants.REDIS_KEY_USER_CONTACT + userId, contactList, Constants.REDIS_KEY_EXPIRE_DAY * 2);
    }

    public void addUserContact(String userId, String contactId) {
        List<String> contactList = getUserContactList(userId);
        if (contactList.contains(contactId)) {
            return;
        }
        redisUtils.lpush(Constants.REDIS_KEY_USER_CONTACT + userId, contactId, Constants.REDIS_KEY_EXPIRE_DAY * 2);
    }

    public List<String> getUserContactList(String userId) {
        return (List<String>)redisUtils.getQueueList(Constants.REDIS_KEY_USER_CONTACT + userId);
    }

    public void removeUserContact(String userId, String contactId) {
        redisUtils.remove(Constants.REDIS_KEY_USER_CONTACT + userId, contactId);
    }
}
