package handles_demo;

import java.lang.invoke.MethodHandles;

public class Worker extends Person {
    private  String work;

    public Worker(String name, String work) {
        super(name);
        this.work = work;
    }

    public String getWork() {
        return this.work;
    }

    @Override
    public String introduce() {
        return String.format("%s I work as a %s.", super.introduce(), this.getWork());
    }

    public static MethodHandles.Lookup lookup(){
        return MethodHandles.lookup();
    }
}
