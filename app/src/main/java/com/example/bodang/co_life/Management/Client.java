package com.example.bodang.co_life.Management;

/**
 * Created by Bodang on 27/10/2016.
 */

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client {
    private static Socket socket;
    private ServerSocket server;
    private static PrintWriter out;
    private static BufferedReader in;
    private static String name = null;
    ArrayList<String> files;
    ArrayList<String> accounts;
    ArrayList<String> paths;

    public static int Init() {
        try {
            if (socket == null) {
                socket = new Socket("121.42.196.2", 8080);
            }
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            return 1;
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        }
    }
    public String[] changeName(String name, String password) {
        String[] str = new String[2];
        out.println("./changeName");
        out.println(name);
        out.println(password);
        try {
            str[0] = in.readLine();
            str[1] = in.readLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }
}
