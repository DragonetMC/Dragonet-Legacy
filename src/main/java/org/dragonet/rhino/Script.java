 /* GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 */
package org.dragonet.rhino;

import java.util.*;
import java.io.*;
import com.google.common.io.Files;
import java.nio.charset.Charset;
import org.mozilla.javascript.*;
import org.dragonet.DragonetServer;
import org.dragonet.rhino.api.functions.*;

/**
 *
 * @author TheMCPEGamer__ edited by Ash (QuarkTheAwesome)
 */
public class Script {

    public String name = "";

    //In order for the ScriptAPI to keep track of scripts without relying on filenames (which change)...
    /**
     * Unique name of script (not filename). Set by script.
     */
    public String UID = "";

    public String fullFilePath = "";

    public String fileContents = "";

    public File file;

    public Script(File scriptFile) {
        this.name = scriptFile.getName();

        this.fullFilePath = scriptFile.getAbsolutePath();

        this.fileContents = getScriptContents(scriptFile);

        this.file = scriptFile;

        this.UID = findScriptUID();
    }

    public File getFile() {
        return this.file;
    }

    public String getName() {
        return this.name;
    }

    public String getPath() {
        return this.fullFilePath;
    }

    private String getScriptContents(File f) {
        List<String> scriptContentList = new ArrayList<>();

        String scriptContents = new String();

        try {
            scriptContentList = Files.readLines(f, Charset.defaultCharset());
        } catch (IOException IOe) {
            System.out.println(IOe.getMessage());
        }

        for (String str : scriptContentList) {
            scriptContents += " " + str;
        }

        return scriptContents;
    }

    private String findScriptUID() {
        Object name = runFunction("getUID", new Object[]{});
        try {
            if ((String) name == null) {
                throw new ClassCastException();
            }
            return (String) name;
        } catch (ClassCastException e) {
            DragonetServer.instance().getLogger().warn("[DragonetAPI] Script " + this.name + " doesn't provide custom name!");
            DragonetServer.instance().getLogger().warn("[DragonetAPI] This script will not be able to use the ScriptAPI.");
    		//TODO Link details section for how this works
            //DragonetServer.instance().getLogger().warn("[DragonetAPI] See <URL> for details.");
            return "INVALID";
        }
    }

    public Object runFunction(String func, Object[] params) {
        BufferedReader script = null;

        try {
            script = new BufferedReader(new FileReader(this.getFile()));
        } catch (IOException IOe) {
            System.out.println(Arrays.toString(IOe.getStackTrace()));
        }

        Context context = Context.enter();
        Object result = null;

        try {
            ScriptableObject scope = context.initStandardObjects();

            try {
                Functions.defineFunctions(scope);
                context.evaluateReader(scope, script, "script", 1, null);
            } catch (IOException IOe) {
                System.out.println(Arrays.toString(IOe.getStackTrace()));
            }

            Object function = scope.get(func, scope);
            if (!(function instanceof UniqueTag)) {
                Function fct = (Function) function;
                result = fct.call(context, scope, scope, params);
            }
        } finally {
            Context.exit();
        }

        return result;
    }
}
