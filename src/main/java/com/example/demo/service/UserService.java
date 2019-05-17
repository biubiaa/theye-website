package com.example.demo.service;

public interface UserService {
    //检查密码
    public int checkPass(String id, String pass);
//    //修改密码
//    public int changePass(String id,String pass);
    //注册
    public int register(String id, String pass);

    //更新账号信息
//    public int updateMes(String userId,UserMes umNew);


}
