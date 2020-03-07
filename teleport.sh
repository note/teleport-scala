#!/bin/bash

# Copied from https://unix.stackexchange.com/a/351658:
if test -n "$BASH" ; then script=$BASH_SOURCE
elif test -n "$TMOUT"; then script=${.sh.file}
elif test -n "$ZSH_NAME" ; then script=${(%):-%x}
elif test ${0##*/} = dash; then x=$(lsof -p $$ -Fn0 | tail -1); script=${x#n}
else script=$0
fi

TELEPORT_SCALA_DIR=`dirname $script`

# mostly copied from https://github.com/bollu/teleport
function tp() {
    # $@ takes all arguments of the shell script and passes it along to `teleport-scala
    # which is our tool
    OUTPUT=`${TELEPORT_SCALA_DIR}/teleport-scala $@`
    # return code 2 tells the shell script to cd to whatever `teleport` outputs
    if [ $? -eq 2 ]
        then cd "$OUTPUT"
        else echo "$OUTPUT"
    fi
}

fpath=(`pwd` $fpath)
