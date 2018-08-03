package fxapp;

import fxapp.db.Student;
import fxapp.logic.Logic;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import sun.rmi.runtime.Log;

import java.util.List;
import java.util.Optional;

public class Controller {

    @FXML private TableView<Student> table;
    @FXML private TableColumn<Student, Integer> idColumn;
    @FXML private TableColumn<Student, String> nameColumn;
    @FXML private TableColumn<Student, String> lastNameColumn;
    @FXML private TableColumn<Student, Integer> ageColumn;
    @FXML private TextField nameField;
    @FXML private TextField lastNameField;
    @FXML private TextField ageField;


//    @FXML
    public void initialize(){
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
    }

    public void showStudents() {
        Logic logic = Logic.getInstance();
        List<Student> students = logic.findAllStudents();
        table.setItems(FXCollections.observableArrayList(students));

    }

    public void addStudent() {
        String name = nameField.getText();
        String lastName = lastNameField.getText();
        try {
            int age = Integer.parseInt(ageField.getText());
            Logic.getInstance().addStudent(name, lastName, age);
            clearFields(nameField, lastNameField, ageField);
            showStudents();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ageField.getText() + "is not a Number!");
            alert.show();
        }
    }

    private void clearFields(TextField...fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }

    public void deleteStudent() {
        Student student = table.getSelectionModel().getSelectedItem();
        if(student==null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "No student selected!");
            alert.show();
        } else {
            Logic.getInstance().deleteStudent(student);
            showStudents();
        }
    }

    public void editStudent() {
        Student student = table.getSelectionModel().getSelectedItem();
        if(student==null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "No student selected!");
            alert.show();
        } else {
            Dialog<Student> dialog = new Dialog<>();
            dialog.setTitle("Edit Student");
            dialog.setHeaderText("Redaktirovanie:");

            ButtonType updateButton = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(updateButton, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(5);  // отступ по горизонтали
            grid.setVgap(5);  // отступ по вертикали

            TextField studentName = new TextField();
            studentName.setText(student.getName());

            TextField studentLastName = new TextField();
            studentLastName.setText(student.getLastName());

            TextField studentAge = new TextField();
            studentAge.setText(String.valueOf(student.getAge()));

            grid.add(new Label("Name"), 0, 0);
            grid.add(studentName, 1, 0);
            grid.add(new Label("lastName"), 0, 1);
            grid.add(studentLastName, 1, 1);
            grid.add(new Label("Age"), 0,2);
            grid.add(studentAge, 1,2);

            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(b->{
                if(b==updateButton) {
                    return new Student(student.getId(), studentName.getText(), studentLastName.getText(), Integer.parseInt(studentAge.getText()));
                } else {
                    return null;
                }
            });

            Optional<Student> result = dialog.showAndWait();
            if(result.isPresent()) {
                Logic.getInstance().updateStudent(result.get());
                showStudents();
            }
        }
    }
}