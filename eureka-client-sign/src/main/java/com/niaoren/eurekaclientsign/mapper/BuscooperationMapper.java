package com.niaoren.eurekaclientsign.mapper;

import com.niaoren.eurekaclientsign.entity.Buscooperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuscooperationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Buscooperation record);

    int insertSelective(Buscooperation record);

    Buscooperation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Buscooperation record);

    int updateByPrimaryKey(Buscooperation record);

    //根据公司id查询合作
    Buscooperation selectBycsoIdAndfacId(@Param("csoId")Integer csoId, @Param("facId")Integer facId);

    //根据公司id更新合作
    int updateSelectiveBycsoIdfacId(Buscooperation record);

    //根据csoid查和它合作的fac列表
    List<Integer> selectfacIdList(Integer csoId);

    //根据facid查和它合作的cso列表
    List<Integer> selectcsoIdList(Integer facId);

    //根据fac子账号id查询合作cso列表
    List<Integer> selectCsoIdListByFacChildId(Integer factoryChildId);

    //根据csoId和fac子账号的id查询合作
    Buscooperation selectByCsoIdFactoryChildId(@Param("csoId")Integer csoId,@Param("factoryChildId")Integer factoryChildId);
}