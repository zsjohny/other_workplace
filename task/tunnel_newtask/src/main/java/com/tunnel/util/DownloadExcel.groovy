package com.tunnel.util

import com.tunnel.controller.TunnelInfoTotalController
import com.tunnel.domain.*
import com.tunnel.service.BaseDomainServer
import jxl.Cell
import jxl.Sheet
import jxl.Workbook
import jxl.format.Alignment
import jxl.format.VerticalAlignment
import jxl.write.*
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

import java.beans.BeanInfo
import java.beans.Introspector
import java.beans.PropertyDescriptor
import java.lang.Boolean
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by Ness on 2016/11/2.
 */
@Component
class DownloadExcel {


    @Autowired
    private BaseDomainServer baseDomainServer;

    static ConcurrentHashMap<String, HashMap<String, List<BaseDomain>>> downloadExcelType = new ConcurrentHashMap<>()
    static Map<String, String> englishToChineseMap = new HashMap<>()
    static Map<String, String> englishToJsonMap = new HashMap<>()
    static Map<String, String> classNameToChineseMap = new HashMap<>()
    static Map<String, String> chineseToEnglishMap = new HashMap<>()
    static Map<String, String> jsonToEnglishMap = new HashMap<>()
    static Map<String, String> chineseToClassNameMap = new HashMap<>()
    static Map<String, String> englishToClassTypeMap = new HashMap<>()
    static ConcurrentHashMap<String, Map<String, Integer>> sortMap = new ConcurrentHashMap<>();
    static {

        //利用java内省技术加载excel
        //加载excel的配置
        Properties properties = new Properties();
        properties.load(new InputStreamReader(DownloadExcel.class.getClassLoader().getResourceAsStream("englishToChinese.properties"), "utf-8"));


        Enumeration<?> enumeration = properties.propertyNames()

        while (enumeration.hasMoreElements()) {
            Object key = enumeration.nextElement()
            englishToChineseMap.put(key, properties.get(key))

        }

        //加载对应的Integer解释的map
        properties.clear()
        properties.load(new InputStreamReader(DownloadExcel.class.getClassLoader().getResourceAsStream("englishToJson.properties"), "utf-8"))

        enumeration = properties.propertyNames()

        while (enumeration.hasMoreElements()) {
            Object key = enumeration.nextElement()
            englishToJsonMap.put(key, properties.get(key))

        }

        //加载对应的类英文名解释中文名
        properties.clear()
        properties.load(new InputStreamReader(DownloadExcel.class.getClassLoader().getResourceAsStream("classNameToChinese.properties"), "utf-8"))

        enumeration = properties.propertyNames()

        while (enumeration.hasMoreElements()) {
            Object key = enumeration.nextElement()
            classNameToChineseMap.put(key, properties.get(key))

        }
        //加载对应的中文解释英文excel
        properties.clear()
        properties.load(new InputStreamReader(DownloadExcel.class.getClassLoader().getResourceAsStream("chineseToEnglish.properties"), "utf-8"))

        enumeration = properties.propertyNames()

        while (enumeration.hasMoreElements()) {
            Object key = enumeration.nextElement()
            chineseToEnglishMap.put(key, properties.get(key))

        }
        //加载对应的map解释Integer的map
        properties.clear()
        properties.load(new InputStreamReader(DownloadExcel.class.getClassLoader().getResourceAsStream("chineseToClassName.properties"), "utf-8"))

        enumeration = properties.propertyNames()

        while (enumeration.hasMoreElements()) {
            Object key = enumeration.nextElement()
            chineseToClassNameMap.put(key, properties.get(key))

        }
        //加载对应的json转数字的map
        properties.clear()
        properties.load(new InputStreamReader(DownloadExcel.class.getClassLoader().getResourceAsStream("jsonToEnglish.properties"), "utf-8"))

        enumeration = properties.propertyNames()

        while (enumeration.hasMoreElements()) {
            Object key = enumeration.nextElement()
            jsonToEnglishMap.put(key, properties.get(key))

        }
        //加载对应的json转class类型的map
        properties.clear()
        properties.load(new InputStreamReader(DownloadExcel.class.getClassLoader().getResourceAsStream("englishToClassType.properties"), "utf-8"))

        enumeration = properties.propertyNames()

        while (enumeration.hasMoreElements()) {
            Object key = enumeration.nextElement()
            englishToClassTypeMap.put(key, properties.get(key))

        }

        //加载类的顺序的配置
        Map<String, Integer> map = null;


        File[] files = new File(DownloadExcel.class.getResource("/domain").getPath()).listFiles();


        for (File file : files) {

            List<String> strings = Files.readAllLines(Paths.get(file.getPath()));
            map = new HashMap<>();

            int len = strings.size();

            int k = 0;
            for (int i = 0; i < len; i++) {
                if (i == len - 2) {
                    break;
                }
                if (strings.get(i + 1).trim().contains("page")) {
                    continue;
                }
                if (strings.get(i).trim().startsWith("//") && (strings.get(i + 1).trim().startsWith("Long") ||
                        strings.get(i + 1).trim().startsWith("Double") ||
                        strings.get(i + 1).trim().startsWith("Integer") || strings.get(i + 1).trim().startsWith("Boolean") ||
                        strings.get(i + 1).trim().startsWith("String") || strings.get(i + 1).trim().startsWith("LocalDateTime")
                ) && strings.get(i + 2).trim().length() == 0) {
                    map.put(strings.get(i + 1).trim().split(" ")[1], k++);
                }


            }
            if (!map.isEmpty()) {
                sortMap.put(file.getName(), map);

            }
            println map
        }

    }


    public void setDownoadExcel(File tempFile) {

        Workbook workbook = Workbook.getWorkbook(tempFile)

        JSONObject jsonObject
        Sheet[] sheets = workbook.getSheets()
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm")

        for (Sheet sheet : sheets) {
            //存储的首行
            List<BaseDomain> baseDomainList = new ArrayList<>()
            String className = chineseToClassNameMap.get(sheet.getName())
            String tunnelNumber = ""
            int len = sheet.getColumns()

            //获取首行
            String[] titles = new String[sheet.getRows()]
            Cell[] cells = sheet.getColumn(0)
            for (int i = 0; i < cells.length; i++) {

                titles[i] = chineseToEnglishMap.get(cells[i].getContents())
            }

            println "________________________________"

            println className

            println "________________________________"

            //数据行
            for (int j = 1; j < len; j++) {


                BaseDomain tempBase = null
                //判断类型
                switch (className) {

                    case ClassType.ClassTypeMapping.tunnelImportPort.getKey():
                        tempBase = new TunnelImportPort()
                        break;

                    case ClassType.ClassTypeMapping.tunnelShallowCover.getKey():
                        tempBase = new TunnelShallowCover()
                        break;
                    case ClassType.ClassTypeMapping.assistTunnel.getKey():
                        tempBase = new AssistTunnel()
                        break;
                    case ClassType.ClassTypeMapping.tunnelGrabageExamine.getKey():
                        tempBase = new TunnelGrabageExamine()
                        break;
                    case ClassType.ClassTypeMapping.tunnelHeadRiskExamine.getKey():
                        tempBase = new TunnelHeadRiskExamine()
                        break;
                    case ClassType.ClassTypeMapping.overBreakRiskExmaine.getKey():
                        tempBase = new OverBreakRiskExmaine()
                        break;
                    case ClassType.ClassTypeMapping.surgeMudRiskExamine.getKey():
                        tempBase = new SurgeMudRiskExamine()
                        break;
                    case ClassType.ClassTypeMapping.shapeRiskExamine.getKey():
                        tempBase = new ShapeRiskExamine()
                        break;
                    case ClassType.ClassTypeMapping.rockOutburstRiskExamine.getKey():
                        tempBase = new RockOutburstRiskExamine()
                        break;
                    case ClassType.ClassTypeMapping.gasRiskExamine.getKey():
                        tempBase = new GasRiskExamine()
                        break;

                    case ClassType.ClassTypeMapping.fireRiskExmaine.getKey():
                        tempBase = new FireRiskExmaine()
                        break;

                    case ClassType.ClassTypeMapping.trafficAccidentRiskExamine.getKey():
                        tempBase = new TrafficAccidentRiskExamine()
                        break;
                    case ClassType.ClassTypeMapping.envirRiskExamine.getKey():
                        tempBase = new EnvirRiskExamine()
                        break;

                    case ClassType.ClassTypeMapping.otherRiskExamine.getKey():
                        tempBase = new OtherRiskExamine()
                        break;
                    case ClassType.ClassTypeMapping.figure.getKey():
                        tempBase = new Figure()
                        break
                }

                cells = sheet.getColumn(j);
                for (int k = 0; k < cells.length; k++) {
                    //特殊处理

                    if ("Integer".equals(englishToClassTypeMap.get(titles[k]))) {

                        if (!StringUtils.isEmpty(cells[k].getContents())) {
                            jsonObject = new JSONObject(jsonToEnglishMap.get(titles[k]))
                            tempBase.setProperty(titles[k], jsonObject.getInt(cells[k].getContents()))
                        }


                    } else if ("Boolean".equals(englishToClassTypeMap.get(titles[k]))) {
                        if (!StringUtils.isEmpty(cells[k].getContents())) {
                            jsonObject = new JSONObject(jsonToEnglishMap.get(titles[k]))

                            tempBase.setProperty(titles[k], jsonObject.getBoolean(cells[k].getContents()))
                        }
                    } else if ("LocalDateTime".equals(englishToClassTypeMap.get(titles[k]))) {
                        LocalDateTime time = LocalDateTime.now()
                        if (!StringUtils.isEmpty(cells[k].getContents())) {
                            time = LocalDateTime.parse(cells[k].getContents(), dateTimeFormatter)

                        }
                        tempBase.setProperty(titles[k], time)

                    } else if ("Double".equals(englishToClassTypeMap.get(titles[k]))) {
                        if (!StringUtils.isEmpty(cells[k].getContents())) {
                            tempBase.setProperty(titles[k], Double.valueOf(cells[k].getContents()))
                        }

                    } else if ("picture".equals(englishToClassTypeMap.get(titles[k]))) {
                        if (!StringUtils.isEmpty(cells[k].getContents())) {
                            /*  Image image = sheet.getDrawing(k)
                              sheet.getDrawing()

                              File file = image.getImageFile()

                              if (file != null) {
                                  //暂不书写
                              }*/

                        }
                    } else {
                        if (!StringUtils.isEmpty(cells[k].getContents())) {
                            //记录tunnelNumber
                            if ("tunnelNumber".equals(titles[k])) {
                                tunnelNumber = cells[k].getContents()
                            }
                            tempBase.setProperty(titles[k], cells[k].getContents())
                        }
                    }


                }
                baseDomainList.add(tempBase)

            }

            //存储
            baseDomainServer.saveBaseDomainAll(baseDomainList)
            //存储对应关系
            TunnelExamineRelationQuery relationQuery = new TunnelExamineRelationQuery("tunnelNumber": tunnelNumber, "tableName": className, "createTime": LocalDateTime.now())

            //先查询调查跟表格的关系是否存在
            if (baseDomainServer.findBaseDomainByBaseDomain(relationQuery) == null) {
                //添加调查和表格的关系
                baseDomainServer.saveTunnelExmaineRelationQuery(relationQuery)

            }


            Thread.sleep(500)

        }


    }

    public byte[] getDownloadExcel(String sessionId, Integer downloadType, String className, BaseDomain baseDomain) {

        List<BaseDomain> list = null;
        //书写Excel
        File tempFile = File.createTempFile("start", "end")
        tempFile.createNewFile()


        WritableWorkbook workbook = Workbook.createWorkbook(tempFile)

        if (downloadType.equals(DownloadExcel.downloadWay.DOWNLOAD_PAGE.getValue())) {
            list = downloadExcelType.get(sessionId).get(className)
            if (list == null || list.isEmpty()) {
                tempFile.delete()
                return null
            }

            WritableSheet sheet = workbook.createSheet("temp", 0)
            //单条书写数据
            writeExcel(list, baseDomain, sheet)
        } else if (downloadType.equals(DownloadExcel.downloadWay.DOWNLOAD_ALL.getValue())) {
            list = baseDomainServer.findBaseDomainALl(baseDomain)
            if (list == null || list.isEmpty()) {
                tempFile.delete()
                return null
            }

            WritableSheet sheet = workbook.createSheet("temp", 0)
            //单条书写数据
            writeExcel(list, baseDomain, sheet)
        } else if (downloadType.equals(DownloadExcel.downloadWay.DOWNLOAD_QUERY_ALL.getValue())) {

            User user = baseDomain
            //分开书写
            String[] classNames = user.getPassKey().split(":")[1].split(",")

            String tunnelNumber = user.getPassKey().split(":")[0].trim()

            //增加封面
            Workbook os = Workbook.getWorkbook(generateCover(tunnelNumber, classNames))
            if (os != null) {
                workbook = Workbook.createWorkbook(tempFile, os)
            }


            WritableSheet sheet
            BaseDomain tempBase
            int k = 4
            for (String str : classNames) {
                if (StringUtils.isEmpty(str)) {
                    continue
                }
                String tempSheetName = ""
                switch (str) {
                    case ClassType.ClassTypeMapping.tunnelImportPort.getKey():
                        tempBase = new TunnelImportPort("tunnelNumber": tunnelNumber, "page": 0)
                        tempSheetName = "tunnelImportPort"
                        break;
                    case ClassType.ClassTypeMapping.tunnelShallowCover.getKey():
                        tempBase = new TunnelShallowCover("tunnelNumber": tunnelNumber, "page": 0)
                        tempSheetName = "tunnelShallowCover"
                        break;
                    case ClassType.ClassTypeMapping.assistTunnel.getKey():
                        tempBase = new AssistTunnel("tunnelNumber": tunnelNumber, "page": 0)
                        tempSheetName = "assistTunnel"
                        break;
                    case ClassType.ClassTypeMapping.tunnelGrabageExamine.getKey():
                        tempBase = new TunnelGrabageExamine("tunnelNumber": tunnelNumber, "page": 0)
                        tempSheetName = "tunnelGrabageExamine"
                        break;

                    case ClassType.ClassTypeMapping.tunnelHeadRiskExamine.getKey():
                        tempBase = new TunnelHeadRiskExamine("tunnelNumber": tunnelNumber, "page": 0)
                        tempSheetName = "tunnelHeadRiskExamine"
                        break;

                    case ClassType.ClassTypeMapping.overBreakRiskExmaine.getKey():
                        tempBase = new OverBreakRiskExmaine("tunnelNumber": tunnelNumber, "page": 0)
                        tempSheetName = "overBreakRiskExmaine"
                        break;
                    case ClassType.ClassTypeMapping.surgeMudRiskExamine.getKey():
                        tempBase = new SurgeMudRiskExamine("tunnelNumber": tunnelNumber, "page": 0)
                        tempSheetName = "surgeMudRiskExamine"
                        break;
                    case ClassType.ClassTypeMapping.shapeRiskExamine.getKey():
                        tempBase = new ShapeRiskExamine("tunnelNumber": tunnelNumber, "page": 0)
                        tempSheetName = "shapeRiskExamine"
                        break;
                    case ClassType.ClassTypeMapping.rockOutburstRiskExamine.getKey():
                        tempBase = new RockOutburstRiskExamine("tunnelNumber": tunnelNumber, "page": 0)
                        tempSheetName = "rockOutburstRiskExamine"
                        break;
                    case ClassType.ClassTypeMapping.gasRiskExamine.getKey():
                        tempBase = new GasRiskExamine("tunnelNumber": tunnelNumber, "page": 0)
                        tempSheetName = "gasRiskExamine"
                        break;
                    case ClassType.ClassTypeMapping.fireRiskExmaine.getKey():
                        tempBase = new FireRiskExmaine("tunnelNumber": tunnelNumber, "page": 0)
                        tempSheetName = "fireRiskExmaine"
                        break;
                    case ClassType.ClassTypeMapping.trafficAccidentRiskExamine.getKey():
                        tempBase = new TrafficAccidentRiskExamine("tunnelNumber": tunnelNumber, "page": 0)
                        tempSheetName = "trafficAccidentRiskExamine"
                        break;
                    case ClassType.ClassTypeMapping.envirRiskExamine.getKey():
                        tempBase = new EnvirRiskExamine("tunnelNumber": tunnelNumber, "page": 0)
                        tempSheetName = "envirRiskExamine"
                        break;

                    case ClassType.ClassTypeMapping.otherRiskExamine.getKey():
                        tempBase = new OtherRiskExamine("tunnelNumber": tunnelNumber, "page": 0)
                        tempSheetName = "otherRiskExamine"
                        break;
                    case ClassType.ClassTypeMapping.figure.getKey():
                        tempBase = new Figure("tunnelNumber": tunnelNumber, "page": 0)
                        tempSheetName = "figure"
                }
                list = baseDomainServer.findBaseDomainALl(tempBase)
                if (list == null || list.isEmpty()) {
//                    k--
                    continue
                }
                sheet = workbook.createSheet(classNameToChineseMap.get(tempSheetName), k++)
                writeExcel(list, tempBase, sheet)
            }

        }

        workbook.write();
        workbook.close();
        byte[] bytes = Files.readAllBytes(Paths.get(tempFile.getPath()))
        tempFile.delete()
        return bytes
    }

    private void writeExcel(List<BaseDomain> list, BaseDomain baseDomain, WritableSheet sheet) {


        WritableCellFormat format = new WritableCellFormat();

        format.setAlignment(Alignment.CENTRE);
        format.setVerticalAlignment(VerticalAlignment.CENTRE);
        format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THICK)
        WritableFont font = new WritableFont(WritableFont.ARIAL)
        font.setColour(jxl.format.Colour.RED)
        WritableCellFormat writeFormat = new WritableCellFormat(font);

        writeFormat.setAlignment(Alignment.CENTRE);
        writeFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
        writeFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THICK)
        Label label = null

        int length = list.size()

        //json转换的类型
        JSONObject jsonObject = null
        String value = null
        //判断是否进行绘画的处理
//        Boolean pictureFlag = false
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm")

        for (int i = 0; i < length; i++) {


            baseDomain = list.get(i)

            BeanInfo beanInfo = Introspector.getBeanInfo(baseDomain.getClass())
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors()

            Map<String, Integer> propertySortMap = sortMap.get(baseDomain.getClass().getSimpleName() + ".groovy");

            PropertyDescriptor[] newPropertys = new PropertyDescriptor[propertyDescriptors.length];

            for (int m = 0; m < propertyDescriptors.length; m++) {


                for (PropertyDescriptor p : propertyDescriptors) {
                    if (propertySortMap.get(p.getName()) == null) {
                        continue;
                    }
                    if (propertySortMap.get(p.getName()) == m) {
                        newPropertys[m] = p;
                    }
                }


            }

            //进行
            int j = 0;
            int k = 0;

            //风险下载时候，不存在则把相应的风险发生概率不显示
            Boolean riskShow = false
            for (PropertyDescriptor property : newPropertys) {
                if (property == null) {
                    continue
                }
                //排除自带的属性
                if ("class".equals(property.getName()) || "metaClass".equals(property.getName()) || "id".equals(property.getName()) || "page".equals(property.getName())) {
                    continue
                }

                //填充栏目
                if (i == 0) {
                    //首次需要填充到Excel第一栏
                    String title = englishToChineseMap.get(property.getName())

                    label = new Label(i, j, title, format)
                    j++
                    sheet.addCell(label)
                }

                //加载图片的需要特殊处理
                if ("photosPicture".equals(property.getName()) || "acccessTunnelAndAirshaftRelationWithMainTunnel".equals(property.getName())) {

                    String paths = property.getReadMethod().invoke(baseDomain).toString()
                    if (StringUtils.isEmpty(paths)) {
                        k++
                        continue
                    }

                    paths = paths.replaceAll("[^\\d+]", "")

//                    pictureFlag = true
                    int len = paths.length() / 15

                    for (int y = 0; y < len; y++) {
                        String str = paths.substring(y * 15, (y + 1) * 15)

                        if (str == null || str.trim().length() == 0) {
                            continue
                        }
                        char[] names = baseDomain.class.getSimpleName().toCharArray()
                        names[0] += 32
                        String path = new String(names)

                        File file = new File(TunnelInfoTotalController.savepath + path, str + ".png")
                        if (!file.exists()) {
                            continue
                        }

                        WritableImage image = new WritableImage(k, i + 1, 2, 2, file);
                        sheet.addImage(image)
                        sheet.setColumnView(k, 50)
                    }
                    k++

                    continue
                }

                //字体颜色的格式选择
                Boolean fontFlag = false

                if (property.getPropertyType() == Integer.class || property.getPropertyType() == Boolean.class) {


                    jsonObject = new JSONObject(englishToJsonMap.get(property.getName()))
                    value = jsonObject.getString(property.getReadMethod().invoke(baseDomain).toString())

                    //风险源的概率的特殊处理
                    if ("riskSourceExists".equals(property.getName())) {
                        if ("存在".equals(value)) {
                            riskShow = true
                        }


                    }

                    //风险源的概率的特殊处理
                    if ("riskHappendProbalility".equals(property.getName()) && !riskShow) {
                        value = ""

                    }


                } else if (property.getPropertyType() == LocalDateTime.class) {
                    LocalDateTime time = property.getReadMethod().invoke(baseDomain)
                    if (time != null) {
                        value = time.format(dateTimeFormatter)
                    } else {
                        value = ""
                    }

                } else {
                    if ("tunnelNumber".equals(property.getName())) {
                        fontFlag = true
                    }
                    value = property.getReadMethod().invoke(baseDomain)

                }


                label = new Label(i + 1, k, value, fontFlag ? writeFormat : format)

                sheet.addCell(label)

                //日期和图片和图片格式化
//                if (pictureFlag || property.getPropertyType() == LocalDateTime.class) {
                sheet.setColumnView(k, 30)
//                }

                k++

            }

        }


    }


    public File generateCover(String tunnelNumber, String[] sheetName) {
        File tempFile = File.createTempFile("oss", "ogg")
//        File tempFile = new File("G:\\Users\\Ness\\Desktop\\1.xls")

        File original = new File(DownloadExcel.class.getClassLoader().getResource("conver.xls").getPath())

        TunnelInfo info = baseDomainServer.findBaseDomainByBaseDomain(new TunnelInfo("tunnelNumber": tunnelNumber))

        if (info == null) {
            return tempFile
        }

        Project project = baseDomainServer.findBaseDomainByBaseDomain(new Project("projectNumber": info.getProjectNumber()))

        if (project == null) {
            return tempFile
        }


        Workbook os = Workbook.getWorkbook(original)
        WritableCellFormat format = new WritableCellFormat();
        format.setAlignment(Alignment.CENTRE);
        format.setVerticalAlignment(VerticalAlignment.CENTRE);
        format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THICK)
        WritableWorkbook workbook = Workbook.createWorkbook(tempFile, os)

        //项目
        WritableSheet sheet = workbook.getSheet(0)
        sheet.getSettings().setShowGridLines(true)
        int i = 6
        Label label = new Label(7, i, project.getProjectName(), format);
        sheet.addCell(label)
        i += 2
        label = new Label(7, i, project.getProjectNumber(), format);
        sheet.addCell(label)

        i += 2
        label = new Label(7, i, project.getExaminePersion(), format);
        sheet.addCell(label)

        i += 2
        label = new Label(7, i, project.getContactInfo(), format);
        sheet.addCell(label)
        i += 2
        label = new Label(7, i, project.getExamineDate(), format);
        sheet.addCell(label)

        //隧道
        sheet = workbook.getSheet(1)
        i = 0
        label = new Label(2, ++i, info.getTunnelNumber(), format);
        sheet.addCell(label)
        label = new Label(2, ++i, new JSONObject(englishToJsonMap.get("designPeriod")).getString(info.getDesignPeriod().toString()), format);
        sheet.addCell(label)
        label = new Label(2, ++i, new JSONObject(englishToJsonMap.get("structuralStyle")).getString(info.getStructuralStyle().toString()), format);
        sheet.addCell(label)
        label = new Label(2, ++i, info.getTunnelDriection(), format);
        sheet.addCell(label)
        label = new Label(2, ++i, (info.getStartCourse_rightWire_importPort() == null ? "" : info.getStartCourse_rightWire_importPort().toString()), format);
        sheet.addCell(label)
        label = new Label(4, i, (info.getStartCourse_rightWire_exportPort() == null ? "" : info.getStartCourse_rightWire_exportPort().toString()), format);
        sheet.addCell(label)
        label = new Label(2, ++i, (info.getStartCourse_leftWire_importPort() == null ? "" : info.getStartCourse_leftWire_importPort().toString()), format);
        sheet.addCell(label)
        label = new Label(4, i, (info.getStartCourse_leftWire_exportPort() == null ? "" : info.getStartCourse_leftWire_exportPort().toString()), format);
        sheet.addCell(label)
        label = new Label(2, ++i, (info.getRoadHigh_rightWire_importPort() == null ? "" : info.getRoadHigh_rightWire_importPort().toString()), format);
        sheet.addCell(label)
        label = new Label(4, i, (info.getRoadHigh_rightWire_exportPort() == null ? "" : info.getRoadHigh_rightWire_exportPort().toString()), format);
        sheet.addCell(label)
        label = new Label(2, ++i, (info.getRoadHigh_leftWire_importPort() == null ? "" : info.getRoadHigh_leftWire_importPort().toString()), format);
        sheet.addCell(label)
        label = new Label(4, i, (info.getRoadHigh_leftWire_exportPort() == null ? "" : info.getRoadHigh_leftWire_exportPort().toString()), format);
        sheet.addCell(label)
        label = new Label(2, ++i, (info.getPortalType_rightWire_importPort() == null ? "" : info.getPortalType_rightWire_importPort().toString()), format);
        sheet.addCell(label)
        label = new Label(4, i, (info.getPortalType_rightWire_exportPort() == null ? "" : info.getPortalType_rightWire_exportPort().toString()), format);
        sheet.addCell(label)
        label = new Label(2, ++i, (info.getPortalType_leftWire_importPort() == null ? "" : info.getPortalType_leftWire_importPort().toString()), format);
        sheet.addCell(label)
        label = new Label(4, i, (info.getPortalType_leftWire_exportPort() == null ? "" : info.getPortalType_leftWire_exportPort().toString()), format);
        sheet.addCell(label)
        label = new Label(2, ++i, (info.getInterWire_tunnelHead_importPort() == null ? "" : info.getInterWire_tunnelHead_importPort().toString()), format);
        sheet.addCell(label)
        label = new Label(4, i, (info.getInterWire_tunnelHead_exportPort() == null ? "" : info.getInterWire_tunnelHead_exportPort().toString()), format);
        sheet.addCell(label)
        label = new Label(2, ++i, (info.getInterWire_tunnelBody() == null ? "" : info.getInterWire_tunnelBody().toString()), format);
        sheet.addCell(label)
        label = new Label(2, ++i, (info.getParallelLine_rightWire() == null ? "" : info.getParallelLine_rightWire().toString()), format);
        sheet.addCell(label)
        label = new Label(2, ++i, (info.getParallelLine_leftWire() == null ? "" : info.getParallelLine_leftWire().toString()), format);
        sheet.addCell(label)
        label = new Label(2, ++i, (info.getSlopeLine_rightWire() == null ? "" : info.getSlopeLine_rightWire().toString()), format);
        sheet.addCell(label)
        label = new Label(2, ++i, (info.getSlopeLine_leftWire() == null ? "" : info.getSlopeLine_leftWire().toString()), format);
        sheet.addCell(label)
        label = new Label(2, ++i, (info.getTunnelLine_rightWire() == null ? "" : info.getTunnelLine_rightWire().toString()), format);
        sheet.addCell(label)
        label = new Label(2, ++i, (info.getTunnelLine_leftWire() == null ? "" : info.getTunnelLine_leftWire().toString()), format);
        sheet.addCell(label)
        label = new Label(2, ++i, info.getConstructionTeamAdvice(), format);
        sheet.addCell(label)
        label = new Label(2, ++i, info.getWeatherCondition(), format);
        sheet.addCell(label)
        label = new Label(2, ++i, info.getTopographicFeatures(), format);
        sheet.addCell(label)
        label = new Label(2, ++i, info.getGeneralSituationFormation(), format);
        sheet.addCell(label)
        label = new Label(2, ++i, info.getBadGeologicalSurvey(), format);
        sheet.addCell(label)
        label = new Label(2, ++i, info.getHydrogeologicalSurvey(), format);
        sheet.addCell(label)
        label = new Label(2, ++i, info.getDynamicParameters(), format);
        sheet.addCell(label)
        label = new Label(2, ++i, info.getShallowBuried(), format);
        sheet.addCell(label)
        label = new Label(2, ++i, info.getPlannedConstruction(), format);
        sheet.addCell(label)

        //目录
        sheet = workbook.getSheet(2)
        sheet.getSettings().setShowGridLines(true)

        label
        i = 1
        for (String str : sheetName) {
            if (StringUtils.isEmpty(str)) {
                continue

            }
            if (classNameToChineseMap.get(str) == null) {
                continue

            }

            label = new Label(0, ++i, (i - 1).toString(), format);
            sheet.addCell(label)
            label = new Label(1, i, classNameToChineseMap.get(str), format);
            sheet.mergeCells(1, i, 3, i)
            sheet.addCell(label)
            label = new Label(4, i, "表格" + (i + 2), format);
            sheet.addCell(label)


        }

        workbook.write()
        workbook.close()
        return tempFile
    }

    public static void main(String[] args) {


        if (1 == 1) {

            DownloadExcel d = new DownloadExcel()
            println d.englishToChineseMap
            println d.englishToJsonMap
            println d.classNameToChineseMap
            println d.chineseToEnglishMap
            println d.jsonToEnglishMap
            println  d.chineseToClassNameMap
            println  d.englishToClassTypeMap
            return

        }

        String className = "tunnelImportPort,tunnelShallowCover,assistTunnel,tunnelGrabageExamine,tunnelHeadRiskExamine,overBreakRiskExmaine,surgeMudRiskExamine,shapeRiskExamine,rockOutburstRiskExamine,gasRiskExamine,fireRiskExmaine,trafficAccidentRiskExamine,envirRiskExamine,otherRiskExamine,figure,"
        className = "tunnelShallowCover,assistTunnel,tunnelGrabageExamine,tunnelHeadRiskExamine,overBreakRiskExmaine,surgeMudRiskExamine,shapeRiskExamine,rockOutburstRiskExamine,gasRiskExamine,fireRiskExmaine,trafficAccidentRiskExamine,envirRiskExamine,otherRiskExamine,figure,"

        File tempFile = new File("G:\\Users\\Ness\\Desktop\\conver.xls")

        Workbook os = Workbook.getWorkbook(tempFile)
        WritableCellFormat format = new WritableCellFormat();
        format.setAlignment(Alignment.CENTRE);
        format.setVerticalAlignment(VerticalAlignment.CENTRE);
        format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THICK)
        WritableWorkbook workbook = Workbook.createWorkbook(new File("G:\\Users\\Ness\\Desktop\\1.xls"), os)

        //项目


        workbook.write()

        workbook.close()

    }

    enum downloadWay {
        DOWNLOAD_PAGE(0, "分页下载"),
        DOWNLOAD_ALL(1, "全部下载"),
        DOWNLOAD_QUERY_ALL(2, "各个项目全部下载");
        int key;
        String value;

        downloadWay(int key, String value) {
            this.key = key
            this.value = value
        }

        public int getValue() {
            return key

        }
    }

    private static TunnelInfo creatInfo() {
        TunnelInfo info = new TunnelInfo()
        info.setTunnelNumber("11111")
        info.setDesignPeriod(1)
        info.setStructuralStyle(2)
        info.setTunnelDriection("22222")
        info.setStartCourse_rightWire_importPort(3333)
        info.setStartCourse_rightWire_exportPort(4444)
        info.setStartCourse_leftWire_importPort(5555)
        info.setStartCourse_leftWire_exportPort(6666)
        info.setRoadHigh_rightWire_importPort(7777)
        info.setRoadHigh_rightWire_exportPort(88888)
        info.setRoadHigh_leftWire_importPort(99999)
        info.setRoadHigh_leftWire_exportPort(1000000)
        info.setPortalType_rightWire_importPort(11000)
        info.setPortalType_rightWire_exportPort(12000)
        info.setPortalType_leftWire_importPort(13000)
        info.setPortalType_leftWire_exportPort(14000)
        info.setInterWire_tunnelHead_importPort(15000)
        info.setInterWire_tunnelHead_exportPort(15000)
        info.setInterWire_tunnelBody(160000)
        info.setParallelLine_rightWire(17000)
        info.setParallelLine_leftWire(18000)
        info.setSlopeLine_rightWire(19000)
        info.setSlopeLine_leftWire(20000)
        info.setTunnelLine_rightWire(21000)
        info.setTunnelLine_leftWire(22000)
        info.setConstructionTeamAdvice("23000")
        info.setWeatherCondition("24000")
        info.setTopographicFeatures("25000")
        info.setGeneralSituationFormation("26000")
        info.setBadGeologicalSurvey("27000")
        info.setHydrogeologicalSurvey("28000")
        info.setDynamicParameters("290000")
        info.setShallowBuried("300000")
        info.setPlannedConstruction("310000")
        return info
    }

}

