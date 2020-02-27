#!/bin/bash

# Exit on any failure
set -e

docker run -it -v $(pwd):/root lolhens/ammonite /bin/bash -c "cd /root && amm /root/smoke-test.sc"
