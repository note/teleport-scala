#!/bin/bash

# Exit on any failure
set -e

native-image --verbose --static --no-fallback  -jar target/scala-2.13/teleport-scala-assembly-0.1.0.jar teleport-scala
