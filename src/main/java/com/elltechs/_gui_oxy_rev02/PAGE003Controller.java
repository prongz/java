/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elltechs._gui_oxy_rev02;

import com.fazecast.jSerialComm.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import static java.time.Clock.system;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Erwin
 */
public class PAGE003Controller implements Initializable {

    @FXML
    private Text Value1Id;
    @FXML
    private Text Value2Id;

    private double Value1 = 0.2;

    private double Value2 = 20;

    private byte errCode = (byte) 0xff;
    @FXML
    private ImageView imageYellowId;
    @FXML
    private ImageView imageBlue1Id;
    @FXML
    private ImageView imageRedId;
    @FXML
    private ImageView imageBlue0Id;
    @FXML
    private ImageView logoImageId1;
    @FXML
    private ImageView logoImageId2;
    @FXML
    private ImageView logoImageId3;
    @FXML
    private Button calibBtnId;

    private SerialPort sp;

    private static oxySensorData osd = new oxySensorData();

    private static OxySensorCalib osCalib = new OxySensorCalib(osd);
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        loadCalibData("calib.dat");
        imageRedId.setVisible(false);
        imageYellowId.setVisible(false);
        SerialPort[] serialPorts = SerialPort.getCommPorts();
        System.out.println(String.format("Number of Port find = %d", serialPorts.length));
        for (int i = 0; i < serialPorts.length; i++) {
            System.out.println(String.format("PORT[%d] = %s", i, serialPorts[i].getDescriptivePortName()));
        }
        sp = serialPorts[0];
        sp.openPort(100);
        while (!sp.isOpen()) {
            System.out.println("cannot opn port");
            sp.openPort(100);
            System.out.println("RETRY");
        }
        new Thread(() -> {

            while (true) {
                try {
                   
                    List<Byte> dataBytes = new ArrayList<Byte>();
                    byte[] e = new byte[10];
                    while (sp.bytesAvailable() > 12) {
                        sp.readBytes(e, 1);
                    }
                    while (sp.bytesAvailable() < 12) {
                        Thread.sleep(1);
                    }
                    if (sp.bytesAvailable() == 12) {
                        do {
                            sp.readBytes(e, 1);
                            dataBytes.add(e[0]);
                        } while (sp.bytesAvailable() > 0);
                        System.out.printf("DATA %d GETED = ", dataBytes.size());
                        for (int i = 0; i < dataBytes.size(); i++) {
                            System.out.printf(" %02x ", dataBytes.get(i));
                        }
                        System.out.printf(" DATA END\r\n");
                        if (dataBytes.size() == 12 && dataBytes.get(0) == 0x3a) {
                            osd.inputDecode(dataBytes);
                            System.out.printf("VALID DATA\r\n");
                            System.out.printf(" OXY RATE IS : %f\r\n", osd.getOxygenRate());
                        }
                    }

                    //if 
                    Thread.sleep(50);
                    //Value1 = Value1 + Math.random() * 0.005;
                    Value1 = osCalib.calibratedOxyRate();//osd.getOxygenRate()*App.getA()+App.getB();
                    //Value2 = Value2 + Math.random() * 0.02;
                    Value2 = osd.getOxyUr();

                    errCode = osd.getErr_code();
                    System.out.printf("errCode = %02x\r\n", errCode);
                    System.out.printf("value2 = %f\r\n", Value2);

                    Platform.runLater(() -> {
                        Value1Id.setText(String.format("%1$,.3f", Value1));
                        Value2Id.setText(String.format("%1$,.2f", Value1 * 14.72));

                        if (errCode == (byte) 0x7f) {
                            imageBlue1Id.setVisible(false);
                            imageBlue0Id.setVisible(false);
                            imageYellowId.setVisible(false);
                            imageRedId.setVisible(true);
                            Value1Id.setFill(Color.web("#FF0000"));
                            System.out.printf("RED\r\n");

                        } else if (Value2 > 1.4 || Value2 < 0.5) {
                            imageBlue1Id.setVisible(false);
                            imageBlue0Id.setVisible(false);
                            imageYellowId.setVisible(true);
                            imageRedId.setVisible(false);
                            Value1Id.setFill(Color.web("#FFF200"));
                            System.out.printf("YELOOW\r\n");

                        } else if (errCode == (byte) 0xff) {

                            imageBlue1Id.setVisible(true);
                            imageBlue0Id.setVisible(true);
                            imageYellowId.setVisible(false);
                            imageRedId.setVisible(false);
                            Value1Id.setFill(Color.web("#282828"));
                            System.out.printf("BLUE\r\n");
                        }
                         
                              loadCalibData("calib.dat");   
                    });
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    public static oxySensorData getOsd() {
        return osd;
    }

    public static OxySensorCalib getOscalib() {
        return osCalib;
    }

    @FXML
    private void calibBtnIdClk(ActionEvent event) throws IOException {
        System.out.println("com.elltechs._gui_oxy_rev02.PAGE003Controller.calibBtnIdClk()");
        sp.closePort();
        App.setRoot("PAGE006");

    }

    private void loadCalibData(String path) {
       try
        {   
            // Reading the object from a file
            FileInputStream file = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(file);
              
            // Method for deserialization of object
            OxySensorCalib rd = (OxySensorCalib)in.readObject();
              
            in.close();
            file.close();
              
            System.out.println("Object has been deserialized ");
            System.out.println("x1 = " + rd.getX1());
            System.out.println("y1 = " + rd.getY1());
            osCalib.setX1(rd.getX1());
            osCalib.setY1(rd.getY1());
            osCalib.setX2(rd.getX2());
            osCalib.setY2(rd.getY2());
            osCalib.setX3(rd.getX3());
            osCalib.setY3(rd.getY3());
            osCalib.setX4(rd.getX4());
            osCalib.setY4(rd.getY4());
            osCalib.setSysStatus(rd.getSysStatus());
        }
        catch(IOException ex)
        {
            System.out.println("IOException is caught");
        }
          
        catch(ClassNotFoundException ex)
        {
            System.out.println("ClassNotFoundException is caught");
        }

    }
}
