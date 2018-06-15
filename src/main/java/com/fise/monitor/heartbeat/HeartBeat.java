package com.fise.monitor.heartbeat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class HeartBeat {

	public static Map<String, String> urlMap = new HashMap<String, String>(); // urlMap
	public static Map<String, Integer> countMap = new HashMap<String, Integer>(); // 计数Map
	
	public static void main(String[] args) {
		System.out.println(loadJSON("http://xiaoyutest.fise-wi.com:8080/xiaoyu/"));
	}

	@Scheduled(cron = "0 0/5 * * * ?") // 每5分钟执行一次
	public void scheduler() {
		heartBeat();
	}

	// 心跳监测方法
	public static void heartBeat() {
		if (urlMap.size() == 0) {
			initUrlMap(urlMap);
		}
		if (countMap.size() == 0) {
			initCountMap(countMap);
		}
		// 遍历url
		for (Map.Entry<String, String> entry : urlMap.entrySet()) {
//			System.out.println(entry.getKey() + ":" + entry.getValue() + ":" + loadJSON(entry.getValue()));
			String result = null;
			// 尝试请求3次,如果result不是"helloworld"继续请求
			for (int i = 0; i < 3; i++) {
				result = loadJSON(entry.getValue());
				if ("helloworld".equals(result)) {
					break;
				}
				try {
					Thread.sleep(10000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// 如果result不是"helloworld",并且发送次数少于2次
			if (!"helloworld".equals(result) && countMap.get(entry.getKey()) < 2) {
				try {
					EmailUtil.sendEmail(entry.getKey() + "红色警报",
							"<h2><font color='red'>红色警报!  <a href=" + SystemEnums.getUrl(entry.getKey()) + ">" + entry.getKey() + "</a>异常!  请速速处理!</font></h2>");
					countMap.put(entry.getKey(), countMap.get(entry.getKey()) + 1);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				 try {
					Runtime.getRuntime().exec("/home/fise/bin/alarm_report.py " + SystemEnums.getAlarm(entry.getKey()));
				} catch (IOException e) {
					e.printStackTrace();
				}
				// Sms.sendSMS("13128726 378", entry.getKey() + "系统异常报告", false);
			}
			// 如果result是"helloworld",并且发送次数大于0次,则发送邮件提示和发送次数初始化为0
			if ("helloworld".equals(result) && countMap.get(entry.getKey()) > 0) {
				try {
					EmailUtil.sendEmail(entry.getKey() + "友情提示", "<h2><font color='green'><a href=" + SystemEnums.getUrl(entry.getKey()) + ">" + entry.getKey() + "</a>恢复正常</font></h2>");
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					Runtime.getRuntime().exec("/home/fise/bin/alarm_report.py " + SystemEnums.getSuccess(entry.getKey()));
				} catch (IOException e) {
					e.printStackTrace();
				}
				countMap.put(entry.getKey(), 0);
			}

		}
	}

	// 初始化urlMap
	public static Map<String, String> initUrlMap(Map<String, String> urlMap) {

		urlMap.put("小位开发系统", "http://10.252.252.250:8787/managesvr/heartbeat/helloworld");
		urlMap.put("小位测试系统", "http://file.fise-wi.com:8787/managesvr/heartbeat/helloworld");
		urlMap.put("小位生产系统", "http://file.fise-wi.com:8589/managesvr/heartbeat/helloworld");
		urlMap.put("猫我开发系统", "http://10.252.252.250:8787/xiaoyusvr/heartbeat/helloworld");
		urlMap.put("猫我测试系统", "http://xiaoyutest.fise-wi.com:8787/xiaoyusvr/heartbeat/helloworld");
		urlMap.put("猫我生产系统", "http://xiaoyu.fise-wi.com:8787/xiaoyusvr/heartbeat/helloworld");
		urlMap.put("物联开发系统", "http://10.252.252.250:8787/iot/heartbeat/helloworld");
		urlMap.put("物联测试系统", "http://39.108.17.123:8787/iot/heartbeat/helloworld");
		return urlMap;

	}

	// 初始化计数Map
	public static Map<String, Integer> initCountMap(Map<String, Integer> urlMap) {

		urlMap.put("小位开发系统", 0);
		urlMap.put("小位测试系统", 0);
		urlMap.put("小位生产系统", 0);
		urlMap.put("猫我开发系统", 0);
		urlMap.put("猫我测试系统", 0);
		urlMap.put("猫我生产系统", 0);
		urlMap.put("物联开发系统", 0);
		urlMap.put("物联测试系统", 0);
		return urlMap;

	}

	// http请求
	public static String loadJSON(String url) {
		StringBuilder json = new StringBuilder();
		try {
			URL oracle = new URL(url);
			URLConnection yc = oracle.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream(), "utf-8"));// 防止乱码
			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				json.append(inputLine);
			}
			in.close();
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		return json.toString();
	}

}
