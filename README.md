[![Build Status](https://travis-ci.com/note/teleport-scala.svg?branch=master)](https://travis-ci.com/note/teleport-scala)
[![GitHub release](https://img.shields.io/github/v/release/note/teleport-scala.svg)](https://GitHub.com/note/teleport-scala/releases/)

# teleport-scala

A clone of [teleport](https://github.com/bollu/teleport) written in Scala as a showcase that writing native CLIs in Scala
might not be such a bad idea.

[![asciicast](https://asciinema.org/a/Zj1ZDAgF02PP3JpD5RNtwBz0M.svg)](https://asciinema.org/a/Zj1ZDAgF02PP3JpD5RNtwBz0M)

## How to build teleport-scala

You should have following on your `$PATH`:

* Java JDK 11
* `sbt`
* `native-image`

You can consult file `.travis.yml` in case of difficulties in installing prerequisites. Once you have everything installed
you can:

```
./build.sh
```

As a result `teleport-scala.jar` and executable `teleport-scala` should be created.

## Bringing `tp` into scope

If you watched the asciiname animation you may have noticed that it uses `tp` command as opposed to `teleport-scala`.
To bring `tp` into scope add the following to your `.zshrc`/`.bashrc`:

```
source /your/path/to/teleport-scala/teleport.sh
```

It's crucial that executable `teleport-scala` is in the same directory as `teleport.sh` (which is the case by default after running `./build.sh`).

### For curious - what's the point of having `tp` in addition to `teleport-scala`?

The problem is that `goto` command cannot be fully implemented in a subprocess; it's not possible for the 
`teleport-scala` to change working directory of the caller process. Therefore, `teleport-scala goto point` returns
status code 2 and prints the absolute path of the `point`. As bash function `fp` is sourced it can change the working 
directory.
 
If that sounds vague to you just read `teleport.sh` file - it's just a few lines of code.  

## Usage

```
> tp --help
Usage:
    teleport-scala [--no-colors] [--no-headers] add
    teleport-scala [--no-colors] [--no-headers] list
    teleport-scala [--no-colors] [--no-headers] remove
    teleport-scala [--no-colors] [--no-headers] goto
    teleport-scala [--no-colors] [--no-headers] version

teleport: A tool to quickly switch between directories

Options and flags:
    --help
        Display this help text.
    --no-colors
        Disable ANSI color codes
    --no-headers
        Disable printing headers for tabular data

Subcommands:
    add
        add a teleport point
    list
        list all teleport points
    remove
        remove a teleport point
    goto
        go to a created teleport point
    version
        display version
```

## Running smoke-test

You need to build the project first. If it's built then:

```
./smoke-test.sh
```
