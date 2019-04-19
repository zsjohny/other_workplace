package com.jiuyuan.util.tree.builder;

import java.util.List;

import com.jiuyuan.util.tree.Tree;
import com.jiuyuan.util.tree.TreeNode;

public class TreeBuilder {

    public static Tree buildTree(Object rootObject, TreeNodeEnumerator enumerator) {
        Tree tree = new Tree(rootObject, null);
        List<?> children = enumerator.getChildren(rootObject);
        addChildren(tree.getRoot(), children, enumerator);
        return tree;
    }

    private static void addChildren(TreeNode parent, List<?> children, TreeNodeEnumerator enumerator) {
        if (children != null) {
            for (Object child : children) {
                TreeNode node = new TreeNode(parent, child);
                parent.addChild(node);

                List<?> grandchildren = enumerator.getChildren(child);
                addChildren(node, grandchildren, enumerator);
            }
        }
    }
}
