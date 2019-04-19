/**
 * 
 */
package com.yujj.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.product.Assessment;

/**
 * @author LWS
 *
 */
@DBMaster
public interface AssessmentMapper {

	public List<Assessment> loadAssessmentList(@Param("productId")long productId,@Param("userId")String userId,
			@Param("pageNum")int pageNum, @Param("pageSize")int pageSize);

	public List<Assessment> loadAll(@Param("productId")String productId,@Param("userId")String userId);
}
