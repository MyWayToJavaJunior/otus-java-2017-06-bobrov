#!/bin/bash
MEMORY="-Xmx64m -Xms64m"
case $2 in
    1)
    GC="-XX:+UseSerialGC"
    ;;
    2)
    GC="-XX:+UseParallelGC -XX:+UseParallelOldGC"
    ;;
    3)
    GC="-XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+UseGCOverheadLimit"
    ;;
    4)
    GC="-XX:+UseG1GC"
    ;;
esac
java $MEMORY $GC -jar $1