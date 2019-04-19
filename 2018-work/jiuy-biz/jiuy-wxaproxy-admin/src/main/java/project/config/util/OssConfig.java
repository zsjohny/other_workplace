/**
 * 
 */
package project.config.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.yujj.util.file.OSSFileUtil;

/**
 * @author DongZhong
 * @version 创建时间: 2017年10月21日 下午4:23:46
 */
@Configuration
public class OssConfig {

	@Bean
	public OSSFileUtil ossFileUtil() {
		return new OSSFileUtil();
	}
}
