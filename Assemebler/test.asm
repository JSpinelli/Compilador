.386
.model flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
.data
CAD15 DB "Este es el anidamiento 3",0
CAD16 DB "Esta es una cadena multilinea",0
CAD13 DB "Este es el anidamiento 1",0
CAD14 DB "Este es el anidamiento 2",0
CAD11 DB "Se hizo la division correctamente",0
CAD12 DB "Se hizo la division incorrectamente",0
CAD10 DB "Se realizo la division",0
@@@x DD 0,0
@j DW 0,0
@x DD 0,0
@y DD 0,0
@z DD 0,0
CAD9 DB "Demostracion de anidamiento",0
CAD8 DB "La precedencia anda bien",0
CAD7 DB "Z es menor a 200",0
CAD6 DB "Z es mayor a 200",0
CAD5 DB "Y es mayor o igual a 3",0
CAD4 DB "Y es menor a 3",0
CAD3 DB "Este es el CASE 4",0
CAD2 DB "Este es el CASE 3",0
CAD1 DB "Este es el CASE 2",0
CAD0 DB "Este es el CASE 1",0
@@x DW 0,0
@@y DW 0,0
@@z DW 0,0
TITULO DB "Error de Compilacion ",0
TERMINO DB "Termino la ejecucion ",0
SALIDA DB "Salida por pantalla ",0
RESTA_NEGATIVA DB "El resultado de una resta da negativo",0
OVERFLOW_MULT DB "Hubo un overflow en una multiplicacion",0
DIVISOR_CERO DB "Se quizo hacer una division por 0",0
.code
start:
;ASIGNACION
MOV @x, 400000
;PRINCIPIO SUMA
MOV BX, 1
ADD BX, 1
;Chequeo OVERFLOW
JO Label2
;TERMINO SUMA
;ASIGNACION
MOV @@x, BX
;PRINCIPIO SWITCH
;PRINCIPIO CASE
CMP @@x, 1
JNE Label3
;CUERPO CON ELSE
invoke MessageBox, NULL, addr CAD0, addr SALIDA , MB_OK
;PRINCIPIO SUMA
MOV BX, @@x
ADD BX, 1
;Chequeo OVERFLOW
JO Label2
;TERMINO SUMA
;ASIGNACION
MOV @@x, BX
JMP Label4
Label3:
;PRINCIPIO CASE
CMP @@x, 2
JNE Label5
;CUERPO CON ELSE
invoke MessageBox, NULL, addr CAD1, addr SALIDA , MB_OK
;PRINCIPIO SUMA
MOV BX, @@x
ADD BX, 1
;Chequeo OVERFLOW
JO Label2
;TERMINO SUMA
;ASIGNACION
MOV @@x, BX
JMP Label6
Label5:
;PRINCIPIO CASE
CMP @@x, 3
JNE Label7
;CUERPO CON ELSE
invoke MessageBox, NULL, addr CAD2, addr SALIDA , MB_OK
;PRINCIPIO SUMA
MOV BX, @@x
ADD BX, 1
;Chequeo OVERFLOW
JO Label2
;TERMINO SUMA
;ASIGNACION
MOV @@x, BX
JMP Label8
Label7:
;PRINCIPIO CASE
CMP @@x, 4
JNE Label9
;CUERPO SIN ELSE
invoke MessageBox, NULL, addr CAD3, addr SALIDA , MB_OK
;PRINCIPIO SUMA
MOV BX, @@x
ADD BX, 1
;Chequeo OVERFLOW
JO Label2
;TERMINO SUMA
;ASIGNACION
MOV @@x, BX
Label9:
Label8:
Label6:
Label4:
;FIN SWITCH
;PRINCIPIO RESTA
MOV BX, @@x
SUB BX, 1
;Chequeo numero negativo
CMP BX, 0
JS Label1 
;ASIGNACION
MOV @@y, BX
;COMPARACION
MOV BX , @@y
CMP BX, 3
JAE Label10
;CUERPO CON ELSE
invoke MessageBox, NULL, addr CAD4, addr SALIDA , MB_OK
JMP Label11
Label10:
;ELSE
invoke MessageBox, NULL, addr CAD5, addr SALIDA , MB_OK
Label11:
;PRINCIPIO SUMA
MOV BX, 4
ADD BX, 5
;Chequeo OVERFLOW
JO Label2
;TERMINO SUMA
;PRINCIPIO SUMA
ADD BX,@@y
;Chequeo OVERFLOW
JO Label2
;TERMINO SUMA
;PRINCIPIO SUMA
ADD BX,4
;Chequeo OVERFLOW
JO Label2
;TERMINO SUMA
;CONVERSION
MOV ECX, 0
MOV CX, BX
;ASIGNACION
MOV @z, ECX
;CONVERSION
MOV EBX, 0
MOV BX, 200
;COMPARACION
MOV ECX , @z
CMP ECX, EBX
JBE Label12
;CUERPO CON ELSE
invoke MessageBox, NULL, addr CAD6, addr SALIDA , MB_OK
JMP Label13
Label12:
;ELSE
invoke MessageBox, NULL, addr CAD7, addr SALIDA , MB_OK
Label13:
;PRINCIPIO MUL
MOV BX , 1
MOV AX,8
MUL BX
;Chequeo OVERFLOW
JO Label2
;PRINCIPIO SUMA
MOV BX,4
ADD BX,AX
;Chequeo OVERFLOW
JO Label2
;TERMINO SUMA
;ASIGNACION
MOV @@z, BX
;COMPARACION
MOV BX , @@z
CMP BX, 12
JNE Label14
;CUERPO SIN ELSE
invoke MessageBox, NULL, addr CAD8, addr SALIDA , MB_OK
;PRINCIPIO MUL
MOV BX , 2
MOV AX,@@z
MUL BX
;Chequeo OVERFLOW
JO Label2
;PRINCIPIO RESTA
SUB AX,1
;Chequeo numero negativo
CMP AX, 0
JS Label1 
;ASIGNACION
MOV @@z, AX
;COMPARACION
MOV BX , @@z
CMP BX, 23
JNE Label15
;CUERPO SIN ELSE
invoke MessageBox, NULL, addr CAD9, addr SALIDA , MB_OK
Label15:
Label14:
;CONVERSION
MOV EBX, 0
MOV BX, 1
;PRINCIPIO DIV
MOV EBX , EBX
CMP EBX, 0
JE Label0
MOV EAX,80000
DIV EBX
;ASIGNACION
MOV @@@x, EAX
invoke MessageBox, NULL, addr CAD10, addr SALIDA , MB_OK
;COMPARACION
MOV EBX , @@@x
CMP EBX, 80000
JNE Label16
;CUERPO CON ELSE
invoke MessageBox, NULL, addr CAD11, addr SALIDA , MB_OK
JMP Label17
Label16:
;ELSE
invoke MessageBox, NULL, addr CAD12, addr SALIDA , MB_OK
Label17:
;ASIGNACION
MOV @j, 1
;PRINCIPIO SWITCH
;PRINCIPIO CASE
CMP @j, 1
JNE Label18
;CUERPO SIN ELSE
invoke MessageBox, NULL, addr CAD13, addr SALIDA , MB_OK
;PRINCIPIO SWITCH
;PRINCIPIO CASE
CMP @j, 1
JNE Label19
;CUERPO SIN ELSE
invoke MessageBox, NULL, addr CAD14, addr SALIDA , MB_OK
;PRINCIPIO SWITCH
;PRINCIPIO CASE
CMP @j, 1
JNE Label20
;CUERPO SIN ELSE
invoke MessageBox, NULL, addr CAD15, addr SALIDA , MB_OK
Label20:
;FIN SWITCH
Label19:
;FIN SWITCH
Label18:
;FIN SWITCH
;PRINCIPIO SWITCH
;PRINCIPIO CASE
CMP @j, 1
JNE Label21
;CUERPO SIN ELSE
;COMPARACION
MOV BX , @j
CMP BX, 4
JA Label22
;CUERPO SIN ELSE
invoke MessageBox, NULL, addr CAD16, addr SALIDA , MB_OK
Label22:
Label21:
;FIN SWITCH
invoke MessageBox, NULL, addr TERMINO, addr TITULO , MB_OK 
EXIT:
invoke ExitProcess, 0
Label0:
invoke MessageBox, NULL, addr DIVISOR_CERO, addr TITULO , MB_OK 
JMP EXIT
Label1:
invoke MessageBox, NULL, addr RESTA_NEGATIVA, addr TITULO , MB_OK 
JMP EXIT
Label2:
invoke MessageBox, NULL, addr OVERFLOW_MULT, addr TITULO , MB_OK 
JMP EXIT
end start
