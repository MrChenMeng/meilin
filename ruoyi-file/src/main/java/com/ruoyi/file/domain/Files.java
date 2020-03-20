package com.ruoyi.file.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author chenm
 * @create 2019-08-19 19:56
 */
@Getter
@Setter
public class Files extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**  */
    private Integer id;
    /**  */
    private String url;
    /**  */
    private String status;
    /**  */
    private String fileName;
    /**  */
    private String remark;
    /** 类型（0代表图片 1代表视频） */
    private String type;
    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;
    /** 创建者 */
    private String createBy;
    private String createByName;
    /** 创建时间 */
    private Date createTime;
    /** 更新者 */
    private String updateBy;
    private String updateByName;
    /** 更新时间 */
    private Date updateTime;

    /**后缀**/
    private String suffix;

    private String content;

    /**修改标识 0 新增 1 修改**/
    private int updateFlag;

    private Long channelId;
}
