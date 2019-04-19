package com.jiuy.base.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * @author 艾成松 解析xml 返回一个map
 **/
@SuppressWarnings("rawtypes")
public class XmlUtil {

	@SuppressWarnings("unchecked")
	public static Map<String, Object> Dom2Map(Document doc) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (doc == null)
			return map;
		Element root = doc.getRootElement();
		Iterator iterator = root.elementIterator();
		for (; iterator.hasNext();) {
			Element e = (Element) iterator.next();
			// System.out.println(e.getName());
			List list = e.elements();
			String name = e.getName();
			if (list.size() > 0) {
				if (map.keySet().contains(name)) {
					Object ob = map.get(name);
					if (List.class.isAssignableFrom(ob.getClass())) {
						ob = ((List) ob).add(Dom2Map(e));
					} else {
						List ls = new ArrayList<Object>();
						ls.add(ob);
						ls.add(Dom2Map(e));
						map.put(name, ls);
					}
				} else {
					map.put(name, Dom2Map(e));
				}
			} else
				map.put(name, e.getText());
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	public static Map Dom2Map(Element e) {

		Map map = new HashMap();
		List list = e.elements();
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Element iter = (Element) list.get(i);
				List mapList = new ArrayList();

				if (iter.elements().size() > 0) {
					Map m = Dom2Map(iter);
					if (map.get(iter.getName()) != null) {
						Object obj = map.get(iter.getName());
						if (!obj.getClass().getName().equals("java.util.ArrayList")) {
							mapList = new ArrayList();
							mapList.add(obj);
							mapList.add(m);
						}
						if (obj.getClass().getName().equals("java.util.ArrayList")) {
							mapList = (List) obj;
							mapList.add(m);
						}
						map.put(iter.getName(), mapList);
					} else
						map.put(iter.getName(), m);
				} else {
					if (map.get(iter.getName()) != null) {
						Object obj = map.get(iter.getName());
						if (!obj.getClass().getName().equals("java.util.ArrayList")) {
							mapList = new ArrayList();
							mapList.add(obj);
							mapList.add(iter.getText());
						}
						if (obj.getClass().getName().equals("java.util.ArrayList")) {
							mapList = (List) obj;
							mapList.add(iter.getText());
						}
						map.put(iter.getName(), mapList);
					} else
						map.put(iter.getName(), iter.getText());
				}
			}
		} else
			map.put(e.getName(), e.getText());
		return map;
	}
	
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> Dom2MapItem(Document doc){  
        Map<String, Object> map = new HashMap<String, Object>();  
        if(doc == null)  
            return map;  
        Element root = doc.getRootElement();  
        Iterator iterator = root.elementIterator();
        for (; iterator.hasNext();) {  
            Element e = (Element) iterator.next();  
            //System.out.println(e.getName());  
            List list = e.elements(); 
            String name = e.getName();
            if(list.size() > 0){  
                if(map.keySet().contains(name)){
                	Object ob = map.get(name);
                	if(ob.getClass().isAssignableFrom(ArrayList.class)){
                		ob = ((List)ob).add(Dom2MapItem(e));
                	}else{
                		List ls = new ArrayList<Object>();
                		ls.add(ob);
                		ls.add(Dom2MapItem(e));
                		map.put(name, ls);
                	}
                }else{
                	Map m =  Dom2MapItem(e);
                	m.putAll(iterToMap(e));
                	map.put(name,m);
                }
            }else {
                map.put(name, iterToMap(e));  
            }
            	
        }  
        return map;  
    }  
	
	@SuppressWarnings("unchecked")
	public static Map Dom2MapItem(Element e){  
       
		Map map = new HashMap();  
        List list = e.elements();  
        if(list.size() > 0){  
            for (int i = 0;i < list.size(); i++) {  
                Element iter = (Element) list.get(i);  
                List mapList = new ArrayList();  
                  
                if(iter.elements().size() > 0){  
                    Map m = Dom2MapItem(iter);  
                    if(map.get(iter.getName()) != null){  
                        Object obj = map.get(iter.getName());  
                        if(!obj.getClass().getName().equals("java.util.ArrayList")){  
                            mapList = new ArrayList();  
                            mapList.add(obj);  
                            mapList.add(m);  
                        }  
                        if(obj.getClass().getName().equals("java.util.ArrayList")){  
                            mapList = (List) obj;  
                            mapList.add(m);  
                        }  
                        map.put(iter.getName(), mapList);  
                    }else  
                        map.put(iter.getName(), m);  
                }  
                else{  
                    if(map.get(iter.getName()) != null){  
                        Object obj = map.get(iter.getName());  
                        if(!obj.getClass().getName().equals("java.util.ArrayList")){  
                            mapList = new ArrayList();  
                            mapList.add(obj);  
                            mapList.add(iterToMap(iter));  
                        }  
                        if(obj.getClass().getName().equals("java.util.ArrayList")){  
                            mapList = (List) obj;  
                            mapList.add(iterToMap(iter));  
                        }  
                        map.put(iter.getName(), mapList);  
                    }else{
                    	map.put(iter.getName(), iterToMap(iter));  
                    }    
                }  
            }  
        }else{
        	map.put(e.getName(), iterToMap(e));  
        }
            
        return map;  
    }  
	
	
	@SuppressWarnings("unchecked")
	public static Map iterToMap(Element iter){
		Map map = new HashMap();
		List<Attribute> attrs = iter.attributes();
		for(Attribute attr: attrs){
			map.put(attr.getName(), attr.getValue());
		}
		map.put("text", iter.getText().trim());
		return map;
	}

	
	public static Map xml2MapItem(String xml){
		try {
			Document db =  DocumentHelper.parseText(xml);  
			return Dom2MapItem(db);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static void main(String[] args) {

		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><DATASET><HEAD><MSG_ID>YXXT_20170111001349280_0001</MSG_ID><NAME>主数据管理系统推送发布或停用数据</NAME><SOURCE>MD</SOURCE><TARGET>sjyx,hyzm,wlgk,zmxt,abxt,wxms,gis</TARGET><OPERATION_IDNY>UPDATE</OPERATION_IDNY><MSG_TYPE>3</MSG_TYPE><MDM_IDNY>CSTR</MDM_IDNY><RT_CODE /><RT_DESC /><WS_MARK /><WS_METHOD /><WS_DESC /><CURR_TIME>2017-01-11 00:19:24.480</CURR_TIME><BACKUP1 /><BACKUP2 /><BACKUP3 /></HEAD><CONDITION /><DATA><CSTR_SET><CSTR><ID>1396066620648_7597_34</ID><ORGL_GIS_LNGD>120.94873</ORGL_GIS_LNGD><ORGL_GIS_LTTD>30.88034</ORGL_GIS_LTTD><GIS_LNGD>120.95292</GIS_LNGD><GIS_LTTD>30.877989</GIS_LTTD><BSNS_SYSM_CODE>zmxt</BSNS_SYSM_CODE><BSNS_SYSM_ID>029003281</BSNS_SYSM_ID><ENBD_IDNY>1</ENBD_IDNY><UPDT_TIME>2017-01-10 16:30:43</UPDT_TIME><LCNS_SORT_CODE>ZM0100301</LCNS_SORT_CODE><ACTV_DDLN>2018-12-31</ACTV_DDLN><ISS_DEPT>嘉善县烟草专卖局</ISS_DEPT><ISS_DATE>2015-11-17</ISS_DATE><IN_CHNG_DPRT>***</IN_CHNG_DPRT><RGSR_FND>2</RGSR_FND><PRCS_NMBR>1</PRCS_NMBR><LGL_RPRV>金海林</LGL_RPRV><AREA_CODE>330421</AREA_CODE><ORGN_CODE>11330404</ORGN_CODE><PAY_TXS_KIND_CODE>KH010151</PAY_TXS_KIND_CODE><INVC_TYPE_CODE>KH010161</INVC_TYPE_CODE><SYSM_INNR_IDNY>0</SYSM_INNR_IDNY><SYSM_INNR_FORM_CODE /><LGT_TYPE_CODE /><PLC_OF_BSNS_ADSN_CODE>KH010281</PLC_OF_BSNS_ADSN_CODE><CRTT_TYPE_CODE>KH0102901</CRTT_TYPE_CODE><MNGR_TLPN>84752400</MNGR_TLPN><PRNL_TLPN>84752400</PRNL_TLPN><PRNL_MBL_PHN_NO> </PRNL_MBL_PHN_NO><SPCL_CLNY_CODE /><PRNL_EDCN_LVL_CODE>99</PRNL_EDCN_LVL_CODE><CPNY_NAME>嘉善县金海林副食品店</CPNY_NAME><BSNS_ADDR>嘉善县魏塘街道魏中村新区七区南起第一排第一幢东起第一间</BSNS_ADDR><MSTR_TEL_NO>84752400</MSTR_TEL_NO><BKUP_TEL_NO>84632017</BKUP_TEL_NO><ORDR_FRQC_SHFT>0000100</ORDR_FRQC_SHFT><PRNL_ADDR>魏塘镇魏中村新区七区</PRNL_ADDR><RTNN_LYT_AREA_CODE>1</RTNN_LYT_AREA_CODE><PAY_MODE_CODE>KH010142</PAY_MODE_CODE><SELF_MNGT_STR_IDNY>0</SELF_MNGT_STR_IDNY><MSTR_STR_IDNY>0</MSTR_STR_IDNY><STCK_STR_IDNY>0</STCK_STR_IDNY><ALLW_SLS_PRFT_IDNY>0</ALLW_SLS_PRFT_IDNY><BSNS_BSNS_PLC_CODE>KH010104</BSNS_BSNS_PLC_CODE><BANK_NAME>嘉善信用联社</BANK_NAME><PAY_ACCT>1104020201101004286319</PAY_ACCT><BSNS_OPEN>0730</BSNS_OPEN><BSNS_CLSE>2130</BSNS_CLSE><INFO_TRML_CODE>KH010813</INFO_TRML_CODE><PRNL_CRTT_NO>330421195308253016</PRNL_CRTT_NO><PRNL_SEX_IDNY>1</PRNL_SEX_IDNY><PRNL_BRTY>1953-08-25</PRNL_BRTY><PRNL_NTN_CODE>01</PRNL_NTN_CODE><EDCN_LVL_CODE>99</EDCN_LVL_CODE><RG_DPRT_ID>029110008</RG_DPRT_ID><HEAD_IDNY_CODE>KH010643</HEAD_IDNY_CODE><DLVR_TIME_CODE>KH010742</DLVR_TIME_CODE><DLVR_TYPE_CODE>KH010731</DLVR_TYPE_CODE><ALL_NGHT_IDNY>0</ALL_NGHT_IDNY><MRKG_DPRT_ID>029210019</MRKG_DPRT_ID><DLVR_DPRT_ID>028300205</DLVR_DPRT_ID><MNGR_NAME> 金海林</MNGR_NAME><MNGR_BRTY>1953-08-25</MNGR_BRTY><MNGR_NTN_CODE>01</MNGR_NTN_CODE><MNGR_SEX_IDNY>1</MNGR_SEX_IDNY><MNGR_ID_CARD_NO> 330421530825301</MNGR_ID_CARD_NO><MNGR_MBL_PHN_NO> </MNGR_MBL_PHN_NO><MKT_GIS_LNGD>13464417.4644</MKT_GIS_LNGD><MKT_GIS_LTTD>3616913.8101</MKT_GIS_LTTD><CSTR_NO>954020</CSTR_NO><INTO_NET_TIME>2004-07-30</INTO_NET_TIME><OUT_NET_TIME /><BSNS_SCP_CODE>KH0100701,KH0100703</BSNS_SCP_CODE><ECNY_KIND_CODE>KH0100310</ECNY_KIND_CODE><ZONE_SCP_CODE>KH010121</ZONE_SCP_CODE><BSNS_MODE_CODE>KH010091</BSNS_MODE_CODE><BSNS_STTS_CODE>KH0102601</BSNS_STTS_CODE><GGRL_ENVT_CODE>KH010055</GGRL_ENVT_CODE><AREA_TYPE_CODE>KH010044</AREA_TYPE_CODE><SPPY_MODE_CODE /><CSTR_TYPE_CODE>KH010021</CSTR_TYPE_CODE><CHN_STR_IDNY>0</CHN_STR_IDNY><MNPY_CGRT_IDNY>0</MNPY_CGRT_IDNY><DEMO_CSTR_IDNY>0</DEMO_CSTR_IDNY><LCNS_NO>33040202040726001A</LCNS_NO><UNT_NO>330421103278</UNT_NO><MNMC_NO>3053</MNMC_NO><BSNS_LCNC_NO>3304213202456</BSNS_LCNC_NO><BSNS_LCNC_ACTV_DDLN>2008-08-11</BSNS_LCNC_ACTV_DDLN><TAX_AFFS_RGSR_NO> </TAX_AFFS_RGSR_NO><BSNS_SIZE_CODE>KH010112</BSNS_SIZE_CODE><OTHR_BSNS_SCP_CODE>KH010082</OTHR_BSNS_SCP_CODE><MSTR_STR_NAME> </MSTR_STR_NAME><MSTR_STR_ADDS> </MSTR_STR_ADDS><PRNL_NAME>金海林</PRNL_NAME></CSTR><CSTR><ID>1396065012290_86_33</ID><ORGL_GIS_LNGD>120.94291</ORGL_GIS_LNGD><ORGL_GIS_LTTD>30.66103</ORGL_GIS_LTTD><GIS_LNGD>120.947061</GIS_LNGD><GIS_LTTD>30.658539</GIS_LTTD><BSNS_SYSM_CODE>zmxt</BSNS_SYSM_CODE><BSNS_SYSM_ID>028007393</BSNS_SYSM_ID><ENBD_IDNY>1</ENBD_IDNY><UPDT_TIME>2017-01-10 16:30:43</UPDT_TIME><LCNS_SORT_CODE>ZM0100301</LCNS_SORT_CODE><ACTV_DDLN>2019-02-17</ACTV_DDLN><ISS_DEPT>嘉兴市烟草专卖局</ISS_DEPT><ISS_DATE>2016-01-05</ISS_DATE><IN_CHNG_DPRT /><RGSR_FND>2</RGSR_FND><PRCS_NMBR>1</PRCS_NMBR><LGL_RPRV>高洪平</LGL_RPRV><AREA_CODE>330401</AREA_CODE><ORGN_CODE>11330401</ORGN_CODE><PAY_TXS_KIND_CODE>KH010151</PAY_TXS_KIND_CODE><INVC_TYPE_CODE>KH010161</INVC_TYPE_CODE><SYSM_INNR_IDNY>0</SYSM_INNR_IDNY><SYSM_INNR_FORM_CODE /><LGT_TYPE_CODE /><PLC_OF_BSNS_ADSN_CODE>KH010281</PLC_OF_BSNS_ADSN_CODE><CRTT_TYPE_CODE>KH0102901</CRTT_TYPE_CODE><MNGR_TLPN>83031560</MNGR_TLPN><PRNL_TLPN>83031560</PRNL_TLPN><PRNL_MBL_PHN_NO> </PRNL_MBL_PHN_NO><SPCL_CLNY_CODE /><PRNL_EDCN_LVL_CODE>99</PRNL_EDCN_LVL_CODE><CPNY_NAME>嘉兴市新丰镇洪平综合早夜商店</CPNY_NAME><BSNS_ADDR>嘉兴市南湖区新丰镇栖王埭村</BSNS_ADDR><MSTR_TEL_NO>15325390620</MSTR_TEL_NO><BKUP_TEL_NO> </BKUP_TEL_NO><ORDR_FRQC_SHFT>0000100</ORDR_FRQC_SHFT><PRNL_ADDR>嘉兴市新丰镇净相桥西街2号</PRNL_ADDR><RTNN_LYT_AREA_CODE>1</RTNN_LYT_AREA_CODE><PAY_MODE_CODE>KH010142</PAY_MODE_CODE><SELF_MNGT_STR_IDNY>0</SELF_MNGT_STR_IDNY><MSTR_STR_IDNY>0</MSTR_STR_IDNY><STCK_STR_IDNY>0</STCK_STR_IDNY><ALLW_SLS_PRFT_IDNY>0</ALLW_SLS_PRFT_IDNY><BSNS_BSNS_PLC_CODE>KH010104</BSNS_BSNS_PLC_CODE><BANK_NAME>禾城信用联社</BANK_NAME><PAY_ACCT>101003778436878</PAY_ACCT><BSNS_OPEN>0000</BSNS_OPEN><BSNS_CLSE>0000</BSNS_CLSE><INFO_TRML_CODE>KH010813</INFO_TRML_CODE><PRNL_CRTT_NO>330411195503164219</PRNL_CRTT_NO><PRNL_SEX_IDNY>1</PRNL_SEX_IDNY><PRNL_BRTY>1955-03-16</PRNL_BRTY><PRNL_NTN_CODE>01</PRNL_NTN_CODE><EDCN_LVL_CODE>99</EDCN_LVL_CODE><RG_DPRT_ID>028110017</RG_DPRT_ID><HEAD_IDNY_CODE>KH010643</HEAD_IDNY_CODE><DLVR_TIME_CODE>KH010743</DLVR_TIME_CODE><DLVR_TYPE_CODE>KH010731</DLVR_TYPE_CODE><ALL_NGHT_IDNY>0</ALL_NGHT_IDNY><MRKG_DPRT_ID>028300418</MRKG_DPRT_ID><DLVR_DPRT_ID>028300178</DLVR_DPRT_ID><MNGR_NAME>高洪平</MNGR_NAME><MNGR_BRTY>1955-03-16</MNGR_BRTY><MNGR_NTN_CODE>01</MNGR_NTN_CODE><MNGR_SEX_IDNY>1</MNGR_SEX_IDNY><MNGR_ID_CARD_NO>330411550316421</MNGR_ID_CARD_NO><MNGR_MBL_PHN_NO> </MNGR_MBL_PHN_NO><MKT_GIS_LNGD>13463765.2435</MKT_GIS_LNGD><MKT_GIS_LTTD>3588482.8810</MKT_GIS_LTTD><CSTR_NO>0286001</CSTR_NO><INTO_NET_TIME>2004-02-18</INTO_NET_TIME><OUT_NET_TIME /><BSNS_SCP_CODE>KH0100701,KH0100703</BSNS_SCP_CODE><ECNY_KIND_CODE>KH0100310</ECNY_KIND_CODE><ZONE_SCP_CODE>KH010121</ZONE_SCP_CODE><BSNS_MODE_CODE>KH010091</BSNS_MODE_CODE><BSNS_STTS_CODE>KH0102601</BSNS_STTS_CODE><GGRL_ENVT_CODE>KH010055</GGRL_ENVT_CODE><AREA_TYPE_CODE>KH010044</AREA_TYPE_CODE><SPPY_MODE_CODE /><CSTR_TYPE_CODE>KH010021</CSTR_TYPE_CODE><CHN_STR_IDNY>0</CHN_STR_IDNY><MNPY_CGRT_IDNY>0</MNPY_CGRT_IDNY><DEMO_CSTR_IDNY>0</DEMO_CSTR_IDNY><LCNS_NO>33040121020708001A</LCNS_NO><UNT_NO>330401106373</UNT_NO><MNMC_NO>5932</MNMC_NO><BSNS_LCNC_NO>3304113100035</BSNS_LCNC_NO><BSNS_LCNC_ACTV_DDLN>2008-05-31</BSNS_LCNC_ACTV_DDLN><TAX_AFFS_RGSR_NO> </TAX_AFFS_RGSR_NO><BSNS_SIZE_CODE>KH010112</BSNS_SIZE_CODE><OTHR_BSNS_SCP_CODE>KH010081</OTHR_BSNS_SCP_CODE><MSTR_STR_NAME> </MSTR_STR_NAME><MSTR_STR_ADDS> </MSTR_STR_ADDS><PRNL_NAME>高洪平</PRNL_NAME></CSTR></CSTR_SET></DATA></DATASET>";
		// System.out.println(xml.substring(0, 30000));
		System.out.println(getDataForDc(xml));
	}

	/**
	 * 数据中心的部分业务
	 **/
	@SuppressWarnings("unchecked")
	public static List<Map> getDataForDc(String xml) {

		try {
			Document db = DocumentHelper.parseText(xml);
			Map mp = Dom2Map(db);
			Object ob = mp.get("DATA");
			if (ob.getClass().isAssignableFrom(ArrayList.class)) {
				return (List<Map>) ob;
			} else {
				List ls = new ArrayList();
				ls.add(ob);
				return ls;
			}
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 通用获取map
	 **/
	public static Map getDataMap(String xml) {

		try {
			Document db = DocumentHelper.parseText(xml);
			Map mp = Dom2Map(db);
			return mp;
		} catch (Exception e) {
			return null;
		}
	}

}
