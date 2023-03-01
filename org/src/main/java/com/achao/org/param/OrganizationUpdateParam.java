package com.achao.org.param;

import lombok.Data;

/**
 * @author achao
 * @date 2022/12/21 18:12
 */
@Data
public class OrganizationUpdateParam {

    private String id;

    private String parentId;

    private String orgName;

    private String orgTypeId;
}
