package model

import (
	"io/ioutil"
	"regexp"
	"strings"
)

//--- TYPES

// Dictionary ...
type Dictionary []string

//--- METHODS

var apostropheS = regexp.MustCompile("'s$")
var punctuation = regexp.MustCompile("[^a-zA-Z0-9]")

// Contains ...
func (d Dictionary) Contains(word string) bool {
	couldBeApostropheS := apostropheS.MatchString(word)
	stripped := punctuation.ReplaceAllString(word, "")
	for _, item := range d {
		if item == word {
			return true
		} else if couldBeApostropheS && item+"'s" == word {
			return true
		} else if stripped == item {
			return true
		}
	}
	return false
}

// IsEmpty ...
func (d Dictionary) IsEmpty() bool {
	if len(d) == 0 {
		return true
	}
	for _, item := range d {
		if item != "" {
			return false
		}
	}
	return true
}

// NonEmpty ...
func (d Dictionary) NonEmpty() bool {
	return !d.IsEmpty()
}

// Len ...
func (d Dictionary) Len() int {
	return len(d)
}

// String ...
func (d Dictionary) String() string {
	return strings.Join(d, " ")
}

//--- FUNCTIONS

// FileToDictionary ...
func FileToDictionary(path string) (d Dictionary, err error) {
	content, err := ioutil.ReadFile(path)
	if err != nil {
		return
	}
	return StringToDictionary(string(content)), nil
}

// StringToDictionary creates a dictionary from the passed string using a list of delimiters (or a space if none provided)
func StringToDictionary(str string, delimiters ...string) Dictionary {
	if len(delimiters) == 0 {
		delimiters = []string{" "}
	}
	dic := []string{str}
	for _, delim := range delimiters {
		var tmp []string
		for _, word := range dic {
			tmp = append(tmp, strings.Split(word, delim)...)
		}
		dic = tmp
	}
	return Dictionary(dic)
}
