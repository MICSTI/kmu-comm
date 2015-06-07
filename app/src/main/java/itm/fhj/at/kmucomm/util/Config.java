package itm.fhj.at.kmucomm.util;

/**
 * Created by michael.stifter on 07.06.2015.
 */
public class Config {
    /**
     * API addresses
     */
    public static final String API_ROOT = "http://10.76.1.147:8080/OpenfireMgmt";
    public static final String API_REST = API_ROOT + "/rest";
    public static final String API_REST_USERS = API_REST + "/users";

    /**
     * Number of tries that the app tries to connect to the API
     */
    public static final int NETWORK_TRIES = 5;
}
