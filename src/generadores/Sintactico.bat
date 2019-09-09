SET JAVA_HOME="C:\Program Files\Java\jdk1.8.0_221\bin"
SET PATH=%JAVA_HOME%;%PATH%
SET CLASSPATH=%JAVA_HOME%;
cd C:\Users\KimEd\Documents\NetBeansProjects\SextoSemestre\[OLC1]Tarea1\src\analizadores
java -jar C:\Librerias\java-cup-11b.jar -parser Parser -symbols Simbolos Sintactico.cup
PAUSE