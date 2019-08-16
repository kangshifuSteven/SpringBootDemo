package com.example.demo.MQ.listener;

import com.example.demo.clouddo.domain.MailBean;
import com.example.demo.utils.MailUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class CommonMqListener2 {

    private static final Logger log = LoggerFactory.getLogger(CommonMqListener2.class);

    @Autowired
    private ObjectMapper objectMapper;

    //@Autowired
    //private MailService mailService;
    @Autowired
    private MailUtil mailUtil;
    /**
     * 监听消费用户日志
     *
     * @param message
     */
    @RabbitListener(queues = "test2.queue", containerFactory = "singleListenerContainer")
    public void consumeUserLogQueue(@Payload byte[] message) {
        try {
            //UserLog userLog = objectMapper.readValue(message, UserLog.class);
            String mess = new String(message);
            log.info("监听test2.queue消息： {} ", mess);
            MailBean mailBean = new MailBean();
            mailBean.setRecipient("908667158@qq.com");
            mailBean.setSubject("队列二发来的test2.queue测试");
            mailBean.setContent(mess);
            mailUtil.sendSimpleMail(mailBean);
            //TODO：记录日志入数据表
            //userLogMapper.insertSelective(userLog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}