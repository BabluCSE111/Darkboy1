import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalTime;

public class EventReminderApp extends Application {

    // Event Model
    static class Event {
        String title;
        LocalDate date;
        LocalTime time;
        String description;

        Event(String title, LocalDate date, LocalTime time, String description) {
            this.title = title;
            this.date = date;
            this.time = time;
            this.description = description;
        }

        @Override
        public String toString() {
            return title + " | " + date + " " + time;
        }
    }

    private final ObservableList<Event> eventList = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {

        // UI Controls
        TextField titleField = new TextField();
        DatePicker datePicker = new DatePicker();
        TextField timeField = new TextField();
        TextArea descArea = new TextArea();

        titleField.setPromptText("Event Title");
        timeField.setPromptText("HH:MM");
        descArea.setPromptText("Description");

        ListView<Event> eventView = new ListView<>(eventList);

        Button addBtn = new Button("Add");
        Button editBtn = new Button("Edit");
        Button deleteBtn = new Button("Delete");

        // Add Event
        addBtn.setOnAction(e -> {
            if (!titleField.getText().isEmpty() && datePicker.getValue() != null) {
                Event event = new Event(
                        titleField.getText(),
                        datePicker.getValue(),
                        LocalTime.parse(timeField.getText()),
                        descArea.getText()
                );
                eventList.add(event);
                clearFields(titleField, datePicker, timeField, descArea);
            }
        });

        // Delete Event
        deleteBtn.setOnAction(e -> {
            Event selected = eventView.getSelectionModel().getSelectedItem();
            if (selected != null) eventList.remove(selected);
        });

        // Edit Event
        editBtn.setOnAction(e -> {
            Event selected = eventView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.title = titleField.getText();
                selected.date = datePicker.getValue();
                selected.time = LocalTime.parse(timeField.getText());
                selected.description = descArea.getText();
                eventView.refresh();
            }
        });

        eventView.setOnMouseClicked(e -> {
            Event selected = eventView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                titleField.setText(selected.title);
                datePicker.setValue(selected.date);
                timeField.setText(selected.time.toString());
                descArea.setText(selected.description);
            }
        });

        VBox form = new VBox(10, titleField, datePicker, timeField, descArea,
                new HBox(10, addBtn, editBtn, deleteBtn));
        form.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setLeft(form);
        root.setCenter(eventView);
        root.setPadding(new Insets(10));

        stage.setTitle("Event Reminder Application");
        stage.setScene(new Scene(root, 650, 400));
        stage.show();
    }

    private void clearFields(TextField t, DatePicker d, TextField time, TextArea a) {
        t.clear();
        d.setValue(null);
        time.clear();
        a.clear();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
