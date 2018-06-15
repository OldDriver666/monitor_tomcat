//package com.fise.monitor.heartbeat;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLConnection;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//public class HeartBeat2 implements CommandLineRunner {
//
//	public static Map<String, String> urlMap = new HashMap<String, String>(); // urlMap
//	public static Map<String, Integer> countMap = new HashMap<String, Integer>(); // 计数Map
//
//	@Override
//    public void run(String... args) throws Exception {
//        System.out.println(">>>>>>>>>>>>>>>服务启动执行，执行加载数据等操作<<<<<<<<<<<<<");
//        while (1 == 1) {
//        	try {
//        		Thread.sleep(10000);
//        	} catch (Exception e) {
//        		e.printStackTrace();
//        	}
//        	heartBeat();
//        }
//    }
//	
//	// 心跳监测方法
//	public static void heartBeat() {
//		if (urlMap.size() == 0) {
//			initUrlMap(urlMap);
//		}
//		if (countMap.size() == 0) {
//			initCountMap(countMap);
//		}
//		// 遍历url
//		for (Map.Entry<String, String> entry : urlMap.entrySet()) {
//			System.out.println(entry.getKey() + ":" + entry.getValue() + ":" + loadJSON(entry.getValue()));
//			String result = null;
//			// 尝试请求3次,如果result不是"helloworld"继续请求
//			for (int i = 0; i < 3; i++) {
//				result = loadJSON(entry.getValue());
//				if ("helloworld".equals(result)) {
//					break;
//				}
//				try {
//					Thread.sleep(10000);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//			// 如果result不是"helloworld",并且发送次数低于3次
//			if (!"helloworld".equals(result) && countMap.get(entry.getKey()) < 3) {
//				try {
//					EmailUtil.sendEmail(entry.getKey() + "系统异常报告", entry.getKey() + "无反应");
//					countMap.put(entry.getKey(), countMap.get(entry.getKey()) + 1);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
////				Sms.sendSMS("13128726378", entry.getKey() + "系统异常报告", false);
//			}
//			if ("helloworld".equals(result)) { // 发送次数初始化为0
//				countMap.put(entry.getKey(), 0);
//			}
//
//		}
//	}
//
//	// 初始化urlMap
//	public static Map<String, String> initUrlMap(Map<String, String> urlMap) {
//
//		urlMap.put("小位开发", "http://10.252.252.250:8787/managesvr/heartbeat/helloworld");
//		urlMap.put("小位测试", "http://file.fise-wi.com:8787/managesvr/heartbeat/helloworld");
//		urlMap.put("小位生产", "http://file.fise-wi.com:8589/managesvr/heartbeat/helloworld");
//		urlMap.put("猫我开发", "http://10.252.252.250:8787/xiaoyusvr/heartbeat/helloworld");
//		urlMap.put("猫我测试", "http://xiaoyutest.fise-wi.com:8787/xiaoyusvr/heartbeat/helloworld");
//		urlMap.put("猫我生产", "http://xiaoyu.fise-wi.com:8787/xiaoyusvr/heartbeat/helloworld");
//		urlMap.put("物联开发", "http://10.252.252.250:8787/iot/heartbeat/helloworld");
//		urlMap.put("物联测试", "http://39.108.17.123:8787/iot/heartbeat/helloworld");
//		return urlMap;
//
//	}
//
//	// 初始化计数Map
//	public static Map<String, Integer> initCountMap(Map<String, Integer> urlMap) {
//
//		urlMap.put("小位开发", 0);
//		urlMap.put("小位测试", 0);
//		urlMap.put("小位生产", 0);
//		urlMap.put("猫我开发", 0);
//		urlMap.put("猫我测试", 0);
//		urlMap.put("猫我生产", 0);
//		urlMap.put("物联开发", 0);
//		urlMap.put("物联测试", 0);
//		return urlMap;
//
//	}
//
//	// http请求
//	public static String loadJSON(String url) {
//		StringBuilder json = new StringBuilder();
//		try {
//			URL oracle = new URL(url);
//			URLConnection yc = oracle.openConnection();
//			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream(), "utf-8"));// 防止乱码
//			String inputLine = null;
//			while ((inputLine = in.readLine()) != null) {
//				json.append(inputLine);
//			}
//			in.close();
//		} catch (MalformedURLException e) {
//		} catch (IOException e) {
//		}
//		return json.toString();
//	}
//
//}
