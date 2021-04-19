package com.lisijietech.service.module.openfeign.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lisijietech.rpc.api.common.CommonFeignInterface;
import com.lisijietech.rpc.api.domain.vo.RequestFeignVO;
import com.lisijietech.rpc.api.openfeign.ConsumerFeign;
import com.lisijietech.rpc.api.openfeign.ProducerFeign;

/**
 * 使用OpenFeign进行Http调用。
 * 生产服务和消费服务功能是一摸一样的，只是分成两个服务项目好理解。
 * 服务间可以用feign随意调用。如，生产者调用生产者集群，生产者调用消费者集群，消费者亦是如此。
 * feign调用的功能都是简单功能，不会出现循环调用的情况。
 * @author LiSiJie
 * @date 2021年3月13日 上午8:39:57
 */
@RestController
public class OpenFeignController {
	@Autowired
	private ProducerFeign producerFeign;
	@Autowired
	private ConsumerFeign consumerFeign;
	
	@GetMapping(value = "/openfeign/test/{service}/{method}")
	public Object doOpenFeignTest(@PathVariable(name = "service") String service
			,@PathVariable(name = "method") String method) throws JsonProcessingException {
		CommonFeignInterface feign = null;
		if("producer".equals(service)) {
			feign = producerFeign;
		}else if("consumer".equals(service)){
			feign = consumerFeign;
		}
		ObjectMapper jacksonMapper = new ObjectMapper();
		
		RequestFeignVO obj = new RequestFeignVO();
		obj.init();
		
		Object result = null;
		switch(method) {
		case "get":
			String get = feign.get();
			result = get;
			break;
		case "getParams":
			String getParams = feign.getParams(obj.getArray()[0],obj.getArray()[1],obj.getArray()[2]);
			result = getParams;
			break;
		case "getParams2":
			String getParams2 = feign.getParams2(obj.getArray()[0],obj.getArray()[1],obj.getArray()[2]);
			result = getParams2;
			break;
		case "getArray":
			String getArray = producerFeign.getArray(obj.getArray());
			result = getArray;
			break;
		case "getList":
			String getList = producerFeign.getList(obj.getList());
			result = getList;
			break;
		case "getMap":
			Map<String,String> getMap = producerFeign.getMap(obj.getMap());
			result = getMap;
			break;
		case "getMap2":
			Map<String,String> getMap2 = producerFeign.getMap2(obj.getMap());
			result = getMap2;
			break;
		case "getMap3":
			Map<String,String> getMap3 = producerFeign.getMap3(obj.getMap());
			result = getMap3;
			break;
		case "getMapRequestParam":
			Map<String,String> getMapRequestParam = producerFeign.getMapRequestParam(obj.getMap());
			result = getMapRequestParam;
			break;
		case "getMapRequestParam2":
			Map<String,String> getMapRequestParam2 = producerFeign.getMapRequestParam2(obj.getMap());
			result = getMapRequestParam2;
			break;
		case "getMapRequestParam3":
			Map<String,String> getMapRequestParam3 = producerFeign.getMapRequestParam3(obj.getMap());
			result = getMapRequestParam3;
			break;
		case "postMapRequestParam":
			Map<String,String> postMapRequestParam = producerFeign.postMapRequestParam(obj.getMap());
			result = postMapRequestParam;
			break;
		case "getObj":
			RequestFeignVO getObj = producerFeign.getObj(obj);
			result = getObj;
			break;
		case "getObj2":
			RequestFeignVO getObj2 = producerFeign.getObj2(obj);
			result = getObj2;
			break;
		case "getObj3":
			RequestFeignVO getObj3 = producerFeign.getObj3(obj);
			result = getObj3;
			break;
		case "getObj4":
			RequestFeignVO simpleObj = new RequestFeignVO();
			simpleObj.setString("对象Map、Array、List引用类型的成员属性没有值，toString不会是引用地址字符");
			RequestFeignVO getObj4 = producerFeign.getObj4(simpleObj);
			result = getObj4;
			break;
		case "getObjRequestParam":
			RequestFeignVO getObjRequestParam = producerFeign.getObjRequestParam(obj);
			result = getObjRequestParam;
			break;
		case "getObjRequestParam2":
			RequestFeignVO getObjRequestParam2 = producerFeign.getObjRequestParam2(obj);
			result = getObjRequestParam2;
			break;
		case "getObjRequestParam3":
			String objStr = jacksonMapper.writeValueAsString(obj);
			RequestFeignVO getObjRequestParam3 = producerFeign.getObjRequestParam3(objStr);
			result = getObjRequestParam3;
			break;
		case "getPathVariable":
			String getPathVariable = producerFeign.getPathVariable(obj.getMap().get("a"),obj.getArray()[0]
					,obj.getMap().get("b"),obj.getMap().get("c"));
			result = getPathVariable;
			break;
		case "postObj":
			RequestFeignVO postObj = producerFeign.postObj(obj);
			result = postObj;
			break;
		case "postObj2":
			RequestFeignVO postObj2 = producerFeign.postObj2(obj);
			result = postObj2;
			break;
		case "postObj3":
			RequestFeignVO postObj3 = producerFeign.postObj3(obj);
			result = postObj3;
			break;
		case "postJson":
			RequestFeignVO postJson = producerFeign.postJson(obj,obj.getArray()[0]
					,obj.getMap().get("b"),obj.getMap().get("c"));
			System.out.println(jacksonMapper.writeValueAsString(postJson));
			result = postJson;
			break;
		case "postPart":
			String postPart = producerFeign.postPart(new MockMultipartFile("file",new byte[0]), obj.getArray()[0]);
			result = postPart;
			break;
		default:
			result = "没有方法";
		}
		
		return result;
	}
}
