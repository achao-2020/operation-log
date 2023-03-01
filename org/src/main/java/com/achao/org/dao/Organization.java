package com.achao.org.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author achao
 * @date 2022/12/21 17:57
 */
@Data
@TableName("organization")
public class Organization {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    private String parentId;

    private String orgName;

    private String orgTypeId;
}
