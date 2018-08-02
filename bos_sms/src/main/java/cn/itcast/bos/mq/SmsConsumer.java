package cn.itcast.bos.mq;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;

import org.springframework.stereotype.Service;

import cn.itcast.bos.utils.SmsUtils;

/**
 * 使用mq监听发送短信
 */
@Service("smsConsumer")
public class SmsConsumer implements MessageListener {

	@Override
	public void onMessage(Message message) {
		try {
			MapMessage mapMessage = (MapMessage) message;
			String telephone = mapMessage.getString("telephone");
			String randomCode = mapMessage.getString("randomCode");
			String msg = mapMessage.getString("msg");
			// 调用SMS服务发送短信
			System.out.println(msg);
			
			// SendSmsResponse sendSms = SmsUtils.sendSms(telephone, randomCode);
		} catch (Exception e) {
			System.out.println("短信发送失败");
			e.printStackTrace();
		}
	}

}
