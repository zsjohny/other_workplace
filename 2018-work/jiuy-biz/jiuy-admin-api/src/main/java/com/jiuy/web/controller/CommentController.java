package com.jiuy.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuy.core.meta.comment.Comment;
import com.jiuy.core.service.comment.CommentService;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

@Controller
@RequestMapping("/comment")
@Login
public class CommentController {
	
	@Autowired
	private CommentService commentService;

	@RequestMapping("/search")
	@ResponseBody
	public JsonResponse search(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "page_size", required = false, defaultValue = "10") int pageSize,
			@RequestParam(value = "checked", required = false, defaultValue = "-1") int checked) {
		JsonResponse jsonResponse = new JsonResponse();
		PageQuery pageQuery = new PageQuery(page, pageSize);
		PageQueryResult pageQueryResult = new PageQueryResult();
		BeanUtils.copyProperties(pageQuery, pageQueryResult);

		Comment comment = new Comment();
		List<Comment> comments = commentService.search(pageQuery, comment);
		
		int count = commentService.searchCount(comment);
		pageQueryResult.setRecordCount(count);
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("list", comments);
		data.put("total", pageQueryResult);
		
		return jsonResponse.setSuccessful().setData(data);
	}
}
