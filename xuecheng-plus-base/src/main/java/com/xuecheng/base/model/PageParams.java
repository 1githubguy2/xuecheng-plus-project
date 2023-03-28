package com.xuecheng.base.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author unbroken
 * @version 1.0
 * @description 分页查询分页参数
 * @date 2023-03-29 00:31
 */
@Data
@ToString
public class PageParams {

    //当前页码(因为mybatis-plus中页码的类型是Long，所以这里也定义为Long)
    @ApiModelProperty("页码")
    private Long pageNo = 1L;
    //每页显示记录数
    @ApiModelProperty("每页记录数")
    private Long pageSize = 30L;

    public PageParams() {
    }

    public PageParams(Long pageNo, Long pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }
}
