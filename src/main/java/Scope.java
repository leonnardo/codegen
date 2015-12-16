public interface Scope {
    public String getScopeName();

    /** Where to look next for symbols */
    public Scope getEnclosingScope();

    /** Define a symbol in the current scope */
    public void define(Symbol sym);

    public void initialize(Symbol sym);

    public Symbol lookup(String name);

    public Symbol lookupLocally(String name);

}
