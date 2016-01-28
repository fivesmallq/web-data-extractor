package im.nll.data.extractor.utils;

import javax.xml.transform.Result;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringReader;

/**
 * Utilities methods for XML files.
 *
 * @author clardeur
 */
public class XmlUtils {

    private static final InputStream XSLT_REMOVE_NAMESPACE;

    static {
        XSLT_REMOVE_NAMESPACE = XmlUtils.class.getResourceAsStream("/remove-namespace.xslt");
        if (XSLT_REMOVE_NAMESPACE == null)
            throw new ExceptionInInitializerError(new FileNotFoundException("No XSLT resource is found!"));
    }

    /**
     * Remove all namespaces from the XML source into the XML output.
     *
     * @param xmlSource the XML source
     * @throws TransformerException the TransformerException
     */
    public static String removeNamespace(String xmlSource) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Templates transformer = factory.newTemplates(new StreamSource(XSLT_REMOVE_NAMESPACE));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Result result = new StreamResult(baos);
        transformer.newTransformer().transform(new StreamSource(new StringReader(xmlSource)), result);
        return baos.toString();
    }
}