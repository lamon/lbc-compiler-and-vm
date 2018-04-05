package br.com.lbc

class ObjectCodeGenerator {

    SimpleResult result

    ObjectCodeGenerator() {
        this.result = new SimpleResult()
    }

    SimpleResult generate(tokenizedLines) {

        result.value = []

        tokenizedLines.each { line ->
            result.value << decodeLine(line)
        }

        return result
    }

    static decodeLine(tokenizedLine) {

        def decodedLine = ""

        tokenizedLine.each { symbol ->
            try {
                decodedLine += Symbols.valueOf(symbol).bitsAsString
            } catch (IllegalArgumentException e) {
                if (symbol?.toString()?.startsWith("0b")) {
                    decodedLine += symbol.toString().replaceAll("0b", "")
                }
            }

        }

        return decodedLine
    }

}
