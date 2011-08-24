package com.example.tests;

import com.thoughtworks.selenium.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.regex.Pattern;

public class Test_05_Temporal_Extent_Filter extends SeleneseTestCase {
	@Before
	public void setUp() throws Exception {
		selenium = new DefaultSelenium("localhost", 4444, "*chrome", "https://buildbot.oceanobservatories.org:8080/");
		selenium.start();
	}

	@Test
	public void test_05_Temporal_Extent_Filter() throws Exception {
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
		selenium.type("id=Email", "U_S_E_R-N_A_M_E_1");
		selenium.type("id=Passwd", "P_A_S_S-W_O_R_D_1");
		selenium.click("id=signIn");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Showing 1 to 8 of 8 entries")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("id=te_from_input");
		selenium.click("id=TE_timeRange_defined");
		selenium.type("id=te_from_input", "2011-05-10T00:00:00Z");
		selenium.type("id=te_to_input", "2011-05-11T00:00:00Z");
		selenium.click("id=apply_filter_button");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Showing 1 to 4 of 4 entries")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

	}

	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}
}
