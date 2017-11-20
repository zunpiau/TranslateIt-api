package zjp.translateit.service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import zjp.translateit.web.exception.EmailSendException;
import zjp.translateit.web.exception.EmailTemplateException;
import zjp.translateit.web.request.FeedbackRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.MessageFormat;

@Service
@PropertySource(value = "classpath:application.properties")
@PropertySource(value = "classpath:application-${spring.profiles.active}.properties")
public class EmailService {

    @Value("${ali.accessKeyID}")
    private String aliKeyID;
    @Value("${ali.accessSecret}")
    private String aliSecret;
    @Value("${ali.emailAccount}")
    private String aliAccount;
    @Value("${email.reply}")
    private String emailReply;
    @Value("classpath:email-template.html")
    private Resource emailTemplate;

    public void sendVerifyEmail(String mailTo, String verifyCode) {
        try {
            String template = new String(Files.readAllBytes(emailTemplate.getFile().toPath()),
                    StandardCharsets.UTF_8);
            String content = MessageFormat.format(template, verifyCode, emailReply, emailReply);
            sendEmail(mailTo, "TranslateIt", content);
        } catch (IOException e) {
            e.printStackTrace();
            throw new EmailTemplateException();
        }
    }

    private void sendEmail(String mailTo, String subject, String content) {
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", aliKeyID, aliSecret);
        IAcsClient client = new DefaultAcsClient(profile);
        SingleSendMailRequest request = new SingleSendMailRequest();
        request.setAccountName(aliAccount);
        request.setFromAlias("TranslateIt");
        request.setAddressType(1);
        request.setReplyToAddress(true);
        request.setToAddress(mailTo);
        request.setSubject(subject);
        request.setHtmlBody(content);
        try {
            HttpResponse response = client.doAction(request, true, 2, profile);
            if (!response.isSuccess()) {
                throw new EmailSendException();
            }
        } catch (ClientException e) {
            e.printStackTrace();
            throw new EmailSendException();
        }
    }

    @Async
    public void sendFeedbackEmail(FeedbackRequest request, String ua) {
        String feedbackTemplate = "<p>From: {0}</p><p>UA: {1}</p><p>{2}</p>";
        String emailContent = MessageFormat.format(feedbackTemplate,
                request.getContent(),
                ua,
                request.getContact());
        sendEmail(emailReply, "Feedback", emailContent);
    }

}
