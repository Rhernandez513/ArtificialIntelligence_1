package com.company;

import java.util.List;

public class Main {

    public static void main(String[] args) throws ApplicationRuntimeException {
        final String path = "resources\\restaurant.csv";
        String contents = Util.readFile(path);
        List<Attributes> atrList = Util.map(contents);
        for(Attributes a : atrList) {
            System.out.println(a);
        }
    }
}
