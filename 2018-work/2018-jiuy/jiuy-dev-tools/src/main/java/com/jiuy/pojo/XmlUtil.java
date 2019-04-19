package com.jiuy.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


@SuppressWarnings("rawtypes")
public class XmlUtil {

	@SuppressWarnings("unchecked")
	private static Map<String, Object> Dom2Map(Document doc){  
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
                	if(List.class.isAssignableFrom(ob.getClass())){
                		ob = ((List)ob).add(Dom2Map(e));
                	}else{
                		List ls = new ArrayList<Object>();
                		ls.add(ob);
                		ls.add(Dom2Map(e));
                		map.put(name, ls);
                	}
                }else{
                	map.put(name, Dom2Map(e));
                }
            }else  
                map.put(name, e.getText());  
        }  
        return map;  
    }  
	
	@SuppressWarnings("unchecked")
	private static Map Dom2Map(Element e){  
		
		Map map = new HashMap();  
        List list = e.elements();  
        if(list.size() > 0){  
            for (int i = 0;i < list.size(); i++) {  
                Element iter = (Element) list.get(i);  
                List mapList = new ArrayList();  
                  
                if(iter.elements().size() > 0){  
                    Map m = Dom2Map(iter);  
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
                            mapList.add(iter.getText());  
                        }  
                        if(obj.getClass().getName().equals("java.util.ArrayList")){  
                            mapList = (List) obj;  
                            mapList.add(iter.getText());  
                        }  
                        map.put(iter.getName(), mapList);  
                    }else  
                        map.put(iter.getName(), iter.getText());  
                }  
            }  
        }else  
            map.put(e.getName(), e.getText());  
        return map;  
    }  
	

	public static void main(String[] args) {
//		DcR1Orgn dcR1Orgn1 = new DcR1Orgn();
//		dcR1Orgn1.setPrntOrgnCode("100043279237SY");
////		String name="100043279237SY";
////	    System.out.println(name.substring(name.length()-1,name.length()));//输出d
////	    System.out.println(name.substring(name.length()-2));//输出d
//		 System.out.println((dcR1Orgn1.getPrntOrgnCode()).substring(dcR1Orgn1.getPrntOrgnCode().length()-2));
//		
		String xml = 
				"<?xml version=\"1.0\" encoding=\"UTF-8\" ?> "+
				"<DATASET>" +
				"<DATASTATUS><![CDATA[2]]></DATASTATUS>"+
				  "<DATA>"+
				   " <LOGINNAME><![CDATA[jiyuanxiang_sj]]></LOGINNAME>"+
				   "  <ORGID><![CDATA[1389361288697_2848_167]]></ORGID>"+
				   " <DISPLAYODER><![CDATA[1110]]></DISPLAYODER>"+
				   " <OFFICEPHONE><![CDATA[87911052]]></OFFICEPHONE>"+
				   " <VIRTUALPHONE><![CDATA[6602]]></VIRTUALPHONE>"+
				   " <POSTNAME><![CDATA[职员]]></POSTNAME>"+
				   " <FLAG><![CDATA[1]]></FLAG>"+
				   " <PUSHTIME><![CDATA[2016-12-01 18:45:27]]></PUSHTIME>"+
				   " </DATA>"+
				   " <DATA>"+
				   " <LOGINNAME><![CDATA[OAguanliyuan_sj]]></LOGINNAME>"+
				   " <ORGID><![CDATA[1389361288697_2848_167]]></ORGID>"+
				   " <DISPLAYODER><![CDATA[10043]]></DISPLAYODER>"+
				   " <OFFICEPHONE><![CDATA[]]></OFFICEPHONE>"+
				   " <VIRTUALPHONE><![CDATA[]]></VIRTUALPHONE>"+
				   " <POSTNAME><![CDATA[职员]]></POSTNAME>"+
				   " <FLAG><![CDATA[1]]></FLAG>"+
				   " <PUSHTIME><![CDATA[2016-12-01 18:45:27]]></PUSHTIME>"+
				   " </DATA>"+
				   " <DATA>"+
				   " <LOGINNAME><![CDATA[zjycadmin_sj]]></LOGINNAME>"+
				   " <ORGID><![CDATA[1389361288697_2848_167]]></ORGID>"+
				   " <DISPLAYODER><![CDATA[10046]]></DISPLAYODER>"+
				   " <OFFICEPHONE><![CDATA[]]></OFFICEPHONE>"+
				   " <VIRTUALPHONE><![CDATA[]]></VIRTUALPHONE>"+
				   " <POSTNAME><![CDATA[职员]]></POSTNAME>"+
				   " <FLAG><![CDATA[1]]></FLAG>"+
				   " <PUSHTIME><![CDATA[2016-12-01 18:45:27]]></PUSHTIME>"+
				   " </DATA>"+
				 "</DATASET>";
				getMap(xml);
	}
	
	/** 
	* @Title: getData 
	* @Description: 
	*             通过xml字符串转成map
	* @author Aison
	* @param @param xml
	* @param @return    
	* @return Map    返回类型 
	* @throws 
	*/
	public static  Map getMap(String xml) {
	    try {
			Document db =  DocumentHelper.parseText(xml);  
			Map mp = Dom2Map(db);
			return mp;
		} catch (Exception e) {
			return null;
		}
	} 

}
