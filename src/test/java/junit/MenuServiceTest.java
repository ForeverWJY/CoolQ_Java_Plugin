package junit;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wjyup.coolq.util.service.IMenuService;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class MenuServiceTest {
	@Resource
	private IMenuService menuService;

	/**
	 * oschina测试
	 */
	@Test
	public void getOschinaNewsTest() {
		menuService.getOschinaNews();
	}
	
}
