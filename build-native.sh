#!/bin/bash

# Exit on any failure
set -e

sbt assembly
find target -iname "*.jar" -exec cp {} teleport-scala.jar \;
native-image --verbose -H:+ReportExceptionStackTraces --static --no-fallback -jar teleport-scala.jar teleport-scala
