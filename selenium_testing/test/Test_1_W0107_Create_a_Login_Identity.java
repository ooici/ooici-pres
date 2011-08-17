package com.example.tests;

import com.thoughtworks.selenium.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.regex.Pattern;

public class Test_1_W0107_Create_a_Login_Identity extends SeleneseTestCase {
	@Before
	public void setUp() throws Exception {
		selenium = new DefaultSelenium("localhost", 4444, "*chrome", "https://buildbot.oceanobservatories.org:8080/");
		selenium.start();
	}

	@Test
	public void test_1_W0107_Create_a_Login_Identity() throws Exception {
		selenium.open("/ooici-pres-0.1/");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Funding for the Ocean")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("//a/div");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Remember this selection:")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("wayflogonbutton");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Can't access your account?")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

                selenium.type("Email", "U_S_E_R-N_A_M_E");
                selenium.type("Passwd", "P_A_S_S-W_O_R_D");
		selenium.uncheck("PersistentCookie");
		selenium.click("signIn");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("You can change your optional selections at any time by")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}



                Thread.sleep(1000);
                Thread.sleep(1000);
                Thread.sleep(1000);
                selenium.type("account_name", "A_C_C_O_U_N_T-N_A_M_E");
                selenium.typeKeys("account_name", "x");
                Thread.sleep(1000);
                selenium.type("account_institution", "OOICI");
                selenium.typeKeys("account_institution", "x");
                Thread.sleep(1000);
                selenium.type("account_email", "U_S_E_R-N_A_M_E");
                selenium.typeKeys("account_email", "x");
                Thread.sleep(1000);

		selenium.click("account_settings_done");
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
