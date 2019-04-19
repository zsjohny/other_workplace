/**
 * 
 */
package com.jiuyuan.util;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author LWS
 *
 */
public class Bucket<Head,Node> {

    public boolean put(Head h,Node n){
        return this.createNode(h, n);
    }
    
    private boolean createHead(Head h){
        if(this._bucketContainer.containsKey(h)){
            return true;
        }
        this._bucketContainer.put(h, new LinkedList<Node>());
        return true;
    }
    
    private boolean createNode(Head h,Node n){
        List<Node> nodeList = this._bucketContainer.get(h);
        if(null == nodeList){
            this.createHead(h);
        }
        nodeList = this._bucketContainer.get(h);
        if(nodeList.contains(n)){
            return true;
        }
        nodeList.add(n);
        return true;
    }
    
    private Map<Head, List<Node>> _bucketContainer;
    {
        _bucketContainer = new Hashtable<Head, List<Node>>();
    }
}
