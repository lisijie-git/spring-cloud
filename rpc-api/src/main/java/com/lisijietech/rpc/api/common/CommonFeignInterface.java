package com.lisijietech.rpc.api.common;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.lisijietech.rpc.api.domain.vo.RequestFeignVO;

/**
 * OpenFeign的常用方法声明接口。
 * 基础的feign方法声明使用案例，用来熟悉feign的准确使用，以及对原理的简单了解。
 * 个人理解，
 * feign使用http协议进行远程调用，就需要知道远程主机host地址，端口port，服务路径path，请求参数格式，以及响应数据格式等。
 * 而这些请求信息主要是通过声明的方法和注解来指明的。如：
 * feign接口的类上使用@FeignClient来指明主机host地址和端口port。
 * feign方法上使用注解来指明路径path、请求类型。请求类型可能没有严格的限制。
 * feign方法参数上使用注解来指明请求传输时，请求参数的格式。放在url的query中还是消息体中，以及参数是什么格式等。
 * 参数注解，声明在不同参数类型上，会有相对应的参数处理方式。
 * 当然，参数没有注解会有一些默认处理方式，
 * feign中，无注解默认处理方式一般都会把参数放在form表单中转换成POST请求，GET方法参数请求传递都要有注解。
 * 响应数据，根据响应内容类型、返回值类型，绑定在返回值中。主要是就json内容类型。
 * 复杂的请求参数最好用post请求方式，放在消息体中。
 * @author LiSiJie
 * @date 2021年3月15日 上午11:38:39
 */
public interface CommonFeignInterface {
	/**
	 * Feign接口，GET请求，无参数
	 * 
	 * 适用于：
	 * 服务端Controller层，GET请求。
	 * @return
	 */
	@PostMapping(value = "/web/get")
	public String get();
	
	/**
	 * Feign接口，GET请求，简单类型参数，参数必须要有@RequestParam。
	 * feign中简单类型参数无注解，会把请求变成POST请求，把无注解的参数处理成form表单放进消息体中，
	 * 原因可能与feign的默认的底层远程调用组件有关，可以设置。
	 * 
	 * 适用于：
	 * 服务端Controller层，GET请求，简单类型参数，参数指明@RequestParam注解或者无注解缺省默认参数解析。
	 * @param echo
	 * @param echo1
	 * @param echo2
	 * @return
	 */
	@GetMapping(value = "/web/get/params")
	public String getParams(
			@RequestParam(value = "echo") String echo
			,@RequestParam String echo1
//			,String echo2);
			,@RequestParam String echo2);
	
	//报错，参数无注解，默认传输处理，会转换成POST请求，参数放在form表单中。
	@GetMapping(value = "/web/get/params")
	public String getParams2(
			@RequestParam(value = "echo") String echo
			,@RequestParam String echo1
			,String echo2);
	
	/**
	 * Feign接口，GET请求，简单类型数组参数。参数必须要指明注解@RequestParam。
	 * 
	 * 适用于：
	 * 服务端的Controller层，GET请求，简单类型数组参数，参数必须要指明注解@RequestParam。
	 * @param echo
	 * @return
	 */
	@GetMapping(value = "/web/get/array")
	public String getArray(@RequestParam(value = "echo") String[] echo);
	
	/**
	 * Feign接口，GET请求，简单类型List参数。参数必须要指明注解@RequestParam。
	 * 
	 * 适用于：
	 * 服务端的Controller层，GET请求，简单类型List参数，参数必须要指明注解@RequestParam。
	 * @param echo
	 * @return
	 */
	@GetMapping(value = "/web/get/list")
	public String getList(@RequestParam(value = "echo") List<String> echo);
	
	/**
	 * feign接口，GET请求，Map类型参数。必须指明参数请求传输方式为@RequestParam，或者较新版本的@SpringQueryMap。
	 * 如果不指明参数绑定方式：
	 * 请求会转换为POST方式，参数放在消息体的form表单里，就报错，
	 * 但是，如果服务端是PostMapping的的处理方法，映射路径不变，map参数有@RequestParam注解，会调用成功。
	 * 参考网络，严格区分请求方法需要给方法注解的consumes参数做配置。
	 * 
	 * 不建议复杂类型参数，无论在GET还是POST等请求方式，通过无注解缺省默认处理或者@SpringQueryMap处理。
	 * 因为，如果成员变量类型或者map的value类型是复杂类型、数组类型或者集合类型，就不会被feign序列化成json字符串，
	 * 会得到他们的引用地址字符串。
	 * 最好用POST请求的@RequestBody处理，能很好的feign能很好的序列化，controller能很好的反序列化。
	 * 可能和参数处理方式，以及消息转换器有关。
	 * 
	 * 适用于：
	 * 服务端的Controller层，不论什么请求方法，Map类型参数，参数必须指明@RequestParam。
	 * 参数默认解析处理，不会自动把key=value放进map中，并且无论是GET还是POST请求，
	 * 请求无法映射到处理方法，报404错误，为什么这样需要看源码。
	 * 如有重复key，但都有不同作用，可配置区分。
	 * 注解@SpringQueryMap是SpringCloud中的，Spring web应该没用。
	 * @param echo
	 * @return
	 */
	@GetMapping(value = "/web/get/map")
	public Map<String,String> getMap(@RequestParam Map<String,String> echo);
	@GetMapping(value = "/web/get/map")
	public Map<String,String> getMap2(@SpringQueryMap Map<String,String> echo);
	@GetMapping(value = "/web/get/map")
	public Map<String,String> getMap3(Map<String,String> echo);
	//服务端Map类型参数有注解对比，服务端必须有注解才能解析Map类型参数。
	@GetMapping(value = "/web/get/map-request-param")
	public Map<String,String> getMapRequestParam(@RequestParam Map<String,String> echo);
	@GetMapping(value = "/web/get/map-request-param")
	public Map<String,String> getMapRequestParam2(@SpringQueryMap Map<String,String> echo);
	@GetMapping(value = "/web/get/map-request-param")
	public Map<String,String> getMapRequestParam3(Map<String,String> echo);
	//无注解参数，GET请求会被转换成POST请求，参数内容放在消息体form表单里
	@GetMapping(value = "/web/post/map-request-param")
	public Map<String,String> postMapRequestParam(Map<String,String> echo);
	
	/**
	 * feign接口，GET请求，Obj复杂类型参数(成员变量不能是复杂类型、数组、集合、map)，指明参数传输方式为@SpringQueryMap。
	 * 参数注解不能为@RequestParam，也不能是缺省默认，会报错。
	 * 复杂类型指明参数注解为@RequestParam：
	 * 复杂类型参数会被处理成url中query或者form-encoded的key=value编码形式，
	 * 但是value不是json字符串，是对象引用，服务端无法把引用字符串转换成对象，
	 * 即使自己把value处理成json字符串，可能Spring Web也没有把key=value的value转换成对象功能，可能要自己处理或集成设置。
	 * 复杂类型指明参数无注解，缺省默认不指明：
	 * 请求方式会转换成POST方式，参数绑定在消息体的form里，服务端是GET不允许POST且无法解析，也报错。
	 * 但是，如果服务端是PostMapping的的处理方法，映射路径不变，map参数有@RequestParam注解，会调用成功。
	 * 
	 * 不建议复杂类型参数，无论在GET还是POST等请求方式，通过无注解缺省默认处理或者@SpringQueryMap处理。
	 * 因为，如果成员变量类型或者map的value类型是复杂类型、数组类型或者集合类型，就不会被feign序列化成json字符串，
	 * 会得到他们的引用地址字符串。
	 * 最好用POST请求的@RequestBody处理，能很好的feign能很好的序列化，controller能很好的反序列化。
	 * 可能和参数处理方式，以及消息转换器有关。
	 * 
	 * 适用于：
	 * 服务端的Controller层，GET请求，Obj复杂类型参数，参数无注解声明，解析绑定请求参数的方法是缺省默认的。
	 * 注解@SpringQueryMap是SpringCloud中的，Spring web应该没用。
	 * 缺省默认参数解析会从url的query中，form表单中解析。如有重复key，但都有不同作用，可配置区分。
	 * 默认解析复杂对象类型参数，会给参数创建一个实例，无论成员属性有没有可映射的值。除非有参数校验注解。
	 * @param echo
	 * @return
	 */
	@GetMapping(value = "/web/get/obj")
	public RequestFeignVO getObj(@SpringQueryMap RequestFeignVO vo);
	@GetMapping(value = "/web/get/obj")
	public RequestFeignVO getObj2(@RequestParam RequestFeignVO vo);
	@GetMapping(value = "/web/get/obj")
	public RequestFeignVO getObj3(RequestFeignVO vo);
	@GetMapping(value = "/web/get/obj")
	public RequestFeignVO getObj4(@SpringQueryMap RequestFeignVO vo);
	
	/**
	 * feign接口，GET请求，Obj复杂类型参数，指明参数请求传输方式为@RequestParam。
	 * 异常信息显示，没有把obj转换成json字符串，而是在url后拼接Query数据key=value，参数名是绑定参数名，值为引用地址。
	 * 可能和参数转换器有关。也可能feign的GET请求方式url的Query参数处理方式就没有复杂对象处理的方式，要自己转换。
	 * 
	 * 复杂对象类型参数有@RequestParam注解，传输方式和解析方式就明确了，
	 * 但和Array、List、Map不同，参数对象会被绑定成key=value形式，参数名称为key，参数引用为value，
	 * 也许可以自己处理value，通过json工具或者重写toString方法等等使value转换为json字符串，
	 * 或者可能spirng有提供自定义功能。
	 * 
	 * 适用于：
	 * 服务端的Controller层，GET请求，Obj复杂类型参数，参数声明@RequestParam，解析绑定请求参数的方法是明确的。
	 * 复杂对象类型参数有@RequestParam注解，传输方式和解析方式和Array、List、Map不同，
	 * 参数对象会被绑定成key=value形式，参数名称为key，参数引用为value，
	 * 也许可以自己处理value，通过json工具或者重写toString方法等等使value转换为json字符串，
	 * 或者可能spirng提供自定义功能。待以后了解。
	 * 复杂对象类型，最好用POST请求，json内容类型，简单方便。
	 * @param vo
	 * @return
	 */
	@GetMapping(value = "/web/get/obj-request-param")
	public RequestFeignVO getObjRequestParam(@RequestParam RequestFeignVO vo);
	@GetMapping(value = "/web/get/obj-request-param")
	public RequestFeignVO getObjRequestParam2(@SpringQueryMap RequestFeignVO vo);
	@GetMapping(value = "/web/get/obj-request-param")
	public RequestFeignVO getObjRequestParam3(@RequestParam String vo);
	
	/**
	 * feign接口，无关请求方式，简单类型参数，参数请求传输方式声明一些常用注解。
	 * 注解@PathVariable：
	 * 路径变量方式的请求地址与参数变量，在请求路径上用{*}表示可变路径，参数上用注解绑定路径值。
	 * 在feign接口中，是将可变路径参数的值，替换到path路径上的相应位置，进行请求传输。在controller中则是相反的解析操作。
	 * feign接口是一个远程调用客户端，真正的请求路径应该没有{*}花括号的转义字符，应该被替换成参数值了，
	 * 那服务端如果有路径重叠会如何处理？猜测，匹配处理方法应该会优先匹配路径明确的处理方法。具体要看源码学习。
	 * 注解@RequestParam：
	 * feign中，在不同类型参数上声明，会有不同的处理方式。但一般都会把参数放在url的query段中。
	 * 不知道怎么把参数放进form表单中，是默认处理方法，还是@RequestParam有什么配置，待了解学习。
	 * 但是在controller中，此注解会从url的query中，form表单中解析数据绑定参数。
	 * 如果query中，和form表单中有重复key，而且作用不同都有用，可以通过spring配置区分，但不要这样设计接口，自我折磨。
	 * 注解@RequestHeader：
	 * feign中，参数请求传输的方式是在请求头中。
	 * 注解@CookieValue：
	 * feign中，注解@CookieValue是无效的。而且@CookieValue是用来解析请求头中Cookie的值对应的key的值绑定参数。
	 * feign中应该设置cookie请求头，然后给参数绑定key=value;这种cookie内容形式的值
	 * 无注解：
	 * feign中，一般来说，参数无注解，默认处理会把请求方式转换成POST，参数按照form表单格式放进消息体。
	 * 无注解参数处理也和feign底层远程调用组件有关，可配置。
	 * 
	 * 适用于：
	 * 服务端的Controller层，无关请求方式，简单类型参数，通过相应注解获得对应参数，
	 * 注解@RequestParam可以不声明，使用默认方式解析绑定。
	 * @param echo
	 * @return
	 */
	@RequestMapping(value = "/web/request/{path}")
	public String getPathVariable(
			@PathVariable(name = "path") String path
			,@RequestParam(value = "param") String  param
			,@RequestHeader(value = "request-header")String header
//			,@CookieValue String cookie
			,@RequestHeader(value = "cookie") String cookie);
	
	/**
	 * feign接口，POST请求，Obj复杂类型参数(成员变量不能是复杂类型、数组、集合、map)，指明@SpringQueryMap。
	 * 复杂类型对象参数请求传输方式为缺省默认处理方式,不起作用，可能需要明确参数绑定方式，效率更快。如,@RequestBody
	 * 如果在复杂对象参数加上@RequestParam，只会把参数按照url的Query中的key=value，参数名为key，参数引用为value。
	 * 
	 * 适用于：
	 * 服务端的Controller层，POST请求，复杂对象类型参数，参数无注解声明，参数请求传输方式为缺省默认处理方式。
	 * 缺省默认参数解析会从url的query中，form表单中解析。如有重复key，但都有不同作用，可配置区分。
	 * @param vo
	 * @return
	 */
	@PostMapping(value = "/web/post/obj")
	public RequestFeignVO postObj(RequestFeignVO vo);
	@PostMapping(value = "/web/post/obj")
	public RequestFeignVO postObj2(@SpringQueryMap RequestFeignVO vo);
	@PostMapping(value = "/web/post/obj")
	public RequestFeignVO postObj3(@RequestParam RequestFeignVO vo);
	
	/**
	 * feign接口，POST请求，Obj复杂类型参数，参数必须指明@RequestBody。
	 * 请求传递内容类型是json，在请求消息体中。
	 * 复杂类型对象参数推荐用法，简单通用。
	 * 
	 * feign接口中注解@CookieValue是无效的。
	 * 
	 * POST方法中，有@RequestBody，其他参数必须有注解，否则默认处理会把其他参数按照form表单格式放进消息体，
	 * 和json格式消息体冲突，报错，太多个消息体。
	 * 
	 * 适用于：
	 * 服务端的Controller层，POST请求，复杂对象类型参数，参数必须指明@RequestBody。
	 * 内容类型是json，在请求消息体中。
	 * @param vo
	 * @param param
	 * @param header
	 * @param cookie
	 * @return
	 */
	@PostMapping(value = "/web/post/json")
	public RequestFeignVO postJson(
			@RequestBody RequestFeignVO vo
			,@RequestParam(value = "param") String param
			,@RequestHeader(value = "request-header") String header
//			,@CookieValue String cookie);
			,@RequestHeader(value = "cookie") String cookie);
	
	/**
	 * feign接口，POST请求，传输文件MultipartFile类型，简单类型也行但没必要，参数必须指明@RequestPart。
	 * 请求传递内容类型是multipart，在请求消息体中。
	 * 主要用来传输文件，一般简单类型信息也行，但是传出数据会多出很多格式信息，没必要。
	 * 
	 * POST方法中，有@RequestPart，其他参数必须有注解，否则默认处理会把其他参数按照form表单格式放进消息体，
	 * 应该和multipart格式消息体冲突，也会报错。
	 * 
	 * 适用于：
	 * 服务端的Controller层，POST请求，解析文件MultipartFile类型，简单类型也行但没必要，参数必须指明@RequestPart。
	 * 内容类型是multipart，在请求消息体中。
	 * 主要用来传输文件，一般简单类型信息也行，但是传出数据会多出很多格式信息，没必要。
	 * @param vo
	 * @return
	 */
	@PostMapping(value = "/web/post/part")
	public String postPart(
			@RequestPart(value = "file") MultipartFile file
			,@RequestPart(value = "param") String param);
}
