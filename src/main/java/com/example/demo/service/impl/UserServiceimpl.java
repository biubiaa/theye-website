package com.example.demo.service.impl;

import com.example.demo.dao.UserMes;
import com.example.demo.mapper.UserMesMapper;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.example.demo.dao.UserPass;
//import com.example.demo.mapper.UserPassMapper;

@Service
public class UserServiceimpl implements UserService {

    @Autowired
    UserMesMapper userMesMapper;
    //检查密码
    @Override
    public int checkPass(String id, String pass) {
        System.out.println("dasasdad");
        try {
            UserMes mes = userMesMapper.selectByPrimaryKey(id);
            if(pass.equals(mes.getPassword())){
                return 1;
            }else {
                return 0;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return 0;
        }
    }
    //获取昵称
    public String gerNickName(String userId){
        UserMes um = userMesMapper.selectByPrimaryKey(userId);
        String nickName = um.getNickname();
        return nickName;
    }
    //添加用户（账号，密码）
    public int insertUser(String userId,String password){
        UserMes um = new UserMes();
        um.setUserId(userId);
        um.setPassword(password);
        um.setSignature("");
        um.setRegion("");
        um.setPhone("");
        um.setEmail("");
        um.setNickname("");
        um.setMoney(0.0);
        um.setAsk(0);
        um.setAnswer(0);
        try{
            userMesMapper.insert(um);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return 0;
        }
        return 1;
    }
//    @Override
//    public int changePass(String id, String pass) {
//        UserPass userPass = new UserPass();
//        userPass.setId(id);
//        userPass.setPass(pass);
//        userPassMapper.updateByPrimaryKey(userPass);
//        return 1;
//    }
    public UserMes selectById(String id){
        UserMes um = userMesMapper.selectByPrimaryKey(id);
        return um;
    }
    @Override
    public int register(String id, String pass) {
       UserMes mes = new UserMes();
       mes.setUserId(id);
       mes.setPassword(pass);
       return userMesMapper.insert(mes);
    }
    /**
     * 充值
     * */
    public int recharger(String  userId,int money){
        return userMesMapper.recharge(userId,money);
    }


    public int updateMes(UserMes umNew) {
        UserMes umPass = userMesMapper.selectByPrimaryKey(umNew.getUserId());
        //更新邮箱
        if(!umNew.getEmail().equals(umPass.getEmail())){
            umPass.setEmail(umNew.getEmail());
        }
        //更新电话
        if(!umNew.getPhone().equals(umPass.getPhone())){
            umPass.setPhone(umNew.getPhone());
        }
        //更新常驻地区
        if(!umNew.getRegion().equals(umPass.getRegion())){
            umPass.setRegion(umNew.getRegion());
        }
        //更新签名
        if(!umNew.getSignature().equals(umPass.getSignature()))
        {
            umPass.setSignature(umNew.getSignature());
        }
        //更新昵称
        if(!umNew.getNickname().equals(umPass.getNickname())){
            umPass.setNickname(umNew.getNickname());
        }

       return userMesMapper.updateByPrimaryKey(umPass);

    }
    /**
     * 判断钱够不够
     *
     */

    public int judgeMoney(String userId,double money){
        double oldMoney = userMesMapper.selectByPrimaryKey(userId).getMoney();
        if(money<=oldMoney)
            return 1;
        else
            return 0;
    }
}
