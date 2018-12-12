/**
 * 
 */
package com.ouliao.controller.versionfirst;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: TestSessionController.java, 2016年3月8日 下午12:43:10
 */
@RequestMapping("/testSession")
@Controller
public class TestSessionController {
	public static void main(String[] args) {
		String[] s = new String[] { "aaa", "bbb", "ccc" };
		List list = new ArrayList();
		list = Arrays.asList(s);
		list = new ArrayList(list);
		list.remove(0);
		System.out.println(list);

	}
}
