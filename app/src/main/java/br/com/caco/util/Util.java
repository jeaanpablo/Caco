package br.com.caco.util;

/**
 * Created by Jean Pablo Bosso on 21/06/2015.
 */
import android.content.Context;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import br.com.caco.R;

public class Util {

    private static final Pattern trimWhiteSpaces = Pattern.compile("[\\s]+");
    private static final Pattern removeInlineComments = Pattern.compile("#");
    private static final Pattern splitBySpace = Pattern.compile(" ");




    public final static List<String> fastSplit(final String text, char separator, final boolean emptyStrings) {
        final List<String> result = new ArrayList<String>();

        if (text != null && text.length() > 0) {
            int index1 = 0;
            int index2 = text.indexOf(separator);
            while (index2 >= 0) {
                String token = text.substring(index1, index2);
                result.add(token);
                index1 = index2 + 1;
                index2 = text.indexOf(separator, index1);
            }

            if (index1 < text.length() - 1) {
                result.add(text.substring(index1));
            }
        }//else: input unavailable

        return result;
    }
    /**
     * returns a canonical line of a obj or mtl file.
     * e.g. it removes multiple whitespaces or comments from the given string.
     * @param line
     * @return
     */
    public static final String getCanonicalLine(String line) {
        line = trimWhiteSpaces.matcher(line).replaceAll(" ");
        if(line.contains("#")) {
            String[] parts = removeInlineComments.split(line);
            if(parts.length > 0)
                line = parts[0];//remove inline comments
        }
        return line;
    }

    public static String getRegId(Context context)
    {
        InstanceID instanceID = InstanceID.getInstance(context);
        String token ="";
        try {
             token = instanceID.getToken(context.getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return token;
    }


}

