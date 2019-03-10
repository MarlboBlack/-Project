package com.niaoren.eurekaclientuser.mapper;

import com.niaoren.eurekaclientuser.entity.File;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(File record);

    int insertSelective(File record);

    File selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(File record);

    int updateByPrimaryKey(File record);

    List<File> selectByCompanyName(String companyName);

    int deleteByCompanyName(String companyName);

    /**
     * 查询本公司本类型文件是否已经存在
     * @param companyName
     * @param type
     * @return
     */
    File selectExists(@Param("companyName") String companyName, @Param("type") Integer type);

}