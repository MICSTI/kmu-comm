package itm.fhj.at.kmucomm.util;

import java.text.SimpleDateFormat;

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
    public static final int NETWORK_TRIES = 3;

    public static final SimpleDateFormat FORMAT_DATE_TIME = new SimpleDateFormat("dd.mm.YYYY HH:mm");

    /**
     * XMPP configuration
     */
    public static final String OF_BOB_USERNAME = "bob";
    public static final String OF_BOB_PASSWORD = "bob";
    public static final String OF_BOB_EMAIL = "bob@projekt.at";

    public static final String OF_ADMIN_USERNAME = "admin";
    public static final String OF_ADMIN_PASSWORD = "openfire_distcomp_2015";
    public static final String OF_ADMIN_EMAIL = "mayflc07@gmail.com";

    public static final String OPENFIRE_HASH = "9f9d51bc70ef21ca5c14f307980a29d8";
    public static final String OPENFIRE_SERVER = "10.76.1.147";
    public static final int OPENFIRE_PORT = 5222;

    public static final String OPENFIRE_RESOURCE = "@projmgmt";

    public static final char CHAR_NEW_LINE = '\n';
    public static final char CHAR_AT = '@';
}
