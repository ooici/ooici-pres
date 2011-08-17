package com.example.tests;

import com.thoughtworks.selenium.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.regex.Pattern;

public class Test_2_W0108_Log_in4 extends SeleneseTestCase {
	@Before
	public void setUp() throws Exception {
		selenium = new DefaultSelenium("localhost", 4444, "*chrome", "https://buildbot.oceanobservatories.org:8080/");
		selenium.start();
	}

	@Test
	public void test_2_W0108_Log_in4() throws Exception {
		selenium.open("/ooici-pres-0.1/");
		selenium.click("//a/div");
		selenium.waitForPageToLoad("30000");
		selenium.click("wayflogonbutton");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Cilogon.org is asking for some information from your Google Account.")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

                selenium.type("Email", "U_S_E_R-N_A_M_E_1");
                selenium.type("Passwd", "P_A_S_S-W_O_R_D_1");
                selenium.uncheck("PersistentCookie");
		selenium.click("signIn");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Showing 1 to 8 of 8 entries")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}
                // LOGOUT OF EVERYTHING!!!!
                selenium.open("/ooici-pres-0.1/dashboard");
                selenium.click("logout_link");
                selenium.waitForPageToLoad("30000");
                selenium.open("http://www.google.com/");
                selenium.click("gbgs4");
                selenium.click("gb_71");
                selenium.waitForPageToLoad("30000");


	}

	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}
}
