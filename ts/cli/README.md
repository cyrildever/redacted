# redacted-cli
_A TypeScript-based CLI to redacting classified documents_

![npm](https://img.shields.io/npm/v/redacted-cli?label=tag)
![GitHub last commit](https://img.shields.io/github/last-commit/cyrildever/redacted)
![GitHub issues](https://img.shields.io/github/issues/cyrildever/redacted)
![npm](https://img.shields.io/npm/dw/redacted-cli)
![NPM](https://img.shields.io/npm/l/redacted-cli)

For more details, see the [`redacted-ts`](../README.md) library.

### Install

```console
$ npm install -g redacted-cli
$ redacted -V
```


### Usage

```console
$ redacted --help
  ____          _            _           _ 
 |  _ \ ___  __| | __ _  ___| |_ ___  __| |
 | |_) / _ \/ _` |/ _` |/ __| __/ _ \/ _` |
 |  _ <  __/ (_| | (_| | (__| ||  __/ (_| |
 |_| \_\___|\__,_|\__,_|\___|\__\___|\__,_|
                                           
Usage: redacted [options]

A TypeScript-based CLI to redacting classified documents

Options:
  -V, --version             output the version number
  -b, --both                add to use both dictionary and tag
  -d, --dictionary <value>  the optional path to the dictionary of words to redact
  -H, --hash <value>        the hash engine for the round function (default "sha-256")
  -i, --input <value>       the path to the document to be redacted
  -k, --key <value>         the optional key for the FPE scheme (leave it empty to use default)
  -o, --output <value>      the name of the output file
  -r, --rounds <value>      the number of rounds for the Feistel cipher (default 10)
  -t, --tag <value>         the optional tag that prefixes words to redact (default "~")
  -x, --expand              add to expand a redacted document
  -h, --help                display help for command
```

eg.
```console
$ redacted -i myInputFile.txt -o myRedactedFile.txt -d myDictionary.txt -b
$ redacted -x -i myRedactedFile.txt -o myExpandedResult.txt -d myDictionary.txt -b
```

_NB: The dictionary file must consist of space-separated words._


### License

The use of the `redacted` libraries and executables are subject to fees for commercial purpose and to the respect of the [BSD-2-Clause-Patent license](LICENSE). \
Please [contact me](mailto:cdever@pep-s.com) to get further information.


<hr />
&copy; 2023-2025 Cyril Dever. All rights reserved.