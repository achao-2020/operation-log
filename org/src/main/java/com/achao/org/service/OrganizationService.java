package com.achao.org.service;

import cn.hutool.core.lang.UUID;
import com.achao.audit.util.AuditThreadLocal;
import com.achao.org.dao.Organization;
import com.achao.org.mapper.OrganizationMapper;
import com.achao.org.param.OrganizationSaveParam;
import com.achao.org.param.OrganizationUpdateParam;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author achao
 * @email achao0218@163.com
 * @date 2022-12-21 17:00:44
 */
@Service("organizationService")
public class OrganizationService extends ServiceImpl<OrganizationMapper, Organization> {

    public void save(OrganizationSaveParam saveParam) {
        Organization organization = new Organization();
        BeanUtils.copyProperties(saveParam, organization);
        this.save(organization);
        AuditThreadLocal.get().setResourceId(organization.getId());
    }

    public void update(OrganizationUpdateParam updateParam) {
        Organization organization = this.getById(updateParam.getId());
        Assert.notNull(organization, "找不到对应组织");
        BeanUtils.copyProperties(updateParam, organization);
        this.updateById(organization);
    }

    public void deleteById(String id) {
        this.removeById(id);
    }

    public String getNameById(String id) {
        return this.getById(id).getOrgName();
    }
}
