package com.achao.org.audit;

import com.achao.audit.annotation.AuditColumn;
import com.achao.audit.common.ColumnType;
import com.achao.audit.convert.IAuditConvert;
import com.achao.org.param.OrganizationUpdateParam;
import com.achao.org.service.OrgTypeService;
import lombok.Data;

/**
 * @author achao
 * @date 2022/12/25 13:06
 */
@Data
public class OrganizationUpdateConvert extends OrganizationUpdateParam implements IAuditConvert<OrganizationUpdateConvert> {

    private OrgTypeService orgTypeService;

    @AuditColumn(columnType = ColumnType.ID, isAudit = false)
    private String id;

    @AuditColumn(name = "父组织id")
    private String parentId;

    @AuditColumn(name = "组织名称", columnType = ColumnType.NAME)
    private String orgName;

    @AuditColumn(name = "父组织名称")
    private String orgTypeName;

    @Override
    public OrganizationUpdateConvert postWrapper() {
        this.orgTypeName = orgTypeService.getById(super.getOrgTypeId()).getOrgTypeName();
        return this;
    }
}
