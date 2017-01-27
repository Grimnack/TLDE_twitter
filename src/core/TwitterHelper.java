package core;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterHelper {

	private static ConfigurationBuilder etablishConnection(boolean proxy){
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey("JFMIooIHV0lH98BaqSqbjUqe4")
		.setOAuthConsumerSecret("R5yShSu0pls9oMg7CkxawOSQep4uzj7eG7shnlZASpPcHCbmfR")
		.setOAuthAccessToken("2918261818-rl3j6BkGZ3HKQeocHZal402jZ6qTucVLPw5Tm4b")
		.setOAuthAccessTokenSecret("V3hPJQHaTUkDKkuF41IwY0oLdILqaJdLGPDqPifUhYq44");


		if(proxy){
			cb.setHttpProxyHost("cache-etu.univ-lille1.fr");
			cb.setHttpProxyPort(3128);
		}

		return cb;
	}
	
	public static QueryResult getTweets(boolean proxy, int nb,String theme){
		ConfigurationBuilder cb = etablishConnection(proxy) ;
		TwitterFactory tf = new TwitterFactory(cb.build()) ;
		Twitter twitter = tf.getInstance() ;
		RateLimitStatus rateLimitStatus = null ;
		int remaining = -1, limit = -1;
		
		try {
			rateLimitStatus = twitter.getRateLimitStatus().get("/search/tweets");
			remaining = rateLimitStatus.getRemaining();
			limit = rateLimitStatus.getLimit();
		} catch (Exception exception){
			exception.printStackTrace();
		}
		Query query = new Query(theme);
		query.setLang("fr");
		query.count(nb);
		QueryResult result = null;

		try {
			result = twitter.search(query);
		} catch (TwitterException e1) {
			String message = "Impossible de récupérer de nouveaux tweets. Veuillez réessayer" ;
			System.out.println(message);
			
		}
		return result ;
	}
	
	public static void streaming(boolean proxy,String theme){
		ConfigurationBuilder cb = etablishConnection(proxy);
		TwitterStream ts = new TwitterStreamFactory(cb.build()).getInstance() ;
		StatusListener listener = new StatusListener() {
			
			@Override
            public void onStatus(Status status) {
                System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                System.out.println("Got stall warning:" + warning);
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
		};
        ts.addListener(listener);
        ts.sample("fr");
	}
}
