

import com.thoughtworks.selenium.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.regex.Pattern;

public class Test_000A_FULL_LOGOUT_ERROR_OK extends SeleneseTestCase {
	@Before
	public void setUp() throws Exception {
		selenium = new DefaultSelenium("sg-hub.oceanobservatories.org", 4444, "IE on Windows7", "https://buildbot.oceanobservatories.org:9443/");
		selenium.start();
	}

	@Test
	public void test_000A_FULL_LOGOUT_ERROR_OK() throws Exception {
		selenium.open("/ooici-pres-0.1/dashboard");
                for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Showing 1 to")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("id=logout_link");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Welcome to Release 1 of the Integrated")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}
	}

	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}
}
