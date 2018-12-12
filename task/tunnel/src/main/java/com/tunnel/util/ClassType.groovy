package com.tunnel.util

/**
 * 前端上传类的映射
 * Created by Ness on 2016/10/18.
 */
class ClassType {

    /**
     * 类的名字
     */
    String className

    /**
     * 隧道的编号
     */
    String tunnelNumber

    /**
     * excel名称
     */
    String excelName

    /**
     * 上传表格的实体类的类型映射
     */
    enum ClassTypeMapping {
        tunnelImportPort("tunnelImportPort", com.tunnel.domain.TunnelImportPort.class),
        tunnelShallowCover("tunnelShallowCover", com.tunnel.domain.TunnelShallowCover.class),
        assistTunnel("assistTunnel", com.tunnel.domain.AssistTunnel.class),
        tunnelGrabageExamine("tunnelGrabageExamine", com.tunnel.domain.TunnelGrabageExamine.class),
        tunnelHeadRiskExamine("tunnelHeadRiskExamine", com.tunnel.domain.TunnelHeadRiskExamine.class),
        overBreakRiskExmaine("overBreakRiskExmaine", com.tunnel.domain.OverBreakRiskExmaine.class),
        surgeMudRiskExamine("surgeMudRiskExamine", com.tunnel.domain.SurgeMudRiskExamine.class),
        shapeRiskExamine("shapeRiskExamine", com.tunnel.domain.ShapeRiskExamine.class),
        rockOutburstRiskExamine("rockOutburstRiskExamine", com.tunnel.domain.RockOutburstRiskExamine.class),
        gasRiskExamine("gasRiskExamine", com.tunnel.domain.GasRiskExamine.class),
        fireRiskExmaine("fireRiskExmaine", com.tunnel.domain.FireRiskExmaine.class),
        trafficAccidentRiskExamine("trafficAccidentRiskExamine", com.tunnel.domain.TrafficAccidentRiskExamine.class),
        envirRiskExamine("envirRiskExamine", com.tunnel.domain.EnvirRiskExamine.class),
        otherRiskExamine("otherRiskExamine", com.tunnel.domain.OtherRiskExamine.class),
        figure("figure", com.tunnel.domain.Figure.class);
        private String key;
        private Class value;

        ClassTypeMapping(String key, Class value) {
            this.key = key
            this.value = value
        }

        public String getKey() {
            return key

        }

        public Class getValue() {
            return value
        }

    }


}
