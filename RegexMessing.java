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

    //Get connection and return a buffered reader.
    public BufferedReader connectAndGetReader(String url) throws IOException {
        URL page = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) page.openConnection();
        conn.connect();
        InputStreamReader in = new InputStreamReader((InputStream) conn.getContent());
        return new BufferedReader(in);
    }

    // Method to find tags checking line by line.
    public Collection<String> getTagsMethod1(BufferedReader buff) throws IOException {
        // Pattern to match tags. 
        Pattern p = Pattern.compile("<[^>]*>");

        // Hashset to remove duplicate tags.
        Collection<String> tags = new HashSet<>();
        String line;
        // Read in and search line by line.
        while ((line = buff.readLine()) != null) {
            Matcher m = p.matcher(line);
            while (m.find()) {
                // Get matches, remove closed tags and comments.
                String match = m.group(0);
                String characterAfterStart = String.valueOf(match.charAt(1));
                if (!characterAfterStart.equals("/") && !characterAfterStart.equals("!")) {
                    tags.add(match.trim());
                }
            }
        }

        return tags;

    }

    // The same as previous method but the whole page is searched as a single string.
    // this catches multiline matches.
    public Collection<String> getTagsMethod2(BufferedReader buff) throws IOException {
        Pattern p = Pattern.compile("<[^>]*>");
        Collection<String> tags = new HashSet<>();
        String line;
        StringBuilder sb = new StringBuilder("");
        while ((line = buff.readLine()) != null) {
            sb.append(line).append("\n");
        }
        String allLines = sb.toString();
        Matcher m = p.matcher(allLines);
        while (m.find()) {
            String match = m.group(0);
            String characterAfterStart = String.valueOf(match.charAt(1));
            if (!characterAfterStart.equals("/") && !characterAfterStart.equals("!")) {
                tags.add(match.trim());
            }
        }

        return tags;

    }

    // Method to put matches in alphabetical and length order.
    public ArrayList sortTags(Collection<String> tags) {
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

    // Get the tags using one of the above methods. Used so that it's easier to
    // add different methods at a later date.
    public ArrayList getTags(String url, int methodNumber) {

        Collection<String> tags = new HashSet();
        try ( // Get connection.
                BufferedReader buff = connectAndGetReader(url)) {

            switch (methodNumber) {
                case 1:
                    tags = getTagsMethod1(buff);
                    break;
                case 2:
                    tags = getTagsMethod2(buff);
                    break;
            }

        } catch (IOException e) {
            System.out.println("IOException!!!");
        }
        return sortTags(tags);
    }

    public static void main(String[] args) throws Exception {
        ArrayList<String> tags1 = new RegexMessing().getTags("https://uk.yahoo.com/?p=us", 1);
        ArrayList<String> tags2 = new RegexMessing().getTags("https://uk.yahoo.com/?p=us", 2);

        System.out.println("Method1: " + tags1.size() + " Method2: " + tags2.size());

        // print tags which are only on one line. Not perfect.
        for (String tag : tags1) {
            System.out.println(tag);
        }

    }
}
