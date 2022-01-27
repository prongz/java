/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elltechs._gui_oxy_rev02;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.image.ImageView;
/**
 * FXML Controller class
 *
 * @author Erwin
 */
public class Page002Controller implements Initializable {


    @FXML
    private ImageView logoImage2Id;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
         new Thread(() -> {
        try {
                    Thread.sleep(3000);
                    App.setRoot("PAGE003");
                } catch (InterruptedException | IOException ex) {
                    ex.printStackTrace();
                }
         }).start();
    }    
                 
    
}
