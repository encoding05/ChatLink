package com.easychat.mappers;

import org.apache.ibatis.annotations.Param;

/**
 *@ Description:会话用户Mapper
 *@ Date:2024/12/26
 */
public interface ChatSessionUserMapper<T, P> extends BaseMapper {

	/**
	 *@ Description:根据UserIdAndContactId查询
	 */
	T selectByUserIdAndContactId(@Param("userId") String userId, @Param("contactId") String contactId);


	/**
	 *@ Description:根据UserIdAndContactId更新
	 */
	Integer updateByUserIdAndContactId(@Param("bean") T t, @Param("userId") String userId, @Param("contactId") String contactId);


	/**
	 *@ Description:根据UserIdAndContactId删除
	 */
	Integer deleteByUserIdAndContactId(@Param("userId") String userId, @Param("contactId") String contactId);

	Integer updateByParam(@Param("bean") T t, @Param("query") P p);

}