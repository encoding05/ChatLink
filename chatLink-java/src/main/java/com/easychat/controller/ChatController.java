package com.easychat.controller;


import com.easychat.annotation.GlobalInterceptor;
import com.easychat.entity.config.AppConfig;
import com.easychat.entity.constants.Constants;
import com.easychat.entity.dto.MessageSendDto;
import com.easychat.entity.dto.TokenUserInfoDto;
import com.easychat.entity.enums.MessageTypeEnum;
import com.easychat.entity.enums.ResponseCodeEnum;
import com.easychat.entity.po.ChatMessage;
import com.easychat.entity.vo.ResponseVO;
import com.easychat.exception.BusinessException;
import com.easychat.service.ChatMessageService;
import com.easychat.service.ChatSessionUserService;
import com.easychat.utils.StringTools;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;


@RestController
@RequestMapping("/chat")
public class ChatController extends ABaseController{
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Resource
    private ChatMessageService chatMessageService;

    @Resource
    private ChatSessionUserService chatSessionUserService;

    @Resource
    private AppConfig appConfig;


    @RequestMapping("/sendMessage")
    @GlobalInterceptor
    public ResponseVO sendMessage(HttpServletRequest request, @NotEmpty String contactId,
                                  @NotEmpty @Max(500) String messageContent,
                                  @NotNull Integer messageType,
                                  Long fileSize,
                                  String fileName,
                                  Integer fileType) {

        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto(request);
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContactId(contactId);
        chatMessage.setMessageContent(messageContent);
        chatMessage.setFileSize(fileSize);
        chatMessage.setFileName(fileName);
        chatMessage.setMessageType(messageType);
        chatMessage.setFileType(fileType);
        MessageSendDto messageSendDto = chatMessageService.saveMessage(chatMessage, tokenUserInfoDto);

        return getSuccessResponseVO(messageSendDto);

    }

    @RequestMapping("/uploadFile")
    @GlobalInterceptor
    public ResponseVO uploadFile(HttpServletRequest request, @NotNull Long messageId,
                                 @NotNull MultipartFile file,
                                 @NotNull MultipartFile cover) {

        TokenUserInfoDto userInfoDto = getTokenUserInfoDto(request);
        chatMessageService.saveMessageFile(userInfoDto.getUserId(), messageId, file, cover);

        return getSuccessResponseVO(null);

    }

    @RequestMapping("/downloadFile")
    @GlobalInterceptor
    public void downloadFile(HttpServletRequest request, HttpServletResponse response, @NotEmpty String fileId,
                                   @NotNull Boolean showCover) {

        TokenUserInfoDto userInfoDto = getTokenUserInfoDto(request);
        OutputStream out = null;
        FileInputStream in = null;
        try {
            File file = null;
            if (StringTools.isNumber(fileId)) {
                String avatarFolderName = Constants.FILE_FOLDER_FILE + Constants.FILE_FOLDER_AVATAR_NAME;
                String avatarPath = appConfig.getProjectFolder() + avatarFolderName + fileId + Constants.IMAGE_SUFFIX;
                if (showCover)
                    avatarPath = avatarPath.replace(Constants.IMAGE_SUFFIX, Constants.COVER_IMAGE_SUFFIX);
                file = new File(avatarPath);
                if (!file.exists()) {
                    throw new BusinessException(ResponseCodeEnum.CODE_602);
                } else {
                    file = chatMessageService.downloadFile(userInfoDto, Long.parseLong(fileId), showCover);
                }
            }
            response.setContentType("application/x-msdownload;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;");
            response.setContentLengthLong(file.length());
            in = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            out = response.getOutputStream();
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            logger.error("下载文件失败", e);
        }finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                logger.error("关闭流失败", e);
            }
        }

    }
}
