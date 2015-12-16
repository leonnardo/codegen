import sun.jvm.hotspot.debugger.cdbg.Sym;

import java.util.HashMap;
import java.util.Map;

public class ClassNode implements Scope {

    private String name;
    private ClassNode superClass;
    private Map<String, Symbol> symTable = new HashMap<>();

    public ClassNode(String name, ClassNode superClass, Map<String, Symbol> symTable) {
        this.name = name;
        this.superClass = superClass;
        this.symTable = symTable;
    }

    public ClassNode getSuperClass() {
        return this.superClass;
    }

    @Override
    public String getScopeName() {
        return name;
    }

    @Override
    public Scope getEnclosingScope() {
        return null;
    }

    @Override
    public void define(Symbol sym) {
        symTable.put(sym.getName(), sym);
    }

    @Override
    public Symbol lookup(String name) {
        Symbol symbol = null;
        for (ClassNode classNode = this; symbol == null && classNode != null; classNode = classNode.getSuperClass()) {
            symbol = classNode.symTable.get(name);
        }
        return symbol;
    }

    @Override
    public Symbol lookupLocally(String name) {
        return symTable.get(name);
    }

    @Override
    public void initialize(Symbol sym) {
        assert false;
    }

    public Map<String, Symbol> getSymTable() { return symTable; }

    @Override
    public String toString() { return name; }
}
