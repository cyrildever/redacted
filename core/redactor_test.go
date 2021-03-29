package core_test

import (
	"testing"

	"github.com/cyrildever/feistel"
	"github.com/cyrildever/feistel/common/utils/hash"
	"github.com/cyrildever/redacted/core"
	"github.com/cyrildever/redacted/model"
	"gotest.tools/assert"
)

// TestRedactor ...
func TestRedactor(t *testing.T) {
	dic := model.Dictionary([]string{"M.", "Cyril", "Antoine", "Laurent", "Dever"})
	ref := "B6ds. is testing ¼= Du:,l26 library while ¾.=y£|v Izizb is listening to Âvhis*l<"

	txt := "Cyril is testing M. Dever's library while Antoine Dever is listening to Laurent."
	redactor := core.NewRedactorWithDictionary(dic, feistel.NewFPECipher(hash.SHA_256, "1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef", 10))
	redacted := redactor.Redact(txt)
	assert.Equal(t, redacted, ref)
	expanded := redactor.Expand(redacted)
	assert.Equal(t, expanded, txt)

	blake2 := "¸lk€$ is testing F: B!@x7;1 library while Cs>v0'* ¹'90< is listening to Pz2;ws?o"
	redacted = core.NewRedactorWithDictionary(dic, feistel.NewFPECipher(hash.BLAKE2b, "1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef", 10)).Redact(txt)
	assert.Equal(t, blake2, redacted)
	assert.Assert(t, redacted != ref)

	keccak := "H1i,{ is testing ½5 ¿&bv8f8 library while ¸&7+r$u ¹|6'h is listening to Å€j;$\"4<"
	redacted = core.NewRedactorWithDictionary(dic, feistel.NewFPECipher(hash.KECCAK, "1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef", 10)).Redact(txt)
	assert.Equal(t, keccak, redacted)
	assert.Assert(t, redacted != ref)
}
