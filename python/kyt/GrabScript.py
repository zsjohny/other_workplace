# -*- coding: utf-8 -*-
import unittest

from selenium import webdriver
from selenium.common.exceptions import NoAlertPresentException
from selenium.common.exceptions import NoSuchElementException


class AppDynamicsJob(unittest.TestCase):
    def setUp(self):
        # AppDynamics will automatically override this web driver
        # as documented in https://docs.appdynamics.com/display/PRO44/Write+Your+First+Script
        self.driver = webdriver.Chrome()
        self.driver.implicitly_wait(30)
        self.base_url = "https://www.katalon.com/"
        self.verificationErrors = []
        self.accept_next_alert = True

    def test_app_dynamics_job(self):
        driver = self.driver
        driver.get(
            "https/"
            ""
            "\://www.alimama.com/member/login.htm?forward=http%3A%2F%2Fpub.alimama.com%2Fmanage%2Foverview%2Findex.htm%3Fspm%3Da219t.7900221%2F1.1998910419.dbb742793.2a8f75a5EBr3b3")
        # ERROR: Caught exception [ERROR: Unsupported command [selectFrame | index=0 | ]]
        driver.find_element_by_id("TPL_username_1").click()
        driver.find_element_by_id("TPL_username_1").clear()
        driver.find_element_by_id("TPL_username_1").send_keys("容易gou")
        driver.find_element_by_id("TPL_password_1").clear()
        driver.find_element_by_id("TPL_password_1").send_keys("xiongyanhao888")
        driver.find_element_by_id("TPL_password_1").click()
        driver.find_element_by_id("J_SubmitStatic").click()
        # ERROR: Caught exception [ERROR: Unsupported command [selectFrame | relative=parent | ]]
        driver.find_element_by_xpath(
            u"(.//*[normalize-space(text()) and normalize-space(.)='效果报表'])[2]/following::i[1]").click()
        driver.find_element_by_xpath(
            u"(.//*[normalize-space(text()) and normalize-space(.)='效果报表'])[2]/following::span[1]").click()
        # ERROR: Caught exception [ERROR: Unsupported command [selectWindow | win_ser_1 | ]]
        driver.find_element_by_xpath(
            u"(.//*[normalize-space(text()) and normalize-space(.)='点此查看'])[1]/following::i[1]").click()

    def is_element_present(self, how, what):
        try:
            self.driver.find_element(by=how, value=what)
        except NoSuchElementException as e:
            return False
        return True

    def is_alert_present(self):
        try:
            self.driver.switch_to_alert()
        except NoAlertPresentException as e:
            return False
        return True

    def close_alert_and_get_its_text(self):
        try:
            alert = self.driver.switch_to_alert()
            alert_text = alert.text
            if self.accept_next_alert:
                alert.accept()
            else:
                alert.dismiss()
            return alert_text
        finally:
            self.accept_next_alert = True

    def tearDown(self):
        # To know more about the difference between verify and assert,
        # visit https://www.seleniumhq.org/docs/06_test_design_considerations.jsp#validating-results
        self.assertEqual([], self.verificationErrors)


if __name__ == "__main__":
    unittest.main()
