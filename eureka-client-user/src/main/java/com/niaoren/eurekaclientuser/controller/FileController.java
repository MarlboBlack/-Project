package com.niaoren.eurekaclientuser.controller;


import com.niaoren.eurekaclientuser.common.ServerResponse;
import com.niaoren.eurekaclientuser.entity.File;
import com.niaoren.eurekaclientuser.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@Slf4j
@RequestMapping("/file/")
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * 查询某公司的文件
     * @param companyName 公司名称
     * @return
     */
    @RequestMapping("queryFileByCompanyName")
    @ResponseBody
    public ServerResponse<List<File>> queryFileByCompanyName(@RequestParam("companyName")String companyName){
        return fileService.queryFileByCompanyName(companyName);
    }


}
