import com.itextpdf.text.DocumentException;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Driver {
    public static void main(String[] args) {

        long time = new Date().getTime();
        System.out.println(time);

        if (args.length < 1 || args.length > 2) {
            System.out.println("Incorrect program usage");
            System.out.println("corect use: java -jar IText_MergedPdf.jar [path to pdf file] [optional path to PDF generated folder]");
            System.exit(0);
        }

        try {
            Set<String> listOfFiles = listFilesUsingDirectoryStream(args[0]);
            listOfFiles.stream().forEach(System.out::println);
            ItextMerge itextMerge = new ItextMerge();
            itextMerge.doMerge(listOfFiles, args[1]);
        } catch (IOException| DocumentException e) {

            System.out.println(e.getMessage());
        }


    }

    public static Set<String> listFilesUsingDirectoryStream(String dir) throws IOException {
        Set<String> fileList = new HashSet<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dir))) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    fileList.add(path.toAbsolutePath()
                            .toString());
                }
            }
        }
        return fileList;
    }
}
