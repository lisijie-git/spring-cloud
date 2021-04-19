package com.lisijietech.rpc.api.domain.vo;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 用于Feign的请求数据对象。
 * 测试请求数据绑定对象的例子
 * @author LiSiJie
 * @date 2021年3月13日 上午7:45:51
 */
public class RequestFeignVO {
	private int i;
	private Integer integer;
	private String string;
	private String[] array;
	private List<String> list;
	private Map<String,String> map;
	private Date date;
	private LocalDateTime localDateTime;
	
	public void init() {
		this.i = 1;
		this.integer = 2;
		this.string = "init";
		this.array = new String[] {"param","param1","param2"};
		//https://blog.csdn.net/x541211190/article/details/79597236
		this.list = Stream.of(array).collect(Collectors.toList());
		
		this.map = new HashMap<>();
		this.map.put("a", "path");
		this.map.put("b", "header");
		this.map.put("c", "cookie-key=key-value");
		
		this.date = new Date();
		this.localDateTime = LocalDateTime.now();
	}
	
	public int getI() {
		return i;
	}
	public void setI(int i) {
		this.i = i;
	}
	public Integer getInteger() {
		return integer;
	}
	public void setInteger(Integer integer) {
		this.integer = integer;
	}
	public String getString() {
		return string;
	}
	public void setString(String string) {
		this.string = string;
	}
	public String[] getArray() {
		return array;
	}
	public void setArray(String[] array) {
		this.array = array;
	}
	public List<String> getList() {
		return list;
	}
	public void setList(List<String> list) {
		this.list = list;
	}
	public Map<String, String> getMap() {
		return map;
	}
	public void setMap(Map<String, String> map) {
		this.map = map;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public LocalDateTime getLocalDateTime() {
		return localDateTime;
	}
	public void setLocalDateTime(LocalDateTime localDateTime) {
		this.localDateTime = localDateTime;
	}

}
