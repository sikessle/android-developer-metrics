package de.htwg.tqm.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;

import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.net.httpserver.HttpServer;

public final class TqmHttpServer {

    public static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        System.out.println("Starting Embedded Jersey HTTPServer...");
        HttpServer server = createHttpServer();
        server.start();
        URI serverUri = getServerUri();
        System.out.printf("Jersey Application Server started with WADL available at %sapplication.wadl\n",
                serverUri);
        System.out.printf("Server available at %s\n", serverUri);
    }

    private static HttpServer createHttpServer() throws IOException {
        ResourceConfig resourceConfig = new PackagesResourceConfig("de.htwg.tqm.server.controller");
        return HttpServerFactory.create(getServerUri(), resourceConfig);
    }

    private static URI getServerUri() {
        return UriBuilder.fromUri("http://" + getHostName() + "/").port(PORT).build();
    }

    private static String getHostName() {
        String hostName = "localhost";
        try {
            hostName = InetAddress.getLocalHost().getCanonicalHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return hostName;
    }
}