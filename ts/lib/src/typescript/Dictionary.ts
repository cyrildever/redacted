export interface Dictionary {
  words: ReadonlyArray<string>

  contains: (word: string) => boolean
  isEmpty: () => boolean
  length: () => number
  toString: () => string
}

const contains = (dictionary: ReadonlyArray<string>) => (word: string): boolean =>
  dictionary.includes(word) || word.endsWith('\'s') && dictionary.includes(word.substr(0, word.length - 2)) || dictionary.includes(word.replace(/[^a-zA-Z0-9]/g, ''))

export const Dictionary = (words: ReadonlyArray<string>): Dictionary => ({
  words,
  contains: contains(words),
  isEmpty: () => words.length === 0,
  length: () => words.length,
  toString: () => words.join(' ')
})

export const stringToDictionary = (str: string, ...delimiters: ReadonlyArray<string>): Dictionary => {
  if (delimiters.length === 0) {
    delimiters = [' ']
  }
  let dic = [str]
  delimiters.forEach(delim => {
    dic = dic.map(word => word.split(delim)).flatMap(_ => _)
  })
  return Dictionary(dic)
}
