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
no registro R1 e salva o resultado no registro R2
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
 
Carrega o valor que está no endereço de memória 0b00001010 para o registro R0
```