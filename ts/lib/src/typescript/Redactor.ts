import * as feistel from 'feistel-cipher'

import { Dictionary } from './Dictionary'

export const DEFAULT_TAG = '~'

export interface Redactor {
  dictionary: Dictionary
  tag: string
  cipher: feistel.FPECipher
  both: boolean

  redact: (line: string, ...delimiters: ReadonlyArray<string>) => string
  expand: (line: string) => string
  clean: (str: string) => string
}

export const redact = (dictionary: Dictionary, tag: string, cipher: feistel.FPECipher, both: boolean) => (line: string, ...delimiters: ReadonlyArray<string>): string =>
  line.split(new RegExp('\\s' + delimiters.map(delim => delim !== ' ' && delim !== '\t' && delim !== '\n' && delim !== '\\s' ? delim : '').join(''))).map(word =>
    both || !dictionary.isEmpty()
      ? dictionary.contains(word)
        ? cipher.encrypt(word)
        : tag !== '' && word.startsWith(tag)
          ? tag + cipher.encrypt(word.substr(tag.length))
          : word
      : !both && dictionary.isEmpty() && tag !== '' && word.startsWith(tag)
        ? tag + cipher.encrypt(word.substr(tag.length))
        : word
  ).join(' ')

export const expand = (dictionary: Dictionary, tag: string, cipher: feistel.FPECipher, both: boolean) => (line: string): string =>
  line.split(' ').map(word =>
    both || !dictionary.isEmpty()
      ? tag !== '' && word.startsWith(tag)
        ? tag + cipher.decrypt(word.substr(tag.length))
        : dictionary.contains(cipher.decrypt(word))
          ? cipher.decrypt(word)
          : word
      : !both && dictionary.isEmpty()
        ? tag !== '' && word.startsWith(tag)
          ? tag + cipher.decrypt(word.substr(tag.length))
          : word
        : word
  ).join(' ')

export const clean = (tag: string) => (str: string): string =>
  tag !== ''
    ? str.replace(new RegExp(tag, 'g'), '')
    : str

export const Redactor = (dictionary: Dictionary, tag: string, cipher: feistel.FPECipher, both: boolean): Redactor => ({
  dictionary,
  tag,
  cipher,
  both: both || !dictionary.isEmpty() && tag !== '',
  redact: redact(dictionary, tag, cipher, both),
  expand: expand(dictionary, tag, cipher, both),
  clean: clean(tag)
})

export const RedactorWithDictionary = (dictionary: Dictionary, cipher: feistel.FPECipher): Redactor =>
  Redactor(dictionary, '', cipher, false)

export const RedactorWithTag = (tag: string, cipher: feistel.FPECipher): Redactor =>
  Redactor(Dictionary([]), tag, cipher, false)

export const DefaultRedactor = (cipher: feistel.FPECipher): Redactor =>
  Redactor(Dictionary([]), DEFAULT_TAG, cipher, false)
