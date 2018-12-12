/**
 * 
 */
package com.ouliao.dao.versionfirst;

import com.ouliao.domain.versionfirst.UserReflect;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: UserReflectCrudDao.java, 2016年2月26日 下午1:28:57
 */

public interface UserReflectDao {
	void creatUserReflect(UserReflect userReflect);

	Long queryReflectCountByAll();

	int updateIsDeletedByUserReflectId(List<Integer> ids);

	List<UserReflect> queryUserReflectAllByIsDeleted();

	Page<UserReflect> queryUserReflectWithDrawByUserId(Integer starPage, Integer pageNum, Integer userId);

	List<UserReflect> queryUserReflectWithDrawAllByUserId(Integer userId);

}
