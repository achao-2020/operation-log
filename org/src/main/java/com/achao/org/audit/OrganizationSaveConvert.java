package com.achao.org.audit;

import com.achao.audit.annotation.AuditColumn;
import com.achao.audit.common.ColumnType;
import com.achao.audit.common.DesType;
import com.achao.audit.convert.IAuditConvert;
import com.achao.org.param.OrganizationSaveParam;
import com.achao.org.service.OrgTypeService;
import lombok.Data;

import java.util.Map;

/**
 * @author achao
 * @date 2022/12/22 23:45
 */
@Data
public class OrganizationSaveConvert extends OrganizationSaveParam implements IAuditConvert<OrganizationSaveConvert> {
    // 会自动注入
    private OrgTypeService orgTypeService;

    @AuditColumn(name = "组织名称", columnType = ColumnType.NAME, dstBean = DesType.DES_NAME)
    private String orgName;

    @AuditColumn(name = "父组织id")
    private String parentId;

    @AuditColumn(name = "组织类型名称")
    private String orgTypeName;

    @Override
    public OrganizationSaveConvert preWrapper(Map<String, Object> map) {
        // 在接口方法执行前调用，可以用来查询删除的对象信息
        return this;
    }

    @Override
    public OrganizationSaveConvert postWrapper() {
        // 在接口方法执行后调用，可以用来更新，编辑查询对象信息
        this.orgTypeName = orgTypeService.getById(super.getOrgTypeId()).getOrgTypeName();
        return this;
    }
}
