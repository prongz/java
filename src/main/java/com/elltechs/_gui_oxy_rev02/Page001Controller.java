/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elltechs._gui_oxy_rev02;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author Erwin
 */
public class Page001Controller implements Initializable {

    @FXML
    private ImageView logoImageId;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
         new Thread(() -> {
          
            while (logoImageId.getOpacity() < 1.0) {

                try {
                    Thread.sleep(20);
                    Platform.runLater(() -> {
                        logoImageId.setOpacity(logoImageId.getOpacity() + 0.01);
                    });
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                
             
            }
               try {
                    Thread.sleep(4000);
                    App.setRoot("PAGE002");
                    System.out.println("com.elltechs._gui_oxy_rev02.Page001Controller.initialize()");
                } catch (InterruptedException | IOException ex) {
                    ex.printStackTrace();
                }
        }).start();
    }    
    
}
