package com.easychat.mappers;

import org.apache.ibatis.annotations.Param;

/**
 *@ Description:聊天消息表Mapper
 *@ Date:2024/12/26
 */
public interface ChatMessageMapper<T, P> extends BaseMapper {

	/**
	 *@ Description:根据MessageId查询
	 */
	T selectByMessageId(@Param("messageId") Long messageId);


	/**
	 *@ Description:根据MessageId更新
	 */
	Integer updateByMessageId(@Param("bean") T t, @Param("messageId") Long messageId);


	/**
	 *@ Description:根据MessageId删除
	 */
	Integer deleteByMessageId(@Param("messageId") Long messageId);


}