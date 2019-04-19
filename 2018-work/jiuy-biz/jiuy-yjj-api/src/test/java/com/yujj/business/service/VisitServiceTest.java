package com.yujj.business.service;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.visit.UserVisitHistory;
import com.yujj.test.util.YjjAbstractUnitilsTest;

public class VisitServiceTest extends YjjAbstractUnitilsTest {

	@Autowired
	private VisitService visitService;

	@Test
	@Ignore
	public void testGetVisitList() {
		List<UserVisitHistory> visitList = visitService.getVisitList(1L,
				new PageQuery());
	}

	@Test
	@Ignore
	public void testGetVisitListCount() {
		int count = visitService.getVisitListCount(1L);
	}

	@Test
	//@Ignore
	public void testAddVisitHistory() {
		Long[] ids = new Long[] { 1L };
		//visitService.addVisitHistory(1L, ids);
        new Dervied(); 
	}
	
	public class Dervied extends Base {

	    private String name = "dervied";

	    public Dervied() {
	        tellName();
	        printName();
	    }
	    
	    public void tellName() {
	    }
	    
	    public void printName() {
	    }
	}

	class Base {
	    
	    private String name = "base";

	    public Base() {
	        tellName();
	        printName();
	    }
	    
	    public void tellName() {
	    }
	    
	    public void printName() {
	    }
	}
}

