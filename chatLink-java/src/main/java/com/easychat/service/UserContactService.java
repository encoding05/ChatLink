package com.easychat.service;

import com.easychat.entity.dto.TokenUserInfoDto;
import com.easychat.entity.dto.UserContactSearchResultDto;
import com.easychat.entity.enums.UserContactStatusEnum;
import com.easychat.entity.po.UserContact;
import com.easychat.entity.query.UserContactQuery;
import java.util.List;
import com.easychat.entity.vo.PaginationResultVO;
/**
 *@ Description:用户联系人表Service
 *@ Date:2024/12/02
 */
public interface UserContactService {

	/**
	 *@ Description:根据条件查询列表
	 */
	List<UserContact> findListByParam(UserContactQuery query);

	/**
	 *@ Description:根据条件查询数量
	 */
	Integer findCountByParam(UserContactQuery query);

	/**
	 *@ Description:分页查询
	 */
	PaginationResultVO<UserContact> findListByPage(UserContactQuery query);

	/**
	 *@ Description:新增
	 */
	Integer add(UserContact bean);

	/**
	 *@ Description:批量新增
	 */
	Integer addBatch(List<UserContact> listBean);

	/**
	 *@ Description:批量新增或修改
	 */
	Integer addOrUpdateBatch(List<UserContact> listBean);

	/**
	 *@ Description:根据UserIdAndContactId查询
	 */
	UserContact getUserContactByUserIdAndContactId(String userId, String contactId);

	/**
	 *@ Description:根据UserIdAndContactId更新
	 */
	Integer updateUserContactByUserIdAndContactId(UserContact bean,String userId, String contactId);

	/**
	 *@ Description:根据UserIdAndContactId删除
	 */
	Integer deleteUserContactByUserIdAndContactId(String userId, String contactId);


	UserContactSearchResultDto searchContact(String userId, String contactId);

	Integer applyAdd(TokenUserInfoDto tokenUserInfoDto, String contactId, String applyInfo);

	void addContact(String applyUserId, String receiveUserId, String contactId, Integer contactType, String applyInfo);

	void removeUserContact(String userId, String contactId, UserContactStatusEnum statusEnum);

	void addContact4Robot(String userId);
}