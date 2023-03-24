package org.jeecg.modules.system.service;

import org.jeecg.modules.system.entity.MailTemplate;
import org.springframework.stereotype.Service;

public interface ISendMailService {

    void SendMail(MailTemplate mailTemplate);
}
