package com.jfinal.weixin.jiuy.cache;

import com.jfinal.aop.Duang;
import com.jfinal.log.Log;
import com.jfinal.utils.ConfigUtil;
import com.schooner.MemCached.SchoonerSockIOPool;
import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;

import java.lang.reflect.Field;
import java.net.Socket;
import java.util.Date;

//import com.danga.MemCached.MemCachedClient;
//import com.danga.MemCached.SockIOPool;

/****
 *  MemCached的工具类
 * @author zhaoxinglin
 * https://github.com/gwhalin/Memcached-Java-Client
 * http://download.csdn.net/detail/xiaokailele/9440908
 * http://blog.csdn.net/xiaokailele/article/details/50725584
 */
public class MemcachedUtil {
    protected static MemcacheApi memcacheApi = Duang.duang(MemcacheApi.class);
    static Log logger = Log.getLog(MemcachedUtil.class);
    private static String SERVERS = "servers";
//	public static void main(String[] args) {
//		//1、准备数据
//		String authorizer_refresh_token = memcacheApi.getRefreshToken("wx16b9f4dfa90e887c");
//		String url = "https://api.weixin.qq.com/cgi-bin/component/api_authorizer_token?component_access_token=x5vXQRwed2w4zmy39Sn76MgjCYNx3TIoTF7DBM9m3H2Eg84vY3Kid13oh1h2V6Yvzcfv8z7rtmrEtddx0RiSuGWGeyMfdFrSgDrDaKrP4e00AffsUdLbp6FdWEhiOaU1SBFaACACLP";
//		//组装参数调用接口
//		Map<String, String> paramMap = new HashMap<String, String>();
//		paramMap.put("component_appid", "wxddd55d6028f404ab");
//		paramMap.put("authorizer_appid", "wx16b9f4dfa90e887c");
//		paramMap.put("authorizer_refresh_token", authorizer_refresh_token);
//    	String param = JSONObject.toJSONString(paramMap).toString();
//    	logger.info("开始获取小程序接口调用凭证api_authorizer_token, url:{"+url+"}, param:{"+param+"}");
//    	Map<String, Object> retMap = WapPayHttpUtil.sendPostHttpReturnMap(url, param);
//    	logger.info("获取小程序接口调用凭证api_authorizer_token成功, url:{"+url+"}, param:{"+param+"}，retMap{"+retMap+"}");
//		//解析返回值，将token放入缓存
//    	String new_authorizer_access_token = (String) retMap.get("authorizer_access_token");
//    	Integer expires_in = (Integer) retMap.get("expires_in");
//    	String new_authorizer_refresh_token = (String) retMap.get("authorizer_refresh_token");
//
////    	memcacheApi.setAccessToken(authorizer_appid , new_authorizer_access_token,expires_in);
////
////    	memcacheApi.setRefreshToken(authorizer_appid , new_authorizer_refresh_token);
//
////    	accessTokenCache.set(authorizer_access_token_groupKey   +  third_appId + authorizer_appid, new_authorizer_access_token);
////    	accessTokenCache.set(authorizer_refresh_token_groupKey   +  third_appId + authorizer_appid, new_authorizer_refresh_token);
//    	logger.info("token放入了缓存码，new_authorizer_access_token:{"+new_authorizer_access_token+"}，new_authorizer_refresh_token："+new_authorizer_refresh_token);
//
//}
    /**
     * memcached客户端单例，唯一实例
     */
    private static MemCachedClient cachedClient = new MemCachedClient();

    /**
     * 初始化连接池
     */
    static {
        // 获取连接池的实例
        SockIOPool pool = SockIOPool.getInstance();

        // 服务器列表及其权重
//        String[] servers = { "192.168.1.3:11211" };
        Socket socket = new Socket();

        //TODO:xxx
//        String[] servers = {"127.0.0.1:11211"};
        String server =  ConfigUtil.getMemcachedServers(SERVERS);
        String[] servers ={server};


        //        String[] servers = { "47.96.20.44:11211" };
        Integer[] weights = {1};

        // 设置服务器信息
        pool.setServers(servers);
        pool.setWeights(weights);

        // 设置初始连接数、最小连接数、最大连接数、最大处理时间
        pool.setInitConn(10);
        pool.setMinConn(10);
        pool.setMaxConn(1000);
        pool.setMaxIdle(1000 * 60 * 60);

        // 设置连接池守护线程的睡眠时间
        pool.setMaintSleep(60);

        // 设置TCP参数，连接超时
        pool.setNagle(false);
        pool.setSocketTO(60);
        pool.setSocketConnectTO(0);

        // 初始化并启动连接池
        pool.initialize();

        // 压缩设置，超过指定大小的都压缩
        // cachedClient.setCompressEnable(true);
        // cachedClient.setCompressThreshold(1024*1024);

        cachedClient.setPrimitiveAsString(true);// 设置序列化

    }


    /**
     * 构造函数:工具类，禁止实例化
     */
    private MemcachedUtil() {
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println(getStr("component_verify_ticket_appid_wx3ef25e066e478873"));

    }

    public static String getServer() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Field f = cachedClient.getClass().getDeclaredField("client");
            f.setAccessible(true);
            Object client = f.get(cachedClient);
            Field poolF = client.getClass().getDeclaredField("pool");
            poolF.setAccessible(true);
            SchoonerSockIOPool pool = (SchoonerSockIOPool) poolF.get(client);
            String[] servers = pool.getServers();
            for (String server : servers) {
                stringBuilder.append(server);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /***
     * 功能描述:新增一个缓存数据，如果key存在不会新增
     *
     * @param 缓存的key
     * @param 缓存的值
     * @return 操作结果
     */
    public static boolean add(String key, Object value) {
        return cachedClient.add(key, value);
    }

    /**
     * 功能描述:新增一个缓存数据，设置过期时间参数为秒
     *
     * @param 缓存的key
     * @param 缓存的值
     * @param 缓存时间
     * @return 操作结果
     */
    public static boolean add(String key, Object value, Integer expire) {
        return cachedClient.add(key, value, expire);
    }

    /**
     * 功能描述:新增一个缓存数据，如果存在key，则更新该key的值
     *
     * @param 缓存的key
     * @param 缓存的值
     * @return 操作结果
     */
    public static boolean put(String key, Object value) {
        return cachedClient.set(key, value);
    }

    /**
     * 功能描述:新增一个缓存数据，如果存在key，则更新该key的值
     *
     * @param 缓存的key
     * @param 缓存的值
     * @param 缓存时间
     * @return 操作结果
     */
    public static boolean put(String key, Object value, Integer expire) {
        if (expire < 0) {
            expire = 0;
        }
        logger.info("cached 地址：" + getServer());
        return cachedClient.set(key, value, new Date(1 * expire * 1000));
//        return cachedClient.set(key, value, new Date(System.currentTimeMillis()+expire));
//        return cachedClient.set(key, value, expire);
    }

    /**
     * 功能描述:替换一个缓存数据，如果存在key则替换，否则返回false
     *
     * @param key
     * @param value
     * @return 操作结果
     */
    public static boolean replace(String key, Object value) {
        return cachedClient.replace(key, value);
    }

    /**
     * 功能描述:替换一个缓存数据，如果存在key则替换，否则返回false
     *
     * @param key
     * @param value
     * @param 缓存时间
     * @return 操作结果
     */
    public static boolean replace(String key, Object value, Integer expire) {
        return cachedClient.replace(key, value, expire);
    }

    /**
     * 功能描述:根据key得到一个缓存数据
     *
     * @param key
     * @return 操作结果
     */
    public static Object get(String key) {
        return cachedClient.get(key);
    }

    /**
     * 功能描述:根据key得到一个缓存数据
     *
     * @param key
     * @return 操作结果
     */
    public static String getStr(String key) {
        logger.info("cached 地址：" + getServer());
        return (String) cachedClient.get(key);
    }

    /**
     * 功能描述:刷新全部缓存(就是将所有缓存设置为过期，以后放入的会覆盖掉这些过期的缓存数据)
     *
     * @return 操作结果
     */
    public static boolean flushAll() {
        return cachedClient.flushAll();
    }

    /***
     * 功能描述:根据key删除一个缓存数据
     * @return 操作结果
     */
    public static boolean delete(String key) {
        return cachedClient.delete(key);
    }


}
