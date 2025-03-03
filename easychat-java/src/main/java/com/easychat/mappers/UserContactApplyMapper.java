package com.easychat.mappers;

import org.apache.ibatis.annotations.Param;

/**
 *@ Description:用户联系人申请表Mapper
 *@ Date:2024/12/02
 */
public interface UserContactApplyMapper<T, P> extends BaseMapper {

	/**
	 *@ Description:根据ApplyId查询
	 */
	T selectByApplyId(@Param("applyId") Integer applyId);


	/**
	 *@ Description:根据ApplyId更新
	 */
	Integer updateByApplyId(@Param("bean") T t, @Param("applyId") Integer applyId);

	Integer updateByParam(@Param("bean") T t, @Param("query") P p);
	/**
	 *@ Description:根据ApplyId删除
	 */
	Integer deleteByApplyId(@Param("applyId") Integer applyId);


	/**
	 *@ Description:根据ApplyUserIdAndReceiveUserIdAndContactId查询
	 */
	T selectByApplyUserIdAndReceiveUserIdAndContactId(@Param("applyUserId") String applyUserId, @Param("receiveUserId") String receiveUserId, @Param("contactId") String contactId);


	/**
	 *@ Description:根据ApplyUserIdAndReceiveUserIdAndContactId更新
	 */
	Integer updateByApplyUserIdAndReceiveUserIdAndContactId(@Param("bean") T t, @Param("applyUserId") String applyUserId, @Param("receiveUserId") String receiveUserId, @Param("contactId") String contactId);


	/**
	 *@ Description:根据ApplyUserIdAndReceiveUserIdAndContactId删除
	 */
	Integer deleteByApplyUserIdAndReceiveUserIdAndContactId(@Param("applyUserId") String applyUserId, @Param("receiveUserId") String receiveUserId, @Param("contactId") String contactId);

}