package org.wiztools.xslttransform;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author subwiz
 */
public class XSLTTransformMain {
    
    private static final int EXIT_CODE_INVALID_PARAM = 1;
    private static final int EXIT_CODE_TRANSFORMER_EXCEPTION = 2;
    private static final int EXIT_CODE_IO_EXCEPTION = 3;
    
    private static void printHelp(PrintStream out) {
        out.println("Usage: java -jar xslt-transform-VERSION.jar stylesheet xml [xml ...]");
        out.println("Where:");
        out.println("\tstylesheet\t: URL or file-system location of the XSL");
        out.println("\txml  ...  \t: URL or file-system location of the XMLs");
    }
    
    private static InputStream getStream(String str) throws IOException {
        try{
            InputStream is;
            try{ // check if the argument is a URL
                final URL url = new URL(str);
                is = url.openStream();
            }
            catch(MalformedURLException ex) { // argument is a file
                is = new FileInputStream(new File(str));
            }
            return is;
        }
        catch(IOException ex) {
            throw new IOException("Error reading file / url: " + str, ex);
        }
    }
    
    public static void main(String[] arg) {
        if(arg.length == 1 && (arg[0].equals("-h") || arg[0].equals("--help"))) {
            printHelp(System.out);
            return;
        }
        else if(arg.length < 2) {
            printHelp(System.err);
            System.exit(EXIT_CODE_INVALID_PARAM);
        }
        
        // When there are 2 or more arguments present:
        try{
            // Argument 1 is the stylesheet:
            final InputStream stylesheetInputStream = getStream(arg[0]);

            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer(
                    new StreamSource(stylesheetInputStream));

            // All arguments other than the first are XML documents to be
            // transformed:
            boolean firstTimeInLoop = true;
            for(String dataFileStr: arg) {
                if(firstTimeInLoop) {
                    firstTimeInLoop = false;
                    continue;
                }

                InputStream dataStream = getStream(dataFileStr);

                transformer.transform(new StreamSource(dataStream),
                        new StreamResult(System.out));

                // Add a new-line after transformed content:
                System.out.println();
            }
        }
        catch(TransformerException ex) {
            ex.printStackTrace(System.err);
            System.exit(EXIT_CODE_TRANSFORMER_EXCEPTION);
        }
        catch(IOException ex) {
            ex.printStackTrace(System.err);
            System.exit(EXIT_CODE_IO_EXCEPTION);
        }
    }
}
