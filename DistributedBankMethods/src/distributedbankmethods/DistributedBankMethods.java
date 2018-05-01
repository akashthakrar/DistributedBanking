/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distributedbankmethods;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Akash Thakrar
 */
public interface DistributedBankMethods extends Remote{
    boolean deposit(int amt,int tr_no) throws RemoteException;
    boolean withdraw(int amt,int tr_no) throws RemoteException;
    int check_balance() throws RemoteException;
}
