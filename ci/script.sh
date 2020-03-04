#!/bin/bash

# Exit on any failure
set -e

sbt assembly

VERSION=`sbt version | tail -n 1 | awk '{print $2}'`

if [ $TRAVIS_OS_NAME = windows ]; then
  find target -iname "*.jar" -exec cp {} teleport-scala.jar \;
  ci/windows.bat
elif [ $TRAVIS_OS_NAME = osx ]; then
  native-image --verbose --no-fallback -jar "target/scala-2.13/teleport-scala-assembly-$VERSION.jar" teleport-scala	
else
  native-image --verbose --static --no-fallback -jar "target/scala-2.13/teleport-scala-assembly-$VERSION.jar" teleport-scala
fi

# smoke-test uses teleport-scala built above
./smoke-test.sh

mkdir release

if [ $TRAVIS_OS_NAME = windows ]; then
  mv teleport-scala.exe release
else
  mv teleport-scala "release/teleport-scala-$TRAVIS_OS_NAME"
fi
