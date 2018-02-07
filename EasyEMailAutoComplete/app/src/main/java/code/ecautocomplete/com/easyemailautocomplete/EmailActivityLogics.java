package code.ecautocomplete.com.easyemailautocomplete;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by karthik on 2/6/2018.
 */

public class EmailActivityLogics {

    final public static List<String> emailStorageList = Arrays.asList("gmail.com", "yahoo.com", "ymail.com", "reddiff.com", "ggmail.com");

    public static boolean isEmailValid (String enteredEMail) {
        boolean isValid = false;
        String []atTheRateSplit = enteredEMail.split("@");
        if (atTheRateSplit.length == 2) {
            if (!atTheRateSplit[0].isEmpty() && !atTheRateSplit[1].isEmpty()) {
                String domainPart = atTheRateSplit[1];
                String[] domainSplit = domainPart.split("\\.");
                if (domainSplit.length == 2) {
                    if (!domainSplit[0].isEmpty() && domainSplit[1].equals("com")) {
                        isValid = true;
                    }
                }
            }
        }
        return isValid;
    }
    public static List<String> getMatchingEmails (String enteredEMail) {
        List<String> emailList = new ArrayList<String>();
        String []atTheRateSplit = enteredEMail.split("@");
        if (atTheRateSplit.length == 2) {
            if (!atTheRateSplit[1].isEmpty()) {
                for (String emailItem : emailStorageList) {
                    if (emailItem.startsWith(atTheRateSplit[1]) && !emailItem.equals(atTheRateSplit[1])) {
                        emailList.add(emailItem);
                    }
                }
            }
        } else if (enteredEMail.endsWith("@")) {
            emailList.addAll(emailStorageList);
        }
        return emailList;
    }

    public int getIndexUpToDomain (String emailString) {
        return emailString.indexOf("@");
    }
}
