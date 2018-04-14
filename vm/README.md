# VM

**Máquina Virtual LBC**

Este código implementa uma máquina virtual (interpretador) capaz de buscar,
decodificar e executar um set de instrução criado exclusivamente para demonstrações
durante as aulas de "IOT010 - Arquiteturas de Computadores, Sistemas Microprocessados e Microcontrolados"
do curso de pós graduação em Internet das Coisas do Instituto Nacional de Telecomunicações (INATEL).   

## Detalhes do set de instrução

*Tamanho das instruções: 16 bits*
	
Código das intruções:

| Instrução   | Bin    | Tipo  |
| ----------- |:------:| :----:|
| ADD         | 0001   | A     |
| SUB         | 0011   | A     |
| CMP         | 0010   | A     |
| LOAD        | 1000   | B     |
| STORE       | 1001   | B     |
| JMP         | 0100   | C     |
| JMPT        | 0101   | C     |
| JMPF        | 0110   | C     |

**Instruções Tipo A:** 
	
Utilizado para operações aritméticas e comparação (soma, subtração, ...)

(Instrução) | (Endereço do Registrador 1) | (Endereço do Registrador 1) | (Endereço do Registrador Destino)
:---: | :---: | :---: | :---:
4 bits | 4 bits | 4 bits | 4 bits
`MSB` | | | `LSB`
     

```
ADD
- Instrução: ADD R0 R1 R2
- Instrução compilada: 0b0001000000010010 -> |0001|0000|0001|0010|
 
Realiza a soma do valor que está no registro R0 com o valor que está
no registro R1 e salva o resultado no registro R2.
```

```
CMP
- Instrução: CMP R0 R1
- Instrução compilada: 0b0010000000010000 -> |0010|0000|0001|0000
 
Compara o valor que está no registro R0 com o valor que está no registro
R1, se os valores forem iguais, seta o bit boleano do registrador Flag.
```
 	 	 
 	 	 	 
**Instruções Tipo B:**
    
Utilizado para operações de LOAD e STORE

(Instrução) | (Endereço do Registrador 1) | (Endereço Memória de Dados)
:---: | :---: | :---:
4 bits | 4 bits | 8 bits
`MSB` | | `LSB`
     	 
```
LOAD
- Instrução: LOAD R0 0b00001010
- Instrução compilada: 0b1000000000001010 -> |1000|0000|0000|1010
 
Carrega o valor que está no endereço de memória 0b00001010 para o registro R0.
```


**Instruções Tipo C:**
    
Utilizado para operações condicionais

(Instrução) | (Endereço Memória de Dados) | (últimos 4 bits não são usados)
:---: | :---: | ---
4 bits | 8 bits | 4 bits
`MSB` | | `LSB`
     	 
```
JMP
- Instrução: JMP 0b00001010
- Instrução compilada: 0b0100000010100000 -> |0100|00001010|0000
 
JMP incondicional, carrega PC com o valor que está no endereço de memória 0b00001010 
```

```
JMPT
- Instrução: JMPT 0b00001010
- Instrução compilada: 0b0101000010100000 -> |0101|00001010|0000
 
JMP condicional, se o bit boleano do registrador Flag estiver setado,
carrega PC com o valor que está no endereço de memória 0b00001010 
```

```
JMPF
- Instrução: JMPT 0b00001010
- Instrução compilada: 0b0101000010100000 -> |0101|00001010|0000
 
JMP condicional, se o bit boleano do registrador Flag NÃO estiver setado,
carrega PC com o valor que está no endereço de memória 0b00001010 
```

**Registradores:**

*Tamanho dos registradores: 16 bits*

| Registrador | Bin    | Descrição         |
| ----------- |:------:| :----------------:|
| RA          | 0001   | registrySourceA   |
| RB          | 0010   | registrySourceB   |
| RD          | 0011   | registryDestiny   |
| RF          | 0100   | registryFlag      |
| R0          | 0000   | registryCommon 0  |
| R1          | 0001   | registryCommon 1  |
| R2          | 0010   | registryCommon 2  |
| R3          | 0011   | registryCommon 3  |
| R4          | 0100   | registryCommon 4  |
| R5          | 0101   | registryCommon 5  |
| R6          | 0110   | registryCommon 6  |
| R7          | 0111   | registryCommon 7  |
| R8          | 1000   | registryCommon 8  |
| R9          | 1001   | registryCommon 9  |


**Cache de instrução**

*Numero de linhas de cache: 2*

Cada linha da cache possui: (definida na struct LineMemoryCacheStruct)

Bit de validação
Campo para TAG
2 words de data

Calculo do BYTE, Word, Linha e TAG  a partir do endereço de memória solicitado

Cada endereço solicitado possui 2 bytes (16 bits - quantidades de bits usada
nesta arquitetura)

0b 0000 0000 0000 0000 (end. 0)

BYTE: Para o campo BYTE não teremos nenhum bit reservado, pois como minha
estrutura trabalha com 16 bits e cada posição da memória de instrução possui
16 bits, será necessária a leitura de apenas um endereço para obter uma
WORD completa.

WORD: Cada linha de cache suporta salvar duas WORDs, portanto 1 bit do endereço
sera utilizado para identificar qual WORD  a CPU deseja

LINE: A cache projetada possui apenas 2 linhas, portanto será nencesário apenas
1 bit para endereçar a linha

TAG: É o que sobra.... 14 bits (16 bits (total) - 1 bit (LINE) - 1 bit (WORD)).

(TAG - 14 bits) | (LINE - 1 bit) | (WORD - 1 bit)
:---: | :---: | ---
0b 0000 0000 0000 00 | 0 | 0

Exemplo de endereçamento na cache:

Suponha que a CPU solicitou a instrução que esta no endereço
0b 0000 0000 0000 0011 (end. 3)

(TAG - 14 bits) | (LINE - 1 bit) | (WORD - 1 bit)
:---: | :---: | ---
0b 0000 0000 0000 00 | 1 | 1

Este endereço deve ser procurado na linha 1 da cache e a TAG deve estar com o
valor 0. Caso ocorra sucesso (a TAG presente na linha 1 seja igual a esperada)
deve ser lido a WORD 1