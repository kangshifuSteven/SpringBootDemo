package com.example.demo;

import com.alibaba.fastjson.JSON;
import com.example.demo.clouddo.dao.OrderDao;
import com.example.demo.clouddo.dao.UserDao;
import com.example.demo.clouddo.domain.MailBean;
import com.example.demo.clouddo.domain.Order;
import com.example.demo.clouddo.domain.UserDO;
import com.example.demo.clouddo.service.UserService;
import com.example.demo.utils.MailUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ReturnListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootDemoApplicationTests {

	private static final Logger logger = LogManager.getLogger(SpringBootDemoApplicationTests.class);

	@Autowired
	private UserService userService;
	@Autowired
	private OrderDao orderDao;



	@Test
	public void testAdd() {
		UserDO user = userService.getUserDo(1l);
		System.out.println(user.getUserId()+":"+user.getUsername());
	}


	@Test
	public void orderTest() {
	    Assert.assertNotNull("查询的数据:"+orderDao.findOrderAndOrderDetails());
		List<Order> orders = orderDao.findOrderAndOrderDetails();
		System.out.println(JSON.toJSONString(orders));
		for (int i = 0; i < orders.size(); i++) {
			System.out.println(orders.get(i).getUser().getUsername());
		}
	}



	@Autowired
	private RedisTemplate redisTemplate;

	@Test
	public void redisTest() {
		// redis存储数据
		String key = "name";
		redisTemplate.opsForValue().set(key, "yukong");
		// 获取数据
		String value = (String) redisTemplate.opsForValue().get(key);
		System.out.println("获取缓存中key为" + key + "的值为：" + value);

		UserDO user = new UserDO();
		user.setUsername("yukong");
		user.setUserId(1L);
		String userKey = "yukong";
		redisTemplate.opsForValue().set(userKey, user);
		UserDO newUser = (UserDO) redisTemplate.opsForValue().get(userKey);
		System.out.println("获取缓存中key为" + userKey + "的值为：" + newUser);

	}

	@Test
	public void testCache(){
		// 根据Id查询
		logger.info("user1 = " + userService.getUserDo(1l));
		sleep(2);
		// 等1s再次查询
		logger.info("user2 = " + userService.getUserDo(1l));
		sleep(5);
		// 等5s再次查询
		logger.info("user3 = " + userService.getUserDo(1l));
	}

	@Autowired
	RabbitTemplate rabbitTemplate;

	@Autowired
	ConnectionFactory connectionFactory;

	@Test
	public void testStrMQ() {
//		Channel channel = connectionFactory.createConnection().createChannel(true);
//		channel.addReturnListener(new ReturnListener() {
//			@Override
//			public void handleReturn(int replyCode,
//									 String replyText,
//									 String exchange,
//									 String routingKey,
//									 AMQP.BasicProperties properties,
//									 byte[] body)
//					throws IOException {
//				System.out.println("=========handleReturn===method============");
//				System.out.println("replyText:"+replyText);
//				System.out.println("exchange:"+exchange);
//				System.out.println("routingKey:"+routingKey);
//				System.out.println("message:"+new String(body));
//			}
//		});
//		rabbitTemplate.addListener(channel);
		// 对象被默认JAVA序列化发送，参数：Exchange，routingKey，消息
		rabbitTemplate.convertAndSend("test.exchange", "dada", "瓦尔克莉111111");
		//rabbitTemplate.convertAndSend("exchange.direct", "queueTwo", "瓦尔克莉");
	}

	@Test
	public void receive() {
		// 接受数据
		String key = rabbitTemplate.receive("test.queue").getMessageProperties().getReceivedRoutingKey();
		System.out.println(key);
		System.out.println(new String(key.getBytes()));
	}

	@Autowired
	private MailUtil mailUtil;

	@Autowired
	private JavaMailSender mailSender;

	@Test
	public void sendSimpleMail() throws Exception {
		MailBean mailBean = new MailBean();
		mailBean.setRecipient("908667158@qq.com");
		mailBean.setSubject("测试");
		mailBean.setContent("测试saf发送发送");
		mailUtil.sendSimpleMail(mailBean);
		logger.info("发送成功");
	}

	@Test
	public void sendTemplateMail() throws Exception {
		MailBean mailBean = new MailBean();
		mailBean.setRecipient("908667158@qq.com");
		mailBean.setSubject("测试SpringBoot发送带FreeMarker邮件");
		mailBean.setContent("希望大家能够学到自己想要的东西，谢谢各位的帮助！！！");
		mailUtil.sendTemplateMail(mailBean);
	}


	private void sleep(int count){
		try {
			Thread.sleep(count*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
