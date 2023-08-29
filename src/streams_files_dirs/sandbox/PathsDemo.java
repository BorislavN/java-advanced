package streams_files_dirs.sandbox;

import java.io.IOException;
import java.nio.file.Path;

public class PathsDemo {
    public static void main(String[] args) throws IOException {
        Path projectRoot = Path.of("");//This returns the root folder of the project
        Path currentFolder = Path.of("src/streams_files_dirs/exercises/solutions");

        System.out.printf("projectRoot: %s%n%n", projectRoot.toAbsolutePath());
        System.out.printf("currentFolder: %s%n%n", currentFolder.toAbsolutePath());
        System.out.printf("currentFolder parent: %s%n%n", currentFolder.toAbsolutePath().getParent());
        System.out.printf("currentFolder parent with '..': %s%n%n", currentFolder.resolve("..").normalize().toAbsolutePath());

        //normalize is used to resolve the "." and ".."
        Path desktop = Path.of("..");

        System.out.printf("desktop: %s%n%n", desktop.toAbsolutePath());
        System.out.printf("desktop normalized: %s%n%n", desktop.toAbsolutePath().normalize());

        //resolve is used to navigate from a path
        Path src = Path.of("src/");

        System.out.printf("Navigating forward with resolve: %s%n%n", src.resolve("generics").resolve("Main.java").toAbsolutePath());
        System.out.printf("Navigating backwards with resolve: %s%n%n", src.resolve("..").toAbsolutePath().normalize());
        System.out.printf("Back and forth with resolve: %s%n%n", src.resolve("..").resolve("src").resolve("generics").toAbsolutePath());
        System.out.printf("Back and forth with resolve normalized: %s%n%n", src.resolve("..").resolve("src").resolve("generics").normalize().toAbsolutePath());

        //relativize is used to create a relative path to the second from the first
        //relativize will throw an error if one path is absolute and the other is relative
        //they must be of one type
        System.out.println(src.relativize(currentFolder));

        Path srcAbsolute = Path.of("src").toAbsolutePath();

        Path relativePathToCurrentFolder = srcAbsolute.relativize(currentFolder.toAbsolutePath());
        System.out.println(relativePathToCurrentFolder);

        System.out.printf("%nNavigating using src and the relative path: %s%n%n", src.resolve(relativePathToCurrentFolder));
        System.out.printf("Navigating using src and the relative path - absolute version: %s%n%n", src.resolve(relativePathToCurrentFolder).toAbsolutePath());
    }
}