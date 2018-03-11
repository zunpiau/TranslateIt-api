package zjp.translateit.service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import zjp.translateit.web.exception.EmailSendException;
import zjp.translateit.web.request.FeedbackRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.MessageFormat;

@Service
@PropertySource(value = "classpath:application.properties", encoding = "UTF-8")
public class EmailService {

    private final IAcsClient client;
    private final String verifyTemplate;
    private String feedbackTemplate;
    @Value("${ali.emailAccount}")
    private String aliAccount;
    @Value("${email.reply}")
    private String emailReply;
    @Value("${email.subject.verify}")
    private String verifyEmailSubject;

    public EmailService(@Value("${ali.accessKeyID}") String keyID,
            @Value("${ali.accessSecret}") String secret) throws IOException {
        client = new DefaultAcsClient(DefaultProfile.getProfile("cn-hangzhou", keyID, secret));
        verifyTemplate = new String(Files.readAllBytes(new ClassPathResource("email-template.html").getFile().toPath()),
                StandardCharsets.UTF_8);
        feedbackTemplate = new String(Files.readAllBytes(new ClassPathResource("email-feedback.html").getFile().toPath()),
                StandardCharsets.UTF_8);
    }

    public void sendVerifyEmail(String mailTo, String verifyCode, String inviteCode) {
        sendEmail(mailTo, verifyEmailSubject, MessageFormat.format(verifyTemplate, verifyCode, inviteCode));
    }

    private void sendEmail(String mailTo, String subject, String content) {
        SingleSendMailRequest request = new SingleSendMailRequest();
        request.setAccountName(aliAccount);
        request.setFromAlias("TranslateIt");
        request.setAddressType(1);
        request.setReplyToAddress(true);
        request.setToAddress(mailTo);
        request.setSubject(subject);
        request.setHtmlBody(content);
        try {
            HttpResponse response = client.doAction(request, true, 2);
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
        sendEmail(emailReply, "Feedback", MessageFormat.format(feedbackTemplate,
                request.getVersion(),
                request.getContact(),
                request.getContent(),
                ua));
    }

}
