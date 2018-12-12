package com.finace.miscroservice.commons.utils;

import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 扫描配置类
 */
public class Scanner {

    private static final String SCANNER_CLASS_KEY = "scanner:class:key";
    private static final String SCANNER_FILE_KEY = "scanner:file:key";
    private static final String SUFFIX_NAME = ".java";
    private static final String TMP_FILE_PATH = System.getProperty("java.io.tmpdir") + File.separatorChar + "java" + File.separatorChar;
    private static final String JAR_SAVE_FILE = System.getProperty("java.io.tmpdir") + File.separatorChar + "scanner" + File.separatorChar;

    static {
        File file = new File(JAR_SAVE_FILE);
        if (!file.exists()) {
            file.mkdirs();
        }

        file = new File(TMP_FILE_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }

    }

    private static Boolean WINDOWS_SYSTEM_FLAG = Boolean.TRUE;


    /**
     * 生成文档
     *
     * @throws IOException
     */
    public static void getDoc() throws IOException {


        if (!System.getProperty("os.name").toLowerCase().contains("windows")) {
            WINDOWS_SYSTEM_FLAG = Boolean.FALSE;
        }

        Set<Class<?>> allAnnotations = new HashSet<>();
        allAnnotations.add(RestController.class);
        List<File> files = scannerFile("com.finace.miscroservice", allAnnotations);
        if (files.isEmpty()) {
            return;
        }
        StringBuilder command = new StringBuilder();
        for (File file : files) {

            String tmpFilePath;
            //更换渲染的文件
            if (WINDOWS_SYSTEM_FLAG) {
                tmpFilePath = normalTemplete2CustomerTemplete(new File(file.getAbsolutePath().replaceAll("out\\\\production\\\\classes\\\\", "src\\\\main\\\\java\\\\")));
            } else {
                tmpFilePath = normalTemplete2CustomerTemplete(new File(file.getAbsolutePath().replaceAll("out/production/classes/", "src/main/java/")));
            }


            command.append(tmpFilePath);
            command.append(" ");
        }

        String start = "";
        String docFilesName = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        if (WINDOWS_SYSTEM_FLAG) {
            start = "cmd /k  ";
            docFilesName = docFilesName.substring(1);
        }

        File docFile = new File(docFilesName.replaceAll("build/classes/main|out/production/classes/", "src/main/resources") + "/static");
        if (!docFile.exists()) {
            docFile.mkdirs();
        }

        Runtime.getRuntime().exec(start + "javadoc -d  " + docFile.getAbsolutePath() + "  " + command.toString() + " -windowtitle 一桶金开发文档 -encoding utf-8 -charset utf-8 --allow-script-in-comments ");


        //拷贝 即刻生效
        File destinationFiles = new File(docFilesName.replace("classes/", "resources/static"));
        if (!destinationFiles.exists()) {
            destinationFiles.mkdirs();
        }
        String cmd;
        if (WINDOWS_SYSTEM_FLAG) {
            cmd = "cmd /k   xcopy \"" + docFile.getAbsolutePath() + "\"  \"" + destinationFiles.getAbsolutePath() + "\" /S /y";

        } else {
            cmd = "cp -r " + docFile.getAbsolutePath() + " " + destinationFiles.getAbsolutePath().replace("/static", "");
        }


        new Thread(() -> {
            try {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {

                }

                Runtime.getRuntime().exec(cmd);
            } catch (IOException e) {

            }

        }).start();


    }


    /**
     * 正常的模板转换渲染的模板
     *
     * @param file 需要修改的文件
     * @return
     * @throws IOException
     */
    private static String normalTemplete2CustomerTemplete(File file) throws IOException {

        List<String> strings = Files.readAllLines(file.toPath());
        StringBuilder allLines = new StringBuilder();

        String line;

        String START_SIGN = "**";
        String FILTER_SIGN = "*";
        String END_SIGN = "/";
        String PARAM_SIGN = " @param";
        String NO_PARAM_SIGN = " <p>";
        String RETURN_SIGN = " @return";


        String paramFormat = "\n* @param  ------------------\n * <table>\n   * <tr><th style=\"text-align:left\">请求参数</th> <th style=\"text-align:left\">是否必填</th> <th style=\"text-align:left\">说明</th> </tr>\n  %s\n  * </table>\n * <p>";
        String paramRepFormat = "\n* <tr><td style=\"text-align:left\"> {0} </td> <td style=\"text-align:left\">%s</td> <td style=\"text-align:left\">{1}</td> </tr>";

        StringBuilder variablesLine = new StringBuilder();
        StringBuilder paramBuild = new StringBuilder();
        StringBuilder paramRequired = new StringBuilder();
        StringBuilder others = new StringBuilder();
        StringBuilder tmpMethod = new StringBuilder();

        List<String> paramRequiredList = new ArrayList<>();
        List<String> methodArr = new ArrayList<>();

        Boolean paramEndFlag = Boolean.FALSE;
        Boolean START_FLAG = Boolean.FALSE;
        Boolean paramSelectFlag = Boolean.FALSE;
        Boolean noParamFlag = Boolean.FALSE;
        Boolean paramHasReplaceFlag = Boolean.FALSE;

        Boolean isFirstCommint = Boolean.TRUE;

        int paramRequiredIndex = 0;

        for (String str : strings) {
            line = str.trim();
            line = line.length() > 1 ? line.substring(1) : line;

            if (line.startsWith(START_SIGN)) {
                if (isFirstCommint) {
                    allLines.append(str);
                    allLines.append("\n");
                } else {
                    START_FLAG = Boolean.TRUE;
                }
                continue;
            } else if (line.equals(END_SIGN)) {

                if (isFirstCommint) {
                    allLines.append(str);
                    allLines.append("\n");
                    isFirstCommint = Boolean.FALSE;
                    continue;
                }


                if (!paramHasReplaceFlag) {
                    //参数结束
                    paramEndFlag = Boolean.TRUE;
                    if (paramBuild.length() != 0) {
                        //组合参数
                        variablesLine.append(String.format(paramFormat, paramBuild.toString()));
                    }
                }


                START_FLAG = Boolean.FALSE;
                continue;

            } else if (line.equals(FILTER_SIGN)) {
                continue;
            } else if (line.equals(NO_PARAM_SIGN)) {
                noParamFlag = Boolean.TRUE;
                continue;
            } else if (paramSelectFlag) {
                tmpMethod.append(str);
                tmpMethod.append("\n");


                if (line.endsWith("{")) {


                    paramRequired.append(str.substring(0, str.lastIndexOf(")")));

                    String params = paramRequired.toString().replaceAll("\",", " ");


                    for (String s : params.split(",")) {
                        if (s.toLowerCase().contains("false")) {
                            paramRequiredList.add("否");
                        } else {
                            paramRequiredList.add("是");
                        }
                    }

                    //进行再次格式化
                    variablesLine.append("\n");
                    if (others.length() != 0) {
                        if (noParamFlag) {
                            variablesLine.append("\n*@param \"\" \"\"<br/>");
                        }
                        variablesLine.append("\n * <span style=\"color:red\">举例说明：</span>\n  * <p> \n<br/>\n");
                        variablesLine.append(others.toString());
                        variablesLine.append("\n\n");
                    }

                    String result = String.format(MessageFormat.format(variablesLine.toString(), methodArr.toArray()), paramRequiredList.toArray());


                    //添加自定义的修改方法
                    allLines.append("/**\n");
                    allLines.append(result);
                    allLines.append("*/\n");

                    allLines.append(tmpMethod.toString());

                    //所有进行初始化 进行下一轮
                    paramRequired.delete(0, paramRequired.length());
                    variablesLine.delete(0, variablesLine.length());
                    paramBuild.delete(0, paramBuild.length());
                    others.delete(0, others.length());
                    tmpMethod.delete(0, tmpMethod.length());

                    paramRequiredList.clear();
                    methodArr.clear();
                    START_FLAG = Boolean.FALSE;
                    paramEndFlag = Boolean.FALSE;
                    paramSelectFlag = Boolean.FALSE;
                    paramHasReplaceFlag = Boolean.FALSE;
                    noParamFlag = Boolean.FALSE;

                    paramRequiredIndex = 0;

                } else {
                    if (paramRequiredIndex == 0) {
                        paramRequired.append(str.substring(str.indexOf("(") + 1));
                    } else {
                        paramRequired.append(str);
                    }
                }

                paramRequiredIndex++;
            } else {
                if (!(str.trim().startsWith("*") || (line.startsWith("GetMapping") || line.startsWith("RequestMapping")
                        || line.startsWith("PostMapping"))) || isFirstCommint) {
                    if (!str.trim().startsWith("@")) {
                        allLines.append(str);
                        allLines.append("\n");

                    }
                }
            }


            if (START_FLAG) {
                //方法
                if (variablesLine.length() == 0) {

                    variablesLine.append("\n * <p>\n * <span>API说明：<a style=\"color:blue\">");
                    variablesLine.append(line);
                    variablesLine.append("</a></span> \n  * <br/>\n * <span>请求方式：<a style = \"color:blue\">{0}");
                    variablesLine.append("</a></span>\n * <br/>\n * <span>URL地址： <a href=\"javascript:void(0)\">{1}</a></span>\n * <br/>\n  * </p >\n ");


                } else {

                    if (paramEndFlag || noParamFlag) {
                        //排除return 的语句
                        if (!line.equals(RETURN_SIGN)) {

                            others.append(line.replaceAll("\\{", "'{'"));
                            others.append("<br/>");
                        }

                    } else {

                        //param 优化
                        if (line.startsWith(PARAM_SIGN)) {


                            StringTokenizer tokenizer = new StringTokenizer(line.split(PARAM_SIGN)[1], " ");

                            List<String> _repSign = new ArrayList<>();
                            while (tokenizer.hasMoreTokens()) {
                                _repSign.add(tokenizer.nextToken());
                            }


                            paramBuild.append(MessageFormat.format(paramRepFormat, _repSign.toArray()));

                        } else {

                            //排除return 的语句
                            if (!line.equals(RETURN_SIGN)) {

                                others.append(line.replaceAll("\\{", "'{'"));
                                others.append("<br/>");
                            }
                            //参数结束
                            paramEndFlag = Boolean.TRUE;
                            paramHasReplaceFlag = Boolean.TRUE;

                            if (paramBuild.length() != 0) {
                                //组合参数
                                variablesLine.append(String.format(paramFormat, paramBuild.toString()));
                            }
                        }


                    }


                }

            } else {


                //方法
                if (((line.startsWith("GetMapping") || line.startsWith("RequestMapping")
                        || line.startsWith("PostMapping"))) && variablesLine.length() != 0) {
                    if (line.toLowerCase().contains("get")) {
                        methodArr.add("GET");
                    } else if (line.toLowerCase().contains("post")) {

                        methodArr.add("POST");
                    } else {

                        methodArr.add("POST/GET(皆可)");
                    }
                    //路径
                    methodArr.add(line.split("\"")[1]);

                    //下一个必然是参数的可选映射
                    paramSelectFlag = Boolean.TRUE;

                }


            }


        }

        File _tmpSaveFile = new File(TMP_FILE_PATH + file.getName());

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(_tmpSaveFile)))) {

            writer.write(allLines.toString());
        }


        return _tmpSaveFile.getAbsolutePath();

    }


    /**
     * 扫描类
     *
     * @param packageName    包路径
     * @param allAnnotations 需要扫描携带的注解类的包
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */

    private static List<File> scannerFile(String packageName, Set<Class<?>> allAnnotations) {
        try {
            return (List<File>) scanner(packageName, allAnnotations).get(SCANNER_FILE_KEY);
        } catch (Exception e) {
            throw new RuntimeException("解析文件类出错" + e);
        }
    }


    /**
     * 扫描类
     *
     * @param packageName    包路径
     * @param allAnnotations 需要扫描携带的注解类的包
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */

    private static Set<Class<?>> scannerClass(String packageName, Set<Class<?>> allAnnotations) {
        try {
            return (Set<Class<?>>) scanner(packageName, allAnnotations).get(SCANNER_CLASS_KEY);

        } catch (Exception e) {
            throw new RuntimeException("解析类包出错" + e);
        }

    }


    /**
     * 扫描包
     *
     * @param packageName    包路径
     * @param allAnnotations 需要扫描携带的注解类的包
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */

    private static Map<String, Object> scanner(String packageName, Set<Class<?>> allAnnotations) throws IOException, ClassNotFoundException {

        Map<String, Object> resultMap = new HashMap<>();

        Set<Class<?>> allClasses = new HashSet<>();

        List<File> allAccordFiles = new ArrayList<>();


        if (packageName == null || packageName.isEmpty()) {
            resultMap.put(SCANNER_CLASS_KEY, allClasses);
            return resultMap;
        }

        String scannerPackageName = packageName.replaceAll("/", ".");

        Enumeration<URL> resource = Thread.currentThread().getContextClassLoader().getResources(packageName.replaceAll("\\.", "/"));
        URL url;
        String protocol;


        while (resource.hasMoreElements()) {
            url = resource.nextElement();
            if (url == null) {
                continue;
            }
            protocol = url.getProtocol();
            if ("file".equals(protocol)) {

                String baseFile;
                //windows处理
                if (WINDOWS_SYSTEM_FLAG) {
                    baseFile = url.getFile().substring(1);
                } else {
                    //默认unix
                    baseFile = url.getFile();
                }

                parseClasses(scannerPackageName, baseFile, allClasses, allAccordFiles, allAnnotations);
            } else if ("jar".equals(protocol)) {
                parseJarClasses((JarURLConnection) url.openConnection(), allClasses, allAccordFiles, allAnnotations);
            }


        }
        resultMap.put(SCANNER_CLASS_KEY, allClasses);
        resultMap.put(SCANNER_FILE_KEY, allAccordFiles);

        return resultMap;

    }


    /**
     * 解析jar的classes
     *
     * @param jarURLConnection      jar的连接池
     * @param originalClasses       原始的classes类
     * @param originalAccordFiles   原始的符合的类
     * @param parseSpecialClassType 解析指定的类型
     * @return
     * @throws ClassNotFoundException
     */
    private static Set<Class<?>> parseJarClasses(JarURLConnection jarURLConnection, Set<Class<?>> originalClasses, List<File> originalAccordFiles, Set<Class<?>> parseSpecialClassType) throws ClassNotFoundException, IOException {

        Set<Class<?>> _allClasses;

        List<File> _allAccordFiles;


        if (originalClasses != null) {
            _allClasses = originalClasses;
        } else {
            _allClasses = new HashSet<>();
        }

        if (originalAccordFiles != null) {
            _allAccordFiles = originalAccordFiles;
        } else {
            _allAccordFiles = new ArrayList<>();

        }


        if (jarURLConnection == null) {
            return _allClasses;
        }

        JarFile jarFile = jarURLConnection.getJarFile();
        Enumeration<JarEntry> entries = jarFile.entries();

        JarEntry jarEntry;
        String jarName;
        while (entries.hasMoreElements()) {
            jarEntry = entries.nextElement();
            if (jarEntry == null) {
                continue;
            }
            jarName = jarEntry.getName();
            if (jarName.endsWith(".class")) {


                int _index = jarName.lastIndexOf("/");


                String fileName = jarName.substring(_index + 1, jarName.lastIndexOf("."));


                if (addClasses(loadClasses(jarName.substring(0, _index).replaceAll("/", "."), fileName),
                        _allClasses, parseSpecialClassType)) {

                    Files.copy(jarFile.getInputStream(jarEntry), Paths.get(JAR_SAVE_FILE + fileName + SUFFIX_NAME), StandardCopyOption.REPLACE_EXISTING);
                    _allAccordFiles.add(new File(JAR_SAVE_FILE + fileName + SUFFIX_NAME));

                }


            }


        }

        return _allClasses;


    }


    /**
     * 解析classes
     *
     * @param scannerPackagerName   原始扫包路径
     * @param parsePackagerName     子解析的路径
     * @param originalClasses       原始的classes类
     * @param originalAccordFiles   原始的符合的类
     * @param parseSpecialClassType 解析指定的类型
     * @return
     * @throws ClassNotFoundException
     */
    private static Set<Class<?>> parseClasses(String scannerPackagerName, String parsePackagerName, Set<Class<?>> originalClasses, List<File> originalAccordFiles, Set<Class<?>> parseSpecialClassType) throws ClassNotFoundException {
        Set<Class<?>> _allClasses;

        if (originalClasses != null) {
            _allClasses = originalClasses;
        } else {
            _allClasses = new HashSet<>();
        }


        List<File> _allAccordFiles;


        if (originalAccordFiles != null) {
            _allAccordFiles = originalAccordFiles;
        } else {
            _allAccordFiles = new ArrayList<>();

        }


        if (parsePackagerName == null || parsePackagerName.isEmpty() || scannerPackagerName == null || scannerPackagerName.isEmpty()) {
            return _allClasses;
        }
        File[] files = new File(parsePackagerName).listFiles(x -> x.isFile() && !x.getName().contains("$") && x.getName().endsWith("class") || x.isDirectory());

        for (File file : files) {
            if (file.isFile()) {
                if (addClasses(loadClasses(scannerPackagerName, file.getName().split("\\.")[0]), _allClasses, parseSpecialClassType)) {

                    _allAccordFiles.add(new File(file.getAbsolutePath().replaceAll("build\\\\classes\\\\main", "src\\\\main\\\\java").split("\\.")[0] + SUFFIX_NAME));


                }

            } else {

                parseClasses(scannerPackagerName + "." + file.getName().split("\\.")[0], file.getPath(), _allClasses, originalAccordFiles, parseSpecialClassType);
            }


        }


        return _allClasses;
    }


    /**
     * 加载class
     *
     * @param scannerPackageName 扫包的路径
     * @param fileName           包的名称
     * @return
     * @throws ClassNotFoundException
     */
    private static Class<?> loadClasses(String scannerPackageName, String fileName) throws ClassNotFoundException {
        return Class.forName(scannerPackageName + "." + fileName);
    }


    /**
     * 添加classes
     *
     * @param aClass                需要添加的classes
     * @param allClasses            需要添加的classes类集合
     * @param parseSpecialClassType 解析指定的类型
     * @param aClass
     */
    private static boolean addClasses(Class<?> aClass, Set<Class<?>> allClasses, Set<Class<?>> parseSpecialClassType) {

        boolean _addSuccess = false;

        if (parseSpecialClassType == null) {
            allClasses.add(aClass);
            _addSuccess = true;
        } else {
            Annotation annotation;
            for (Class cl : parseSpecialClassType) {
                annotation = aClass.getAnnotation(cl);

                if (annotation != null) {
                    allClasses.add(aClass);
                    _addSuccess = true;
                    break;
                }

            }
        }


        return _addSuccess;

    }

}