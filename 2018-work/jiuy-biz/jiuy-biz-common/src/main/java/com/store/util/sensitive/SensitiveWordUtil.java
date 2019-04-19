package com.store.util.sensitive;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.store.util.sensitive.KWSeeker;
import com.store.util.sensitive.KWSeekerManage;
import com.store.util.sensitive.KeyWord;
import com.store.util.sensitive.SensitiveWordResult;
import com.store.util.sensitive.SimpleKWSeekerProcessor;

/**
 * 
 * 敏感词测试
 * 
 * @author hailin0@yeah.net
 * @createDate 2016年6月4日
 *
 */
public class SensitiveWordUtil {

    public static void main(String[] args) {

        test1();

        System.out.println("\n************************************************\n");

        test2();

    }
    /**
     * 将文本敏感词替换为***
     * 例如：
     * 原文：在中国这是一部黄色电影，在日本这不是一部黄色电影，在美国这不是一部黄色电影
敏感词：中国,美国,黄色
识别结果:
返回敏感词及下标：[SensitiveWordResult [word=中国, positions=[PositionNode [startPosition=1, endPosition=3]]], SensitiveWordResult [word=黄色, positions=[PositionNode [startPosition=6, endPosition=8], PositionNode [startPosition=18, endPosition=20], PositionNode [startPosition=29, endPosition=31]]], SensitiveWordResult [word=美国, positions=[PositionNode [startPosition=23, endPosition=25]]]]
html高亮：在<font color='red'>中国</font>这是一部<font color='red'>黄色</font>电影，在日本这不是一部<font color='red'>黄色</font>电影，在<font color='red'>美国</font>这不是一部<font color='red'>黄色</font>电影
字符替换：在***这是一部***电影，在日本这不是一部***电影，在***这不是一部***电影

     * @param text2
     * @return
     */
    public static String  replaceWords (String text2){
    	// 搜索器组,构建敏感词管理器,可同时管理多个搜索器，
        KWSeekerManage kwSeekerManage = SimpleKWSeekerProcessor.newInstance();

        System.out.println("\n============================\n");

        // 开始测试
//        String text2 = "在中国这是一部黄色电影，在日本这不是一部黄色电影，在美国这不是一部黄色电影";


        List<SensitiveWordResult> words2 = kwSeekerManage.getKWSeeker("common-sensitive-word").findWords(text2);
        System.out.println("返回敏感词及下标：" + words2);
        String s2 = kwSeekerManage.getKWSeeker("common-sensitive-word").htmlHighlightWords(text2);
        System.out.println("html高亮：" + s2);
        String r2 = kwSeekerManage.getKWSeeker("common-sensitive-word").replaceWords(text2);
        System.out.println("字符替换：" + r2);
		return r2;
    }

    /**
     * 代码调用初始化
     */
    public static void test1() {
        // 初始化敏感词
        Set<KeyWord> kws1 = new HashSet<KeyWord>();
        kws1.add(new KeyWord("黄色"));
        kws1.add(new KeyWord("AV"));

        Set<KeyWord> kws2 = new HashSet<KeyWord>();
        kws2.add(new KeyWord("中国"));
        kws2.add(new KeyWord("美国"));
        kws2.add(new KeyWord("黄色"));

        // 根据敏感词,初始化敏感词搜索器
        KWSeeker kwSeeker1 = new KWSeeker(kws1);
        KWSeeker kwSeeker2 = new KWSeeker(kws2);

        // 搜索器组,构建敏感词管理器,可同时管理多个搜索器，map的key为自定义搜索器标识
        Map<String, KWSeeker> seekers = new HashMap<String, KWSeeker>();
        String wordType1 = "sensitive-word-1";
        seekers.put(wordType1, kwSeeker1);
        String wordType2 = "sensitive-word-2";
        seekers.put(wordType2, kwSeeker2);

        KWSeekerManage kwSeekerManage = new KWSeekerManage(seekers);

        // 开始测试
        String text1 = "这是一部黄色电影，也是一部AV电影";
        System.out.println("原文：" + text1);
        System.out.println("敏感词：" + kws1);
        System.out.println("识别结果:");

        List<SensitiveWordResult> words1 = kwSeekerManage.getKWSeeker(wordType1).findWords(text1);
        System.out.println("返回敏感词及下标：" + words1);
        String s1 = kwSeekerManage.getKWSeeker(wordType1).htmlHighlightWords(text1);
        System.out.println("html高亮：" + s1);
        String r1 = kwSeekerManage.getKWSeeker(wordType1).replaceWords(text1);
        System.out.println("字符替换：" + r1);

        System.out.println("\n============================\n");

        // 开始测试
        String text2 = "在中国这是一部黄色电影，在日本这不是一部黄色电影，在美国这不是一部黄色电影";
        System.out.println("原文：" + text2);
        System.out.println("敏感词：" + kws2);
        System.out.println("识别结果:");

        List<SensitiveWordResult> words2 = kwSeekerManage.getKWSeeker(wordType2).findWords(text2);
        System.out.println("返回敏感词及下标：" + words2);
        String s2 = kwSeekerManage.getKWSeeker(wordType2).htmlHighlightWords(text2);
        System.out.println("html高亮：" + s2);
        String r2 = kwSeekerManage.getKWSeeker(wordType2).replaceWords(text2);
        System.out.println("字符替换：" + r2);
    }

    /**
     * 配置文件读取初始化
     */
    public static void test2() {

        // 搜索器组,构建敏感词管理器,可同时管理多个搜索器，
        KWSeekerManage kwSeekerManage = SimpleKWSeekerProcessor.newInstance();

        // 开始测试
        String text1 = "这是一部黄色电影，也是一部AV电影";
        System.out.println("原文：" + text1);
        System.out.println("敏感词：黄色,AV");
        System.out.println("识别结果:");

        List<SensitiveWordResult> words1 = kwSeekerManage.getKWSeeker("comment-sensitive-word")
                .findWords(text1);
        System.out.println("返回敏感词及下标：" + words1);
        String s1 = kwSeekerManage.getKWSeeker("comment-sensitive-word").htmlHighlightWords(text1);
        System.out.println("html高亮：" + s1);
        String r1 = kwSeekerManage.getKWSeeker("comment-sensitive-word").replaceWords(text1);
        System.out.println("字符替换：" + r1);

        System.out.println("\n============================\n");

        // 开始测试
        String text2 = "在中国这是一部黄色电影，在日本这不是一部黄色电影，在美国这不是一部黄色电影";
        System.out.println("原文：" + text2);
        System.out.println("敏感词：中国,美国,黄色");
        System.out.println("识别结果:");

        List<SensitiveWordResult> words2 = kwSeekerManage.getKWSeeker("topic-sensitive-word")
                .findWords(text2);
        System.out.println("返回敏感词及下标：" + words2);
        String s2 = kwSeekerManage.getKWSeeker("topic-sensitive-word").htmlHighlightWords(text2);
        System.out.println("html高亮：" + s2);
        String r2 = kwSeekerManage.getKWSeeker("topic-sensitive-word").replaceWords(text2);
        System.out.println("字符替换：" + r2);

    }
}
