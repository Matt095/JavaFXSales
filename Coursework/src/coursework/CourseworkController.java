package coursework;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class CourseworkController implements Initializable {
    private String markup;
    private LinkedList<CarSales> carSales;
    private List<String> yearFilterBar;
    private List<String> yearFilterLine;
    private List<String> vehicleFilterBar;
    private List<String> vehicleFilterLine;
    private List<String> regionFilterBar;
    private List<String> regionFilterLine;
    
    private DashService service;
    
    private CheckBox[] checkBoxes, checkBoxes2, checkBoxes3, checkBoxes4, checkBoxes5, checkBoxes6;
    
    @FXML 
    private AnchorPane AnchorPane1;
    
    @FXML
    private RadioButton RadioButton1, RadioButton3, RadioButton4, RadioButton5, RadioButton6;
    
    @FXML
    private BarChart<?,?>BarChart1, BarChart2, BarChart3;
    
    @FXML
    private LineChart<?,?>LineChart1, LineChart2, LineChart3;
    
    @FXML
    private HBox HBox1, HBox2, HBox3, HBox4, HBox5, HBox6, HBox7, HBox8;
    
    @FXML
    private TableView TableView1;
    
    @FXML
    private TableColumn ColYear;
    
    @FXML
    private TableColumn ColQTR;
    
    @FXML
    private TableColumn ColRegion;
    
    @FXML
    private TableColumn ColVehicle;
    
    @FXML
    private TableColumn ColSales;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        service = new DashService();
        service.setAddress("http://glynserver.cms.livjm.ac.uk/DashService/SalesGetSales");
        service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent e) {
                markup = e.getSource().getValue().toString();
                carSales = (new Gson()).fromJson(markup, new TypeToken<LinkedList<CarSales>>() {}.getType());
                
                /*Testing
                carSales.add(new CarSales("Africa", "Audi", 90, "2016", 1));
                carSales.add(new CarSales("Africa", "Audi", 200, "2016", 2));
                */

                System.out.println(markup);

                yearFilterBar = carSales.stream().map(o -> o.getYear()).distinct().collect(Collectors.toList());
                yearFilterLine = carSales.stream().map(o -> o.getYear()).distinct().collect(Collectors.toList());
                vehicleFilterBar = carSales.stream().map(o -> o.getVehicle()).distinct().collect(Collectors.toList());
                vehicleFilterLine = carSales.stream().map(o -> o.getVehicle()).distinct().collect(Collectors.toList());
                regionFilterBar = carSales.stream().map(o -> o.getRegion()).distinct().collect(Collectors.toList());
                regionFilterLine = carSales.stream().map(o -> o.getRegion()).distinct().collect(Collectors.toList());
                
                //System.out.println(yearFilterBar);

                for (CarSales carSale : carSales) {
                    System.out.println(carSale.toString());
                }
                constructCheckBoxes6();
                constructCheckBoxes5();
                constructCheckBoxes4();
                constructCheckBoxes3();
                constructCheckBoxes2();
                constructCheckBoxes();
                
            }
        });

        service.start();
        BarChart1.visibleProperty().bind(RadioButton1.selectedProperty());
        BarChart2.visibleProperty().bind(RadioButton1.selectedProperty());
        BarChart3.visibleProperty().bind(RadioButton1.selectedProperty());
        LineChart1.visibleProperty().bind(RadioButton6.selectedProperty());
        LineChart2.visibleProperty().bind(RadioButton6.selectedProperty());
        LineChart3.visibleProperty().bind(RadioButton6.selectedProperty());
        HBox1.visibleProperty().bind(RadioButton1.selectedProperty());
        HBox4.visibleProperty().bind(RadioButton6.selectedProperty());
        HBox5.visibleProperty().bind(RadioButton1.selectedProperty());
        HBox6.visibleProperty().bind(RadioButton6.selectedProperty());
        HBox7.visibleProperty().bind(RadioButton1.selectedProperty());
        HBox8.visibleProperty().bind(RadioButton6.selectedProperty());
    }
    
    private void constructCheckBoxes() {
        
        checkBoxes = new CheckBox[yearFilterBar.size()];

        for (byte index = 0; index < yearFilterBar.size(); index++) {
            yearFilterBar.toString();
            checkBoxes[index] = new CheckBox(yearFilterBar.get(index));
            checkBoxes[index].setSelected(true);
            checkBoxes[index].addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    System.out.println("Firstly, Event Filters !");
                }
            });
            checkBoxes[index].addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    System.out.println("Secondly, Event Handlers !");
                }
            });
            checkBoxes[index].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {                    
                    System.out.println("Thirdly, Convenience Methods !");

                    constructSeries();
                }
            });

            HBox1.getChildren().add(checkBoxes[index]);
        }  
        AnchorPane1.getScene().getWindow().sizeToScene();
        constructSeries();
    }
    
    private void constructSeries() {
        BarChart1.getData().clear();
        
        for (CheckBox checkBox : checkBoxes) {
            if (checkBox.isSelected()) {
                XYChart.Series series = new XYChart.Series();
                series.setName(checkBox.getText());
                
                for (CarSales carsale : carSales) {
                    Map<String, Integer> years = carSales.stream()
                    .filter(o -> o.getYear().equals(checkBox.getText()))
                        .collect(Collectors.groupingBy(CarSales::getRegion, Collectors.reducing(0, CarSales::getQuantity, Integer::sum)));

                    for (Map.Entry<String, Integer> year : years.entrySet()) {
                        System.out.println(year.getKey() + ":" + year.getValue());
                        series.getData().add(new XYChart.Data<>(year.getKey(), year.getValue()));
                    }
                }
                BarChart1.getData().add(series);
            }
        }
    }
    
    private void constructCheckBoxes2() {
        checkBoxes2 = new CheckBox[yearFilterLine.size()];

        for (byte index = 0; index < yearFilterLine.size(); index++) {
            yearFilterLine.toString();
            checkBoxes2[index] = new CheckBox(yearFilterLine.get(index));
            checkBoxes2[index].setSelected(true);
            checkBoxes2[index].addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    System.out.println("Firstly, Event Filters !");
                }
            });
            checkBoxes2[index].addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    System.out.println("Secondly, Event Handlers !");
                }
            });
            checkBoxes2[index].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {                    
                    System.out.println("Thirdly, Convenience Methods !");

                    constructSeries2();
                }
            });

            HBox4.getChildren().add(checkBoxes2[index]);
        }

        AnchorPane1.getScene().getWindow().sizeToScene();
        constructSeries2();
    }
    
    private void constructSeries2() {
        LineChart1.getData().clear();
        
        for (CheckBox checkBox2 : checkBoxes2) {
            if (checkBox2.isSelected()) {
                XYChart.Series series = new XYChart.Series();
                series.setName(checkBox2.getText());
                
                for (CarSales carsale : carSales) {
                    Map<String, Integer> years = carSales.stream()
                    .filter(o -> o.getYear().equals(checkBox2.getText()))
                        .collect(Collectors.groupingBy(CarSales::getRegion, Collectors.reducing(0, CarSales::getQuantity, Integer::sum)));

                    for (Map.Entry<String, Integer> year : years.entrySet()) {
                        System.out.println(year.getKey() + ":" + year.getValue());
                        series.getData().add(new XYChart.Data<>(year.getKey(), year.getValue()));
                    }
                }
                LineChart1.getData().add(series);
            }
        }
    }
    
    private void constructCheckBoxes3() {
        
        checkBoxes3 = new CheckBox[vehicleFilterBar.size()];

        for (byte index = 0; index < vehicleFilterBar.size(); index++) {
            vehicleFilterBar.toString();
            checkBoxes3[index] = new CheckBox(vehicleFilterBar.get(index));
            checkBoxes3[index].setSelected(true);
            checkBoxes3[index].addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    System.out.println("Firstly, Event Filters !");
                }
            });
            checkBoxes3[index].addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    System.out.println("Secondly, Event Handlers !");
                }
            });
            checkBoxes3[index].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {                    
                    System.out.println("Thirdly, Convenience Methods !");

                    constructSeries3();
                }
            });

            HBox5.getChildren().add(checkBoxes3[index]);
        }  
        AnchorPane1.getScene().getWindow().sizeToScene();
        constructSeries3();
    }
    
    
    private void constructSeries3() {
        BarChart2.getData().clear();
        
        for (CheckBox checkBox3 : checkBoxes3) {
            if (checkBox3.isSelected()) {
                XYChart.Series series = new XYChart.Series();
                series.setName(checkBox3.getText());
                
                for (CarSales carsale : carSales) {
                    Map<String, Integer> vehicles = carSales.stream()
                    .filter(o -> o.getVehicle().equals(checkBox3.getText()))
                        .collect(Collectors.groupingBy(CarSales::getRegion, Collectors.reducing(0, CarSales::getQuantity, Integer::sum)));

                    for (Map.Entry<String, Integer> vehicle : vehicles.entrySet()) {
                        System.out.println(vehicle.getKey() + ":" + vehicle.getValue());
                        series.getData().add(new XYChart.Data<>(vehicle.getKey(), vehicle.getValue()));
                    }
                }
                BarChart2.getData().add(series);
            }
        }
    }
    
    private void constructCheckBoxes4() {
        checkBoxes4 = new CheckBox[vehicleFilterLine.size()];

        for (byte index = 0; index < vehicleFilterLine.size(); index++) {
            vehicleFilterLine.toString();
            checkBoxes4[index] = new CheckBox(vehicleFilterLine.get(index));
            checkBoxes4[index].setSelected(true);
            checkBoxes4[index].addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    System.out.println("Firstly, Event Filters !");
                }
            });
            checkBoxes4[index].addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    System.out.println("Secondly, Event Handlers !");
                }
            });
            checkBoxes4[index].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {                    
                    System.out.println("Thirdly, Convenience Methods !");

                    constructSeries4();
                }
            });

            HBox6.getChildren().add(checkBoxes4[index]);
        }

        AnchorPane1.getScene().getWindow().sizeToScene();
        constructSeries4();
    }
    
    private void constructSeries4() {
        LineChart2.getData().clear();
        
        for (CheckBox checkBox4 : checkBoxes4) {
            if (checkBox4.isSelected()) {
                XYChart.Series series = new XYChart.Series();
                series.setName(checkBox4.getText());
                
                for (CarSales carsale : carSales) {
                    Map<String, Integer> vehicles = carSales.stream()
                    .filter(o -> o.getVehicle().equals(checkBox4.getText()))
                        .collect(Collectors.groupingBy(CarSales::getRegion, Collectors.reducing(0, CarSales::getQuantity, Integer::sum)));

                    for (Map.Entry<String, Integer> vehicle : vehicles.entrySet()) {
                        System.out.println(vehicle.getKey() + ":" + vehicle.getValue());
                        series.getData().add(new XYChart.Data<>(vehicle.getKey(), vehicle.getValue()));
                    }
                }
                LineChart2.getData().add(series);
            }
        }
    }
    
    private void constructCheckBoxes5() {
        
        checkBoxes5 = new CheckBox[regionFilterBar.size()];

        for (byte index = 0; index < regionFilterBar.size(); index++) {
            regionFilterBar.toString();
            checkBoxes5[index] = new CheckBox(regionFilterBar.get(index));
            checkBoxes5[index].setSelected(true);
            checkBoxes5[index].addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    System.out.println("Firstly, Event Filters !");
                }
            });
            checkBoxes5[index].addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    System.out.println("Secondly, Event Handlers !");
                }
            });
            checkBoxes5[index].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {                    
                    System.out.println("Thirdly, Convenience Methods !");

                    constructSeries5();
                }
            });

            HBox7.getChildren().add(checkBoxes5[index]);
        }  
        AnchorPane1.getScene().getWindow().sizeToScene();
        constructSeries5();
    }
    
    private void constructSeries5() {
        BarChart3.getData().clear();
        
        for (CheckBox checkBox5 : checkBoxes5) {
            if (checkBox5.isSelected()) {
                XYChart.Series series = new XYChart.Series();
                series.setName(checkBox5.getText());
                
                for (CarSales carsale : carSales) {
                    Map<String, Integer> regions = carSales.stream()
                    .filter(o -> o.getRegion().equals(checkBox5.getText()))
                        .collect(Collectors.groupingBy(CarSales::getVehicle, Collectors.reducing(0, CarSales::getQuantity, Integer::sum)));

                    for (Map.Entry<String, Integer> region : regions.entrySet()) {
                        System.out.println(region.getKey() + ":" + region.getValue());
                        series.getData().add(new XYChart.Data<>(region.getKey(), region.getValue()));
                    }
                }
                BarChart3.getData().add(series);
            }
        }
    }
    
    private void constructCheckBoxes6() {
        checkBoxes6 = new CheckBox[regionFilterLine.size()];

        for (byte index = 0; index < regionFilterLine.size(); index++) {
            regionFilterLine.toString();
            checkBoxes6[index] = new CheckBox(regionFilterLine.get(index));
            checkBoxes6[index].setSelected(true);
            checkBoxes6[index].addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    System.out.println("Firstly, Event Filters !");
                }
            });
            checkBoxes6[index].addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    System.out.println("Secondly, Event Handlers !");
                }
            });
            checkBoxes6[index].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {                    
                    System.out.println("Thirdly, Convenience Methods !");

                    constructSeries6();
                }
            });

            HBox8.getChildren().add(checkBoxes6[index]);
        }
        AnchorPane1.getScene().getWindow().sizeToScene();
        constructSeries6();
    }
    
    private void constructSeries6() {
        LineChart3.getData().clear();
        
        for (CheckBox checkBox6 : checkBoxes6) {
            if (checkBox6.isSelected()) {
                XYChart.Series series = new XYChart.Series();
                series.setName(checkBox6.getText());
                
                for (CarSales carsale : carSales) {
                    Map<String, Integer> regions = carSales.stream()
                    .filter(o -> o.getRegion().equals(checkBox6.getText()))
                        .collect(Collectors.groupingBy(CarSales::getVehicle, Collectors.reducing(0, CarSales::getQuantity, Integer::sum)));

                    for (Map.Entry<String, Integer> region : regions.entrySet()) {
                        System.out.println(region.getKey() + ":" + region.getValue());
                        series.getData().add(new XYChart.Data<>(region.getKey(), region.getValue()));
                    }
                }
                LineChart3.getData().add(series);
            }
        }
    }
    
    private static class DashService extends Service<String> {
        private StringProperty address = new SimpleStringProperty();

        public final void setAddress(String address) {
            this.address.set(address);
        }

        public final String getAddress() {
            return address.get();
        }

        public final StringProperty addressProperty() {
            return address;
        }

        @Override
        protected Task<String> createTask() {
        return new Task<String>() {
                private URL url;
                private HttpURLConnection connect;
                private String markup = "";

                @Override
                protected String call() {
                    try {
                        url = new URL(getAddress());
                        connect = (HttpURLConnection)url.openConnection();
                        connect.setRequestMethod("GET");
                        connect.setRequestProperty("Accept", "application/json");
                        connect.setRequestProperty("Content-Type", "application/json");                        

                        markup = (new BufferedReader(new InputStreamReader(connect.getInputStream()))).readLine();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    finally {
                        if (connect != null) {
                            connect.disconnect();
                        }
                    }
                    return markup;
                }
            };
        }
    }
}
