package br.com.lbc

class LexicalAnalyzer {

    SimpleResult result

    LexicalAnalyzer() {
        this.result = new SimpleResult()
    }

    SimpleResult analyze(File sourceFile) {

        result.value = []

        def line
        def lineNumber = 1
        sourceFile.withReader { reader ->
            while ((line = reader.readLine()) != null) {
                result.value << analyzeLine(line, lineNumber)
                lineNumber++
            }
        }

        return result
    }

    def analyzeLine(String line, lineNumber) {

        def lineTokenized = line.tokenize()

        lineTokenized.each { symbol ->
            try {
                Symbols.valueOf(symbol)
            } catch (IllegalArgumentException e) {
                if (!symbol?.toString()?.startsWith("0b")) {
                    result.addError("Invalid symbol at line ${lineNumber}: ${symbol}")
                }
            }
        }

        return lineTokenized
    }

}
