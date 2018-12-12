package com.finace.miscroservice.commons.utils.PDF;

import com.finace.miscroservice.commons.log.Log;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;

public class PDFKitUtil {

    private static Log logger = Log.getInstance(PDFKitUtil.class);

    //PDF页眉、页脚定制工具
    private HeaderFooterBuilder headerFooterBuilder;
    private String saveFilePath;

    /**
     * @param fileName 输出PDF文件名
     * @param data     模板所需要的数据
     * @description 导出pdf到文件
     */
    public String exportToFile(String fileName, Object data, String sFilePath) {

        String htmlData = FreeMarkerUtil.getContent(fileName, data);
        if (StringUtils.isEmpty(sFilePath)) {
            sFilePath = getDefaultSavePath(fileName);
        }
        File file = new File(sFilePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream outputStream = null;
        try {
            //设置输出路径
            outputStream = new FileOutputStream(sFilePath);
            //设置文档大小
            Document document = new Document(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            //设置页眉页脚
            /*PDFBuilder builder = new PDFBuilder(headerFooterBuilder, data);
            builder.setPresentFontSize(10);
            writer.setPageEvent(builder);*/

            //输出为PDF文件
            convertToPDF(writer, document, htmlData);
        } catch (Exception ex) {
//            throw new PDFException("PDF export to File fail",ex);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
        return sFilePath;
    }


    /**
     * 生成PDF到输出流中（ServletOutputStream用于下载PDF）
     *
     * @param ftlPath  ftl模板文件的路径（不含文件名）
     * @param data     输入到FTL中的数据
     * @param response HttpServletResponse
     * @return
     */
    public OutputStream exportToResponse(String ftlPath, Object data,
                                         HttpServletResponse response) {

        String html = FreeMarkerUtil.getContent(ftlPath, data);

        try {
            OutputStream out = null;
            ITextRenderer render = null;
            out = response.getOutputStream();
            //设置文档大小
            Document document = new Document(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, out);
            //设置页眉页脚
            PDFBuilder builder = new PDFBuilder(headerFooterBuilder, data);
            writer.setPageEvent(builder);
            //输出为PDF文件
            convertToPDF(writer, document, html);
            return out;
        } catch (Exception ex) {
//            throw  new PDFException("PDF export to response fail",ex);
        }
        return null;
    }

    /**
     * @description PDF文件生成
     */
    private void convertToPDF(PdfWriter writer, Document document, String htmlString) {
        //获取字体路径
        String fontPath = getFontPath();
        document.open();
        try {
            //
            String classpath = PDFKitUtil.class.getClassLoader().getResource("template").getPath();
            String templatePath = classpath.replaceAll("build/classes/main|out/production", "src/main");
            XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                    new ByteArrayInputStream(htmlString.getBytes()),
                    XMLWorkerHelper.class.getResourceAsStream(templatePath + "/contract.css"),
                    Charset.forName("UTF-8"), new XMLWorkerFontProvider(fontPath));
//            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(htmlString.getBytes()), Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
//            throw new PDFException("PDF文件生成异常",e);
        } finally {
            document.close();
        }

    }

    /**
     * @description 创建默认保存路径
     */
    private String getDefaultSavePath(String fileName) {
        String classpath = PDFKitUtil.class.getClassLoader().getResource("").getPath();
        String saveFilePath = classpath + "pdf/" + fileName;
        File f = new File(saveFilePath);
        if (!f.getParentFile().exists()) {
            f.mkdirs();
        }
        return saveFilePath;
    }

    /**
     * @description 获取字体设置路径
     */
    public static String getFontPath() {
        String classpath = PDFKitUtil.class.getClassLoader().getResource("font").getPath();
        return classpath.replaceAll("build/classes/main|out/production", "src/main");
    }

    public HeaderFooterBuilder getHeaderFooterBuilder() {
        return headerFooterBuilder;
    }

    public void setHeaderFooterBuilder(HeaderFooterBuilder headerFooterBuilder) {
        this.headerFooterBuilder = headerFooterBuilder;
    }

    public String getSaveFilePath() {
        return saveFilePath;
    }

    public void setSaveFilePath(String saveFilePath) {
        this.saveFilePath = saveFilePath;
    }


    public static void export(String templateView, Object model, String temp) throws Exception {
        logger.info("生成的合同文件地址：" + temp);
        FileOutputStream tempOs = new FileOutputStream(temp);
//                Template template = FreeMarkerRender.getConfiguration().getTemplate(templateView);
//                StringWriter result = new StringWriter();
//                template.process(model, result);
//            String htmlData = result.toString();//模版变量
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, tempOs);

        //添加水印和页码
        PDFBuilder builder = new PDFBuilder();
        writer.setPageEvent(builder);

        document.open();
       /* XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                new ByteArrayInputStream(templateView.getBytes()), Charset.forName("UTF-8"));*/

        String classpath = PDFKitUtil.class.getClassLoader().getResource("template").getPath();
        String templatePath = classpath.replaceAll("build/classes/main|out/production", "src/main");
        System.out.println(templatePath);
        System.out.println(getFontPath());
        XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                new ByteArrayInputStream(templateView.getBytes()),
                XMLWorkerHelper.class.getResourceAsStream(templatePath + "/contract.css"),
                Charset.forName("UTF-8"), new XMLWorkerFontProvider(getFontPath()));


        document.close();
    }


}