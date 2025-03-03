package com.easychat.service;

import com.easychat.entity.dto.TokenUserInfoDto;
import com.easychat.entity.enums.MessageTypeEnum;
import com.easychat.entity.po.GroupInfo;
import com.easychat.entity.query.GroupInfoQuery;

import java.io.IOException;
import java.util.List;
import com.easychat.entity.vo.PaginationResultVO;
import org.springframework.web.multipart.MultipartFile;

/**
 *@ Description:Service
 *@ Date:2024/12/02
 */
public interface GroupInfoService {

	/**
	 *@ Description:根据条件查询列表
	 */
	List<GroupInfo> findListByParam(GroupInfoQuery query);

	/**
	 *@ Description:根据条件查询数量
	 */
	Integer findCountByParam(GroupInfoQuery query);

	/**
	 *@ Description:分页查询
	 */
	PaginationResultVO<GroupInfo> findListByPage(GroupInfoQuery query);

	/**
	 *@ Description:新增
	 */
	Integer add(GroupInfo bean);

	/**
	 *@ Description:批量新增
	 */
	Integer addBatch(List<GroupInfo> listBean);

	/**
	 *@ Description:批量新增或修改
	 */
	Integer addOrUpdateBatch(List<GroupInfo> listBean);

	/**
	 *@ Description:根据GroupId查询
	 */
	GroupInfo getGroupInfoByGroupId(String groupId);

	/**
	 *@ Description:根据GroupId更新
	 */
	Integer updateGroupInfoByGroupId(GroupInfo bean,String groupId);

	/**
	 *@ Description:根据GroupId删除
	 */
	Integer deleteGroupInfoByGroupId(String groupId);


	void saveGroup(GroupInfo groupInfo, MultipartFile avatarFile, MultipartFile avatarCover) throws IOException;

	void dissolutionGroup(String groupOwnerId, String groupId);

	void addOrRemoveGroupUser(TokenUserInfoDto tokenUserInfoDto, String groupId, String selectContacts, Integer opType);

	void leaveGroup(String userId, String groupId, MessageTypeEnum messageTypeEnum);
}