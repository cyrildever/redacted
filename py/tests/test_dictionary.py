from unittest import TestCase

from redacted import Dictionary, file2Dictionary, string2Dictionary


class TestDictionary(TestCase):
    def test_string2Dictionary(self):
        string = "Cyril Antoine Laurent,Dever"
        dic = string2Dictionary(string)
        self.assertEqual(dic.length(), 3)
        self.assertFalse(dic.contains("Dever"))
        dic = string2Dictionary(string, " ", ",")
        self.assertEqual(dic.length(), 4)
        self.assertTrue(dic.contains("Dever"))
        self.assertFalse(dic.is_empty())
        self.assertEqual(dic.to_string(), "Cyril Antoine Laurent Dever")

        dic2 = Dictionary(["Cyril", "Antoine", "Laurent", "Dever"])
        self.assertEqual(dic, dic2)

        dic = file2Dictionary("./tests/dictionaryExample.txt")
        self.assertEqual(dic.length(), 5)
        self.assertEqual(dic.to_string(), "M. Cyril Antoine Laurent Dever")
