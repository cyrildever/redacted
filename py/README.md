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
pip install redacted-py
```

```python
from redacted import DefaultRedactor, Dictionary
from feistel.fpe import FPECipher
from feistel.utils.hash import SHA_256

source = "Some text ~tagged or using words in a dictionary"

cipher = FPECipher(SHA_256, key, 10)
redactor = DefaultRedactor(cipher)
redacted = redactor.redact(source)

expanded = redactor.expand(redacted)
assert expanded == source, "Original data should equal ciphered then deciphered data"

cleansed = redactor.clean(expanded)
assert cleansed == "Some text tagged or using words in a dictionary", "Cleaning should remove any tag mark"
```


### License

The use of the `redacted` libraries and executables are subject to fees for commercial purpose and to the respect of the [BSD-2-Clause-Patent license](LICENSE). \
Please [contact me](mailto:cdever@pep-s.com) to get further information.

_NB: It is still under development so use in production at your own risk for now._


<hr />
&copy; 2024 Cyril Dever. All rights reserved.