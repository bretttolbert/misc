Find and replace
```find source/DDL -type f \( -name "*.cpp" -or -name "*.hpp" \) -print0 | xargs -0 sed -i -e 's/[^o]stringstream/ostringstream/g'```

https://linux.die.net/man/7/audit.rules

```ln -s /path/to/file /path/to/symlink```

Search installed packages:
```dpkg -l | grep <pkg>```

Remove installed package:
```sudo dpkg -r <pkg>```

See files installed by a package:
```dpkg-query -L <pkg>```

Install java:
```sudo apt-get install default-jre icedtea-plugin```


X Do Tool
```sudo apt-get install xdotool xbase-clients```
```xdotool key ctrl+shift+t```

https://en.wikipedia.org/wiki/Lsof
https://en.wikipedia.org/wiki/Pipeline_(Unix)

Customize terminal:
Shorten your bash terminal prompt
```export PS1='\u@\h$ '```

Alternatively:
```export PS1="\u@\h \w> "```

Really shorten your bash terminal prompt
```export PS1='$ '```


Set terminal tab background color:

DARKRED:
```echo -ne '\e]11;#560505\a'```


Search robot framework files
```
grep --include=\*.{txt,robot} -rs "Get " .
```


Install Java JDK
```
sudo add-apt-repository ppa:openjdk-r/ppa
sudo apt-get update
sudo apt-get install openjdk-8-jdk
sudo update-alternatives --config java
sudo update-alternatives --config javac
```

See which process is using a port
```lsof -i :5000```

Nmap scan of local network
```nmap -sP 192.168.1.0/24```

Nmap TCP SYN scan of host
```nmap -sS 10.6.199.5```


Change to the parent directory
```cd ..```

Change to the previous directory
```cd -```

List the 10 most recently entered commands
```history 10```

Search command history
```history | grep <pattern>```

Open GUI File Browser at the current directory using the user's default application
```xdg-open .```

Configure firewall
```
iptables -D INPUT 6
iptables --list
service iptables save
iptables-save > /etc/sysconfig/iptables
iptables -A INPUT -j DROP
iptables -A OUTPUT -j DROP
iptables -A FORWARD -j DROP
iptables -I INPUT 6 -p tcp --dport 443 -j ACCEPT
service iptables save
```

Search for packages to install (RHEL)
```yum list <package-name>```
Note: If you don't know the exact name, use wildcards e.g. yum list *gtk*

Install a package (RHEL)
```su -c 'yum -y install <package-name>' ```

Search for packages to install (Debian)
```apt-cache search <package-name>```
Note: You don't need to use wildcards, it will list partial matches.

Search for packages with names beginning with "tcl"
```apt-cache search ^tcl```

Install a package (Debian)
```sudo apt-get install <package-name>```

Add a repository (Debian)
```
sudo vim /etc/apt/sources.list
sudo apt-get update
```

Add a PPA (Ubuntu)
```sudo add-apt-repository ppa:videolan/stable-daily```

Install apt-file
```sudo apt-get install apt-file```

Search all packages for filename
```apt-file search filename```

Remove and purge packages matching wildcard
```sudo apt-get remove --purge vlc*```

Once I got an error installing VLC. I removed the Universe repository, ran apt-get update, and then I was able to install VLC successfully.

Translate characters
```echo 'I LovE linuX. one is better Than 2' | tr "a-z" "A-Z"```

```top```
Shows memory usage of a proess.

```
/
  etc
    crontab    
    inittab
    fstab
    hosts    
    rc.local
    resolv.conf
    init.d
      iptables
    sysconfig
      iptables-config
    network
      interfaces
      network-scripts
        ifcfg-eth0
        route-eth0
      wpa-supplicant
    httpd
      conf
      conf.d
  var
    www
      html
```

Display interface status
```ifconfig -a```

grep with 2 lines of trailing context
```
pi@raspberrypi ~ $ ifconfig | grep -A 2 "eth0"
eth0      Link encap:Ethernet  HWaddr b8:27:eb:ef:75:88
          inet addr:10.17.127.200  Bcast:10.17.127.255  Mask:255.255.255.0
          UP BROADCAST RUNNING MULTICAST  MTU:1500  Metric:1
pi@raspberrypi ~ $
```

```
$ grep -r --color='auto' -P -n "[\x81]" .
```


Display the first two lines of a file:
Method 1:
```
pi@raspberrypi ~ $ head -n 2 wxtest.py
#!/usr/bin/env pythonf
import wx
pi@raspberrypi ~ $
```

Method 2:
```
pi@raspberrypi ~ $ cat wxtest.py | sed -n '1,2p'
#!/usr/bin/env python
import wx
pi@raspberrypi ~ $
```

sed flags:
```
-r enable extended regular expressions (requires less escaping)
-n quiet mode (implicit printing is disabled)
-e expression
-i edit files in place
```

sed commands
```
s search and replace
p print
```

```
grep [OPTIONS] PATTERN [FILE...]
Options:
-r recursive
-n print line numbers
-i ignore case
-I ignore binary files
-s suppress errors (e.g. "file not found")
-o print only the matching portion
-A <num> print num lines of trailing context
-B <num> print num lines of leading context
-C <num> print num lines of output context
```

Recursively search .cpp and .h files for "define" (case insensitive)
```grep -riI --include="*.{cpp,h}" define .```

Display the last line of a file:
```
pi@raspberrypi ~ $ tail -n 1 /var/log/dmesg
[   23.748486] bcm2835-cpufreq: switching to governor ondemand
pi@raspberrypi ~ $
```

Use WiringPi to display status of GPIO 0
```
pi@raspberrypi ~ $ gpio readall | grep "GPIO 0"
|      0   |  17  |  11  | GPIO 0 | IN   | Low   |
pi@raspberrypi ~ $
```

Display the value ("High" or "Low") of GPIO 0
```
pi@raspberrypi ~ $ gpio readall | grep "GPIO 0" | sed -e 's/\s*|\s*/\n/g' | sed -n '7p'
Low
pi@raspberrypi ~ $
```

Display IP address of eth0
```
ifconfig | grep -A 2 "eth0" | sed -n '2p' | grep -o '[0-9][0-9]*\.[0-9][0-9]*\.[0-9][0-9]*\.[0-9][0-9]*' | sed -n '1p'
```

Convert tabs to spaces
```expand -t 4 src-tabs.py > src-spaces.py```

Another way
```sed -i 's/\t/    /g' <file>```

Remove blank lines
```sed '/^\s*$/d' main.cpp > main2.cpp```

Combine stderr (2) and stdout (1) into the stdout stream for further manipulation
```g++ lots_of_errors.cpp 2>&1 | head```

```>&``` is the syntax to redirect a stream to another file descriptor.
0 is stdin. 1 is stdout. 2 is stderr.

Append (send in addition)
```echo "some text" >> file.txt```

Replace (send to as a whole completed file)
```echo "some text" > file.txt```

Trim leading and trailing whitespace
```echo " test test " | sed -e 's/^ *//g' -e 's/ *$//g'```

Remove all whitespace
```echo " test test " | tr -d ' '```

If you're launching a GUI program from an SSH terminal, you may need to run the following command first:
```export DISPLAY=:0 ```


```curl http://www.bretttolbert.com/downloads/map.min.json | python -m simplejson.tool```


Extract a tar.gz achive
```tar xvzf archive.tar.gz```

Extract a tar.bz2 achive
```tar xvjf archive.tar.bz2```

tar flags
```
x extract
v verbose
z bunzip (bz2)
g gunzip (gz)
f file
```

Create a virtual python environment with specified interpreter
```virtualenv -p /usr/bin/python2.6 <path/to/new/virtualenv/>```

Turn off system bell
```xset b off```

Stop/Start/Restart networking (RHEL)
```
sudo service network stop
sudo service network start
sudo service network restart
```

Stop/Start/Restart networking (Debian)
```
sudo service networking stop
sudo service networking start
sudo service networking restart
```


List status of all services (ubuntu):
```services --status-all```

Edit network configuration (Debian)
```sudo vim /etc/network/interfaces```

Find files matching a pattern and delete them (note the use of xargs)
```find -type f | grep "RcObject.*\.c" | xargs rm -f```

Find - redirect error output to /dev/null
```find / -name "openssl.cnf" 2>/dev/null```

Open gedit in the background without it spewing debug into the terminal
```gedit PCAP.py > /dev/null 2>&1 &```

Force apt-get to prefer IPv4 - append the following to /etc/gai.conf:
```precedence ::ffff:0:0/96  100```
http://unix.stackexchange.com/questions/9940/convince-apt-get-not-to-use-ipv6-method
gai.conf is the getaddrinfo configuration file
http://linux.die.net/man/5/gai.conf

Install a Debian package directly
```sudo dpkg -i package.deb```

Open Raspberry Pi configuration menu
```sudo raspi-config```

Modify Raspberry Pi configuration file
```sudo vim /boot/config.txt```

Edit bash configuration file
```vim ~/.bashrc```

Edit vim configuration file
```vim ~/.vimrc```

Edit xserver configuration file
```vim ~/.xinitrc```

Edit SSH server configuration file
```sudo vim /etc/ssh/sshd_config```

SSH with verbose output for troubleshooting
```ssh -v user@host```

Add user to group
```usermod -a -G group user```

Remove user from group
```gpasswd -d user group```

View syslog
```tail -f /var/log/syslog```

View auth log
```tail -f /var/log/auth.log```

Play a video (Raspberry Pi)
```omxplayer myvideo.mp4```

View an image (Raspberry Pi)
```gpicview myimage.jpg```

Shutdown a linux computer immediately
```sudo shutdown now```

List files sorted by the 3rd column (owner)
```ls -l | sort -k 2```

List files sorted by the 4th column (size)
```ls -l | sort -k 5 -n```

List files with classifier (* after executable files)
```ls -F```

List all the groups that a user belongs to
```groups <username>```

Determine where a program resides
```$ which tclsh
/usr/bin/tclsh
$ whereis tclsh
tclsh: /usr/bin/tclsh8.5 /usr/bin/tclsh /usr/bin/X11/tclsh8.5 /usr/bin/X11/tclsh /usr/share/man/man1/tclsh.1.gz 
```

List available Java JREs
```update-java-alternatives -l```

Pick desired Java JRE
```sudo update-alternatives --config java```

List all exported variables and functions
```export -p```

To syntax highlight a source file (html output format)
```
#!/bin/bash
export WWW_ROOT=/usr/share/nginx/www
pygmentize -f html -O full,style=manni -o $WWW_ROOT/pyg.html $1
```

To see all pygments filters, lexers, and styles
```pygmentize -L | less```

To remove all IP addresses from an interface
```sudo ip addr flush dev eth1```

To display the routing table you can use any of the following methods:
```
sudo route -n
netstat -rn
ip route list
```

Add a static route
```ip route add 10.10.10.0/24 via 192.168.1.254 dev eth1```

netstat options
```
-r : kernel IP routing table
-n : numeric addresses
-i : interface stats
-e : extended info
-u : UDP
-t : TCP
-a : All
```

Display X monitor infok
```xrandr```

Set display position
```xrandr --output VBOX0 --left-of VBOX1```

Set screen resolution
Step 1 - copy Modeline from output of
```
cvt 1920 1080
```
Step 2 - Add a new mode
```
xrandr --newmode <Modeline>
$ xrandr --newmode "1920x1080_60.00"   173.00  1920 2048 2248 2576  1080 1083 1088 1120 -hsync +vsync
```
Step 3 - Add mode to monitor
```
xrandr --addmode VBOX0 <ModeName>
$ xrandr --addmode VBOX0 1920x1080_60.00
```
Step 4 - Set monitor mode
```
xrandr --output VBOX0 --mode <ModeName)
```
http://www.ubuntugeek.com/how-change-display-resolution-settings-using-xrandr.html

A good way to inspect what a command is
```type <cmd>```
If it's a program or script, it will give you it's location. If it's an alias, it will tell you what it's aliased to, if it's a function, it will print the function; otherwise, it will tell you if it is a built-in or a keyword.

Links:
https://wiki.debian.org/NetworkConfiguration
https://wiki.debian.org/NetworkConfiguration#Bringing_up_an_interface_without_an_IP_address
http://askubuntu.com/questions/62681/how-do-i-setup-dual-monitors-in-xfce

Set up an FTP server (pure-ftpd):
#install pure-ftpd (for Polycom phone configs)
#add universe repository (if needed)
#append this to /etc/apt/sources.list:
deb http://us.archive.ubuntu.com/ubuntu/ saucy universe
deb-src http://us.archive.ubuntu.com/ubuntu/ saucy universe
deb http://us.archive.ubuntu.com/ubuntu/ saucy-updates universe
deb-src http://us.archive.ubuntu.com/ubuntu/ saucy-updates universe
#refresh package manager:
sudo apt-get update
#install pure-ftpd
sudo apt-get install pure-ftpd
(Per https://help.ubuntu.com/community/PureFTP)
sudo groupadd ftpgroup
sudo useradd -g ftpgroup -d /dev/null -s /etc ftpuser
sudo mkdir /var/ftproot
sudo chown -R ftpuser:ftpgroup /var/ftproot
sudo pure-pw useradd polycomftp -u ftpuser -d /var/ftproot
#set the password to "password" when prompted
sudo pure-pw useradd brett -u ftpuser -d /var/ftproot
#set the password to "brett" when prompted
sudo pure-pw mkdb
sudo ln -s /etc/pure-ftpd/pureftpd.passwd /etc/pureftpd.passwd
sudo ln -s /etc/pure-ftpd/pureftpd.pdb /etc/pureftpd.pdb
sudo ln -s /etc/pure-ftpd/conf/PureDB /etc/pure-ftpd/auth/PureDB
sudo service pure-ftpd restart



Display current logged in user's username
$ whoami

List all currently logged in users (two different ways)
$ w
$ who

Time a command (measure execution time)
$ time my_script.sh

real    0m17.401s
user    0m3.076s
sys     0m4.384s

Display current date and time
$ date
Sun Mar  9 17:01:08 CDT 2014



Set date and time
date -s "2 OCT 2006 18:00:00"

Display NTP associations
$ ntpq -p

Display NTP date
$ ntpdate

Check if a process (ntpd) is running:
$ ps -f -C ntpd

Grep manpage (for the ps command) for a particular option (-f) with 2 lines of trailing context (--after-context=NUM or -A NUM)
$ man ps | grep "\-f" -A2

Set the raw sockets capability on the python interpreter executable:
sudo setcap 'CAP_NET_RAW+eip CAP_NET_ADMIN+eip' /usr/bin/python2.7
sudo setcap 'CAP_NET_RAW+eip CAP_NET_ADMIN+eip' /usr/sbin/tcpdump

Recursively change owner of a directory
sudo chown -R user:pass dir

Read crontab manual page
man crontab

Display your crontab
crontab -l

Edit your crontab (or create one if it doesn't already exist):
crontab -e

crontab syntax:
*     *     *   *    *        command to be executed
-     -     -   -    -
|     |     |   |    |
|     |     |   |    +----- day of week (0 - 6) (Sunday=0)
|     |     |   +------- month (1 - 12)
|     |     +--------- day of        month (1 - 31)
|     +----------- hour (0 - 23)
+------------- min (0 - 59)

crontab quick reference:
http://www.adminschoice.com/crontab-quick-reference/

Display system uptime
brett@OPGX270MVT1:~$ uptime
 14:58:32 up  2:00,  4 users,  load average: 0.18, 0.10, 0.12

List all mounted devices
mount

List usb verbosely
lsusb -v

Open a serial console to the specified serial port
cu -l /dev/ttyS0 -s 9600

To exit serial console (cu), type:
~.

Perform a basic port scan with nmap
nmap -A -T4 <host>

Switch between tabs in gnome-terminal, gedit, and some other programs
```Alt + n```
Where n is the tab number. 1=1st tab, 2=2nd tab, etc.

random vs. urandom
random - will block if the entropy pool is empty
urandom - will generate data using an algorithm if entropy pool is empty, will never block.
http://stupefydeveloper.blogspot.com/2007/12/random-vs-urandom.html


Generate a fake file with dd
```dd if=/path/to/input of=/path/to/output [options]```
e.g.
```dd if=/dev/urandom of=testfile.biz bs=1M count=1```

Clean package manager (try this if you are having issues):
sudo apt-get clean

Install a TFTP server
sudo apt-get install tftpd-hpa
Create root directory and set the permissions (the lazy way)
sudo mkdir /var/tftpboot
sudo chmod 777 /var/tftpboot

Edit configuration file
/etc/default/tftpd-hpa
TFTP_USERNAME="tftp"
TFTP_DIRECTORY="/var/tftpboot"
TFTP_ADDRESS="0.0.0.0:69"
TFTP_OPTIONS="-s -c -v"
RUN_DAEMON="yes"
Restart service
sudo service tftpd-hpa start
Source: http://www.cesareriva.com/install-tftp-server-on-linux/

Add a user
sudo useradd admin
Set password
sudo passwd admin
Add a user to sudoers
sudo adduser admin sudo
Delete a user
sudo userdel admin
Remove user from suders
sudo deluser admin sudo
Enable the root account
sudo passwd root

http://en.wikipedia.org/wiki/Setuid

Setting the setgid permission on a directory (chmod g+s) causes new files and subdirectories created within it to inherit its group ID, rather than the primary group ID of the user who created the file (the owner ID is never affected, only the group ID). 

Expect script to scp all files in the current directory
```
#!/usr/bin/expect -f

spawn scp -r . admin@10.17.127.11:~/AudioPathVerification/
#######################
expect {
  -re ".*es.*o.*" {
    exp_send "yes\r"
    exp_continue
  }
  -re ".*sword.*" {
    exp_send "H81Fv5G2\r"
  }
}
interact
```
Install oracle-java8 on Ubuntu 14.04
http://www.webupd8.org/2012/09/install-oracle-java-8-in-ubuntu-via-ppa.html

Edit the udev rules to change the names of network adapters
Step 1. Determine the MAC addresses of the interfaces (using ifconfig -a)
Step 2. Edit /etc/udev/rules.d/70-persistent-net.rules
http://www.cyberciti.biz/faq/howto-linux-rename-ethernet-devices-named-using-udev/



Configuration file '/etc/X11/Xsession.d/98vboxadd-xclient'
 ==> Modified (by you or by a script) since installation.
 ==> Package distributor has shipped an updated version.
   What would you like to do about it ?  Your options are:
    Y or I  : install the package maintainer's version
    N or O  : keep your currently-installed version
      D     : show the differences between the versions
      Z     : start a shell to examine the situation
 The default action is to keep your current version.
*** 98vboxadd-xclient (Y/I/N/O/D/Z) [default=N] ? 
Setting up virtualbox-guest-dkms (4.3.36-dfsg-1+deb8u1ubuntu1.14.04.1) ...
Loading new virtualbox-guest-4.3.36 DKMS files...
Building only for 3.13.0-24-generic


btolbert@vm4:~/Perforce/btolbert.cn.lnx.dev4/team/MVT/mvt_myacmed$ 
btolbert@vm4:~/Perforce/btolbert.cn.lnx.dev4/team/MVT/mvt_myacmed$ make test
Sending build context to Docker daemon 113.9 MB
Step 1 : FROM docker.brett.com:5000/osa-ne/service-kit:8
Get https://docker.brett.com:5000/v1/_ping: tls: oversized record received with length 20527
-e \e[0m
\e[1m\e[31m(E) Failed to build Docker image, mvt_myacmed_default, from build/default/Dockerfile.  Aborting with error code of 1...\e[0m

make: *** [out/default/.dockerImage] Error 1
btolbert@vm4:~/Perforce/btolbert.cn.lnx.dev4/team/MVT/mvt_myacmed$ 


Check for incoming packets from a host
sudo tcpdump -nnxX -i eth0 src 10.17.199.251

Scan a subnet for open web servers
nmap -p 80 --open -sV 10.17.199.0/24

Count the number of lines of output
I.e. the number of warning messages generated by the first command
pylama -l pep8,import_order src test | wc -l

pep8 comes with pylint package
don't use pycodestyle, can't configure max-line-length

```pylama --options tox.ini verb_conjugate_fr test```


Delete all .pyc files
```find . -type f -name "*.pyc" | xargs rm```

Check the nameservers
```
$ nmcli -t -f IP4.DNS device show eth0
IP4.DNS[1]:192.168.1.1
IP4.DNS[2]:8.8.8.8
```