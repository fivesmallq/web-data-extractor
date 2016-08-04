package im.nll.data.extractor.utils;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

/**
 * Utilities methods for XML files.
 *
 * @author clardeur
 */
public class XmlUtils {
    /**
     * Remove all namespaces from the XML source into the XML output.
     *
     * @param xmlSource the XML source
     * @throws TransformerException the TransformerException
     */
    public static String removeNamespace(String xmlSource) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        InputStream xsltRemoveNamespace = XmlUtils.class.getResourceAsStream("/remove-namespace.xslt");
        if (xsltRemoveNamespace == null)
            throw new ExceptionInInitializerError(new FileNotFoundException("No XSLT resource is found!"));
        Templates transformer = factory.newTemplates(new StreamSource(xsltRemoveNamespace));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Result result = new StreamResult(baos);
        Source src = new StreamSource(new StringReader(xmlSource));
        transformer.newTransformer().transform(src, result);
        String newXml = baos.toString();
        try {
            xsltRemoveNamespace.close();
            baos.close();
        } catch (IOException e) {
            //eat it
        }
        return newXml;
    }
}
