#!/bin/bash

# Exit on any failure
set -e

which unzip || echo "not found: unzip"
which curl || echo "not found: curl"
which wget || echo "not found: wget"


echo $SDKMAN_PLATFORM
echo $JAVA_HOME
echo $PATH
#ls -al /c/Users/travis/.sdkman/candidates/java/current/bin

which java || echo "fail0"
which javac || echo "fail1"


