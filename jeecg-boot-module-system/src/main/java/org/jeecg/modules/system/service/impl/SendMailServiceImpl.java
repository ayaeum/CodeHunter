package org.jeecg.modules.system.service.impl;

import org.jeecg.modules.system.entity.MailTemplate;
import org.jeecg.modules.system.service.ISendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * 功能描述
 *
 * @author: caiguapi
 * @date: 2022年10月18日 10:28
 */
@Service
public class SendMailServiceImpl implements ISendMailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void SendMail(MailTemplate mailTemplate){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailTemplate.getForm());
        message.setTo(mailTemplate.getTo());
        message.setSubject(mailTemplate.getSubject());
        message.setText(mailTemplate.getContext());
        javaMailSender.send(message);
    }
}
