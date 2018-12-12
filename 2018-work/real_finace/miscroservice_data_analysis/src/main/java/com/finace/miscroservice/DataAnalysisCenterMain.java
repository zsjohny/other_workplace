package com.finace.miscroservice;


import com.finace.miscroservice.commons.annotation.ServiceStart;
import com.finace.miscroservice.commons.utils.ApplicationContextUtil;
import zipkin.server.EnableZipkinServer;

@ServiceStart

@EnableZipkinServer
public class DataAnalysisCenterMain {

    public static void main(String[] args) {
        ApplicationContextUtil.run(DataAnalysisCenterMain.class, args);

    }


}
