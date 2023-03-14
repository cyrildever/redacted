package core

import (
	"regexp"
	"strings"

	"github.com/cyrildever/feistel"
	"github.com/cyrildever/feistel/common/utils/base256"
	"github.com/cyrildever/redacted/go/model"
)

const DEFAULT_TAG = "~"

//--- TYPES

// Redactor ...
type Redactor struct {
	Dictionary model.Dictionary
	Tag        string
	Cipher     *feistel.FPECipher
	both       bool
}

//--- METHODS

// Redact transforms the passed line
func (r Redactor) Redact(line string, delimiters ...string) string {
	var actualDelimiters []string
	for _, delim := range delimiters {
		if delim != " " && delim != "\t" && delim != "\n" {
			actualDelimiters = append(actualDelimiters, delim)
		}
	}
	if len(actualDelimiters) == 0 {
		actualDelimiters = append(actualDelimiters, "\\s")
	}
	var outputs []string
	re := regexp.MustCompile(strings.Join(actualDelimiters, ""))
	words := re.Split(line, -1)
	for _, word := range words {
		if r.both || r.Dictionary.NonEmpty() {
			if r.Dictionary.Contains(word) {
				if obfuscated, err := r.Cipher.Encrypt(word); err == nil && obfuscated != "" {
					outputs = append(outputs, obfuscated.String())
					continue
				}
			} else if r.Tag != "" && strings.HasPrefix(word, r.Tag) {
				toRedact := word[len(r.Tag):]
				if obfuscated, err := r.Cipher.Encrypt(toRedact); err == nil && obfuscated != "" {
					outputs = append(outputs, r.Tag+obfuscated.String())
					continue
				}
			}
		} else if !r.both && r.Dictionary.IsEmpty() {
			if r.Tag != "" && strings.HasPrefix(word, r.Tag) {
				toRedact := word[len(r.Tag):]
				if obfuscated, err := r.Cipher.Encrypt(toRedact); err == nil && obfuscated != "" {
					outputs = append(outputs, r.Tag+obfuscated.String())
					continue
				}
			}
		}
		outputs = append(outputs, word)
	}
	return strings.Join(outputs, " ")
}

// Expand deciphers the passed line
func (r Redactor) Expand(line string) string {
	var outputs []string
	re := regexp.MustCompile(`\s`)
	words := re.Split(line, -1)
	for _, word := range words {
		if r.both || r.Dictionary.NonEmpty() {
			toExpand := word
			expanded := false
			if r.Tag != "" && strings.HasPrefix(word, r.Tag) {
				toExpand = word[len(r.Tag):]
				expanded = true
			}
			if deciphered, err := r.Cipher.Decrypt(base256.Readable(toExpand)); err == nil {
				if r.Dictionary.Contains(deciphered) {
					if expanded {
						deciphered = r.Tag + deciphered
					}
					outputs = append(outputs, deciphered)
					continue
				}
				if expanded {
					outputs = append(outputs, r.Tag+deciphered)
					continue
				}
			}

		} else if !r.both && r.Dictionary.IsEmpty() {
			if r.Tag != "" && strings.HasPrefix(word, r.Tag) {
				toExpand := word[len(r.Tag):]
				if deciphered, err := r.Cipher.Decrypt(base256.Readable(toExpand)); err == nil {
					outputs = append(outputs, r.Tag+deciphered)
					continue
				}
			}
		}
		outputs = append(outputs, word)
	}
	return strings.Join(outputs, " ")
}

// Clean remove tags from the passed string
func (r Redactor) Clean(str string) string {
	if r.Tag != "" {
		re := regexp.MustCompile(r.Tag)
		return re.ReplaceAllString(str, "")
	}
	return str
}

//--- FUNCTIONS

// NewRedactor ...
func NewRedactor(dictionary model.Dictionary, tag string, cipher *feistel.FPECipher) (r *Redactor) {
	if dictionary.NonEmpty() {
		if tag != "" {
			r = &Redactor{
				Dictionary: dictionary,
				Tag:        tag,
				Cipher:     cipher,
				both:       true,
			}
		} else {
			r = &Redactor{
				Dictionary: dictionary,
				Cipher:     cipher,
			}
		}
	} else {
		r = &Redactor{
			Tag:    tag,
			Cipher: cipher,
		}
	}
	return
}

// NewRedactorWithDictionary ...
func NewRedactorWithDictionary(dic model.Dictionary, cipher *feistel.FPECipher) *Redactor {
	return NewRedactor(dic, "", cipher)
}

// NewRedactorWithDictionary ...
func NewRedactorWithTag(tag string, cipher *feistel.FPECipher) *Redactor {
	return NewRedactor(model.Dictionary{}, tag, cipher)
}

// NewDefaultRedactor ...
func NewDefaultRedactor(cipher *feistel.FPECipher) *Redactor {
	return NewRedactor(model.Dictionary{}, DEFAULT_TAG, cipher)
}
