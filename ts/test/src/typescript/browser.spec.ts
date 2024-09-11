import * as feistel from 'feistel-cipher'
import { BLAKE2b, KECCAK, SHA_256 } from 'feistel-cipher/dist/lib/src/typescript/utils/hash'

import { DefaultRedactor, Dictionary, RedactorWithDictionary, RedactorWithTag, stringToDictionary } from '../../../lib/src/typescript'

/* eslint-disable @typescript-eslint/no-unused-expressions */
describe('Dictionary', () => {
  it('should build equivalent instances with either a string or an array', () => {
    const str = 'Cyril Antoine Laurent,Dever'
    let dic = stringToDictionary(str)
    dic.length().should.equal(3)
    dic.contains('Dever').should.be.false
    dic = stringToDictionary(str, ' ', ',')
    dic.length().should.equal(4)
    dic.contains('Dever').should.be.true
    dic.isEmpty().should.be.false
    dic.toString().should.equal('Cyril Antoine Laurent Dever')

    const dic2 = Dictionary(['Cyril', 'Antoine', 'Laurent', 'Dever'])
    dic2.words.should.eqls(dic.words)
  })
})
describe('Redactor', () => {
  const sha256Cipher = new feistel.FPECipher(SHA_256, '1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef', 10)
  const txt = `Cyril is testing M. Dever's library while Antoine Dever is listening to Laurent.` // eslint-disable-line quotes

  describe('RedactorWithDictionary', () => {
    it('should be deterministic', () => {
      const expected = 'B6ds. is testing ¼= Du:,l26 library while ¾.=y£|v Izizb is listening to Âvhis*l<'
      const dic = Dictionary(['M.', 'Cyril', 'Antoine', 'Laurent', 'Dever'])
      let redactor = RedactorWithDictionary(dic, sha256Cipher)
      let found = redactor.redact(txt)
      found.should.equal(expected)

      const blake2 = `¸lk€$ is testing F: B!@x7;1 library while Cs>v0'* ¹'90< is listening to Pz2;ws?o` // eslint-disable-line quotes
      const blake2Cipher = new feistel.FPECipher(BLAKE2b, '1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef', 10)
      redactor = RedactorWithDictionary(dic, blake2Cipher)
      found = redactor.redact(txt)
      found.should.equal(blake2)

      const keccak = `H1i,{ is testing ½5 ¿&bv8f8 library while ¸&7+r$u ¹|6'h is listening to Å€j;$"4<` // eslint-disable-line quotes
      const keccakCipher = new feistel.FPECipher(KECCAK, '1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef', 10)
      redactor = RedactorWithDictionary(dic, keccakCipher)
      found = redactor.redact(txt)
      found.should.equal(keccak)
    })
  })
  describe('RedactorWithTag', () => {
    it('should be deterministic', () => {
      const expected = '~B6ds. is testing ~¼= ~Du:,l26 library while ~¾.=y£|v ~Izizb is listening to ~Âvhis*l<'
      const txt2 = `~Cyril is testing ~M. ~Dever's library while ~Antoine ~Dever is listening to ~Laurent.` // eslint-disable-line quotes
      let redactor = DefaultRedactor(sha256Cipher)
      const redacted = redactor.redact(txt2)
      redacted.should.equal(expected)
      const expanded = redactor.expand(redacted)
      expanded.should.equal(txt2)

      const cleansed = redactor.clean(expanded)
      cleansed.should.equal(txt)

      const txt3 = `§ §§Cyril is testing §§M. §§Dever's library while §§Antoine §§Dever is listening to §§Laurent.` // eslint-disable-line quotes
      redactor = RedactorWithTag('§§', sha256Cipher)
      const found = redactor.clean(redactor.expand(redactor.redact(txt3)))
      found.should.equal('§ ' + txt)
    })
  })
})
/* eslint-enable @typescript-eslint/no-unused-expressions */
