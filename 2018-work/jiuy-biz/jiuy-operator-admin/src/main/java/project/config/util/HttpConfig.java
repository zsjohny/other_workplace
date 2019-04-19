/**
 * 
 */
package project.config.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jiuyuan.service.common.HttpClientService;
import com.jiuyuan.util.http.HttpClientFactory;

/**
 * @author DongZhong
 * @version 创建时间: 2017年10月23日 上午9:45:56
 */
@Configuration
public class HttpConfig {
	private HttpClientFactory httpClient() {
		HttpClientFactory httpClient = new HttpClientFactory();
		httpClient.setMaxThreads(500);
		httpClient.setConnectionTimeoutSecs(5);
		httpClient.setSoTimeoutSecs(10);
		httpClient.setRetryCount(0);
		httpClient.setRequestSentRetryEnabled(false);
		httpClient.setTimeoutRetryEnabled(false);

		return httpClient;
	}

	@Bean
	public HttpClientService httpClientService() throws Exception {
		HttpClientService httpClientService = new HttpClientService();
		httpClientService.setHttpClient(httpClient().getObject());

		return httpClientService;
	}
}
