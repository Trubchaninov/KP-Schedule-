package com.table.schedule;

import com.table.schedule.DBeditor.DBConnection;
import com.table.schedule.DBeditor.Dao;
import com.table.schedule.DBeditor.ModelPass;
import com.table.schedule.DBeditor.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    public ToggleButton visiblity;
    public PasswordField password;
    public TextField name;
    Dao dbConnection;

    /** enter in program
     * @throws IOException no user
     */
    @FXML
    private void SignIn() throws IOException {
        int UserID;
        if(!name.getText().isEmpty() && !password.getText().isEmpty()) {
            UserID = dbConnection.getUser(name.getText(), password.getText());
            if(UserID!=-1){
                enterToSchedule(UserID);
                name.getScene().getWindow().hide();
            }
            else new Alert(Alert.AlertType.ERROR,"Данного пользователя не существует!").show();
        }
    }

    /** create new user
     * @throws IOException no user
     */
    @FXML
    private void SignUp() throws IOException {
        int UserID;
        if(!name.getText().isEmpty() && !password.getText().isEmpty()) {
            UserID = dbConnection.addUser(new User(-1, name.getText(), password.getText(), visiblity.isSelected()));
            if(UserID!=-1){
                enterToSchedule(UserID);
            name.getScene().getWindow().hide();
            }
            else new Alert(Alert.AlertType.ERROR,"Ползователь с таким логином уже существует!").show();
        }
    }

    /** create new controller
     * @param user entered
     * @throws IOException no user
     */
    public void enterToSchedule(int user) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Расписание");
        stage.setScene(scene);
        MainController controller=fxmlLoader.getController();
        controller.init(user);
        stage.show();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ModelPass pass=new ModelPass(1);
        dbConnection= pass.getGenerator();
    }
}