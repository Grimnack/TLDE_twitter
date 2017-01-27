package core;

import twitter4j.QueryResult;
import twitter4j.Status;

public class Main {
//	public static void main(String[] args){
//		QueryResult qr = TwitterHelper.getTweets(true, 10, "Donald");
//		for (Status status : qr.getTweets()) {
//			System.out.println(status.getText());
//		}
//	}
	public static void main(String[] args){
		TwitterHelper.streaming(true, "Fillon");
	}
}
