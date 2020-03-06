#!/bin/bash

# Exit on any failure
set -e

find target -iname "*.jar" -exec cp {} teleport-scala.jar \;
native-image --verbose --static --no-fallback  -jar teleport-scala.jar teleport-scala
