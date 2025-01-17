# WREPL - Configure the REPL with Integrant

Run a REPL where all "letters" allow for dependency injection.  This
allows easier mixing and matching components you want to have in your
REPL.  E.g. use rebel-readline, but use your own pretty-printer; or
write your own specialized prompts like "REST-REPL", to talk to the web
(see examples).

**Note:** Since version 0.2 this integrates now with the Clojure CLI
tools; if you are interested in the stand-alone version, use the `0.1.X`
branch.


## Examples

Check the [example](./example) folder.

- `deps.edn` : Clojure CLI configuration with two aliases.  You can put the aliases in your `~/.config/clojure/deps.edn` file.
- `wrepl` : Run a WREPL with rebel-readline and puget pretty-printing
  with the colors configured
- `wrepl.edn` : Configuration for the above; you can put this file at
  `~/.wrepl/wrepl.edn`.
- `rest-repl` : Same like above, but runs REST-REPL; note the `-b
  rest-repl` arguments, to give the configuration a different base-name,
  so different things may be configured.
- `rest-repl.edn` : Configuration for the above; you can put this file
  at `~/.wrepl/rest-repl.edn`


## Options

The main from WREPL has the following options:

 ```
  -b, --base-name base-name  Base name for the user config to search for; e.g. $HOME/.wrepl/$BASENAME.edn (default: wrepl)
  -c, --config config.edn    Read the integrant system config from this file and merge it with the default
      --no-user-config       Don't load the default user config
  -i, --init script.clj      Run the given file before the first prompt
  -e, --eval string          Evaluate the expression (after --init if both given)
  -h, --help
```

## Configuration

Configuration is done with an `EDN` file, that
[`immutant`](https://github.com/weavejester/integrant) will read.  So a
basic understanding of that syntax is helpful.

By default, WREPL will attempt to find and load the files or resources
in the following order:

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
  current running evaluation (used as default)


### Replacements

If supported by the configuration (mentioned above) the following
replacements are available inside strings:

- `$HOME`: What Java considers the user home (the property `user.home`)


## Known plugins

- [clj-wrepl-puget](https://github.com/christoph-frick/clj-wrepl-puget) Color pretty print
- [clj-wrepl-rebel-readline](https://github.com/christoph-frick/clj-wrepl-rebel-readline) Rebel Readline
- [clj-wrepl-relative-clj-http](https://github.com/christoph-frick/clj-wrepl-relative-clj-http) Exploring web APIs interactively
