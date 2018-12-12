/**
 * onway.com Inc.
 * Copyright (c) 2016-2016 All Rights Reserved.
 */
package com.onway.web.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.onway.baib.common.service.enums.BaibCoreResultCodeEnum;
import com.onway.baib.common.service.exception.BaibCoreException;
import com.onway.baib.common.util.RegexpUtils;
import com.onway.baib.common.util.signatur.MD5Signaturer;
import com.onway.baib.common.util.signatur.Signaturer;
import com.onway.baib.core.model.Constant;
import com.onway.common.lang.StringUtils;
import com.onway.gotone.common.service.enums.SmsTypeEnum;
import com.onway.platform.common.configration.ConfigrationFactory;
import com.onway.platform.common.enums.PlatformCodeEnum;
import com.onway.web.controller.result.JsonPageResult;

/**
 * 
 * 
 * @author li.hong
 * @version $Id: AbstractJsonController.java, v 0.1 2016年9月8日 下午4:56:41 li.hong Exp $
 */
public class AbstractJsonController extends AbstractController {
    /**
     * logger
     */
    protected static final Logger logger  = Logger.getLogger(AbstractJsonController.class);

    protected DecimalFormat       dfZero  = new DecimalFormat("0");

    protected DecimalFormat       dfDigit = new DecimalFormat("0.00");

    @Resource
    protected Signaturer          signaturer;


    private MD5Signaturer         md5     = new MD5Signaturer();

    protected String adjustDecode(HttpServletRequest request,String key,String errorMessage){
        String value = request.getParameter(key);
        if(value == null){
            throw new BaibCoreException(errorMessage);
        }
        try {
            return URLDecoder.decode(value, Constant.UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new BaibCoreException(Constant.TRANSCODING_ERROR,e);
        }
    }

    protected int adjustPageNum(HttpServletRequest request) {
        int originalPageNum = 1;

        originalPageNum = request.getParameter(Constant.PAGE_NUM) == null ?
                originalPageNum : Integer.valueOf(request.getParameter(Constant.PAGE_NUM));

        if (originalPageNum <= 0) {
            return Constant.PAGE_NUM_DIGIT;
        }
        return originalPageNum;
    }

    protected int adjustPageSize(HttpServletRequest request) {
        int originalPageSize = 10;

        originalPageSize = request.getParameter(Constant.PAGE_SIZE) == null ?
                originalPageSize : Integer.valueOf(request.getParameter(Constant.PAGE_SIZE));

        if (originalPageSize <= 0) {
            return Constant.PAGE_SIZE_DIGIT;
        }
        return originalPageSize;
    }

    protected void cellVerify(String cell) throws BaibCoreException {

        if (StringUtils.isEmpty(cell)
            || !RegexpUtils.isHardRegexpValidate(cell, RegexpUtils.CELL_REGEXP)) {
            throw new BaibCoreException(BaibCoreResultCodeEnum.ILLEGAL_ARGUMENT.getCode(),
                "手机号码格式不正确");
        }

    }

    protected void checkCodeVerify(String checkCode) throws BaibCoreException {

        if (StringUtils.isEmpty(checkCode)
            || !RegexpUtils.isHardRegexpValidate(checkCode, RegexpUtils.CHECK_CODE)) {
            throw new BaibCoreException(BaibCoreResultCodeEnum.ILLEGAL_ARGUMENT.getCode(), "验证码格式不正确");
        }

    }

    /**
     * 手机验证码确认
     * 
     * @param phone         手机号码
     * @param checkCode     验证码
     * @return
     */
    protected void smsVerify(SmsTypeEnum type, String phone, String checkCode,
                             PlatformCodeEnum platformCode) throws BaibCoreException {

        checkCodeVerify(checkCode);

        if (!ConfigrationFactory.getConfigration().isProd()) {
            if (checkCode.equals("000000")) {
                return;
            }

            throw new BaibCoreException(BaibCoreResultCodeEnum.ERROR_CHECK_CODE.getCode(),
                BaibCoreResultCodeEnum.ERROR_CHECK_CODE.getMessage());

        }
//            SmDO smDO = smDAO.selectByCell(phone);
//            if (StringUtils.notEquals(checkCode, smDO.getVerifyCode())) {
//                throw new BaibCoreException(BaibCoreResultCodeEnum.ERROR_CHECK_CODE.getCode(),
//                    BaibCoreResultCodeEnum.ERROR_CHECK_CODE.getMessage());
//            }

//        throw new baibCoreException(baibCoreResultCodeEnum.ERROR_CHECK_CODE.getCode(),
//            baibCoreResultCodeEnum.ERROR_CHECK_CODE.getMessage());

    }

//    /**
//     * 验证来源的合法性和手机登陆的唯一性
//     *
//     * @param request
//     */
//    protected void validate(HttpServletRequest request) throws baibCoreException {
//
//        String userId = request.getParameter(Constant.USER_ID);
//        String appType = request.getParameter(Constant.APP_TYPE);
//        String version = request.getParameter(Constant.VERSION);
//        String tokenId = request.getParameter(Constant.TOKEN);
//
//        //测试环境
//        if (!ConfigrationFactory.getConfigration().isProd()) {
//            return;
//        }
//
//        if (StringUtils.isBlank(userId) || StringUtils.isBlank(tokenId) || !verifySrc(request)) {
//            throw new baibCoreException(BaibCoreResultCodeEnum.INVALID_REQUEST,
//                BaibCoreResultCodeEnum.INVALID_REQUEST.getMessage());
//        }
//
//        if (userInfoQueryServiceClient.queryByUserId(userId).getResultObject() == null) {
//            throw new baibCoreException(BaibCoreResultCodeEnum.USER_NOT_EXSIT,
//                BaibCoreResultCodeEnum.USER_NOT_EXSIT.getMessage());
//        }
//
//
//
//        // 手机登陆唯一校验
//        UserTokenResult tokenResult = userTokenHelper.token(userId, tokenId, appType, version);
//        tokenResult.setSuccess(true);
//        if (!tokenResult.isSuccess()) {
//            throw new BaibCoreException(BaibCoreResultCodeEnum.FORCE_LOG_OUT.getCode(),
//                BaibCoreResultCodeEnum.FORCE_LOG_OUT.getMessage());
//        }
//    }

    /**
     * 验证来源的合法性
     *
     * @param request
     * @throws TokenInvalidException
     */
    protected void validateLegal(HttpServletRequest request) throws BaibCoreException {

        if (!verifySrc(request)) {
            throw new BaibCoreException(BaibCoreResultCodeEnum.INVALID_REQUEST,
                BaibCoreResultCodeEnum.INVALID_REQUEST.getMessage());
        }
    }

    protected boolean verifySrc(HttpServletRequest request) {

        //测试环境
        if (!ConfigrationFactory.getConfigration().isProd()) {
            return true;
        }

        String version = request.getParameter(Constant.VERSION);
        String appType = request.getParameter(Constant.APP_TYPE);
        String sign = request.getParameter(Constant.SIGN);
        String stime = request.getParameter(Constant.TIME);

        if (StringUtils.isBlank(appType) || StringUtils.isBlank(stime)
            || StringUtils.isBlank(version) || StringUtils.isBlank(sign)) {
            throw new BaibCoreException(BaibCoreResultCodeEnum.INVALID_REQUEST,
                BaibCoreResultCodeEnum.INVALID_REQUEST.getMessage());
        }

        String encryTime = md5.sign(stime);

        if (StringUtils.notEquals(sign, encryTime)) {
            throw new BaibCoreException(BaibCoreResultCodeEnum.INVALID_REQUEST,
                BaibCoreResultCodeEnum.INVALID_REQUEST.getMessage());
        }
        return true;
    }

    protected <T> void buildDefaultPageResult(JsonPageResult<T> result) {

        result.setListObject(null);
        result.setNext(false);
        result.setPageNum(Constant.PAGE_NUM_DIGIT);
        result.setTotalPages(0);

    }

    protected void checkProdCode(String prodCode) {
        if (StringUtils.isEmpty(prodCode)) {

            throw new BaibCoreException("prodCode不能为空！");
        }
    }

    /**
     * 转码
     * 
     * @param request
     * @param key
     * @return
     *  return new JsonResult(false, "", TRANSCODING_ERROR);
     */
    protected String transcoding(final HttpServletRequest request, String key, String msg) {
        String str = "";
        try {
            str = new String(request.getParameter(key).getBytes("iso-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.error(msg + "编码方式转型异常", e);
            throw new BaibCoreException(Constant.TRANSCODING_ERROR);
        }
        return str;
    }
}