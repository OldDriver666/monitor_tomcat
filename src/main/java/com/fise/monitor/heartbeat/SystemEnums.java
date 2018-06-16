package com.fise.monitor.heartbeat;

/**
 * 系统类型
 * @author tany
 *
 */

public enum SystemEnums {
	
    XW_DEV     ("小位开发系统", "http://10.252.252.250:8787/managesvr/heartbeat/helloworld" ,"XIAOWEI_MANAGER_SYSTEM_DEV_BACK_ONLINE", "XIAOWEI_MANAGER_SYSTEM_DEV_ALARM" , "http://10.252.252.250:8888/boss/"),
    XW_TEST    ("小位测试系统", "http://file.fise-wi.com:8787/managesvr/heartbeat/helloworld" ,"XIAOWEI_MANAGER_SYSTEM_TEST_BACK_ONLINE", "XIAOWEI_MANAGER_SYSTEM_TEST_ALARM" , "http://tboss.fise-wi.com/"),
	XW_PRODUCE ("小位生产系统", "http://file.fise-wi.com:8589/managesvr/heartbeat/helloworld" ,"XIAOWEI_MANAGER_SYSTEM_PRODUCE_BACK_ONLINE", "XIAOWEI_MANAGER_SYSTEM_PRODUCE_ALARM" , "http://boss.fise-wi.com/"),
	MW_DEV     ("猫我开发系统", "http://10.252.252.250:8787/xiaoyusvr/heartbeat/helloworld" ,"MAOWO_MANAGER_SYSTEM_DEV_BACK_ONLINE", "MAOWO_MANAGER_SYSTEM_DEV_ALARM" , "http://10.252.252.250:8888/xiaoyu/"),
	MW_TEST    ("猫我测试系统", "http://xiaoyutest.fise-wi.com:8787/xiaoyusvr/heartbeat/helloworld" ,"MAOWO_MANAGER_SYSTEM_TEST_BACK_ONLINE", "MAOWO_MANAGER_SYSTEM_TEST_ALARM" , "http://xiaoyutest.fise-wi.com:8080/xiaoyu/" ),
	MW_PRODUCE ("猫我生产系统", "http://xiaoyu.fise-wi.com:8787/xiaoyusvr/heartbeat/helloworld" ,"MAOWO_MANAGER_SYSTEM_PRODUCE_BACK_ONLINE", "MAOWO_MANAGER_SYSTEM_PRODUCE_ALARM" , "http://xiaoyu.fise-wi.com:8080/xiaoyu/" ),
	IOT_DEV    ("物联开发系统", "http://10.252.252.250:8787/iot/heartbeat/helloworld" ,"IOT_DEV_BACK_ONLINE", "IOT_DEV_ALARM" , "http://10.252.252.250:8787/iot/" ),
	IOT_TEST   ("物联测试系统", "http://39.108.17.123:8787/iot/heartbeat/helloworld" ,"IOT_TEST_BACK_ONLINE", "IOT_TEST_ALARM" , "http://39.108.17.123:8787/iot/");
	
	private String name;
	private String heartbeat;
	private String success;
	private String alarm;
	private String url;

	private SystemEnums(String name, String heartbeat, String success, String alarm, String url) {
		this.name = name;
		this.heartbeat = heartbeat;
		this.success = success;
		this.alarm=alarm;
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public String getHeartbeat() {
		return heartbeat;
	}

	public String getUrl() {
		return url;
	}

	public String getAlarm() {
		return alarm;
	}

	public String getSuccess() {
		return success;
	}

	public static String getAlarm(String name){
		for (SystemEnums item : SystemEnums.values()) {
			if(name.equals(item.getName())){
				return item.getAlarm();
			}
		}
		return "";
	}
	public static String getHeartbeat(String name){
		for (SystemEnums item : SystemEnums.values()) {
			if(name.equals(item.getName())){
				return item.getHeartbeat();
			}
		}
		return "";
	}
	
	public static String getSuccess(String name){
		for (SystemEnums item : SystemEnums.values()) {
			if(name.equals(item.getName())){
				return item.getSuccess();
			}
		}
		return "";
	}
	
	public static String getUrl(String name){
		for (SystemEnums item : SystemEnums.values()) {
			if(name.equals(item.getName())){
				return item.getUrl();
			}
		}
		return "";
	}

}
