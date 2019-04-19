package com.jiuy.base.util;

public class GlobalsFields {


	/** 确认 **/
	public final static Integer YES = 1;

	/** 未确认 **/
	public final static Integer NO = 0;

	/** 缓存字典json **/
	public static String enumsStr = "";

	/** 阿里BUCKET_NAME */
	public final static String BUCKET_NAME = "tobacco-files";

	/******************************** 待办状态 ******************************************************/
	/** 待处理 */
	public final static Integer MANAGE_WAIT = 0;
	/** 已处理 */
	public final static Integer MANAGE_PASS = 1;
	/******************************** 待办类型 ******************************************************/
	/** 题库审核 */
	public final static Integer QUESTION_EXMINE = 1;
	/** 模板审核 */
	public final static Integer EXAMTEMPLET_EXMINE = 2;
	/** 课件审核 */
	public final static Integer COURSE_EXMINE = 3;
	/************************************* 状态代码 ************************************************/
	/** 未提交 */
	public final static Integer EXMINE_NOT_SUB = 0;
	/** 待审核 */
	public final static Integer EXMINE_WAIT = 1;
	/** 审核失败 */
	public final static Integer EXMINE_FAILE = 3;
	/** 审核通过 */
	public final static Integer EXMINE_PASS = 2;

	/** 用户缓存 */
	public final static String SESSION_USER = "_SESSION_USER";

	/** 用户手机号码 */
	public final static String SESSION_USER_PHONE = "_USER_PHONE";

	/** 用户对象 */
	public final static String SESSION_USER_VO = "_USER_VO";
	/** 用户名id的key */
	public final static String SESSION_USER_ID = "_USER_ID";

	/** 用户名的key */
	public final static String SESSION_USER_NAME = "_USER_NAME";

	/** 组织机构编码 */
	public final static String SESSION_ORG_CODE = "_ORG_CODE";

	/** 专卖局编码 */
	public final static String SESSION_UNIT_ORG_CODE = "_UNIT_ORG_CODE";

	/** 组织机构全称 */
	public final static String SESSION_FULL_ORG_NAME = "_FULL_ORG_NAME";

	/** 专卖局名称 */
	public final static String SESSION_UNIT_ORG_NAME = "_UNIT_ORG_NAME";

	/** 角色名称 */
	public final static String SESSION_ROLE_CODE = "_ROLE_CODE";

	/** 菜单 */
	public final static String SESSION_PERSON_MEUNS = "_PERSON_MEUNS";

	/** 组织机构名 */
	public final static String SESSION_ORG_NAME = "_ORG_NAME";

	/** 验证码key */
	public final static String IDENTIFYING_CODE = "identifying_code";

	/** 统一时间格式y */
	public final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/** 统一时间格式y */
	public final static String DATE_FORMAT_EXAMINE = "yyyy-MM-dd HH:mm";

	/** 默认页面尺寸 **/
	public final static Integer DEFAULT_PAGE_SIZE = 20;

	/** 启用 1 **/
	public final static Integer ENABEL = 1;

	/** 禁用 0 **/
	public final static Integer DESENABEL = 0;

	public final static String webPath = "http://localhost:8088";

	public final static String version = "v=1.0";

	/******************************** 人员统计信息 ************************************************/
	/** 考试次数 **/
	public final static String EXAMINE_COUNT = "examineCount";

	/** 学习时长 **/
	public final static String LEARING_TIME_COUNT = "learingTimeCount";

	/** 练习次数 **/
	public final static String TRANING_COUNT = "traningCount";

	/******************************** 组织机构统计信息 ************************************************/
	/** 提交课件总数 */
	public final static String COURSE_COUNT = "courseCount";

	/** 提交课件审核通过数 */
	public final static String COURSE_COUNT_PASS = "courseCountPass";

	/** 提交课件总数 */
	public final static String COURSE_COUNT_FAILE = "courseCountFaile";

	/** 提交课件审核失败数 */
	public final static String QUESTION_COUNT = "questionCount";

	/** 提交题目总数 */
	public final static String QUESTION_COUNT_PASS = "questionCountPass";

	/** 提交题目审核失败 */
	public final static String QUESTION_COUNT_FAILE = "questionCountFaile";

	/** 提交模板审核总数 */
	public final static String TEMPLET_COUNT = "templetCount";

	/** 提交模板审核失败 */
	public final static String TEMPLET_COUNT_FAILE = "templetCountFaile";

	/** 提交模板审核通过 */
	public final static String TEMPLET_COUNT_PASS = "templetCountPass";

	/******************************* 题库统计信息 ************************************************/
	/** 被答题次数 */
	public final static String ANSWER_COUNT = "answerCount";
	/** 正确答题次数 */
	public final static String RIGHT_ANSWER_COUNT = "rightAnswerCount";
	/** 错误答题次数 */
	public final static String ERROR_ANSWER_COUNT = "errorAnswerCount";
	/** 练习次数 */
	public final static String QUESTION_TRANING_COUNT = "traningCount";
	/** 考试次数 */
	public final static String EXAMINATION_COUNT = "examinaTionCount";

	/******************************* 课件统计信息 ************************************************/
	/** 点击次数 */
	public final static String FOCUS_COUNT = "focusCount";

	/** 学习次数 */
	public final static String LEARING_COUNT = "learingCount";
	
	/*********************************论坛管理员*************************************************/
	public final static String BBS_ADMIN = "_BBS_ADMIN";
	
	public final static String VALID_RESULT = "_VALID_RESULT";
	
}
