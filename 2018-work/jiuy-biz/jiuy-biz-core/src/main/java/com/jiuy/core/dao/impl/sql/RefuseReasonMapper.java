package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.meta.member.RefuseReason;

@Repository
public class RefuseReasonMapper {
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
    
	public List<RefuseReason> getList(){
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.mapper.RefuseReasonMapper.getList");
	}

	public int deleteById(long id){
		HashMap<String, Object> params = new HashMap<String,Object>();
		
		params.put("id", id);
		return sqlSessionTemplate.delete("com.jiuy.core.dao.mapper.RefuseReasonMapper.deleteById",params);
	}

	public int insertRefuseReason(String refuseReason){
		HashMap<String, Object> params = new HashMap<String,Object>();
		
		params.put("refuseReason", refuseReason);
		return sqlSessionTemplate.insert("com.jiuy.core.dao.mapper.RefuseReasonMapper.insertRefuseReason",params);
	}
	
	
   
}
