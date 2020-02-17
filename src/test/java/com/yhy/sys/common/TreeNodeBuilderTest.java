package com.yhy.sys.common;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest

class TreeNodeBuilderTest {

    @Test
    void build() {
        List<TreeNode> treeNodes= new ArrayList<>();
        TreeNode t1=new TreeNode(2,1,"基础管理",false);
        TreeNode t2=new TreeNode(3,1,"进货管理",false);
        TreeNode t11=new TreeNode(4,2,"客户管理",false);
        TreeNode t12=new TreeNode(5,2,"商品管理",false);
        TreeNode t21=new TreeNode(6,3,"商品进货",false);
        TreeNode t22=new TreeNode(7,3,"商品退货查询",false);
        treeNodes.add(t1);
        treeNodes.add(t2);
        treeNodes.add(t11);
        treeNodes.add(t12);
        treeNodes.add(t21);
        treeNodes.add(t22);
        List<TreeNode> build = TreeNodeBuilder.build(treeNodes, 1);
    }

}