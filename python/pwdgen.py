#!/usr/bin/env python
import os
import sys
import string
from struct import *

if len(sys.argv) != 2:
    print 'Usage: gen_pwd.py <pwd_len>'
else:
    pwd_len = int(sys.argv[1])
    chars = string.ascii_letters + string.digits + string.punctuation
    rand_bytes = os.urandom(pwd_len)
    pwd = ''
    for i in range(pwd_len):
        pwd += chars[unpack_from('B', rand_bytes, i)[0] % len(chars)]
    print pwd
