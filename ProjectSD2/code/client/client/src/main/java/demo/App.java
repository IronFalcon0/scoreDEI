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

import java.net.URLEncoder;

/**
 * Hello world!
 *
 */
public class App {
        public static void main(String[] args)
                        throws IOException, XPathExpressionException, ParserConfigurationException,
                        SAXException, TransformerException {

                /*
                 * // Host url
                 * String host = "https://v3.football.api-sports.io/";
                 * String charset = "UTF-8";
                 * // Headers for a request
                 * String x_rapidapi_host = "v3.football.api-sports.io";
                 * String x_rapidapi_key = "1d62131e48800ca55b3698f34fded5a0";// Type here your
                 * key
                 * // Params
                 * String s = "Tirana";
                 * // Format query for preventing encoding problems
                 * String query = String.format("s=%s",
                 * URLEncoder.encode(s, charset));
                 * 
                 * // ----------------------
                 * 
                 * HttpResponse<JsonNode> response = Unirest.get(host + "?" +
                 * query).header("x-rapidapi-host", x_rapidapi_host)
                 * .header("x-rapidapi-key", x_rapidapi_key).asJson();
                 * System.out.println(response.getStatus());
                 * System.out.println(response.getHeaders().get("Content-Type"));
                 * 
                 * // ---------------------
                 * 
                 * // Host, charset and headers vars should be the same
                 * String i = "tt0110912";
                 * // Format query for preventing encoding problems
                 * query = String.format("i=%s",
                 * URLEncoder.encode(i, charset));
                 * // Json response
                 * HttpResponse<JsonNode> response1 = Unirest.get(host + "?" +
                 * query).header("x-rapidapi-host", x_rapidapi_host)
                 * .header("x-rapidapi-key", x_rapidapi_key).asJson();
                 * // Prettifying
                 * Gson gson = new GsonBuilder().setPrettyPrinting().create();
                 * JsonParser jp = new JsonParser();
                 * JsonElement je = jp.parse(response1.getBody().toString());
                 * String prettyJsonString = gson.toJson(je);
                 * System.out.println(prettyJsonString);
                 * 
                 */

                // ----------------------

                URL url = new URL("https://v3.football.api-sports.io/teams");
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
