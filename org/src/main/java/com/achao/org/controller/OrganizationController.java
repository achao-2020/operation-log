package com.achao.org.controller;

import com.achao.audit.annotation.AuditLog;
import com.achao.audit.common.ActionType;
import com.achao.org.audit.OrganizationSaveConvert;
import com.achao.org.audit.OrganizationUpdateConvert;
import com.achao.org.param.OrganizationSaveParam;
import com.achao.org.param.OrganizationUpdateParam;
import com.achao.org.service.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * @author achao
 * @email achao0218@163.com
 * @date 2022-12-21 17:00:44
 */
@Tag(name = "组织操作接口")
@RestController
@RequestMapping("/app")
public class OrganizationController {

    @Resource(name = "organizationService")
    private OrganizationService organizationService;

    /**
     * 保存
     */
    @Operation(description = "保存")
    @PostMapping("/org/save")
    @AuditLog(actionType = ActionType.ADD, actionName = "新增", resourceType = "组织", convertClass = OrganizationSaveConvert.class)
    public void save(@RequestBody OrganizationSaveParam saveParam) {
        organizationService.save(saveParam);
    }

    /**
     * 修改
     */
    @Operation(description = "修改")
    @AuditLog(actionType = ActionType.UPDATE, actionName = "更新", resourceType = "组织", convertClass = OrganizationUpdateConvert.class)
    @PostMapping("/org/update")
    public void update(@RequestBody OrganizationUpdateParam updateParam) {
        organizationService.update(updateParam);
    }

    /**
     * 删除
     */
    @Operation(description = "删除")
    @AuditLog(actionType = ActionType.DELETE, actionName = "删除组织", resourceType = "组织", springEl = "删除了组织名称为:#{@organizationService.getById(#args[0]).getOrgName()}")
    @PostMapping("/org/delete/{id}")
    public void delete(@PathVariable String id) {
        organizationService.deleteById(id);
    }

}
