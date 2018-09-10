package junit;

import com.wjyup.coolq.MainApp;
import com.wjyup.coolq.service.impl.MenuService;
import com.wjyup.coolq.util.ConfigCache;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MainApp.class})
//相当于  --spring.profiles.active=dev
@ActiveProfiles(value="dev")
public class SpringJunit {
	private final Logger log = LogManager.getLogger("SpringJunit");
	@Resource
	private MenuService menuService;
	
	@Before
	public void inital(){
		System.out.println("============inital============");
	}

	@Test
	public void test() {
		// 华夏大中华混合(QDII)[002230]
		String fundGP = menuService.getFundGP("002230");
		log(fundGP);
	}

	public void log(Object o) {
		System.out.println("<========================");
		System.out.println(o);
		System.out.println("========================>");
	}
}
