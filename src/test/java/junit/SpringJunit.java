package junit;

import com.wjyup.coolq.MainApp;
import com.wjyup.coolq.entity.RequestData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MainApp.class})
//相当于  --spring.profiles.active=dev
@ActiveProfiles(value="dev")
public class SpringJunit {
	private final Logger log = LogManager.getLogger("SpringJunit");
	private RequestData requestData = new RequestData();
//	@Resource
//	private FundService fundService;
	
	@Before
	public void inital(){
		System.out.println("============inital============");
		// 接收测试消息的QQ
		requestData.setQQ(1066231345L);
		requestData.setType(1);
		requestData.setSubType(1);
	}

	@Test
	public void test() throws Exception {
		// 华夏大中华混合(QDII)[002230]
		requestData.setMsg("基金 002230");
//		fundService.doit(requestData);
		requestData.setMsg("jj 002230");
//		fundService.doit(requestData);
	}

	public void log(Object o) {
		System.out.println("<========================");
		System.out.println(o);
		System.out.println("========================>");
	}
}
