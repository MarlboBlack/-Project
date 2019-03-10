package com.niaoren.eurekaclientsign.mapper;

import com.niaoren.eurekaclientsign.entity.ProtocolservicePrice;
import org.springframework.stereotype.Repository;

@Repository
public interface ProtocolservicePriceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProtocolservicePrice record);

    int insertSelective(ProtocolservicePrice record);

    ProtocolservicePrice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProtocolservicePrice record);

    int updateByPrimaryKey(ProtocolservicePrice record);

    ProtocolservicePrice selectByAgreementId(Integer agreementId);
}