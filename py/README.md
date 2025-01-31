# redacted-py
_Redacting classified documents_

![GitHub tag (latest by date)](https://img.shields.io/github/v/tag/cyrildever/redacted)
![GitHub last commit](https://img.shields.io/github/last-commit/cyrildever/redacted)
![GitHub issues](https://img.shields.io/github/issues/cyrildever/redacted)
![GitHub license](https://img.shields.io/github/license/cyrildever/redacted)
![PyPI - Version](https://img.shields.io/pypi/v/redacted-py)

This repository holds the code base for my `redacted-py` library in Python. \
It is mainly based off my [Feistel cipher for Format-Preserving Encryption](https://github.com/cyrildever/feistel-py) to which I added a few tools to handle document, database and file manipulation to ease out the operation.

### Motivation

In some fields (like healthcare for instance), protecting the privacy of data whilst being able to conduct in-depth studies is both vital and mandatory. Redacting documents and databases is therefore the obligatory passage.
With `redacted-py`, I provide a simple yet secure tool to help redacting documents based on either a dictionary, a record layout or a tag to decide which parts should actually be redacted.


### Usage

You can use either a dictionary or a tag (or both) to identify the words you want to redact in a document.
The tag should be placed before any word that should be redacted. The default tag is the tilde character (`~`).

For example, the following sentence will only see the word `tagged` redacted: `"This is a ~tagged sentence"`.

```console
$ pip install redacted-py
```

```python
from redacted import DefaultRedactor, Dictionary
from feistel import FPECipher, SHA_256

source = "Some text ~tagged or using words in a dictionary"

cipher = FPECipher(SHA_256, key, 10)
redactor = DefaultRedactor(cipher)
redacted = redactor.redact(source)

expanded = redactor.expand(redacted)
assert expanded == source, "Original data should equal ciphered then deciphered data"

cleansed = redactor.clean(expanded)
assert cleansed == "Some text tagged or using words in a dictionary", "Cleaning should remove any tag mark"
```

You may also use it in the console with the following command line instructions:
```
usage: python3 -m redacted [-h] [-b | --both | --no-both] [-d DICTIONARY] [-H HASH] [-i INPUT] [-k KEY] [-o OUTPUT] [-r ROUNDS] [-t TAG] [-x | --expand | --no-expand]

options:
  -h, --help            show this help message and exit
  -b, --both, --no-both
                        Add to use both dictionary and tag
  -d DICTIONARY, --dictionary DICTIONARY
                        The optional path to the dictionary of words to redact
  -H HASH, --hash HASH  The hash engine for the round function [default sha-256]
  -i INPUT, --input INPUT
                        The path to the document to be redacted
  -k KEY, --key KEY     The optional key for the FPE scheme (leave it empty to use default)
  -o OUTPUT, --output OUTPUT
                        The name of the output file
  -r ROUNDS, --rounds ROUNDS
                        The number of rounds for the Feistel cipher [default 10]
  -t TAG, --tag TAG     The optional tag that prefixes words to redact [default ~]
  -x, --expand, --no-expand
                        Add to expand a redacted document
```


### Tests

```console
$ git clone https://github.com/cyrildever/redacted.git
$ cd redacted/py/
$ pip install -e .
$ python3 -m unittest discover
```


### License

The use of the `redacted` libraries and executables are subject to fees for commercial purpose and to the respect of the [BSD-2-Clause-Patent license](LICENSE). \
Please [contact me](mailto:cdever@pep-s.com) to get further information.

_NB: It is still under development so use in production at your own risk for now._


<hr />
&copy; 2024-2025 Cyril Dever. All rights reserved.