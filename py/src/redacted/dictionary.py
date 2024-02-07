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
        return word in self.words

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


def string2Dictionary(string: str, *delimiters) -> Dictionary:
    """
    Transforms a string into a dictionary (using the optionally passed delimiters [default space])
    """
    if len(delimiters) == 0:
        delimiters = [" "]
    words = list[str]()
    for delimiter in delimiters:
        words.expand(string.split(delimiter))
    return Dictionary(words)
