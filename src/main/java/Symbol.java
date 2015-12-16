public class Symbol {
    public String name;
    public ClassNode type;


    public Symbol(String name) { this.name = name; }
    public Symbol(String name, ClassNode type) {
        this(name);
        this.type = type;
    }
    public String getName() { return name; }
    public ClassNode getType() { return type; }


    public String toString() {
        if (type != null) return '<'+getName()+":"+type+'>';
        return getName();
    }
}
