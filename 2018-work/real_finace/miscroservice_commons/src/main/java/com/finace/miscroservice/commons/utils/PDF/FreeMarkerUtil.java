package com.finace.miscroservice.commons.utils.PDF;

import com.finace.miscroservice.commons.log.Log;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.lang.StringUtils;
import java.io.File;
import java.io.StringWriter;

/**
 * Created by fgm on 2017/4/22.
 * FREEMARKER 模板工具类
 *
 */
public class FreeMarkerUtil {
    private static Log logger = Log.getInstance(FreeMarkerUtil.class);

    /**
     * @description 获取模板
     */
    public static String getContent(String fileName,Object data){

        String templatePath=getPDFTemplatePath(fileName);
        if(StringUtils.isEmpty(templatePath)){
            return null;
        }

        String templateFileName=getTemplateName(templatePath);
        String templateFilePath=getTemplatePath(templatePath);

        try{
            Configuration config = new Configuration(Configuration.VERSION_2_3_25);
            config.setDefaultEncoding("UTF-8");
            config.setDirectoryForTemplateLoading(new File(templateFilePath));
            config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            config.setLogTemplateExceptions(false);
            Template template = config.getTemplate(templateFileName);
            StringWriter writer = new StringWriter();
            template.process(data, writer);
            writer.flush();
            String html = writer.toString();
            return html;
        }catch (Exception ex){

        }
        return null;
    }

    private static String getTemplatePath(String templatePath) {
        if(StringUtils.isEmpty(templatePath)){
            return "";
        }
        String path=templatePath.substring(0,templatePath.lastIndexOf("/"));
        return path;
    }

    private static String getTemplateName(String templatePath) {
        if(StringUtils.isEmpty(templatePath)){
            return "";
        }
        String fileName=templatePath.substring(templatePath.lastIndexOf("/")+1);
        return fileName;
    }

    /**
     * @description 获取PDF的模板路径,
     * 默认按照PDF文件名匹对应模板
     * @param fileName PDF文件名    (hello.pdf)
     * @return         匹配到的模板名
     */
    public static String getPDFTemplatePath(String fileName){
        String  classpath=PDFKitUtil.class.getClassLoader().getResource("template").getPath();
        String templatePath=classpath.replaceAll("build/classes/main|out/production", "src/main");
        File file=new File(templatePath);
        if(!file.isDirectory()){
            //PDF模板文件不存在,请检查templates文件夹!
            return null;
        }
        String pdfFileName=fileName.substring(0,fileName.lastIndexOf("."));
        File defaultTemplate=null;
        File matchTemplate=null;
        for(File f:file.listFiles()){
            if(!f.isFile()){
                continue;
            }
            String templateName=f.getName();
            if(templateName.lastIndexOf(".ftl")==-1){
                continue;
            }
            if(defaultTemplate==null){
                defaultTemplate=f;
            }
            if(StringUtils.isEmpty(fileName)&&defaultTemplate!=null){
                break;
            }
            templateName=templateName.substring(0,templateName.lastIndexOf("."));
            if(templateName.toLowerCase().equals(pdfFileName.toLowerCase())){
                matchTemplate=f;
                break;
            }
        }
        if(matchTemplate!=null){
            return matchTemplate.getAbsolutePath();
        }
        if(defaultTemplate!=null){
            return defaultTemplate.getAbsolutePath();
        }

        return null;

    }



}
