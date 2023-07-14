package com.ynu.entity;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * @author wxg
 * @des 邮件服务service
 * @date 2020年4月2日
 */
@Service
public class MailService {

    @Autowired(required = false)
    JavaMailSender javaMailSender;
    @Value("${mail.from}")
    private String sendFrom;

    /**
     * 发送html格式邮件
     *
     * @param toMail
     * @param mailTitle
     * @param mailContent
     * @return
     */
    public boolean sendHtml(String toMail, String mailTitle, String mailContent) throws MessagingException {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "GBK"); //解决乱码问题

            helper.setFrom(sendFrom);
            helper.setTo(toMail);
            helper.setSubject(mailTitle);
            //设置META解决乱码问题
            helper.setText(mailContent, true);

            this.javaMailSender.send(message);
        } catch (Exception e) {
            return false;
        }
        return true;

    }


    /**
     * 发送文本邮件
     *
     * @param toMail
     * @param mailTitle
     * @param mailContent
     * @return
     */
    public boolean send(String toMail, String mailTitle, String mailContent) {
        try {
            //建立邮件消息
            SimpleMailMessage mainMessage = new SimpleMailMessage();
            //发送者
            mainMessage.setFrom(sendFrom);
            //接收者
            mainMessage.setTo(toMail);
            //发送的标题
            mainMessage.setSubject(mailTitle);
            //发送的内容
            mainMessage.setText(mailContent);
            javaMailSender.send(mainMessage);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}