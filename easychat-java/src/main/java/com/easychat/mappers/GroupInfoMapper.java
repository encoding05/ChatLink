package com.easychat.mappers;

import org.apache.ibatis.annotations.Param;

/**
 *@ Description:Mapper
 *@ Date:2024/12/02
 */
public interface GroupInfoMapper<T, P> extends BaseMapper {

	/**
	 *@ Description:根据GroupId查询
	 */
	T selectByGroupId(@Param("groupId") String groupId);


	/**
	 *@ Description:根据GroupId更新
	 */
	Integer updateByGroupId(@Param("bean") T t, @Param("groupId") String groupId);


	/**
	 *@ Description:根据GroupId删除
	 */
	Integer deleteByGroupId(@Param("groupId") String groupId);


}