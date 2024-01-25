package LE7Q1;

public class TylerValueEntry
{
    // Fields
    private Integer key;

    // Default Constructor (key implicitly null)
    public TylerValueEntry(){}

    // Parameterized Constructor
    public TylerValueEntry(Integer k)
    {
        this.key = k;
    }

    // Accessors
    public Integer getKey() {return key;}

    // Mutators
    public void setKey(Integer key) {this.key = key;}
}