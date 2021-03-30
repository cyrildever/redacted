import * as chai from 'chai'
chai.should()
import 'mocha'

import { Dictionary, stringToDictionary } from '../../../lib/src/typescript'

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
