#!/usr/bin/python
# coding=utf-8
import smtplib
from email.mime.text import MIMEText
from threading import Thread


class Email:
    def __init__(self, notify_email):
        self.__send_content = None
        self.__send_subject = None
        self.__notify_email = notify_email
        self.__msg_from = "systemNotify@126.com"
        self.__send_password = "Ytj2018"

    def task(self):
        with smtplib.SMTP_SSL("smtp.126.com", 465) as email:
            email.login(self.__msg_from, self.__send_password)
            msg = MIMEText(self.__send_content)
            msg['Subject'] = self.__send_subject
            msg['From'] = self.__msg_from
            msg['To'] = self.__notify_email
            email.sendmail(self.__msg_from, self.__notify_email, msg.as_string())

    def send_msg(self, send_subject, send_content):
        self.__send_subject = send_subject
        self.__send_content = send_content
        Thread(target=self.task()).start()


