# redacted
_Redacting classified documents_

![GitHub tag (latest by date)](https://img.shields.io/github/v/tag/cyrildever/redacted)
![GitHub last commit](https://img.shields.io/github/last-commit/cyrildever/redacted)
![GitHub issues](https://img.shields.io/github/issues/cyrildever/redacted)
![GitHub license](https://img.shields.io/github/license/cyrildever/redacted)
![npm](https://img.shields.io/npm/dw/redacted-ts)
![NPM](https://img.shields.io/npm/l/redacted-ts)

This repository holds the code base for my `redacted` libraries and executables.
It is mainly based off my [Feistel cipher for Format-Preserving Encryption](https://github.com/cyrildever/feistel) to which I added a few tools to handle document, database and file manipulation to ease out the operation.

### Motivation

In some fields (like healthcare for instance), protecting the privacy of data whilst being able to conduct in-depth studies is both vital and mandatory. Redacting documents and databases is therefore the obligatory passage.
With `redacted`, I provide a simple yet secure tool to help redacting documents based on either a dictionary, a record layout or a tag to decide which parts should actually be redacted.

As of the latest version, this repository comes with four different flavours:
* Executables (to use on either Linux, MacOS or Windows environments);
* A Go library;
* A Scala library to use in the JVM (which is not yet available on Maven Central Repository);
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
$ ./redacted -i=myFile.txt -o=myRedactedFile.txt -d=myDictionary.txt -b
```

__IMPORTANT: Do not use with input texts having lines longer than 65536 characters.__

##### <u>Alternative using Java and the redacted JAR</u>

```console
$ java -cp path/to/redacted.jar com.cyrildever.redacted.Main -i=myFile.txt -o=myRedactedFile.txt -d=myDictionary.txt -b
```

#### <u>Alternative using the TypeScript CLI</u>

```console
$ redacted -i myFile.txt -o myRedactedFile.txt -d myDictionary.txt -b
```

@see Installation procedure [here](ts/cli/README.md)

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
redactor := core.NewRedactorWithDictionary(dic, cipher)

// Redact a line
redacted := redactor.Redact(line)
fmt.Println(redacted)

// Expand a redacted line
assert.Equal(t, redactor.Expand(redacted), line)
```
See the [`Dictionary`](model/dictionary.go) and the [`Redactor`](core/redactor.go) implementations to use other kinds of dictionaries (as a slice or from a string) and/or redactors (with or without tag and dictionary).

NB: You may use any other kind of Format-Preserving Encryption library as long as it respects the following interface:
```golang
type FPE interface {
    Decrypt(base256.Readable) (string, error)
    Encrypt(string) (base256.Readable, error)
}
```
_See my implementation of the `base256.Readable` string type alias in its [module](https://github.com/cyrildever/feistel/common/utils/base256)._

To build in 64-bits (after cloning the repository and assuming you are on MacOS):

_(for MacOS)_
```console
$ cd go
$ GOOS=darwin GOARCH=amd64 go build -o bin/redacted main.go
```

_(for Linux)_
```console
$ brew install FiloSottile/musl-cross/musl-cross --with-arm
$ git clone https://github.com/cyrildever/redacted.git && cd redacted/go
$ CGO_ENABLED=1 GOOS=linux GOARCH=amd64 CC="x86_64-linux-musl-gcc" go build -o bin/redacted-linux --ldflags '-w -linkmode external -extldflags "-static"' main.go
```
&ensp;&ensp;&ensp;_@see [https://github.com/FiloSottile/homebrew-musl-cross](https://github.com/FiloSottile/homebrew-musl-cross)_

_(for Windows)_
```console
$ brew install mingw-w64
$ git clone https://github.com/cyrildever/redacted.git && cd redacted/go
$ CGO_ENABLED=1 GOOS=windows GOARCH=amd64 CC="x86_64-w64-mingw32-gcc" go build -o bin/redacted.exe main.go
```


<u>Scala</u>

In a Scala 2.12 project:
```sbt
libraryDependencies ++= Seq(
    "com.cyrildever" %% "feistel-jar" % "1.5.0",
    "com.cyrildever" %% "redacted" % "0.4.0"
)
```

```scala
import com.cyrildever.feistel.common.utils.hash.Engine._
import com.cyrildever.feistel.Feistel
import com.cyrildever.redacted.core.Redactor

val source = "Some text ~tagged or using words in a dictionary"

val cipher = Feistel.FPECipher(SHA_256, key, 10)
val redactor = Redactor(dictionary, tag, cipher, true)
val redacted = redactor.redact(source)

val expanded = redactor.expand(redacted)
assert(expanded == source)
```

_NB: You might need to provide the expected BouncyCastle JAR file, eg. `bcprov-jdk15to18-1.72.jar`._


<u>TypeScript/JavaScript</u>

```console
npm install redacted-ts
```

```typescript
import { DefaultRedactor, Dictionary } from 'redacted-ts'
import { FPECipher, SHA_256 } from 'feistel-cipher'

const source = 'Some text ~tagged or using words in a dictionary'

const cipher = new FPECipher(SHA_256, key, 10)
const redactor = DefaultRedactor(cipher)
const redacted = redactor.redact(source)

const expanded = redactor.expand(redacted)
assert(expanded === source)

const cleansed = redactor.clean(expanded)
assert(cleansed === 'Some text tagged or using words in a dictionary')
```

### License

The use of the `redacted` libraries and executables are subject to fees for commercial purpose and to the respect of the [BSD-2-Clause-Patent license](LICENSE).
Please [contact me](mailto:cdever@pep-s.com) to get further information.


<hr />
&copy; 2021-2023 Cyril Dever. All rights reserved.