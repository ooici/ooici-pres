package com.example.tests;

import com.thoughtworks.selenium.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.regex.Pattern;

public class Test_04_W0101_Examine_a_data_set extends SeleneseTestCase {
	@Before
	public void setUp() throws Exception {
		selenium = new DefaultSelenium("localhost", 4444, "*chrome", "https://buildbot.oceanobservatories.org:8080/");
		selenium.start();
	}

	@Test
	public void test_04_W0101_Examine_a_data_set() throws Exception {
		selenium.open("/ooici-pres-0.1/");
		selenium.click("login_button");
		selenium.waitForPageToLoad("60000");
		selenium.click("wayflogonbutton");
		selenium.waitForPageToLoad("60000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Cilogon.org is asking for some informa")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("Email", "U_S_E_R-N_A_M_E");
		selenium.type("Passwd", "P_A_S_S-W_O_R_D");
		selenium.uncheck("PersistentCookie");
		selenium.click("signIn");
		selenium.waitForPageToLoad("60000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Showing 1 to 8 of 8 entries")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("//tr[@id='3319A67F-81F3-424F-8E69-4F28C4E047F1']/td[2]");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Title: NDBC Sensor Observation Service data")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

                selenium.click("logout_link");
                selenium.waitForPageToLoad("60000");
                selenium.open("http://google.com");
                selenium.click("gbgs4");
                selenium.click("gb_71");
                selenium.waitForPageToLoad("60000");

	}

	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}
}
