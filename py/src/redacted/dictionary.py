class Dictionary:
    """
    A Dictionary holds the list of words to be tagged
    """

    def __init__(self, words: list[str]):
        self.words = words

    def contains(self, word: str) -> bool:
        """
        Check whether the passed word is already in the Dictionary
        """
        return (
            word in self.words
            or (word.endswith("'s") and word[: len(word) - 2]) in self.words
            or _remove_punctuation(word.strip()) in self.words
        )

    def is_empty(self) -> bool:
        """
        Returns `True` if there is no word in the Dictionary
        """
        return len(self.words) == 0

    def length(self) -> int:
        """
        Gets the number of words
        """
        return len(self.words)

    def to_string(self) -> str:
        """
        Returns the dictionay as a space-separated list of words
        """
        return " ".join(self.words)

    def __eq__(self, other) -> bool:
        return self.words == other.words


def file2Dictionary(path: str) -> Dictionary:
    """
    Upload the content of a file to a Dictionary
    """
    with open(path, "r") as f:
        data = f.read()

    return string2Dictionary(data)


def string2Dictionary(string: str, *delimiters) -> Dictionary:
    """
    Transforms a string into a dictionary (using the optionally passed delimiters [default space])
    """
    if len(delimiters) == 0:
        delimiters = [" "]

    words = [string]
    for delimiter in delimiters:
        tmp = list[str]()
        for word in words:
            tmp.extend(word.split(delimiter))
        words = tmp

    return Dictionary(words)


def _remove_punctuation(string: str) -> str:
    return (
        string.strip(".")
        .strip(",")
        .strip(":")
        .strip(";")
        .strip("?")
        .strip("!")
        .strip("(")
        .strip(")")
        .strip("-")
        .strip("_")
        .strip("+")
        .strip("/")
        .strip("\\")
    )
