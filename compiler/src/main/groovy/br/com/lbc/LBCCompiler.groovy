package br.com.lbc

class LBCCompiler {

    static sourceCodeFilename
    static compiledCodeFilename

    static void main(String[] args) {

        if (!args) {
            noArgsError()
            return
        }

        if (args.size() < 2) {
            noArgsError()
            return
        }

        sourceCodeFilename = args[0]
        compiledCodeFilename = args[1]

        def sourceFile = new File(sourceCodeFilename)

        // TODO implementar limite de 256 linhas de código

        if (!sourceFile) {
            println "Arquivo não encontrado... :("
        } else {
            def resultFromCompileSource = compileSource(sourceFile)
            if (resultFromCompileSource.hasErrors()) {
                println resultFromCompileSource.toString()
            } else {
                def compiledCodeFile = new File(compiledCodeFilename)
                compiledCodeFile.withWriter('UTF-8') { writer ->
                    resultFromCompileSource.value.each {
                        writer.write(it.toString().padRight(16, "0") + "\n")
                    }
                }
                println "Arquivo gerado: ${compiledCodeFilename}"
            }
        }

    }

    static noArgsError() {
        println "Por favor informe um arquivo fonte .lbc e um nome de arquivo qualquer para o destino"
        println "Exemplo: \$ java -jar lbc-compiler.jar source.lbc destiny.out"
    }

    static compileSource(File sourceFile) {

        def lexicalAnalyzer = new LexicalAnalyzer()

        def resultFromLexicalAnalyzer = lexicalAnalyzer.analyze(sourceFile)
        if (resultFromLexicalAnalyzer.hasErrors()) {
            println resultFromLexicalAnalyzer.toString()
            return resultFromLexicalAnalyzer
        }

        // TODO implementar analisador sintático
        // TODO implementar as outras fases de compilação

        def objectCodeGenerator = new ObjectCodeGenerator()

        def resultFromObjectCodeGenerator = objectCodeGenerator.generate(resultFromLexicalAnalyzer.value)
        if (resultFromObjectCodeGenerator.hasErrors()) {
            println resultFromObjectCodeGenerator.toString()
        }

        return resultFromObjectCodeGenerator
    }

}