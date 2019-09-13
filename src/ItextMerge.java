import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class ItextMerge {

    public static String RESULT = "/result.pdf";
    public final String dir = System.getProperty("user.dir");
    final static Logger logger = Logger.getLogger(ItextMerge.class);

    public ItextMerge() {
        PropertyConfigurator.configure(dir + "/config/log4j.properties");
    }

    /**
     * Merge multiple pdf into one pdf
     * @param listOfFiles of pdf input stream
     * @param destination output file output stream
     * @throws DocumentException
     * @throws IOException
     */
    public  void doMerge(Set<String> listOfFiles, String destination)
            throws DocumentException, IOException {

        logger.info("Destination dir =" + destination);

        logger.info("Merging " + listOfFiles.size() + " files");

        try {
            List<InputStream> list = new ArrayList<InputStream>();

            for (String s: listOfFiles) {
                list.add(new FileInputStream(new File(s)));
                System.out.println(s);
                logger.info(s);
            }

            // Resulting pdf
            OutputStream out = new FileOutputStream(new File(destination + RESULT));


            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();
            PdfContentByte cb = writer.getDirectContent();

            for (InputStream in : list) {
                PdfReader reader = new PdfReader(in);
                for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                    document.newPage();
                    //import the page from source pdf
                    PdfImportedPage page = writer.getImportedPage(reader, i);
                    //add the page to the destination pdf
                    cb.addTemplate(page, 0, 0);
                }
            }

            out.flush();
            document.close();
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}