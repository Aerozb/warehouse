package com.yhy.sys.common;

import java.util.ArrayList;
import java.util.List;

public class TreeNodeBuilder {


//    public static List<TreeNode> build(List<TreeNode> treeNodes, Integer pid) {
//        List<TreeNode> nodes = new ArrayList<>();
//        //遍历菜单
//        for (TreeNode node1 : treeNodes) {
//            //如果是父节点为1的菜单添加到新的结果集
//            if (node1.getPid().equals(pid)) {
//                //因为添加的是引用，所以后续进行子菜单增加是可以的
//                nodes.add(node1);
//                //第二层遍历，循环父节点为node1的id的节点，然后把它加到node1的子节点菜单
//                for (TreeNode node2 : treeNodes) {
//                    if (node1.getId().equals(node2.getPid())) {
//                        node1.getChildren().add(node2);
//                    }
//                }
//            }
//        }
//        return nodes;
//    }
    /**
     * 通过递归把没有层级关系的集合变成有层级关系的
     *
     * @param originalTreeNodes 传进来还没转换的菜单
     * @param pid     父节点
     * @return
     */
    public static List<TreeNode> build(List<TreeNode> originalTreeNodes, Integer pid) {
        //对于递归调用来说是定义一个保存子菜单的list
        //因为是new的，每次递归都会生成新的，来保存当前查询的子菜单，返回给上一层递归调用
        //对调用这个方法的用户来说，返回的就是结果
        List<TreeNode> treeNodes= new ArrayList<>();
        //循环
        for (TreeNode o : originalTreeNodes) {
            //找到父节点为pid的菜单对象
            if (o.getPid().equals(pid)) {
                //添加到list，相当于把子菜单存起来
                treeNodes.add(o);
                //再把当前的节点当做父节点参数传过去，递归，找到父节点为当前菜单对象
                List<TreeNode> childMenus = build(originalTreeNodes, o.getId());
                //设置子菜单
                o.setChildren(childMenus);
            }
        }
        //返回菜单，对于for循环里调用build（），返回的结果就是子菜单
        return treeNodes;
    }
}