package lk.ijse.controller;

import javafx.animation.*;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import lk.ijse.dao.custom.DashBoardDAO;
import lk.ijse.dao.custom.impl.DashBoardDAOImpl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DashBoradFormController {

    @FXML
    private AnchorPane anchorDash1;


    @FXML
    private Label lblPaymentCount;

    @FXML
    private Label lblProgramCount;

    @FXML
    private Label lblStudentCount;

    @FXML
    private LineChart<String, Number> lineChart;

    @FXML
    private PieChart pieChart;

    DashBoardDAO dashBoardDAO = new DashBoardDAOImpl();

    public void initialize() throws IOException {
        int studentCount;
        int programCount;
        int paymentCount;

        iniLineChart();
        iniPieChart();

        studentCount = dashBoardDAO.getStudentCount();
        programCount = dashBoardDAO.getProgramCount();
        paymentCount = dashBoardDAO.getPaymentCount();

        setStudentCount(studentCount);
        setProgramCount(programCount);
        setPaymentCount(paymentCount);

        addLabelHoverEffect(lblStudentCount);
        addLabelHoverEffect(lblProgramCount);
        addLabelHoverEffect(lblPaymentCount);
    }

    private void setPaymentCount(int paymentCount) {
        lblPaymentCount.setText("" + paymentCount);
    }

    private void setProgramCount(int programCount) {
        lblProgramCount.setText(String.valueOf(programCount));
    }

    private void setStudentCount(int studentCount) {
        lblStudentCount.setText(String.valueOf(studentCount));
    }
/*
   private void iniPieChart() throws IOException {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        Map<String, Long> studentsByCourse = dashBoardDAO.getStudentCountByCourse();

        for (Map.Entry<String, Long> entry : studentsByCourse.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        pieChart.setData(pieChartData);
    }


    private void iniLineChart() throws IOException {
        XYChart.Series series = new XYChart.Series();
        Map<String, Double> paymentsByDay = dashBoardDAO.getPaymentsByDay();
        for (Map.Entry<String, Double> entry : paymentsByDay.entrySet()) {
            series.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
        }
        lineChart.getData().add(series);
    }
}*/


    private void iniPieChart() throws IOException {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        Map<String, Long> studentsByCourse = dashBoardDAO.getStudentCountByCourse();

        for (Map.Entry<String, Long> entry : studentsByCourse.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        pieChart.setData(pieChartData);

        javafx.application.Platform.runLater(() -> {
            for (PieChart.Data data : pieChartData) {
                if (data.getNode() != null) {
                    Tooltip tooltip = new Tooltip(data.getName() + ": " + data.getPieValue());
                    Tooltip.install(data.getNode(), tooltip);

                    data.getNode().setOnMouseEntered(event -> {
                        data.getNode().setStyle("-fx-scale-x: 1.1; -fx-scale-y: 1.1; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0, 0, 0);");
                    });
                    data.getNode().setOnMouseExited(event -> {
                        data.getNode().setStyle("-fx-scale-x: 1; -fx-scale-y: 1; -fx-effect: none;");
                    });
                }
            }
        });
        animatePieChart();
    }


    private void iniLineChart() throws IOException {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        Map<String, Double> paymentsByDay = dashBoardDAO.getPaymentsByDay();

        for (Map.Entry<String, Double> entry : paymentsByDay.entrySet()) {
            XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(entry.getKey(), entry.getValue());
            series.getData().add(dataPoint);
        }

        lineChart.getData().add(series);

        javafx.application.Platform.runLater(() -> {
            for (XYChart.Data<String, Number> dataPoint : series.getData()) {
                if (dataPoint.getNode() != null) {
                    Tooltip tooltip = new Tooltip(dataPoint.getXValue() + ": " + dataPoint.getYValue());
                    Tooltip.install(dataPoint.getNode(), tooltip);

                    dataPoint.getNode().setOnMouseEntered(event -> {
                        dataPoint.getNode().setStyle("-fx-scale-x: 1.5; -fx-scale-y: 1.5;");
                    });
                    dataPoint.getNode().setOnMouseExited(event -> {
                        dataPoint.getNode().setStyle("-fx-scale-x: 1; -fx-scale-y: 1;");
                    });
                }
            }
        });

        animateLineChart();
    }

    private void animatePieChart() {
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(5), pieChart);
        rotateTransition.setByAngle(2);
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
        rotateTransition.setInterpolator(javafx.animation.Interpolator.LINEAR);
        rotateTransition.play();
    }


    private void animateLineChart() {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), lineChart);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }

    private void addLabelHoverEffect(Label label) {
        label.setOnMouseEntered(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), label);
            scaleTransition.setToX(1.2);
            scaleTransition.setToY(1.2);
            scaleTransition.play();

            label.setStyle("-fx-text-fill: #007bff;");
        });

        label.setOnMouseExited(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), label);
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);
            scaleTransition.play();

            label.setStyle("-fx-text-fill: black;");
        });
    }

}
