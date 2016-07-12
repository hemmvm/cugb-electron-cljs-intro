
# CUGB Electron Intro

## Caution

This is extremely hacky code.
It was written during a live coding demonstration for the Clojure User Group Bonn --
please feel free to fork and refactor ;-)

## Description - TBD

You may want to have a look at the [intro notes from the talk](https://github.com/hemmvm/cugb-electron-cljs-intro/blob/master/talk/notes.org)

<img src="https://raw.githubusercontent.com/hemmvm/cugb-electron-cljs-intro/master/talk/screenshot_01.png" width="450px">

## Requirements

* JDK 1.7+
* Leiningen 2.5.3
* node.js 5.1.1+
* sass

## Setup

Ensure node version

```
$ node --version
# v5.7.0
```

Fetch all npm

```
$ scripts/setup.sh
```

or (depending on your platform)

```
$ scripts/setup.bat
```

## Development

Start a Clojure REPL

```
$ lein repl
```

Fire up a ClojureScript REPL (along with cljs+sass autoloading)

```
user> (user/fig-init)
```

Launch Electron


```
$ grunt launch
```

## Grunt commands

To run a command, type `grunt <command>` in the terminal.

| Command       | Description                                               |
|---------------|-----------------------------------------------------------|
| setup         | Download electron project, setups up the app config file. |
| launch        | Launches the electron app in dev-mode                     |
| release       | Creates a Win/OSX/Linux executables                       |


## License

MIT
