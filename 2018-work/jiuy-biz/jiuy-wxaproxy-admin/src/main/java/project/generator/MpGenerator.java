package project.generator;

import java.util.HashMap;
import java.util.Map;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 * <p>
 * 代码生成器演示
 * </p>
 */
public class MpGenerator {

	/**
	 * <p>
	 * MySQL 生成演示
	 * </p>
	 */
	public static void main(String[] args) {
		AutoGenerator mpg = new AutoGenerator();

		// 全局配置
		GlobalConfig gc = new GlobalConfig();
		//gc.setOutputDir("D:\\ideaSpace\\guns\\src\\main\\java");// 这里写你自己的java目录
		gc.setOutputDir("D:\\ideaSpace\\guns\\src\\main\\java");// 这里写你自己的java目录
		gc.setFileOverride(true);// 是否覆盖
		gc.setActiveRecord(true);
		gc.setEnableCache(false);// XML 二级缓存
		gc.setBaseResultMap(true);// XML ResultMap
		gc.setBaseColumnList(false);// XML columList
		gc.setAuthor("nijin");
		mpg.setGlobalConfig(gc);

		// 数据源配置
		DataSourceConfig dsc = new DataSourceConfig();
		dsc.setDbType(DbType.MYSQL);
		dsc.setTypeConvert(new MySqlTypeConvert() {
			// 自定义数据库表字段类型转换【可选】
			@Override
			public DbColumnType processTypeConvert(String fieldType) {
				return super.processTypeConvert(fieldType);
			}
		});
		dsc.setDriverName("com.mysql.jdbc.Driver");
		dsc.setUsername("mysql");
		dsc.setPassword("daf24fzirewafarqgwba131");
		dsc.setUrl("jdbc:mysql://192.168.1.6:53306/yjjlocal?characterEncoding=utf8");
		mpg.setDataSource(dsc);

		// 策略配置
		StrategyConfig strategy = new StrategyConfig();
		strategy.setTablePrefix(new String[] { "yjj_" });// 此处可以修改为您的表前缀
		strategy.setInclude(new String[] { " yjj_Brand" }); // 需要生成的表
		strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
		mpg.setStrategy(strategy);

		// 包配置
		PackageConfig pc = new PackageConfig();
		pc.setParent(null);
		pc.setEntity("com.supplier.entity");//com.supplier.entity
		pc.setMapper("com.supplier.dao");//com.supplier.dao
		pc.setXml("com.supplier.dao.mapping");//com.supplier.dao.mapping
		//pc.setService("TTT"); // 本项目没用，生成之后删掉
		//pc.setServiceImpl("TTT"); // 本项目没用，生成之后删掉
		//pc.setController("TTT"); // 本项目没用，生成之后删掉
		mpg.setPackageInfo(pc);

		// 注入自定义配置，可以在 VM 中使用 cfg.abc 设置的值
		InjectionConfig cfg = new InjectionConfig() {
			@Override
			public void initMap() {
				Map<String, Object> map = new HashMap<>();
				map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
				this.setMap(map);
			}
		};
		mpg.setCfg(cfg);

		// 执行生成
		mpg.execute();

		// 打印注入设置
		System.err.println(mpg.getCfg().getMap().get("abc"));
	}

}