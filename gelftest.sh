#!/bin/bash

ghost=graylog2.rjocta012ahobe-88.cp.globoi.com

#ghost=10.132.4.138
#ghost=logging.syslog.udp.qa.globoi.com

type=$1
bigstack=$2
messages=$3
threads=$4
tag=$5
log4jversion=$6


pwd=`pwd`

echo host: $ghost
echo type: $type
echo threads: $threads
echo messages: $messages
echo tag: $tag
echo bigstack: $bigstack
echo log4jversion: $log4jversion

log4jConfigFile=""

if [ $log4jversion == "1" ]; then 
	log4jConfigFile="-Dlog4j.configuration=file:$(pwd)/src/main/resources/log4j.xml"
else
	log4jConfigFile="-Dlog4j.configurationFile=file:$(pwd)/src/main/resources/log4j2.xml"
fi


echo $log4jConfigFile

command="java $log4jConfigFile -DXmx1G -DXms1G -Dghost=$ghost -Dta=$tag -Dtype=$type -jar target/gelftest.jar $messages $threads $bigstack $log4jversion"

echo $command

$command