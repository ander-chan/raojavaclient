/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.medicamecanica.raojavaclient;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.Transport;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ander
 */
public class IoJavaClient {

    private static io.socket.client.Socket socket;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws URISyntaxException {
        IO.Options opts = new IO.Options();
        // opts.forceNew = true;
        //opts.query = "sid=test";
        socket = IO.socket("http://localhost:8080/chat", opts);

        socket.io().on(Manager.EVENT_TRANSPORT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Transport transport = (Transport) args[0];

                transport.on(Transport.EVENT_REQUEST_HEADERS, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        @SuppressWarnings("unchecked")
                        Map<String, List<String>> headers = (Map<String, List<String>>) args[0];
                        // modify request headers
                        headers.put("Cookie", Arrays.asList("rao_session=MN0FxcRLR3410HZI;"));
                    }
                });

                transport.on(Transport.EVENT_RESPONSE_HEADERS, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        @SuppressWarnings("unchecked")
                        Map<String, List<String>> headers = (Map<String, List<String>>) args[0];
                        // access response headers
                        //  String cookie = headers.get("Set-Cookie").get(0);
                    }
                });
            }
        });
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {

                //  socket.disconnect();
            }

        }).on("message", new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                System.out.println(args);
            }

        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
            }

        });
        socket.connect();
        // Receiving an object
        socket.on("message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject obj = (JSONObject) args[0];
                System.out.println(obj);
            }
        });

        socket.on("online", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println(args.length);
                JSONArray obj = (JSONArray) args[0];
                System.out.println(obj);
                JSONObject o = new JSONObject();
                try {
                    o.put("message", ">.<");
                    o.put("user", "Excel");
                    o.put("chatName", "Neliel");
                    o.put("chatColor", "96095e");
                    o.put("rank", 1);
                    o.put("image", "http://localhost/raochat/avatar/s/Excel57d5d58a63294.png");
                    o.put("chatText", "c21892");
                     o.put("time", new Date().toString());
                   
                    socket.emit("message", o);
                } catch (JSONException ex) {
                    Logger.getLogger(IoJavaClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                // o.put("binary", new byte[42]);
                // o.put("binary", new byte[42]);

            }
        });
    }

}
