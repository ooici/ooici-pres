package com.example.tests;

import com.thoughtworks.selenium.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.regex.Pattern;

public class Test_000B_FULL_LOGOUT_ERROR_OK extends SeleneseTestCase {
	@Before
	public void setUp() throws Exception {
		selenium = new DefaultSelenium("localhost", 4444, "*chrome", "http://www.google.com/");
		selenium.start();
	}

	@Test
	public void test_000B_FULL_LOGOUT_ERROR_OK() throws Exception {
		selenium.open("http://www.google.com/");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Change background image")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("gbgs4");
		selenium.click("gb_71");
		selenium.waitForPageToLoad("30000");
	}

	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}
}
