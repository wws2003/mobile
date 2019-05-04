#!/bin/bash

if [ $# -lt 1 ]; then
	nb=25
else
	nb=$1;
fi
git log -n $nb --pretty='format:%cd %h %s | %an'
