package com.example.tests;

import com.thoughtworks.selenium.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.regex.Pattern;

public class Test_03_W0100_Search_for_a_data_set extends SeleneseTestCase {
	@Before
	public void setUp() throws Exception {
		selenium = new DefaultSelenium("localhost", 4444, "*chrome", "https://buildbot.oceanobservatories.org:8080/");
		selenium.start();
	}

	@Test
	public void test_03_W0100_Search_for_a_data_set() throws Exception {
		selenium.open("/ooici-pres-0.1/");
		selenium.click("login_button");
		selenium.waitForPageToLoad("60000");
		selenium.click("wayflogonbutton");
		selenium.waitForPageToLoad("60000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Cilogon.org is asking for some information from")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("Email", "U_S_E_R-N_A_M_E");
		selenium.type("Passwd", "P_A_S_S-W_O_R_D");
		selenium.click("signIn");
		selenium.waitForPageToLoad("60000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Showing 1 to 8 of 8 entries")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("radioBoundingDefined");
		selenium.type("ge_bb_north", "21.4");
		selenium.type("ge_bb_south", "21.3");
		selenium.type("ge_bb_east", "157.6");
		selenium.type("ge_bb_west", "157.9");
		//selenium.click("apply_filter_button");  // This is the one to lose
		selenium.click("radioAllPubRes");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Showing 1 to 2 of 2 entries")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("//tr[@id='3319A67F-81F3-424F-8E69-4F28C4E04806']/td[2]");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("7723 Moanalua RG No 1 at alt 1000 ft")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("radioBoundingAll");
		//selenium.click("apply_filter_button");  // This is the one to lose
		selenium.click("radioAllPubRes");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Showing 1 to 8 of 8 entries")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}
                
                //selenium.type("//input[@type='text']", "HYCOM");
		selenium.typeKeys("//input[@type='text']", "HYCOM");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Showing 1 to 2 of 2 entries")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("//tr[@id='3319A67F-81F3-424F-8E69-4F28C4E04800']/td[3]");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Data HyCom")) break; } catch (Exception e) {}
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
