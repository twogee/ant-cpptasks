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
package net.sf.antcontrib.cpptasks.os400;

import net.sf.antcontrib.cpptasks.OptimizationEnum;
import net.sf.antcontrib.cpptasks.compiler.AbstractCompiler;
import net.sf.antcontrib.cpptasks.compiler.CommandLineCCompiler;
import net.sf.antcontrib.cpptasks.compiler.LinkType;
import net.sf.antcontrib.cpptasks.compiler.Linker;
import net.sf.antcontrib.cpptasks.compiler.Processor;
import org.apache.tools.ant.types.Environment;

import java.io.File;
import java.util.Vector;

import static net.sf.antcontrib.cpptasks.CUtil.getPathFromEnvironment;

/**
 * Adapter for the IBM (R) OS/390 (tm) C++ Compiler
 *
 * @author Hiram Chirino {@literal <cojonudo14@hotmail.com>}
 */
public class IccCompiler extends CommandLineCCompiler {
    private static final AbstractCompiler instance = new IccCompiler(false,
            null);

    public static AbstractCompiler getInstance() {
        return instance;
    }

    private IccCompiler(boolean newEnvironment, Environment env) {
        super("icc", null, new String[]{".c", ".cc", ".cpp", ".cxx", ".c++", ".s"},
                new String[]{".h", ".hpp"}, ".o", false, null, newEnvironment, env);
    }

    protected void addImpliedArgs(final Vector<String> args,
                                  final boolean debug,
                                  final boolean multithreaded,
                                  final boolean exceptions,
                                  final LinkType linkType,
                                  final Boolean rtti,
                                  final OptimizationEnum optimization) {
        // Specifies that only compilations and assemblies be done.
        //  Link-edit is not done
        args.addElement("-c");
        /*
         * if (exceptions) { args.addElement("/GX"); }
         */
        if (debug) {
            args.addElement("-g");
            /*
             * args.addElement("-D"); args.addElement("_DEBUG"); if
             * (multithreaded) { args.addElement("/D_MT"); if (staticLink) {
             * args.addElement("/MTd"); } else { args.addElement("/MDd");
             * args.addElement("/D_DLL"); } } else { args.addElement("/MLd"); }
             */
        } else {
            /*
             * args.addElement("-D"); args.addElement("NEBUG"); if
             * (multithreaded) { args.addElement("/D_MT"); if (staticLink) {
             * args.addElement("/MT"); } else { args.addElement("/MD");
             * args.addElement("/D_DLL"); } } else { args.addElement("/ML"); }
             */
        }
    }

    protected void addWarningSwitch(Vector<String> args, int level) {
        IccProcessor.addWarningSwitch(args, level);
    }

    public Processor changeEnvironment(boolean newEnvironment, Environment env) {
        if (newEnvironment || env != null) {
            return new IccCompiler(newEnvironment, env);
        }
        return this;
    }

    /*
     * @see CommandLineCompiler#getDefineSwitch(StringBuilder, String, String)
     */
    protected void getDefineSwitch(StringBuilder buffer, String define, String value) {
        buffer.append("-q");
        buffer.append(define);
        if (value != null && !value.isEmpty()) {
            buffer.append('=');
            buffer.append(value);
        }
    }

    protected File[] getEnvironmentIncludePath() {
        return getPathFromEnvironment("INCLUDE", ":");
    }

    protected String getIncludeDirSwitch(String includeDir) {
        return IccProcessor.getIncludeDirSwitch(includeDir);
    }

    public Linker getLinker(LinkType type) {
        return IccLinker.getInstance().getLinker(type);
    }

    public int getMaximumCommandLength() {
        return Integer.MAX_VALUE;
    }

    /* Only compile one file at time for now */
    protected int getMaximumInputFilesPerCommand() {
        return 1;
        //return Integer.MAX_VALUE;
    }

    /*
     * @see CommandLineCompiler#getUndefineSwitch(StringBuilder, String)
     */
    protected void getUndefineSwitch(StringBuilder buffer, String define) {
        /*
         * buffer.addElement("-q"); buf.append(define);
         */
    }
}
