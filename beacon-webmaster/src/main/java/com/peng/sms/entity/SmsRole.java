package com.peng.sms.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 角色表
 * sms_role
 */
@Data
public class SmsRole implements Serializable {
    private Integer id;

    /**
     * 角色名
     */
    private String name;

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