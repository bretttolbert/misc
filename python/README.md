mkdir ~/Git && cd ~/Git
pip uninstall pyang
git clone git@github.com:mbj4668/pyang.git
cd pyang
python setup.py develop
export YANG_MODPATH=~/Git/yang-explorer/default-models/
export PYANG_XSLT_DIR=~/Git/pyang/xslt
export PYANG_RNG_LIBDIR=~/Git/pyang/schema
sudo apt-get install xsltproc
cd ~/Git/misc/python/yang/models
pyang --strict example-sports.yang
pyang -f tree example-sports.yang
pyang -f sample-xml-skeleton -o sample-skeleton.xml example-sports.yang
yang2dsdl example-sports.yang
 
Useful links:
https://www.yumaworks.com/pub/docs/15.10/pdf/YANG_Cheat_Sheet.pdf
https://github.com/CiscoDevNet/yang-explorer
https://en.wikipedia.org/wiki/YANG
http://www.tail-f.com/education/what-is-netconf/
http://manpages.ubuntu.com/manpages/zesty/man1/yangcli.1.html
http://www.yang-central.org/twiki/pub/Main/YangTools/pyang.1.html
https://github.com/mbj4668/pyang
https://www.yumaworks.com/pub/docs/16.10/pdf/yumapro-yangdiff-manual.pdf
https://github.com/mbj4668/pyang/wiki/Documentation
http://www.cisco.com/c/dam/global/cs_cz/assets/ciscoconnect/2014/assets/tech_sdn10_sp_netconf_yang_restconf_martinkramolis.pdf
http://www.yang-central.org/twiki/bin/view/Main/YangTools :
 Validation of instance XML documents (datastore contents or NETCONF PDUs) using pyang is explained in DSDLMappingTutorial <http://www.yang-central.org/twiki/bin/view/Main/DSDLMappingTutorial> and yang2dsdl manual page <http://www.yang-central.org/twiki/pub/Main/YangTools/yang2dsdl.1.html>
 
pyang
The following directories are always added to the search path:
current directory
$YANG_MODPATH
$HOME/yang/modules
$YANG_INSTALL/yang/modules OR if $YANG_INSTALL is unset <the default installation directory>/yang/modules (on Unix systems: /usr/share/yang/modules)
 
If you get errors like:
warning: failed to load external entity "/usr/local/share/yang/xslt/basename.xsl"
warning: failed to load external entity "schema-dir"
If you look at the yang2dsdl source, you'll see there are two environment variable you can set to override the default dirs:
https://github.com/mbj4668/pyang/blob/master/bin/yang2dsdl
xslt_dir=${PYANG_XSLT_DIR:-/usr/local/share/yang/xslt}
schema_dir=${PYANG_RNG_LIBDIR:-/usr/local/share/yang/schema}
pip uninstall pyang
git clone git@github.com:mbj4668/pyang.git
cd pyang
python setup.py develop
export YANG_MODPATH=~/Git/yang-explorer/default-models/
export PYANG_XSLT_DIR=~/Git/pyang/xslt
export PYANG_RNG_LIBDIR=~/Git/pyang/schema
sudo apt-get install xsltproc
cd ~/Git/misc/python/yang/models
pyang --strict example-sports.yang
pyang -f tree example-sports.yang
pyang -f sample-xml-skeleton -o sample-skeleton.xml example-sports.yang
yang2dsdl example-sports.yang
 
Useful links:
https://www.yumaworks.com/pub/docs/15.10/pdf/YANG_Cheat_Sheet.pdf
https://github.com/CiscoDevNet/yang-explorer
https://en.wikipedia.org/wiki/YANG
http://www.tail-f.com/education/what-is-netconf/
http://manpages.ubuntu.com/manpages/zesty/man1/yangcli.1.html
http://www.yang-central.org/twiki/pub/Main/YangTools/pyang.1.html
https://github.com/mbj4668/pyang
https://www.yumaworks.com/pub/docs/16.10/pdf/yumapro-yangdiff-manual.pdf
https://github.com/mbj4668/pyang/wiki/Documentation
http://www.cisco.com/c/dam/global/cs_cz/assets/ciscoconnect/2014/assets/tech_sdn10_sp_netconf_yang_restconf_martinkramolis.pdf
http://www.yang-central.org/twiki/bin/view/Main/YangTools :
 Validation of instance XML documents (datastore contents or NETCONF PDUs) using pyang is explained in DSDLMappingTutorial <http://www.yang-central.org/twiki/bin/view/Main/DSDLMappingTutorial> and yang2dsdl manual page <http://www.yang-central.org/twiki/pub/Main/YangTools/yang2dsdl.1.html>
 
pyang
The following directories are always added to the search path:
current directory
$YANG_MODPATH
$HOME/yang/modules
$YANG_INSTALL/yang/modules OR if $YANG_INSTALL is unset <the default installation directory>/yang/modules (on Unix systems: /usr/share/yang/modules)
 
If you get errors like:
warning: failed to load external entity "/usr/local/share/yang/xslt/basename.xsl"
warning: failed to load external entity "schema-dir"
If you look at the yang2dsdl source, you'll see there are two environment variable you can set to override the default dirs:
https://github.com/mbj4668/pyang/blob/master/bin/yang2dsdl
xslt_dir=${PYANG_XSLT_DIR:-/usr/local/share/yang/xslt}
schema_dir=${PYANG_RNG_LIBDIR:-/usr/local/share/yang/schema}
