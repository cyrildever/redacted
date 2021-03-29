package core

import (
	"regexp"
	"strings"

	"github.com/cyrildever/feistel"
	"github.com/cyrildever/feistel/common/utils/base256"
	"github.com/cyrildever/redacted/model"
)

//--- TYPES

// Redactor ...
type Redactor struct {
	Dictionary model.Dictionary
	Tag        string
	Cipher     *feistel.FPECipher
}

//--- METHODS

// Redact ...
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
		if r.Dictionary.Contains(word) {
			obfuscated, err := r.Cipher.Encrypt(word)
			if err != nil {
				obfuscated = base256.ToBase256Readable([]byte(word))
			}
			outputs = append(outputs, obfuscated.String())
		} else {
			outputs = append(outputs, word)
		}
	}
	return strings.Join(outputs, " ")
}

// Expand ...
func (r Redactor) Expand(line string, delimiters ...string) string {
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
		if deciphered, err := r.Cipher.Decrypt(base256.Readable(word)); err == nil {
			if r.Dictionary.Contains(deciphered) {
				outputs = append(outputs, deciphered)
			} else {
				outputs = append(outputs, word)
			}
		} else {
			outputs = append(outputs, word)
		}
	}
	return strings.Join(outputs, " ")
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
