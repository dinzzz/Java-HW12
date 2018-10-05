package hr.fer.zemris.java.webserver;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * Class that represents a smart http server. This is a simple server
 * implementation that supports running scripts and presentation of some
 * graphical and textual elements to the client.
 * 
 * @author Dinz
 *
 */
public class SmartHttpServer {
	@SuppressWarnings("unused")
	/**
	 * Address of the server.
	 */
	private String address;

	/**
	 * Domain name of the server.
	 */
	private String domainName;

	/**
	 * Port where the server is run.
	 */
	private int port;

	/**
	 * Number of the worker threads on the server.
	 */
	private int workerThreads;

	/**
	 * Duration of the session on the server.
	 */
	private int sessionTimeout;

	/**
	 * Map of mime types supported on this server.
	 */
	private Map<String, String> mimeTypes = new HashMap<String, String>();

	/**
	 * Server thread instance.
	 */
	private ServerThread serverThread;

	/**
	 * Thread pool.
	 */
	private ExecutorService threadPool;

	/**
	 * Root directory of the files used on the server like scripts, text documents
	 * and pictures.
	 */
	private Path documentRoot;

	/**
	 * Location of the mime configuration file.
	 */
	private String mimeConfig;

	/**
	 * Location of the workers configuration file.
	 */
	private String workersConfig;

	/**
	 * Map of the workers supported by this server.
	 */
	private Map<String, IWebWorker> workersMap = new HashMap<>();

	/**
	 * Map of currently active sessions.
	 */
	private Map<String, SessionMapEntry> sessions = new HashMap<String, SmartHttpServer.SessionMapEntry>();

	/**
	 * Randomizer for generating session ID.
	 */
	private Random sessionRandom = new Random();

	/**
	 * Thread which cleans the session after a five minute time-out.
	 */
	private CleanerThread cleanerThread = new CleanerThread();

	/**
	 * Constructs a new Smart Http Server
	 * 
	 * @param configFileName
	 *            Configuration file for the server.
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public SmartHttpServer(String configFileName) throws FileNotFoundException, IOException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		// … do stuff here …
		Properties properties = new Properties();
		properties.load(new FileInputStream(configFileName));
		address = properties.getProperty("server.address");
		domainName = properties.getProperty("server.domainName");
		port = Integer.parseInt(properties.getProperty("server.port"));
		workerThreads = Integer.parseInt(properties.getProperty("server.workerThreads"));
		sessionTimeout = Integer.parseInt(properties.getProperty("session.timeout"));
		documentRoot = Paths.get(properties.getProperty("server.documentRoot"));
		mimeConfig = properties.getProperty("server.mimeConfig");
		workersConfig = properties.getProperty("server.workers");
		loadMimeTypes();
		loadWorkers();

		serverThread = new ServerThread();
	}

	/**
	 * Method that loads the workers to the workers map.
	 * 
	 * @throws IOException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	private void loadWorkers()
			throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		List<String> workers = Files.readAllLines(Paths.get(workersConfig));
		for (String worker : workers) {
			if (!worker.startsWith("#") && !worker.isEmpty()) {
				String path = worker.split("=")[0].trim();
				String fqcn = worker.split("=")[1].trim();

				if (workersMap.containsKey(path)) {
					throw new IllegalArgumentException("There is a duplicate path in the workers map.");
				}

				Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);
				@SuppressWarnings("deprecation")
				Object newObject = referenceToClass.newInstance();
				IWebWorker iww = (IWebWorker) newObject;
				workersMap.put(path, iww);
			}
		}
	}

	/**
	 * Method that loads the mime types into a mime type map.
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void loadMimeTypes() throws FileNotFoundException, IOException {
		List<String> mimes = Files.readAllLines(Paths.get(mimeConfig));
		for (String mime : mimes) {
			if (!mime.startsWith("#") && !mime.isEmpty()) {
				String key = mime.split("=")[0].trim();
				String value = mime.split("=")[1].trim();

				mimeTypes.put(key, value);
			}
		}

	}

	/**
	 * Starts the server.
	 */
	protected synchronized void start() {
		// … start server thread if not already running …
		// … init threadpool by Executors.newFixedThreadPool(...); …
		if (!serverThread.isAlive()) {
			serverThread.start();
			threadPool = Executors.newFixedThreadPool(workerThreads);
			cleanerThread.start();
		}
	}

	/**
	 * Stops the server.
	 */
	protected synchronized void stop() {
		// … signal server thread to stop running …
		// … shutdown threadpool …
		if (serverThread.isAlive()) {
			serverThread.interrupt();
			threadPool.shutdown();
			cleanerThread.interrupt();
		}
	}

	/**
	 * Main method that runs the server with the given configuration file.
	 * 
	 * @param args
	 *            Configuration file path wanted from the command line.
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		if (args.length != 1) {
			System.out.println("Server config missing as a program argument");
			return;
		}
		String config = args[0];
		SmartHttpServer server = new SmartHttpServer(config);
		server.start();
	}

	/**
	 * Class that represents a server thread which is run when the server starts.
	 * 
	 * @author Dinz
	 *
	 */
	protected class ServerThread extends Thread {
		@Override
		public void run() {
			// given in pseudo-code:
			// open serverSocket on specified port
			// while(true) {
			// Socket client = serverSocket.accept();
			// ClientWorker cw = new ClientWorker(client);
			// submit cw to threadpool for execution
			// }
			try {
				@SuppressWarnings("resource")
				ServerSocket serverSocket = new ServerSocket();
				serverSocket.bind(new InetSocketAddress((InetAddress) null, port));
				while (true) {
					Socket client = serverSocket.accept();
					ClientWorker cw = new ClientWorker(client);
					threadPool.submit(cw);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Class that represents a client connection to the server in form of a runnable
	 * thread. Every client worker supports only one action for the client. However,
	 * some parameters are affected by session duration.
	 * 
	 * @author Dinz
	 *
	 */
	private class ClientWorker implements Runnable, IDispatcher {

		/**
		 * Client socket.
		 */
		private Socket csocket;

		/**
		 * Input stream.
		 */
		private PushbackInputStream istream;

		/**
		 * Output stream.
		 */
		private OutputStream ostream;

		/**
		 * Version of the request.
		 */
		private String version;

		/**
		 * Method of the request.
		 */
		private String method;

		/**
		 * Host.
		 */
		private String host;

		/**
		 * Map of parameters.
		 */
		private Map<String, String> params = new HashMap<String, String>();

		/**
		 * Map of temporary parameters.
		 */
		private Map<String, String> tempParams = new HashMap<String, String>();

		/**
		 * Map of persistent parameters.
		 */
		private Map<String, String> permParams = new HashMap<String, String>();

		/**
		 * List of output cookies.
		 */
		private List<RCCookie> outputCookies = new ArrayList<RequestContext.RCCookie>();

		/**
		 * Session ID.
		 */
		private String SID;

		/**
		 * Instance of request context.
		 */
		private RequestContext context = null;

		/**
		 * Constructs a new client worker.
		 * 
		 * @param csocket
		 *            Client socket.
		 */
		public ClientWorker(Socket csocket) {
			super();
			this.csocket = csocket;
		}

		@Override
		public void run() {

			try {
				istream = new PushbackInputStream(csocket.getInputStream());
				ostream = csocket.getOutputStream();

				byte[] requestBytes = readRequest(istream);
				if (requestBytes == null) {
					sendError(ostream, 400, "Bad request");
					return;
				}
				String requestStr = new String(requestBytes, StandardCharsets.US_ASCII);

				List<String> request = extractHeaders(requestStr);

				String firstLine = request.get(0);
				String[] extractions = firstLine.split("\\s+");
				method = extractions[0].trim();
				String reqPath = extractions[1].trim();
				version = extractions[2].trim();

				if (!method.equals("GET") || (!version.equals("HTTP/1.0") && !version.equals("HTTP/1.1"))) {
					sendError(ostream, 400, "Bad request");
					return;
				}

				boolean hostFound = false;
				for (String header : request) {
					if (header.trim().startsWith("Host:")) {
						host = header.split(":")[1].trim();
						hostFound = true;
					}
				}
				if (!hostFound) {
					host = domainName;
				}
				checkSession(request);

				String path;
				if (reqPath.contains("?")) {
					path = reqPath.split("\\?")[0];
					String paramString = reqPath.split("\\?")[1];
					parseParameters(paramString);

				} else {
					path = reqPath;
				}

				try {
					internalDispatchRequest(path, true);
				} catch (Exception e) {
					e.printStackTrace();
				}

				csocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Method that checks the session ID to determine if the session is currently
		 * active or timed-out.
		 * 
		 * @param request
		 *            Request from the client.
		 */
		private synchronized void checkSession(List<String> request) {
			String sidCandidate = null;
			for (String line : request) {
				if (line.startsWith("Cookie:")) {
					line = line.substring("Cookie:".length()).trim();
					String[] cookies = line.split(";");
					for (String cookie : cookies) {
						if (cookie.trim().split("=")[0].equals("sid")) {
							sidCandidate = cookie.trim().split("=")[1].replaceAll("\"", "");
						}
					}
				}
			}
			if (sidCandidate != null) {
				SessionMapEntry sessionCandidate = sessions.get(sidCandidate);
				if (sessionCandidate == null) {
					newSession();
				} else if (!sessionCandidate.host.equals(host)) {
					newSession();
				} else if (new Date().getTime() > sessionCandidate.validUntil) {
					sessions.remove(sidCandidate);
					newSession();
				} else {
					SID = sidCandidate;
					sessionCandidate.validUntil = new Date().getTime() + sessionTimeout * 1000;
				}
			} else {
				newSession();
			}

			permParams = sessions.get(SID).map;

		}

		/**
		 * Method that creates a new session when needed.
		 */
		private synchronized void newSession() {
			SID = generateSID();
			SessionMapEntry smEntry = new SessionMapEntry();
			smEntry.sid = SID;
			smEntry.validUntil = new Date().getTime() + sessionTimeout * 1000;
			smEntry.map = new ConcurrentHashMap<>();
			smEntry.host = host;
			sessions.put(SID, smEntry);
			outputCookies.add(new RCCookie("sid", SID, null, host, "/"));

		}

		/**
		 * Method that generates a new random session ID.
		 * 
		 * @return Session ID.
		 */
		private synchronized String generateSID() {
			String alpha = "ABCDEFGHIJKLMNOPRSTVZ";
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < 20; i++) {
				int rand = Math.abs(sessionRandom.nextInt() % (alpha.length()));
				sb.append(alpha.charAt(rand));
			}
			return sb.toString();
		}

		/**
		 * Method which reads the document from the disk.
		 * 
		 * @param string
		 *            Location of the document.
		 * @return Document read and transformed to text.
		 * @throws IOException
		 */
		private String readFromDisk(String string) throws IOException {
			Path path = Paths.get(string);
			byte[] file = Files.readAllBytes(path);
			String docBody = new String(file, StandardCharsets.UTF_8);
			return docBody;
		}

		/**
		 * Method that extracts the extension from the file.
		 * 
		 * @param requestedPath
		 *            Location of the file.
		 * @return Extension of the file.
		 */
		private String extension(Path requestedPath) {
			String[] split = requestedPath.toString().split("\\.");
			return split[split.length - 1];
		}

		/**
		 * Method that parses the parameters from the request and stores them to the
		 * map.
		 * 
		 * @param paramString
		 *            Extracted line of parameters from the request.
		 */
		private void parseParameters(String paramString) {
			String[] parameters = paramString.split("&");
			for (String parameter : parameters) {
				String name = parameter.split("=")[0];
				String value = parameter.split("=").length == 2 ? parameter.split("=")[1] : null;
				params.put(name, value);
			}

		}

		/**
		 * Method that sends an error page to the client.
		 * 
		 * @param ostream
		 *            Output stream.
		 * @param i
		 *            Status code of the error.
		 * @param string
		 *            Status text of the error.
		 * @throws IOException
		 */
		private void sendError(OutputStream ostream, int i, String string) throws IOException {
			ostream.write(("HTTP/1.1 " + i + " " + string + "\r\n" + "Server: Simple java server\r\n"
					+ "Content-Type: text/plain;charset=UTF-8\r\n" + "Content-Length: 0\r\n" + "Connection: close\r\n"
					+ "\r\n").getBytes(StandardCharsets.US_ASCII));
			ostream.flush();

		}

		/**
		 * Method that reads a request from the input stream.
		 * 
		 * @param is
		 *            Input stream.
		 * @return Request in byte format.
		 * @throws IOException
		 */
		private byte[] readRequest(InputStream is) throws IOException {

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int state = 0;
			l: while (true) {
				int b = is.read();
				if (b == -1)
					return null;
				if (b != 13) {
					bos.write(b);
				}
				switch (state) {
				case 0:
					if (b == 13) {
						state = 1;
					} else if (b == 10)
						state = 4;
					break;
				case 1:
					if (b == 10) {
						state = 2;
					} else
						state = 0;
					break;
				case 2:
					if (b == 13) {
						state = 3;
					} else
						state = 0;
					break;
				case 3:
					if (b == 10) {
						break l;
					} else
						state = 0;
					break;
				case 4:
					if (b == 10) {
						break l;
					} else
						state = 0;
					break;
				}
			}
			return bos.toByteArray();
		}

		/**
		 * Method which extracts headers from the request.
		 * 
		 * @param requestHeader
		 *            Client's request.
		 * @return List of arranged headers.
		 */
		private List<String> extractHeaders(String requestHeader) {
			List<String> headers = new ArrayList<String>();
			String currentLine = null;
			for (String s : requestHeader.split("\n")) {
				if (s.isEmpty())
					break;
				char c = s.charAt(0);
				if (c == 9 || c == 32) {
					currentLine += s;
				} else {
					if (currentLine != null) {
						headers.add(currentLine);
					}
					currentLine = s;
				}
			}
			if (!currentLine.isEmpty()) {
				headers.add(currentLine);
			}
			return headers;
		}

		@Override
		public void dispatchRequest(String urlPath) throws Exception {
			internalDispatchRequest(urlPath, false);

		}

		/**
		 * Method that executes an internal dispatch request - it redirects the request
		 * to the appropriate action or a worker.
		 * 
		 * @param urlPath
		 *            URL path of the request.
		 * @param directCall
		 *            Determines whether the call was direct or indirect.
		 * @throws Exception
		 */
		public void internalDispatchRequest(String urlPath, boolean directCall) throws Exception {
			if (urlPath.startsWith("/")) {
				urlPath = urlPath.replaceFirst("/", "");
			}
			Path requestedPath = documentRoot.resolve(urlPath).normalize();
			if (!requestedPath.isAbsolute()) {
				sendError(ostream, 403, "Forbidden");
				return;
			}
			if (urlPath.startsWith("private") && directCall == true) {
				sendError(ostream, 404, "Private");
				return;
			}
			if (urlPath.startsWith("ext/")) {
				if (context == null) {
					context = new RequestContext(ostream, params, permParams, outputCookies, tempParams, this);
				}
				String workerClass = urlPath.replaceFirst("ext/", "");
				Class<?> referenceToClass = this.getClass().getClassLoader()
						.loadClass("hr.fer.zemris.java.webserver.workers." + workerClass);
				@SuppressWarnings("deprecation")
				Object newObject = referenceToClass.newInstance();
				IWebWorker iww = (IWebWorker) newObject;
				iww.processRequest(context);
				return;
			}

			String checker = "/" + urlPath;
			if (workersMap.containsKey(checker)) {
				if (context == null) {
					context = new RequestContext(ostream, params, permParams, outputCookies, tempParams, this);
				}
				workersMap.get(checker).processRequest(context);
				return;
			}

			if (!requestedPath.toFile().exists() || !requestedPath.toFile().canRead()) {
				sendError(ostream, 404, "File not found.");
				return;
			}

			String extension = extension(requestedPath);
			String mimeType = mimeTypes.get(extension);
			if (mimeType == null) {
				mimeType = "application/octet-stream";
			}

			if (extension.equals("smscr")) {
				if (context == null) {
					context = new RequestContext(ostream, params, permParams, outputCookies, tempParams, this);
				}
				String docBody = readFromDisk(requestedPath.toString());
				new SmartScriptEngine(new SmartScriptParser(docBody).getDocumentNode(), context).execute();

			} else {
				if (context == null) {
					context = new RequestContext(ostream, params, permParams, outputCookies);
				}
				context.setMimeType(mimeType);
				context.setStatusCode(200);
				context.write(Files.readAllBytes(requestedPath));
			}
		}

	}
	
	/**
	 * Class that represents a session entry constructed when the client sends a request.
	 * @author Dinz
	 *
	 */
	private static class SessionMapEntry {
		/**
		 * Session ID.
		 */
		@SuppressWarnings("unused")
		String sid;
		
		/**
		 * Host.
		 */
		String host;
		
		/**
		 * Time until this session is valid.
		 */
		long validUntil;
		
		/**
		 * Map of parameters.
		 */
		Map<String, String> map;
	}
	
	/**
	 * Class that represents a thread which cleans timed-out session every 5 minutes.
	 * @author Dinz
	 *
	 */
	private class CleanerThread extends Thread {
		/**
		 * Constructs a new cleaner thread.
		 */
		public CleanerThread() {
			this.setDaemon(true);
		}
		
		@Override
		public void run() {
			while (!this.isInterrupted()) {
				for (String session : sessions.keySet()) {
					if (sessions.get(session).validUntil < new Date().getTime()) {
						sessions.remove(session);
					}
				}

				try {
					sleep(600 * 500);
				} catch (InterruptedException e) {
					this.interrupt();
				}

			}
		}
	}

}
