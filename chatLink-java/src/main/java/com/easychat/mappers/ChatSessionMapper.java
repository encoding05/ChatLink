package com.easychat.mappers;

import org.apache.ibatis.annotations.Param;

/**
 *@ Description:会话信息Mapper
 *@ Date:2024/12/26
 */
public interface ChatSessionMapper<T, P> extends BaseMapper {

	/**
	 *@ Description:根据SessionId查询
	 */
	T selectBySessionId(@Param("sessionId") String sessionId);


	/**
	 *@ Description:根据SessionId更新
	 */
	Integer updateBySessionId(@Param("bean") T t, @Param("sessionId") String sessionId);


	/**
	 *@ Description:根据SessionId删除
	 */
	Integer deleteBySessionId(@Param("sessionId") String sessionId);


}