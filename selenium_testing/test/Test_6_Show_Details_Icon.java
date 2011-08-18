package com.example.tests;

import com.thoughtworks.selenium.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.regex.Pattern;

public class Test_6_Show_Details_Icon extends SeleneseTestCase {
	@Before
	public void setUp() throws Exception {
		selenium = new DefaultSelenium("localhost", 4444, "*chrome", "https://buildbot.oceanobservatories.org:8080/");
		selenium.start();
	}

	@Test
	public void test_6_Show_Details_Icon() throws Exception {
		selenium.open("/ooici-pres-0.1/index.html");
		selenium.click("id=login_button");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Select An Identity Provider:")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("id=wayflogonbutton");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Cilogon.org is asking for some information from your Google Account. To see and approve the request, sign in.")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.uncheck("id=PersistentCookie");
		selenium.type("id=Email", "OOICI.Test@gmail.com");
		selenium.type("id=Passwd", "RubberDuck4");
		selenium.click("id=signIn");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Showing 1 to 8 of 8 entries")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("css=img.dataset_details");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Moana Loa Data Source")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Title: Moana Loa Data Source")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		verifyTrue(selenium.isTextPresent("Oahu HI (212359157502601) - Instantaneous"));
		verifyTrue(selenium.isTextPresent("Units = seconds since 1970-01-01 00:00:00"));
		selenium.click("css=#dataset_return_button > img");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Showing 1 to 8 of 8 entries")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		verifyTrue(selenium.isTextPresent("Variables"));
		selenium.click("//tr[@id='3319A67F-81F3-424F-8E69-4F28C4E04805']/td[6]/img");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Metadata for WHOTS 7 near-real-time Mooring Data, System 2")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("22.75")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		verifyTrue(selenium.isTextPresent("comment = surface data; nominal depth == 0"));
		verifyTrue(selenium.isTextPresent("history = 20110518T112117 Argos data processed. N.Galbraith"));
		verifyTrue(selenium.isTextPresent("2011-05-01T00:00:00Z - 2011-05-15T00:00:00Z"));
		selenium.click("css=#dataset_return_button > img");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Showing 1 to 8 of 8 entries")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("id=logout_link");
		selenium.waitForPageToLoad("30000");
		selenium.open("http://google.com");
		selenium.click("css=#gbi4 > span.gbma");
		selenium.click("id=gb_71");
		selenium.waitForPageToLoad("30000");
	}

	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}
}
