package com.easychat.service;

import com.easychat.entity.po.UserContactApply;
import com.easychat.entity.query.UserContactApplyQuery;
import java.util.List;
import com.easychat.entity.vo.PaginationResultVO;
/**
 *@ Description:用户联系人申请表Service
 *@ Date:2024/12/02
 */
public interface UserContactApplyService {

	/**
	 *@ Description:根据条件查询列表
	 */
	List<UserContactApply> findListByParam(UserContactApplyQuery query);

	/**
	 *@ Description:根据条件查询数量
	 */
	Integer findCountByParam(UserContactApplyQuery query);

	/**
	 *@ Description:分页查询
	 */
	PaginationResultVO<UserContactApply> findListByPage(UserContactApplyQuery query);

	/**
	 *@ Description:新增
	 */
	Integer add(UserContactApply bean);

	/**
	 *@ Description:批量新增
	 */
	Integer addBatch(List<UserContactApply> listBean);

	/**
	 *@ Description:批量新增或修改
	 */
	Integer addOrUpdateBatch(List<UserContactApply> listBean);

	/**
	 *@ Description:根据ApplyId查询
	 */
	UserContactApply getUserContactApplyByApplyId(Integer applyId);

	/**
	 *@ Description:根据ApplyId更新
	 */
	Integer updateUserContactApplyByApplyId(UserContactApply bean,Integer applyId);

	/**
	 *@ Description:根据ApplyId删除
	 */
	Integer deleteUserContactApplyByApplyId(Integer applyId);

	/**
	 *@ Description:根据ApplyUserIdAndReceiveUserIdAndContactId查询
	 */
	UserContactApply getUserContactApplyByApplyUserIdAndReceiveUserIdAndContactId(String applyUserId, String receiveUserId, String contactId);

	/**
	 *@ Description:根据ApplyUserIdAndReceiveUserIdAndContactId更新
	 */
	Integer updateUserContactApplyByApplyUserIdAndReceiveUserIdAndContactId(UserContactApply bean,String applyUserId, String receiveUserId, String contactId);

	/**
	 *@ Description:根据ApplyUserIdAndReceiveUserIdAndContactId删除
	 */
	Integer deleteUserContactApplyByApplyUserIdAndReceiveUserIdAndContactId(String applyUserId, String receiveUserId, String contactId);

	void dealWithApply(String userId, Integer applyId, Integer status);

}