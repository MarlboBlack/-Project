package com.niaoren.eurekaclientuser.mapper;

import com.niaoren.eurekaclientuser.entity.Validstate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ValidstateMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Validstate record);

    int insertSelective(Validstate record);

    Validstate selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Validstate record);

    int updateByPrimaryKey(Validstate record);

    int updateByCompanyName(Validstate validstate);

    List<Validstate> selectByAdminUserId(Integer adminUserId);


}