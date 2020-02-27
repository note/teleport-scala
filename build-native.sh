#!/bin/bash

# Exit on any failure
set -e

VERSION=`sbt version | tail -n 1 | awk '{print $2}'`
native-image --verbose --static --no-fallback  -jar "target/scala-2.13/teleport-scala-assembly-$VERSION.jar" teleport-scala
