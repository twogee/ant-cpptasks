/*
 *
 * Copyright 2002-2004 The Ant-Contrib project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package net.sf.antcontrib.cpptasks.sun;


import net.sf.antcontrib.cpptasks.types.LibraryTypeEnum;

import java.util.Vector;

/**
 * A add-in class for Sun C89 compilers and linkers
 *
 * @author Hiram Chirino {@literal <cojonudo14@hotmail.com>}
 */
public class C89Processor {
    private static int addLibraryPatterns(String[] libnames, StringBuilder buf,
                                          String prefix, String extension, String[] patterns, int offset) {
        for (int i = 0; i < libnames.length; i++) {
            buf.setLength(0);
            buf.append(prefix);
            buf.append(libnames[i]);
            buf.append(extension);
            patterns[offset + i] = buf.toString();
        }
        return offset + libnames.length;
    }

    public static void addWarningSwitch(Vector<String> args, int level) {
        switch (level) {
            /*
             * case 0: args.addElement("/W0"); break;
             *
             * case 1: args.addElement("/W1"); break;
             *
             * case 2: break;
             *
             * case 3: args.addElement("/W3"); break;
             *
             * case 4: args.addElement("/W4"); break;
             */
        }
    }

    public static String getCommandFileSwitch(String cmdFile) {
        StringBuilder buf = new StringBuilder("@");
        if (cmdFile.contains(" ")) {
            buf.append('\"');
            buf.append(cmdFile);
            buf.append('\"');
        } else {
            buf.append(cmdFile);
        }
        return buf.toString();
    }

    public static void getDefineSwitch(StringBuilder buf, String define, String value) {
        buf.setLength(0);
        buf.append("-D");
        buf.append(define);
        if (value != null && !value.isEmpty()) {
            buf.append('=');
            buf.append(value);
        }
    }

    public static String getIncludeDirSwitch(String includeDir) {
        return "-I" + includeDir;
    }

    public static String[] getLibraryPatterns(String[] libnames, LibraryTypeEnum libType) {
        StringBuilder buf = new StringBuilder();
        int patternCount = libnames.length * 2;
        if (libType != null) {
            patternCount = libnames.length;
        }
        String[] patterns = new String[patternCount];
        int offset = 0;
        if (libType == null || "static".equals(libType.getValue())) {
            offset = addLibraryPatterns(libnames, buf, "lib", ".a", patterns, 0);
        }
        if (libType == null || !"static".equals(libType.getValue())) {
            offset = addLibraryPatterns(libnames, buf, "lib", ".so", patterns,
                    offset);
        }
        return patterns;
    }

    public static String[] getOutputFileSwitch(String outPath) {
        StringBuilder buf = new StringBuilder("-o ");
        if (outPath.contains(" ")) {
            buf.append('\"');
            buf.append(outPath);
            buf.append('\"');
        } else {
            buf.append(outPath);
        }
        return new String[]{buf.toString()};
    }

    public static void getUndefineSwitch(StringBuilder buf, String define) {
        buf.setLength(0);
        buf.append("-U");
        buf.append(define);
    }

    public static boolean isCaseSensitive() {
        return true;
    }

    private C89Processor() {
    }
}
