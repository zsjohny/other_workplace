package com.jiuyuan.util.tree;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {

    private TreeNode parent;

    private List<TreeNode> children;

    private Object data;

    public TreeNode(TreeNode parent, Object data) {
        this(parent, data, false);
    }

    protected TreeNode(TreeNode parent, Object data, boolean root) {
        if (!root && parent == null) {
            throw new IllegalArgumentException("Parent can't be null for a non-root tree node.");
        }

        this.parent = parent;
        this.data = data;
        this.children = new ArrayList<TreeNode>();
    }

    public void addChild(TreeNode child) {
        this.children.add(child);
    }

    public TreeNode getParent() {
        return parent;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}