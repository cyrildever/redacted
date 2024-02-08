from unittest import TestCase
from feistel import FPECipher, BLAKE2B, KECCAK, SHA_256


from redacted import Dictionary, DefaultRedactor, RedactorWithDictionary


class TestRedactor(TestCase):
    def test_dictionary_redactor(self):
        dic = Dictionary(["M.", "Cyril", "Antoine", "Laurent", "Dever"])
        ref = "B6ds. is testing ¼= Du:,l26 library while ¾.=y£|v Izizb is listening to Âvhis*l<"

        txt = "Cyril is testing M. Dever's library while Antoine Dever is listening to Laurent."
        redactor = RedactorWithDictionary(
            dic,
            FPECipher(
                SHA_256,
                "1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef",
                10,
            ),
        )
        redacted = redactor.redact(txt)
        self.assertEqual(redacted, ref)
        expanded = redactor.expand(redacted)
        self.assertEqual(expanded, txt)

        blake2 = "¸lk€$ is testing F: B!@x7;1 library while Cs>v0'* ¹'90< is listening to Pz2;ws?o"
        redacted = RedactorWithDictionary(
            dic,
            FPECipher(
                BLAKE2B,
                "1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef",
                10,
            ),
        ).redact(txt)
        self.assertEqual(redacted, blake2)
        self.assertTrue(redacted != ref)

        keccak = "H1i,{ is testing ½5 ¿&bv8f8 library while ¸&7+r$u ¹|6'h is listening to Å€j;$\"4<"
        redacted = RedactorWithDictionary(
            dic,
            FPECipher(
                KECCAK,
                "1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef",
                10,
            ),
        ).redact(txt)
        self.assertEqual(redacted, keccak)
        self.assertTrue(redacted != ref)

    def test_tag_redactor(self):
        ref = "~B6ds. is testing ~¼= ~Du:,l26 library while ~¾.=y£|v ~Izizb is listening to ~Âvhis*l<"

        txt = "~Cyril is testing ~M. ~Dever's library while ~Antoine ~Dever is listening to ~Laurent."
        redactor = DefaultRedactor(
            FPECipher(
                SHA_256,
                "1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef",
                10,
            )
        )
        redacted = redactor.redact(txt)
        self.assertEqual(redacted, ref)
        expanded = redactor.expand(redacted)
        self.assertEqual(expanded, txt)

        cleansed = redactor.clean(expanded)
        self.assertEqual(
            cleansed,
            "Cyril is testing M. Dever's library while Antoine Dever is listening to Laurent.",
        )
