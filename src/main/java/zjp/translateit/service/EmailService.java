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
import zjp.translateit.web.request.FeedbackRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.MessageFormat;

@Service
@PropertySource(value = "classpath:application.properties")
@PropertySource(value = "classpath:application-${spring.profiles.active}.properties")
public class EmailService {

    private static final String feedbackTemplate = "<p>From: {0}</p><p>UA: {1}</p><p>{2}</p>";
    @Value("${ali.accessKey}")
    private String aliKey;
    @Value("${ali.accessSecret}")
    private String aliSecret;
    @Value("${ali.emailAccount}")
    private String aliAccount;
    @Value("classpath:email-template.html")
    private Resource emailTemplate;

    boolean sendVerifyEmail(String mailTo, String verifyCode) throws IOException, ClientException {
        String template = new String(Files.readAllBytes(emailTemplate.getFile().toPath()), StandardCharsets.UTF_8);
        String content = MessageFormat.format(template, verifyCode);
        return sendEmail(mailTo, "TranslateIt", content);
    }

    private boolean sendEmail(String mailTo, String subject, String content) throws ClientException {
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", aliKey, aliSecret);
        IAcsClient client = new DefaultAcsClient(profile);
        SingleSendMailRequest request = new SingleSendMailRequest();
        request.setAccountName(aliAccount);
        request.setFromAlias("TranslateIt");
        request.setAddressType(1);
        request.setReplyToAddress(true);
        request.setToAddress(mailTo);
        request.setSubject(subject);
        request.setHtmlBody(content);
        HttpResponse response;
        response = client.doAction(request, true, 2, profile);
        return response.isSuccess();
    }

    @Async
    public void sendFeedbackEmail(FeedbackRequest request, String ua) throws ClientException {
        String emailContent = MessageFormat.format(feedbackTemplate, request.getContent(), ua, request.getContact());
        sendEmail("tra@shadowland.cn", "Feedback", emailContent);
    }

}
