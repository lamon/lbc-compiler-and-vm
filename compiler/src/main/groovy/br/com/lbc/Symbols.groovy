package br.com.lbc

enum Symbols {

    ADD("0001"),
    SUB("0011"),
    CMP("0010"), // compare

    LOAD("1000"),
    STORE("1001"),

    JMP("0100"), // jump
    JMPT("0101"), // jump if true
    JMPF("0110"), // jump if false

    RA("0001"), // registrySourceA
    RB("0010"), // registrySourceB
    RD("0011"), // registryDestiny
    RF("0100"), // registryFlag

    R0("0000"), // registryCommon 0
    R1("0001"), // registryCommon 1
    R2("0010"), // registryCommon 2
    R3("0011"), // registryCommon 3
    R4("0100"), // registryCommon 4
    R5("0101"), // registryCommon 5
    R6("0110"), // registryCommon 6
    R7("0111"), // registryCommon 7
    R8("1000"), // registryCommon 8
    R9("1001")  // registryCommon 9

    Symbols(String bitsAsString) {
        this.bitsAsString = bitsAsString
    }

    def bitsAsString

}