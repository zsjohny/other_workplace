package project.config.web.beetl;

import org.beetl.ext.spring.BeetlGroupUtilConfiguration;

import com.admin.core.util.KaptchaUtil;
import com.admin.core.util.ToolUtil;
import com.jiuy.wxaproxy.core.beetl.ShiroExt;

public class BeetlConfiguration extends BeetlGroupUtilConfiguration {

	@Override
	public void initOther() {

		groupTemplate.registerFunctionPackage("shiro", new ShiroExt());
		groupTemplate.registerFunctionPackage("tool", new ToolUtil());
		groupTemplate.registerFunctionPackage("kaptcha", new KaptchaUtil());
	}

}
