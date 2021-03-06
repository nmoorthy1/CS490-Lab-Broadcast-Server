package edu.purdue.cs490.server.api.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.purdue.cs490.server.SQLiteData;
import edu.purdue.cs490.server.Server;
import edu.purdue.cs490.server.data.HTTPRequest;
import edu.purdue.cs490.server.data.HTTPResponse;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Linux {
    private static final Logger log = Logger.getLogger(Status.class.getName());
    private static SQLiteData sqlData = Server.getInstance().getSQLData();

    public static HTTPResponse handleUpdateLinux(HTTPRequest request) {
        HTTPResponse response = new HTTPResponse();
        ObjectMapper mapper = new ObjectMapper();

        switch (request.getMethod()) {
            case PUT:
            case POST:
                try {
                    log.fine(request.getBody());
                    Map data = mapper.readValue(request.getBody(), Map.class);

                    String name = (String) data.get("name");
                    Boolean occupied = (Boolean) data.get("occupied");
                    int time = (int) data.get("time");
                    int uptime = (int) data.get("uptime");

                    String currentUser = (String) data.get("current_user");

                    //There is probably a better way to do this, but its fine for now.
                    String labroom = "";
                    if (name.contains("moore")) {
                        labroom = "LWSNB146";
                    } else if (name.contains("sslab")) {
                        labroom = "LWSNB158";
                    } else if (name.contains("pod")) {
                        labroom = "LWSNB148";
                    } else if (name.contains("borg")) {
                        labroom = "HAASG40";
                    } else if (name.contains("xinu")) {
                        labroom = "HAAS257";
                    }

                    sqlData.updateLinux(name, labroom, currentUser, time, uptime, occupied);

                    // 200 = Success, and since we are always successful we always success.
                    // In the future we can parse the body and validate it.
                    response.setStatus(200);
                } catch (IOException e) {
                    log.log(Level.WARNING, "Error while trying to map JSON from response", e);
                }
                return response;
            default:
                return HTTPResponse.getHTTPError(405);
        }
    }
}
