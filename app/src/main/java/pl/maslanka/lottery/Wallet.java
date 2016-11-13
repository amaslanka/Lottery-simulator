package pl.maslanka.lottery;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Artur on 11.10.2016.
 */

public class Wallet implements Serializable {

    private long value;
    private int[] hits06;


    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }


    public Wallet() {
        value = 0;
        hits06 = new int[7];
    }

    public Wallet deepCopy() {

        Wallet copied;

        try {
            //Serialization of object
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(this);

            //De-serialization of object
            ByteArrayInputStream bis = new   ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bis);
            copied = (Wallet) in.readObject();

            //Verify that object is not corrupt

            //validateNameParts(fName);
            //validateNameParts(lName);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }

        return copied;
    }


    public int[] getHits06() {
        return hits06;
    }

    public void setHits06(int[] hits06) {
        this.hits06 = hits06;
    }

}
