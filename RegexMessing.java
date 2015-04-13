/**
 *
 * @author George
 */
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;

public class RegexMessing {

    //Get connection and return the page as a string.
    public String connectAndGetPage(String url) throws IOException {
        URL page = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) page.openConnection();
        conn.connect();
        InputStreamReader in = new InputStreamReader((InputStream) conn.getContent());
        BufferedReader buff = new BufferedReader(in);
        String line;
        StringBuilder sb = new StringBuilder("");
        while ((line = buff.readLine()) != null) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    // Get all tags on page, excluding closing tags and comments.
    public ArrayList<String> getTags(String url) {
        // Use hashset to remove duplicates
        Collection<String> tags = new HashSet<>();
        try {
            String page = connectAndGetPage(url);
            Pattern p = Pattern.compile("<[^>]*>");
            Matcher m = p.matcher(page);
            while (m.find()) {
                // Get match and add to hashset if not a comment or closing tag.
                String match = m.group(0);
                String characterAfterStart = String.valueOf(match.charAt(1));
                if (!characterAfterStart.equals("/") && !characterAfterStart.equals("!")) {
                    tags.add(match.trim());
                }
            }
        } catch (IOException e) {
            System.out.println("IOException");
        }
        return sortTags(tags);

    }
    
    public ArrayList<String> getScripts(String url){
        Collection<String> scripts = new HashSet<>();
        try {
            String page = connectAndGetPage(url);
            // [\\s\\S]*? the question mark stops the [\\s\\S]* from being greedy.
            Pattern p = Pattern.compile("<script[^>]*>[\\s\\S]*?</script>");
            Matcher m = p.matcher(page);
            while (m.find()){
                scripts.add(m.group(0));
            }
            
        } catch (IOException e) {
            System.out.println("IOException!!!");
        }
        
        return sortTags(scripts);
    }

    // Method to put matches in alphabetical and length order.
    public ArrayList<String> sortTags(Collection<String> tags) {
        // Transform set to list so can sort.
        ArrayList<String> list = new ArrayList<>(tags);

        // Sort alphabetically.
        Collections.sort(list);

        // Sort by length.
        Collections.sort(list, new Comparator<String>() {

            @Override
            public int compare(String x, String y) {

                int diff = x.length() - y.length();
                if (diff < 0) {
                    return -1;
                } else if (diff == 0) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        return list;
    }

    

    public static void main(String[] args){
        ArrayList<String> tags = new RegexMessing().getTags("https://uk.yahoo.com/?p=us");
        ArrayList<String> scripts = new RegexMessing().getScripts("https://uk.yahoo.com/?p=us");
        
        // print tags
        for (String tag : tags) {
            System.out.println(tag);
        }
        
        // Alternative functional foreach to print scripts
        scripts.stream().forEach((script) -> {
            System.out.println(script);
        });
        
        
    }
}
