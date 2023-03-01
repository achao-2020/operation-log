package com.achao.org.controller;

import com.achao.org.dao.OrgType;
import com.achao.org.param.OrgTypeAddParam;
import com.achao.org.service.OrgTypeService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author achao
 * @date 2022/12/28 21:49
 */
@RestController
@RequestMapping("/orgType")
public class OrgTypeController {

    @Resource
    private OrgTypeService orgTypeService;

    @PostMapping("/save")
    @Operation(description = "组织类型新增")
    public void save(@RequestBody OrgTypeAddParam addParam) {
        orgTypeService.save(addParam);
    }
}
