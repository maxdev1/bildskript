package org.bildskript.parser.layout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.bildskript.interpretation.parts.components.Component;
import org.bildskript.parser.syntax.SyntaxItem;
import org.bildskript.parser.syntax.SyntaxList;

/**
 *
 */
public class LayoutFileLoader {

	/**
	 * @param f
	 */
	public static void loadSyntax(File f) {

		String line = null;
		BufferedReader reader = null;

		try {

			// Open layout file
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));

			Class<?> componentClass = null;
			List<SyntaxItem[]> lines = new ArrayList<>();

			// Read the layout file
			boolean first = true;
			while ((line = reader.readLine()) != null) {
				if (first) {
					componentClass = Class.forName(line);
					first = false;
				} else {

					while (line.contains("  ")) {
						line = line.replaceAll("  ", " ");
					}

					String[] parts = line.split(" ");
					SyntaxItem[] lineValues = new SyntaxItem[parts.length];

					int pos = 0;
					for (String part : parts) {
						SyntaxItem item = new SyntaxItem();

						// Convert parts to syntax items
						part = part.trim();
						if (part.length() == 0) {
							continue;
						}

						if (part.startsWith("x")) {
							item.type = SyntaxItem.COMPONENT;

						} else if (part.startsWith("ni")) {
							item.type = SyntaxItem.IN;
							item.additional = Integer.parseInt(part.substring(2));

						} else if (part.startsWith("no")) {
							item.type = SyntaxItem.OUT;
							item.additional = Integer.parseInt(part.substring(2));

						} else if (part.startsWith("i")) {
							item.type = SyntaxItem.REQUIRED_IN;
							item.additional = Integer.parseInt(part.substring(1));

						} else if (part.startsWith("o")) {
							item.type = SyntaxItem.REQUIRED_OUT;
							item.additional = Integer.parseInt(part.substring(1));

						} else if (part.startsWith(".")) {
							item.type = SyntaxItem.SPACE;

						} else {
							item.type = SyntaxItem.ANY;

						}

						lineValues[pos++] = item;
					}
					lines.add(lineValues);
				}
			}

			// Check
			if (lines.size() == 0) {
				System.err.println("layout file '" + f.getName()
						+ "' does not specify layout lines");
				return;
			}

			// Put into three-dimensional array
			SyntaxItem[][] syntaxResult = new SyntaxItem[lines.size()][];
			for (int i = 0; i < lines.size(); i++) {
				syntaxResult[i] = lines.get(i);
			}

			// Check type of loaded class
			if (Component.class.isAssignableFrom(componentClass)) {
				SyntaxList.addSyntax((Class<? extends Component>) componentClass, syntaxResult);
			} else {
				System.err.println("class '" + componentClass + "' defined for component layout '"
						+ f.getName() + "' is not a subtype of Component");
			}

		} catch (NumberFormatException e) {
			System.err.println("error in component layout '" + f.getName() + "'");

		} catch (IOException e) {
			System.err.println("could not read component layout '" + f.getName() + "'");

		} catch (ClassNotFoundException e) {
			System.err.println("could not find class '" + line + "' defined for component layout '"
					+ f.getName() + "'");
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
