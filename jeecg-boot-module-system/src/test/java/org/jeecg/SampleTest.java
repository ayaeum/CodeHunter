package org.jeecg;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.domain.Data;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.kevinsawicki.http.HttpRequest;
import freemarker.cache.StringTemplateLoader;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONUtil;
import net.sf.json.util.JSONUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.checkerframework.checker.fenum.qual.AwtAlphaCompositingRule;
import org.jeecg.modules.demo.mock.MockController;
import org.jeecg.modules.demo.test.entity.JeecgDemo;
import org.jeecg.modules.demo.test.mapper.JeecgDemoMapper;
import org.jeecg.modules.demo.test.service.IJeecgDemoService;
import org.jeecg.modules.system.entity.*;
import org.jeecg.modules.system.mapper.AccessTokenMapper;
import org.jeecg.modules.system.mapper.AppUserInfoMapper;
import org.jeecg.modules.system.mapper.TaskScanningSchemeMapper;
import org.jeecg.modules.system.model.HttpClientUtil;
import org.jeecg.modules.system.service.*;
import org.jeecg.modules.system.util.CodeUtil;
import org.jeecg.modules.system.util.MethodExtractorUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = JeecgSystemApplication.class)
public class SampleTest {

	@Resource
	private ISendMailService iSendMailService;

	@Resource
	private IGitlabService iGitlabService;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Resource
	private TaskScanningSchemeMapper taskScanningSchemeMapper;

	private static final ObjectMapper mapper = new ObjectMapper();

	@Resource
	private org.jeecg.modules.system.util.sendHtmlMail sendHtmlMail;

	@Autowired
	TemplateEngine templateEngine;

	@Resource
	private ITaskManagementTableService iTaskManagementTableService;

	@Resource
	private ITaskScanResultService iTaskScanResultService;

	@Resource
	private CodeUtil codeUtil;

	String code="\t@Test\n" +
			"\tpublic void test(){\n" +
			"\t\tString filePath=\"F:\\\\jeecg-boot-master\\\\jeecg-boot-master\\\\jeecg-boot\\\\jeecg-boot-module-system\\\\src\\\\main\\\\java\\\\org\\\\jeecg\\\\modules\\\\system\\\\service\\\\impl\\\\SysDataSourceServiceImpl.java\";\n" +
			"\t\tFile file = new File(filePath);\n" +
			"\t\tCompilationUnit cu = null;\n" +
			"\t\ttry {\n" +
			"\t\t\tcu = StaticJavaParser.parse(file);\n" +
			"\t\t} catch (FileNotFoundException e) {\n" +
			"\t\t\te.printStackTrace();\n" +
			"\t\t}\n" +
			"\t\t//1.获取方法名和参数\n" +
			"\t\tnew MethodExtractorUtil.MethodVisitor().visit(cu, null);\n" +
			"\n" +
			"\t\t//2.分割方法\n" +
			"\t\ttry {\n" +
			"\t\t\tJSONArray jsonArray = new MethodExtractorUtil.MethodVisitor().methodCutter(cu, filePath);\n" +
			"\t\t\ttry {\n" +
			"\t\t\t\tCloseableHttpClient httpClient = HttpClients.createDefault();//创建一个获取连接客户端的工具\n" +
			"\t\t\t\tHttpPost httpPost = new HttpPost(\"http://8.134.53.160:5000/cloneDetection/\");//创建Post请求\n" +
			"\t\t\t\thttpPost.addHeader(\"Content-Type\", \"application/json;charset=utf-8\");//添加请求头\n" +
			"\t\t\t\tStringEntity entity = new StringEntity(jsonArray.toJSONString());//使用StringEntity转换成实体类型\n" +
			"\t\t\t\tSystem.out.println(jsonArray.toJSONString());\n" +
			"\t\t\t\tentity.setContentEncoding(\"UTF-8\");\n" +
			"\t\t\t\tentity.setContentType(\"application/json\");//发送json数据需要设置contentType\n" +
			"\t\t\t\thttpPost.setEntity(entity);//将封装的参数添加到Post请求中\n" +
			"\t\t\t\tCloseableHttpResponse response = httpClient.execute(httpPost);//执行请求\n" +
			"\t\t\t\tHttpEntity responseEntity = response.getEntity();//获取响应的实体\n" +
			"\t\t\t\tString entityString = EntityUtils.toString(responseEntity);//转化成字符串\n" +
			"\t\t\t\tSystem.out.println(entityString);\n" +
			"\t\t\t\tJSONArray jsonArray1 = JSONArray.parseArray(entityString);\n" +
			"\t\t\t\tSystem.out.println(jsonArray1);\n" +
			"\n" +
			"\t\t\t\tString httpResult = new String(entityString.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);//返回的是Utf-8格式，所以需要转换一下格式，不然乱码\n" +
			"\t\t\t\tSystem.out.println(httpResult);\n" +
			"\t\t\t\tresponse.close();\n" +
			"\t\t\t\thttpClient.close();\n" +
			"\t\t\t} catch (Exception e) {\n" +
			"\t\t\t}\n" +
			"\t\t} catch (IOException e) {\n" +
			"\t\t\te.printStackTrace();\n" +
			"\t\t}\n" +
			"\n" +
			"\t\tHttpClientUtil.createGetHttpByPath(\"http://8.134.53.160:5000/test/\");\n" +
			"\t}\n" +
			"\n" +
			"\t@Test\n" +
			"\tpublic void test2(){\n" +
			"\n" +
			"\t}\n" +
			"\n" +
			"\t@Test\n" +
			"\tpublic void sendHtmlMailThymeleaf(){\n" +
			"\t\tSystem.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern(\"yyyy-MM-dd HH:mm:ss\")));\n" +
			"\t\tContext ctx = new Context();\n" +
			"\t\t//查出任务信息\n" +
			"\t\tctx.setVariable(\"timestamp\", LocalDateTime.now().format(DateTimeFormatter.ofPattern(\"yyyy-MM-dd HH:mm:ss\")));\n" +
			"\t\tString mail = templateEngine.process(\"scanresult.html\", ctx);\n" +
			"\t\tsendHtmlMail.sendHtmlMails(\"1192129669@qq.com\",\n" +
			"\t\t\t\t\"1192129669@qq.com\",\n" +
			"\t\t\t\t\"测试邮件主题（Thymeleaf）\",\n" +
			"\t\t\t\tmail);\n" +
			"\t}\n" +
			"\n" +
			"\t@Test\n" +
			"\tpublic void sendMailHtml(){\n" +
			"\t\tString to=\"1192129669@qq.com\";\n" +
			"\t\tString subject=\"主题:HTML的邮件 \";\n" +
			"\t\tString content=\"<h1>这是一个HTML类型的邮件</h1>\";\n" +
			"\t\tsendHtmlMail.sendHtmlMail(to,subject,content);\n" +
			"\t}\n" +
			"\n" +
			"\t@Test\n" +
			"\tpublic void testRedis() {\n" +
			"\t\tstringRedisTemplate.opsForValue().set(\"host:port\",\"8.134.53.160:8080\",1, TimeUnit.MINUTES);\n" +
			"//\t\tSystem.out.println(stringRedisTemplate.opsForValue().get(\"host:port\"));\n" +
			"\t}\n" +
			"\n" +
			"\t@Test\n" +
			"\tpublic void testMail() {\n" +
			"\t\tMailTemplate template = new MailTemplate(\"1192129669@qq.com\",\"1192129669@qq.com\",\"CodeHunter通知\",\"欢迎使用CodeHunter！\");\n" +
			"\t\tiSendMailService.SendMail(template);\n" +
			"\t}\n" +
			"\n" +
			"\t@Test\n" +
			"\tpublic void TestGitlab(){\n" +
			"\t\t//获取所有用户的所有仓库\n" +
			"\t\tString s = HttpClientUtil.createGetHttpByPath(\"http://8.134.53.160:8081/api/v4/projects/1/members?private_token=1sRC1BZfxk7V2ZizkGJe&per_page=10\");\n" +
			"\t\tSystem.out.println(s);\n" +
			"\t}\n" +
			"\n" +
			"\t@Test\n" +
			"\tpublic void getUserProjects(){\n" +
			"\t\tString private_token = \"1sRC1BZfxk7V2ZizkGJe\";\n" +
			"\t\tString host = \"8.134.53.160:8081\";\n" +
			"\t\tSystem.out.println(iGitlabService.getUserProjects(host,private_token));\n" +
			"\t}\n" +
			"\n" +
			"\t@Test\n" +
			"\tpublic void getFileTree(){\n" +
			"\t\tString path = \"root%2Fcodehunter\";\n" +
			"\t\tString private_token = \"1sRC1BZfxk7V2ZizkGJe\";\n" +
			"\t\tString host = \"8.134.53.160:8081\";\n" +
			"\t\tiGitlabService.getFileTree(host,path,\"master\",private_token);\n" +
			"\t}";

	@Test
	public void testQueryCurrentTaskResult(){
		String[] strings = codeUtil.codeDivideToLine(code);
//		List<TaskScanResult> taskScanResults = iTaskScanResultService.queryAllTaskResultsById(32);
//		System.out.println(taskScanResults.get(taskScanResults.size()-1));
	}

	@Test
	public void test(){
		String filePath="F:\\jeecg-boot-master\\jeecg-boot-master\\jeecg-boot\\jeecg-boot-module-system\\src\\main\\java\\org\\jeecg\\modules\\system\\service\\impl\\SysDataSourceServiceImpl.java";
		File file = new File(filePath);
		CompilationUnit cu = null;
		try {
			cu = StaticJavaParser.parse(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		//1.获取方法名和参数
		new MethodExtractorUtil.MethodVisitor().visit(cu, null);

		//2.分割方法
		try {
			JSONArray jsonArray = new MethodExtractorUtil.MethodVisitor().methodCutter(cu, filePath);
			try {
				CloseableHttpClient httpClient = HttpClients.createDefault();//创建一个获取连接客户端的工具
				HttpPost httpPost = new HttpPost("http://8.134.53.160:5000/cloneDetection/");//创建Post请求
				httpPost.addHeader("Content-Type", "application/json;charset=utf-8");//添加请求头
				StringEntity entity = new StringEntity(jsonArray.toJSONString());//使用StringEntity转换成实体类型
				System.out.println(jsonArray.toJSONString());
				entity.setContentEncoding("UTF-8");
				entity.setContentType("application/json");//发送json数据需要设置contentType
				httpPost.setEntity(entity);//将封装的参数添加到Post请求中
				CloseableHttpResponse response = httpClient.execute(httpPost);//执行请求
				HttpEntity responseEntity = response.getEntity();//获取响应的实体
				String entityString = EntityUtils.toString(responseEntity);//转化成字符串
				System.out.println(entityString);
				JSONArray jsonArray1 = JSONArray.parseArray(entityString);
				System.out.println(jsonArray1);

				String httpResult = new String(entityString.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);//返回的是Utf-8格式，所以需要转换一下格式，不然乱码
				System.out.println(httpResult);
				response.close();
				httpClient.close();
			} catch (Exception e) {
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		HttpClientUtil.createGetHttpByPath("http://8.134.53.160:5000/test/");
	}

	@Test
	public void test2(){

	}

	@Test
	public void sendHtmlMailThymeleaf(){
		System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		Context ctx = new Context();
		//查出任务信息
		ctx.setVariable("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		String mail = templateEngine.process("scanresult.html", ctx);
		sendHtmlMail.sendHtmlMails("1192129669@qq.com",
				"1192129669@qq.com",
				"测试邮件主题（Thymeleaf）",
				mail);
	}

	@Test
	public void sendMailHtml(){
		String to="1192129669@qq.com";
		String subject="主题:HTML的邮件 ";
		String content="<h1>这是一个HTML类型的邮件</h1>";
		sendHtmlMail.sendHtmlMail(to,subject,content);
	}

	@Test
	public void testRedis() {
		stringRedisTemplate.opsForValue().set("host:port","8.134.53.160:8080",1, TimeUnit.MINUTES);
//		System.out.println(stringRedisTemplate.opsForValue().get("host:port"));
	}

	@Test
	public void testMail() {
		MailTemplate template = new MailTemplate("1192129669@qq.com","1192129669@qq.com","CodeHunter通知","欢迎使用CodeHunter！");
		iSendMailService.SendMail(template);
	}

	@Test
	public void TestGitlab(){
		//获取所有用户的所有仓库
		String s = HttpClientUtil.createGetHttpByPath("http://8.134.53.160:8081/api/v4/projects/1/members?private_token=1sRC1BZfxk7V2ZizkGJe&per_page=10");
		System.out.println(s);
	}

	@Test
	public void getUserProjects(){
		String private_token = "1sRC1BZfxk7V2ZizkGJe";
		String host = "8.134.53.160:8081";
		System.out.println(iGitlabService.getUserProjects(host,private_token));
	}

	@Test
	public void getFileTree(){
		String path = "root%2Fcodehunter";
		String private_token = "1sRC1BZfxk7V2ZizkGJe";
		String host = "8.134.53.160:8081";
		iGitlabService.getFileTree(host,path,"master",private_token);
	}

	@Test
	public void getFileContext(){
		String host = "8.134.53.160:8081";
		String repository_path = "root%2Fcodehunter";
		String file_path = "file%2FRandomFile.txt";
		String private_token="1sRC1BZfxk7V2ZizkGJe";
		String ref = "master";
		System.out.println(iGitlabService.getFileContext(host,repository_path,file_path,ref,private_token));
	}

	@Test
	public void getRepoBranchs(){
		String host = "8.134.53.160:8081";
		String repository_path = "root%2Fcodehunter";
		String id = "1";
		String file_path = "file%2FRandomFile.txt";
		String private_token="oQyrkG87R7ufL5ixRtpz";
		String ref = "master";
		String str = iGitlabService.getRepoBranchs(host,"1",private_token);
		JSONArray strjson = JSON.parseArray(str);

//		System.out.println(iGitlabService.getRepoBranchs(host,id,private_token));
	}

	@Test
	public void testDet(){

		JSONArray output = new JSONArray();

		//1.直接使用全部规则的方法{现在只放了3个规约}
//        output.addAll(DetectionRun.runByString(javaCode));
//        output.clear();


		JSONObject jsonObject=new JSONObject();
		JSONObject out=new JSONObject();
		jsonObject.put("1","严重");
		jsonObject.put("2","警告");
		jsonObject.put("3","建议");
		out.put("ruleid",jsonObject);
		out.put("code",javaCode);

		//输出
//		output.addAll(DetectionRun.runByArrary(javaCode,out));
//		out.put("result",output);
//		System.out.println(out);
	}

	//代码转化为字符(测试代码)
	public static final String javaCode = "/**\n" +
			" * @Author: 黄美端\n" +
			" * @CreateTime: 2022-11-10  15:41\n" +
			" * @Description: TODO\n" +
			" */\n" +
			"\n" +
			"package test.naming;\n" +
			"\n" +
			"public class LongName {\n" +
			"    static int jhjskfhjksdhjkfhjdjakdkfbd=1;\n" +
			"    String jsahgfdsghgashgfjhgfcbvcx=\"23\";\n" +
			"    int say(){\n" +
			"        int a=1;\n" +
			"        int b=6;\n" +
			"        String FFFFFFFFFFFFFFFFFFFFFFFFF=\"sdhf\";\n" +
			"        String F=\"sfd\";\n" +
			"        return 0;\n" +
			"    }\n" +
			"    int main(){\n" +
			"        int aaaaaaaaaaaaaaaaaaaaaaaaa=1;\n" +
			"        int dddddd=12;\n" +
			"        Object asdsdsssssssssssssssssssssssss=new LongName();\n" +
			"        return 0;\n" +
			"    }\n" +
			"    class Constance{\n" +
			"        int constancenameshouldbeupper=1;\n" +
			"        int constance=2;\n" +
			"    }\n" +
			"}\n//1.类名“foo”不符合规则：'[A-Z][a-zA-Z0-9]*'\n" +
			"@covfefe\n" +
			"public class $foo {\n" +
			"\n" +
			"    @interface covfefe {\n" +
			"    }\n" +
			"\n" +
			"\n" +
			"}\n" +
			"//2.实用程序类名“Foo”不匹配规则： '[A-Z][a-zA-Z0-9]+(Utils?|Helper|Constants)\n" +
			"public class Foo {\n" +
			"\n" +
			"    static final int ZERO = 0;\n" +
			"\n" +
			"    static int bar() {\n" +
			"        return bar();\n" +
			"    }\n" +
			"\n" +
			"    static class BarUtil {\n" +
			"\n" +
			"        static final int ONE = 1;\n" +
			"\n" +
			"    }\n" +
			"}\n" +
			"//3.类名“foo”不匹配规则：'[A-Z][a-zA-Z0-9]*'\n" +
			"@covfefe\n" +
			"public class foo {\n" +
			"\n" +
			"    @interface covfefe {\n" +
			"    }\n" +
			"\n" +
			"}";



}

