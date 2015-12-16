import org.antlr.runtime.TokenRewriteStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.stringtemplate.v4.ST;
import parser.MinijavaLexer;
import parser.MinijavaParser;
import parser.MinijavaVisitor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class Main {
    private static String inputFile = null;
    public static void main(String[] args) throws IOException {
        //The file input stream for lexing the file.
        InputStream is = null;
        if (args.length > 0) {
            //Assign the first argument of the program as the inputFile name
            inputFile = args[0];
            is = new FileInputStream(inputFile);
        }

        // Do ANTLR things
        ANTLRInputStream input = new ANTLRInputStream(is);
        MinijavaLexer lexer = new MinijavaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MinijavaParser parser = new MinijavaParser(tokens);

        // Intialize at "program" rule
        ParseTree tree = parser.program();
        ParseTreeWalker walker = new ParseTreeWalker();
        // walk in symbol table
        SymbolTable symTab = new SymbolTable(new HashMap<String, ClassNode>(), new ParseTreeProperty<Scope>(), parser);
        walker.walk(symTab, tree);
        //walker.walk(new CodeGenerator(symTab), tree);
        CodeGenerator codeGen = new CodeGenerator(symTab);
        codeGen.visit(tree);
        for (ST st : codeGen.assembler) {
            System.out.println(st.render());
        }

    }
}
