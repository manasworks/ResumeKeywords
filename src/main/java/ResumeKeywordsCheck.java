import java.io.FileInputStream;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import java.util.*;
import java.util.stream.Collectors;

public class ResumeKeywordsCheck {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println("========================================================");
        System.out.println("Enter full path to Resume. Example: \"C:/myresume.docx\"");
        System.out.println("========================================================");
        System.out.print("File path: ");
        String path=s.next();
        s.close();
        String resume = convertTextFileToString(path);
        resumeCheck(resume);

    }

    static void resumeCheck(String resume){

        List<String> keywords = new ArrayList<>(Arrays.asList("SDET", "QA", "Java", "Selenium WebDriver", "JUnit", "Maven",
                "Cucumber", "Gherkin", "SQL", "JDBC", "TestNG", "Postman", "REST Assured Library", "API Web Services testing",
                "API Testing", "REST Assured with Java", "Rest Assured API", "Behavioral Driven Development", "BDD",
                "Page Object Model", "POM", "Web and Client-Server software applications", "Web-based applications",
                "Functional and Automation Testing", "Database Testing", "Jira", "Git", "GitHub", "Jenkins", "HTML", "CSS",
                "Mobile Testing", "Apium", "IntelliJ/Eclipse", "Oracle Database", "Object-Oriented Programming", "OOP",
                "Test Scenarios", "Test Cases", "Functional Testing", "Regression Testing", "Smoke Testing", "Positive and Negative Testing",
                "Software Development Life Cycle", "SDLC", "Software Test Life Cycle", "STLC", "Defect Life Cycle", "BLC",
                "Agile/Scrum", "Waterfall", "Sprint Planning Meeting", "Daily Standup Meeting", "Sprint Review Meeting",
                "Sprint Retrospective", "Sprint Demo", "Extent-Reports", "HTML reports", "Cucumber JSON reports"));
        /**
         * Buzzwords from https://www.thebalancecareers.com/resume-buzzwords-to-avoid-1287368
         */
        List<String> buzzwords = new ArrayList<>(Arrays.asList("Extensive experience", "Innovative", "Motivated", "Results-oriented",
                "Dynamic", "Proven track record", "Team player", "Fast-paced", "Problem solver", "Entrepreneurial", "Best-in-Breed",
                "Best-in-Class", "Bottom-Line Oriented", "Client Focused", "Creative Thinker", "Cutting Edge", "Detail Oriented",
                "Driven Professional", "Dynamic", "Entrepreneurial", "Evangelist", "Extensive Experience", "Fast Paced", "Go-To Person",
                "Goal Oriented", "Guru", "Highly Skilled", "Innovative", "Motivated", "Multi-Tasker", "Out-of-the-Box", "Perfectionist",
                "Proactive", "Problem Solver", "Proven Track Record", "Quality Driven", "Quick Learner", "Results-Oriented", "Road Warrior",
                "Seasoned Professional", "Self-Starter", "Skill Set", "Strategic Thinker", "Strong Work Ethic", "Team Player", "Tiger Team",
                "Trustworthy", "Value Add", "Works Well Under Pressure", "Works Well With Others"));


        String[] words = resume.replaceAll("\\p{Punct}", "").toLowerCase().split("\\s+");
        Map<String, Integer> allWords = new HashMap<>();
        for (String word : words) {
            if (allWords.containsKey(word)) allWords.put(word, allWords.get(word)+1);
            else allWords.put(word, 1);
        }

        Map allWordsSorted = sort(allWords);
        Map<String, Integer> map = new HashMap<>();
        List<String> missingKeywords = new ArrayList<>();

        for (String keyword : keywords) {
            int count = 0, fromIndex = 0;
            while ((fromIndex = resume.indexOf(keyword, fromIndex)) != -1 ){
                count++;
                fromIndex++;
            }
            if (count>0) map.put(keyword, count);
            else missingKeywords.add(keyword);
        }

        Map<String, Integer> buzzMap = new HashMap<>();
        String resumeLower=resume.toLowerCase();
        for (String keyword : buzzwords) {
            int count = 0, fromIndex = 0;
            while ((fromIndex = resumeLower.indexOf(keyword.toLowerCase(), fromIndex)) != -1 ){
                count++;
                fromIndex++;
            }
            if (count>0) buzzMap.put(keyword, count);
        }

        Map sorted = sort(map);
        Map buzzs = sort(buzzMap);



        System.out.println(allWordsSorted);
        System.out.println("======================================================");
        System.out.print("Buzzwords found: ");
        System.out.println(buzzs);
        System.out.println("======================================================");
        System.out.print("Keywords found: ");
        System.out.println(sorted);
        System.out.print("\nMissing keywords:");
        System.err.println(missingKeywords);
        System.err.println("\nTotal resume words count: "+words.length+"\nTotal buzzword to avoid found: "+buzzs.size());

    }

    public static String convertTextFileToString(String fileName) {
        String text="";
        try(FileInputStream fis = new FileInputStream(fileName)) {
            XWPFDocument file   = new XWPFDocument(OPCPackage.open(fis));
            XWPFWordExtractor ext = new XWPFWordExtractor(file);
            text=ext.getText();
        }catch(Exception e) {
            System.out.println(e);
        }
        return text;
    }

    public static Map sort(Map<String, Integer> map){
        LinkedHashMap<String, Integer> sorted = map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue,
                        (e1, e2) -> null, LinkedHashMap::new));

        return sorted;
    }
}
