package com.hgy.distributed.lock.mysql.model;


import lombok.Data;

import java.util.Date;

/**
 * @author heguoya
 * @version 1.0
 * @className ResourceLock
 * @description 资源锁对象
 * @date 2019/4/25 20:39
 */
@Data
public class ResourceLockEntity {
    /**
     * 主键id
     */
  private long id;
    /**
     * 资源名称
     */
  private String resourceName;
    /**
     * 节点信息
     */
  private String nodeInfo;
    /**
     * 重入次数
     */
  private Integer count;
    /**
     * 描述
     */
  private String description;
    /**
     * 修改时间
     */
  private Date gmtModified;
    /**
     * 创建时间
     */
  private Date gmtCreate;
}
