package com.niaoren.eurekaclientuser.service.serviceImpl;

import com.google.common.collect.Lists;
import com.niaoren.eurekaclientuser.Utils.JavaSmsApi;
import com.niaoren.eurekaclientuser.Utils.MD5Util;
import com.niaoren.eurekaclientuser.Utils.redis.JedisUtil;
import com.niaoren.eurekaclientuser.Utils.redis.UserKey;
import com.niaoren.eurekaclientuser.common.Const;
import com.niaoren.eurekaclientuser.common.ServerResponse;
import com.niaoren.eurekaclientuser.common.LocalCache;

import com.niaoren.eurekaclientuser.entity.Company;
import com.niaoren.eurekaclientuser.entity.File;
import com.niaoren.eurekaclientuser.entity.Staff;
import com.niaoren.eurekaclientuser.entity.Validstate;
import com.niaoren.eurekaclientuser.mapper.CompanyMapper;
import com.niaoren.eurekaclientuser.mapper.FileMapper;
import com.niaoren.eurekaclientuser.mapper.StaffMapper;
import com.niaoren.eurekaclientuser.mapper.ValidstateMapper;
import com.niaoren.eurekaclientuser.service.FileService;
import com.niaoren.eurekaclientuser.service.StaffService;
import com.niaoren.eurekaclientuser.vo.CompanyInfoVo;
import com.niaoren.eurekaclientuser.vo.LoginTotleVo;
import com.niaoren.eurekaclientuser.vo.StaffInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;


@Service
@Slf4j
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffMapper staffMapper;
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private ValidstateMapper validstateMapper;
    @Autowired
    private FileService fileService;
    @Autowired
    private JedisUtil jedisUtil;




    /**
     * 校验参数是否已存在方法
     *
     * @param str  参数value
     * @param type 参数类型
     * @return
     */
    public ServerResponse<String> checkVaild(String str, String type) {
        if (StringUtils.isNotBlank(type)) {
            //开始校验
            if (Const.USERNAME.equals(type)) {
                int resultCount = staffMapper.checkUsername(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    //验证码 6位
    private String getValidateCode() {
        Random random = new Random();
        String str = random.nextInt(1000000) + "";
        if (str.length() != 6) {
            return getValidateCode();
        }
        return str;
    }

    /**
     * 用户注册信息填写，获取验证码
     *
     * @param username 用户名
     * @return
     */

    public ServerResponse<String> register(String username) {
        ServerResponse validResponse = this.checkVaild(username, Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        //短信验证
        //1.生成6位验证码
        String code = this.getValidateCode();
        //2.将用户的手机号信息和验证码放入缓存
        LocalCache.setKey(LocalCache.VALID_PREFIX +username, code);
        //3.发送信息
        JavaSmsApi.SendSmsMessage(username, code);
        return ServerResponse.createBySuccessMessage("验证码已发送，请注意接收");
    }

    /**
     * 用户注册提交验证码验证
     *
     * @param staff 注册用户的信息
     * @param code  验证码
     * @return
     */
    public ServerResponse<String> ValidateCode(Staff staff, Integer userType,String code) {
        ServerResponse validResponse = this.checkVaild(staff.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        if (StringUtils.isBlank(code)) {
            return ServerResponse.createByErrorMessage("请输入验证码");
        }
        //取出缓存中的验证码
        String cacheCode = LocalCache.getKey(LocalCache.VALID_PREFIX + staff.getUsername());
        if (StringUtils.isBlank(cacheCode)) {
            return ServerResponse.createByErrorMessage("验证码已过期");
        }
        if (StringUtils.equals(cacheCode, code)) {
            //md5加密
            staff.setPassword(MD5Util.MD5EncodeUtf8(staff.getPassword()));
            //设置用户角色
            staff.setUserType(userType);
            int resultCount = staffMapper.insertSelective(staff);
            if (resultCount > 0) {
                return ServerResponse.createBySuccessMessage("注册成功");
            }
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createByErrorMessage("验证码错误");
    }

    /**
     * 已改
     * staff登录
     * @param username 用户名
     * @param password 密码
     * @return
     */
    public ServerResponse<Staff> login(String username, String password, HttpServletResponse response){
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        Staff staff = staffMapper.selectLogin(username,md5Password);
        if(staff == null){
            return ServerResponse.createByErrorMessage("用户名或密码错误");
        }
        //将用户信息存入redis中，并把redis的key值放入cookie
        String token = UUID.randomUUID().toString().replace("-","");
        jedisUtil.set(UserKey.token,token,staff);
        Cookie cookie = new Cookie(Const.COOKIE_NAME_TOKEN,token);
        cookie.setMaxAge(UserKey.token.expireSeconds());
        cookie.setMaxAge(3600);
        cookie.setPath("/");
        response.addCookie(cookie);
        //更新上次登录时间
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String lastLogin = sdf.format(now);
        staff.setLastLogin(lastLogin);
        staffMapper.updateByPrimaryKeySelective(staff);
        //将用户密码置为空之后将用户信息返回给用户
        staff.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功",staff);
    }

    /**
     * 已改
     * 登录状态下更改密码
     * @param passwordOld 旧密码
     * @param passwordNew 新密码
     * @param request
     * @return
     */
    public ServerResponse<String> updatePassword(String passwordOld, String passwordNew, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for(Cookie c : cookies){
            if(Const.COOKIE_NAME_TOKEN.equals(c.getName())){
                String token = c.getValue();
                Staff currentStaff = jedisUtil.get(UserKey.token, token, Staff.class);
                if(currentStaff  == null)
                    return ServerResponse.createByErrorMessage("请重新登录");
                //确认是本人操作
                int resultCount = staffMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),currentStaff.getId());
                if(resultCount == 0){
                    return ServerResponse.createByErrorMessage("密码错误");
                }
                currentStaff.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
                resultCount = staffMapper.updateByPrimaryKeySelective(currentStaff);
                if (resultCount >0){
                    return ServerResponse.createBySuccessMessage("更改密码成功");
                }
            }
        }
        return ServerResponse.createByErrorMessage("更改密码失败");
    }

    /**
     * 上传头像
     * @return
     */
    public ServerResponse<String> updateHeadImg(HttpServletRequest request,MultipartFile headImg){
        Cookie[] cookies = request.getCookies();
        for(Cookie c : cookies){
            if(Const.COOKIE_NAME_TOKEN.equals(c.getName())){
                String token  = c.getValue();
                Staff currentStaff = jedisUtil.get(UserKey.token, token, Staff.class);
                if(currentStaff  == null)
                    return ServerResponse.createByErrorMessage("请重新登录");
                //设置路径，服务器项目根目录下的uploadfile
//                String path = request.getServletContext().getContextPath();
                String path = "D:/yidao/gateway/gateway/src/main/resources/static/upload/headImg";
                //发送文件到ftp服务器，返回文件在服务器上的名字
                String targetFileName = fileService.uploadFile(headImg, path);
//                String url = Const.FTP_SERVER_HTTP_PREFIX + targetFileName;
                String url = "upload/headImg/"+targetFileName;
                currentStaff.setHeadimg(url);
                int resultCount = staffMapper.updateByPrimaryKeySelective(currentStaff);
                if (resultCount > 0){
                    return ServerResponse.createBySuccessMessage("上传头像成功");
                }
            }
        }
        return ServerResponse.createByErrorMessage("上传头像失败");
    }

    /**
     * 登录状态下获取用户信息
     * @param request
     * @return
     */
    public ServerResponse<Staff> getInformation(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        for (Cookie c : cookies) {
            if (Const.COOKIE_NAME_TOKEN.equals(c.getName())) {
                String token = c.getValue();
                Staff currentStaff = jedisUtil.get(UserKey.token, token, Staff.class);
                if(currentStaff  == null)
                    return ServerResponse.createByErrorMessage("请重新登录");
                Staff staff = staffMapper.selectByPrimaryKey(currentStaff.getId());
                if (staff == null)
                    return ServerResponse.createByErrorMessage("找不到当前用户");
                staff.setPassword(null);
                return ServerResponse.createBySuccess(staff);
            }
        }
        return ServerResponse.createByErrorMessage("用户未登录");
    }



    /**
     * OK
     * 登录状态下更新个人信息
     * @param staff 要更新的用户
     * @return
     */
    public ServerResponse<Staff> updateInformation(HttpServletRequest request,Staff staff){
        Cookie[] cookies = request.getCookies();
        for(Cookie c : cookies){
            if(Const.COOKIE_NAME_TOKEN.equals(c.getName())){
                String token = c.getValue();
                Staff currentStaff = jedisUtil.get(UserKey.token, token, Staff.class);
                if(currentStaff  == null)
                    return ServerResponse.createByErrorMessage("请重新登录");
                staff.setId(currentStaff.getId());//id不变,作为主键值查询
                int updateCount = staffMapper.updateByPrimaryKeySelective(staff);
                if(updateCount > 0){
                    //将用户在redis中的信息更改（token是不变的）
                    jedisUtil.set(UserKey.token,token,staff);
                    return ServerResponse.createBySuccess("更新个人信息成功",staff);
                }
            }
        }
        return ServerResponse.createByErrorMessage("更新个人信息失败");
    }


    /**
     * 登录状态下获取子账号列表
     * @param request
     * @return
     */
    public ServerResponse<List<StaffInfoVo>> getChildStaff(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        for(Cookie c : cookies){
            if(Const.COOKIE_NAME_TOKEN.equals(c.getName())){
                String token = c.getValue();
                Staff currentStaff = jedisUtil.get(UserKey.token, token, Staff.class);
                if(currentStaff == null)
                    return ServerResponse.createByErrorMessage("请重新登录");
                String companyName = currentStaff.getCompanyName();
                //用一个arrayList来接收子账号
                List<StaffInfoVo> staffInfoVoList = Lists.newArrayList();
                List<Staff> staffList = staffMapper.selectByCompanyName(companyName);
                if(staffList != null){
                    int i = 0;
                    for (Staff staff:staffList) {
                        i = i+1;
                        StaffInfoVo staffInfoVo = new StaffInfoVo();
                        staffInfoVo.setNumber(i);//序号
                        staffInfoVo.setPost(staff.getJob());//岗位
                        staffInfoVo.setName(staff.getName());//真实姓名
                        staffInfoVo.setAcctNumOrPhone(staff.getUsername());//手机号（账号）
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        staffInfoVo.setRegisterTime(sdf.format(staff.getTime()));//注册时间
                        staffInfoVo.setOptions("");//操作
                        staffInfoVo.setArea(staff.getArea());//负责区域（工厂子账号）
                        staffInfoVoList.add(staffInfoVo);
                    }
                    return ServerResponse.createBySuccess(staffInfoVoList);
                }
            }
        }
        return ServerResponse.createByErrorMessage("没有查询到该公司员工");
    }


    /**
     * 管理员添加员工
     * @param staff 要添加的员工
     * @param companyCode 公司编号
     * @return
     */
    public ServerResponse<String> addStaff(Staff staff,String companyCode){
        ServerResponse validResponse = this.checkVaild(staff.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        staff.setPassword(MD5Util.MD5EncodeUtf8("12345678"));
        staff.setCompanyCode(companyCode);
        int resultCount = staffMapper.insertSelective(staff);
        if (resultCount > 0) {
            return ServerResponse.createBySuccessMessage("添加员工成功");
        }
        return ServerResponse.createByErrorMessage("添加员工失败");
    }

    /**
     * 管理员删除员工
     * @param username
     * @return
     */
    public ServerResponse<String> deleteStaff(String username){
        int resultCount = staffMapper.deleteByUsername(username);
        if(resultCount > 0){
            return ServerResponse.createBySuccessMessage("删除成功");
        }
        return ServerResponse.createByErrorMessage("删除失败");
    }

    /**
     * 管理员修改员工信息
     * @param staffInfoVo
     * @param phoneNumberOld
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse<String> updateStaff(StaffInfoVo staffInfoVo,String phoneNumberOld){
        try {
            Staff staff = new Staff();
            staff.setCompanyName(staffInfoVo.getCompanyName());//公司名称
            staff.setUsername(staffInfoVo.getAcctNumOrPhone());//用户名（电话号码）
            staff.setNickname(staffInfoVo.getNickName());//昵称
            staff.setAgentName(staffInfoVo.getAgent());//代理商名字
            staff.setQq(staffInfoVo.getQqnumber());//QQ
            staff.setWechat(staffInfoVo.getWechat());//微信
            staff.setDepartment(staffInfoVo.getDepartment());//部门
            staff.setJob(staffInfoVo.getPost());//岗位
            staff.setName(staffInfoVo.getName());//真实姓名
            staff.setArea(staffInfoVo.getArea());//负责区域
            staffMapper.updateByUsernameSelective(staff,phoneNumberOld);
            Company company = new Company();
            company.setName(staffInfoVo.getCompanyName());//公司名称
            company.setWorkAddress(staffInfoVo.getAddress());//联系地址
            companyMapper.updateByCompanyNameSelective(company);
            return ServerResponse.createBySuccessMessage("更新成功");
        }catch(Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ServerResponse.createByErrorMessage("提交发生异常，请重试");
        }
    }


    /**
     * 管理员查看员工信息
     * @param username
     * @return
     */
    public ServerResponse<CompanyInfoVo> selectStaff(String username){
        Staff staff = staffMapper.selectByUsername(username);
        if(staff == null){
            return ServerResponse.createByErrorMessage("查询不到该员工信息");
        }
        CompanyInfoVo companyInfoVo = new CompanyInfoVo();
        companyInfoVo.setCompanyName(staff.getCompanyName());//公司名称
        companyInfoVo.setAcctNumOrPhone(staff.getUsername());//手机号（账号）
        companyInfoVo.setNickName(staff.getNickname());//昵称
        companyInfoVo.setAgent(staff.getAgentName());//代理商名称
        companyInfoVo.setRealName(staff.getName());//真实姓名
        companyInfoVo.setContactAddress(staff.getContactAddress());//联系地址（代理商必填）
        companyInfoVo.setQqnumber(staff.getQq());//QQ
        companyInfoVo.setWechat(staff.getWechat());//微信号
        companyInfoVo.setArea(staff.getArea());//负责区域（facChild）
        companyInfoVo.setDepartment(staff.getDepartment());//部门
        companyInfoVo.setPost(staff.getJob());//岗位
        return ServerResponse.createBySuccess(companyInfoVo);
    }

    /**
     * 公司管理员重置员工密码
     * @param username 要重置密码的用户
     * @return
     */
    public ServerResponse<String> resetPassword(String username) {
            String rsetPassword = MD5Util.MD5EncodeUtf8("123456");
            int resultCount = staffMapper.resetPassword(username,rsetPassword);
            if (resultCount > 0){
                return ServerResponse.createBySuccessMessage("重置密码成功");
            }
            return ServerResponse.createByErrorMessage("重置密码失败，请重试");


    }

    /**
     * 提交公司资料
     * @param name
     * @param unifyCreditCode
     * @param contactName
     * @param contactIdNumber
     * @param contactPhoneNumber
     * @param synopsis
     * @param email
     * @param workAddress
     * @param recordCode
     * @param website
     * @param bank
     * @param bankNumber
     * @param gmpNumber
     * @param businessLicense
     * @param openingPermit
     * @param officialSeal
     * @param qualificationCertificate
     * @param enterpriseSystem
     * @param GPMcertificate
     * @param medicineOfficial
     * @param contactIdCard
     * @param servletContext
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse submitFile(String name,
                                     String unifyCreditCode,
                                     String contactName,
                                     String contactIdNumber,
                                     String contactPhoneNumber,
                                     String synopsis,
                                     String email,
                                     String workAddress,
                                     String recordCode,
                                     String website,
                                     String bank,
                                     String bankNumber,
                                     String gmpNumber,
                                     MultipartFile businessLicense,
                                     MultipartFile openingPermit,
                                     MultipartFile officialSeal,
                                     MultipartFile[] qualificationCertificate,
                                     MultipartFile enterpriseSystem,
                                     MultipartFile GPMcertificate,
                                     MultipartFile medicineOfficial,
                                     MultipartFile contactIdCard,
                                     ServletContext servletContext){
        try {
            Company newCompany = new Company();
            newCompany.setName(name);
            newCompany.setUnifyCreditCode(unifyCreditCode);
            newCompany.setContactName(contactName);
            newCompany.setContactIdNumber(contactIdNumber);
            newCompany.setContactPhoneNumber(contactPhoneNumber);
            newCompany.setSynopsis(synopsis);
            newCompany.setEmail(email);
            newCompany.setWorkAddress(workAddress);
            newCompany.setRecordCode(recordCode);
            newCompany.setWebsite(website);
            newCompany.setBank(bank);
            newCompany.setBankNumber(bankNumber);
            newCompany.setGmpNumber(gmpNumber);

            int resultCount = companyMapper.checkName(name);
            if(resultCount > 0){
                return ServerResponse.createByErrorMessage("企业名已存在");
            }
            companyMapper.insertSelective(newCompany);
            //资质证书
            if(qualificationCertificate != null) {
                for (int i = 0; i < qualificationCertificate.length; i++) {
                    //设置路径，服务器项目根目录下的uploadfile
//                    String path = servletContext.getContextPath();//"uploadfile/QC/"
                    String path = "D:/yidao/gateway/gateway/src/main/resources/static/upload/QC";
                    //发送文件到ftp服务器，返回文件在服务器上的名字
                    String targetFileName = fileService.uploadFile(qualificationCertificate[i], path);
//                    String url = Const.FTP_SERVER_HTTP_PREFIX + targetFileName;
                    String url = "upload/QC/"+targetFileName;
                    File newFile = new File();
                    newFile.setType(1);
                    newFile.setFtpAddress(url);
                    newFile.setCompanyName(name);
                    File existFile = fileMapper.selectExists(name, 1);
                    if (existFile == null) {
                        fileMapper.insert(newFile);
                    } else {
                        existFile.setFtpAddress(url);
                        fileMapper.updateByPrimaryKey(existFile);
                    }
                }
            }
            //营业执照
            if(businessLicense != null){
                //设置路径，服务器项目根目录下的uploadfile
//                String path = servletContext.getContextPath();//"uploadfile/BL/"
                String path = "D:/yidao/gateway/gateway/src/main/resources/static/upload/BL";
                //发送文件到ftp服务器，返回文件在服务器上的名字
                String targetFileName = fileService.uploadFile(businessLicense, path);
//                String url = Const.FTP_SERVER_HTTP_PREFIX + targetFileName;
                String url = "upload/BL/"+targetFileName;
                File newFile = new File();
                newFile.setType(2);
                newFile.setFtpAddress(url);
                newFile.setCompanyName(name);
                File existFile  = fileMapper.selectExists(name,2);
                if (existFile == null){
                    fileMapper.insert(newFile);
                }else {
                    existFile.setFtpAddress(url);
                    fileMapper.updateByPrimaryKey(existFile);
                }
            }
            //开户许可证
            if(openingPermit != null){
                //设置路径，服务器项目根目录下的uploadfile
//                String path = servletContext.getContextPath();//"uploadfile/OP/"
                String path = "D:/yidao/gateway/gateway/src/main/resources/static/upload/OP";
                //发送文件到ftp服务器，返回文件在服务器上的名字
                String targetFileName = fileService.uploadFile(openingPermit, path);
//                String url = Const.FTP_SERVER_HTTP_PREFIX + targetFileName;
                String url = "upload/OP/"+targetFileName;
                File newFile = new File();
                newFile.setType(3);
                newFile.setFtpAddress(url);
                newFile.setCompanyName(name);
                File existFile  = fileMapper.selectExists(name,3);
                if (existFile == null){
                    fileMapper.insert(newFile);
                }else {
                    existFile.setFtpAddress(url);
                    fileMapper.updateByPrimaryKey(existFile);
                }
            }
            //公章水印
            if(officialSeal != null){
                //设置路径，服务器项目根目录下的uploadfile
//                String path = servletContext.getContextPath();//uploadfile/OS/
                String path = "D:/yidao/gateway/gateway/src/main/resources/static/upload/OS";
                //发送文件到ftp服务器，返回文件在服务器上的名字
                String targetFileName = fileService.uploadFile(officialSeal, path);
//                String url = Const.FTP_SERVER_HTTP_PREFIX + targetFileName;
                String url = "upload/OS/"+targetFileName;
                File newFile = new File();
                newFile.setType(4);
                newFile.setFtpAddress(url);
                newFile.setCompanyName(name);
                File existFile  = fileMapper.selectExists(name,4);
                if (existFile == null){
                    fileMapper.insert(newFile);
                }else {
                    existFile.setFtpAddress(url);
                    fileMapper.updateByPrimaryKey(existFile);
                }
            }
            //企业内控制度(服务承诺书)
            if(enterpriseSystem != null){
                //设置路径，服务器项目根目录下的uploadfile
//                String path = servletContext.getContextPath();//"uploadfile/ES/"
                String path = "D:/yidao/gateway/gateway/src/main/resources/static/upload/ES";
                //发送文件到ftp服务器，返回文件在服务器上的名字
                String targetFileName = fileService.uploadFile(enterpriseSystem, path);
//                String url = Const.FTP_SERVER_HTTP_PREFIX + targetFileName;
                String url = "upload/ES/"+targetFileName;
                File newFile = new File();
                newFile.setType(5);
                newFile.setFtpAddress(url);
                newFile.setCompanyName(name);
                File existFile  = fileMapper.selectExists(name,5);
                if (existFile == null){
                    fileMapper.insert(newFile);
                }else {
                    existFile.setFtpAddress(url);
                    fileMapper.updateByPrimaryKey(existFile);
                }
            }
            //药品批文
            if(medicineOfficial != null){
                //设置路径，服务器项目根目录下的uploadfile
//                String path = servletContext.getContextPath();//"uploadfile/MO/"
                String path = "D:/yidao/gateway/gateway/src/main/resources/static/upload/MO";
                //发送文件到ftp服务器，返回文件在服务器上的名字
                String targetFileName = fileService.uploadFile(medicineOfficial, path);
//                String url = Const.FTP_SERVER_HTTP_PREFIX + targetFileName;
                String url = "upload/MO/"+targetFileName;
                File newFile = new File();
                newFile.setType(6);
                newFile.setFtpAddress(url);
                newFile.setCompanyName(name);
                File existFile  = fileMapper.selectExists(name,6);
                if (existFile == null){
                    fileMapper.insert(newFile);
                }else {
                    existFile.setFtpAddress(url);
                    fileMapper.updateByPrimaryKey(existFile);
                }
            }
            //GMP证书
            if(GPMcertificate != null){
                //设置路径，服务器项目根目录下的uploadfile
//                String path = servletContext.getContextPath();//"uploadfile/GMP/"
                String path = "D:/yidao/gateway/gateway/src/main/resources/static/upload/GMP";
                //发送文件到ftp服务器，返回文件在服务器上的名字
                String targetFileName = fileService.uploadFile(GPMcertificate, path);
//                String url = Const.FTP_SERVER_HTTP_PREFIX + targetFileName;
                String url = "upload/GMP/"+targetFileName;
                File newFile = new File();
                newFile.setType(7);
                newFile.setFtpAddress(url);
                newFile.setCompanyName(name);
                File existFile  = fileMapper.selectExists(name,7);
                if (existFile == null){
                    fileMapper.insert(newFile);
                }else {
                    existFile.setFtpAddress(url);
                    fileMapper.updateByPrimaryKey(existFile);
                }
            }
            //身份证
            if(contactIdCard != null){
                //设置路径，服务器项目根目录下的uploadfile
//                String path = servletContext.getContextPath();//"uploadfile/CIC/"
                String path = "D:/yidao/gateway/gateway/src/main/resources/static/upload/CIC";
                //发送文件到ftp服务器，返回文件在服务器上的名字
                String targetFileName = fileService.uploadFile(contactIdCard, path);
//                String url = Const.FTP_SERVER_HTTP_PREFIX + targetFileName;
                String url = "upload/CIC/"+targetFileName;
                File newFile = new File();
                newFile.setType(8);
                newFile.setFtpAddress(url);
                newFile.setCompanyName(name);
                File existFile  = fileMapper.selectExists(name,8);
                if (existFile == null){
                    fileMapper.insert(newFile);
                }else {
                    existFile.setFtpAddress(url);
                    fileMapper.updateByPrimaryKey(existFile);
                }
            }
            Validstate validstate = new Validstate();
            //设置公司名称
            validstate.setCompanyName(name);
            //设置提交状态-提交
            validstate.setSubState(0);
            //设置审核状态-审核
            validstate.setAuditState(0);
            //设置通过状态-通过
            validstate.setPassState(0);
            validstateMapper.insertSelective(validstate);
            return ServerResponse.createBySuccessMessage("提交成功");
        }catch(Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ServerResponse.createByErrorMessage("提交发生异常，请重试");
        }
    }


    /**
     * admin审核公司通过
     * @param companyName 被审核的公司名
     * @param adminId  审核操作的admin成员id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse<String> validPassCompany(String companyName,Integer adminId){
        try {
            //查询该公司
            Company company = companyMapper.selectByCompanyName(companyName);
            //设置审核状态为通过
            company.setValidState(1);
//            //生成公司编号
//            String str = company.getType()+String.format("%8d",company.getId()).replace(" ","0");//9位
//            company.setCode(str);
            companyMapper.updateByPrimaryKeySelective(company);
//            //管理员生成公司编号
//            Staff staff = staffMapper.selectByUsername(username);
//            staff.setCompanyCode(str);
//            staffMapper.updateByPrimaryKeySelective(staff);
            //更改状态表
            Validstate validstate = new Validstate();
            //设置公司名称
            validstate.setCompanyName(companyName);
            //设置提交状态-提交
            validstate.setSubState(0);
            //设置审核状态-审核
            validstate.setAuditState(1);
            //设置通过状态-通过
            validstate.setPassState(1);
            //设置审核操作的adminId
            validstate.setAdminUserId(adminId);
            validstateMapper.updateByCompanyName(validstate);
            return ServerResponse.createBySuccessMessage("审核完成");
        }catch(Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ServerResponse.createByErrorMessage("审核发生异常，请重试");
        }
    }

    /**
     * admin审核公司未通过
     * @param companyName 被审核的公司名称
     * @param adminId  审核操作的admin成员id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse<String> validNotPassCompany(String companyName,Integer adminId){
            try {
                companyMapper.deleteByCompanyName(companyName);
                Validstate validstate = new Validstate();
                //设置公司名称
                validstate.setCompanyName(companyName);
                //设置提交状态-提交
                validstate.setSubState(0);
                //设置审核状态-审核
                validstate.setAuditState(1);
                //设置通过状态-未通过
                validstate.setPassState(0);
                //设置审核操作的adminId
                validstate.setAdminUserId(adminId);
                validstateMapper.updateByCompanyName(validstate);
                return ServerResponse.createBySuccessMessage("审核完成");
            }catch(Exception e){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ServerResponse.createByErrorMessage("审核发生异常，请重试");
            }
    }

    /**
     * 根据当前时间（小时）统计登录次数
     * @param nowTime  当前小时
     * @return
     */
    public ServerResponse getLoginTotle(Integer nowTime){
        String nowTimeStr = nowTime.toString();
        //本小时的登录统计
        Integer count = Integer.parseInt(jedisUtil.get(nowTimeStr));
        //根据现在时间获取前6小时登录统计（此处为了使0到24的时间循环，用了取模运算）
        Integer count1 = Integer.parseInt(jedisUtil.get((nowTime+24-1)%24+""));
        Integer count2 = Integer.parseInt(jedisUtil.get((nowTime+24-2)%24+""));
        Integer count3 = Integer.parseInt(jedisUtil.get((nowTime+24-3)%24+""));
        Integer count4 = Integer.parseInt(jedisUtil.get((nowTime+24-4)%24+""));
        Integer count5 = Integer.parseInt(jedisUtil.get((nowTime+24-5)%24+""));
        Integer count6 = Integer.parseInt(jedisUtil.get((nowTime+24-6)%24+""));
        LoginTotleVo loginTotleVo = new LoginTotleVo();
        loginTotleVo.setCount(count);
        loginTotleVo.setCount1(count1);
        loginTotleVo.setCount2(count2);
        loginTotleVo.setCount3(count3);
        loginTotleVo.setCount4(count4);
        loginTotleVo.setCount5(count5);
        loginTotleVo.setCount6(count6);
        return ServerResponse.createBySuccess(loginTotleVo);
    }

}