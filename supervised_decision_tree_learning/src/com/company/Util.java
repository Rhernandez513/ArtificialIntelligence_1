package com.company;

import com.company.enums.Patrons;
import com.company.enums.Type;
import com.company.enums.Price;
import com.company.enums.WaitEstimate;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Util {

    static String readFile(String path) {
        StringBuilder builder = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String a;
            while((a = reader.readLine()) != null) {
                builder.append(a);
                builder.append("\n");
                a = null;
            }
        } catch (FileNotFoundException e) {
            System.err.println(String.format("File not found at=%s", path));
        } catch (IOException e) {
            System.err.println(String.format("IOException", e.getMessage()));
        }
        return builder.toString();
    }

    static List<Example> map(String input) throws ApplicationRuntimeException {
        String [] lines = input.split("\n");
        List<Example> attributes = new ArrayList<>();
        for(String s : lines) {
            String [] data = s.replaceAll("\\s", "").split(",");
            Example atr = new Example();
            atr.setAlternative(yesOrNo(data[0]));
            atr.setBar(yesOrNo(data[1]));
            atr.setFridayOrSaturday(yesOrNo(data[2]));
            atr.setHungry(yesOrNo(data[3]));
            atr.setPatrons(parsePatrons(data[4]));
            atr.setPrice(parsePrice(data[5]));
            atr.setRaining(yesOrNo(data[6]));
            atr.setReservation(yesOrNo(data[7]));
            atr.setType(parseType(data[8]));
            atr.setWaitEstimate(parseWaitEstimate(data[9]));
            atr.setWillWait(yesOrNo(data[10]));
            attributes.add(atr);
        }
        return attributes;
    }

    private static boolean yesOrNo(String in) {
        return in.equals("Yes") ? true : false;
    }

    private static WaitEstimate parseWaitEstimate(String s) throws ApplicationRuntimeException {
        if(s.equals("0-10")) {
            return WaitEstimate.Short;
        } else if (s.equals("10-30")) {
            return WaitEstimate.Moderate;
        } else if (s.equals("30-60")) {
            return WaitEstimate.Long;
        } else if (s.equals(">60")) {
            return WaitEstimate.Extreme;
        }
        throw new ApplicationRuntimeException(String.format("WaitEstimate parse error, was=%s", s));
    }

    private static Patrons parsePatrons(String s) throws ApplicationRuntimeException {
        if(s.equals("None")) {
            return Patrons.None;
        } else if (s.equals("Some")) {
            return Patrons.Some;
        } else if (s.equals("Full")) {
            return Patrons.Full;
        }
        throw new ApplicationRuntimeException(String.format("Patron parse error, was=%s", s));
    }

    private static Price parsePrice(String s) throws ApplicationRuntimeException {
        if(s.equals("$")) {
            return Price.$;
        } else if (s.equals("$$")) {
            return Price.$$;
        } else if (s.equals("$$$")) {
            return Price.$$$;
        }
        throw new ApplicationRuntimeException(String.format("Price parse error, was=%s", s));
    }
    private static Type parseType(String s) throws ApplicationRuntimeException {
        if(s.equals("Burger")) {
            return Type.Burger;
        } else if (s.equals("Thai")) {
            return Type.Thai;
        } else if (s.equals("Italian")) {
            return Type.Italian;
        } else if (s.equals("French")) {
            return Type.French;
        }
        throw new ApplicationRuntimeException(String.format("Type parse error, was=%s", s));
    }
}
