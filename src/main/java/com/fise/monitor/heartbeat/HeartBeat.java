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
import org.springframework.util.StringUtils;

@Configuration
@EnableScheduling
public class HeartBeat {

	public static Map<String, Integer> countMap = new HashMap<String, Integer>(); // 计数Map
	
//	public static void main(String[] args) {
//		System.out.println(loadJSON("http://xiaoyutest.fise-wi.com:8080/xiaoyu/"));
//	}

	@Scheduled(cron = "0 0/5 * * * ?") // 每5分钟执行一次
	public void scheduler() {
		heartBeat();
	}

	// 心跳监测方法
	public static void heartBeat() {
		if (countMap.size() == 0) {
			initCountMap(countMap);
		}
		// 遍历url
		for (SystemEnums item : SystemEnums.values()) {
			String result1 = null;
			String result2 = null;
			String name = item.getName();
			// 后端尝试请求3次,如果result1不是"helloworld"继续请求
			for (int i = 0; i < 3; i++) {
				result1 = loadJSON(item.getHeartbeat());				
				if ("helloworld".equals(result1)) {
					break;
				}
				try {
					Thread.sleep(10000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// 前端尝试请求3次,如果result2是空值继续请求
			for (int i = 0; i < 3; i++) {
				result2 = loadJSON(item.getUrl());				
				if (!StringUtils.isEmpty(result2)) {
					break;
				}
				try {
					Thread.sleep(10000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// 如果result1不是"helloworld"或者 result2是空值,并且发送次数少于2次
			if ((StringUtils.isEmpty(result2) || !"helloworld".equals(result1)) && countMap.get(name) < 2) {
				try {
					EmailUtil.sendEmail(name + "红色警报",
							"<h2><a href=\"" + item.getUrl() + "\"><font color=\"red\">红色警报!" + name + "异常!请速速处理!</font></a></h2>");
					countMap.put(name, countMap.get(name) + 1);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					Runtime.getRuntime().exec("/home/fise/bin/alarm_report.py " + item.getAlarm());
				} catch (IOException e) {
					e.printStackTrace();
				}
				// Sms.sendSMS("13128726 378", name + "系统异常报告", false);
			}
			// 如果result是"helloworld",同时result2不是是空值,并且发送次数大于0次,则发送邮件提示和发送次数初始化为0
			if (!StringUtils.isEmpty(result2) && "helloworld".equals(result1) && countMap.get(name) > 0) {
				try {
					EmailUtil.sendEmail(name + "友情提示", "<h2><a href=\"" + item.getUrl() + "\"><font color=\"green\">" + name + "恢复正常</font></a></h2>");
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					Runtime.getRuntime().exec("/home/fise/bin/alarm_report.py " + item.getSuccess());
				} catch (IOException e) {
					e.printStackTrace();
				}
				countMap.put(name, 0);
			}

		}
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
