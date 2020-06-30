package com.pa.asvblrapi.spring;

import com.pa.asvblrapi.entity.Subscription;
import com.pa.asvblrapi.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EmailServiceImpl {

    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    private SpringTemplateEngine thymeleafTemplateEngine;

    @Value("classpath:/mail-logo.png")
    Resource resourceFile;

    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);
        }
        catch (MailException exception) {
            exception.printStackTrace();
        }
    }

    public void sendMessageCreateSubscription(Subscription subscription) throws MessagingException {
        Context thymeleafContext = new Context();

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("name", String.format("%s %s", subscription.getFirstName(), subscription.getLastName()));

        thymeleafContext.setVariables(templateModel);
        String htmlBody = thymeleafTemplateEngine.process("template-new-subscription.html", thymeleafContext);

        sendHtmlMessage(subscription.getEmail(), "Inscription prise en compte", htmlBody);
    }

    public void sendMessageCreateUser(User user, String password) throws MessagingException {
        Context thymeleafContext = new Context();

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("name", String.format("%s %s", user.getFirstName(), user.getLastName()));
        templateModel.put("username", user.getUsername());
        templateModel.put("password", password);

        thymeleafContext.setVariables(templateModel);
        String htmlBody = thymeleafTemplateEngine.process("template-new-user.html", thymeleafContext);

        sendHtmlMessage(user.getEmail(), "Inscription validée", htmlBody);
    }

    public void sendMessageResetPassword(String token, User user) throws MessagingException {
        Context thymeleafContext = new Context();

        //String url = contextPath + "/users/change-password?token=" + token;
        String url = "http://localhost:4200/login/reset-password?token=" + token;
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("name", String.format("%s %s", user.getFirstName(), user.getLastName()));
        templateModel.put("username", user.getUsername());
        templateModel.put("url", url);

        thymeleafContext.setVariables(templateModel);
        String htmlBody = thymeleafTemplateEngine.process("template-reset-password.html", thymeleafContext);

        sendHtmlMessage(user.getEmail(), "Réinitialisation mot de passe", htmlBody);
    }

    public void sendMessage(List<User> users, String subject, String content) throws MessagingException {
        Context thymeleafContext = new Context();
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("content", content);
        templateModel.put("subject", subject);

        thymeleafContext.setVariables(templateModel);
        String htmlBody = this.thymeleafTemplateEngine.process("template-mail.html", thymeleafContext);

        String[] emails = new String[users.size()];
        for (int i = 0; i < users.size(); i++) {
            emails[i] = users.get(i).getEmail();
        }
        this.sendHtmlMessage(emails, subject, htmlBody);
    }

    public void sendMessageUsingThymeleafTemplate(String to, String subject, Map<String, Object> templateModel)
        throws MessagingException {

        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);
        String htmlBody = thymeleafTemplateEngine.process("template-new-subscription.html", thymeleafContext);

        sendHtmlMessage(to, subject, htmlBody);
    }

    private void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        //helper.addInline("attachment.png", resourceFile);
        emailSender.send(message);
    }

    private void sendHtmlMessage(String[] to, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setBcc(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        //helper.addInline("attachment.png", resourceFile);
        emailSender.send(message);
    }
}
