# WREPL

Starting the *Clojure REPL* in a customizable fashion.  The goal is to make it
easer to write "prepped" REPLs.


## Build

Build an *uberjar*

```shell
lein uberjar
```

## Run

```shell
% alias clj="java -jar target/wrepl-0.1.0-SNAPSHOT-standalone.jar"
% clj --help
WREPL

wrepl [options...]

  -b, --base-name base-name  Base name for the user config to search for; e.g. $HOME/.wrepl/$BASENAME.edn (default: wrepl)
  -c, --config config.edn    Read the integrant system config from this file and merge it with the default
      --no-user-config       Don't load the default user config
  -i, --init script.clj      Run the given file before the first prompt
  -e, --eval string          Evaluate the expression (after --init if both given)
  -h, --help
```

Without a config some basic REPL is started.  A simple config to add
rebel-readline and puget can look like this (e.g. in
`~/.wrepl/wrepl.edn`; `wrepl` is the default base-name):

```clojure
{:wrepl/deps {:coordinates [[net.ofnir/wrepl.puget "0.1.0"]
                            [net.ofnir/wrepl.rebel-readline "0.1.0"]]}
 :wrepl/repl #ig/ref :wrepl.rebel-readline/repl
 :wrepl/print #ig/ref :wrepl.puget/print
 :wrepl.puget/print {:seq-limit 20
                     :color-scheme {:delimiter [:red]
                                    :string nil
                                    :character nil
                                    :keyword [:yellow]
                                    :symbol [:magenta]
                                    :function-symbol [:bold :magenta]
                                    :class-delimiter [:magenta]
                                    :class-name [:bold :magenta]}}
 :wrepl.rebel-readline/repl {}}
```

## Known plugins

- [clj-wrepl-relative-clj-http](https://github.com/christoph-frick/clj-wrepl-relative-clj-http) Exploring web APIs interactively
- [clj-wrepl-puget](https://github.com/christoph-frick/clj-wrepl-puget) Color pretty print
- [clj-wrepl-rebel-readline](https://github.com/christoph-frick/clj-wrepl-rebel-readline) Rebel Readline

### Deprecated

- ~~[clj-wrepl-pomegranate](https://github.com/christoph-frick/clj-wrepl-pomegranate) Load dependencies~~ Moved into the core itself; use `:wrepl/deps` instead 
