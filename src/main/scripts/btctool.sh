#!/bin/sh

WORK_HOME=$(
  cd ../
  pwd
)

LIB_PATH=$WORK_HOME/conf
RUN_JAVA=$JAVA_HOME/bin/java

# shellcheck disable=SC2045
for jar in $(ls "$WORK_HOME"/lib/*.jar); do
  LIB_PATH=$LIB_PATH:$jar
done

#export LD_LIBRARY_PATH='$LD_LIBRARY_PATH:$WORK_HOME/tools/lib'
$RUN_JAVA -Dfile.encoding=UTF-8 -classpath "$LIB_PATH" org.ligson.coincap.CCBoot
