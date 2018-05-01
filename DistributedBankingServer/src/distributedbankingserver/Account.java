/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distributedbankingserver;

import distributedbankmethods.DistributedBankMethods;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 *
 * @author Akash Thakrar
 */
public class Account extends UnicastRemoteObject implements DistributedBankMethods{
    int cur_bal;
    int last_tr_num;
    String account_num;
    
    public Account(String acc_no,int ob) throws RemoteException{
        cur_bal = ob;
        account_num = acc_no;
    }
    
    @Override
    public boolean deposit(int amt,int tr_no) throws RemoteException {
        if(tr_no == last_tr_num + 1){
            cur_bal = cur_bal + amt;
            last_tr_num = last_tr_num + 1;
            String a = account_num + " " + last_tr_num;
            Clients temp = new Clients(a);
            temp.start();
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean withdraw(int amt,int tr_no) throws RemoteException {
        if(tr_no == last_tr_num + 1){
            if(cur_bal >= amt){
                cur_bal = cur_bal - amt;
                last_tr_num = last_tr_num + 1;
                
                String a = account_num + " " + last_tr_num;
                Clients temp = new Clients(a);
                temp.start();
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    @Override
    public int check_balance() throws RemoteException {
        return cur_bal;
    }
}

class Clients extends Thread{
    String msg;
    Clients(String a){
        this.msg = a;
    }
    public DataInputStream din;
    public DataOutputStream dout;
    static ArrayList clients = new ArrayList();
    public Clients(Socket s) throws IOException{
        din = new DataInputStream(s.getInputStream());
        dout = new DataOutputStream(s.getOutputStream());
    }
    public void run(){
        if(clients.isEmpty()){
            ServerSocket ss = null;
            try {
                ss = new ServerSocket(5012);
                System.out.println("Socket created");
                while(true){
                    Socket s = ss.accept();
                    clients.add(new Clients(s));
                }
            } catch (IOException e) {
                System.out.println(e);
            }
            
        }
        else{
            Clients temp;
            for(int i=0;i<clients.size();i++){
                temp = (Clients)clients.get(i);
                try {
                    temp.dout.writeUTF(msg);
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }
    }
}