package com.niaoren.eurekaclientuser.service.serviceImpl;



import com.niaoren.eurekaclientuser.Utils.FTPUtil;
import com.niaoren.eurekaclientuser.common.ServerResponse;
import com.niaoren.eurekaclientuser.mapper.FileMapper;
import com.niaoren.eurekaclientuser.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@Slf4j
public class FileServiceImpl implements FileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    private FileMapper fileMapper;

    /**
     * 上传文件到ftp服务器的方法
     * @param file 要上传的文件
     * @param path 上传的服务器目录
     * @return
     */
    @Override
    public String uploadFile(MultipartFile file, String path) {
        //得到上传时提交的文件名
        String fileName = file.getOriginalFilename();
        //扩展名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf( ".")+1);
        //上传到服务器的文件名
        String uploadFileName = UUID.randomUUID().toString().replaceAll("-","")+"."+fileExtensionName;

        //创建目录(在项目所在服务器的项目路径下创建的path目录)
        File fileDir = new File(path);
        if(!fileDir.exists()){
            //设置文件可写
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        //创建文件
        File targetFile = new File(path,uploadFileName);

        try {
            //上传文件
            file.transferTo(targetFile);
//            List<File> fileList = new ArrayList<File>();
//            fileList.add(targetFile);
//            //将targetFile上传到FTP服务器
//            FTPUtil.uploadFile(fileList);
//            //上传到FTP服务器之后，删除tomcat服务器上的文件
//            targetFile.delete();

        } catch (IOException e) {
            logger.error("上传文件异常",e);
        }
        //上传成功后返回上传到服务器之后的文件名
        return targetFile.getName();
    }


    /**
     * 添加文件
     * @param file 要添加的文件
     * @return
     */
    public ServerResponse<String> addFile(com.niaoren.eurekaclientuser.entity.File file){
        String companyName = file.getCompanyName();
        Integer type = file.getType();
        //判断文件表中是否已有该公司的该类型文件，有就更新，没有就插入
        com.niaoren.eurekaclientuser.entity.File existFile  = fileMapper.selectExists(companyName,type);
        if (existFile == null){
            int resultCount = fileMapper.insert(file);
            if (resultCount > 0){
                return ServerResponse.createBySuccessMessage("添加文件成功");
            }
        }else{
            existFile.setFtpAddress(file.getFtpAddress());
            int resultCount = fileMapper.updateByPrimaryKey(existFile);
            if (resultCount > 0){
                return ServerResponse.createBySuccessMessage("添加文件成功");
            }
        }


        return ServerResponse.createByErrorMessage("添加文件失败");
    }

    /**
     * 查询某公司的文件
     * @param companyName 公司名
     * @return
     */
    public ServerResponse<List<com.niaoren.eurekaclientuser.entity.File>> queryFileByCompanyName(String companyName){
         List<com.niaoren.eurekaclientuser.entity.File> fileList =  fileMapper.selectByCompanyName(companyName);
        if (CollectionUtils.isEmpty(fileList)){
            return ServerResponse.createByErrorMessage("找不到该公司下的文件");
        }
        return ServerResponse.createBySuccess(fileList);
    }

    /**
     * 删除某公司文件
     * @param companyName 公司名
     * @return
     */
    public ServerResponse<String> deleteFileByCompanyName(String companyName){
        int resultCount = fileMapper.deleteByCompanyName(companyName);
        if(resultCount > 0){
            return ServerResponse.createBySuccessMessage("删除成功");
        }
        return ServerResponse.createByErrorMessage("删除失败");
    }

}
