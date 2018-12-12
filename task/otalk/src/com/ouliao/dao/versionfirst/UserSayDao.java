/**
 * 
 */
package com.ouliao.dao.versionfirst;

import com.ouliao.domain.versionfirst.UserCommont;
import com.ouliao.domain.versionfirst.UserSayContent;
import com.ouliao.domain.versionfirst.UserSupportSay;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: UserCommontDao.java, 2016年2月19日 下午6:57:33
 */

public interface UserSayDao {
	boolean createUserCommontByUserId(UserCommont userCommont);

	boolean createUserSayContentByUserId(UserSayContent userSayContent);

	int deleteUserSayContentByUserId(Integer userId, Integer userSayContentId);

	List<UserSayContent> querySayContentByUserId(Integer userId);

	public Iterable<UserSupportSay> querySayAllSupports();

	void saveSupportsByUserId(UserSupportSay userSupportSay);

	List<UserSupportSay> querySupporIsDeletedByUserId(Integer userSayContentId);

	void updateSupportSayContentById(String isDeleted, Integer userId, Integer userSayContentId);

	UserSupportSay querySupportUniqueById(Integer userId, Integer userSayContentId);

	UserSupportSay querySupportUniqueExpecIsDeletedById(Integer userId, Integer userSayContentId);

	int deleteCommontById(Integer userCommontId, Integer userId, Integer ownerId);

	Page<UserCommont> querySayCommontAllByUserSayContentId(Integer startPage, Integer pageCount,
														   Integer userSayContentId);

	UserSayContent querySayContentByUserSayContentId(Integer userSayContentId);

	UserSayContent querySayContentUniqueById(Integer userId, Integer userSayContentId);

	UserCommont querySayCommontOneByUserCommontId(Integer userCommontId);

	void deleteCommontAllByUserCommontId(Integer userCommontId);

	List<UserCommont> querySayCommontAllByUserSayContentId(Integer userSayContentId);

	Integer querySupportCountById(Integer userSayContentId);

	Page<UserSayContent> querySayContentAllIsDeletedByUserId(Integer startPage, Integer pageCount, Integer userId);

	Integer querySayCommontCountCountByUserSayContentId(Integer userSayContentId);


}
