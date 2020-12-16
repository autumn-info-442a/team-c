package me.victoria.school.madlibs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ProfanityCheck {
    static Map<String, String[]> wordList = new HashMap<>();
    static int largestWordLength = 0;

    public static void load() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL("https://docs.google.com/spreadsheets/d/1hIEi2YG3ydav1E06Bzf2mQbGZ12kh2fe4ISgLg_UBuM/export?format=csv").openConnection().getInputStream()));
            String line = "";
            int counter = 0;
            while ((line = bufferedReader.readLine()) != null) {
                counter++;
                String[] content = null;
                try {
                    content = line.split(",");
                    if (content.length == 0) {
                        continue;
                    }
                    String word = content[0];
                    String[] ignore = new String[]{};
                    if (content.length > 1) {
                        ignore = content[1].split("_");
                    }

                    if (word.length() > largestWordLength) {
                        largestWordLength = word.length();
                    }
                    wordList.put(word.replaceAll(" ", ""), ignore);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isBadWord(String word) {
        word = word.toLowerCase().replaceAll("[^a-zA-Z]", "");
        for(int start = 0; start < word.length(); start++) {
            for(int offset = 1; offset < (word.length()+1 - start) && offset < largestWordLength; offset++)  {
                String wordToCheck = word.substring(start, start + offset);
                if(wordList.containsKey(wordToCheck)) {
                    String[] ignoreCheck = wordList.get(wordToCheck);
                    boolean ignore = false;
                    for (String value : ignoreCheck) {
                        if (word.contains(value)) {
                            ignore = true;
                            break;
                        }
                    }
                    if(!ignore) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
