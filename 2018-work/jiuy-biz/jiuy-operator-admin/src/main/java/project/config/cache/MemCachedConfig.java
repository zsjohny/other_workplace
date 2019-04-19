package project.config.cache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jiuyuan.constant.Constants;
import com.jiuyuan.service.common.MemcachedService;

import net.spy.memcached.MemcachedClient;
import net.spy.memcached.spring.MemcachedClientFactoryBean;

/**
 * ehcache缓存的配置
 *
 * @author fengshuonan
 * @date 2017-04-24 20:41
 */
@Configuration
public class MemCachedConfig {

	@Bean
	public MemcachedClientFactoryBean objectCacheClient() {
		MemcachedClientFactoryBean mem = new MemcachedClientFactoryBean();
		// System.out.println(Constants);
		mem.setServers(Constants.MEMCACHED_SERVERS);
		mem.setOpTimeout(1000);
		return mem;
	}

	@Bean
	public MemcachedService memcachedService() throws Exception {
		MemcachedService mem = new MemcachedService();
		mem.setRealKeyPrefix("provider_web_");
		mem.setMemcachedClient((MemcachedClient) objectCacheClient().getObject());
		return mem;
	}
}
