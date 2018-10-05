package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Class that represents a request context. It takes a client request and based
 * on the data received, forms an appropriate header and an output data for the
 * client.
 * 
 * @author Dinz
 *
 */
public class RequestContext {

	/**
	 * Inner class that represents a context cookie which stores some of the
	 * information about the context.
	 * 
	 * @author Dinz
	 *
	 */
	public static class RCCookie {

		/**
		 * Cookie name.
		 */
		String name;

		/**
		 * Cookie value.
		 */
		String value;

		/**
		 * Domain.
		 */
		String domain;

		/**
		 * Path
		 */
		String path;

		/**
		 * Cookie's maximum age.
		 */
		Integer maxAge;

		/**
		 * Contructs a new cookie.
		 * 
		 * @param name
		 *            Name of the cookie.
		 * @param value
		 *            Value of the cookie.
		 * @param maxAge
		 *            Max age of the cookie.
		 * @param domain
		 *            Domain.
		 * @param path
		 *            Path.
		 */
		public RCCookie(String name, String value, Integer maxAge, String domain, String path) {
			super();
			this.name = name;
			this.value = value;
			this.domain = domain;
			this.path = path;
			this.maxAge = maxAge;
		}

		/**
		 * Gets the cookie name-
		 * 
		 * @return Name of the cookie.
		 */
		public String getName() {
			return name;
		}

		/**
		 * Gets the cookie value.
		 * 
		 * @return Value of the cookie.
		 */
		public String getValue() {
			return value;
		}

		/**
		 * Gets the domain.
		 * 
		 * @return Domain.
		 */
		public String getDomain() {
			return domain;
		}

		/**
		 * Gets the path.
		 * 
		 * @return Path.
		 */
		public String getPath() {
			return path;
		}

		/**
		 * Gets max age of the cookie.
		 * 
		 * @return Max age.
		 */
		public Integer getMaxAge() {
			return maxAge;
		}

	}

	/**
	 * Output stream where the context should be presented.
	 */
	private OutputStream outputStream;

	/**
	 * Charset of the context.
	 */
	private Charset charset;

	/**
	 * Encoding of the context, default set to UTF-8.
	 */
	private String encoding = "UTF-8";

	/**
	 * Status code of the context, default set to 200.
	 */
	private int statusCode = 200;

	/**
	 * Status text of the context.
	 */
	private String statusText = "OK";

	/**
	 * Mime type of the context.
	 */
	private String mimeType = "text/html";

	/**
	 * Parameters passed to the context.
	 */
	private Map<String, String> parameters;

	/**
	 * Temporary parameters.
	 */
	private Map<String, String> temporaryParameters = new HashMap<>();

	/**
	 * Persistent parameters.
	 */
	private Map<String, String> persistentParameters;

	/**
	 * Output cookies.
	 */
	private List<RCCookie> outputCookies;

	/**
	 * Flag which notes if the header has been generated.
	 */
	private boolean headerGenerated = false;

	/**
	 * Dispatcher.
	 */
	private IDispatcher dispatcher;

	/**
	 * Sets the output stream.
	 * 
	 * @param outputStream
	 *            Output stream where the context should be presented.
	 */
	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	/**
	 * Sets the encoding for the context.
	 * 
	 * @param encoding
	 *            Encoding to be set.
	 */
	public void setEncoding(String encoding) {
		if (headerGenerated) {
			throw new RuntimeException("Can't change properties after creating header.");
		}

		this.encoding = encoding;
	}

	/**
	 * Sets the status code for the context.
	 * 
	 * @param statusCode
	 *            Status code to be set.
	 */
	public void setStatusCode(int statusCode) {
		if (headerGenerated) {
			throw new RuntimeException("Can't change properties after creating header.");
		}
		this.statusCode = statusCode;
	}

	/**
	 * Sets the status text for the context.
	 * 
	 * @param statusText
	 *            Status text to be set.
	 */
	public void setStatusText(String statusText) {
		if (headerGenerated) {
			throw new RuntimeException("Can't change properties after creating header.");
		}
		this.statusText = statusText;
	}

	/**
	 * Sets the mime type for the context.
	 * 
	 * @param mimeType
	 *            Mime type to be set.
	 */
	public void setMimeType(String mimeType) {
		if (headerGenerated) {
			throw new RuntimeException("Can't change properties after creating header.");
		}
		this.mimeType = mimeType;
	}

	/**
	 * Constructs a new Request context.
	 * 
	 * @param outputStream
	 *            Output stream of the context.
	 * @param parameters
	 *            Map of parameters to be passed to the context.
	 * @param persistentParameters
	 *            Persistent parameters of the context.
	 * @param outputCookies
	 *            Output cookies.
	 */
	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
			Map<String, String> persistentParameters, List<RCCookie> outputCookies) {
		this(outputStream, parameters, persistentParameters, outputCookies, new HashMap<>(), null);
	}

	/**
	 * Constructs a new Request context.
	 * 
	 * @param outputStream
	 *            Output stream of the context.
	 * @param parameters
	 *            Map of parameters to be passed to the context.
	 * @param persistentParameters
	 *            Persistent parameters of the context.
	 * @param outputCookies
	 *            Output cookies.
	 * @param temporaryParameters
	 *            Temporary parameters of the context.
	 * @param dispatcher
	 *            Dispatcher.
	 */
	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
			Map<String, String> persistentParameters, List<RCCookie> outputCookies,
			Map<String, String> temporaryParameters, IDispatcher dispatcher) {

		Objects.requireNonNull(outputStream);

		this.outputStream = outputStream;

		if (parameters == null) {
			this.parameters = new HashMap<>();
		} else {
			this.parameters = parameters;
		}

		if (persistentParameters == null) {
			this.persistentParameters = new HashMap<>();
		} else {
			this.persistentParameters = persistentParameters;
		}

		if (outputCookies == null) {
			this.outputCookies = new ArrayList<>();
		} else {
			this.outputCookies = outputCookies;
		}

		this.temporaryParameters = temporaryParameters;
		this.dispatcher = dispatcher;
	}

	/**
	 * Gets the parameter from the map.
	 * 
	 * @param name
	 *            Name of the parameter.
	 * @return Value of the wanted parameter.
	 */
	public String getParameter(String name) {
		return parameters.get(name);
	}

	/**
	 * Gets all the parameter names from the parameters map.
	 * 
	 * @return Parameter names.
	 */
	public Set<String> getParameterNames() {
		return parameters.keySet();
	}

	/**
	 * Gets the dispatcher from the context.
	 * 
	 * @return Dispatcher from the context.
	 */
	public IDispatcher getDispatcher() {
		return dispatcher;
	}

	/**
	 * Gets the persistent parameter from the context persistent parameter map.
	 * 
	 * @param name
	 *            Name of the parameter.
	 * @return Value of the parameter.
	 */
	public String getPersistentParameter(String name) {
		return persistentParameters.get(name);
	}

	/**
	 * Gets all the persistent parameter names from the context.
	 * 
	 * @return All persistent parameter names.
	 */
	public Set<String> getPersistentParameterNames() {
		return persistentParameters.keySet();
	}

	/**
	 * Sets the persistent parameter in the context.
	 * 
	 * @param name
	 *            Parameter to be set.
	 * @param value
	 *            New value of the parameter.
	 */
	public void setPersistentParameter(String name, String value) {
		persistentParameters.put(name, value);
	}

	/**
	 * Removes the persistent parameter from the context.
	 * 
	 * @param name
	 *            Parameter to be removed.
	 */
	public void removePersistentParameter(String name) {
		persistentParameters.remove(name);
	}

	/**
	 * Sets the temporary parameter in the context.
	 * 
	 * @param name
	 *            Name of the parameter.
	 * @param value
	 *            Value of the parameter.
	 */
	public void setTemporaryParameter(String name, String value) {
		temporaryParameters.put(name, value);
	}

	/**
	 * Removes the temporary parameter from the context.
	 * 
	 * @param name
	 *            Parameter to be removed.
	 */
	public void removeTemporaryParameter(String name) {
		temporaryParameters.remove(name);
	}

	/**
	 * Gets the temporary parameter from the context.
	 * 
	 * @param name
	 *            Name of the parameter.
	 * @return Value of the parameter.
	 */
	public String getTemporaryParameter(String name) {
		return temporaryParameters.get(name);
	}

	/**
	 * Adds the cookie to the context. If the header has already been generated, no
	 * cookies are allowed to be added.
	 * 
	 * @param cookie
	 *            Cookie to be added.
	 */
	public void addRCCookie(RCCookie cookie) {
		if (headerGenerated) {
			throw new RuntimeException("Can't change properties after creating header.");
		}
		outputCookies.add(cookie);
	}

	/**
	 * Method which writes the data to the output stream.
	 * 
	 * @param data
	 *            Data to be written.
	 * @return Context.
	 * @throws IOException
	 */
	public RequestContext write(byte[] data) throws IOException {
		if (!headerGenerated) {
			generateHeader();
			headerGenerated = true;
		}

		outputStream.write(data, 0, data.length);
		outputStream.flush();
		return this;
	}

	/**
	 * Method which writes the text to the output stream.
	 * 
	 * @param text
	 *            Text to be written.
	 * @return Context.
	 * @throws IOException
	 */
	public RequestContext write(String text) throws IOException {
		if (!headerGenerated) {
			generateHeader();
			headerGenerated = true;
		}

		byte[] data = text.getBytes(charset);
		return this.write(data);
	}

	/**
	 * Method which generates the header.
	 * 
	 * @throws IOException
	 */
	private void generateHeader() throws IOException {
		charset = Charset.forName(encoding);

		String header = "HTTP/1.1 " + statusCode + " " + statusText + "\r\n" + "Content-Type: "
				+ (mimeType.startsWith("text/") ? mimeType.concat("; charset=" + encoding) : mimeType) + "\r\n"
				+ printCookies() + "\r\n";

		outputStream.write(header.getBytes(charset), 0, header.length());

	}

	/**
	 * Method which prints the cookies while generating header.
	 * 
	 * @return String format of the cookies.
	 */
	private String printCookies() {
		StringBuilder sb = new StringBuilder();
		for (RCCookie cookie : outputCookies) {
			sb.append("Set-Cookie: " + cookie.getName() + "=\"" + cookie.getValue() + "\"");
			if (cookie.getDomain() != null) {
				sb.append("; Domain=" + cookie.getDomain());
			}
			if (cookie.getPath() != null) {
				sb.append("; Path=" + cookie.getPath());
			}
			if (cookie.getMaxAge() != null) {
				sb.append("; Max-Age=" + cookie.getMaxAge());
			}
			sb.append("\r\n");
		}

		return sb.toString();
	}

}
