#!/bin/bash

# Exit on any failure
set -e

if [ "$TRAVIS_OS_NAME" = windows ] || [ "$TRAVIS_OS_NAME" = osx ] ; then
  # We skip on both windows and osx but for different reasons
  # In case of windows it's about difficulty to programatically mount a volume
  # In case of macos it's about lack of support for docker on travis ci
  # The alternative would be to run non-dockerized ammonite but currently tests do some assumptions about paths
  echo "Skipping smoke-test, running basic test instead..."

  if [ "$TRAVIS_OS_NAME" = windows ] ; then
    EXTENSION=".exe"
  else
    EXTENSION=""
  fi
  
  # It does not verify a lot, mostly just that executable is not corrupted
  OUT=`./teleport-scala$EXTENSION --no-colors list`

  if [ "$OUT" != "teleport points: (total 0)" ]; then
    echo "unexpected output of 'teleport-scala.exe --no-colors list':"
    echo $OUT
    exit 1
  fi

  # The following almost works, the missing part is mounting a volume
  # On Windows desktop you can make it work by sharing C drive using Docker UI: 
  # https://docs.microsoft.com/en-us/archive/blogs/stevelasker/configuring-docker-for-windows-volumes
  # There's a thread about possibility of doing that programmatically:
  # https://forums.docker.com/t/is-there-a-way-to-share-drives-via-command-line/35967
  # One of recent comments mentions https://github.com/docker/for-win/issues/5139 which looks promising
  # 
  # winpty docker run -it -v $(pwd)/smoke-test.sc:/root/smoke-test.sc \
  #              -v $(pwd)/teleport-scala:/root/teleport-scala \
  #              -v $HOME/.cache/coursier:/root/.cache/coursier \
  #              lolhens/ammonite /bin/bash -c "cd /root && amm /root/smoke-test.sc teleport-scala.exe"

else # It's run either on travis on linux or locally
  docker run -it -v $(pwd)/smoke-test.sc:/root/smoke-test.sc \
               -v $(pwd)/teleport-scala:/root/teleport-scala \
               -v $HOME/.cache/coursier:/root/.cache/coursier \
               lolhens/ammonite /bin/bash -c "cd /root && amm /root/smoke-test.sc teleport-scala"
fi
