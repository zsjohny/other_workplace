package com.jiuyuan.util.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import com.jiuyuan.util.tree.help.equality.EqualityTester;
import com.jiuyuan.util.tree.help.equality.EqualsEqualityTester;
import com.jiuyuan.util.tree.help.stringify.Stringifier;
import com.jiuyuan.util.tree.help.stringify.ToStringStringifier;

public class Tree {

    private TreeNode root;

    private EqualityTester<Object> equalityTester;

    public Tree(Object data) {
        this(data, new EqualsEqualityTester<Object>());
    }

    public Tree(Object data, EqualityTester<Object> equalityTester) {
        this.root = new TreeNode(null, data, true);
        this.equalityTester = equalityTester;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void addPath(Collection<Object> data) {
        addPath(root, data);
    }

    public void addPath(TreeNode parent, Collection<Object> data) {
        if (parent == null) {
            throw new IllegalArgumentException("Node can't be null.");
        }
        for (Object theData : data) {
            TreeNode treeNode = findTreeNode(parent, theData, false);
            if (treeNode == null) {
                treeNode = new TreeNode(parent, theData);
                parent.addChild(treeNode);
            } else {
                treeNode.setData(theData);
            }
            parent = treeNode;
        }
    }

    public TreeNode findTreeNode(Object data, boolean deep) {
        return findTreeNode(null, data, deep);
    }

    public TreeNode findTreeNode(TreeNode parent, Object data, boolean deep) {
        List<TreeNode> children = null;
        if (parent == null) {
            children = new ArrayList<TreeNode>();
            children.add(root);
        } else {
            children = parent.getChildren();
        }

        for (TreeNode child : children) {
            if (equalityTester.equals(child.getData(), data)) {
                return child;
            }
            if (deep) {
                TreeNode node = findTreeNode(child, data, deep);
                if (node != null) {
                    return node;
                }
            }
        }

        return null;
    }

    public List<String> print(boolean printRoot) {
        return print(new ToStringStringifier<Object>(), printRoot);
    }

    public List<String> print(Stringifier<Object> stringifier, boolean printRoot) {
        List<String> treeLines = new ArrayList<String>();
        if (printRoot) {
            print(treeLines, root, stringifier, new Stack<Boolean>());
        } else {
            for (TreeNode child : root.getChildren()) {
                print(treeLines, child, stringifier, new Stack<Boolean>());
            }
        }
        return treeLines;
    }

    private void print(List<String> treeLines, TreeNode node, Stringifier<Object> stringifier,
                       Stack<Boolean> isLastChildStack) {
        StringBuilder prefix = new StringBuilder();
        int depth = isLastChildStack.size();
        if (depth > 0) {
            for (int i = 0; i < isLastChildStack.size(); i++) {
                boolean isLastChild = isLastChildStack.get(i);
                if (isLastChild) {
                    if (i == isLastChildStack.size() - 1) {
                        prefix.append(" !- ");
                    } else {
                        prefix.append("    ");
                    }
                } else {
                    if (i == isLastChildStack.size() - 1) {
                        prefix.append(" |- ");
                    } else {
                        prefix.append(" |  ");
                    }
                }
            }
        }
        treeLines.add(prefix + stringifier.toString(node.getData()));

        int i = 0;
        for (TreeNode child : node.getChildren()) {
            boolean isLastChild = i == node.getChildren().size() - 1;
            isLastChildStack.push(isLastChild);
            print(treeLines, child, stringifier, isLastChildStack);
            isLastChildStack.pop();
            ++i;
        }
    }
}