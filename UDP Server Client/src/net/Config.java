package net;

public class Config {
	public static final int PORT=8888;
	public static final int MAX_CLIENTS=16;
	public static final int DEFAULT_TIMEOUT=1000;
	public static final boolean REQUIRE_UNIQUE_CLIENTS=false;
	public class PingSettings{
		public static final int CLIENT_MAX_DEAD_TIME=10000;
		public static final int NUMBER_OF_PINGS=4;
		public static final int TIME_BETWEEN_PINGS=500;
		public static final int TOTAL_WAIT=CLIENT_MAX_DEAD_TIME+(NUMBER_OF_PINGS*TIME_BETWEEN_PINGS);
	}
}
