package com.easychat.mappers;

import org.apache.ibatis.annotations.Param;

/**
 *@ Description:app发布Mapper
 *@ Date:2024/12/14
 */
public interface AppUpdateMapper<T, P> extends BaseMapper {

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

	T selectLatestUpdate(@Param("appVersion") String appVersion, @Param("uid") String uid);
}