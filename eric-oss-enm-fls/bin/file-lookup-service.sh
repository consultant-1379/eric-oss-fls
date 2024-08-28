#!/bin/bash
# ********************************************************************
# Ericsson Inc.                                                         SCRIPT
# ********************************************************************
#
# (c) Ericsson Inc. 2020 - All rights reserved.
#
# The copyright to the computer program(s) herein is the property of
# Ericsson Inc. The programs may be used and/or copied only with written
# permission from Ericsson Inc. or in accordance with the terms and
# conditions stipulated in the agreement/contract under which the
# program(s) have been supplied.
#
# ********************************************************************
# Name    : file-lookup-service.sh
# Purpose : This script to used to host file-lookup-service application.
#
# Usage   : ${INSTALL_DIR}/file-lookup-service.sh
#
# ********************************************************************

echo Your container args are: "$@"

# read the url from the config/application.properties
input=/eric-oss-enm-fls/config/application.properties
serviceName=""

while IFS= read -r line
do
  if [[ $line == "spring.datasource.url"* ]]; then
        IFS='//' read -ra ADDR <<< "$line"
        baseUrl="${ADDR[0]}"//
        line1="${ADDR[2]}"
        defaultDb="${ADDR[3]}"
        IFS=':' read -ra ADDR2 <<< "$line1"
        defaultIp="${ADDR2[0]}"
        defaultPort="${ADDR2[1]}"
  fi

done < "$input"

# get the command line params and update config/application.properties
#serviceName=<value> //This is db-service name running on K8.
for var in "$@"
do
  if [[ $var == "serverPort"* ]]; then
    serverPort=${var#*=}
    sed -i "s@server.port=8080@server.port=$serverPort@" /eric-oss-enm-fls/config/application.properties
  fi
  if [[ $var == "dbIp"* ]]; then
    defaultIp=${var#*=}
  fi
  if [[ $var == "dbPort"* ]]; then
    defaultPort=${var#*=}
  fi
  if [[ $var == "dbName"* ]]; then
    defaultDb=${var#*=}
  fi
  if [[ $var == "serviceName"* ]];then
        serviceName=${var#*=}
  fi
done

if [ ! -z "$serviceName" ]; then
 serviceIp=`printenv | grep ${serviceName^^}"_SERVICE_HOST"`
 servicePort=`printenv | grep ${serviceName^^}"_SERVICE_PORT"`
 defaultIp=${serviceIp#*=}
 defaultPort=${servicePort#*=}
fi

ENM_ID=`echo $FLS_ENM_ID | awk -F'-' '{print $5}'`
sed -i "s@id-postgres-db:5432@${ENM_ID}-postgres-db:5432@" /eric-oss-enm-fls/config/application.properties

sed -i "s@id:5454@${ENM_ID}:8080@" /eric-oss-enm-fls/config/application.properties
sed -i "s@fns.id=-1@fns.id=${FNS_MODE}@" /eric-oss-enm-fls/config/application.properties
sed -i "s@pipeline=true@pipeline=${PIPELINE}@" /eric-oss-enm-fls/config/application.properties
PIPELINE_VALUES="RadioNode Pcc Pcg NrCtr LteCtr Ebsn Cnf"
for KEY in $PIPELINE_VALUES; do
    VALUE=`printenv ${KEY}`
    sed -i "s@expected${KEY}Count=.*@expected${KEY}Count=${VALUE}@" /eric-oss-enm-fls/config/application.properties
done

cat /eric-oss-enm-fls/config/application.properties
INSTALL_DIR="/eric-oss-enm-fls"

java -Xms2m -jar ${INSTALL_DIR}/lib/eric-oss-enm-fls.jar --spring.config.location=file:/eric-oss-enm-fls/config/application.properties ${INSTALL_DIR}
