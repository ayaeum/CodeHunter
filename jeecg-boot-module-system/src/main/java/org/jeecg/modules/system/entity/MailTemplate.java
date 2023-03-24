package org.jeecg.modules.system.entity;

import lombok.Data;

/**
 * 功能描述
 *
 * @author: scott
 * @date: 2022年10月18日 11:17
 */
@Data
public class MailTemplate {
    private String Form;
    private String To;
    private String Subject;
    private String Context;

    public MailTemplate(String form, String to, String subject, String context) {
        Form = form;
        To = to;
        Subject = subject;
        Context = context;
    }
}
