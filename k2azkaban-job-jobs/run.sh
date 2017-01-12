#!/bin/bash

job_dir=$(dirname $0)

conf=./conf

CLASSPATH=$job_dir/k2azkaban-job-jobs-1.0.jar

for file in $job_dir/lib/*.jar;
do
  CLASSPATH=$CLASSPATH:$file
done

if [ -f $conf/log4j2.xml ]; then
  OPTS="$OPTS -Dlog4j.configuration=file:$conf/log4j2.xml"
fi

echo java $OPTS -cp $CLASSPATH com.k2data.job.common.MainClass $1

java $OPTS -cp $CLASSPATH com.k2data.job.common.MainClass $1