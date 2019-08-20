package com.zhang.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @author 张会丽
 * @create 2019/8/9
 */
public class EmailUtil implements Runnable{
    private String email;
    private String code;
    public EmailUtil(String email, String code) {
        this.email = email;
        this.code = code;
    }
    //email 你要发给谁      //authcode  验证码
    public void run() {
        // 1.创建连接对象javax.mail.Session
        // 2.创建邮件对象 javax.mail.Message
        // 3.发送一封激活邮件
        String from = "1067556810@qq.com";// 发件人电子邮箱
        String host = "smtp.qq.com"; // 指定发送邮件的主机smtp.qq.com(QQ)|smtp.163.com(网易)
        Properties properties = System.getProperties();// 获取系统属性
        properties.setProperty("mail.smtp.host", host);// 设置邮件服务器
        properties.setProperty("mail.smtp.auth", "true");// 打开认证
        // 1.获取默认session对象
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("1067556810@qq.com", "mgqgaixbxpukbdhd"); // 发件人邮箱账号、授权码
            }
        });
        // 2.创建邮件对象
        Message messgae = new MimeMessage(session);
        try {
            // 2.1设置发件人
            messgae.setFrom(new InternetAddress(from));//设置发送人
            // 2.2设置接收人
            messgae.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            // 2.3设置邮件主题
            messgae.setSubject("咎由自取平台安全中心【密码找回】");
            // 2.4设置邮件内容
            String content = "<html><head></head><body><h1>【咎由自取平台安全中心】这是一封密码找回邮件,更改密码请点击以下链接,本链接30分钟内有效(请勿回复)</h1>" +
                    "<h3><a href='https://127.0.0.1:8080/reset_pwd?code="
                    + code + "'>https://127.0.0.1:8080/reset_pwd?code=" + code
                    + "</href></h3></body></html>";
            messgae.setContent(content, "text/html;charset=UTF-8");
            // 3.发送邮件
            Transport.send(messgae);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
