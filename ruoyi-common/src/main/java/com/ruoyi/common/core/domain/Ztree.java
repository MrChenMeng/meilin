package com.ruoyi.common.core.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Ztree树结构实体类
 * 
 * @author ruoyi
 */
public class Ztree implements Serializable
{
    private static final long serialVersionUID = 1L;


    /**
     * 节点的子节点数据集合。
     *
     * 1、如果不使用 children 属性保存子节点数据，请修改 setting.data.key.children
     *
     * 2、异步加载时，对于设置了 isParent = true 的节点，在展开时将进行异步加载
     *
     * 默认值：无
     */
    private List<Ztree> children;

    /**
     * 记录 treeNode 节点是否为父节点。
     *
     * 1、初始化节点数据时，根据 treeNode.children 属性判断，有子节点则设置为 true，否则为 false
     *
     * 2、初始化节点数据时，如果设定 treeNode.isParent = true，即使无子节点数据，也会设置为父节点
     *
     * 3、为了解决部分朋友生成 json 数据出现的兼容问题, 支持 "false","true" 字符串格式的数据
     *
     * true 表示是父节点
     *
     * false 表示不是父节点
     */
    private boolean isParent;


    /** 节点ID */
    private Object id;

    /** 节点父ID */
    private Object pId;

    /** 节点名称 */
    private String name;

    /** 节点标题 */
    private String title;

    /** 是否勾选 */
    private boolean checked = false;

    /** 是否展开 */
    private boolean open = false;

    /** 是否能勾选 */
    private boolean nocheck = false;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Object getpId() {
        return pId;
    }

    public void setpId(Object pId) {
        this.pId = pId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public boolean isChecked()
    {
        return checked;
    }

    public void setChecked(boolean checked)
    {
        this.checked = checked;
    }

    public boolean isOpen()
    {
        return open;
    }

    public void setOpen(boolean open)
    {
        this.open = open;
    }

    public boolean isNocheck()
    {
        return nocheck;
    }

    public void setNocheck(boolean nocheck)
    {
        this.nocheck = nocheck;
    }

    public List<Ztree> getChildren() {
        return children;
    }

    public void setChildren(List<Ztree> children) {
        this.children = children;
    }

    public boolean isParent() {
        return isParent;
    }

    public void setParent(boolean parent) {
        isParent = parent;
    }
}
