package com.niaoren.eurekaclientsign.mapper;

import com.niaoren.eurekaclientsign.entity.Agreement;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgreementMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Agreement record);

    int insertSelective(Agreement record);

    Agreement selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Agreement record);

    int updateByPrimaryKey(Agreement record);

    //提交协议插入数据返回id
    Integer  insertSelectiveReturnId(Agreement record);

    //查出cso的签约对象列表
    List<Agreement> selectByCsoId(Integer csoId);

    //查出fac的签约对象列表
    List<Agreement> selectByFacId(Integer facID);

    //根据协议编号查询该协议
    Agreement selectByAgreementCode(String agreementCode);

    //根据协议编号修改协议内容
    int updateByAgreementCodeSelective(Agreement record);
}