//package com.tunnel.util
//
//import com.tunnel.controller.TunnelInfoTotalController
//import com.tunnel.domain.*
//import com.tunnel.service.BaseDomainServer
//import jxl.Cell
//import jxl.Sheet
//import jxl.Workbook
//import jxl.format.Alignment
//import jxl.format.VerticalAlignment
//import jxl.write.*
//import org.json.JSONObject
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.stereotype.Component
//import org.springframework.util.StringUtils
//
//import java.beans.BeanInfo
//import java.beans.Introspector
//import java.beans.PropertyDescriptor
//import java.lang.Boolean
//import java.nio.file.Files
//import java.nio.file.Paths
//import java.time.LocalDateTime
//import java.time.format.DateTimeFormatter
//import java.util.concurrent.ConcurrentHashMap
//
///**
// * Created by Ness on 2016/11/2.
// */
//@Component
//class DownloadExcel1 {
//
//
//    @Autowired
//    private BaseDomainServer baseDomainServer;
//
//
//    static ConcurrentHashMap<String, HashMap<String, List<BaseDomain>>> downloadExcelType = new ConcurrentHashMap<>()
//    static Map<String, String> englishToChineseMap = new HashMap<>()
//    static Map<String, String> englishToJsonMap = new HashMap<>()
//    static Map<String, String> classNameToChineseMap = new HashMap<>()
//    static Map<String, String> chineseToEnglishMap = new HashMap<>()
//    static Map<String, String> jsonToEnglishMap = new HashMap<>()
//    static Map<String, String> chineseToClassNameMap = new HashMap<>()
//    static Map<String, String> englishToClassTypeMap = new HashMap<>()
//    static {
//
//        //利用java内省技术加载excel
//        //加载excel的配置
//        Properties properties = new Properties();
//        properties.load(new InputStreamReader(DownloadExcel1.class.getClassLoader().getResourceAsStream("englishToChinese.properties"), "gbk"));
//
//        Enumeration<?> enumeration = properties.propertyNames()
//
//        while (enumeration.hasMoreElements()) {
//            Object key = enumeration.nextElement()
//            englishToChineseMap.put(key, properties.get(key))
//
//        }
//
//        //加载对应的Integer解释的map
//        properties.clear()
//        properties.load(new InputStreamReader(DownloadExcel1.class.getClassLoader().getResourceAsStream("englishToJson.properties"), "utf-8"))
//
//        enumeration = properties.propertyNames()
//
//        while (enumeration.hasMoreElements()) {
//            Object key = enumeration.nextElement()
//            englishToJsonMap.put(key, properties.get(key))
//
//        }
//
//        //加载对应的类英文名解释中文名
//        properties.clear()
//        properties.load(new InputStreamReader(DownloadExcel1.class.getClassLoader().getResourceAsStream("classNameToChinese.properties"), "gbk"))
//
//        enumeration = properties.propertyNames()
//
//        while (enumeration.hasMoreElements()) {
//            Object key = enumeration.nextElement()
//            classNameToChineseMap.put(key, properties.get(key))
//
//        }
//        //加载对应的中文解释英文excel
//        properties.clear()
//        properties.load(new InputStreamReader(DownloadExcel1.class.getClassLoader().getResourceAsStream("chineseToEnglish.properties"), "gbk"))
//
//        enumeration = properties.propertyNames()
//
//        while (enumeration.hasMoreElements()) {
//            Object key = enumeration.nextElement()
//            chineseToEnglishMap.put(key, properties.get(key))
//
//        }
//        //加载对应的map解释Integer的map
//        properties.clear()
//        properties.load(new InputStreamReader(DownloadExcel1.class.getClassLoader().getResourceAsStream("chineseToClassName.properties"), "gbk"))
//
//        enumeration = properties.propertyNames()
//
//        while (enumeration.hasMoreElements()) {
//            Object key = enumeration.nextElement()
//            chineseToClassNameMap.put(key, properties.get(key))
//
//        }
//        //加载对应的json转数字的map
//        properties.clear()
//        properties.load(new InputStreamReader(DownloadExcel1.class.getClassLoader().getResourceAsStream("jsonToEnglish.properties"), "utf-8"))
//
//        enumeration = properties.propertyNames()
//
//        while (enumeration.hasMoreElements()) {
//            Object key = enumeration.nextElement()
//            jsonToEnglishMap.put(key, properties.get(key))
//
//        }
//        //加载对应的json转class类型的map
//        properties.clear()
//        properties.load(new InputStreamReader(DownloadExcel1.class.getClassLoader().getResourceAsStream("englishToClassType.properties"), "gbk"))
//
//        enumeration = properties.propertyNames()
//
//        while (enumeration.hasMoreElements()) {
//            Object key = enumeration.nextElement()
//            englishToClassTypeMap.put(key, properties.get(key))
//
//        }
//    }
//
//
//    public void setDownoadExcel(File tempFile) {
//
//        Workbook workbook = Workbook.getWorkbook(tempFile)
//
//        JSONObject jsonObject
//        Sheet[] sheets = workbook.getSheets()
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm")
//
//        for (Sheet sheet : sheets) {
//            //存储的首行
//            List<BaseDomain> baseDomainList = new ArrayList<>()
//            String className = chineseToClassNameMap.get(sheet.getName())
//            String tunnelNumber = ""
//            int len = sheet.getRows()
//
//            //获取首行
//            String[] titles = new String[sheet.getColumns()]
//            Cell[] cells = sheet.getRow(0);
//            for (int i = 0; i < cells.length; i++) {
//
//                titles[i] = chineseToEnglishMap.get(cells[i].getContents())
//            }
//
//            //数据行
//            for (int j = 1; j < len; j++) {
//
//
//                BaseDomain tempBase = null
//                //判断类型
//                switch (className) {
//
//                    case ClassType.ClassTypeMapping.tunnelImportPort.getKey():
//                        tempBase = new TunnelImportPort()
//                        break;
//
//                    case ClassType.ClassTypeMapping.tunnelShallowCover.getKey():
//                        tempBase = new TunnelShallowCover()
//                        break;
//                    case ClassType.ClassTypeMapping.assistTunnel.getKey():
//                        tempBase = new AssistTunnel()
//                        break;
//                    case ClassType.ClassTypeMapping.tunnelGrabageExamine.getKey():
//                        tempBase = new TunnelGrabageExamine()
//                        break;
//                    case ClassType.ClassTypeMapping.tunnelHeadRiskExamine.getKey():
//                        tempBase = new TunnelHeadRiskExamine()
//                        break;
//                    case ClassType.ClassTypeMapping.overBreakRiskExmaine.getKey():
//                        tempBase = new OverBreakRiskExmaine()
//                        break;
//                    case ClassType.ClassTypeMapping.surgeMudRiskExamine.getKey():
//                        tempBase = new SurgeMudRiskExamine()
//                        break;
//                    case ClassType.ClassTypeMapping.shapeRiskExamine.getKey():
//                        tempBase = new ShapeRiskExamine()
//                        break;
//                    case ClassType.ClassTypeMapping.rockOutburstRiskExamine.getKey():
//                        tempBase = new RockOutburstRiskExamine()
//                        break;
//                    case ClassType.ClassTypeMapping.gasRiskExamine.getKey():
//                        tempBase = new GasRiskExamine()
//                        break;
//
//                    case ClassType.ClassTypeMapping.fireRiskExmaine.getKey():
//                        tempBase = new FireRiskExmaine()
//                        break;
//
//                    case ClassType.ClassTypeMapping.trafficAccidentRiskExamine.getKey():
//                        tempBase = new TrafficAccidentRiskExamine()
//                        break;
//                    case ClassType.ClassTypeMapping.envirRiskExamine.getKey():
//                        tempBase = new EnvirRiskExamine()
//                        break;
//
//                    case ClassType.ClassTypeMapping.otherRiskExamine.getKey():
//                        tempBase = new OtherRiskExamine()
//                        break;
//                    case ClassType.ClassTypeMapping.figure.getKey():
//                        tempBase = new Figure()
//                        break
//                }
//
//                cells = sheet.getRow(j);
//                for (int k = 0; k < cells.length; k++) {
//                    //特殊处理
//
//                    if ("Integer".equals(englishToClassTypeMap.get(titles[k]))) {
//
//                        if (!StringUtils.isEmpty(cells[k].getContents())) {
//                            jsonObject = new JSONObject(jsonToEnglishMap.get(titles[k]))
//                            tempBase.setProperty(titles[k], jsonObject.getInt(cells[k].getContents()))
//                        }
//
//
//                    } else if ("Boolean".equals(englishToClassTypeMap.get(titles[k]))) {
//                        if (!StringUtils.isEmpty(cells[k].getContents())) {
//                            jsonObject = new JSONObject(jsonToEnglishMap.get(titles[k]))
//
//                            tempBase.setProperty(titles[k], jsonObject.getBoolean(cells[k].getContents()))
//                        }
//                    } else if ("LocalDateTime".equals(englishToClassTypeMap.get(titles[k]))) {
//                        LocalDateTime time = LocalDateTime.now()
//                        if (!StringUtils.isEmpty(cells[k].getContents())) {
//                            time = LocalDateTime.parse(cells[k].getContents(), dateTimeFormatter)
//
//                        }
//                        tempBase.setProperty(titles[k], time)
//
//                    } else if ("Double".equals(englishToClassTypeMap.get(titles[k]))) {
//                        if (!StringUtils.isEmpty(cells[k].getContents())) {
//                            tempBase.setProperty(titles[k], Double.valueOf(cells[k].getContents()))
//                        }
//
//                    } else if ("picture".equals(englishToClassTypeMap.get(titles[k]))) {
//                        if (!StringUtils.isEmpty(cells[k].getContents())) {
//                            /*  Image image = sheet.getDrawing(k)
//                              sheet.getDrawing()
//
//                              File file = image.getImageFile()
//
//                              if (file != null) {
//                                  //暂不书写
//                              }*/
//
//                        }
//                    } else {
//                        if (!StringUtils.isEmpty(cells[k].getContents())) {
//                            //记录tunnelNumber
//                            if ("tunnelNumber".equals(titles[k])) {
//                                tunnelNumber = cells[k].getContents()
//                            }
//                            tempBase.setProperty(titles[k], cells[k].getContents())
//                        }
//                    }
//
//
//                }
//                baseDomainList.add(tempBase)
//
//            }
//
//            //存储
//            baseDomainServer.saveBaseDomainAll(baseDomainList)
//            //存储对应关系
//            TunnelExamineRelationQuery relationQuery = new TunnelExamineRelationQuery("tunnelNumber": tunnelNumber, "tableName": className, "createTime": LocalDateTime.now())
//
//            //先查询调查跟表格的关系是否存在
//            if (baseDomainServer.findBaseDomainByBaseDomain(relationQuery) == null) {
//                //添加调查和表格的关系
//                baseDomainServer.saveTunnelExmaineRelationQuery(relationQuery)
//
//            }
//
//
//            Thread.sleep(500)
//
//        }
//
//
//    }
//
//    public byte[] getDownloadExcel(String sessionId, Integer downloadType, String className, BaseDomain baseDomain) {
//
//        List<BaseDomain> list = null;
//        //书写Excel
//        File tempFile = File.createTempFile("start", "end")
//        tempFile.createNewFile()
//        WritableWorkbook workbook = Workbook.createWorkbook(tempFile)
//
//        if (downloadType.equals(DownloadExcel1.downloadWay.DOWNLOAD_PAGE.getValue())) {
//            list = downloadExcelType.get(sessionId).get(className)
//            if (list == null || list.isEmpty()) {
//                tempFile.delete()
//                return null
//            }
//
//            WritableSheet sheet = workbook.createSheet("temp", 0)
//            //单条书写数据
//            writeExcel(list, baseDomain, sheet)
//        } else if (downloadType.equals(DownloadExcel1.downloadWay.DOWNLOAD_ALL.getValue())) {
//            list = baseDomainServer.findBaseDomainALl(baseDomain)
//            if (list == null || list.isEmpty()) {
//                tempFile.delete()
//                return null
//            }
//
//            WritableSheet sheet = workbook.createSheet("temp", 0)
//            //单条书写数据
//            writeExcel(list, baseDomain, sheet)
//        } else if (downloadType.equals(DownloadExcel1.downloadWay.DOWNLOAD_QUERY_ALL.getValue())) {
//
//            User user = baseDomain
//            //分开书写
//            String[] classNames = user.getPassKey().split(":")[1].split(",")
//
//            String tunnelNumber = user.getPassKey().split(":")[0].trim()
//            WritableSheet sheet
//            BaseDomain tempBase
//            int k = 0
//            for (String str : classNames) {
//                if (StringUtils.isEmpty(str)) {
//                    continue
//                }
//                switch (str) {
//                    case ClassType.ClassTypeMapping.tunnelImportPort.getKey():
//                        tempBase = new TunnelImportPort("tunnelNumber": tunnelNumber, "page": 0)
//                        sheet = workbook.createSheet(classNameToChineseMap.get("tunnelImportPort"), k++)
//                        break;
//                    case ClassType.ClassTypeMapping.tunnelShallowCover.getKey():
//                        tempBase = new TunnelShallowCover("tunnelNumber": tunnelNumber, "page": 0)
//                        sheet = workbook.createSheet(classNameToChineseMap.get("tunnelShallowCover"), k++)
//                        break;
//                    case ClassType.ClassTypeMapping.assistTunnel.getKey():
//                        tempBase = new AssistTunnel("tunnelNumber": tunnelNumber, "page": 0)
//                        sheet = workbook.createSheet(classNameToChineseMap.get("assistTunnel"), k++)
//                        break;
//                    case ClassType.ClassTypeMapping.tunnelGrabageExamine.getKey():
//                        tempBase = new TunnelGrabageExamine("tunnelNumber": tunnelNumber, "page": 0)
//                        sheet = workbook.createSheet(classNameToChineseMap.get("tunnelGrabageExamine"), k++)
//                        break;
//
//                    case ClassType.ClassTypeMapping.tunnelHeadRiskExamine.getKey():
//                        tempBase = new TunnelHeadRiskExamine("tunnelNumber": tunnelNumber, "page": 0)
//                        sheet = workbook.createSheet(classNameToChineseMap.get("tunnelHeadRiskExamine"), k++)
//                        break;
//
//                    case ClassType.ClassTypeMapping.overBreakRiskExmaine.getKey():
//                        tempBase = new OverBreakRiskExmaine("tunnelNumber": tunnelNumber, "page": 0)
//                        sheet = workbook.createSheet(classNameToChineseMap.get("overBreakRiskExmaine"), k++)
//                        break;
//                    case ClassType.ClassTypeMapping.surgeMudRiskExamine.getKey():
//                        tempBase = new SurgeMudRiskExamine("tunnelNumber": tunnelNumber, "page": 0)
//                        sheet = workbook.createSheet(classNameToChineseMap.get("surgeMudRiskExamine"), k++)
//                        break;
//                    case ClassType.ClassTypeMapping.shapeRiskExamine.getKey():
//                        tempBase = new ShapeRiskExamine("tunnelNumber": tunnelNumber, "page": 0)
//                        sheet = workbook.createSheet(classNameToChineseMap.get("shapeRiskExamine"), k++)
//                        break;
//                    case ClassType.ClassTypeMapping.rockOutburstRiskExamine.getKey():
//                        tempBase = new RockOutburstRiskExamine("tunnelNumber": tunnelNumber, "page": 0)
//                        sheet = workbook.createSheet(classNameToChineseMap.get("rockOutburstRiskExamine"), k++)
//                        break;
//                    case ClassType.ClassTypeMapping.gasRiskExamine.getKey():
//                        tempBase = new GasRiskExamine("tunnelNumber": tunnelNumber, "page": 0)
//                        sheet = workbook.createSheet(classNameToChineseMap.get("gasRiskExamine"), k++)
//                        break;
//                    case ClassType.ClassTypeMapping.fireRiskExmaine.getKey():
//                        tempBase = new FireRiskExmaine("tunnelNumber": tunnelNumber, "page": 0)
//                        sheet = workbook.createSheet(classNameToChineseMap.get("fireRiskExmaine"), k++)
//                        break;
//                    case ClassType.ClassTypeMapping.trafficAccidentRiskExamine.getKey():
//                        tempBase = new TrafficAccidentRiskExamine("tunnelNumber": tunnelNumber, "page": 0)
//                        sheet = workbook.createSheet(classNameToChineseMap.get("trafficAccidentRiskExamine"), k++)
//                        break;
//                    case ClassType.ClassTypeMapping.envirRiskExamine.getKey():
//                        tempBase = new EnvirRiskExamine("tunnelNumber": tunnelNumber, "page": 0)
//                        sheet = workbook.createSheet(classNameToChineseMap.get("envirRiskExamine"), k++)
//                        break;
//
//                    case ClassType.ClassTypeMapping.otherRiskExamine.getKey():
//                        tempBase = new OtherRiskExamine("tunnelNumber": tunnelNumber, "page": 0)
//                        sheet = workbook.createSheet(classNameToChineseMap.get("otherRiskExamine"), k++)
//                        break;
//                    case ClassType.ClassTypeMapping.figure.getKey():
//                        tempBase = new Figure("tunnelNumber": tunnelNumber, "page": 0)
//                        sheet = workbook.createSheet(classNameToChineseMap.get("figure"), k++)
//                }
//                list = baseDomainServer.findBaseDomainALl(tempBase)
//                if (list == null || list.isEmpty()) {
////                    k--
//                    continue
//                }
//                writeExcel(list, tempBase, sheet)
//            }
//
//        }
//
//        workbook.write();
//        workbook.close();
//        byte[] bytes = Files.readAllBytes(Paths.get(tempFile.getPath()))
//        tempFile.delete()
//        return bytes
//    }
//
//    private void writeExcel(List<BaseDomain> list, BaseDomain baseDomain, WritableSheet sheet) {
//
//
//        WritableCellFormat format = new WritableCellFormat();
//
//        format.setAlignment(Alignment.CENTRE);
//        format.setVerticalAlignment(VerticalAlignment.CENTRE);
//        Label label = null
//
//        int length = list.size()
//
//        //json转换的类型
//        JSONObject jsonObject = null
//        String value = null
//        //判断是否进行绘画的处理
//        Boolean pictureFlag = false
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm")
//
//        for (int i = 0; i < length; i++) {
//
//
//            baseDomain = list.get(i)
//
//            BeanInfo beanInfo = Introspector.getBeanInfo(baseDomain.getClass())
//            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors()
//            int j = 0;
//            int k = 0;
//
//
//            for (PropertyDescriptor property : propertyDescriptors) {
//
//                //排除自带的属性
//                if ("class".equals(property.getName()) || "metaClass".equals(property.getName()) || "id".equals(property.getName()) || "page".equals(property.getName())) {
//                    continue
//                }
//
//                //填充栏目
//                if (i == 0) {
//                    //首次需要填充到Excel第一栏
//                    String title = englishToChineseMap.get(property.getName())
//
//                    label = new Label(j, i, title, format)
//                    j++
//                    sheet.addCell(label)
//                }
//
//                //加载图片的需要特殊处理
//                if ("photosPicture".equals(property.getName()) || "acccessTunnelAndAirshaftRelationWithMainTunnel".equals(property.getName())) {
//
//                    String paths = property.getReadMethod().invoke(baseDomain).toString()
//                    if (StringUtils.isEmpty(paths)) {
//                        k++
//                        continue
//                    }
//
//                    paths = paths.replaceAll("[^\\d+]", "")
//
//                    pictureFlag = true
//                    int len = paths.length() / 15
//
//                    for (int y = 0; y < len; y++) {
//                        String str = paths.substring(y * 15, (y + 1) * 15)
//
//                        if (str == null || str.trim().length() == 0) {
//                            continue
//                        }
//                        char[] names = baseDomain.class.getSimpleName().toCharArray()
//                        names[0] += 32
//                        String path = new String(names)
//
//                        File file = new File(TunnelInfoTotalController.savepath + path, str + ".png")
//                        if (!file.exists()) {
//                            continue
//                        }
//                        WritableImage image = new WritableImage(k, i + 1, 2, 2, file);
//                        sheet.addImage(image)
//                        sheet.setColumnView(k, 50)
//                    }
//                    k++
//
//                    continue
//                }
//
//
//
//                println property.getName()
//                println property.getPropertyType()
//                println property.getReadMethod().invoke(baseDomain)
//                if (property.getPropertyType() == Integer.class || property.getPropertyType() == Boolean.class) {
//
//                    jsonObject = new JSONObject(englishToJsonMap.get(property.getName()))
//                    value = jsonObject.getString(property.getReadMethod().invoke(baseDomain).toString())
//                } else if (property.getPropertyType() == LocalDateTime.class) {
//                    LocalDateTime time = property.getReadMethod().invoke(baseDomain)
//                    if (time != null) {
//                        value = time.format(dateTimeFormatter)
//                    } else {
//                        value = ""
//                    }
//
//                } else {
//
//                    value = property.getReadMethod().invoke(baseDomain)
//                }
//
//                label = new Label(k, i + 1, value, format)
//
//                sheet.addCell(label)
//
//                //日期和图片和图片格式化
//                if (pictureFlag || property.getPropertyType() == LocalDateTime.class) {
//                    sheet.setColumnView(k, 20)
//                }
//
//                k++
//
//            }
//
//        }
//
//
//    }
//
//
//    public static void main(String[] args) {
//
//
//
//    }
//
//
//    enum downloadWay {
//        DOWNLOAD_PAGE(0, "分页下载"),
//        DOWNLOAD_ALL(1, "全部下载"),
//        DOWNLOAD_QUERY_ALL(2, "各个项目全部下载");
//        int key;
//        String value;
//
//        downloadWay(int key, String value) {
//            this.key = key
//            this.value = value
//        }
//
//        public int getValue() {
//            return key
//
//        }
//    }
//
//
//}
//
