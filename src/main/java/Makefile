SHELL := /bin/bash 
CLASSPATH=.:../lib/Parser.jar:../lib/antlr-4.5.1-complete.jar

all: antlr4.o java.o

antlr4.o: jarParser.o
	java -jar ../lib/antlr-4.5.1-complete.jar -no-listener -visitor com/parser/Minijava.g4

jarParser.o:
	jar cf ../lib/Parser.jar com/

java.o:
	javac -classpath $(CLASSPATH) com/codegen/*.java

run:
	java -classpath $(CLASSPATH) com/codegen/CodeGenerator $(INPUT) $(OUTPUT)

clean:
	rm -rf out/*
