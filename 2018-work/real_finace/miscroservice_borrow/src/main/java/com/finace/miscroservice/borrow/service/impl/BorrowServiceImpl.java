package com.finace.miscroservice.borrow.service.impl;

import com.finace.miscroservice.borrow.dao.BorrowDao;
import com.finace.miscroservice.borrow.dao.FinanceBidDao;
import com.finace.miscroservice.borrow.entity.enums.BorrowTabEnums;
import com.finace.miscroservice.borrow.entity.response.DataCollectionResponse;
import com.finace.miscroservice.borrow.entity.response.UserProportion;
import com.finace.miscroservice.borrow.po.BorrowPO;
import com.finace.miscroservice.borrow.po.FinanceBidPO;
import com.finace.miscroservice.borrow.rpc.UserRpcService;
import com.finace.miscroservice.borrow.service.BorrowService;
import com.finace.miscroservice.commons.entity.User;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Response;
import com.finace.miscroservice.commons.utils.tools.DateUtils;
import com.finace.miscroservice.commons.utils.tools.NumberUtil;
import com.finace.miscroservice.commons.utils.tools.TextUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.*;

import static com.finace.miscroservice.borrow.utils.AppBorrowUtils.clsHtmlImage;
import static com.finace.miscroservice.borrow.utils.AppBorrowUtils.searchHtmlImage;


/**
 * 标的的service 实现层
 */
@Service
public class BorrowServiceImpl implements BorrowService {
    private Log logger = Log.getInstance(BorrowServiceImpl.class);

    @Autowired
    private BorrowDao borrowDao;

    @Autowired
    private FinanceBidDao financeBidDao;

    @Autowired
    private UserRpcService userRpcService;

    @Value("${borrow.evaluation}")
    private String evaluation;  //标的是否需要测评 0--不需要 1--需要
    @Value("${borrow.ninety.start}")
    private String startTime;  //90天标 活动 开始时间
    @Value("${borrow.ninety.end}")
    private String endTime;  //90天标 活动 结束时间
    @Value("${borrow.tab.start}")
    private String tabBorrowStartTime;  //标签活动开始时间
    @Value("${borrow.tab.name}")
    private String tabName;  //标签名称

    @Override
    public BorrowPO getBorrowById(int id) {
        return borrowDao.getBorrowById(id);
    }

    @Override
    public List<BorrowPO> getShowFinaceList(Map<String, Object> map, int page) {
        return borrowDao.getShowFinaceList(map, page);
    }

    @Override
    public BorrowPO getShowFinaceById(int id) {
        return borrowDao.getShowFinaceById(id);
    }

    @Override
    public Map<String, Map<String, Object>> getBorrowByIndex(Integer isxs) {
        Map<String, Map<String, Object>> rmap = new HashMap<>();
        //活动时间 2018-07-02 到 2018-07-10前
        List<BorrowPO> sycp;
        if (!DateUtils.compareDate(startTime, DateUtils.getNowDateStr()) && DateUtils.compareDate(endTime, DateUtils.getNowDateStr())) {
            sycp =  borrowDao.getBorrowNinetyByIndex();
        }else {
            sycp =  borrowDao.getBorrowByIndex("sycp",BorrowTabEnums.TAB_BKJX.getValue());
        }

        logger.info("爆款加息活动时间={}",tabBorrowStartTime);
            List<BorrowPO> tabBorrow =  borrowDao.getBorrowByTab(BorrowTabEnums.TAB_BKJX.getValue());
            if (null!=tabBorrow&&tabBorrow.size()>0){
                Map<String, Object> cpmap1 = new HashMap<>();
                cpmap1.put("name", tabBorrow.get(0).getName());
                cpmap1.put("account", tabBorrow.get(0).getAccount());
                cpmap1.put("accountYes", tabBorrow.get(0).getAccountYes());
                cpmap1.put("apr", tabBorrow.get(0).getApr());
                cpmap1.put("lowestAccount", tabBorrow.get(0).getLowestAccount());
                cpmap1.put("mostAccount", tabBorrow.get(0).getMostAccount());
                cpmap1.put("timeLimitDay", tabBorrow.get(0).getTimeLimitDay());
                cpmap1.put("scales", tabBorrow.get(0).getScales());
                cpmap1.put("use", tabBorrow.get(0).getUse());
                cpmap1.put("addApr", tabBorrow.get(0).getAddApr());
                cpmap1.put("id",tabBorrow.get(0).getId());  //标的id
                cpmap1.put("litpic",tabBorrow.get(0).getLitpic());
                cpmap1.put("status",tabBorrow.get(0).getStatus());
                cpmap1.put("remmoney",tabBorrow.get(0).getRemmoney());
                rmap.put("cp1", cpmap1);
            }else  if( null != sycp && sycp.size() > 0 ){
                Map<String, Object> cpmap1 = new HashMap<>();
                cpmap1.put("name", sycp.get(0).getName());
                cpmap1.put("account", sycp.get(0).getAccount());
                cpmap1.put("accountYes", sycp.get(0).getAccountYes());
                cpmap1.put("apr", sycp.get(0).getApr());
                cpmap1.put("lowestAccount", sycp.get(0).getLowestAccount());
                cpmap1.put("mostAccount", sycp.get(0).getMostAccount());
                cpmap1.put("timeLimitDay", sycp.get(0).getTimeLimitDay());
                cpmap1.put("scales", sycp.get(0).getScales());
                cpmap1.put("use", sycp.get(0).getUse());
                cpmap1.put("addApr", sycp.get(0).getAddApr());
                cpmap1.put("id",sycp.get(0).getId());  //标的id
                cpmap1.put("litpic",sycp.get(0).getLitpic());
                cpmap1.put("status",sycp.get(0).getStatus());
                cpmap1.put("remmoney",sycp.get(0).getRemmoney());
                rmap.put("cp1", cpmap1);
            }


        if( null != sycp && sycp.size() > 1 ){
            Map<String, Object> cpmap2 = new HashMap<>();
            cpmap2.put("name", sycp.get(1).getName());
            cpmap2.put("account", sycp.get(1).getAccount());
            cpmap2.put("accountYes", sycp.get(1).getAccountYes());
            cpmap2.put("apr", sycp.get(1).getApr());
            cpmap2.put("lowestAccount", sycp.get(1).getLowestAccount());
            cpmap2.put("mostAccount", sycp.get(1).getMostAccount());
            cpmap2.put("timeLimitDay", sycp.get(1).getTimeLimitDay());
            cpmap2.put("scales", sycp.get(1).getScales());
            cpmap2.put("use", sycp.get(1).getUse());
            cpmap2.put("addApr", sycp.get(1).getAddApr());
            cpmap2.put("id",sycp.get(1).getId());  //标的id
            cpmap2.put("litpic",sycp.get(1).getLitpic());
            cpmap2.put("status",sycp.get(1).getStatus());
            cpmap2.put("remmoney",sycp.get(1).getRemmoney());
            rmap.put("cp2", cpmap2);
        }

        if( null == isxs || 1 == isxs ){
            List<BorrowPO> xsb = borrowDao.getBorrowByIndex("xsb",BorrowTabEnums.TAB_XSB.getValue());
            if( null != xsb && xsb.size() > 0 ){
                Map<String, Object> xsbmap = new HashMap<>();
                xsbmap.put("name", xsb.get(0).getName());
                xsbmap.put("account", xsb.get(0).getAccount());
                xsbmap.put("accountYes", xsb.get(0).getAccountYes());
                xsbmap.put("apr", xsb.get(0).getApr());
                xsbmap.put("lowestAccount", xsb.get(0).getLowestAccount());
                xsbmap.put("mostAccount", xsb.get(0).getMostAccount());
                xsbmap.put("timeLimitDay", xsb.get(0).getTimeLimitDay());
                xsbmap.put("scales", xsb.get(0).getScales());
                xsbmap.put("use", xsb.get(0).getUse());
                xsbmap.put("addApr", xsb.get(0).getAddApr());
                xsbmap.put("id",xsb.get(0).getId());  //标的id
                xsbmap.put("litpic",xsb.get(0).getLitpic());
                xsbmap.put("status",xsb.get(0).getStatus());
                xsbmap.put("remmoney",xsb.get(0).getRemmoney());
                rmap.put("xsb", xsbmap);
            }
        }else{
            if( null != sycp && sycp.size() > 2 ){
                Map<String, Object> cpmap3 = new HashMap<>();
                cpmap3.put("name", sycp.get(2).getName());
                cpmap3.put("account", sycp.get(2).getAccount());
                cpmap3.put("accountYes", sycp.get(2).getAccountYes());
                cpmap3.put("apr", sycp.get(2).getApr());
                cpmap3.put("lowestAccount", sycp.get(2).getLowestAccount());
                cpmap3.put("mostAccount", sycp.get(2).getMostAccount());
                cpmap3.put("timeLimitDay", sycp.get(2).getTimeLimitDay());
                cpmap3.put("scales", sycp.get(2).getScales());
                cpmap3.put("use", sycp.get(2).getUse());
                cpmap3.put("addApr", sycp.get(2).getAddApr());
                cpmap3.put("id",sycp.get(2).getId());  //标的id
                cpmap3.put("litpic",sycp.get(2).getLitpic());
                cpmap3.put("status",sycp.get(2).getStatus());
                cpmap3.put("remmoney",sycp.get(2).getRemmoney());
                rmap.put("cp3", cpmap3);
            }
        }



        return rmap;
    }
    
	/**
	 * 获取正在售卖的标的
	 * @param borrowGroup
	 * @return
	 */
	public List<BorrowPO> getBorrowPOBySellOut(String borrowGroup){
		return this.borrowDao.getBorrowPOBySellOut(borrowGroup);
	}
	
	/**
	 * 在售标的卖完后查找可以自动审核的标的
	 * @param borrowGroup
	 * @return
	 */
	public BorrowPO getBorrowPOByAutoAdd(String borrowGroup){
		return this.borrowDao.getBorrowPOByAutoAdd(borrowGroup);
	}


    /**
     *
     * @param orderId
     * @return
     */
    public String autoUpBorrow(String orderId){
        FinanceBidPO financeBidPO = financeBidDao.getFidByOrderId(orderId);
        if( null == financeBidPO ){
             return null;
        }

        BorrowPO borrow = borrowDao.getBorrowById(financeBidPO.getBorrowId());
        if( null == borrow ){
            return null;
        }

        List<BorrowPO> bList = this.borrowDao.getBorrowPOBySellOut(borrow.getBorrow_group());
        logger.info("重复上标记录自动上标：分组={}是否有该标的={}", borrow.getBorrow_group(), bList.size());
        if( bList.size() == 0 ){
            BorrowPO borrowPO = this.borrowDao.getBorrowPOByAutoAdd(borrow.getBorrow_group());
            if( null != borrowPO){
                logger.info("重复上标记录自动上标开始：分组={}标的信息id={},名称={} ", borrowPO.getBorrow_group(), borrowPO.getId(), borrowPO.getName());
                borrowPO.setStatus(1);//审核通过
                borrowPO.setVerifyRemark("自动审核通过");
                borrowPO.setVerifyTime(DateUtils.getNowTimeStr());
                this.borrowDao.updateAutoCheckBorrow(borrowPO);
                logger.info("重复上标记录自动上标结束：分组={}标的信息id={},名称={}   ", borrowPO.getBorrow_group(), borrowPO.getId(), borrowPO.getName());
            }
        }

        return "success";
    }


    /**
     *
     * @param id
     * @return
     */
    public Response getBorrowDetail(Integer id){
        try {
            Map<String, Object> map = new HashMap<>();
            BorrowPO borrow = borrowDao.getBorrowById(id);
            if (borrow == null) {
                logger.warn("id={},标的信息不存在", id);
                return Response.errorMsg("标的信息不存在");
            }

            if (id > 10599) { //10599   21626
                map.put("borrowFlag", "newBorrow");  //老的标的
            } else {
                map.put("borrowFlag", "oldBorrow");   //新标的
            }

            map.put("financeCompany", borrow.getFinance_company());  //融资企业
            map.put("ficAccount", borrow.getFicAccount()); //借款额度
            map.put("timeLimitDay", borrow.getTimeLimitDay());  //借款期限
            map.put("loanUsage", borrow.getLoan_usage()); //借款用途
            map.put("payment", borrow.getPayment()); //还款来源
            map.put("use", borrow.getUse());  //标的类型


            List<String> img = new ArrayList<>();
            img.add(borrow.getImgurl1());
            img.add(borrow.getImgurl2());
            img.add(borrow.getImgurl3());
            img.add(borrow.getImgurl4());
            img.add(borrow.getImgurl5());
            img.add(borrow.getImgurl6());
            img.add(borrow.getImgurl7());
            img.add(borrow.getImgurl8());
            map.put("img", img);//项目图片

            List<String> imagestrlist = searchHtmlImage(borrow.getContent());
            map.put("imagestrlist", imagestrlist);
            String noimgContent = clsHtmlImage(borrow.getContent());
            map.put("noimgContent", noimgContent);
            List<String> imagestrlist2 = searchHtmlImage(borrow.getContent2());
            map.put("imagestrlist2", imagestrlist2);
            String noimgContent2 = clsHtmlImage(borrow.getContent2());
            map.put("noimgContent2", noimgContent2);

            map.put("trustLevel", borrow.getTrustLevel() != null || borrow.getTrustLevel() == 0 ? borrow.getTrustLevel() : 3);
            map.put("validTime", borrow.getValidTime() != null ? borrow.getValidTime() : "1");

            User user = userRpcService.getUserByUserId(String.valueOf(borrow.getUserId()));
            if( null != user ){
                map.put("privacy", user.getPrivacy() != null ? TextUtil.hideRealnameChar(user.getPrivacy()) : "");  //法人
                map.put("address", user.getAddress() != null ? TextUtil.hideAddressChar(user.getAddress()) : "");  //地址
                map.put("realname", user.getRealname() != null ?  TextUtil.hideCompanyChar(user.getRealname()) : ""); //公司名称
                map.put("uptime", user.getUptime()); //成立时间
            }else {
                map.put("privacy", "");  //法人
                map.put("address", "");  //地址
                map.put("realname", ""); //公司名称
                map.put("uptime", ""); //成立时间
            }

            logger.info("id={},获取标的详情结束", id);

            map.put("viewType", borrow.getViewType()); // 0--老的项目详情 1--新的项目详情

            return Response.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("id={},获取信息失败,异常信息{}", id, e);
            return Response.errorMsg("获取标的信息失败");
        }

    }

    @Override
    public Response getDataCollection() {
        return Response.success(dataCollectionChange());
    }

    public DataCollectionResponse dataCollectionChange(){
        //-- 累计成交金额 累计用户收益 累计注册人数
        DataCollectionResponse response = borrowDao.getDatas();
        // 累计成交笔数
        Integer ljcjbs = borrowDao.getLjcjbs();
        response.setLjcjbs(ljcjbs);
        //累计出借人总数(人)
        Integer ljcjrzs = borrowDao.getLjcjrzs();
        response.setLjcjrzs(ljcjrzs);
        //本年度成交金额(元)
        Double bndcjje = borrowDao.getBndcjje(DateUtils.thisYear());
        response.setBncjje(bndcjje);
        //待还金额(万元)
        Double dhje = borrowDao.dhje();
        response.setDhje(dhje);
        //最大单户出借余额占比
        Double zddhcjyezb = borrowDao.getZddhcjyezb();
        Double zddhc = NumberUtil.divide(4,zddhcjyezb,dhje);
        String zddhcjb = NumberUtil.strFormat2(NumberUtil.multiply(2,zddhc,100));
        response.setZddhcjyezb(zddhcjb);
        //其余单户出借余额占比
        Double qydhcjyezb =  NumberUtil.subtract(4,1,zddhc);
        String qydhcjb = NumberUtil.strFormat2(NumberUtil.multiply(2,qydhcjyezb,100));
        response.setQydhcjyezb(qydhcjb);
        //最大10户投资出借占比
        Double zdshtzcjzb = borrowDao.getZdshtzcjzb();
        Double zdzb = NumberUtil.divide(4,zdshtzcjzb,dhje);
        String zdshcjb = NumberUtil.strFormat2(NumberUtil.multiply(2,zdzb,100));
        response.setZdshtzcjzb(zdshcjb);
        //其余用户出借余额占比
        Double qyyhcjyezb = NumberUtil.subtract(4,1,zdzb);
        String qyyhzb = NumberUtil.strFormat2(NumberUtil.multiply(2,qyyhcjyezb,100));
        response.setQyyhcjyezb(qyyhzb);
        //性别男女 占比
        UserProportion xbnan = borrowDao.userProportion();
        response.setXbnan(NumberUtil.strFormat2(NumberUtil.multiply(2,xbnan.getNanzb(),100)));
        response.setXbnv(NumberUtil.strFormat2(NumberUtil.multiply(2,xbnan.getNvzb(),100)));
        //待还金额笔数(笔)
        Integer dhjebs = borrowDao.dhjebs();
        response.setDhjebs(dhjebs);
        //累计借款人总数(人)
        Integer ljjkrzs = borrowDao.ljjkrzs();
        response.setLjjkrzs(ljjkrzs);

//        当前借款人数量
        Integer dqjkrsl = borrowDao.dqjkrsl();
        response.setDqjkrsl(dqjkrsl);
//        当前出借人数量
        Integer dqcjrsl = borrowDao.dqcjrsl();
        response.setDqcjrsl(dqcjrsl);
        return response;
    }

    @Override
    public Double getUserAmtMoneyInTime(Integer userId, String starttime, String endtime) {
        return borrowDao.getUserAmtMoneyInTime(userId,starttime,endtime);
    }

    @Override
    public Response showHt(int fbid) {
        Map<String, Object> map = new HashMap<>();
        logger.info("用户查看投资记录获取合同信息开始,{}", fbid);

        FinanceBidPO financeBid = financeBidDao.getFidById(fbid);
        if (null == financeBid) {
            logger.error("fbid={},合同信息显示有误", fbid);
            return Response.error();
        }
        financeBid.setQxr(DateUtils.dateStr2(financeBid.getBeginProfit()));
        financeBid.setDqr(DateUtils.dateStr2(financeBid.getEndProfit()));
        map.put("buyAmt", financeBid.getBuyAmt());
        map.put("interest", financeBid.getInterest());
        map.put("dqr", financeBid.getDqr());
        map.put("qxr", financeBid.getQxr());

        String contract = DateUtils.dateStr5(financeBid.getPayTime()) + financeBid.getId();
        map.put("contract", contract); //合同编号

        logger.info("获取投资人信息");
        //获取投资人信息
        User tuser = userRpcService.getUserByUserId(String.valueOf(financeBid.getUserId()));
        map.put("tusername", tuser.getUsername());
        map.put("tphone", tuser.getPhone());
        map.put("trealname", financeBid.getPayName());
        map.put("tcard", financeBid.getPayPid());

        BorrowPO borrow = borrowDao.getBorrowById(financeBid.getBorrowId());
        map.put("limitDay", borrow.getTimeLimitDay());
        map.put("apr", borrow.getApr());
        map.put("loanUage", borrow.getLoan_usage());
        map.put("account", borrow.getAccount());

        //获取借款人信息
        User juser = userRpcService.getUserByUserId(String.valueOf(borrow.getUserId()));
        if (null != juser) {
            map.put("jusername", juser.getUsername());
            map.put("jphone", TextUtil.hidePhoneNo(juser.getPhone()));
            map.put("jrealname", TextUtil.hideRealnameChar(juser.getRealname()));
        } else {
            map.put("jusername", "");
            map.put("jphone", "");
            map.put("jrealname", "杭州**网络科技有限公司");
        }

        logger.info("用户查看投资记录获取合同信息结束,{}", fbid);
        return Response.successByMap(map);

    }

    @Override
    public Response showTzHt(int borrowId, String buyAmt,String userId) {
        Map<String, Object> map = new HashMap<>();
        logger.info("用户{}访问投资记录里面的合同", userId);

        BorrowPO borrow = borrowDao.getBorrowById(borrowId);
        map.put("limitDay", borrow.getTimeLimitDay());
        map.put("apr", borrow.getApr());
        map.put("loanUage", borrow.getLoan_usage());
        map.put("account", borrow.getAccount());
        map.put("qxr", DateUtils.dateStr2(DateUtils.rollDay(new Date(), 1)));
        map.put("dqr", DateUtils.dateStr2(DateUtils.rollDay(new Date(), borrow.getTimeLimitDay() + 1)));
        if (buyAmt != null && !"".equals(buyAmt)) {
            Double interest = Double.valueOf(buyAmt) * borrow.getTimeLimitDay() / 36500 * Double.valueOf(borrow.getApr());
            map.put("interest", NumberUtil.round(interest, 2));
            map.put("buyAmt", buyAmt);
        } else {
            map.put("interest", "");
            map.put("buyAmt", "");
        }

        map.put("contract", ""); //合同编号

        //获取投资人信息
        User tuser = this.userRpcService.getUserByUserId(userId);
        map.put("tusername", TextUtil.hideUsernameChar(tuser.getUsername()));
        map.put("tphone", TextUtil.hidePhoneNo(tuser.getPhone()));
        map.put("trealname", "");
        map.put("tcard", "");

        //获取借款人信息
        User juser = this.userRpcService.getUserByUserId(String.valueOf(borrow.getUserId()));
        if (null != juser) {
            map.put("jusername", TextUtil.hideUsernameChar(juser.getUsername()));
            map.put("jphone", TextUtil.hidePhoneNo(juser.getPhone()));
            map.put("jrealname", TextUtil.hideRealnameChar(juser.getRealname()));
        } else {
            map.put("jusername", "");
            map.put("jphone", "");
            map.put("jrealname", "");
        }

        logger.info("用户{}访问投资记录里面的合同结束", userId);
        return Response.successByMap(map);
    }

    @Override
    public Response financeList(Integer page, Integer isxs) {

        Map<String, Object> map = new HashMap<>();
        if (isxs != null && isxs == 0) {
            map.put("isxs", "0");
        }

        List<BorrowPO> list = borrowDao.getShowFinaceList(map, page);

        //活动时间 2018-07-02 到 2018-07-10前
        if (!DateUtils.compareDate(startTime, DateUtils.getNowDateStr()) && DateUtils.compareDate(endTime, DateUtils.getNowDateStr())) {
            List<BorrowPO> ninetyBorrow = new ArrayList<BorrowPO>();


            //获取90天标的
            if (list != null && list.size() > 0) {
                logger.info("理财列表刷新, 获取90天标的");
                Iterator<BorrowPO> ib = list.iterator();
                while (ib.hasNext()) {
                    BorrowPO borrow = (BorrowPO) ib.next();
                    if ("0".equals(borrow.getUse()) && (Double.valueOf(borrow.getScales()) >= 0 && Double.valueOf(borrow.getScales()) < 1)) {
                        ninetyBorrow.add(0,borrow);
                        ib.remove();
                    }
                    if (borrow.getTimeLimitDay()==90 && (Double.valueOf(borrow.getScales()) >= 0 && Double.valueOf(borrow.getScales()) < 1)) {
                        ninetyBorrow.add(borrow);
                        ib.remove();
                    }
                }
            }
            //新手标放到最前面
            if (ninetyBorrow != null && ninetyBorrow.size() > 0) {
                int bi = 0;
                for (BorrowPO borrow : ninetyBorrow) {
                    list.add(bi, borrow);
                    bi++;
                }
                logger.info("理财列表刷新, 获取90天标的放前面");
            }
            return Response.success(list);
        }else {
            List<BorrowPO> xsBorrow = new ArrayList<BorrowPO>();
            //获取新手标
            if (list != null && list.size() > 0) {
                logger.info("理财列表刷新, 获取新手标");
                Iterator<BorrowPO> ib = list.iterator();
                while (ib.hasNext()) {
                    BorrowPO borrow = (BorrowPO) ib.next();
                    if ("0".equals(borrow.getUse()) && (Double.valueOf(borrow.getScales()) >= 0 && Double.valueOf(borrow.getScales()) < 1)) {
                        xsBorrow.add(borrow);
                        ib.remove();
                    }
                }
            }
            List<BorrowPO> tabBorrow = new ArrayList<BorrowPO>();
            //获取标签活动标
            if (list != null && list.size() > 0) {
                logger.info("理财列表刷新, 获取标签活动标");
                Iterator<BorrowPO> ib = list.iterator();
                while (ib.hasNext()) {
                    BorrowPO borrow = (BorrowPO) ib.next();
                    if (BorrowTabEnums.TAB_BKJX.getValue().equals(borrow.getLitpic())&&(Double.valueOf(borrow.getAccount())-Double.valueOf(borrow.getAccountYes()))>0) {
                        tabBorrow.add(borrow);
                        ib.remove();
                    }
                }
            }

            //新手标放到最前面
            if (xsBorrow != null && xsBorrow.size() > 0) {
                int bi = 0;
                for (BorrowPO borrow : xsBorrow) {
                    list.add(bi, borrow);
                    bi++;
                }
                    int tab = xsBorrow.size();
                    for (BorrowPO borrow : tabBorrow) {
                        list.add(tab, borrow);
                        tab++;
                    }

                logger.info("理财列表刷新, 新手标放在前面");
            }else {
                    int tab = 0;
                    for (BorrowPO borrow : tabBorrow) {
                        list.add(tab, borrow);
                        tab++;
                    }
            }

            return Response.success(list);
        }


    }

    @Override
    public Response getBorrow(Integer id) {

        logger.info("开始获取{}标的信息", id);

        Map<String, Object> rmap = new HashMap<>();
        BorrowPO borrow = borrowDao.getShowFinaceById(id);
        rmap.put("name", borrow.getName());
        rmap.put("account", borrow.getAccount());
        rmap.put("accountYes", borrow.getAccountYes());
        rmap.put("apr", borrow.getApr());
        rmap.put("lowestAccount", borrow.getLowestAccount());
        rmap.put("mostAccount", borrow.getMostAccount());
        rmap.put("timeLimitDay", borrow.getTimeLimitDay());
        rmap.put("scales", borrow.getScales());
        rmap.put("use", borrow.getUse());
        rmap.put("addApr", borrow.getAddApr());
        rmap.put("id", borrow.getId());  //标的id
        rmap.put("litpic", borrow.getLitpic());
        rmap.put("status", borrow.getStatus());
        rmap.put("remmoney", borrow.getRemmoney());
        rmap.put("trustLevel", borrow.getTrustLevel() != null ? borrow.getTrustLevel() : 1); //标的安全等级  1-保守型2-谨慎型3-稳健型4-积极型5-激进型
        rmap.put("validTime", borrow.getValidTime() != null ? borrow.getValidTime() : "7");
        rmap.put("isEval", evaluation); //标的是否需要测评 0--不需要 1--需要
        rmap.put("riskGrade", borrow.getIsMb());  //风险等级  0低风险 1-中低风险 2中等风险 3中高风险 4高风险

        rmap.put("interestDay", "T(成交日)+1");
        rmap.put("refundTyep", "到期本息还款");
        rmap.put("releaseType", borrow.getReleaseType());

        logger.info("结束获取{}标的信息", id);
        return Response.success(rmap);
    }

    @Override
    public Response getBorrowDetail(int id) {
        try {

            logger.info("id={},访问标的详情开始", id);
            Map<String, Object> map = new HashMap<>();
            BorrowPO borrow = borrowDao.getBorrowById(id);
            if (borrow == null) {
                logger.warn("id={},标的信息不存在", id);
                return Response.errorMsg("标的信息不存在");
            }

            if (id > 10599) { //10599   21626
                map.put("borrowFlag", "newBorrow");  //老的标的
            } else {
                map.put("borrowFlag", "oldBorrow");   //新标的
            }

            map.put("financeCompany", borrow.getFinance_company());  //融资企业
            map.put("ficAccount", borrow.getFicAccount()); //借款额度
            map.put("timeLimitDay", borrow.getTimeLimitDay());  //借款期限
            map.put("loanUsage", borrow.getLoan_usage()); //借款用途
            map.put("payment", borrow.getPayment()); //还款来源
            map.put("use", borrow.getUse());  //标的类型

            List<String> img = new ArrayList<>();
            img.add(borrow.getImgurl1());
            img.add(borrow.getImgurl2());
            img.add(borrow.getImgurl3());
            img.add(borrow.getImgurl4());
            img.add(borrow.getImgurl5());
            img.add(borrow.getImgurl6());
            img.add(borrow.getImgurl7());
            img.add(borrow.getImgurl8());
            map.put("img", img);//项目图片

            List<String> imagestrlist = searchHtmlImage(borrow.getContent());
            map.put("imagestrlist", imagestrlist);
            String noimgContent = clsHtmlImage(borrow.getContent());
            map.put("noimgContent", noimgContent);
            List<String> imagestrlist2 = searchHtmlImage(borrow.getContent2());
            map.put("imagestrlist2", imagestrlist2);
            String noimgContent2 = clsHtmlImage(borrow.getContent2());
            map.put("noimgContent2", noimgContent2);

            map.put("trustLevel", borrow.getTrustLevel() != null || borrow.getTrustLevel() == 0 ? borrow.getTrustLevel() : 3);
            map.put("validTime", borrow.getValidTime() != null ? borrow.getValidTime() : "1");

            logger.info("id={},获取标的详情结束", id);
            return Response.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("id={},获取信息失败", id);

        }

        return Response.errorMsg("获取信息失败");
    }
}



