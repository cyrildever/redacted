export interface Dictionary {
  words: ReadonlyArray<string>

  contains: (word: string) => boolean
  isEmpty: () => boolean
  length: () => number
  toString: () => string
}

const contains = (dictionary: ReadonlyArray<string>) => (word: string): boolean =>
  dictionary.includes(word)

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
