/**
 * 
 */
package com.ouliao.service.versionfirst;

import com.ouliao.domain.versionfirst.UserReflect;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: UserReflectCrudService.java, 2016年2月26日 下午1:29:32
 */

public interface UserReflectService {
	void creatUserReflect(UserReflect userReflect);

	Long queryReflectCountByAll();

	int updateIsDeletedByUserReflectId(List<Integer> ids);

	List<UserReflect> queryUserReflectAllByIsDeleted();

	Page<UserReflect> queryUserReflectWithDrawByUserId(Integer starPage, Integer pageNum, Integer userId);

	List<UserReflect> queryUserReflectWithAllDrawByUserId(Integer userId);
}
