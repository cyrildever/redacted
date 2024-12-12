#!/usr/bin/env node

import { Command } from 'commander'
import { Engine, FPECipher, isAvailableEngine, SHA_256 } from 'feistel-cipher'
import figlet from 'figlet'
import { appendFileSync, createReadStream, existsSync, readFileSync } from 'fs'
import * as readline from 'readline'
import {
  DefaultRedactor, DEFAULT_TAG, Dictionary, Redactor, RedactorWithDictionary, RedactorWithTag, stringToDictionary
} from 'redacted-ts'

const DEFAULT_KEY = 'd51e1d9a9b12cd88a1d232c1b8730a05c8a65d9706f30cdb8e08b9ed4c7b16a0'


console.log(figlet.textSync('Redacted'))

// Define CLI
const program = new Command()

// TODO Make it programmatic
const version = process.env.npm_package_version || '1.0.10' // eslint-disable-line @typescript-eslint/strict-boolean-expressions

program
  .version(version)
  .description('A TypeScript-based CLI to redacting classified documents')
  .option('-b, --both', 'add to use both dictionary and tag')
  .option('-d, --dictionary <value>', 'the optional path to the dictionary of words to redact')
  .option('-H, --hash <value>', 'the hash engine for the round function (default "sha-256")')
  .option('-i, --input <value>', 'the path to the document to be redacted')
  .option('-k, --key <value>', 'the optional key for the FPE scheme (leave it empty to use default)')
  .option('-o, --output <value>', 'the name of the output file')
  .option('-r, --rounds <value>', 'the number of rounds for the Feistel cipher (default 10)')
  .option('-t, --tag <value>', 'the optional tag that prefixes words to redact (default "~")')
  .option('-x, --expand', 'add to expand a redacted document')
  .parse(process.argv)

// Parse options
const options = program.opts()

const both = options.both === true
const dictionary = options.dictionary !== undefined ? stringToDictionary(readFileSync(options.dictionary as string, 'utf-8')) : ''
const hashEngine = options.hash !== undefined && isAvailableEngine(options.hash as Engine) ? options.hash as string : SHA_256
const input = options.input !== undefined ? options.input as string : ''
const key = options.key !== undefined ? options.key as string : DEFAULT_KEY
const output = options.output !== undefined ? options.output as string : ''
const rounds = options.rounds !== undefined ? parseInt(options.rounds) : 10 // eslint-disable-line @typescript-eslint/no-unsafe-argument
let tag = options.tag !== undefined && (options.tag as string).length === 1 ? options.tag as string : ''
const expand = options.expand === true

// Check params
if (input === '' || output === '') {
  console.error('\x1b[31m%s\x1b[0m', 'Invalid input and/or output paths')
  console.log('===')
  program.outputHelp()
} else if (both && dictionary === '') {
  console.error('\x1b[31m%s\x1b[0m', 'Missing either dictionary path or tag')
  console.log('===')
  program.outputHelp()
} else if (existsSync(output)) {
  console.error('\x1b[31m%s\x1b[0m', 'Output file already exists!')
  console.log('===')
  program.outputHelp()
} else {
  // Prepare cipher
  const cipher = new FPECipher(hashEngine, key, rounds)

  // Instantiate redactor
  if (both && tag === '') {
    tag = DEFAULT_TAG
  }
  const redactor = both
    ? Redactor(dictionary as Dictionary, tag, cipher, both)
    : dictionary !== ''
      ? RedactorWithDictionary(dictionary, cipher)
      : tag !== ''
        ? RedactorWithTag(tag, cipher)
        : DefaultRedactor(cipher)

  // Process redaction or expansion
  if (expand) {
    console.log('Expanding and cleaning document...')

    const lineReader = readline.createInterface({
      input: createReadStream(input),
      terminal: false
    })
    lineReader.on('line', (line) => {
      appendFileSync(output, redactor.clean(redactor.expand(line)))
    })
  } else {
    console.log('Redacting document...')

    const lineReader = readline.createInterface({
      input: createReadStream(input),
      terminal: false
    })
    lineReader.on('line', (line) => {
      appendFileSync(output, redactor.redact(line))
    })
  }
  console.log('Done!')
}
