package com.example.bodang.co_life.Management;

/**
 * Created by Bodang on 27/10/2016.
 */

import com.example.bodang.co_life.Objects.Carrier;
import com.example.bodang.co_life.Objects.ContentType;
import com.example.bodang.co_life.Objects.Room;
import com.example.bodang.co_life.Objects.User;
import com.example.bodang.co_life.Objects.UserLocation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client {
    private Socket socket;
    static ObjectInputStream in;
    static ObjectOutputStream out;
    Carrier carrierSent;

    public int Init() {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress("121.42.196.2", 80), 5000);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            return 1;

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        }
    }

    public Boolean[] Login(String name, String password) {
        String[] check;
        Boolean[] checkresult = {false, false};
        User user = new User();
        user.setUserId(name);
        user.setUserPassword(password);
        carrierSent = new Carrier(user, ContentType.login, name);
        send(carrierSent, out);
        System.out.println(carrierSent.getSender() + "!!!!!!!!!!!!!!!");
        try {
            Carrier carrierReceived = (Carrier) in.readObject();
            check = (String[]) carrierReceived.getObject();
            if (check[0].equals("true")) {
                checkresult[0] = true;
            }
            if (check[1].equals("true")) {
                checkresult[1] = true;
            }
            close();
        } catch (Exception e) {
            close();
            e.printStackTrace();
        }
        return checkresult;
    }

    public boolean UpdateLocation(String username, double longitude, double latitude) {
        boolean result = false;
        UserLocation location = new UserLocation(longitude, latitude);
        carrierSent = new Carrier(location, ContentType.updatelocation, username);
        send(carrierSent, out);
        try {
            Carrier carrierReceived = (Carrier) in.readObject();
            result = (boolean) carrierReceived.getObject();
            close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean JoinRoom(String username, String roomid, String roompassword) {
        boolean roomCheckResult = false;
        int roomID = Integer.parseInt(roomid);///////need to judgy whether null/size=0/not int?
        Room roomToSend = new Room(roomID, roompassword);
        carrierSent = new Carrier(roomToSend, ContentType.joinroom, username);
        send(carrierSent, out);
        try {
            Carrier carrierReceived = (Carrier) in.readObject();
            roomCheckResult = (boolean) carrierReceived.getObject();
            close();
        } catch (Exception e) {
            close();
            e.printStackTrace();
        }
        return roomCheckResult;
    }

    public int createroom(String username, String password) {
        int roomId = 0;
        Room roomToSend = new Room(roomId, password);
        carrierSent = new Carrier(roomToSend, ContentType.createroom, username);
        send(carrierSent, out);
        try {
            Carrier carrierReceived = (Carrier) in.readObject();
            roomId = (int) carrierReceived.getObject();
            close();
        } catch (Exception e) {
            close();
            e.printStackTrace();
        }
        return roomId;
    }

    public ArrayList groupList(String username) {
        ArrayList<User> groupList = new ArrayList<>();
        Carrier carrierSent = new Carrier(null, ContentType.getgrouplist, username);//不知道send null 行不行
        send(carrierSent, out);
        try {
            Carrier carrierReceived = (Carrier) in.readObject();
            groupList = (ArrayList<User>) carrierReceived.getObject();
            close();
        } catch (Exception e) {
            close();
            e.printStackTrace();
        }
        return groupList;
    }

    public int roomId(String username) {
        int roomId = 0;
        carrierSent = new Carrier(username, ContentType.getRoomId, username);
        send(carrierSent, out);
        try {
            Carrier carrierReceived = (Carrier) in.readObject();
            roomId = (int) carrierReceived.getObject();
            close();
        } catch (Exception e) {
            close();
            e.printStackTrace();
        }
        return roomId;
    }

    public synchronized void send(Carrier carrier, ObjectOutputStream out) {
        try {
            out.writeObject(carrier);
            out.flush();
            notify();
        } catch (IOException e) {
            close();
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            out.flush();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}