import re
from feistel import FPECipher


from redacted import Dictionary, DEFAULT_TAG


class Redactor:
    """
    Parent class for redactors
    """

    def __init__(self, dictionary: Dictionary, tag: str, cipher: FPECipher, both: bool):
        self.dictionary = dictionary
        self.tag = tag
        self.cipher = cipher
        self.both = both

    def redact(self, line: str, *delimiters) -> str:
        """
        Returns the ciphered version of the passed input data
        """
        actual_delimiters = "\\s" + "".join(
            [
                (
                    delim
                    if delim != " "
                    and delim != "\t"
                    and delim != "\n"
                    and delim != "\\s"
                    else ""
                )
                for delim in delimiters
            ]
        )
        words = re.split(actual_delimiters, line)
        tokens = list[str]()
        for word in words:
            if self.both or not self.dictionary.is_empty():
                if self.dictionary.contains(word):
                    tokens.append(self.cipher.encrypt(word))
                else:
                    if self.tag and word.startswith(self.tag):
                        tokens.append(
                            self.tag + self.cipher.encrypt(word[len(self.tag) :])
                        )
                    else:
                        tokens.append(word)
            else:
                if (
                    not self.both
                    and self.dictionary.is_empty()
                    and self.tag
                    and word.startswith(self.tag)
                ):
                    tokens.append(self.tag + self.cipher.encrypt(word[len(self.tag) :]))
                else:
                    tokens.append(word)

        return " ".join(tokens)

    def expand(self, line: str) -> str:
        """
        Returns the deciphered version of the passed input data
        """
        words = list[str]()
        for word in line.split(" "):
            if self.both or not self.dictionary.is_empty():
                if self.tag and word.startswith(self.tag):
                    words.append(self.tag + self.cipher.decrypt(word[len(self.tag) :]))
                else:
                    if not word.strip():
                        # Avoid using new lines
                        continue
                    else:
                        deciphered = self.cipher.decrypt(word)
                        if deciphered and self.dictionary.contains(deciphered):
                            words.append(deciphered)
                        else:
                            words.append(word)
            else:
                if not self.both and self.dictionary.is_empty():
                    if self.tag and word.startswith(self.tag):
                        words.append(
                            self.tag + self.cipher.decrypt(word[len(self.tag) :])
                        )
                    else:
                        words.append(word)
                else:
                    words.append(word)

        return " ".join(words)

    def clean(self, string: str) -> str:
        """
        Removes any delimiters from the passed string
        """
        if self.tag:
            return string.replace(self.tag, "")
        else:
            return string


class DefaultRedactor(Redactor):
    def __init__(self, cipher: FPECipher):
        super().__init__(Dictionary([]), DEFAULT_TAG, cipher, False)


class RedactorWithDictionary(Redactor):
    def __init__(self, dictionary: Dictionary, cipher: FPECipher):
        super().__init__(dictionary, "", cipher, False)


class RedactorWithTag(Redactor):
    def __init__(self, tag: str, cipher: FPECipher):
        super().__init__(Dictionary([]), tag, cipher, False)
