package com.ruoyi.file.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * @author chenm
 * @create 2019-08-19 19:58
 */
@Getter
@Setter
public class PlaysOrder extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**  */
    private Long id;
    /**  */
    private String fileName;
    /** 0 图片 1 视频 2 文字 */
    private Integer types;

    private Long channelId;
    /**  */
    private String ur;
    /**  */
    private String content;
    /** 播放顺序 */
    private Integer orderBy;
    /**  */
    private String remark;

    private String suffix;
}
