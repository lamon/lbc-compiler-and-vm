package br.com.lbc

class SimpleResult {

    def value
    def errors = []
    def warnings = []
    def infos = []

    void addError(String message) {
        errors.add(message)
    }

    boolean hasErrors() {
        return !errors.isEmpty()
    }

    void addWarning(String message) {
        warnings.add(message)
    }

    boolean hasWarnings() {
        return !warnings.isEmpty()
    }

    void addInfo(String message) {
        infos.add(message)
    }

    boolean hasInfos() {
        return !infos.isEmpty()
    }

    @Override
    public String toString() {
        def stringToReturn = ""

        if (hasErrors()) {
            stringToReturn += "Errors: \n"
            errors.each { error ->
                stringToReturn += error + "\n"
            }

        } else {

            if (value) {
                stringToReturn += "Value: ${value} \n"
            }

            if (infos) {
                stringToReturn += "Infos: \n"
                infos.each { info ->
                    stringToReturn += info + "\n"
                }
            }

            if (warnings) {
                stringToReturn += "Warnings: \n"
                warnings.each { warning ->
                    stringToReturn += warning + "\n"
                }
            }
        }

        return stringToReturn
    }

}
