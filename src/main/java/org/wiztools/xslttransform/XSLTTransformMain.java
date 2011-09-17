package org.wiztools.xslttransform;

import java.io.File;
import java.io.PrintStream;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author subhash
 */
public class XSLTTransformMain {
    private static void printHelp(PrintStream out) {
        out.println("Usage: java -jar xslt-transform.jar stylesheet file [file ...]");
    }
    
    public static void main(String[] arg) throws Exception {
        if(arg.length < 2) {
            printHelp(System.err);
            System.exit(1);
        }
        File stylesheet = new File(arg[0]);
        
        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer transformer = transFactory.newTransformer(
                new StreamSource(stylesheet));
        
        boolean firstTimeInLoop = true;
        for(String dataFileStr: arg) {
            if(firstTimeInLoop) {
                firstTimeInLoop = false;
                continue;
            }
            
            File dataFile = new File(dataFileStr);
            
            transformer.transform(new StreamSource(dataFile),
                    new StreamResult(System.out));
            
            System.out.println();
        }
    }
}
