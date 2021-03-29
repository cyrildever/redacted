# redacted
_Redacting classified documents_

This repository holds the code base for my `redacted` libraries and executables.
It is mainly based off my [Feistel cipher for Format-Preserving Encryption](https://github.com/cyrildever/feistel) to which I added a few tools to handle document, database and file manipulation to ease out the operation.

### Motivation

In some fields (like healthcare for instance), protecting the privacy of data whilst being able to conduct in-depth studies is both vital and mandatory. Redacting documents and databases is therefore the obligatory passage.
With `redacted`, I provide a simple yet secure tool to help redacting documents based on either a dictionary, a record layout or a tag to decide which parts should actually be redacted.

As of the latest version, this repository comes with three different ways to use it:
* An executable (to use on either Linux, MacOS or Windows environment);
* A Go library;
* A TypeScript library (which is also available on [NPM](https://www.npmjs.com/package/redacted-ts)).


### Usage

#### 1. Executables

```
Usage of ./redacted:
  -b    add to use both dictionary and tag
  -d string
        the optional path to the dictionary of words to redact
  -i string
        the path to the document to be redacted
  -o string
        the name of the output file
  -t string
        the optional tag that prefixes words to redact (default "~")
```

Download the version for the platform of your choice then execute the following command:
```console
$ ./redacted -i=myFile.txt -d=myDictionay.txt -o=myRedactedFile.txt
```
_NB: Use `redacted.exe` for Windows._

#### 2. Libraries

<u>Go</u>

```console
go get github.com/cyrildever/redacted
```

<u>TypeScript/JavaScript</u>

```console
npm install redacted-ts
```


### License

The use of the `redacted` libraries and executables are subject to fees for commercial purpose and to the respect of the [BSD-2-Clause-Patent license](LICENSE).
Please [contact me](mailto:cdever@edgewhere.fr) to get further information.


<hr />
&copy; 2021 Cyril Dever. All rights reserved.