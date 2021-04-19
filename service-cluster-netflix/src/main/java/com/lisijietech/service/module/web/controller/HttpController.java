package com.lisijietech.service.module.web.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lisijietech.service.module.web.vo.RequestVO;

/**
 * Http协议类型的Controller例子。
 * Spring Web会根据方法和注解以及其他配置来处理不同请求。
 * 进行请求参数绑定的时候，会根据方法注解和参数注解做不同的解析方式绑定参数，没有注解时候会有缺省默认的解析方式绑定参数。
 * 一些参数需要指明解析方式去绑定参数。
 * 参考：
 * https://segmentfault.com/a/1190000014766457?utm_source=tag-newest
 * https://blog.csdn.net/jy02268879/article/details/82830789
 * @author LiSiJie
 * @date 2021年3月12日 上午9:51:09
 */
@RestController
public class HttpController {
	@Value("${spring.application.name}")
	private String appName;
	
	@Value("${server.port}")
	private String port;
	
	//测试spring什么时候注入@Value标记的属性，是对象或者代理对象在初始化时，还是生成对象后。
	//生成对象后注入的，所以这里是null值
	protected String serviceInfo = appName + port;
	
	public String getServiceInfo() {
		return new StringBuilder(appName).append(",").append(port).append("    ").toString();
	}
	
	/**
	 * 服务端Controller层，GET请求。
	 * @return
	 */
	@GetMapping(value = "/web/get")
	public String get() {
		return getServiceInfo() + "对比成员变量serviceInfo:" + serviceInfo;
	}
	
	/**
	 * 服务端Controller层，GET请求，简单类型参数，参数指明@RequestParam注解或者无注解缺省默认参数解析。
	 * 简单类型参数的无注解默认解析，会从url的query和消息体的form表单中获取数据。
	 * @param echo
	 * @param echo1
	 * @param echo2
	 * @return
	 */
	@GetMapping(value = "/web/get/params")
	public String getParams(
			String echo
			,@RequestParam(value = "echo1") String echo1
			,@RequestParam String echo2) {
		return getServiceInfo() + echo + "," + echo1 + "," + echo2;
	}
	
	/**
	 * 服务端的Controller层，GET请求，简单类型数组参数，参数必须要指明注解@RequestParam。
	 * @param echo
	 * @return
	 */
	@GetMapping(value = "/web/get/array")
	public String getArray(@RequestParam(value = "echo") String[] echo) {
		return getServiceInfo() + Arrays.toString(echo);
	}
	
	/**
	 * 服务端的Controller层，GET请求，简单类型List参数，参数必须要指明注解@RequestParam。
	 * @param echo
	 * @return
	 */
	@GetMapping(value = "/web/get/list")
	public String getList(@RequestParam(value = "echo") List<String> echo) {
//		System.out.println(echo);
//		System.out.println("a" + echo);
//		System.out.println(Arrays.toString(echo.toArray()));
//		System.out.println(echo.stream().collect(Collectors.toList()));
//		System.out.println(echo.stream().collect(Collectors.joining(",")));
		return getServiceInfo() + echo;
	}
	
	/**
	 * 报错。
	 * 服务端的Controller层，不论什么请求方法，Map类型参数，参数必须指明@RequestParam。
	 * 参数默认解析处理，不会自动把key=value放进map中，并且无论是GET还是POST请求，
	 * 请求无法映射到处理方法，报404错误，为什么这样需要看源码。
	 * 如有重复key，但都有不同作用，可配置区分。
	 * @param echo
	 * @return
	 */
	@GetMapping(value = "/web/get/map")
	public Map<String,String> getMap(Map<String,String> echo) {
		if(echo != null) {
			echo.put("serviceInfo", getServiceInfo());
		}
		return echo;
	}
	
	/**
	 * 服务端的Controller层，不论什么请求，Map类型参数，参数必须指明@RequestParam。
	 * 如有重复key，但都有不同作用，可配置区分。
	 * 参数无注解缺省默认方式，会报错，可能和底层远程调用有关。
	 * @param echo
	 * @return
	 */
	@GetMapping(value = "/web/get/map-request-param")
	public Map<String,String> getMapRequestParam(@RequestParam Map<String,String> echo) {
		if(echo != null) {
			echo.put("serviceInfo", getServiceInfo());
		}
		return echo;
	}
	
	/**
	 * 服务端的Controller层，不论什么请求，Map类型参数，参数必须指明@RequestParam。
	 * 如有重复key，但都有不同作用，可配置区分。
	 * 参数无注解缺省默认方式，会报错，可能和底层远程调用有关。
	 * @param echo
	 * @return
	 */
	@PostMapping(value = "/web/post/map-request-param")
	public Map<String,String> postMapRequestParam(@RequestParam Map<String,String> echo){
		if(echo != null) {
			echo.put("serviceInfo", getServiceInfo());
		}
		return echo;
	}
	
	/**
	 * 服务端的Controller层，GET请求，Obj复杂类型参数，参数无注解声明，解析绑定请求参数的方法是缺省默认的。
	 * 缺省默认参数解析会从url的query中，form表单中解析。如有重复key，但都有不同作用，可配置区分。
	 * 默认解析复杂对象类型参数，会给参数创建一个实例，无论成员属性有没有可映射的值。除非有参数校验注解。
	 * @param vo
	 * @return
	 */
	@GetMapping(value = "/web/get/obj")
	public RequestVO getObj(RequestVO vo) {
		if(vo != null) {
			vo.setString(getServiceInfo() + vo.getString());
		}
		return vo;
	}
	
	/**
	 * 服务端的Controller层，GET请求，Obj复杂类型参数，参数声明@RequestParam，解析绑定请求参数的方法是明确的。
	 * 复杂对象类型参数有@RequestParam注解，传输方式和解析方式和Array、List、Map不同，
	 * 参数对象会被绑定成key=value形式，参数名称为key，参数引用为value，
	 * 也许可以自己处理value，通过json工具或者重写toString方法等等使value转换为json字符串，
	 * 或者可能spirng提供自定义功能。待以后了解。
	 * 复杂对象类型，最好用POST请求，json内容类型，简单方便。
	 * @param echo
	 * @return
	 */
	@GetMapping(value = "/web/get/obj-request-param")
	public RequestVO getObjRequestParam(@RequestParam RequestVO vo) {
		if(vo != null) {
			vo.setString(getServiceInfo() + vo.getString());
		}
		return vo;
	}
	
	/**
	 * 服务端的Controller层，无关请求方式，简单类型参数，通过相应注解获得对应参数，
	 * 注解@PathVariable：
	 * 路径变量方式的请求地址与参数变量，在请求路径上用{*}表示可变路径，参数上用注解绑定路径值。
	 * 在feign接口中，是将可变路径参数的值，替换到path路径上的相应位置，进行请求传输。在controller中则是相反的解析操作。
	 * 那服务端如果有路径重叠会如何处理？猜测，匹配处理方法应该会优先匹配路径明确的处理方法。具体要看源码学习。
	 * 注解@RequestParam：
	 * 在controller中，此注解会从url的query中，form表单中解析数据绑定参数。
	 * 注解@RequestParam可以不声明，使用默认方式解析绑定。
	 * 如果query中，和form表单中有重复key，而且作用不同都有用，可以通过spring配置区分，但不要这样设计接口，自我折磨。
	 * 注解@RequestHeader：
	 * 解析请求头中数据绑定参数。
	 * 注解@CookieValue：
	 * 解析请求头中Cookie的值对应的key的值绑定参数。
	 * 无注解：
	 * 在controller中，会从url的query中，form表单中解析数据绑定参数，还会处理复杂类型对象。
	 * @param echo
	 * @return
	 */
	@GetMapping(value = "/web/request/{path}")
	public String getPathVariable(
			@PathVariable(name = "path") String path
			,@RequestParam(value = "param") String  param
			,@RequestHeader(value = "request-header")String header
			,@CookieValue("cookie-key") String cookie) {
		return getServiceInfo() + "path:" + path + ",param:" + param + ",header:" + header + ",cookie:" + cookie;
	}
	
	/**
	 * 服务端的Controller层，POST请求，复杂对象类型参数，参数无注解声明，参数请求传输方式为缺省默认处理方式。
	 * 缺省默认参数解析会从url的query中，form表单中解析。如有重复key，但都有不同作用，可配置区分。
	 * @param vo
	 * @return
	 */
	@PostMapping(value = "/web/post/obj")
	public RequestVO postObj(RequestVO vo) {
		if(vo != null) {
			vo.setString(getServiceInfo() + vo.getString());
		}
		return vo;
	}
	
	/**
	 * 服务端的Controller层，POST请求，复杂对象类型参数，参数必须指明@RequestBody。
	 * 内容类型是json，在请求消息体中。
	 * @param vo
	 * @return
	 */
	@PostMapping(value = "/web/post/json")
	public RequestVO postJson(
			@RequestBody RequestVO vo
			,@RequestParam(value = "param") String  param
			,@RequestHeader(value = "request-header")String header
			,@CookieValue("cookie-key") String cookie) {
		if(vo != null) {
			vo.setString(getServiceInfo() + vo.getString() 
			+ ",param:" + param + ",header:" + header + ",cookie:" + cookie);
		}
		return vo;
	}
	
	/**
	 * 服务端的Controller层，POST请求，解析文件MultipartFile类型，简单类型也行但没必要，参数必须指明@RequestPart。
	 * 内容类型是multipart，在请求消息体中。
	 * 主要用来传输文件，一般简单类型信息也行，但是传出数据会多出很多格式信息，没必要。
	 * @param file
	 * @param param
	 * @return
	 */
	@PostMapping(value = "/web/post/part")
	public String postPart(
			@RequestPart(value = "file") MultipartFile file
			,@RequestPart(value = "param") String param) {
		Long fileSize = null;
		if(file != null) {
			fileSize = file.getSize();
		}
		return getServiceInfo() + ",fileSize:" + fileSize + ",param:" + param;
	}
	
}
