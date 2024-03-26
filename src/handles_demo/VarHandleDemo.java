package handles_demo;

import java.lang.invoke.VarHandle;

public class VarHandleDemo {
    private static  VarHandle NAME_HANDLER;
    private static  VarHandle WORK_HANDLER;

    static {
        try {
            NAME_HANDLER = Person.lookup().findVarHandle(Person.class,"name", String.class);
            WORK_HANDLER = Worker.lookup().findVarHandle(Worker.class,"work", String.class);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("Ooops, failed to crate varhandles..");
        }
    }

    public static void main(String[] args) {
        Person person=new Person("Pesho");
        Worker worker=new Worker("Stamat","Programmer");

        System.out.println("Before");
        System.out.println(NAME_HANDLER.get(person));
        System.out.println(NAME_HANDLER.get(worker));
        System.out.println(WORK_HANDLER.get(worker));

        System.out.println();
        System.out.println("After");
        NAME_HANDLER.set(person,"Pesho1");
        NAME_HANDLER.set(worker,"Stamat1");
        WORK_HANDLER.setVolatile(worker,"DEVOPS");

        System.out.println(NAME_HANDLER.get(person));
        System.out.println(NAME_HANDLER.get(worker));
        System.out.println(WORK_HANDLER.get(worker));
    }
}
