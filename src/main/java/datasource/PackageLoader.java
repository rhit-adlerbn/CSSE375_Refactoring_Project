package datasource;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PackageLoader {
    public static List<byte[]> loadPackage(String packagePath) throws IOException{
        ArrayList<byte[]> bytes = new ArrayList<>();
        Path dir = Paths.get(packagePath);
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dir)) {
            for (Path path : directoryStream) {
                bytes.add(Files.readAllBytes(path)); //get the .class bytecode
            }
        } catch (IOException e) {
            throw e;
        }
        return bytes;
    }
}
