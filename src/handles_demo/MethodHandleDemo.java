package handles_demo;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class MethodHandleDemo {
    public static void main(String[] args) throws Throwable {
        MethodHandle personHandle = Person.lookup().findVirtual(Person.class, "introduce", MethodType.methodType(String.class));
        MethodHandle workerHandle = Worker.lookup().findSpecial(Person.class, "introduce", MethodType.methodType(String.class), Worker.class);
        MethodHandle messageHandle = MethodHandles.lookup().findStatic(MethodHandleDemo.class, "printMessage", MethodType.methodType(void.class, Person.class, Person.class));

        Person person = new Person("Pesho");
        Worker worker = new Worker("Stamat", "Programmer");

        System.out.println(personHandle.invoke(person));
        System.out.println(personHandle.invoke(worker));
        System.out.println(workerHandle.invoke(worker));

        MethodHandle bound = messageHandle.bindTo(person);
        bound.invoke(worker);

        System.out.println();
        System.out.println("Loop:");

        MethodHandle initHandle = MethodHandles.lookup().findStatic(MethodHandleDemo.class, "initCounter", MethodType.methodType(int.class));
        MethodHandle predicateHandle = MethodHandles.lookup().findStatic(MethodHandleDemo.class, "predicate", MethodType.methodType(boolean.class, int.class, int.class));
        MethodHandle logicHandle = MethodHandles.lookup().findStatic(MethodHandleDemo.class, "logic", MethodType.methodType(int.class, int.class, int.class, Person.class, Person.class));

        MethodHandle loopHandle = MethodHandles.whileLoop(initHandle, predicateHandle, logicHandle);

        loopHandle.invoke(10, person, worker);
    }

    private static void printMessage(Person person1, Person person2) {
        System.out.printf("%s and %s became friends!%n", person1.getName(), person2.getName());
    }

    private static int initCounter() {
        return 0;
    }

    private static boolean predicate(int count, int limit) {
        return count < limit;
    }

    private static int logic(int count, int limit, Person one, Person two) {
        count++;

        System.out.print(count + ". ");
        printMessage(one, two);

        return count;
    }
}


