/*
 * (c) 2015 The Dragonet Team
 * All rights reserved.
 */
package org.dragonet.rhino;

import java.util.*;
import java.io.*;
import com.google.common.io.Files;
import java.nio.charset.Charset;
import org.mozilla.javascript.*;
import org.dragonet.rhino.api.functions.*;

/**
 *
 * @author TheMCPEGamer__
 */
public class Script {
	public String name = "";

	public String fullFilePath = "";

	public String fileContents = "";

	public File file;

	public Script(File scriptFile) {
		this.name = scriptFile.getName();

		this.fullFilePath = scriptFile.getAbsolutePath();

		this.fileContents = getScriptContents(scriptFile);

		this.file = scriptFile;
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
		}

		catch (IOException IOe) {
			System.out.println(IOe.getMessage());
		}

		for (String str : scriptContentList) {
			scriptContents += " " + str;
		}

		return scriptContents;
	}

	public Object runFunction(String func, Object[] params) {
		BufferedReader script = null;

		try {
			script = new BufferedReader(new FileReader(this.getFile()));
		}

		catch (IOException IOe) {
			System.out.println(Arrays.toString(IOe.getStackTrace()));
		}

		Context context = Context.enter();
		Object result = null;

		try {
			ScriptableObject scope = context.initStandardObjects();

			try {
				Functions.defineFunctions(scope);
				context.evaluateReader(scope, script, "script", 1, null);
			}

			catch (IOException IOe) {
				System.out.println(Arrays.toString(IOe.getStackTrace()));
			}

			Object function = scope.get(func, scope);
			if (!(function instanceof UniqueTag)) {
				Function fct = (Function) function;
				result = fct.call(context, scope, scope, params);
			}
		}

		finally {
			Context.exit();
		}
		return result;
	}
}
