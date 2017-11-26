#!/bin/bash
export WWW_ROOT=/usr/share/nginx/www
pygmentize -f html -O full,style=manni -o $WWW_ROOT/pyg.html $1