package weibo4j;

import weibo4j.http.HttpClient;

public class Weibo implements java.io.Serializable {
        public static String CONSUMER_KEY = "3022154838";
	public static String CONSUMER_SECRET = "6da37cb19da52f894fdbdf8d9b261bd5";
	private static final long serialVersionUID = 4282616848978535016L;

	public HttpClient client = new HttpClient();

	public  void setToken(String token) {
		client.setToken(token);
	}

}