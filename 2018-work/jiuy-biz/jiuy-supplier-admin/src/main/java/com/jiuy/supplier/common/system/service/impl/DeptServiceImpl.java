package com.jiuy.supplier.common.system.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuy.supplier.common.system.persistence.dao.DeptMapper;
import com.jiuy.supplier.common.system.persistence.model.Dept;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuy.supplier.common.system.service.IDeptService;

@Service
@Transactional()
public class DeptServiceImpl implements IDeptService {

	@Resource
	DeptMapper deptMapper;

	@Override
	public void deleteDept(Integer deptId) {

		Dept dept = deptMapper.selectById(deptId);

		Wrapper<Dept> wrapper = new EntityWrapper<>();
		wrapper = wrapper.like("pids", "%[" + dept.getId() + "]%");
		List<Dept> subDepts = deptMapper.selectList(wrapper);
		for (Dept temp : subDepts) {
			temp.deleteById();
		}

		dept.deleteById();
	}
}
