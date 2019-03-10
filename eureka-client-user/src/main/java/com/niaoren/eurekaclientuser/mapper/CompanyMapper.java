package com.niaoren.eurekaclientuser.mapper;

import com.niaoren.eurekaclientuser.entity.Company;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CompanyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Company record);

    int insertSelective(Company record);

    Company selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Company record);

    int updateByPrimaryKey(Company record);

    int checkName(String name);

    int checkEmail(String email);

    Company selectByCompanyName(String name);

    int deleteByCompanyName(String name);

    List<Company> selectByCompanyType(Integer type);

    int updateByCompanyNameSelective(Company company);

    Integer selectIdByCompanyName(String name);

    String selectCompanyNameById(Integer companyId);

}