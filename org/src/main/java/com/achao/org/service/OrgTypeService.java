package com.achao.org.service;

import com.achao.audit.util.AuditThreadLocal;
import com.achao.org.dao.OrgType;
import com.achao.org.mapper.OrgTypeMapper;
import com.achao.org.param.OrgTypeAddParam;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author achao
 * @date 2022/12/28 21:44
 */
@Service
public class OrgTypeService extends ServiceImpl<OrgTypeMapper, OrgType> {
    public void save(OrgTypeAddParam addParam) {
        OrgType orgType = new OrgType();
        orgType.setOrgTypeName(addParam.getOrgTypeName());
        this.save(orgType);
        AuditThreadLocal.get().setResourceId(orgType.getId());
    }
}
