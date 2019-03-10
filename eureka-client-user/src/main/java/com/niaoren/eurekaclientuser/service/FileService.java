package com.niaoren.eurekaclientuser.service;

import com.niaoren.eurekaclientuser.common.ServerResponse;
import com.niaoren.eurekaclientuser.entity.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    /**
     * 上传文件到ftp服务器的方法
     * @param file 要上传的文件
     * @param  path 上传的服务器目录
     * @return
     */
    String uploadFile(MultipartFile file,String path);

    /**
     * 添加文件
     * @param file 要添加的文件
     * @return
     */
    public ServerResponse<String> addFile(com.niaoren.eurekaclientuser.entity.File file);

    /**
     * 查询某公司的文件
     * @param companyName 公司名
     * @return
     */
    public ServerResponse<List<File>> queryFileByCompanyName(String companyName);

}
