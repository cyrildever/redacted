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
* An executable (to use on either Linux, MacOS or Windows environment);
* A Go library;
* A Scala library to use in the JVM;
* The current TypeScript library.


### Usage

You can use either a dictionary or a tag (or both) to identify the words you want to redact in a document.
The tag should be placed before any word that should be redacted. The default tag is the tilde character (`~`).

For example, the following sentence will only see the word `tagged` redacted: `"This is a ~tagged sentence"`.

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

_NB: It is still under development so use in production at your own risk for now._


<hr />
&copy; 2021-2023 Cyril Dever. All rights reserved.