# log-sender
Mail all the visited URLs from a computer by using apache2 (HTTPD) as forward proxy.

Let's have a look at instructions to use this project. Initially this project is developed for linux only. I will develope this for windows as well.

# Install apache2
**sudo apt update**<br/>
**sudo apt install apache2**

# Configure Apache as Forward Proxy Server
Enable the **proxy, proxy_http, and proxy_connect** modules. We can do that using the a2enmod command.

**cd /etc/apache2/mods-available/**<br/>
**sudo a2enmod proxy proxy\_http proxy\_connect**

# Uncomment ProxyRequest & <Proxy *> Block in proxy.conf
Go to the **/etc/apache2/mods-enabled** directory and open the file **proxy.conf** in a text editor. Uncomment the '#ProxyRequests On' line and the '<Proxy *>' block.

# Add Custom Log Format
Go to /etc/apache2<br/>
Open apache2.conf<br/>
search for LogFormat<br/>
Add following line after all pre defined log formats :<br/>
**LogFormat "%t \\"%r\\" %>s %O" my_format**

# Pipe Logs to File
Go to /etc/apache2/sites-enabled<br/>
Open 000-default.conf<br/>
Comment CustomLog ${APACHE\_LOG\_DIR}/access.log combined<br/>
Add following line:<br/>
**CustomLog "|/usr/bin/rotatelogs ${APACHE_LOG_DIR}/access.log-%Y.%m.%d 86400" my_format**

# Restart Apache
sudo service apache2 reload

# Add Proxy to Linux
Go to Network in System Settings<br/>
Select Network Proxy<br/>
Select proxy method manual<br/>
Set host as localhost and port as 80<br/>
Finally click on "Apply system wide" Button

# References
For more information, please visit following pages:

[configure-apache2-as-a-forward-proxy](https://geek-university.com/apache/configure-apache-as-a-forward-proxy/)<br/>
[rotate-logs-apache2](https://httpd.apache.org/docs/2.4/logs.html)

# What Next???
Clone this repository<br/>
Open **me.deepak.spy.email.EmailSender** Class<br/>
Change USER, PASSWORD, SENDER\_EMAIL, RECEIVER\_EMAIL<br/>
Build the project<br/>
Export this as runnable jar<br/>
Run exported runnable jar as cron job :<br/>
crontab -e<br/>
Add an entry as following :<br/>
0 12 * * * java -jar Desktop/log-sender.jar<br/>
This enables log-sender.jar which is at Desktop to run everyday 12 PM

# Algorithm
1. Read log folder of apache2 (eg. for linux /var/log/apache2/) files startsWith("access.log")
2. Exclude current day file
3. Zip all files
4. Send mail
5. if successfully sent mail then delete sent files
