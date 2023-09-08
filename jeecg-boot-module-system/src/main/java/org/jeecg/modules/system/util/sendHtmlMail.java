package org.jeecg.modules.system.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


    /*
    @Param to 收件人
    @Param subject 主题
    @Param content
     */

//发送HTML邮件
@Component
public class sendHtmlMail{

    @Autowired
    private JavaMailSender javaMailSender;

    //根据配置文件中自己的QQ邮箱
    @Value("${spring.mail.username}")
    private String from;

//    public static final String htmlTemplate="<button>点我</button>";

    public void sendHtmlMail(String to,String subject,String content){
        //获取MimeMessage
        //面向对象的多态，javaMailSender.createMimeMessage()，多用途的网际邮件扩充协议
        MimeMessage message=javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            mimeMessageHelper=new MimeMessageHelper(message,true);
            //邮件发送人
            mimeMessageHelper.setFrom(from);
            //邮件接收人
            mimeMessageHelper.setTo(to);
            //邮件主题
            mimeMessageHelper.setSubject(subject);
            //邮件内容,HTML格式
            mimeMessageHelper.setText(content,true);
            javaMailSender.send(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendHtmlMails(String from, String to, String subject, String content){
        try{
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            javaMailSender.send(message);
        }catch (MessagingException e){
            System.out.println("发送失败！");
        }
    }

}

