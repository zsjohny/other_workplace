package com.ouliao.controller.versionfirst;// package com.ouliao.controller;
//
// import java.util.Date;
// import java.util.Properties;
//
// import javax.servlet.http.HttpServletResponse;
//
// import org.apache.commons.lang.StringUtils;
// import org.apache.http.HttpEntity;
// import org.apache.http.HttpResponse;
// import org.apache.http.client.HttpClient;
// import org.apache.http.client.methods.HttpPost;
// import org.apache.http.entity.StringEntity;
// import org.apache.http.impl.client.HttpClients;
// import org.apache.http.util.EntityUtils;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.ResponseBody;
//
// import com.ouliao.domain.versionfirst.HuanXin;
// import com.ouliao.domain.versionfirst.User;
// import com.ouliao.domain.versionfirst.UserBlackList;
// import com.ouliao.service.versionfirst.HuanXinService;
// import com.ouliao.service.versionfirst.OuLiaoService;
// import com.ouliao.service.versionfirst.UserBlackListService;
//
// import net.sf.json.JSONArray;
// import net.sf.json.JSONObject;
//
// @Controller
// @RequestMapping(value = "user/huaxin")
// public class HuanXinInterfacter1 {
// @Autowired
// private HuanXinService huanXinService;
//
// @Autowired
// private OuLiaoService ouLiaoService;
// @Autowired
// private UserBlackListService userBlackListService;
//
// @ResponseBody
// @RequestMapping(value = "loadAccount/{id}/{sendId}")
// public synchronized JSONObject loadAccount(@PathVariable("id") Integer ids,
// @PathVariable("sendId") Integer sendId,
// HttpServletResponse response) {
//
// // HXname = 15924179757;
// // HeadUrl =
// //
// "http://139.196.40.11:8080/ouliao/user/download/setUser/890/985595/head/download";
// // code = 200;
// // name = 1458131873824;
// // sendHeadUrl =
// //
// "http://139.196.40.11:8080/ouliao/user/download/setUser/190/1458049657620/head/download";
// // sendName = 1458132551495;
// // }
//
// JSONObject jsonObject = new JSONObject();
// try {
// // 用户是否存在
// if (ouLiaoService.queryUserByUserId(ids) == null ||
// ouLiaoService.queryUserByUserId(sendId) == null) {
// jsonObject.put("code", 203);
// response.setStatus(HttpServletResponse.SC_OK);
// return jsonObject;
// }
//
// // 是否在黑名单---互相查询
// UserBlackList userBlackList =
// userBlackListService.queryUserIsBlackListById(ids, sendId);
// if (userBlackList != null && "0".equals(userBlackList.getIsDeleted())) {
// jsonObject.put("code", 217);
// response.setStatus(HttpServletResponse.SC_OK);
// return jsonObject;
// }
// userBlackList = userBlackListService.queryUserIsBlackListById(sendId, ids);
// if (userBlackList != null && "0".equals(userBlackList.getIsDeleted())) {
// jsonObject.put("code", 218);
// response.setStatus(HttpServletResponse.SC_OK);
// return jsonObject;
// }
//
// // 加载配置
// Properties properties = new Properties();
// properties.load(OuLiaoSayController.class.getClassLoader().getResourceAsStream("paramsSet.properties"));
//
// HuanXin huanXin = huanXinService.queryIsExist(ids);
// HuanXin huanXinSend = huanXinService.queryIsExist(sendId);
// if (huanXin != null && huanXinSend != null) {
// User user = ouLiaoService.queryUserByUserId(ids);
// jsonObject.put("name", huanXin.getHuaXinName());
//
// if (StringUtils.isEmpty(user.getUserNickName())) {
// jsonObject.put("HXname", "");
// } else if (user.getUserNickName()
// .matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {
// jsonObject.put("HXname", user.getUserNickName().substring(0, 3) + "******"
// + new StringBuilder(user.getUserNickName()).reverse().substring(0, 2));
// } else {
// jsonObject.put("HXname", user.getUserNickName());
// }
//
// String headUrl = user.getUserHeadPic();
// if (StringUtils.isEmpty(headUrl)) {
// headUrl = "985595";
// } else {
// headUrl = headUrl.split("\\.")[0];
// }
//
// jsonObject.put("HeadUrl",
// properties.getProperty("headUrl") + user.getUserId() + "/" + headUrl +
// "/head/download");
//
// jsonObject.put("sendName", huanXinSend.getHuaXinName());
// user = ouLiaoService.queryUserByUserId(sendId);
// if (StringUtils.isEmpty(user.getUserNickName())) {
//
// jsonObject.put("sendHXName", "");
// } else if (user.getUserNickName()
// .matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {
// jsonObject.put("sendHXName", user.getUserNickName().substring(0, 3) +
// "******"
// + new StringBuilder(user.getUserNickName()).reverse().substring(0, 2));
// } else {
// jsonObject.put("sendHXName", user.getUserNickName());
// }
//
// headUrl = user.getUserHeadPic();
// if (StringUtils.isEmpty(headUrl)) {
// headUrl = "985595";
// } else {
// headUrl = headUrl.split("\\.")[0];
// }
//
// jsonObject.put("sendHeadUrl",
// properties.getProperty("headUrl") + user.getUserId() + "/" + headUrl +
// "/head/download");
//
// } else if (huanXin == null && huanXinSend != null) {
//
// HttpClient httpClient = HttpClients.createDefault();
// HttpPost post = new
// HttpPost("https://a1.easemob.com/szkj2016/ouliao/users");
//
// String name = System.currentTimeMillis() + "";
// String pass = "xiaoluo";
// JSONObject json = new JSONObject();
// json.put("username", name);
// json.put("password", pass);
//
// StringEntity entity = new StringEntity(json.toString());
// entity.setContentEncoding("utf-8");
// entity.setContentType("application/json");
// post.setEntity(entity);
//
// HttpResponse responsea = httpClient.execute(post);
// HttpEntity entitys = responsea.getEntity();
// String data = EntityUtils.toString(entitys);
// json = JSONObject.fromObject(data);
// JSONArray array = JSONArray.fromObject(json.get("entities"));
//
// json = JSONObject.fromObject(array.get(0));
// String id = (String) json.get("uuid");
// if (StringUtils.isNotEmpty(id)) {
// HuanXin huanXinPost = new HuanXin();
// huanXinPost.setCreatTime(new Date());
// huanXinPost.setHuaXinUUid(id);
// huanXinPost.setHuaXinName(name);
// huanXinPost.setOwnerId(ids);
// huanXinPost.setPass(pass);
// huanXinService.saveHuanXin(huanXinPost);
// }
//
// User user = ouLiaoService.queryUserByUserId(ids);
// jsonObject.put("name", name);
// if (StringUtils.isEmpty(user.getUserNickName())) {
// jsonObject.put("HXname", "");
// } else if (user.getUserNickName()
// .matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {
// jsonObject.put("HXname", user.getUserNickName().substring(0, 3) + "******"
// + new StringBuilder(user.getUserNickName()).reverse().substring(0, 2));
// } else {
// jsonObject.put("HXname", user.getUserNickName());
// }
//
// String headUrl = user.getUserHeadPic();
// if (StringUtils.isEmpty(headUrl)) {
// headUrl = "985595";
// } else {
// headUrl = headUrl.split("\\.")[0];
// }
//
// jsonObject.put("HeadUrl",
// properties.getProperty("headUrl") + user.getUserId() + "/" + headUrl +
// "/head/download");
//
// jsonObject.put("sendName", huanXinSend.getHuaXinName());
// user = ouLiaoService.queryUserByUserId(sendId);
// if (StringUtils.isEmpty(user.getUserNickName())) {
//
// jsonObject.put("sendHXName", "");
// } else if (user.getUserNickName()
// .matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {
// jsonObject.put("sendHXName", user.getUserNickName().substring(0, 3) +
// "******"
// + new StringBuilder(user.getUserNickName()).reverse().substring(0, 2));
// } else {
// jsonObject.put("sendHXName", user.getUserNickName());
// }
//
// headUrl = user.getUserHeadPic();
// if (StringUtils.isEmpty(headUrl)) {
// headUrl = "985595";
// } else {
// headUrl = headUrl.split("\\.")[0];
// }
//
// jsonObject.put("sendHeadUrl",
// properties.getProperty("headUrl") + user.getUserId() + "/" + headUrl +
// "/head/download");
//
// } else if (huanXin != null && huanXinSend == null) {
//
// HttpClient httpClient = HttpClients.createDefault();
// HttpPost post = new
// HttpPost("https://a1.easemob.com/szkj2016/ouliao/users");
//
// String name = System.currentTimeMillis() + "";
// String pass = "xiaoluo";
// JSONObject json = new JSONObject();
// json.put("username", name);
// json.put("password", pass);
//
// StringEntity entity = new StringEntity(json.toString());
// entity.setContentEncoding("utf-8");
// entity.setContentType("application/json");
// post.setEntity(entity);
//
// HttpResponse responsea = httpClient.execute(post);
// HttpEntity entitys = responsea.getEntity();
// String data = EntityUtils.toString(entitys);
// json = JSONObject.fromObject(data);
// JSONArray array = JSONArray.fromObject(json.get("entities"));
//
// json = JSONObject.fromObject(array.get(0));
// String id = (String) json.get("uuid");
// if (StringUtils.isNotEmpty(id)) {
// HuanXin huanXinPost = new HuanXin();
// huanXinPost.setCreatTime(new Date());
// huanXinPost.setHuaXinUUid(id);
// huanXinPost.setHuaXinName(name);
// huanXinPost.setOwnerId(sendId);
// huanXinPost.setPass(pass);
// huanXinService.saveHuanXin(huanXinPost);
// }
//
// User user = ouLiaoService.queryUserByUserId(ids);
// jsonObject.put("name", huanXin.getHuaXinName());
// if (StringUtils.isEmpty(user.getUserNickName())) {
// jsonObject.put("HXname", "");
// } else if (user.getUserNickName()
// .matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {
// jsonObject.put("HXname", user.getUserNickName().substring(0, 3) + "******"
// + new StringBuilder(user.getUserNickName()).reverse().substring(0, 2));
// } else {
// jsonObject.put("HXname", user.getUserNickName());
// }
//
// String headUrl = user.getUserHeadPic();
// if (StringUtils.isEmpty(headUrl)) {
// headUrl = "985595";
// } else {
// headUrl = headUrl.split("\\.")[0];
// }
//
// jsonObject.put("HeadUrl",
// properties.getProperty("headUrl") + user.getUserId() + "/" + headUrl +
// "/head/download");
//
// jsonObject.put("sendName", name);
// user = ouLiaoService.queryUserByUserId(sendId);
// jsonObject.put("sendHXName", user.getUserNickName());
//
// headUrl = user.getUserHeadPic();
// if (StringUtils.isEmpty(headUrl)) {
// headUrl = "985595";
// } else {
// headUrl = headUrl.split("\\.")[0];
// }
//
// jsonObject.put("sendHeadUrl",
// properties.getProperty("headUrl") + user.getUserId() + "/" + headUrl +
// "/head/download");
//
// } else {
// HttpClient httpClient = HttpClients.createDefault();
// HttpPost post = new
// HttpPost("https://a1.easemob.com/szkj2016/ouliao/users");
//
// String name = System.currentTimeMillis() + "";
// String pass = "xiaoluo";
// JSONObject json = new JSONObject();
// json.put("username", name);
// json.put("password", pass);
//
// StringEntity entity = new StringEntity(json.toString());
// entity.setContentEncoding("utf-8");
// entity.setContentType("application/json");
// post.setEntity(entity);
//
// HttpResponse responsea = httpClient.execute(post);
// HttpEntity entitys = responsea.getEntity();
// String data = EntityUtils.toString(entitys);
// json = JSONObject.fromObject(data);
// JSONArray array = JSONArray.fromObject(json.get("entities"));
//
// json = JSONObject.fromObject(array.get(0));
// String id = (String) json.get("uuid");
// if (StringUtils.isNotEmpty(id)) {
// HuanXin huanXinPost = new HuanXin();
// huanXinPost.setCreatTime(new Date());
// huanXinPost.setHuaXinUUid(id);
// huanXinPost.setHuaXinName(name);
// huanXinPost.setOwnerId(ids);
// huanXinPost.setPass(pass);
// huanXinService.saveHuanXin(huanXinPost);
// }
//
// User user = ouLiaoService.queryUserByUserId(ids);
// jsonObject.put("name", name);
// if (StringUtils.isEmpty(user.getUserNickName())) {
// jsonObject.put("HXname", "");
// } else if (user.getUserNickName()
// .matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {
// jsonObject.put("HXname", user.getUserNickName().substring(0, 3) + "******"
// + new StringBuilder(user.getUserNickName()).reverse().substring(0, 2));
// } else {
// jsonObject.put("HXname", user.getUserNickName());
// }
//
// String headUrl = user.getUserHeadPic();
// if (StringUtils.isEmpty(headUrl)) {
// headUrl = "985595";
// } else {
// headUrl = headUrl.split("\\.")[0];
// }
//
// jsonObject.put("HeadUrl",
// properties.getProperty("headUrl") + user.getUserId() + "/" + headUrl +
// "/head/download");
//
// // 主播用户id
// httpClient = HttpClients.createDefault();
// post = new HttpPost("https://a1.easemob.com/szkj2016/ouliao/users");
//
// name = System.currentTimeMillis() + "";
// pass = "xiaoluo";
// json = new JSONObject();
// json.put("username", name);
// json.put("password", pass);
//
// entity = new StringEntity(json.toString());
// entity.setContentEncoding("utf-8");
// entity.setContentType("application/json");
// post.setEntity(entity);
//
// responsea = httpClient.execute(post);
// entitys = responsea.getEntity();
// data = EntityUtils.toString(entitys);
// json = JSONObject.fromObject(data);
// array = JSONArray.fromObject(json.get("entities"));
//
// json = JSONObject.fromObject(array.get(0));
// id = (String) json.get("uuid");
// if (StringUtils.isNotEmpty(id)) {
// HuanXin huanXinPost = new HuanXin();
// huanXinPost.setCreatTime(new Date());
// huanXinPost.setHuaXinUUid(id);
// huanXinPost.setHuaXinName(name);
// huanXinPost.setOwnerId(sendId);
// huanXinPost.setPass(pass);
// huanXinService.saveHuanXin(huanXinPost);
// }
//
// jsonObject.put("sendName", name);
// user = ouLiaoService.queryUserByUserId(sendId);
// if (StringUtils.isEmpty(user.getUserNickName())) {
//
// jsonObject.put("sendHXName", "");
// } else if (user.getUserNickName()
// .matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {
// jsonObject.put("sendHXName", user.getUserNickName().substring(0, 3) +
// "******"
// + new StringBuilder(user.getUserNickName()).reverse().substring(0, 2));
// } else {
// jsonObject.put("sendHXName", user.getUserNickName());
// }
//
// headUrl = user.getUserHeadPic();
// if (StringUtils.isEmpty(headUrl)) {
// headUrl = "985595";
// } else {
// headUrl = headUrl.split("\\.")[0];
// }
//
// jsonObject.put("sendHeadUrl",
// properties.getProperty("headUrl") + user.getUserId() + "/" + headUrl +
// "/head/download");
//
// }
// System.out.println(jsonObject.toString() + "-------------------");
// jsonObject.put("code", 200);
//
// response.setStatus(HttpServletResponse.SC_OK);
// } catch (Exception e) {
// jsonObject.put("code", 201);
// response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//
// }
// return jsonObject;
//
// }
//
// @ResponseBody
// @RequestMapping(value = "queryClient/{name}")
// public JSONObject queryClient(@PathVariable("name") String name,
// HttpServletResponse response) {
//
// JSONObject jsonObject = new JSONObject();
// try {
// // 用户是否存在
//
// // 加载配置
// Properties properties = new Properties();
// properties.load(OuLiaoSayController.class.getClassLoader().getResourceAsStream("paramsSet.properties"));
//
// HuanXin huanXin = huanXinService.queryHuanXinByName(name);
//
// if (huanXin != null) {
//
// User user = ouLiaoService.queryUserByUserId(huanXin.getOwnerId());
// if (StringUtils.isEmpty(user.getUserNickName())) {
// jsonObject.put("name", "");
// } else if (user.getUserNickName()
// .matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {
// jsonObject.put("name", user.getUserNickName().substring(0, 3) + "******"
// + new StringBuilder(user.getUserNickName()).reverse().substring(0, 2));
// } else {
// jsonObject.put("name", user.getUserNickName());
// }
//
// String headUrl = user.getUserHeadPic();
// if (StringUtils.isEmpty(headUrl)) {
// headUrl = "985595";
// } else {
// headUrl = headUrl.split("\\.")[0];
// }
// jsonObject.put("author", StringUtils.isEmpty(user.getUserAuth()) ? "" :
// user.getUserAuth());
// jsonObject.put("id", user.getUserId());
//
// jsonObject.put("url",
// properties.getProperty("headUrl") + user.getUserId() + "/" + headUrl +
// "/head/download");
//
// jsonObject.put("code", 200);
// } else {
// jsonObject.put("code", 210);
// }
// response.setStatus(HttpServletResponse.SC_OK);
// } catch (Exception e) {
// jsonObject.put("code", 201);
// response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//
// }
// return jsonObject;
//
// }
//
// }
