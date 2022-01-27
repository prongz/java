/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.elltechs._gui_oxy_rev02;

import com.fazecast.jSerialComm.SerialPort;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Soheil
 */
public class PAGE006Controller implements Initializable {

    @FXML
    private Text oxygenText;
    @FXML
    private TextField textbox1;
    @FXML
    private TextField textbox2;
    @FXML
    private TextField textbox4;
    @FXML
    private TextField textbox3;
    @FXML
    private Button butMnus;
    @FXML
    private Button but1;
    @FXML
    private Button but2;
    @FXML
    private Button but3;
    @FXML
    private Button but4;
    @FXML
    private Button but5;
    @FXML
    private Button but6;
    @FXML
    private Button but7;
    @FXML
    private Button but8;
    @FXML
    private Button but9;
    @FXML
    private Button butBack;
    @FXML
    private Button but0;
    @FXML
    private Button butEnt1;
    @FXML
    private Button butReset;
    @FXML
    private Button butfreeAir;
    @FXML
    private Button butPont;
    @FXML
    private Button butEnt2;
    @FXML
    private Button butEnt3;
    @FXML
    private Button butEnt4;

    private int selectedIndex = 0;

    private oxySensorData osd = PAGE003Controller.getOsd();

    private OxySensorCalib osCalib = PAGE003Controller.getOscalib();

    private SerialPort sp;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        defineAction(but1);
        defineAction(but2);
        defineAction(but3);
        defineAction(but4);
        defineAction(but5);
        defineAction(but6);
        defineAction(but7);
        defineAction(but8);
        defineAction(but9);
        defineAction(but0);
        defineAction(butBack);
        defineAction(butEnt1);
        defineAction(butEnt2);
        defineAction(butEnt3);
        defineAction(butEnt4);
        defineAction(butReset);
        defineAction(butfreeAir);
        defineAction(butPont);
        //defineAction(butMnus);

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

        Platform.runLater(() -> textbox1.requestFocus());

        textbox1.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (newPropertyValue) {
                    System.out.println("Textfield 1 on focus");
                    selectedIndex = 0;
                } else {
                    System.out.println("Textfield 1 out focus");
                }
            }
        });
        textbox2.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (newPropertyValue) {
                    System.out.println("Textfield 2 on focus");
                    selectedIndex = 1;
                } else {
                    System.out.println("Textfield 2 out focus");
                }
            }
        });
        textbox3.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (newPropertyValue) {
                    System.out.println("Textfield 3 on focus");
                    selectedIndex = 2;
                } else {
                    System.out.println("Textfield 3 out focus");
                }
            }
        });
        textbox4.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (newPropertyValue) {
                    System.out.println("Textfield 4 on focus");
                    selectedIndex = 3;
                } else {
                    System.out.println("Textfield 4 out focus");
                }
            }
        });

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

                        if (dataBytes.size() == 12 && dataBytes.get(0) == 0x3a) {
                            osd.inputDecode(dataBytes);

                        }
                    }

                    Thread.sleep(50);
                    Platform.runLater(() -> {
                        oxygenText.setText(String.format("%1$,.3f", osCalib.calibratedOxyRate()));
                    });
                } catch (InterruptedException ex) {
                    System.out.println("com.elltechs._gui_oxy_rev02.PAGE004Controller.initialize()");
                }
            }
        }).start();
    }

    @FXML
    private void buttMnusClk(ActionEvent event) {

    }

    @FXML
    private void butt0Clk(ActionEvent event) {
        keyboardNum("0");
    }

    @FXML
    private void butt1Clk(ActionEvent event) {
        keyboardNum("1");
    }

    @FXML
    private void butt2Clk(ActionEvent event) {
        keyboardNum("2");
    }

    @FXML
    private void butt3Clk(ActionEvent event) {
        keyboardNum("3");
    }

    @FXML
    private void butt4Clk(ActionEvent event) {
        keyboardNum("4");
    }

    @FXML
    private void butt5Clk(ActionEvent event) {
        keyboardNum("5");
    }

    @FXML
    private void butt6Clk(ActionEvent event) {
        keyboardNum("6");
    }

    @FXML
    private void butt7Clk(ActionEvent event) {
        keyboardNum("7");
    }

    @FXML
    private void butt8Clk(ActionEvent event) {
        keyboardNum("8");
    }

    @FXML
    private void butt9Clk(ActionEvent event) {
        keyboardNum("9");
    }

    @FXML
    private void buttPontClk(ActionEvent event) {
        keyboardNum(".");
    }

    @FXML
    private void buttBackClk(ActionEvent event) {
        if (selectedIndex == 0) {
            if (textbox1.getText().length() > 0) {
                textbox1.setText(textbox1.getText().substring(0, textbox1.getText().length() - 1));
            }
        } else if (selectedIndex == 1) {
            if (textbox2.getText().length() > 0) {
                textbox2.setText(textbox2.getText().substring(0, textbox2.getText().length() - 1));
            }
        } else if (selectedIndex == 2) {
            if (textbox3.getText().length() > 0) {
                textbox3.setText(textbox3.getText().substring(0, textbox3.getText().length() - 1));
            }
        } else if (selectedIndex == 3) {
            if (textbox4.getText().length() > 0) {
                textbox4.setText(textbox4.getText().substring(0, textbox4.getText().length() - 1));
            }
        }
    }

    @FXML
    private void buttEnt1Clk(ActionEvent event) {
        try {
            osCalib.setX1Y1((float) Double.parseDouble(textbox1.getText()));
            System.out.println("*********setX1Y1 = " + osCalib.getX1() + "," + osCalib.getY1() + "************");

            Platform.runLater(() -> {
                textbox1.setStyle("-fx-text-fill: green; -fx-background-color :  transparent;");
            });
            calibrateMode();
        } catch (NumberFormatException ex) {
            Platform.runLater(() -> {
                textbox1.setStyle("-fx-text-fill: red; -fx-background-color :  transparent;");
            });
        }

    }

    @FXML
    private void buttEnt2Clk(ActionEvent event) {
        try {
            osCalib.setX2Y2((float) Double.parseDouble(textbox2.getText()));
            System.out.println("*********setX2Y2 = " + osCalib.getX1() + "," + osCalib.getY1() + "************");

            Platform.runLater(() -> {
                textbox2.setStyle("-fx-text-fill: green; -fx-background-color :  transparent;");
            });
            calibrateMode();
        } catch (NumberFormatException ex) {
            Platform.runLater(() -> {
                textbox2.setStyle("-fx-text-fill: red; -fx-background-color :  transparent;");
            });
        }
    }

    @FXML
    private void buttEnt3Clk(ActionEvent event) {
        try {
            osCalib.setX3Y3((float) Double.parseDouble(textbox3.getText()));
            System.out.println("*********setX3Y3 = " + osCalib.getX1() + "," + osCalib.getY1() + "************");

            Platform.runLater(() -> {
                textbox3.setStyle("-fx-text-fill: green; -fx-background-color :  transparent;");
            });
            calibrateMode();
        } catch (NumberFormatException ex) {
            Platform.runLater(() -> {
                textbox3.setStyle("-fx-text-fill: red; -fx-background-color :  transparent;");
            });
        }
    }

    @FXML
    private void buttEnt4Clk(ActionEvent event) {
        try {
            osCalib.setX4Y4((float) Double.parseDouble(textbox4.getText()));
            System.out.println("*********setX4Y4 = " + osCalib.getX1() + "," + osCalib.getY1() + "************");

            Platform.runLater(() -> {
                textbox4.setStyle("-fx-text-fill: green; -fx-background-color :  transparent;");
            });
            calibrateMode();
        } catch (NumberFormatException ex) {
            Platform.runLater(() -> {
                textbox4.setStyle("-fx-text-fill: red; -fx-background-color :  transparent;");
            });
        }
    }

    @FXML
    private void butResetClk(ActionEvent event) throws IOException {
        sp.closePort();
        App.setRoot("PAGE003");
        System.out.println("com.elltechs._gui_oxy_rev02.PAGE004Controller.butResetClk()");
    }

    @FXML
    private void butfreeAirClk(ActionEvent event) {
        byte[] b = {':'};
        System.out.println("com.elltechs._gui_oxy_rev02.PAGE004Controller.butfreeAirClk()");
        sp.writeBytes(b, 1);
        Platform.runLater(() -> {
        //butfreeAir.setStyle("-fx-background-color : green;");
         System.out.println("-fx-background-color : green;");
           
        //butfreeAir.setStyle(value);
        });
        
          osCalib.setX1Y1((float) 0.0);
        
        
        osCalib.setX2Y2((float) 0.0);
        
        
        osCalib.setX3Y3((float) 0.0);
       
        
        osCalib.setX4Y4((float) 0.0);
       
        Platform.runLater(() -> {
            textbox1.setText("0");
            textbox2.setText("0");
            textbox3.setText("0");
            textbox4.setText("0");
        });
        calibrateMode();
    }

    private void defineAction(Button but) {
        but.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                new Thread(() -> {
                    try {
                        Platform.runLater(() -> {
                            but.setStyle("-fx-background-color: #000000; ");
                        });
                        Thread.sleep(200);
                        Platform.runLater(() -> {
                            but.setStyle("-fx-background-color: transparent ");
                        });
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                ).start();
            }
        });
      
        /*but.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                but.setStyle("-fx-background-color: transparent ");
            }
        });*/
    }

    private void keyboardNum(String key) {
        if (selectedIndex == 0) {
            textbox1.setText(textbox1.getText() + String.valueOf(key));
            Platform.runLater(() -> {
                textbox1.setStyle("-fx-text-fill: black; -fx-background-color :  transparent;");
            });
        } else if (selectedIndex == 1) {
            textbox2.setText(textbox2.getText() + String.valueOf(key));
            Platform.runLater(() -> {
                textbox2.setStyle("-fx-text-fill: black; -fx-background-color :  transparent;");
            });

        } else if (selectedIndex == 2) {
            textbox3.setText(textbox3.getText() + String.valueOf(key));
            Platform.runLater(() -> {
                textbox3.setStyle("-fx-text-fill: black; -fx-background-color :  transparent;");
            });

        } else if (selectedIndex == 3) {
            textbox4.setText(textbox4.getText() + String.valueOf(key));
            Platform.runLater(() -> {
                textbox4.setStyle("-fx-text-fill: black; -fx-background-color :  transparent;");
            });

        }
    }

    private void calibrateMode() {
        if ( osCalib.getY1() > 0 && osCalib.getY2() > 0 && osCalib.getY3() > 0 &&  osCalib.getY4() > 0 ){
            osCalib.setSysStatus(OxySensorCalib.status.FOURPOINT);
            System.out.printf("OxySensorCalib.status.FOURPOINT");
        }
        else if (osCalib.getY1() > 0 && osCalib.getY2() > 0 && osCalib.getY3() > 0){
             osCalib.setSysStatus(OxySensorCalib.status.THREEPOINT);
             System.out.printf("OxySensorCalib.status.THREEPOINT");
        }
        else if (osCalib.getY1() > 0 && osCalib.getY2() > 0){
               osCalib.setSysStatus(OxySensorCalib.status.TWOPOINT);
               System.out.printf("OxySensorCalib.status.TWOPOINT");
        }
        else {
            osCalib.setSysStatus(OxySensorCalib.status.NOTSET);
             System.out.printf("OxySensorCalib.status.NOTSET");
        }
        
        try {
         FileOutputStream fileOut =
         new FileOutputStream("calib.dat");
         ObjectOutputStream out = new ObjectOutputStream(fileOut);
         out.writeObject(osCalib);
         out.close();
         fileOut.close();
         System.out.printf("Serialized data is saved in calib.ser");
      } catch (IOException i) {
         i.printStackTrace();
      }

          /* FileWriter writer = null;
            try {
                 writer = new FileWriter("calib.txt");
                
                writer.write(String.format("X1Y1 = %f , %f \r\n", osCalib.getX1(),osCalib.getY1()));
                writer.write(String.format("X2Y2 = %f , %f \r\n", osCalib.getX2(),osCalib.getY2()));
                writer.write(String.format("X3Y3 = %f , %f \r\n", osCalib.getX3(),osCalib.getY3()));
                writer.write(String.format("X3Y3 = %f , %f \r\n", osCalib.getX4(),osCalib.getY4()));
                writer.close();
                
            } catch (IOException ex) {
                // Report
            } finally {
               try {writer.close();} catch (Exception ex) {}
            } */
        
    }

}
