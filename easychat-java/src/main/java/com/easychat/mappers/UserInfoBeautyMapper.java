package com.easychat.mappers;

import org.apache.ibatis.annotations.Param;

/**
 *@ Description:靓号Mapper
 *@ Date:2024/11/21
 */
public interface UserInfoBeautyMapper<T, P> extends BaseMapper {

	/**
	 *@ Description:根据Id查询
	 */
	T selectById(@Param("id") Integer id);


	/**
	 *@ Description:根据Id更新
	 */
	Integer updateById(@Param("bean") T t, @Param("id") Integer id);


	/**
	 *@ Description:根据Id删除
	 */
	Integer deleteById(@Param("id") Integer id);


	/**
	 *@ Description:根据Email查询
	 */
	T selectByEmail(@Param("email") String email);


	/**
	 *@ Description:根据Email更新
	 */
	Integer updateByEmail(@Param("bean") T t, @Param("email") String email);


	/**
	 *@ Description:根据Email删除
	 */
	Integer deleteByEmail(@Param("email") String email);


	/**
	 *@ Description:根据UserId查询
	 */
	T selectByUserId(@Param("userId") String userId);


	/**
	 *@ Description:根据UserId更新
	 */
	Integer updateByUserId(@Param("bean") T t, @Param("userId") String userId);


	/**
	 *@ Description:根据UserId删除
	 */
	Integer deleteByUserId(@Param("userId") String userId);


}