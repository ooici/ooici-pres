package com.example.tests;

import com.thoughtworks.selenium.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.regex.Pattern;

public class Test_08_Guest_View_Dataset extends SeleneseTestCase {
	@Before
	public void setUp() throws Exception {
		selenium = new DefaultSelenium("localhost", 4444, "*chrome", "https://buildbot.oceanobservatories.org:9443/");
		selenium.start();
	}

	@Test
	public void test_08_Guest_View_Dataset() throws Exception {
		selenium.open("/ooici-pres-0.1/");
		selenium.click("guest_button");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Showing 1 to 8 of 8 entries")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("radioAllPubRes");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Showing 1 to 8 of 8 entries")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("radioAllPubRes");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Showing 1 to 8 of 8 entries")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("//tr[@id='3319A67F-81F3-424F-8E69-4F28C4E04806']/td[1]");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("7723 Moanalua RG No 1 at alt 1000 ft")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

	}

	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}
}
