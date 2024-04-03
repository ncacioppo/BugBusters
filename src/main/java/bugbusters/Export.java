package bugbusters;

import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.*;

public class Export {

//    public static void main(String[] args) {
//        Search search = new Search();
//        ArrayList<Course> courses = new ArrayList<>(search.getAllCoursesFromExcel());
//        Term term = new Term("Spring", 2020);
//        Schedule schedule = new Schedule("Test", term, new ArrayList<>(courses.subList(10, 19)));
//
////        String xmlData = generateXMLData(schedule);
////        System.out.println(xmlData);
//        toPDF(schedule, "TEST2.pdf");
//
//    }

    private Export(){}

    public static Boolean toPDF(Schedule schedule, String filename){
        try {
            // Step 1: Prepare your XML Data (assuming xmlData is your XML string)
            String xmlData = generateXMLData(schedule);

            // Step 2: Setup Apache FOP
            FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());

            // Step 3: Generate PDF from XML using Apache FOP's internal renderer
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, outStream);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            Source src = new StreamSource(new ByteArrayInputStream(xmlData.getBytes()));
            Result res = new SAXResult(fop.getDefaultHandler());
            transformer.transform(src, res);

            // Step 4: Write the PDF output to a file or use as needed
            byte[] pdfBytes = outStream.toByteArray();
            // Example: Write PDF to a file
             FileOutputStream fos = new FileOutputStream(filename);
             fos.write(pdfBytes);
             fos.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String generateXMLData(Schedule schedule){
        try {
            ArrayList<Course> courses = new ArrayList<>(schedule.getCourses());
            Map<MeetingTime, Course> sortedCourseMap = new TreeMap<>();

            Map<Day, ArrayList<Course>> sortedDayMap = new TreeMap<>();

            for (Course course : courses) {
                for (MeetingTime meetTime : course.getMeetingTimes()) {
                    sortedCourseMap.put(meetTime, course);
                }
            }

            for (MeetingTime meetingTime : sortedCourseMap.keySet()) {
                if (sortedDayMap.keySet().contains(meetingTime.getDay())){
                    sortedDayMap.get(meetingTime.getDay()).add(sortedCourseMap.get(meetingTime));
                } else {
                    Course courseToAdd = sortedCourseMap.get(meetingTime);
                    ArrayList<Course> coursesToAdd = new ArrayList<>();
                    coursesToAdd.add(courseToAdd);
                    sortedDayMap.put(meetingTime.getDay(), coursesToAdd);
                }
            }

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

// Create an empty XSL-FO document
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElementNS("http://www.w3.org/1999/XSL/Format", "fo:root");
            doc.appendChild(rootElement);

// Create layout-master-set and simple-page-master elements
            Element layoutMasterSet = doc.createElementNS("http://www.w3.org/1999/XSL/Format", "fo:layout-master-set");
            rootElement.appendChild(layoutMasterSet);

            Element simplePageMaster = doc.createElementNS("http://www.w3.org/1999/XSL/Format", "fo:simple-page-master");
            simplePageMaster.setAttribute("master-name", "simple-page");
            layoutMasterSet.appendChild(simplePageMaster);

            // Add region-body element
            Element regionBody = doc.createElementNS("http://www.w3.org/1999/XSL/Format", "fo:region-body");
            simplePageMaster.appendChild(regionBody);

// Create fo:page-sequence element
            Element pageSequence = doc.createElementNS("http://www.w3.org/1999/XSL/Format", "fo:page-sequence");
            pageSequence.setAttribute("master-reference", "simple-page"); // Set the master-reference attribute
            rootElement.appendChild(pageSequence);

// Create fo:flow element to contain content
            Element flow = doc.createElementNS("http://www.w3.org/1999/XSL/Format", "fo:flow");
            flow.setAttribute("flow-name", "xsl-region-body"); // Set the flow-name attribute
            pageSequence.appendChild(flow);

// Iterate over sortedCourseMap and add content to fo:flow
            for (Day day : sortedDayMap.keySet()) {
                String addString = "\n" + day + ":\n";
                addString += "-----------------------------------------\n";
                ArrayList<Course> listedCourses = sortedDayMap.get(day);

                Element block = doc.createElementNS("http://www.w3.org/1999/XSL/Format", "fo:block");
                block.setAttribute("linefeed-treatment", "preserve");


                for (Course course : listedCourses){
                    addString += course.forPDf(day);
                    addString += "-----------------------------------------\n";
                }

                addString += "==============================================\n";

                block.setTextContent(addString);
                flow.appendChild(block);
            }

// Transform the XSL-FO document into a string
            StringWriter writer = new StringWriter();
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            return writer.toString();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
