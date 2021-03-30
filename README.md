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

You can use either a dictionary or a tag (or both) to identify the words you want to redact in a document.
The tag should be placed before any word that should be redacted. The default tag is the tilde character (`~`).

For example, the following sentence will only see the word `tagged` redacted: `"This is a ~tagged sentence"`.

#### 1. Executables

```
Usage of ./redacted:
  -b    add to use both dictionary and tag
  -d string
        the optional path to the dictionary of words to redact
  -h string
        the hash engine for the round function (default "sha-256")
  -i string
        the path to the document to be redacted
  -k string
        the optional key for the FPE scheme (leave it empty to use default)
  -o string
        the name of the output file
  -r int
        the number of rounds for the Feistel cipher (default 10)
  -t string
        the optional tag that prefixes words to redact (default "~")
  -x    add to expand a redacted document
```
The dictionary file must contain a list of word separated by a space.

Download the version for the platform of your choice then execute the following command:
```console
$ ./redacted -i=myFile.txt -d=myDictionay.txt -o=myRedactedFile.txt
```
_NB: Use `redacted.exe` for Windows._

__IMPORTANT: Do not use with input texts having lines longer than 65536 characters.__

#### 2. Libraries

<u>Go</u>

```console
go get github.com/cyrildever/redacted
```

```golang
import (
    "github.com/cyrildever/feistel"
    "github.com/cyrildever/redacted/core"
    "github.com/cyrildever/redacted/model"
)

// Load dictionary
dic, err := model.FileToDictionary("/path/to/dictionary.txt")

// Prepare FPE cipher
cipher := feistel.NewFPECipher(hashEngine, key, rounds)

// Instantiate redactor
redactor := core.NewRedactorWithDictionary(dictionary, cipher)

// Redact a line
redacted := redactor.Redact(line)
fmt.Println(redacted)

// Expand a redacted line
assert.Equal(t, redactor.Expand(redacted), line)
```
See the [`Dictionary`](model/dictionary.go) and the [`Redactor`](core/redactor.go) implementations to use other kinds of dictionaries (as a slice or from a string) and/or redactors (with or without tag and dictionary).

NB: You may use any other kind of Format-Preserving Encryption library as long as it respects the followinf interface:
```golang
type FPE interface {
    Decrypt(base256.Readable) (string, error)
    Encrypt(string) (base256.Readable, error)
}
```
_See my implementation of the `base256.Readable` string type alias in its [module](https://github.com/cyrildever/feistel/common/utils/base256)._


<u>TypeScript/JavaScript</u>

```console
npm install redacted-ts
```


### License

The use of the `redacted` libraries and executables are subject to fees for commercial purpose and to the respect of the [BSD-2-Clause-Patent license](LICENSE).
Please [contact me](mailto:cdever@edgewhere.fr) to get further information.


<hr />
&copy; 2021 Cyril Dever. All rights reserved.