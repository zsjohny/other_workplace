package com.finace.miscroservice.commons.utils.PDF;

import com.finace.miscroservice.commons.utils.UUIdUtil;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.io.IOUtils;

import java.io.FileOutputStream;

public class Test {


    public static void main(String arg[]) {
//        String path = kit.exportToFile("content"+ UUIdUtil.generateUuid()+".pdf", null, "/root/Desktop/"+"content"+Math.random()+".pdf");

        /*String  classpath=PDFKitUtil.class.getClassLoader().getResource("template").getPath();
        System.out.println(classpath);
        String templatePath=classpath.replaceAll("build/classes/main|out/production", "src/main/resources");
        System.out.println(templatePath);*/

        PDFHeaderFooter headerFooter=new PDFHeaderFooter();

        PDFKitUtil kit = new PDFKitUtil();
//        kit.setHeaderFooterBuilder(headerFooter);
//
//        String path = kit.exportToFile("contractTmplate.pdf", null, "/root/Desktop/pdf/"+"content"+UUIdUtil.generateUuid()+".pdf");


        try{
//            String html = FreeMarkerUtil.getContent("contractTmplate.pdf", null);
            String html = "<!DOCTYPE html>\n" +
                    "<html><style>body{font-family:pingfang sc light;}</style> \n" +
                    "<body>\n" +
                    "<div> 你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好" +
                    "</div>\n" +
                    "</body>\n" +
                    "</html>\n";
            System.out.println(html);
            kit.export(html,"","/root/Desktop/pdf/"+"content"+UUIdUtil.generateUuid()+".pdf");
        }catch(Exception ex){
            ex.printStackTrace();

        }finally{
        }


    }







}
