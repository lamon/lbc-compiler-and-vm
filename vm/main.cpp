#include <iostream>
#include <fstream>
#include <sstream>
#include <bitset>

using namespace std;

const int memorySize = 256;

// Memoria de programa
unsigned int programMemory[memorySize];

// Memoria de dados
unsigned int dataMemory[memorySize] = {1, 2, 0, 0, 0, 0, 0, 0}; // maximo seria 256, pq somente 8 bits foram usados para endereçar

// Cache a nível de memória de dados
unsigned int dataMemoryCache[memorySize];

// Registradores
unsigned int pc;
unsigned int instruction;
unsigned int instructionType;
unsigned int registrySourceA;
unsigned int registrySourceB;
unsigned int registryDestiny;
unsigned int registryAddressMemory;
unsigned int registryFlag;
unsigned int registryCommon[10]; // dez registradores de uso geral

void decode() {

    instructionType = instruction >> 12;

    if (instructionType == 1 || instructionType == 3) {
        /* ADD, SUB */
        registrySourceA = instruction >> 8; // acrescentou 8 zeros na frente deslocando pra direita e descartou os 8 ultimos
        registrySourceA = registrySourceA & 0b0000000000001111; // AND lógico
        registrySourceB = instruction >> 4;
        registrySourceB = registrySourceB & 0b0000000000001111;
        registryDestiny = instruction & 0b0000000000001111;
    } else if (instructionType == 8) {
        /* LOAD */
        registryDestiny = instruction >> 8;
        registryDestiny = registryDestiny & 0b0000000000001111;
        registryAddressMemory = instruction & 0b0000000011111111;
    } else if (instructionType == 9) {
        /* STORE */
        registrySourceA = instruction >> 8;
        registrySourceA = registrySourceA & 0b0000000000001111;
        registryAddressMemory = instruction & 0b0000000011111111;
    } else if (instructionType == 2) {
        /* CMP */
        registrySourceA = instruction >> 8;
        registrySourceA = registrySourceA & 0b0000000000001111;
        registrySourceB = instruction >> 4;
        registrySourceB = registrySourceB & 0b0000000000001111;
    } else if (instructionType == 4 || instructionType == 5 || instructionType == 6) {
        /* JMP, JMPT, JMPF */
        registryAddressMemory = instruction >> 12; // descartado os 4 ultimos bits
        registryAddressMemory = instruction & 0b0000000011111111;
    }

}

void execute() {

    if (instructionType == 1) {
        // ADD
        registryCommon[registryDestiny] = registryCommon[registrySourceA] + registryCommon[registrySourceB];
    } else if (instructionType == 3) {
        // SUB
        registryCommon[registryDestiny] = registryCommon[registrySourceA] - registryCommon[registrySourceB];
    } else if (instructionType == 8) {
        // LOAD

        if (dataMemoryCache[registryAddressMemory] != 0b0000000000000000) {
            // cache hit
            registryCommon[registryDestiny] = dataMemoryCache[registryAddressMemory];
        } else {
            // cache miss
            registryCommon[registryDestiny] = dataMemory[registryAddressMemory];
            dataMemoryCache[registryAddressMemory] = registryCommon[registryDestiny]; // salva no cache
        }

    } else if (instructionType == 9) {
        // STORE

        if (dataMemoryCache[registryAddressMemory] != 0b0000000000000000) {
            dataMemoryCache[registryAddressMemory] = 0b0000000000000000; // invalida o cache
        }

        dataMemory[registryAddressMemory] = registryCommon[registrySourceA];
    } else if (instructionType == 2) {
        // CMP
        if (registryCommon[registrySourceA] == registryCommon[registrySourceB]) {
            registryFlag = 0b0000000000000001; // seta o bit boleano (bit menos significativo) como true
        } else {
            registryFlag = 0b0000000000000000; // seta o bit boleano (bit menos significativo) como false
        }
    } else if (instructionType == 4) {
        // JMP
        pc = dataMemory[registryAddressMemory];
    } else if (instructionType == 5) {
        // JMPT
        if (registryFlag == 0b0000000000000001) {
            pc = dataMemory[registryAddressMemory];
        }
    } else if (instructionType == 6) {
        // JMPF
        if (registryFlag == 0b0000000000000000) {
            pc = dataMemory[registryAddressMemory];
        }
    }

}

void loadProgramMemory(const string &compiledCodeFilename) {

    ifstream infile(compiledCodeFilename);

    string line;
    int count = 0;
    while (getline(infile, line)) {
        istringstream iss(line);
        programMemory[count] = bitset<18>(line).to_ulong();
        count++;
    }

}

int main(int argc, char** argv) {

    if (argc < 2) {
        cout << "Please input a .lbc compiled code\n";
        cout << "Example: $ " << argv[0] << " destiny.out\n";
        return 1;
    }

    loadProgramMemory(argv[1]);

    unsigned char i;

    // Inicializacao dos registros
    pc = 0;
    for (i = 0; i < 10; i++) {
        registryCommon[i] = 0;
    }

    while (pc < memorySize) {
        instruction = programMemory[pc]; // busca da instrução
        pc = pc + 1; // ja defino a proxima instrução
        decode();    // decodificação
        execute();  // execução
    }

    return 0;
}