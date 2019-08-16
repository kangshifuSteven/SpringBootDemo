package com.example.demo.MQ.listener;

import com.example.demo.order.service.ConcurrencyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/8/30.
 */
@Component
public class UserOrderListener implements ChannelAwareMessageListener {

    private static final Logger log= LoggerFactory.getLogger(UserOrderListener.class);

    @Autowired
    private ObjectMapper objectMapper;

    //@Autowired
    //private UserOrderMapper userOrderMapper;

    @Autowired
    private ConcurrencyService concurrencyService;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long tag=message.getMessageProperties().getDeliveryTag();
        try {
            byte[] body=message.getBody();
            /*UserOrderDto entity=objectMapper.readValue(body, UserOrderDto.class);
            log.info("用户商城抢单监听到消息： {} ",entity);

            UserOrder userOrder=new UserOrder();
            BeanUtils.copyProperties(entity,userOrder);
            userOrder.setStatus(1);
            userOrderMapper.insertSelective(userOrder);*/

            String mobile=new String(body,"UTF-8");
            log.info("监听到抢单手机号： {} ",mobile);

            concurrencyService.manageRobbing(String.valueOf(mobile));
            channel.basicAck(tag,true);
        }/*catch (QueException ce) {
            //特定exception重发
            if (ce.getErrCode() == RecordErr.HTTP_SEND_ERR.getErrCode()) {
                log.info("抢单失败,3秒后重新处理 " + new String(message.getBody()));
                try {
                    Thread.sleep(3000);
                    //ack表示失败，要求重新发送
                    channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
                    return;
                }
                catch (Exception e) {
                    log.error("重发mq消息" + new String(message.getBody()) + " 失败", e);
                }
            }
        }*/ catch (Exception e){
            log.error("用户商城下单 发生异常：",e.fillInStackTrace());
            channel.basicReject(tag,false);
        }
//        try {
//            //最终应答rabbitmq返回，防止消息堵塞
//            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//        }catch (IOException e) {
//            log.error("deal Record message is " + new String(message.getBody()) + "error ", e);
//        }
    }
}




























