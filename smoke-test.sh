#!/bin/bash

# Exit on any failure
set -e

docker run -it -v $(pwd)/smoke-test.sc:/root/smoke-test.sc \
               -v $(pwd)/teleport-scala:/root/teleport-scala \
               -v $HOME/.cache/coursier:/root/.cache/coursier \
               lolhens/ammonite /bin/bash -c "cd /root && amm /root/smoke-test.sc"
