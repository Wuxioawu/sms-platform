package com.peng.sms.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 菜单表
 * sms_menu
 */
@Data
public class SmsMenu implements Serializable {
    private Integer id;

    /**
     * 菜单名
     */
    private String name;

    /**
     * 父菜单id
     */
    private Long parentId;

    /**
     * 跳转的连接地址
     */
    private String url;

    /**
     * 按钮的小图标
     */
    private String icon;

    /**
     * 菜单的类型
     */
    private Integer type;

    /**
     * 菜单排序规则
     */
    private Integer sort;

    /**
     * 创建时间，默认系统时间
     */
    private Date created;

    /**
     * 创建人id
     */
    private Long createId;

    /**
     * 修改时间，默认系统时间
     */
    private Date updated;

    /**
     * 修改人id
     */
    private Long updateId;

    /**
     * 是否删除 0-未删除 ， 1-已删除
     */
    private Byte isDelete;

    /**
     * 备用字段1
     */
    private String extend1;

    /**
     * 备用字段2
     */
    private String extend2;

    /**
     * 备用字段3
     */
    private String extend3;

    /**
     * 备用字段4
     */
    private String extend4;

    private static final long serialVersionUID = 1L;
}