package org.bildskript.parser.syntax;

/**
 *
 */
public class SyntaxItem {

	public static final int SPACE = 0;
	public static final int COMPONENT = 1;
	public static final int IN = 2;
	public static final int OUT = 3;
	public static final int REQUIRED_IN = 4;
	public static final int REQUIRED_OUT = 5;
	public static final int ANY = 6;

	public int type;
	public int additional;

}
