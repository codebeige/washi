# Washi

Unified HTML templating for Clojure and ClojureScript.

## Why?

While we all learned to love the concise and declarative nature of Hiccup-style
templating, subtle differences in input format and implementation can be
irritating and pretend us from sharing markup between different render targets.

What if we do not need to know about the final application context at all when
writing our markup?

## Development

For continuously running tests on every code change trigger the following
command:

```sh
% boot watch test
```

In order to run the tests in a JS environment use:

```sh
% boot test-cljs
```
