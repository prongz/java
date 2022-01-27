/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elltechs._gui_oxy_rev02;

import com.fazecast.jSerialComm.SerialPort;
import java.io.IOException;
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
import javafx.scene.input.TouchEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Soheil
 */
public class PAGE004Controller implements Initializable {

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
    private Button butEnt;
    @FXML
    private Text oxygenText;
    @FXML
    private Button butReset;
    @FXML
    private TextField textbox1;
    @FXML
    private TextField textbox2;
    @FXML
    private Button butfreeAir;

    private int selectedIndex = 0;
    @FXML
    private Button butPont;
    @FXML
    private Button butMnus;

    private SerialPort sp;
    
    private oxySensorData osd = new oxySensorData();
   

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
        defineAction(butEnt);
        defineAction(butReset);
        defineAction(butfreeAir);
        defineAction(butPont);
        defineAction(butMnus);
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
         new Thread(() -> {

            while (true) {
                try {
                     List<Byte> dataBytes = new ArrayList<Byte>();
                    byte[] e = new byte[10];
                    while (sp.bytesAvailable() > 12 )
                        {
                            sp.readBytes(e, 1);
                        }   
                    while (sp.bytesAvailable() <  12)
                        Thread.sleep(1);
                    if (sp.bytesAvailable()== 12)
                        {
                            do {
                                 sp.readBytes(e, 1);
                                 dataBytes.add(e[0]);
                            }    while (sp.bytesAvailable()> 0);
                            System.out.printf("DATA %d GETED = ",dataBytes.size());
                               for ( int i = 0  ; i < dataBytes.size();i++)
                                    {
                                            System.out.printf(" %02x ",dataBytes.get(i));
                                    }
                             System.out.printf(" DATA END\r\n");
                             if (dataBytes.size() == 12 && dataBytes.get(0) == 0x3a )
                                    {
                                        osd.inputDecode(dataBytes);
                                        System.out.printf("VALID DATA\r\n"); 
                                        System.out.printf (" OXY RATE IS : %f\r\n",osd.getOxygenRate());
                                    }
                        } 
                    
                    //if 
                    Thread.sleep(50);
                    Platform.runLater(() -> {
                            oxygenText.setText(String.format("%1$,.3f", osd.getOxygenRate()*App.getA()+App.getB()));
                        });
            } 
                catch(InterruptedException ex)
                {
                    System.out.println("com.elltechs._gui_oxy_rev02.PAGE004Controller.initialize()");
                }
            }
        }).start();
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
        } else {
            textbox2.setText(textbox2.getText() + String.valueOf(key));
            Platform.runLater(() -> {
            textbox2.setStyle("-fx-text-fill: black; -fx-background-color :  transparent;");
            });

        }
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
    private void buttBackClk(ActionEvent event) {
        if (selectedIndex == 0) {
            if (textbox1.getText().length() > 0) {
                textbox1.setText(textbox1.getText().substring(0, textbox1.getText().length() - 1));
            }
        } else if (textbox2.getText().length() > 0) {
            textbox2.setText(textbox2.getText().substring(0, textbox2.getText().length() - 1));
        }

    }

    @FXML
    private void butt0Clk(ActionEvent event) {
        keyboardNum("0");
    }

    private void buttEntClk(ActionEvent event) {
          if (selectedIndex == 0) {
              try{
              App.setB(Double.parseDouble(textbox1.getText()));
              System.out.println("*********SET B************");
              Platform.runLater(() -> {
              textbox1.setStyle("-fx-text-fill: green; -fx-background-color :  transparent;");
                });
              }
              catch(NumberFormatException ex)
              {
                  Platform.runLater(() -> {
                      textbox1.setStyle("-fx-text-fill: red; -fx-background-color :  transparent;");
                });
              }
          }
          else
          {
              try{
              App.setA(Double.parseDouble(textbox2.getText()));
                  System.out.println("*********SET B************");
                  Platform.runLater(() -> {
              textbox2.setStyle("-fx-text-fill: green; -fx-background-color :  transparent;");
                });
              }
               catch(NumberFormatException ex)
              {
                  Platform.runLater(() -> {
                      textbox2.setStyle("-fx-text-fill: red; -fx-background-color :  transparent;");
                });
              }
          }
    }

    @FXML
    private void butResetClk(ActionEvent event) throws IOException {
        sp.closePort();
        App.setRoot("PAGE003");
        System.out.println("com.elltechs._gui_oxy_rev02.PAGE004Controller.butResetClk()");
    }

    @FXML
    private void butfreeAirClk(ActionEvent event) throws IOException {
        byte[] b = {':'};
        System.out.println("com.elltechs._gui_oxy_rev02.PAGE004Controller.butfreeAirClk()");
        sp.writeBytes(b, 1);
        App.setA(1);
        App.setB(0);
        Platform.runLater(() -> {
        //butfreeAir.setStyle("-fx-background-color : green;");
         System.out.println("-fx-background-color : green;");
           
        //butfreeAir.setStyle(value);
        });
        /*
         new Thread(() -> {
             Platform.runLater(() -> {
                 butfreeAir.setStyle("-fx-background-color :  green;");
                 
          
          });
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
          Platform.runLater(() -> {
              butfreeAir.setStyle("-fx-background-color :  transparent;");
          
          });
                 }).start();*/
    }

    @FXML
    private void buttPontClk(ActionEvent event) {
        keyboardNum(".");
    }

    @FXML
    private void buttMnusClk(ActionEvent event) {
        if (selectedIndex == 0) {
             Platform.runLater(() -> {
              textbox1.setStyle("-fx-text-fill: black; -fx-background-color :  transparent;");
              
                });
            if (textbox1.getText().length() > 0) {
                if (textbox1.getText().charAt(0) == '-') {
                    textbox1.setText(textbox1.getText().substring(1, textbox1.getText().length()));
                } else {
                    textbox1.setText("-" + textbox1.getText());
                }
            } else {
                textbox1.setText("-");
            }
        } else if (textbox2.getText().length() > 0) {
             Platform.runLater(() -> {
              textbox2.setStyle("-fx-text-fill: black; -fx-background-color :  transparent;");
                });
            if (textbox2.getText().charAt(0) == '-') {
                textbox2.setText(textbox2.getText().substring(1, textbox2.getText().length()));
            } else {
                textbox2.setText("-" + textbox2.getText());
            }
        } else {
            textbox2.setText("-");
        }
    }

    @FXML
    private void buttEnt1Clk(ActionEvent event) {
    }

}
