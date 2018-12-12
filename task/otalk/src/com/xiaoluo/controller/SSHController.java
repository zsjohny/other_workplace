package com.xiaoluo.controller;

import com.xiaoluo.dao.SSHDao;
import com.xiaoluo.entity.Customer;
import com.xiaoluo.service.SSHService;
import net.sf.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/ssh")
// 访问路径http://localhost:8080/Spring_SpringMVC_Hibernate_SpringData/ssh/create?age=23
public class SSHController {
	@Resource
	private SSHService sshService;

	// 调取任何方法之前都会进行的
	// @ModelAttribute()
	public void test(@RequestParam String name) {
		System.out.println(name);
	}

	// 增加

	// @RequestMapping(value = "/query", method = RequestMethod.GET)
	@RequestMapping("/create")
	@ResponseBody
	public List<Customer> createOrder(@RequestParam("age") int age) {

		Customer customer = new Customer();

		customer.setCustomerAge(age);
		customer.setCustomerName("Helllo");
		sshService.createCustomerByCusual(customer);

		List<Customer> list = new ArrayList<Customer>();

		list.add(customer);

		return list;

	}

	// 查询--使用hibernate中hql语句查询
	@RequestMapping(value = "/query", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject queryOrder() {
		// 这里返回为json,不然有的查询结果不能转换为json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("SOURCE", sshService.queryCustomer());
		return jsonObject;

	}

	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String index() {
		System.out.println("hello");
		// 因为controller前有个namespace所以返回的时候也要加namespace
		return "index";
	}

	@RequestMapping(value = "upload", method = RequestMethod.POST)
	public String upload(HttpServletRequest request, @RequestParam CommonsMultipartFile file) {
		try {
			System.out.println(file.getOriginalFilename() + "..." + file.getSize());
			file.transferTo(new File("C:\\Users\\l\\Desktop\\1", file.getName()));
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		request.setAttribute("success", "success");
		return null;
	}

	/*
	 * 测试
	 */
	@RequestMapping(value = "test", method = RequestMethod.GET)
	public String query() {

		System.out.println("into.....");
		return "suuccess.jsp";

	}

	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("SSH-servlet.xml");
		SSHDao sshDao = applicationContext.getBean(SSHDao.class);

		Customer customer = new Customer();

		customer.setCustomerAge(23);
		customer.setCustomerName("Helllo");
		sshDao.createCustomerByCusual(customer);

	}
}
