package main

import (
	"bufio"
	"flag"
	"os"
	"time"

	"github.com/cyrildever/feistel"
	"github.com/cyrildever/feistel/common/utils/hash"
	"github.com/cyrildever/go-utls/common/logger"
	"github.com/cyrildever/redacted/core"
	"github.com/cyrildever/redacted/model"
)

// TODO in config
const (
	DEFAULT_KEY    = "d51e1d9a9b12cd88a1d232c1b8730a05c8a65d9706f30cdb8e08b9ed4c7b16a0"
	DEFAULT_ROUNDS = 10
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
//	`$ ./redacted -i=myFile.txt -o=myRedactedFile.txt -d=myDictionary.txt -t="§§" -b`
//	`$ go build && ./redacted -i=exampleInput.txt -o=exampleOutput.txt -d dictionaryExample.txt -t="~" -b`
// - to expand a redacted document:
//	`$ ./redacted -x -i=myRedactedFile.txt -o=myRecoveredFile.txt -d=myDictionary.txt -t="§§"
//	`$ go build && ./redacted -x -i=exampleOutput.txt -o=exampleRecovery.txt -d dictionaryExample.txt -t="~" -b`
func main() {
	log := logger.Init("main", "application")
	t0 := time.Now()

	// Read arguments
	expand := flag.Bool("x", false, "add to expand a redacted document")
	input := flag.String("i", "", "the path to the document to be redacted")
	output := flag.String("o", "", "the name of the output file")
	dic := flag.String("d", "", "the optional path to the dictionary of words to redact")
	tag := flag.String("t", "~", "the optional tag that prefixes words to redact")
	useBoth := flag.Bool("b", false, "add to use both dictionary and tag")
	key := flag.String("k", "", "the optional key for the FPE scheme (leave it empty to use default)")
	rounds := flag.Int("r", 10, "the number of rounds for the Feistel cipher")
	engine := flag.String("h", string(hash.SHA_256), "the hash engine for the round function")

	flag.Parse()

	if *input == "" || *output == "" {
		log.Crit("Input and output file paths are mandatory")
		os.Exit(1)
		return
	}
	if *tag == "" && *dic == "" {
		log.Crit("Use to set either a tag or a dictionary")
		os.Exit(1)
		return
	}
	if *useBoth && (*tag == "" || *dic == "") {
		log.Crit("Tag and dictionary must be set if you want to use them both")
		os.Exit(1)
		return
	}
	hashEngine := hash.Engine(*engine)
	if *engine != "" && !hash.IsAvailableEngine(hashEngine) {
		log.Error("Wrong hash engine: default SHA-256 will be used instead!")
		hashEngine = hash.SHA_256
	}
	steadyKey := *key
	if steadyKey == "" {
		steadyKey = DEFAULT_KEY
	}
	feistelRounds := *rounds
	if feistelRounds < 2 {
		log.Error("Not enough rounds: default 10 will be used instead!")
		feistelRounds = DEFAULT_ROUNDS
	}

	msg := "Start redacting..."
	if *expand {
		msg = "Start expanding..."
	}
	log.Info(msg, "input", *input, "output", *output, "dictionary", *dic, "tag", *tag, "useBoth", *useBoth, "expand", *expand, "engine", hashEngine, "rounds", *rounds)

	// Prepare processing
	var dictionary model.Dictionary
	if *dic != "" {
		d, err := model.FileToDictionary(*dic)
		if err != nil && *useBoth {
			log.Crit("Unable to build dictionary", "error", err)
			os.Exit(1)
			return
		}
		dictionary = d
	}

	// TODO Handle unsteadiness
	cipher := feistel.NewFPECipher(hashEngine, steadyKey, feistelRounds)
	var redactor *core.Redactor
	if *useBoth {
		redactor = core.NewRedactor(dictionary, *tag, cipher)
	} else if dictionary.NonEmpty() {
		redactor = core.NewRedactorWithDictionary(dictionary, cipher)
	} else {
		redactor = core.NewRedactorWithTag(*tag, cipher)
	}

	// Read input
	f, err := os.Open(*input)
	if err != nil {
		log.Crit("Impossible to read input file", "error", err)
		os.Exit(1)
		return
	}
	defer f.Close()
	scanner := bufio.NewScanner(f)

	// Prepare output file
	out, err := os.Create(*output)
	if err != nil {
		log.Crit("Impossible to write to output file", "error", err)
		os.Exit(1)
		return
	}
	defer out.Close()

	// Do process
	if !*expand {
		for scanner.Scan() {
			line := scanner.Text()
			redactedLine := redactor.Redact(line)
			if _, err = out.WriteString(redactedLine + "\n"); err != nil {
				log.Error("An error occurred while writing to the output file", "error", err)
			}
		}
		if err := scanner.Err(); err != nil {
			log.Error("An error occurred while reading the input file", "error", err)
		}
		log.Info("Redacted over. Everything went well ;-)", "elapsed", time.Since(t0).Milliseconds())
		os.Exit(0)
	} else {
		for scanner.Scan() {
			line := scanner.Text()
			expandedLine := redactor.Expand(line)
			if _, err = out.WriteString(expandedLine + "\n"); err != nil {
				log.Error("An error occurred while writing to the output file", "error", err)
			}
		}
		if err := scanner.Err(); err != nil {
			log.Error("An error occurred while reading the input file", "error", err)
		}
		log.Info("Expansion over. Everything went well ;-)", "elapsed", time.Since(t0).Milliseconds())
		os.Exit(0)
	}
}
