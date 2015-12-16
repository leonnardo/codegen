import antlr.RuleBlock;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import parser.MinijavaBaseListener;
import parser.MinijavaParser;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class SymbolTable extends MinijavaBaseListener {
    MinijavaParser parser;
    private Map<String, ClassNode> classes;
    private ParseTreeProperty<Scope> scopes;
    Scope currentScope = null;

    public Map<String, ClassNode> getClasses() { return this.classes; }
    public ParseTreeProperty<Scope> getScopes() { return this.scopes; }

    public SymbolTable(Map<String, ClassNode> classes, ParseTreeProperty<Scope> scopes, MinijavaParser parser) {
        this.classes = classes;
        this.scopes = scopes;
        this.parser = parser;
    }

    @Override
    public void enterMainClass(@NotNull MinijavaParser.MainClassContext ctx) {
        ClassNode currentClass;
        /* adds int, int[], boolean and main classes to symbol table */
        currentClass = new ClassNode("int", null, null);
        classes.put(currentClass.getScopeName(), currentClass);


        currentClass = new ClassNode("int[]", null, null);
        classes.put(currentClass.getScopeName(), currentClass);

        currentClass = new ClassNode("boolean", null, null);
        classes.put(currentClass.getScopeName(), currentClass);

        currentClass = new ClassNode("main", null, null);
        classes.put(currentClass.getScopeName(), currentClass);
    }

    @Override
    public void enterClassDecl(@NotNull MinijavaParser.ClassDeclContext ctx) {
        String identifier = ctx.identifier().getText();
        ClassNode classNode;
        Map<String, Symbol> symbolMap = new HashMap<>();

        /* This is needed if you declare a class B as superclass
         * of a class A before class B declaration, such as:
         * class A extends B { ... }
         * class B { ... }
         */
        if (!classes.containsKey(identifier)) {
            classNode = new ClassNode(identifier, null, symbolMap);
        } else {
            classNode = classes.get(identifier);
        }
        currentScope = classNode;
        classes.put(identifier,classNode);
        scopes.put(ctx, currentScope);
    }

    @Override
    public void exitClassDecl(@NotNull MinijavaParser.ClassDeclContext ctx) {
        currentScope = currentScope.getEnclosingScope();
    }


    @Override
    public void enterClassDeclExtends(@NotNull MinijavaParser.ClassDeclExtendsContext ctx) {
        String identifier = ctx.identifier(0).getText();
        String superId = ctx.identifier(1).getText();
        Map<String, Symbol> symbolMap = new HashMap<String, Symbol>();
        ClassNode superClass;
        ClassNode classNode;
        if (!classes.containsKey(superId)) {
            superClass = new ClassNode(superId, null, symbolMap);
        } else {
            superClass = classes.get(superId);
        }

        /* Consider that if you are creating a class with a SuperClass,
         * this is a whole new class
         */
        classNode = new ClassNode(identifier, superClass, symbolMap);
        currentScope = classNode;
        classes.put(identifier, classNode);
        scopes.put(ctx, currentScope);
    }

    @Override
    public void exitClassDeclExtends(@NotNull MinijavaParser.ClassDeclExtendsContext ctx) {
        currentScope = currentScope.getEnclosingScope();
    }


    @Override
    public void enterVarDecl(@NotNull MinijavaParser.VarDeclContext ctx) {
        String type = ctx.type().getText();
        String varName = ctx.identifier().getText();
        currentScope.define(new Symbol(varName, classes.get(type)));
    }

    @Override
    public void enterMethodDecl(@NotNull MinijavaParser.MethodDeclContext ctx) {
        ClassNode returnType = classes.get(ctx.type().getText());
        Scope owner = currentScope;
        MethodNode methodNode = new MethodNode(returnType, ctx.identifier().getText(), owner);
        currentScope.define(methodNode);
        currentScope = methodNode;
        scopes.put(ctx, currentScope);
    }

    @Override
    public void exitMethodDecl(@NotNull MinijavaParser.MethodDeclContext ctx) {
        currentScope = currentScope.getEnclosingScope();
    }

    @Override
    public void enterFormalList(@NotNull MinijavaParser.FormalListContext ctx) {
        // get identifiers if method has formals
        int size = ctx.identifier().size();
        MethodNode method = (MethodNode) currentScope;
        for (int i = 0; i < size; i++) {
            String type = ctx.type(i).getText();
            String identifier = ctx.identifier(i).getText();
            Symbol sym = new Symbol(identifier, classes.get(type));
            method.defineFormal(sym);
        }
    }
}
