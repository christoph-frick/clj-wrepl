# WREPL

Starting the *Clojure REPL* in a customizable fashion.  The goal is to
make it easer to write "prepped" REPLs.


## Motivation

Before the Clojure CLI tools where released, I found myself often in the
need outside of project to be able to fire up a REPL (e.g. to answer an
Stack-Overflow question).  That was very east before the days of
`clojure.spec`, which then needed multiple JARs to be present.

I ended up doing some pre-packaged JARs for different use-cases (e.g.
a "comfort" REPL with things like `specter` in it, one for `incanter`, one
for interactive exploration of web API
([`rest-repl`](https://github.com/christoph-frick/rest-repl)).  But in the
end it became cat herding.

So this project aims to fix my problems:

- provide a modular setup for (differently named) REPLs to run outside
  of projects
- put features for the REPL in dedicated plugins

Is it still relevant?  All this work started long before (even in
public) the Clojure CLI tools where released.  I think many of the
features here are superseded now, yet I never came around to make the
REST-REPL scenario work and I think `clj` is focused more around the
project setup and not so much to get a "prepped" REPL (e.g. override
multiple different things around the REPL).


## Run

Download [a
release](https://github.com/christoph-frick/clj-wrepl/releases) or
checkout this repository and build the *uberjar* (`lein uberjar`).

```shell
% alias wrepl="java -jar target/wrepl-*-standalone.jar"
% wrepl --help
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

## Configuration

Configuration is done with an `EDN` file, that
[`immutant`](https://github.com/weavejester/integrant) will read.  So an
basic understanding of that syntax is helpful.

By default the WREPL uberjar will try to attempt to find and load the
files or resources in the following order:

- File: `".$BASENAME.edn"`
- File: `".wrepl/$BASENAME.edn"`
- File: `"$HOME/.$BASENAME.edn"`
- File: `"$HOME/.wrepl/$BASENAME.edn"`
- Resource: `"$BASENAME.edn"`

Where `$BASENAME` is the configured base name given as an argument, or
the default, `wrepl`.

### Entry points

The main configuration points to override the behaviour.  These are
basically what REPL to start and what to pass to it, following the
example of the `clojure.main/repl`:

- `:wrepl/repl`: REPL-Function; this must behave like
  `clojure.main/repl`, which is the default
- `:wrepl/init`: List of functions to run first in started REPL; the
  default is changing into the `user` namespace and providing another
  hook to add more init functions without overwriting the default (named
  `:wrepl/append-init`)
- `:wrepl/prompt`: Function to print a prompt for input; defaults to
  whatever the REPL provides
- `:wrepl/read`: Function to read the input; defaults to whatever the
  REPL provides
- `:wrepl/eval`: Function to evaluate the read form; defaults to
  `:wrepl.eval/interruptible`, which allows to `CTRL-C` the current
  running eval.
- `:wrepl/print`: Function to print the result of the evaluation;
  defaults to whatever the REPL provides

### Default expansion points

There are already some "plugins" the wrepl comes pre-packaged:

- `:wrepl.init/in-ns {:ns user}`: Change the default namespace of the
  REPL; run by default
- `:wrepl.init/load-file {:filename "$HOME/.wrepl/tools.clj"}`: Read and
  run the given `:filename`; does restricted variable replacements (see
  below)
- `:wrepl.init/eval {:expr "(+ 1 2 3)"}`: Reads the string `:expr`,
  evaluates it, and prints the result
- `:wrepl.eval/interruptible`: `eval`, that allows `CTRL-C` of the
  current running evaluation (used as default

### Replacements

If supported by the configuration (mentioned above) the following
replacements are available inside strings:

- `$HOME`: What Java considers the user home (the property `user.home`)

### Adding plugins

The entry point `:wrepl/deps` is a special case, that does not take
part in the `integrant` setup, but is used before to load additional
plugins, which then can be used inside the configuration.

`:wrepl/deps` uses
[`pomegranate`](https://github.com/cemerick/pomegranate_ internally and
uses the same configuration syntax.  E.g.

```clojure
:wrepl/deps {:coordinates [[net.ofnir/wrepl.puget "0.1.0"]
                           [net.ofnir/wrepl.rebel-readline "0.1.0"]
                           [net.ofnir/wrepl.pomegranate "0.1.0"]]
             ; this is the default and does not need to be added:
             :repositories {"clojars" "https://clojars.org/repo"
                            "jcenter" "https://jcenter.bintray.com"}}
```


## Known plugins

- [clj-wrepl-pomegranate](https://github.com/christoph-frick/clj-wrepl-pomegranate) Load dependencies
- [clj-wrepl-puget](https://github.com/christoph-frick/clj-wrepl-puget) Color pretty print
- [clj-wrepl-rebel-readline](https://github.com/christoph-frick/clj-wrepl-rebel-readline) Rebel Readline
- [clj-wrepl-relative-clj-http](https://github.com/christoph-frick/clj-wrepl-relative-clj-http) Exploring web APIs interactively
