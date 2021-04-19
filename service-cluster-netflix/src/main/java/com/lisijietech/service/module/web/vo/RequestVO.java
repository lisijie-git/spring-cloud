package com.lisijietech.service.module.web.vo;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 请求数据对象。
 * 测试请求数据绑定对象的例子
 * @author LiSiJie
 * @date 2021年3月12日 上午11:38:41
 */
public class RequestVO {
	private int i;
	private Integer integer;
	private String string;
	private String[] array;
	private List<String> list;
	private Map<String,String> map;
	private Date date;
	private LocalDateTime localDateTime;
	
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
