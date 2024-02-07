from unittest import TestCase
from feistel.fpe import FPECipher
from feistel.utils.hash import SHA_256


from redacted.redactor import DefaultRedactor


class TestRedactor(TestCase):
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
