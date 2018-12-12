package com.onway.baib.biz.service.helper;

import com.onway.baib.biz.service.query.SysConfigCacheService;
import com.onway.baib.core.exception.ParamErrorException;
import com.onway.baib.core.model.base.result.UserTokenResult;
import com.onway.cif.common.service.enums.AppTypeEnum;
import com.onway.common.lang.StringUtils;

/**
 * 用户token的辅助类
 * 
 * @author guangdong.li
 * @version $Id: UserTokenHelper.java, v 0.1 17 Feb 2016 15:02:17 guangdong.li Exp $
 */
public class UserTokenHelper {

    protected SysConfigCacheService sysConfigCacheService;

    /**
     * 校验用户token，确保用户使用手机的唯一性 ,强制版本更新 
     * 
     * @param userId 用户ID
     * @param token APP Token
     * @return 结果JSON
     */
    public UserTokenResult token(String userId, String token, String appType, String version) {
        UserTokenResult result = new UserTokenResult();
        result.setSuccess(true);
        /* if (StringUtils.isBlank(version)) {
             // 避免因为Android客户端取不到版本号而导致无法作重复登录判断  add by guangdong.li 
             version = "1.0.4.2";
         }*/
        if (StringUtils.equals(appType, AppTypeEnum.PC.code())) {
            return result;
        }

        /*  if (StringUtils.equals(appType, AppTypeEnum.IOS.code())) {
              if ((compareVersion(version, "2.12.0") < 0)) {
                  result.setMessage("请升级到最新版本！");
                  result.setSuccess(false);
                  return result;
              }
          }
          if (StringUtils.equals(appType, AppTypeEnum.ANDROID.code())) {
              if ((compareVersion(version, "4.2.5.0") < 0)) {
                  result.setMessage("请升级到最新版本！");
                  result.setSuccess(false);
                  return result;
              }
          }
        */
        /*  int versionInt = Integer.parseInt(StringUtils.replace(version, ".", ""));
          if (StringUtils.equals(appType, AppTypeEnum.IOS.code()) && versionInt <= 120) {
              return result;
          }
          if (StringUtils.equals(appType, AppTypeEnum.ANDROID.code()) && versionInt <= 1024) {
              return result;
          }
        */
        /* QueryResult<CifUserToken> queryUserToken = userAppInfoServiceClient
             .getCifUserTokenByUserId(userId);*/

        /*if (queryUserToken == null) {
            result.setSuccess(false);
            result.setMessage("账户登录超时，请重新登录");
            return result;
        }*/
        /*
          if (StringUtils.isEmpty(queryUserToken.getResultObject().getTokenLogin())) {
              result.setSuccess(false);
              result.setMessage("审核通过，请用新手机号重新登录");
              return result;
          }*/

        /*  if (!StringUtils.equals(
              StringUtils.defaultString(queryUserToken.getResultObject().getTokenLogin()).replaceAll(
                  "\n", ""), StringUtils.defaultString(token).replaceAll("\n", ""))) {
              result.setSuccess(false);
              result.setMessage("账号已在其他设备上登录，为了账号安全，请重新登录");
              return result;
          }
          result.setEncrpKey(queryUserToken.getResultObject().getEncrpKey());*/
        return result;
    }

    /**
     * 比较版本高低，version1 &lt; version2 返回-1 ，等于返回0，大于返回1
     * 
     * @param version1 版本1
     * @param version2 版本2
     * @return  version1 &lt; version2 返回-1 ，等于返回0，大于返回1
     * @throws ParamErrorException 版本格式不正确
     */
    public static int compareVersion(String version1, String version2) {
        if (version1 == null || version2 == null) {
            throw new ParamErrorException("版本号格式错误");
        }
        String[] versionArray1 = version1.split("\\.");
        String[] versionArray2 = version2.split("\\.");
        int idx = 0;
        int minLength = Math.min(versionArray1.length, versionArray2.length);//取最小长度值  
        int diff = 0;
        while (idx < minLength
               && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0//先比较长度  
               && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {//再比较字符  
            ++idx;
        }
        //如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；  
        diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
        return diff;
    }

}
