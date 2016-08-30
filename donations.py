#!/usr/bin/python

import sys
import traceback
import time
import os 
from pyvirtualdisplay import Display
from selenium import webdriver


try:
  USER = str(sys.argv[1])
  PASS = str(sys.argv[2])

  TEST = 0

  print "[donations]: Updating Sponsors!"

  display = Display(visible=0, size=(1280, 800))
  display.start()

  profile = webdriver.FirefoxProfile()
  profile.set_preference('browser.download.folderList', 2)
  profile.set_preference('browser.download.manager.showWhenStarting', False)
  profile.set_preference('browser.download.dir', ('/home/meticulus/Android'))
  profile.set_preference('browser.helperApps.neverAsk.saveToDisk', ('application/x-csv'))

  browser = webdriver.Firefox(profile)
  print "[donations]: Navigating to PayPal"
  browser.get('https://www.paypal.com/signin/?country.x=US&locale.x=en_US')
  browser.implicitly_wait(10)

  print "[donations]: Logging in"
  if 1 == 1:
     unamebox = browser.find_element_by_id("email")
     unamebox.clear()
     unamebox.send_keys(USER)

     pwdbox = browser.find_element_by_id("password")
     pwdbox.clear()
     pwdbox.send_keys(PASS + "\n")
     browser.implicitly_wait(20)
     regbutton = browser.find_element_by_link_text("Log Out")

     if regbutton != None:
       print "[donations]: Login Success"

       browser.get('https://history.paypal.com/us/cgi-bin/webscr?cmd=_history-download');
       browser.implicitly_wait(10)

       mm = browser.find_element_by_name("from_a")
       mm.clear()
       mm.send_keys("01")

       dd = browser.find_element_by_name("from_b")
       dd.clear()
       dd.send_keys("01")

       yyyy = browser.find_element_by_name("from_c")
       yyyy.clear()
       yyyy.send_keys("2014")

       filetype =  browser.find_element_by_name("custom_file_type")
       browser.execute_script("arguments[0].value = arguments[1]", filetype, "comma_completed")

       submit = browser.find_element_by_name("submit.x")
       submit.click() 
       time.sleep(30)
       
     else:
       print "[donations]: Login Failed!"

  print "[donations]: Success!"
  browser.quit()
  display.stop()

except Exception, e:
  print >> sys.stderr, traceback.format_exc()
  browser.quit()
  display.stop()
  sys.exit(9)

