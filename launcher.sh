#!/bin/bash


# Change this to your netid
netid=xxh163430

#
# Root directory of your project
PROJDIR=$HOME

#
# This assumes your config file is named "Config"
# and is located in your project directory
#
CONFIG=$PROJDIR/Config

#
# Directory your java classes are in
#
BINDIR=$PROJDIR

#
# Your main project class
#
PROG=Node

n=1

cat $CONFIG | sed -e "s/#.*//" | sed -e "/^\s*$/d" |
(
    read i
    echo $i
    while read line
    do
        host=$( echo $line | awk '{ print $1 }' )

        ssh $netid@$host.utdallas.edu java Node $n &

        n=$(( n + 1 ))
    done
   
)


