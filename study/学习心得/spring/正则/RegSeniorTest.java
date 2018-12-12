package com.newman.task.collect;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegSeniorTest {
    public static void main(String[] args) {

        String reg = "\\d+(?=</span>)";
        String test = "<span class=\"read-count\">阅读数：641</span>";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(test);
        System.out.println(matcher.find());
        System.out.println(matcher.group());
        //向后取
        Pattern compile = Pattern.compile("Windows(?=95|98|NT|2000)");
        Matcher windows2000 = compile.matcher("Windows2000");
        System.out.println(windows2000.find());
        System.out.println(windows2000.group());
        //向前取
        Pattern compile1 = Pattern.compile("(?<=Office|Word|Excel)2000");
        Matcher office2000 = compile1.matcher("Office2000");
        System.out.println(office2000.find());
        System.out.println(office2000.group());

        //捕获组和反向引用  --找出重复的两个字母
        test = "aabbbbgbddesddfiid";
        //@方式1的命名 就是捕获组的数字命令
        pattern = Pattern.compile("(\\w)\\1");
        Matcher mc = pattern.matcher(test);
        System.out.println(mc.find());
        System.out.println(mc.group());

        //@方式2的命名 就是捕获组的名称的命名
        pattern = Pattern.compile("(?<code>\\w)\\k<code>");
        mc = pattern.matcher(test);
        System.out.println(mc.find());
        System.out.println(mc.group());


    }
	
	 public static void main(String[] args) {
        String url = "跨业通;https://uland.taobao.com/coupon/edetail?activityId=1d84db6f6201457ba38f6ef55d2b3f96&pid=mm_107798109_40296482_154266366&itemId=578488135052,惠购通;https://uland.taobao.com/coupon/edetail?activityId=1d84db6f6201457ba38f6ef55d2b3f96&pid=mm_107798109_132050073_35124950260&itemId=578488135052,淘天通;https://uland.taobao.com/coupon/edetail?activityId=1d84db6f6201457ba38f6ef55d2b3f96&pid=mm_107798109_43590384_338650676&itemId=578488135052,天淘通;https://uland.taobao.com/coupon/edetail?activityId=1d84db6f6201457ba38f6ef55d2b3f96&pid=mm_107798109_42016602_198178475&itemId=578488135052,华夏信用大师;https://uland.taobao.com/coupon/edetail?activityId=1d84db6f6201457ba38f6ef55d2b3f96&pid=mm_107798109_133200130_36124700165&itemId=578488135052";

        String regex = "%s;(.*?),";
        String source = "惠购通";

        Map<String, Pattern> patternMap = new ConcurrentHashMap<>();
        Pattern pattern = patternMap.get(source);
        if (pattern == null) {
            pattern = Pattern.compile(String.format(regex, source));
            patternMap.put(source, pattern);
        }


        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            System.out.println(matcher.group());
            System.out.println(matcher.group(1));//不包含两者之间
        }


    }

}
