package com.yujj.entity.comment;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jiuyuan.entity.newentity.alipay.direct.UtilDate;

public class Comment implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5683289316850283669L;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public long getSkuId() {
		return skuId;
	}
	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}	
	public long getBrandId() {
		return brandId;
	}
	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}	
	public int getLiker() {
		return liker;
	}
	public void setLiker(int liker) {
		this.liker = liker;
	}
	public String getContent() {
		if (content == null)
			return null;
		
		// 显示屏蔽
    	String patternString = "赌博机|卖血|出售肾|出售器官|眼角膜|求肾|换肾|有偿肾|预测答案|考前预测|押题|代写论文|司考答案|带考|待考|提供答案|包过|考后付款|考题|顺利过|级答案|传送答案|替考|助考|保过|六级|考中答案|无线耳机|语音传送|考试作弊|考前密卷|四六级答案|漏题|短信答案|轻松过|四大舰队|8023部队|防区和任务|编成与基地|空军部署|陆军部署|海军部署|七个军区|七大军区|7个军区|7大军区|不办人事|圣战组织|劣等民族|劣等人|北大校园BBS|回汉冲突|民族冲突|鞭满|关塔摩|民族问题|伊斯兰|惨案|红客联盟|靖国|招收|雇佣|围攻|参拜|文革|中特|一肖|报码|合彩|香港彩|彩宝|3D轮盘|liuhecai|一码|盗号|盗取密码|盗取qq|买真枪|嗑药|手机监听|帮招人|社会混|拜大哥|电警棒|枪支|帮人怀孕|打手|征兵计划|切腹|杀手|自杀|电鸡|仿真手枪|证件|做炸弹|买枪|卖枪|氯胺酮|ONS|走私车|代孕|史久武|刘亚洲|李登辉|江猪|耀邦|马英九|胡锦|邓矮子|xiaoping|邓狗|曾培炎|俞正声|张德江|张立昌|吴仪|刘淇|回良玉|王兆国|王乐泉|徐才厚|王刚|贺国强|周永康|刘云山|罗干|曾庆红|贾庆林|吴邦国|活体取肾|苏家屯|九抨|无网界|车仑大法|李红痔|XX功|法仑功|FL大法|zifen|李大轮子|jiuping|影子政府|张宏堡|张宏宝|中功|李晓英|轮功|董元辰|大妓院|法0功|法O功|9坪|法伦|法L|fa轮|9评|九凭|LHZ|自焚|藏字石|李洪X|9ping|九ping|自fen|法X功|轮子功|FL功|flg|法某功|东方闪电|38集团军|吾爾開希|64时期|TAM|紫阳|北京风波|政治风波|xuechao|八九六四|8964|liusi|89风波|4事件|6四|四事件|六4|二十四事件|历史的伤口|年的事|1989年|一九八九年|年事件|学生领袖|学潮|杀害学生|八九年|tiananmen|流血事件|民主女神|89之|89事件|17年前|血染的风采|广场事件|89年|屠杀学生|暴行|何新|天安门事件|六四信息|官匪一家|警匪一家|暴力执法|示wei|袁伟民|9.12事件|8.18事件|瑞安事件|武装镇压|武力镇压|暴力袭警|集体抗议|警民冲突|群众冲击|复员|民主|股民|造反|信访|军转|聚集|买断|罢市|维权|上访|示威|陪聊|h图|h漫|开苞|找男|找女|口淫|女士服务|卖身|元一夜|男奴|女奴|电话交友|激情交友|激情电话|看JJ|幼交|性伙伴|有偿服务|做台|厕奴|骚女|嫩逼|一夜激情|母子乱伦|泡友|富姐|富婆|足交|群交|阴户|乳交|屁眼|性服务|性伴侣|反社会主义|现在的公安|第二首都|中国复兴党|共贼|联盟党|同盟党|言论自由|专政|一党专政|一党私利|软弱外交|政府无能|一党执政|讨厌中国|张志新|现在的党|外蒙回归|香港独立|上海独立|北京独立|民主还专政|人权保护|蒋公|中正纪念歌|中国人权|台独|台du|国统纲领|国统会|网络警察|纪念文革|共独|陆独|现在的社会|上海帮|现在的政府|反对党|现在的共产党|铁血师|现在的警察|共产觉|灭共|社会主义灭亡|不爱党|共X党|独夫民贼|伪大|伪政府|第三次世界大战|自由亚洲|公产党|夺权|网特|GONG党|CCP|中共洗脑|反G|死刑现场|雍战胜|校园改造工程|记签|王政|华闻|张文中|饲养基地|涿州|集会|中组部前部长直言|广安事件|四川广安|段桂清|寂寞少妇|有偿捐献|6合|六合|六HE彩|偷拍|有码|无码|包养|海洛因|犬交|兽交|根浴|买肾|卖肾|肾源|小电影|援交|性息|裸聊|夫妻交换|广安|中石油国家电网倒数|国办发|中办发|菊暴|菊爆|暴菊|爆菊|小口径|性虐|性爱|刻章|我虽死去|我们自杀吧|胡锦涛|共产党|吃人|GCD|西藏作家组织|投毒杀人|自杀指南|盘古乐队|肉棍|自由西藏|自由门|自烧|自杀手册|转法论|转法轮|属灵教|主神教|重阳兵变|中央黑幕|中南海权力斗争|中南海黑幕|中南海恩仇录|中南海斗争|中南海的权力游戏|中华养生益智气|中华养生益智功|中华昆仑女神功|中国猪|中国支配下的朝鲜经济|中国政府封锁消息|中国正义党|中国舆论监督网周洪|中国网络审查|中国贪官在海外|中国实行血腥教育|中国人民党|中国没有自由|中国海外腐败兵团|中国过渡政府|中国官场情杀案|中国共和党|中国高层权力斗争|中共政治游戏|中共邪毒素|中共邪党|中共退党|中共三大势力谁来执政|中共任用|中共权力斗争|中共恐惧|中共近期权力斗争|中共解体|中共黑帮|中共腐败|中共封网|中共封锁|中共独裁|中共第五代|中共第六代|中共帝国|中共的罪恶|中共的血旗|中共的腐败与残暴|中共党文化|中共裁|中共保命|中共帮凶|支那猪|支那|支持台湾独立|政治人祸的源头|政治局委员增加一倍|政治局十七|真善忍|运动正名|援藏网|元极功|渊盖苏文|宇宙主佛|宇宙毁灭|宇宙大法|优化官员|婴儿汤|一中一台|一通健康法|一通功|一党独裁|夜半加税|业力轮|业力回报|耶稣基督血水圣灵全备福音布道团|燕玲论坛|延安日记|循环轮回论|血腥清场|血色京畿|血色京机|血溅人民天堂|性奴集中营|兴华论谈|新中华战记|新闻总署态度蛮横|新搪人|新唐人|新疆独立|新疆暴乱|小活佛|消业之说|向巴平措|香功|西藏人民大起义运动|西藏独立|无人性的政权|无官正|无耻语录|无帮国|乌云其木格|瘟家宝|温休曾退|温下台|温加饱|魏京生|未来的自由中国在民间|伪基百科|为党不为国|网络封锁|网管办|亡共者胡|亡党|万法归一功|退h集会|推翻专制独裁的共产党|突破网封索的软件|统治术|统一教|挺胡|天葬|天音功|天要亡|天要灭|天灭中共|特务机构|特别党费|太王四神记|台湾有权独立|台湾应该独立|台湾国|台湾独立|四二六社论|四川朱昱|四川大地震异象揭密|司马璐回忆录|税务总局致歉|谁为腐败晚餐买单|谁是新中国|谁是胡的接班人|双桨飞机|树亲民形象不如推动政改|世界之门|世界以利亚福音宣教会|世界十大独裁者|世界基督教统一神灵协会|实际神|十七位老部长|十七大人事安排|十七大权力争霸战|十大独裁|十八大接班人|师涛|剩火|圣灵重建教会|圣火护卫|圣殿教|生命树的分叉|沈昌人体科技|沈昌功|神州忏悔录|神的教会|涉台政局|沙皇李长春|三脱|三退|三三九乘元功|三股势力|三二二攻台作战计划|三二二攻台作战|三班仆人派|赛克网|萨斯病|日月气功|人宇特能功|人民币恶搞|人类罪恶论|群体灭绝|全范围教会|清华帮|清海无上师|清海师父|清官团|清场内幕|青海无上师|沁园春血|钦点接班人|侵犯国外专利|亲共媒体|亲共分子|亲共|强烈抗议中共当局|千禧弘法|器官贩卖|平反六四|跑官要官|派系斗争|女神教|鸟巢最少死|内争人权|牟新生|漠视生命自私到了极点的中共政权|末世论|末世劫难|魔难论|明镜出版社|明慧网|明hui|民主不能等待|民运|民殇|灭中共|迷失北京|蒙古回归|门徒会|毛主席的嫡孙|毛贼|毛泽东侄子|毛泽东复活|毛时代大饥荒揭秘|买官卖官|轮子小报|旅游新报|龙虎斗|六四资料馆|六四受难者家属证辞|六四内部日记|六合彩|六代接班人|留四进三|流亡藏人|领导忽悠百姓叫号召|临震预报|练功群众|李伟信的笔供|李洪志|拉线飞机|拉萨僧人接连抗议|拉票贿选|拉帮游说|昆仑女神功|恐共|空中民主墙|抗议中共当局|抗议磁悬浮|开天目|卡辛纳大道和三福大道交界处|救度众生说|京夫子|津人治津|解体中共|解体的命运|讲法传功|疆独|江贼民|江贼|江宰民|江系人马|江戏子|江梳头|江氏政治委员|江氏政治局|江氏家族|江氏集团|江三条腿|江人马|江泉集团|江派人马|江派和胡派|江派|江绵恒|江驴|江家帮|江祸心|江黑心|江核心|江蛤蟆|江独裁|江毒|江嫡系|江丑闻|江z民|监狱里的斗争|监狱管理局|妓女的口号|纪念达赖喇嘛流亡49周年|疾病业债说|激流中国|基督灵恩布道团|积克馆|活摘器官|回忆六四|回民猪|回民暴动|黄菊遗孀|话在肉身显现|华藏功|护卫团|胡下台|胡温|胡派人马|胡派军委|胡派|胡进涛|胡紧套|胡紧掏|胡江争斗|胡江曾|胡江关系|胡江风云|胡江|胡的接班人|红病历|哈狗帮|观音法门|共字玄机|共青团派|共青背景|共和国2049|共匪|共党|共产主义黑皮书|共产党专制|共产党的末日|共产党的报应|个邪的党|个人圆满说|蛤蟆转世|佛怀煽仇录|芳香型智悟气功|反中共黑色暴力|反共言论|反共传单|反攻大陆|反腐总攻|反对日本介入中国铁路工业|反对共产主义|反对共产党|反党|珐(工力)学|法西藏主义|法能功|法论功|法轮义|法轮功|法轮公|法轮工|法轮佛法|法轮大法|法轮|法力像佛|法拉盛缅街|法拉盛|法功|发轮功陈果|发轮功|发伦工|发仑da发|恶警|恶搞人民币|恶党|对中共的姑息就是对死难者的残忍|对共产党清算|东伊运|东突|第五代接班梯队|第五代红人|递进民主|抵制北京奥运|邓颖超日记|党内言事潮|党内危机|党内权争|党内权力|党禁|党棍|党风日下|大圆满法|大学暴动|大法修炼者|大法弟子|打砸抢|达米宣教会|慈悲功|传九退三|赤匪|成佛做主|禅密功|藏独|踩江|北美巡回讲法|北京政坛清华名人|北京市委黑幕|北京黑幕|北京帮|爱国者同盟网站|爱国运动正名|www.cipfg.org|wenjiabao|VOA采访热比娅|tuidang|Seven Years in Tibet|Red Corner|Power to the Falun Gong|m功学|minghui|lihongzhi|huzhuxi|http://www.ZhUiChaGuOji.org/cn/|global human rights torch relay|Fa轮功|fanhua|fangong|falungong|falundafa|FaLun|bjork|89学潮血腥屠杀|89学潮大屠杀|70天大事记|64大屠杀|3退|322攻台作战计划|《中国民主》|《争鸣》|《动向》|脏独|脏毒|西藏国家民主党|反共|藏青会|自由西藏学生运动|九十三运动|藏妇会|摇头丸|出售假币|监听王|昏药|侦探设备|升达毕业证|手机复制|佳静安定片|蒙汗药粉|古方迷香|强效失忆药|迷奸药|透视眼睛|子女任职名|催情药|拍肩神|身份证生成器|三唑仑|麻醉乙醚|最后圆满|作弊器|治疗肿瘤|治疗乙肝|治疗性病|治疗红斑狼疮|针孔摄像机|债务公司|游戏机破解|隐形摄像机|隐形耳机|隐蔽式摄像机|银行卡复制设备|一卡多号|遥控信号拦截器|信用卡套现|消防枪|消防灭火枪|香港生子|土炮|胎盘|手机窃听器|手机魔卡|手机卡复制器|手机监听器|手机跟踪定位器|容弹量|窃听器|枪模|铅弹|汽枪|汽狗|汽车走表器|气枪|气狗|普提功|女伟哥|纽扣摄像机|免电灯|迷药|迷魂药|迷昏药|迷幻药|蒙汗药|卖QQ号码|麻醉药|麻醉枪|雷管|康生丹|警徽|假证件|假文凭|假发票|假币出售|加油机干扰器|记号扑克|激光汽|激光气|红床|狗友|跟帖器|干扰器|仿真枪|防身枪|反雷达测速|短信投票业务|顶帖器|电子狗导航手机|电击枪|电话追杀系统|地磅遥控器|地磅解码器|弹种|弹夹|代追债|代讨债|春药|车用电子狗|避孕膜|办理证件|办理文凭|斑蝥|暗访包|SIM卡复制器|BB枪|BB弹|追债公司|消声器|霰弹|卫星广播器材|卫星高频头|卫星电视|土枪|双筒|双桶|手枪|手榴弹|人弹|麻醉弹|猎枪|弓弩|赴港生子|电狗|导爆索|长狗|爆炸物|爆破|左棍|生孩子没屁眼|傻逼|日你妈|妈了个逼|贱人|贱货|狗日的|狗娘养的|粪青|操你妈|婊子|换妻|成人片|激情小电影|黄色小电影|淫靡|淫水|淫兽|阴毛|阴蒂|阴道|阴唇|性交|小穴|缩阴|少妇自拍|色情片|色情电影|三级片|三级电影|肉棒|情杀|免费电影|裸照|奸杀|乱伦|口交|禁网禁片|激情视频|激情电影|黄色电影|肛交|春宫图|SM用品|自动群发|证件公司|网站推广软件|网络推广扩散器|私家侦探服务|生意宝|商务快车|商务短信|群发软件|群发广告机|解密软件|慧聪|供应发票|发贴机|发贴工具|发票代开|短信群发|短信猫|顶贴机|点金商务|代开发票|特码|士的宁|士的年|六合采|乐透码|皇家轮盘|俄罗斯轮盘|赌具|彩票预测|彩票选号机|彩票|包青天机|百乐二呓|百家乐|阿凡提机|6合彩|祖母绿|重亚硒酸钠|粘氯酸|原砷酸|原藜芦碱A|永伏虫|蝇毒|罂粟籽|罂粟壳|银氰化钾|因毒硫磷|因毒磷|异氰酸甲酯|异氰酸苯酯|异硫氰酸烯丙酯|乙酰亚砷酸铜|乙酰替硫脲|乙烯甲醇|乙酸亚铊|乙酸铊|乙酸三乙基锡|乙酸三甲基锡|乙酸甲氧基乙基汞|乙酸汞|乙硼烷|乙醇腈|乙撑亚胺|乙撑氯醇|伊皮恩|一氧化汞|一氧化二氟|一氯乙醛|一氯丙酮|氧氯化磷|氧化亚铊|氧化铊|氧化汞|氧化二丁基锡|烟碱|亚硝酰乙氧|亚硝酸乙酯|亚硒酸氢钠|亚硒酸钠|亚硒酸镁|亚硒酸二钠|亚硒酸|亚砷酸钠|亚砷酸钾|亚砷（酸）酐|冰毒|性奴|菊花洞";
    	Pattern pattern = Pattern.compile(patternString);
    	Matcher matcher = pattern.matcher(content);
    	
		return matcher.replaceAll("*");
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@JsonIgnore
	public String getEscapeContent() {
		return StringEscapeUtils.escapeHtml4(content);
	}
	@JsonIgnore
	public void setEscapeContent(String content) {
		this.content = StringEscapeUtils.unescapeHtml4(content);
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public int getAnonymity() {
		return anonymity;
	}
	public void setAnonymity(int anonymity) {
		this.anonymity = anonymity;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getCreateTime() {
		return createTime;
	}
	public String getCreateTimeStr() {
		if(createTime <= 0){
			return "";
		}
		return UtilDate.getDateStrFromMillis(createTime, UtilDate.yearMonthDay);
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	public String getUpdateTimeStr() {
		if(updateTime <= 0){
			return "";
		}
		return UtilDate.getDateStrFromMillis(updateTime, UtilDate.yearMonthDay);
	}
	
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	public String getUserNickname() {		
		return StringUtils.left(userNickname, 2) + "***" + StringUtils.right(userNickname, 4);
	}
	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}

	public String getUserIcon() {
		return userIcon;
	}
	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}

	public String getPropertyIds() {
		return propertyIds;
	}
	public void setPropertyIds(String propertyIds) {
		this.propertyIds = propertyIds;
	}

//	public long getOrderId() {
//		return orderId;
//	}
//	public void setOrderId(long orderId) {
//		this.orderId = orderId;
//	}
	

	public long getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}
	
	public String[] getImageUrlArr() {
		try{
			JSONArray array = JSON.parseArray(getImageUrl().replaceAll("\\[\\{\"url\":", "\\[").replaceAll("}]", "]"));
			if (array == null) {
				return new String[]{};
			}
			return array.toArray(new String[]{});
			
		}catch(Exception e){
			return new String[]{}; 
		}
    }


	private long id;
//	private long orderId;
	private long orderNo;
	private long userId;
	private long productId;
	private long skuId;
	private long brandId;
	private int liker;
	private String content;
	private String imageUrl;
	private int anonymity;
	private int status;
	private long createTime;
	private long updateTime;
	
	private String propertyIds;
    private String userNickname;
    private String userIcon;	
}
