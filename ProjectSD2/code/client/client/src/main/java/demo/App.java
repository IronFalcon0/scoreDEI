package demo;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
* Hello world!
*
*/
public class App 
{
    public static void main( String[] args ) throws IOException, XPathExpressionException, ParserConfigurationException, SAXException, TransformerException
    {
        URL url = new URL("http://localhost:8080/rest/students/1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        // HTTP Verb
        connection.setRequestMethod("GET");
        // Get requests data from the server.
        
        // We are interested in the output
        connection.setDoOutput(true);
        
        // If there is a 3xx error, we want to know.
        connection.setInstanceFollowRedirects(false);
        
        // The Accept header defines what kind of formats we are interested in.
        connection.setRequestProperty("Accept", "application/xml");
        // You should play with "*/*", "application/xml" and "application/json"
        // JSON might need a third party library to parse the response.
        
        // User Agent is the name of your application.
        connection.setRequestProperty("User-agent", "Pablo v1");
        // Some of the most common are Mozilla, Internet Explorer and GoogleBot.
        
        // If the response is OK
        if (connection.getResponseCode() < 300) {
            // OK, let us change the student
            
            // Response body from InputStream.
            InputSource inputSource = new InputSource(connection.getInputStream());
            
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true); // never forget this!
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputSource);
            
            NodeList list = doc.getChildNodes().item(0).getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                
                if (node.getNodeName().equals("age"))
                node.setTextContent("22");
            }
            
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource src = new DOMSource(doc);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            StreamResult res = new StreamResult(baos);
            transformer.transform(src, res);
            byte[] b = baos.toByteArray();
            
            connection.disconnect();


            // let's update
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("User-agent", "Pablo v1");
            connection.setRequestProperty("Content-Type", "application/xml");
            connection.setRequestProperty("Content-Length", "" + b.length);
            connection.setRequestProperty("Accept", "*/*");
            
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            
            dataOutputStream.write(b);
            dataOutputStream.close();

            System.out.println(new String(b, 0, b.length));

            connection.getInputStream();

            connection.disconnect();
        }    
    }
}
