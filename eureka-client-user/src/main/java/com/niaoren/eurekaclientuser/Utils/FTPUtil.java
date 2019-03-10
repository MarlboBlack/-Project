package com.niaoren.eurekaclientuser.Utils;


import com.niaoren.eurekaclientuser.common.Const;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;


public class FTPUtil {

    private  final Logger logger = LoggerFactory.getLogger(FTPUtil.class);


    private  static String ftpIp  = Const.FTP_SERVER_IP;

    private  static String ftpUser = Const.FTP_USER;

    private  static String ftpPwd = Const.FTP_PWD;

    private String ip;
    private int port;
    private String user;
    private String pwd;
    private FTPClient ftpClient;

    public FTPUtil(){}

    public FTPUtil(String ip,int port,String user,String pwd){
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }



    /**
     * 获取服务器连接
     * @param ip 服务器ip
     * @param port 服务器端口
     * @param user 服务器登录用户名
     * @param pwd 服务器登录密码
     * @return
     */
    private boolean connectServer(String ip,int port,String user,String pwd){
        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip);
            isSuccess = ftpClient.login(user,pwd);
        } catch (IOException e) {
            logger.error("连接服务器异常",e);
        }
        return isSuccess;
    }

    /**
     * 上传方法
     * @param remotePath 上传到的远程路径
     * @param fileList 要上传的文件
     * @return
     * @throws IOException 关流异常
     */
    private boolean uploadFileMethod(String remotePath,List<File> fileList) throws IOException {
        //设置一个标志，用于返回上传成功和失败
        boolean uploaded = true;
        FileInputStream fis = null;
        if (connectServer(this.ip,this.port,this.user,this.pwd)){
            try {
                //切换文件夹
                ftpClient.changeWorkingDirectory(remotePath);
                //设置缓冲区大小
                ftpClient.setBufferSize(1024);
                //设置编码
                ftpClient.setControlEncoding("utf-8");
                //设置文件类型为二进制，防止乱码
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                //设置每次数据连接之前，ftp client告诉ftp server开通一个端口来传输数据。
                ftpClient.enterLocalPassiveMode();

                //开始传输
                for (File fileItem : fileList){
                    fis = new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(),fis);
                }

            } catch (IOException e) {
                logger.error("文件传输异常",e);
                uploaded = false;
            }finally {
                //释放资源
                fis.close();
                ftpClient.disconnect();
            }
        }
        return uploaded;
    }


    /**
     * 上传文件
     * @param fileList 要上传的文件
     * @return
     * @throws IOException 调用uploadFileMethod方法的关流异常
     */
    public static boolean uploadFile(List<File> fileList) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(ftpIp,21,ftpUser,ftpPwd);
        //上传文件
        boolean result =  ftpUtil.uploadFileMethod("yidaoFile",fileList);
        return  result;
    }
}

