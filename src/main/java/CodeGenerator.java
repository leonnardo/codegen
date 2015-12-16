import org.antlr.v4.runtime.misc.NotNull;

import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;
import parser.MinijavaBaseVisitor;
import parser.MinijavaParser;

import java.lang.reflect.Array;
import java.util.*;

public class CodeGenerator extends MinijavaBaseVisitor {

    SymbolTable symTab;
    private STGroup template = new STGroupFile("llvm.stg");
    public ArrayList<ST> assembler = new ArrayList<>();
    int tmpRegCount = 0;
    Map<String, ClassNode> classNodeMap = new HashMap<>();
    ParseTreeProperty<Scope> scopes;
    Scope currentScope = null;

    public CodeGenerator(SymbolTable symTab) {
        this.symTab = symTab;
        this.classNodeMap = symTab.getClasses();
        this.scopes = symTab.getScopes();
    }

    // Generate code for Main Class
    @Override
    public ST visitProgram(@NotNull MinijavaParser.ProgramContext ctx) {
        ST classSt;
        Set<String> ignoredClasses = new HashSet<>(4); // 4 for using less space
        ignoredClasses.add("int");
        ignoredClasses.add("int[]");
        ignoredClasses.add("boolean");
        ignoredClasses.add("main");

        if (symTab.getClasses() != null) {
            for (ClassNode clazz : symTab.getClasses().values()) {
                if (!ignoredClasses.contains(clazz.getScopeName())) {
                    // class template for each class
                    classSt = template.getInstanceOf("classDecl");
                    // class name
                    classSt.add("name", clazz.getScopeName());
                    // superclass name
                    if (clazz.getSuperClass() != null) {
                        classSt.add("superClass", clazz.getSuperClass().getScopeName());
                    }
                    // var types
                    List<String> vars = new ArrayList<>();
                    for (Symbol symbol : clazz.getSymTable().values()) {
                        vars.add(symbol.getType().getScopeName());
                    }
                    classSt.add("vars", vars);
                    assembler.add(classSt);
                }
            }
        }
        ctx.mainClass().accept(this);
        ctx.classDecl().forEach((V) -> V.accept(this));
        return null;
    }

    @Override
    public ST visitClassDecl(@NotNull MinijavaParser.ClassDeclContext ctx) {
        currentScope = scopes.get(ctx);
        for (MinijavaParser.MethodDeclContext methodCtx : ctx.methodDecl()) {
            methodCtx.accept(this);
        }
        currentScope = currentScope.getEnclosingScope();
        return null;
    }

    @Override
    public ST visitMethodDecl(@NotNull MinijavaParser.MethodDeclContext ctx) {
        ST methodSt = template.getInstanceOf("methodDecl");
        // currentScope acting like a MethodNode
        currentScope = scopes.get(ctx);
        MethodNode methodNode = (MethodNode) currentScope;
        methodSt.add("ret", methodNode.getType().getScopeName());
        methodSt.add("name", methodNode.getName());
        methodSt.add("classType", methodNode.getEnclosingScope().getScopeName());
        methodSt.add("formals", methodNode.getFormals().values());
        assembler.add(methodSt);
        return null;
    }
}

