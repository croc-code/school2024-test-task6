package com.example.util;

public class CommandLineArgsUtil {

    public static String getInputFilePath(String[] args) {
        return parseArg(args, "--input");
    }

    public static String getOutputFilePath(String[] args) {
        return parseArg(args, "--output");
    }

    private static String parseArg(String[] args, String argName) {
        for (String arg: args) {
            String[] argArr = arg.split("=");
            if (argArr.length == 2 && argArr[0].equals(argName) && !argArr[1].isEmpty()) {
                return argArr[1];
            }
        }
        return "";
    }
}
