import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MethodNode extends Symbol implements Scope {
    private LinkedHashMap<String, Symbol> formals = new LinkedHashMap<>();
    private Scope owner;
    private Scope body;
    private Map<String, Symbol> localVars = new HashMap<>();
    private Map<String, Symbol> initVars = new HashMap<>();


    public MethodNode(ClassNode returnType, String name, Scope owner) {
        super(name, returnType);
        this.owner = owner;
    }

    public String getScopeName() {
        return this.name;
    }

    public Scope getEnclosingScope(){
        return owner;
    }



    @Override
    public Symbol lookup(String name){
        if(formals.containsKey(name)){
            return formals.get(name);
        } else if(localVars.containsKey(name)){
            return localVars.get(name);
        } else{
            return this.getEnclosingScope().lookup(name);
        }
    }

    @Override
    public Symbol lookupLocally(String name){
        if(formals.containsKey(name)){
            return formals.get(name);
        }else{
            return localVars.get(name);
        }
    }

    @Override
    public void define(Symbol sym){
        localVars.put(sym.getName(), sym);
    }

    @Override
    public void initialize(Symbol sym){
        initVars.put(sym.getName(), sym);
    }

    public void defineFormal(Symbol sym) { formals.put(sym.getName(), sym); }

    public LinkedHashMap<String,Symbol> getFormals() { return this.formals; }

}