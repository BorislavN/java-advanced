package handles_demo;

import java.lang.invoke.MethodHandles;

public class Person {
    private  String name;

    public Person(String name) {
        this.name = name;
    }

    public String introduce(){
        return String.format("Hello, I'm %s!",this.getName());
    }

    public String getName() {
        return this.name;
    }

    public static MethodHandles.Lookup lookup(){return MethodHandles.lookup();}
}
