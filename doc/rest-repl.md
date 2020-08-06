# Using `wrepl` as [`rest-repl`](https://github.com/christoph-frick/rest-repl) replacement

One of the main motivations to write `wrepl` was being able to allow for
a way more configuration driven experience with something like
`rest-repl`.

## Setup

- Download
  a [release](https://github.com/christoph-frick/clj-wrepl/releases)
- Add an alias to quickly run `rest-repl`.  E.g.  `alias rest-repl='java
  -jar $HOME/bin/wrepl-*-standalone.jar -b rest-repl'` (note the
  location of the `.jar` file)
- Add a dedicated configuration file `$HOME/.wrepl/rest-repl.edn`, that
  at least loads the
  [`net.ofnir/wrepl.relative-clj-http`](https://github.com/christoph-frick/clj-wrepl-relative-clj-http)
  and sets up the prompt and allows for the initialization of the plugin
  (See below for an example).

### Example configuration

This is a fully working example configuration with:

- Full blown `rebel-readline` with the `relative-clj-http` prompt
- Color and pretty printing of results with `puget`
- Pull in several dependencies like `specter` and set them up

`wrepl` configuration for basename=`rest-repl`,
`~/.wrepl/rest-repl.edn`:

``` 
{:wrepl/deps {:coordinates [[net.ofnir/wrepl.puget "0.1.0"]
                            [net.ofnir/wrepl.rebel-readline "0.1.0"]
                            [net.ofnir/wrepl.relative-clj-http "0.1.0"]
                            [net.ofnir/wrepl.pomegranate "0.1.0"]]}

 :wrepl.relative-clj-http/init {}
 :wrepl.relative-clj-http/prompt {} 

 [:wrepl/append-init :wrepl/init] [#ig/ref :wrepl.relative-clj-http/init
                                   #ig/ref :repl/deps
                                   #ig/ref :repl/init]

 [:repl/deps :wrepl.pomegranate/init] {:coordinates [[org.clojure/test.check "0.9.0"]
                                                     [com.rpl/specter "1.1.2"]
                                                     [criterium "0.4.5"]
                                                     [io.aviso/pretty "0.1.37"]
                                                     [less-awful-ssl "1.0.4"]]}

 [:repl/init :wrepl.init/load-file] {:filename "$HOME/.wrepl/rest-repl.clj"} 

 :wrepl/repl #ig/ref :wrepl.rebel-readline/repl
 :wrepl.rebel-readline/repl {:prompt #ig/ref :wrepl.relative-clj-http/prompt}

 :wrepl/print #ig/ref :wrepl.puget/print
 :wrepl.puget/print {:seq-limit 20 
                     :color-scheme {:delimiter [:red]
                                    :string nil
                                    :character nil
                                    :keyword [:yellow]
                                    :symbol [:magenta]
                                    :function-symbol [:bold :magenta]
                                    :class-delimiter [:magenta]
                                    :class-name [:bold :magenta]}}}
```

And the referenced `~/.wrepl/rest-repl.clj`

```
(require 'io.aviso.repl)
(io.aviso.repl/install-pretty-exceptions)

(require '[criterium.core :refer [bench quick-bench]]) 

(require '[clojure.inspector :refer [inspect-table inspect-tree]])

(use 'com.rpl.specter) 
```
