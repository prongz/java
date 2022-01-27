/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elltechs._gui_oxy_rev02;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Soheil
 */
public class oxySensorData implements Serializable {
    private float oxygenRate;
    private float oxyUr;
    private byte  err_code;
    private byte  crc;
    private List<Byte> rawDataBytes = new ArrayList<Byte>();

    public oxySensorData() {
        this.oxygenRate = 0;
        this.oxyUr     = 0;
        this.err_code   = 0x3f;
        this.crc        = 0; 
    };
    public void inputDecode(List<Byte> dataBytes)
    {
        if (dataBytes.size() == 12 && dataBytes.get(0) == 0x3a )
        {
            this.rawDataBytes = new ArrayList<>(dataBytes);
            byte[] bytes = new byte[4];
            bytes[0] = dataBytes.get(4);
            bytes[1] = dataBytes.get(3);
            bytes[2] = dataBytes.get(2);
            bytes[3] = dataBytes.get(1);
            int s =  ByteBuffer.wrap(bytes).getInt();
            //System.out.printf("DATA INT **** = %02x %02x %02x %02x %08x\r\n",dataBytes.get(1),dataBytes.get(2),dataBytes.get(3),dataBytes.get(4),s); 
             
             this.oxygenRate =   Float.intBitsToFloat( s);
                     
             
        }
        {
        byte[] bytes = new byte[4];
            bytes[0] = dataBytes.get(8);
            bytes[1] = dataBytes.get(7);
            bytes[2] = dataBytes.get(6);
            bytes[3] = dataBytes.get(5);
            int s =  ByteBuffer.wrap(bytes).getInt();
        this.oxyUr =  Float.intBitsToFloat( s);
        //System.out.printf("DATA INT = %f",this.oxyUr);
                   
    }
        this.err_code = dataBytes.get(9);
        
    }

    public float getOxygenRate() {
        return oxygenRate;
    }

    public void setOxygenRate(float oxygenRate) {
        this.oxygenRate = oxygenRate;
    }

    public float getOxyUr() {
        return oxyUr;
    }

    public void setOxyUR(float oxyUr) {
        this.oxyUr = oxyUr;
    }

    public byte getCrc() {
        return crc;
    }

    public void setCrc(byte crc) {
        this.crc = crc;
    }

    public List<Byte> getRawDataBytes() {
        return rawDataBytes;
    }

    public void setRawDataBytes(List<Byte> rawDataBytes) {
        this.rawDataBytes = rawDataBytes;
    }

    public byte getErr_code() {
        return err_code;
    }


    
    
    
    
    
    
}


