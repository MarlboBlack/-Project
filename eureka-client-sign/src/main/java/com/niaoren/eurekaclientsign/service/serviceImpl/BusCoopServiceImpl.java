package com.niaoren.eurekaclientsign.service.serviceImpl;

import com.google.common.collect.Lists;
import com.niaoren.eurekaclientsign.common.Const;
import com.niaoren.eurekaclientsign.common.ServerResponse;
import com.niaoren.eurekaclientsign.entity.Buscooperation;
import com.niaoren.eurekaclientsign.entity.apiEntity.Company;
import com.niaoren.eurekaclientsign.entity.apiEntity.Staff;
import com.niaoren.eurekaclientsign.mapper.BuscooperationMapper;
import com.niaoren.eurekaclientsign.service.BusCoopService;
import com.niaoren.eurekaclientsign.service.UserApiFeign;
import com.niaoren.eurekaclientsign.vo.apiVo.CompanyInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class BusCoopServiceImpl implements BusCoopService {


    @Autowired(required = false)
    private UserApiFeign userApiFeign;
    @Autowired
    private BuscooperationMapper buscooperationMapper;


    /**
     * cso获取可以合作的fac公司列表
     * @param myCompanyName
     * @return
     */
    public ServerResponse<List<CompanyInfoVo>> getfacComList(String myCompanyName){
        //获取本公司id
        Integer csoCompanyId = userApiFeign.getCompanyId(myCompanyName).getData();
        List<CompanyInfoVo> companyInfoVoList = userApiFeign.getComList(2).getData();
        if(companyInfoVoList == null){
            return ServerResponse.createByErrorMessage("未查询到公司信息");
        }
        int i = 0;
        for (CompanyInfoVo companyInfoVo:companyInfoVoList) {
            i = i+1;
            companyInfoVo.setNumber(i);//设置查出来的公司序号
            companyInfoVo.setOptions("");//设置页面操作列
            //根据企业id查询合作状态
            Integer facCompanyId = companyInfoVo.getId();
            Buscooperation buscooperation = buscooperationMapper.selectBycsoIdAndfacId(csoCompanyId,facCompanyId);
            if (buscooperation == null){
                companyInfoVo.setCooperStatus(Const.COOPER_NOTHING);
            }else{
                Integer status = buscooperation.getStatus();
                if(status == 1){
                    companyInfoVo.setCooperStatus("申请中");//cso向fac发送申请
                }else if(status == 2){
                    companyInfoVo.setCooperStatus("待审核");//fac向cso发送申请
                }else if(status == 3){
                    companyInfoVo.setCooperStatus("合作中");
                }else
                    companyInfoVo.setCooperStatus("已拒绝");
            }

        }
        return ServerResponse.createBySuccess(companyInfoVoList);
    }

    /**
     * fac获取可以合作的cso公司列表
     * @param myCompanyName
     * @return
     */
    public ServerResponse<List<CompanyInfoVo>> getcsoComList(String myCompanyName){
        //获取本公司id
        Integer facCompanyId = userApiFeign.getCompanyId(myCompanyName).getData();
        //获取cso公司列表
        List<CompanyInfoVo> companyInfoVoList = userApiFeign.getComList(3).getData();
        if(companyInfoVoList == null){
            return ServerResponse.createByErrorMessage("未查询到公司信息");
        }
        int i = 0;
        for (CompanyInfoVo companyInfoVo:companyInfoVoList) {
            i = i+1;
            companyInfoVo.setNumber(i);//设置查出来的公司序号
            companyInfoVo.setOptions("");//设置页面操作列
            //根据企业id查询与本公司的合作状态
            Integer csoCompanyId = companyInfoVo.getId();
            Buscooperation buscooperation = buscooperationMapper.selectBycsoIdAndfacId(csoCompanyId,facCompanyId);
            if (buscooperation == null){
                companyInfoVo.setCooperStatus(Const.COOPER_NOTHING);
            }
            Integer status = buscooperation.getStatus();
            if(status == 1) {
                companyInfoVo.setCooperStatus("待审核");//cso向fac发送申请
            }else if(status == 2){
                companyInfoVo.setCooperStatus("申请中");//fac向cso发送申请
            }else if(status == 3){
                companyInfoVo.setCooperStatus("合作中");
            }else
                companyInfoVo.setCooperStatus("已拒绝");
        }
        return ServerResponse.createBySuccess(companyInfoVoList);
    }

    /**
     * 根据公司名称获取公司信息
     * @param companyName
     * @return
     */
    public ServerResponse<CompanyInfoVo> getCompanyInfo(String companyName){
        return userApiFeign.getCompanyInfoBycompanyName(companyName);
    }


    /**
     * cso向工厂申请合作
     * @param myCompanyName
     * @param facCompanyName
     * @return
     */
    public ServerResponse csoApplyCoop(String myCompanyName,String facCompanyName){
        //获取本公司id
        Integer csoCompanyId = userApiFeign.getCompanyId(myCompanyName).getData();
        //获取fac公司id
        Integer facCompanyId = userApiFeign.getCompanyId(facCompanyName).getData();
        if(csoCompanyId == null||facCompanyId == null){
            return ServerResponse.createByErrorMessage("未查询到公司");
        }
        Buscooperation buscooperation = new Buscooperation();
        buscooperation.setCsoId(csoCompanyId);//csoid
        buscooperation.setFactoryId(facCompanyId);//facid
        buscooperation.setStatus(1);//cso申请
        int resultCount = buscooperationMapper.insertSelective(buscooperation);
        if(resultCount > 0){
            return ServerResponse.createBySuccessMessage("申请成功");
        }
        return ServerResponse.createByErrorMessage("申请失败");
    }

    /**
     * fac向cso申请合作
     * @param myCompanyName
     * @param csoCompanyName
     * @return
     */
    public ServerResponse facApplyCoop(String myCompanyName,String csoCompanyName){
        //获取本公司id
        Integer facCompanyId = userApiFeign.getCompanyId(myCompanyName).getData();
        //获取cso公司id
        Integer csoCompanyId = userApiFeign.getCompanyId(csoCompanyName).getData();
        if(csoCompanyId == null||facCompanyId == null){
            return ServerResponse.createByErrorMessage("未查询到公司");
        }
        Buscooperation buscooperation = new Buscooperation();
        buscooperation.setCsoId(csoCompanyId);//csoid
        buscooperation.setFactoryId(facCompanyId);//facid
        buscooperation.setStatus(2);//fac申请
        int resultCount = buscooperationMapper.insertSelective(buscooperation);
        if(resultCount > 0){
            return ServerResponse.createBySuccessMessage("申请成功");
        }
        return ServerResponse.createByErrorMessage("申请失败");
    }

    /**
     * 审核合作请求
     * @param csoCompanyName
     * @param facCompanyName
     * @param status
     * @return
     */
    public ServerResponse cooperExamine(String csoCompanyName,String facCompanyName,Integer status){
        //获取cso公司id
        Integer csoCompanyId = userApiFeign.getCompanyId(csoCompanyName).getData();
        //获取fac公司id
        Integer facCompanyId = userApiFeign.getCompanyId(facCompanyName).getData();
        if(csoCompanyId == null||facCompanyId == null){
            return ServerResponse.createByErrorMessage("未查询到公司");
        }
        Buscooperation buscooperation = new Buscooperation();
        buscooperation.setCsoId(csoCompanyId);//csoid
        buscooperation.setFactoryId(facCompanyId);//facid
        buscooperation.setStatus(status);//3-通过,4-拒绝
        int resultCount = buscooperationMapper.updateSelectiveBycsoIdfacId(buscooperation);
        if(resultCount > 0){
            return ServerResponse.createBySuccessMessage("审核成功");
        }
        return ServerResponse.createByErrorMessage("审核失败");
    }

    /**
     * fac子账号分配服务
     * @param myCompanyName
     * @param csoCompanyName
     * @param facStaffName
     * @return
     */
    public ServerResponse facDistributionService(String myCompanyName, String csoCompanyName, String facStaffName){
        //获取本公司id
        Integer facCompanyId = userApiFeign.getCompanyId(myCompanyName).getData();
        //获取cso公司id
        Integer csoCompanyId = userApiFeign.getCompanyId(csoCompanyName).getData();
        //获取fac员工id
        Integer facStaffId = userApiFeign.getStaffIdByCompanyNameName(myCompanyName,facStaffName).getData();
        if(facCompanyId==null||csoCompanyId==null||facStaffId==null){
            return ServerResponse.createByError();
        }
        Buscooperation newBuscooperation = new Buscooperation();
        newBuscooperation.setCsoId(csoCompanyId);
        newBuscooperation.setFactoryChildId(facCompanyId);
        newBuscooperation.setFactoryChildId(facStaffId);
        int resultCount = buscooperationMapper.updateSelectiveBycsoIdfacId(newBuscooperation);
        if(resultCount > 0){
            return ServerResponse.createBySuccess("分配服务成功");
        }
        return ServerResponse.createByErrorMessage("分配服务失败");

    }


    /**
     * fac子账号huoqu
     * @param facChildId
     * @return
     */
    public ServerResponse facChildGetCsoList(Integer facChildId){
        List<Integer> csoIdList = buscooperationMapper.selectCsoIdListByFacChildId(facChildId);
        if(csoIdList != null){
            StringBuffer sb = new StringBuffer();
            for(Integer id:csoIdList){
                sb.append(id+",");
            }
            sb.deleteCharAt(sb.length()-1);
            String csoIdListString = sb.toString();
            List<CompanyInfoVo> companyInfoVoList = Lists.newArrayList();
            //根据id列表获取company列表
            List<Company> csoCompanyList = userApiFeign.getCompanyListBycompanyIds(csoIdListString).getData();
            if(csoCompanyList != null){
                int x = 0;
                for(Company company:csoCompanyList){
                    CompanyInfoVo companyInfoVo = new CompanyInfoVo();
                    x = x+1;
                    companyInfoVo.setNumber(x);
                    companyInfoVo.setCompanyName(company.getName());
                    Integer i = new Integer(Integer.parseInt(company.getCreditRating()));
                    String str = "";
                    if(i>80)
                        str = "<a href='#'><span class='fa fa-star'></span></a><a href='#'><span class='fa fa-star'></span></a><a href='#'><span class='fa fa-star'></span></a><a href='#'><span class='fa fa-star'></span></a><a href='#'><span class='fa fa-star'></span></a>";
                    else if(i>60)
                        str = "<a href='#'><span class='fa fa-star'></span></a><a href='#'><span class='fa fa-star'></span></a><a href='#'><span class='fa fa-star'></span></a><a href='#'><span class='fa fa-star'></span></a>";
                    else if(i>40)
                        str = "<a href='#'><span class='fa fa-star'></span></a><a href='#'><span class='fa fa-star'></span></a><a href='#'><span class='fa fa-star'></span></a>";
                    else if(i>20)
                        str = "<a href='#'><span class='fa fa-star'></span></a><a href='#'><span class='fa fa-star'></span></a>";
                    else
                        str = "<a href='#'><span class='fa fa-star'></span></a>";
                    companyInfoVo.setCreditHtml(str);
                    companyInfoVo.setName(company.getContactName());
                    companyInfoVo.setTellPhone(company.getContactPhoneNumber());
                    Buscooperation buscooperation = buscooperationMapper.selectByCsoIdFactoryChildId(company.getId(),facChildId);
                    if (buscooperation == null){
                        companyInfoVo.setCooperStatus(Const.COOPER_NOTHING);
                    }else{
                        Integer status = buscooperation.getStatus();
                        if(status == 1){
                            companyInfoVo.setCooperStatus("申请中");//cso向fac发送申请
                        }else if(status == 2){
                            companyInfoVo.setCooperStatus("待审核");//fac向cso发送申请
                        }else if(status == 3){
                            companyInfoVo.setCooperStatus("合作中");
                        }else
                            companyInfoVo.setCooperStatus("已拒绝");
                    }
                    companyInfoVo.setOptions("");
                    companyInfoVoList.add(companyInfoVo);
                }


            }
            return ServerResponse.createBySuccess(companyInfoVoList);
        }
        return ServerResponse.createByErrorMessage("未查到合作对象");
    }

    /**
     * fac子账号向cso申请合作
     * @param facChildId
     * @param csoCompanyName
     * @return
     */
    public ServerResponse facChildApplyCoop(Integer facChildId,String csoCompanyName){

        //获取cso公司id
        Integer csoCompanyId = userApiFeign.getCompanyId(csoCompanyName).getData();
        if(csoCompanyId == null){
            return ServerResponse.createByErrorMessage("未查询到公司");
        }
        Buscooperation buscooperation = new Buscooperation();
        buscooperation.setCsoId(csoCompanyId);//csoid
        buscooperation.setFactoryChildId(facChildId);//fac子账号id
        buscooperation.setStatus(2);//fac申请
        int resultCount = buscooperationMapper.insertSelective(buscooperation);
        if(resultCount > 0){
            return ServerResponse.createBySuccessMessage("申请成功");
        }
        return ServerResponse.createByErrorMessage("申请失败");
    }


}
