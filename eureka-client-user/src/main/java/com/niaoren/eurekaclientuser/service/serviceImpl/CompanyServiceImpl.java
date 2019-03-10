package com.niaoren.eurekaclientuser.service.serviceImpl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.niaoren.eurekaclientuser.Utils.redis.JedisUtil;
import com.niaoren.eurekaclientuser.Utils.redis.UserKey;
import com.niaoren.eurekaclientuser.common.Const;
import com.niaoren.eurekaclientuser.common.ServerResponse;

import com.niaoren.eurekaclientuser.entity.Company;
import com.niaoren.eurekaclientuser.entity.File;
import com.niaoren.eurekaclientuser.entity.Staff;
import com.niaoren.eurekaclientuser.entity.Validstate;
import com.niaoren.eurekaclientuser.mapper.CompanyMapper;
import com.niaoren.eurekaclientuser.mapper.FileMapper;
import com.niaoren.eurekaclientuser.mapper.StaffMapper;
import com.niaoren.eurekaclientuser.mapper.ValidstateMapper;
import com.niaoren.eurekaclientuser.service.CompanyService;
import com.niaoren.eurekaclientuser.vo.CompanyInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private ValidstateMapper validstateMapper;
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private StaffMapper staffMapper;
    @Autowired
    private JedisUtil jedisUtil;


    /**
     * 校验参数是否已存在方法
     * @param str 参数value
     * @param type 参数类型
     * @return
     */
    public ServerResponse<String> checkVaild(String str, String type){
       if (StringUtils.isNotBlank(type)){
           //开始校验
           if(Const.NAME.equals(type)) {
               int resultCount = companyMapper.checkName(str);
               if (resultCount > 0) {
                   return ServerResponse.createByErrorMessage("企业名已存在");
               }
           }
           if (Const.EMAIL.equals(type)){
               int resultCount = companyMapper.checkEmail(str);
               if(resultCount > 0){
                   return ServerResponse.createByErrorMessage("邮箱已注册");
               }
           }
       }else{
           return ServerResponse.createByErrorMessage("参数错误");
       }
       return ServerResponse.createBySuccessMessage("校验成功");
    }

    /**
     * 获取当前公司主页信息（返回组装类companyInfoVo）
     * @param request
     * @return
     */
    public ServerResponse getInformation(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        for (Cookie c : cookies) {
            if (Const.COOKIE_NAME_TOKEN.equals(c.getName())) {
                String token = c.getValue();
                Staff currentStaff = jedisUtil.get(UserKey.token, token, Staff.class);
                //-------此处用公司编号更好，说明公司审核通过了
                String companyName = currentStaff.getCompanyName();
                CompanyInfoVo companyInfoVo = new CompanyInfoVo();
                companyInfoVo.setUserhead(currentStaff.getHeadimg());
                companyInfoVo.setLastLogin(currentStaff.getLastLogin());
                companyInfoVo.setAcctNumOrPhone(currentStaff.getUsername());
                companyInfoVo.setNickName(currentStaff.getNickname());
                companyInfoVo.setAgent(currentStaff.getAgentName());
                companyInfoVo.setDepartment(currentStaff.getDepartment());
                companyInfoVo.setPost(currentStaff.getJob());
                companyInfoVo.setArea(currentStaff.getArea());
                companyInfoVo.setQqnumber(currentStaff.getQq());
                companyInfoVo.setRealName(currentStaff.getName());
                companyInfoVo.setWechat(currentStaff.getWechat());
                companyInfoVo.setWorkAptitude(currentStaff.getWorkAptitude());
                Company company = companyMapper.selectByCompanyName(companyName);
                if(company == null){
                    return ServerResponse.createByErrorMessage("找不到当前公司信息");
                }
                //在组装类companyInfoVo中放入company信息
                companyInfoVo.setCode(company.getCode());
                companyInfoVo.setCompanyName(company.getName());
                companyInfoVo.setSynopsis(company.getSynopsis());
                companyInfoVo.setEmail(company.getEmail());
                companyInfoVo.setAddress(company.getWorkAddress());
                companyInfoVo.setWebadress(company.getWebsite());
                companyInfoVo.setRedNumber(company.getRecordCode());
                companyInfoVo.setCreditCode(company.getUnifyCreditCode());
                companyInfoVo.setName(company.getContactName());
                companyInfoVo.setIdNumber(company.getContactIdNumber());
                companyInfoVo.setTellPhone(company.getContactPhoneNumber());
                companyInfoVo.setOpenBank(company.getBank());
                companyInfoVo.setBankNum(company.getBankNumber());
                companyInfoVo.setGmpNumber(company.getGmpNumber());
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
                companyInfoVo.setJoinTime(company.getTime());
                //在组装类companyInfoVo中放入file信息
                List<File> filelist =  fileMapper.selectByCompanyName(companyName);
                for(File file:filelist){
                    if("1".equals(file.getType().toString())){
                        companyInfoVo.setAptitude(file.getFtpAddress());
                    }
                    else if("2".equals(file.getType().toString())){
                        companyInfoVo.setBusinLicence(file.getFtpAddress());
                    }else if("3".equals(file.getType().toString())){
                        companyInfoVo.setOpenPermit(file.getFtpAddress());
                    }else if("4".equals(file.getType().toString())){
                        companyInfoVo.setSeal(file.getFtpAddress());
                    }else if("5".equals(file.getType().toString())){
                        companyInfoVo.setControlSystem(file.getFtpAddress());
                    }else if("6".equals(file.getType().toString())){
                        companyInfoVo.setDrugAppr(file.getFtpAddress());
                    }else if("7".equals(file.getType().toString())){
                        companyInfoVo.setGmpPermit(file.getFtpAddress());
                    }else if("8".equals(file.getType().toString())){
                        companyInfoVo.setIdNumberImg(file.getFtpAddress());
                    }
                }
                return ServerResponse.createBySuccess(companyInfoVo);

            }
        }
        return ServerResponse.createByErrorMessage("请重新登录");
    }

    /**
     * 获取cso或fac公司信息列表
     * @param type
     * @return
     */
    public ServerResponse<List<CompanyInfoVo>> getComList(Integer type){
        List<CompanyInfoVo> csofacList = Lists.newArrayList();
        List<Company> companyList = companyMapper.selectByCompanyType(type);
        if(companyList == null){
            return ServerResponse.createByErrorMessage("没有查询到该类型公司");
        }
        int i = 0;
        for (Company company:companyList) {
            i = i+1;
            CompanyInfoVo companyInfoVo = new CompanyInfoVo();
            companyInfoVo.setId(company.getId());//公司id
            companyInfoVo.setNumber(i);//序号
            companyInfoVo.setCompanyName(company.getName());//企业名称
            Integer x = new Integer(Integer.parseInt(company.getCreditRating()));
            String str = "";
            if(x>80)
                str = "<a href='#'><span class='fa fa-star'></span></a><a href='#'><span class='fa fa-star'></span></a><a href='#'><span class='fa fa-star'></span></a><a href='#'><span class='fa fa-star'></span></a><a href='#'><span class='fa fa-star'></span></a>";
            else if(x>60)
                str = "<a href='#'><span class='fa fa-star'></span></a><a href='#'><span class='fa fa-star'></span></a><a href='#'><span class='fa fa-star'></span></a><a href='#'><span class='fa fa-star'></span></a>";
            else if(x>40)
                str = "<a href='#'><span class='fa fa-star'></span></a><a href='#'><span class='fa fa-star'></span></a><a href='#'><span class='fa fa-star'></span></a>";
            else if(x>20)
                str = "<a href='#'><span class='fa fa-star'></span></a><a href='#'><span class='fa fa-star'></span></a>";
            else
                str = "<a href='#'><span class='fa fa-star'></span></a>";
            companyInfoVo.setCreditHtml(str);//企业信用等级
            companyInfoVo.setName(company.getContactName());//联系人姓名
            companyInfoVo.setTellPhone(company.getContactPhoneNumber());//手机号码
            companyInfoVo.setJoinTime(company.getTime());//注册日期
            companyInfoVo.setValidState(company.getValidState());//审核状态
            companyInfoVo.setOptions("");//操作
            csofacList.add(companyInfoVo);
        }
        return ServerResponse.createBySuccess(csofacList);
    }

    /**
     * 根据公司名称获取公司id
     * @param companyName
     * @return
     */
    public ServerResponse<Integer> getCompanyId(String companyName){
        Integer id = companyMapper.selectIdByCompanyName(companyName);
        if(id == null){
            return ServerResponse.createByErrorMessage("未查询到id");
        }
        return ServerResponse.createBySuccess(id);
    }

    /**
     * 根据id列表查询公司信息返回一个公司信息列表
     * @param idListString
     * @return
     */
    public ServerResponse<List<Company>> getCompanyInfoByIds(String idListString){
        //将字符串转为List
        String[] a = idListString.split(",");
        List<String> idList = Arrays.asList(a);
        List<Company> companyList = Lists.newArrayList();
        for (String s:idList) {
            Integer companyId = Integer.parseInt(s);
            Company company = companyMapper.selectByPrimaryKey(companyId);
            if(company == null){
                return ServerResponse.createByErrorMessage("未查询到该公司");
            }
            companyList.add(company);
        }
        return ServerResponse.createBySuccess(companyList);
    }

    /**
     * 根据公司名称获取员工信息列表
     * @param compangName
     * @return
     */
    public ServerResponse<List<Staff>> getStaffInfoByCompanyName(String compangName) {
        List<Staff> staffList = staffMapper.selectByCompanyName(compangName);
        if(staffList == null) {
            return ServerResponse.createByError();
        }
        return ServerResponse.createBySuccess(staffList);
    }

    /**
     * 获取admin子账号及代理商username列表
     * @param compangName
     * @return
     */
    public ServerResponse<List<String>> getAdminStaffNameList(String compangName) {
        List<Staff> staffList = staffMapper.selectByCompanyName(compangName);
        if(staffList == null) {
            return ServerResponse.createByError();
        }
        List<String> staffNameList = Lists.newArrayList();
        for(Staff staff:staffList){
            String userName = staff.getUsername();
            staffNameList.add(userName);//添加admin子账号username
        }
        List<Staff> agentList = staffMapper.selectByUserType(2);
        if(agentList != null){
            for(Staff staff:agentList){
                String userName = staff.getUsername();
                staffNameList.add(userName);//添加代理商username
            }
        }
        return ServerResponse.createBySuccess(staffNameList);
    }

    /**
     * 根据 公司名称，员工名称获取电话号码
     * @param companyName
     * @param staffName
     * @return
     */
    public ServerResponse<String> getUsername(String companyName,String staffName){
        String username = staffMapper.getUsernameByCompNameRealName(companyName,staffName);
        if(username == null){
            return ServerResponse.createByError();
        }
        return ServerResponse.createBySuccess(username);
    }

    /**
     * 根据公司名称，员工名称获取员工id
     * @param companyName
     * @param staffName
     * @return
     */
    public ServerResponse<Integer> getStaffIdByCompanyNameName(String companyName,String staffName){
        Integer staffId = staffMapper.getIdByNameCompanyName(companyName,staffName);
        if(staffId == null){
            return ServerResponse.createByError();
        }
        return ServerResponse.createBySuccess(staffId);
    }

    /**
     * 根据公司id获取公司名称
     * @param companyId
     * @return
     */
    public ServerResponse<String> getCompanyName(Integer companyId){
        String name = companyMapper.selectCompanyNameById(companyId);
        if(name == null){
            return ServerResponse.createByError();
        }
        return ServerResponse.createBySuccess(name);
    }
    /**
     * 根据公司名称获取公司信息
     * @param companyName
     * @return
     */
    public ServerResponse<CompanyInfoVo> getCompanyInfo(String companyName){
        CompanyInfoVo companyInfoVo = new CompanyInfoVo();
        Company company = companyMapper.selectByCompanyName(companyName);
        if(company == null){
            return ServerResponse.createByErrorMessage("找不到当前公司信息");
        }
        //在组装类companyInfoVo中放入company信息
        companyInfoVo.setId(company.getId());
        companyInfoVo.setCode(company.getCode());
        companyInfoVo.setCompanyName(company.getName());
        companyInfoVo.setSynopsis(company.getSynopsis());
        companyInfoVo.setEmail(company.getEmail());
        companyInfoVo.setAddress(company.getWorkAddress());
        companyInfoVo.setWebadress(company.getWebsite());
        companyInfoVo.setRedNumber(company.getRecordCode());
        companyInfoVo.setCreditCode(company.getUnifyCreditCode());
        companyInfoVo.setName(company.getContactName());
        companyInfoVo.setIdNumber(company.getContactIdNumber());
        companyInfoVo.setTellPhone(company.getContactPhoneNumber());
        companyInfoVo.setOpenBank(company.getBank());
        companyInfoVo.setBankNum(company.getBankNumber());
        companyInfoVo.setGmpNumber(company.getGmpNumber());
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
        companyInfoVo.setJoinTime(company.getTime());
        //在组装类companyInfoVo中放入file信息
        List<File> filelist =  fileMapper.selectByCompanyName(companyName);
        for(File file:filelist){
            if("1".equals(file.getType().toString())){
                companyInfoVo.setAptitude(file.getFtpAddress());
            }
            else if("2".equals(file.getType().toString())){
                companyInfoVo.setBusinLicence(file.getFtpAddress());
            }else if("3".equals(file.getType().toString())){
                companyInfoVo.setOpenPermit(file.getFtpAddress());
            }else if("4".equals(file.getType().toString())){
                companyInfoVo.setSeal(file.getFtpAddress());
            }else if("5".equals(file.getType().toString())){
                companyInfoVo.setControlSystem(file.getFtpAddress());
            }else if("6".equals(file.getType().toString())){
                companyInfoVo.setDrugAppr(file.getFtpAddress());
            }else if("7".equals(file.getType().toString())){
                companyInfoVo.setGmpPermit(file.getFtpAddress());
            }else if("8".equals(file.getType().toString())){
                companyInfoVo.setIdNumberImg(file.getFtpAddress());
            }
        }
        return ServerResponse.createBySuccess(companyInfoVo);
    }

    /**
     * 公司注册 提交审核
     * @param company 注册公司信息
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse<String> registerCompany(Company company){
        ServerResponse validResponse = this.checkVaild(company.getName(),Const.NAME);
        if(!validResponse.isSuccess()){
            return validResponse;
        }
        validResponse = this.checkVaild(company.getEmail(),Const.EMAIL);
        if (!validResponse.isSuccess()){
            return validResponse;
        }
            try {
                companyMapper.insertSelective(company);
                Validstate validstate = new Validstate();
                //设置公司名称
                validstate.setCompanyName(company.getName());
                //设置提交状态
                validstate.setSubState(0);
                //设置审核状态
                validstate.setAuditState(0);
                //设置通过状态
                validstate.setPassState(0);
                validstateMapper.insert(validstate);
                return ServerResponse.createBySuccessMessage("已提交管理员审核,我们会在3天内告知审核结果");
            }catch(Exception e){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ServerResponse.createByErrorMessage("提交发生异常，请重试");
            }
    }

    //admin分配子账号客服
    public ServerResponse<String> distributeAdminUser(String companyName,String adminStaffUserName){
        Staff staff = staffMapper.selectByUsername(adminStaffUserName);
        Integer id = staff.getId();
        Validstate validstate = new Validstate();
        validstate.setCompanyName(companyName);//公司名称
        validstate.setAdminUserId(id);//admin子账号id
        int resultCount = validstateMapper.updateByCompanyName(validstate);
        if(resultCount > 0){
            return ServerResponse.createBySuccess("分配成功");
        }
        return ServerResponse.createByErrorMessage("分配失败");
    }

    //admin子账号获取公司列表
    public ServerResponse adminStaffGetCompanyList(Integer myId,Integer companyType){
        //根据自己的id去valid表查公司名称列表
        List<Validstate> validstateList = validstateMapper.selectByAdminUserId(myId);
        if(validstateList == null){
            return ServerResponse.createByError();
        }
        List<CompanyInfoVo> csoList = Lists.newArrayList();
        int i = 0;
        for(Validstate validstate:validstateList){
            String companyName = validstate.getCompanyName();
            Company company = companyMapper.selectByCompanyName(companyName);
            if(company != null){
                Integer type = company.getType();
                if(type == companyType){
                    i = i+1;
                    CompanyInfoVo companyInfoVo = new CompanyInfoVo();
                    companyInfoVo.setId(company.getId());//公司id
                    companyInfoVo.setNumber(i);//序号
                    companyInfoVo.setCompanyName(company.getName());//企业名称
                    Integer x = new Integer(Integer.parseInt(company.getCreditRating()));
                    String str = "";
                    if(x>80)
                        str = "<a href='#'><span class='fa fa-star'></span></a><a href='#'><span class='fa fa-star'></span></a><a href='#'><span class='fa fa-star'></span></a><a href='#'><span class='fa fa-star'></span></a><a href='#'><span class='fa fa-star'></span></a>";
                    else if(x>60)
                        str = "<a href='#'><span class='fa fa-star'></span></a><a href='#'><span class='fa fa-star'></span></a><a href='#'><span class='fa fa-star'></span></a><a href='#'><span class='fa fa-star'></span></a>";
                    else if(x>40)
                        str = "<a href='#'><span class='fa fa-star'></span></a><a href='#'><span class='fa fa-star'></span></a><a href='#'><span class='fa fa-star'></span></a>";
                    else if(x>20)
                        str = "<a href='#'><span class='fa fa-star'></span></a><a href='#'><span class='fa fa-star'></span></a>";
                    else
                        str = "<a href='#'><span class='fa fa-star'></span></a>";
                    companyInfoVo.setCreditHtml(str);//企业信用等级
                    companyInfoVo.setName(company.getContactName());//联系人姓名
                    companyInfoVo.setTellPhone(company.getContactPhoneNumber());//手机号码
                    companyInfoVo.setJoinTime(company.getTime());//注册日期
                    companyInfoVo.setValidState(company.getValidState());//审核状态
                    companyInfoVo.setOptions("");//操作
                    csoList.add(companyInfoVo);
                }
            }
        }
        return ServerResponse.createBySuccess(csoList);
    }







}
