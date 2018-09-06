package com.delllogistics.controller;

import com.delllogistics.dto.*;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.service.CompanyService;
import com.delllogistics.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**组织机构
 * Created by xzm on 2017-11-6.
 */
@RestController
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    /**
     * 查询组织机构列表
     * @param page 页数
     * @param size 条数
     * @param company 查询条件
     * @return 分页查询结果
     */
    @PostMapping("/findCompanys")
    public Result<Page<CompanyInfo>> findCompanys(int page, int size, Company company){
        Page<CompanyInfo> pageCompany = companyService.findCompanyInfos(page, size, company);
        return ResultUtil.success(pageCompany);
    }

    @PostMapping("/findCompanyList")
    public List<Company> findCompanyList(){
        return companyService.findCompanyList();
    }

    /**
     * 查询机构树
     * @return 机构树
     */
    @PostMapping("/findCompanyTree")
    public Result<Set<Company>> findCompanyTree(CompanyTreeModel companyTreeModel){
        Set<Company> company = companyService.findCompanyTree(companyTreeModel);
        return ResultUtil.success(company);
    }

    /**
     * 新增或修改机构
     * @param company 机构属性
     * @param bindingResult 验证结果
     * @return 提交结果
     */
    @PostMapping("/submitCompany")
    public Result submitCompany(@Valid Company company, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResultUtil.error(-1,bindingResult.getFieldError().getDefaultMessage());
        }
        companyService.submitCompany(company);
        return ResultUtil.success();
    }

    /**
     * 删除机构
     * @param id 机构ID
     * @return 删除结果
     */
    @PostMapping("/delCompany")
    public Result delCompany(@RequestParam Long id){
        companyService.delCompany(id);
        return ResultUtil.success();
    }

    @PostMapping("/findGoodsRelationByCompanyId")
    public Result<TransferInfo> findGoodsRelationByCompanyId(Long createCompanyId, Long grantCompanyId){
        TransferInfo set=companyService.findGoodsRelationByCompanyId(createCompanyId,grantCompanyId);
        return ResultUtil.success(set);
    }

    @PostMapping("/grantGoodsToCompany")
    public Result grantGoodsToCompany(Company company){
        companyService.grantGoodsToCompany(company);
        return ResultUtil.success();
    }

    @GetMapping("/getSelectCompanies")
    public Result getSelectCompanies(){
        List<PickerModel> companies=companyService.getSelectCompanies();
        return ResultUtil.success(companies);
    }


}
