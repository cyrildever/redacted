package main

import (
	"flag"

	"github.com/cyrildever/go-utls/common/logger"
)

// Usage
//
// For document processing:
//
// - to use a dictionary:
//	`$ ./redacted -i=myFile.txt -o=myRedactedFile.txt -d=myDictionary.txt`
// - to use a tag prefix:
//	`$ ./redacted -i=myFile.txt -o=myRedactedFile.txt`
//	`$ ./redacted -i=myFile.txt -o=myRedactedFile.txt -t="§§"`
// - to use them both:
//	`$ ./redacted -i=myFile.txt -o=myRedactedFile.txt -d=myDictionary.txt  -t="§§" -b`
func main() {
	log := logger.Init("main", "application")

	// Read arguments
	input := flag.String("i", "", "the path to the document to be redacted")
	output := flag.String("o", "", "the name of the output file")
	dic := flag.String("d", "", "the optional path to the dictionary of words to redact")
	tag := flag.String("t", "~", "the optional tag that prefixes words to redact")
	useBoth := flag.Bool("b", false, "add to use both dictionary and tag")
	flag.Parse()
	if *input == "" || *output == "" {
		log.Crit("Input and output file paths are mandatory")
		return
	}
	if *tag == "" && *dic == "" {
		log.Crit("Use to set either a tag or a dictionary")
		return
	}
	if *useBoth && (*tag == "" || *dic == "") {
		log.Crit("Tag and dictionary must be set if you want to use them both")
		return
	}
	log.Info("Start redacting...", "input", *input, "output", *output, "dictionary", *dic, "tag", *tag, "useBoth", *useBoth)
}
