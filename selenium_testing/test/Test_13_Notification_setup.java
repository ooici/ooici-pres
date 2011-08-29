package com.example.tests;

import com.thoughtworks.selenium.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.regex.Pattern;

public class Test_13_Notification_setup2 extends SeleneseTestCase {
	@Before
	public void setUp() throws Exception {
		selenium = new DefaultSelenium("localhost", 4444, "*chrome", "https://buildbot.oceanobservatories.org:9443/");
		selenium.start();
	}

	@Test
	public void test_13_Notification_setup2() throws Exception {
		selenium.open("/ooici-pres-0.1/index.html");
		selenium.click("login_button");
		selenium.waitForPageToLoad("30000");
		selenium.click("wayflogonbutton");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Cilogon.org is asking for some information from your Google Account. To see and approve the request, sign in.")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("Email", "U_S_E_R-N_A_M_E");
		selenium.type("Passwd", "P_A_S_S-W_O_R_D");
		selenium.click("signIn");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Showing 1 to 8 of 8 entries")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("//tr[@id='3319A67F-81F3-424F-8E69-4F28C4E04806']/td[1]");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Title: Moana Loa Data Source")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("setup_notifications");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Send notifications when:")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("updateWhenAvailable");
		selenium.click("datasourceIsOffline");
		Thread.sleep(1000);
		selenium.click("start_notifications");
		selenium.click("radioAllPubRes");
		assertEquals("Notification saved", selenium.getAlert());
		Thread.sleep(1000);
		selenium.click("radioMySub");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("7723 Moanalua RG No 1 at alt 1000 ft")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.check("//input[@type='checkbox']");
		selenium.click("delete_selected");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Showing 0 to 0 of 0 entries")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

	}

	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}
}
