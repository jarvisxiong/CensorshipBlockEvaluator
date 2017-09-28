package com.global.elastic.builder;

import org.elasticsearch.script.Script;

public class ScriptBuilder {

	/**
	 * Creates the script.
	 *
	 * @param fieldName
	 *            the field name
	 * @param newValue
	 *            the new value
	 * @return the script
	 */
	public static Script createScript(String fieldName, String newValue) {
		Script script = new Script("ctx._source." + fieldName + "=\"" + newValue + "\"");
		return script;
	}

}
