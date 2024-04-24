package bugbusters.Scraping;

import bugbusters.Major;
import bugbusters.Minor;
import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.ArrayList;
import java.util.Collection;

import static bugbusters.Scraping.Web.chromeTest;

public class MyGCC {

    private static final String url = "https://login.microsoftonline.com/83918960-2218-4cd3-81fe-302a8e771da9/saml2?SAMLRequest=fVJRb5swGHxepf4H5HdjTFggVtIoSzQtUruhku1hL5OxDXjBNrNN1P77AUuUSu36%2bvnuu%2b%2fOt1w%2fqTY4Ceuk0SuAwwgEQjPDpa5XoPcVzMD67vZm6ahqO7LpfaMfxZ9eOB%2fc3nwYyNqR6W1AW00MddIRTZVwxDNSbB7uSRxGpLPGG2ZaMJD2uxX4xXV3lI0xnWqqqmoNV6Yu62Pzuz5yVXY1GNf%2fuNw1rBiZu0FWauqnWeN95whCramlDpVk1jhTeaNbqUXIjELZbIGzxTyCcYwzmDA%2bgxmuBJxFMc1EmmJOF2g8Pp7Ocq4Xe%2b081X5QjOIERjOI40MUkwSTj%2bnPEZWfjXyS%2bpzRO67LfyBHvhwOOcy%2fFYfzipPkwn4d8Fcb6jmsGQsF75FkbsRtnBN29Lo12vVK2ELYk2Ti%2b%2bP9m7T9tkCFH9JhOa2FQ%2bMZ6My5aKLNtgip657WSvjG8D1fMVpmCc7mkOO4gkmEU1imaQkpm2PGaCpitgBDBYJgKgGZcrLB9evfz4BeXIC7t60u0Yu1V52OjPnsd7lpJXsOPhurqP%2b%2fFA7xNJEcVhOU9Np1gslKCg4CNHYYvS7xMP4L&RelayState=%2fICS";
    private String email;
    private String password;

    public String authenticationNum = "0";
    public ArrayList<Major> majors = new ArrayList<>();
    public ArrayList<Minor> minors = new ArrayList<>();

    public MyGCC(String username, String password){
        setEmail(username);
        setPassword(password);
    }

    private void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return email;
    }

    private void setPassword(String password){
        this.password = password;
    }

    public Pair<ArrayList<Major>, ArrayList<Minor>> getInfo(){

        try {
            RemoteWebDriver driver = Web.chrome(5);

            if(logIn(driver)) {

                //https://my.gcc.edu/ICS/My_Info

                //Click the Academics tab
                Web.xPath.click(driver, "/html/body/div[1]/form/header/div/div[2]/div/nav/div/div/div/ul/li[2]/a\n");
                //Click the view all details link for degree progress
                Web.ID.click(driver, "pg5_V_btnViewDetails");
                //Get text containing majors and minors
                String majorMinrString = Web.xPath.getText(driver, "/html/body/div[1]/form/div[5]/div/div/div/div[3]/div/div/div/div[2]/div/div[2]\n");
                String[] majorMinors = majorMinrString.split("Advisors")[0].split("\n");
                //Get text for requirements year
                String requirementsYear = Web.xPath.getText(driver, "/html/body/div[1]/form/div[5]/div/div/div/div[3]/div/div/div/div[2]/div/div[4]/table/tbody/tr[2]/td[2]\n");

                for (String string : majorMinors) {
                    if (string.toUpperCase().contains("MAJOR")) {
                        majors.add(new Major(string.split(":")[1].strip(), Integer.parseInt(requirementsYear)));
                    } else if (string.toUpperCase().contains("MINOR")) {
                        minors.add(new Minor(string.split(":")[1].strip(), Integer.parseInt(requirementsYear)));
                    }
                }
            } else {
                return Pair.of(new ArrayList<>((Collection) new Major("Failed", 1000)), new ArrayList<>((Collection) new Minor("Failed", 1000)));
            }
            return Pair.of(majors, minors);
        } catch (Exception e){
            return Pair.of(new ArrayList<>((Collection) new Major("Failed", 1000)), new ArrayList<>((Collection) new Minor("Failed", 1000)));
        }
    }

    private boolean logIn(RemoteWebDriver driver){

        try {
            driver.get(url);

            Web.ID.type(driver, "i0116", email);
            Web.hitEnter(driver);

            try {
                Web.ID.type(driver, "i0118", password);
            } catch (org.openqa.selenium.StaleElementReferenceException ex) {
                Web.ID.type(driver, "i0118", password);
            }
            Web.hitEnter(driver);

            while (Web.ID.exists(driver, "idRichContext_DisplaySign")) {
                if (authenticationNum.equalsIgnoreCase("0")) {
                    authenticationNum = Web.ID.getText(driver, "idRichContext_DisplaySign");
                    System.out.println("Authenticator number: " + authenticationNum);
                }
            }


            boolean clicked = false;

            while (!clicked) {

                boolean found = false;

                try {
                    while (!Web.ID.exists(driver, "idBtn_Back")){}
                    if (Web.ID.exists(driver, "idBtn_Back")){
                        found = true;
                    }
                } catch (org.openqa.selenium.StaleElementReferenceException ex) {

                }

                try {
                    Web.ID.carefulClick(driver, "idBtn_Back");
                    clicked = true;
                } catch (org.openqa.selenium.StaleElementReferenceException ex) {
                    clicked = false;
                }
            }

            return true;
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            return false;
        }
    }
}
