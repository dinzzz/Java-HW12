package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Class that represents the lexer that provides a lexical analysis based on a
 * given text. The class uses a character field to store each character from a
 * text, SmartScriptToken as a representation of each token that our language
 * accepts, integer that stores the currentIndex of an unanalyzed character and
 * state of the lexer. In the end parser takes the stored tokens from the Lexer
 * and creates a document body. The class should be optimized by adding more
 * methods that prevent code redundancy.
 * 
 * @author Dinz
 *
 */
public class SmartScriptLexer {
	/**
	 * Character field that stores character from an input.
	 */
	private char[] data; // ulazni tekst
	/**
	 * Current analyzed token.
	 */
	private SmartScriptToken token; // trenutni token
	/**
	 * Index of a token next in line for analysis.
	 */
	private int currentIndex; // indeks prvog neobrađenog znaka
	/**
	 * State of a lexer.
	 */
	private SmartScriptLexerState state;

	/**
	 * Constructs a new SmartScriptLexer that analyze the input text.
	 * 
	 * @param text
	 *            Input text.
	 */
	public SmartScriptLexer(String text) {
		if (text == null) {
			throw new IllegalArgumentException("String must not be null");
		}
		this.data = text.toCharArray();
		this.state = SmartScriptLexerState.TEXT;
	}

	/**
	 * Method that processes the next token from a text. Key method of the class
	 * which iterates the input characters, creates tokens and stores them for a
	 * parser to process. Also switches the state of a lexer when it's needed.
	 * Returns an analyzed token.
	 * 
	 * @return Analyzed SmartScriptToken.
	 * @throws SmartScriptLexerException
	 *             if the input is invalid.
	 */
	public SmartScriptToken nextToken() {
		if (currentIndex > data.length) {
			throw new SmartScriptLexerException("No more tokens available.");
		}
		if (currentIndex == data.length) {
			currentIndex++;
			SmartScriptToken newToken = new SmartScriptToken(SmartScriptTokenType.EOF, null);
			this.token = newToken;
			return newToken;
		}
		if (Character.toString(data[currentIndex]).equals("{")
				&& Character.toString(data[currentIndex + 1]).equals("$")) {
			state = SmartScriptLexerState.TAG;
		}

		if (state == SmartScriptLexerState.TAG) {
			if (Character.toString(data[currentIndex]).equals("{")
					&& Character.toString(data[currentIndex + 1]).equals("$")) {
				currentIndex += 2;
				SmartScriptToken newToken = new SmartScriptToken(SmartScriptTokenType.STARTTAG, "{$");
				this.token = newToken;
				return newToken;
			}

			else if (Character.toString(data[currentIndex]).equals("$")
					&& Character.toString(data[currentIndex + 1]).equals("}")) {
				currentIndex += 2;
				this.setState(SmartScriptLexerState.TEXT);
				SmartScriptToken newToken = new SmartScriptToken(SmartScriptTokenType.ENDTAG, "$}");
				this.token = newToken;
				return newToken;
			} else if ((currentIndex < data.length - 2) && Character.toString(data[currentIndex]).equals("F")
					&& Character.toString(data[currentIndex + 1]).equals("O")
					&& Character.toString(data[currentIndex + 2]).equals("R")) {
				currentIndex += 3;
				SmartScriptToken newToken = new SmartScriptToken(SmartScriptTokenType.FOR, "FOR");
				this.token = newToken;
				return newToken;

			} else if ((currentIndex < data.length - 2) && Character.toString(data[currentIndex]).equals("E")
					&& Character.toString(data[currentIndex + 1]).equals("N")
					&& Character.toString(data[currentIndex + 2]).equals("D")) {
				currentIndex += 3;
				SmartScriptToken newToken = new SmartScriptToken(SmartScriptTokenType.END, "END");
				this.token = newToken;
				return newToken;
			} else if (Character.toString(data[currentIndex]).equals("=")) {
				currentIndex++;
				SmartScriptToken newToken = new SmartScriptToken(SmartScriptTokenType.TAGNAME, "=");
				this.token = newToken;
				return newToken;
			} else if (Character.toString(data[currentIndex]).equals("+")
					|| Character.toString(data[currentIndex]).equals("-")
					|| Character.toString(data[currentIndex]).equals("*")
					|| Character.toString(data[currentIndex]).equals("/")
					|| Character.toString(data[currentIndex]).equals("^")) {
				if (Character.toString(data[currentIndex]).equals("-") && Character.isDigit(data[currentIndex + 1])) {
					StringBuilder tokenBuilder = new StringBuilder();
					tokenBuilder.append(data[currentIndex]);
					currentIndex++;
					while (!Character.toString(data[currentIndex]).equals(" ")) {
						tokenBuilder.append(data[currentIndex]);
						currentIndex++;
					}
					if (isInteger(tokenBuilder.toString())) {
						SmartScriptToken newToken = new SmartScriptToken(SmartScriptTokenType.INTEGER,
								Integer.parseInt(tokenBuilder.toString()));
						this.token = newToken;
						return newToken;
					} else if (isDouble(tokenBuilder.toString())) {
						SmartScriptToken newToken = new SmartScriptToken(SmartScriptTokenType.DOUBLE,
								Double.parseDouble(tokenBuilder.toString()));
						this.token = newToken;
						return newToken;
					} else {
						throw new SmartScriptLexerException("Wrong number format");
					}
				} else {
					SmartScriptToken newToken = new SmartScriptToken(SmartScriptTokenType.OPERATOR,
							Character.toString(data[currentIndex]));
					this.token = newToken;
					currentIndex++;
					return newToken;
				}

			} else if (Character.toString(data[currentIndex]).equals("@")) {
				StringBuilder tokenBuilder = new StringBuilder();
				tokenBuilder.append(data[currentIndex]);
				currentIndex++;
				while (!Character.toString(data[currentIndex]).equals(" ") && !Character.toString(data[currentIndex]).equals("\r")) {
					if (Character.isLetter(data[currentIndex]) || Character.isDigit(data[currentIndex])
							|| Character.toString(data[currentIndex]).equals("_")) {
						tokenBuilder.append(data[currentIndex]);
						currentIndex++;
					} else {
						System.out.println(data[currentIndex-2]);
						System.out.println(data[currentIndex-1]);
						System.out.println(data[currentIndex]);
						System.out.println(data[currentIndex+1]);
						throw new SmartScriptLexerException();
					}
				}
				SmartScriptToken newToken = new SmartScriptToken(SmartScriptTokenType.FUNCTION,
						tokenBuilder.toString());
				this.token = newToken;
				return newToken;

			} else if (Character.toString(data[currentIndex]).equals("\"")) { 
				StringBuilder tokenBuilder = new StringBuilder();
				tokenBuilder.append(data[currentIndex]);
				currentIndex++;
				while (!Character.toString(data[currentIndex]).equals("\"")) {
					tokenBuilder.append(data[currentIndex]);
					currentIndex++;
				}
				if (Character.toString(data[currentIndex]).equals("\"")) {
					tokenBuilder.append(data[currentIndex]);
					currentIndex++;
				}
				SmartScriptToken newToken = new SmartScriptToken(SmartScriptTokenType.STRING, tokenBuilder.toString().replaceAll("\"", "").replaceAll("\\\\r\\\\n", "\r\n"));
				this.token = newToken;
				return newToken;

			} else if (Character.isDigit(data[currentIndex])) {
				StringBuilder tokenBuilder = new StringBuilder();
				while ((currentIndex < data.length) && !(Character.toString(data[currentIndex]).equals(" ")
						|| Character.toString(data[currentIndex]).equals("$") || Character.toString(data[currentIndex]).equals("\n"))) {
					tokenBuilder.append(data[currentIndex]);
					currentIndex++;
				}
				if (isInteger(tokenBuilder.toString())) {
					SmartScriptToken newToken = new SmartScriptToken(SmartScriptTokenType.INTEGER,
							Integer.parseInt(tokenBuilder.toString()));
					this.token = newToken;
					return newToken;
				} else if (isDouble(tokenBuilder.toString())) {
					SmartScriptToken newToken = new SmartScriptToken(SmartScriptTokenType.DOUBLE,
							Double.parseDouble(tokenBuilder.toString()));
					this.token = newToken;
					return newToken;
				} else {
					throw new SmartScriptLexerException("Wrong number format");
				}

			} else if (Character.isLetter(data[currentIndex])) {
				StringBuilder tokenBuilder = new StringBuilder();
				while (!(Character.toString(data[currentIndex]).equals(" ")
						|| Character.toString(data[currentIndex]).equals("$"))) {
					if (Character.isLetter(data[currentIndex]) || Character.isDigit(data[currentIndex])
							|| Character.toString(data[currentIndex]).equals("_")) {
						tokenBuilder.append(data[currentIndex]);
						currentIndex++;
					} else {
						throw new SmartScriptLexerException();
					}
				}
				SmartScriptToken newToken = new SmartScriptToken(SmartScriptTokenType.VARIABLE,
						tokenBuilder.toString());
				this.token = newToken;
				return newToken;
			} else if (Character.toString(data[currentIndex]).equals(" ")
					|| Character.toString(data[currentIndex]).equals("\r")
					|| Character.toString(data[currentIndex]).equals("\n")
					|| Character.toString(data[currentIndex]).equals("\t")) {
				currentIndex++;
				return this.nextToken();
			} else {
				throw new SmartScriptLexerException();
			}

		}

		else {
			StringBuilder textBuilder = new StringBuilder();
			while ((currentIndex < data.length)
					&& !((currentIndex < (data.length - 1)) && Character.toString(data[currentIndex]).equals("{")
							&& Character.toString(data[currentIndex + 1]).equals("$"))) {
				if ((currentIndex < data.length - 1) && Character.toString(data[currentIndex]).equals("\\")
						&& Character.toString(data[currentIndex + 1]).equals("{")) {
					textBuilder.append(data[currentIndex]);
					currentIndex++;
					textBuilder.append(data[currentIndex]);
					currentIndex++;
				}

				textBuilder.append(data[currentIndex]);
				currentIndex++;
			}

			SmartScriptToken newToken = new SmartScriptToken(SmartScriptTokenType.TEXT, textBuilder.toString());
			this.token = newToken;
			return newToken;

		}

	}

	/**
	 * Returns the last analyzed token.
	 * 
	 * @return Last analyzed token.
	 */
	public SmartScriptToken getToken() {
		return this.token;
	}

	/**
	 * Sets the state of the lexer.
	 * 
	 * @param state
	 *            State to be set.
	 * @throws IllegalArgumentException
	 *             if the given state is null.
	 */
	public void setState(SmartScriptLexerState state) {
		if (state == null)
			throw new IllegalArgumentException("State must not be null.");
		this.state = state;
	}

	/**
	 * Returns the current state of a lexer.
	 * 
	 * @return Current state of a lexer.
	 */
	public SmartScriptLexerState getState() {
		return state;
	}

	/**
	 * Method that checks if the given string is double.
	 * 
	 * @param str
	 *            Given string.
	 * @return True if a given string is parseable to double, False otherwise.
	 */
	public static boolean isDouble(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * Method that checks if the given string is integer.
	 * 
	 * @param str
	 *            Given string.
	 * @return True if a given string is parseable to integer, False otherwise.
	 */
	public static boolean isInteger(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

}
