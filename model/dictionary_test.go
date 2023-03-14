package model_test

import (
	"testing"

	"github.com/cyrildever/redacted/model"
	"gotest.tools/assert"
)

// TestDictionary ...
func TestDictionary(t *testing.T) {
	str := "Cyril Antoine Laurent,Dever"
	dic := model.StringToDictionary(str)
	assert.Equal(t, dic.Len(), 3)
	assert.Assert(t, dic.Contains("Dever") == false)
	dic = model.StringToDictionary(str, " ", ",")
	assert.Equal(t, dic.Len(), 4)
	assert.Assert(t, dic.Contains("Dever") == true)
	assert.Assert(t, dic.IsEmpty() == false)
	assert.Assert(t, dic.NonEmpty() == true)
	assert.Equal(t, dic.String(), "Cyril Antoine Laurent Dever")

	dic2 := model.Dictionary([]string{"Cyril", "Antoine", "Laurent", "Dever"})
	assert.DeepEqual(t, dic, dic2)

	dic, err := model.FileToDictionary("../dictionaryExample.txt")
	if err != nil {
		t.Fatal(err)
	}
	assert.Equal(t, dic.Len(), 5)
	assert.Equal(t, dic.String(), "M. Cyril Antoine Laurent Dever")
}
