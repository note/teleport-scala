# teleport-scala

[teleport](https://github.com/bollu/teleport) rewritten in Scala as a showcase that writing native CLIs in Scala
might not be such a bad idea.

[![asciicast](https://asciinema.org/a/Zj1ZDAgF02PP3JpD5RNtwBz0M.svg)](https://asciinema.org/a/Zj1ZDAgF02PP3JpD5RNtwBz0M)

`goto` command cannot be fully implemented in subprocess. `teleport-scala goto point` executable will return
status code 2 after printing the absolute path of the `point` (if `point` is registered teleport point). It's not 
possible for the `teleport-scala` to change current directory of the caller process. The solution for that
is to have a bash function sourced. That function can use absolute path returned by `teleport scala`.

In `.zshrc`/`.bashrc`:

```
source /path/to/teleport-scala/teleport.sh
```  
