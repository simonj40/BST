package application;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.fauberteau.rbtree.BinarySearchTree;
import org.fauberteau.rbtree.RandomWordGenerator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class BenchmarkController {

	@FXML
	private TextField nodesNumber;
	@FXML
	private TextField threadsNumber;
	@FXML
	private ProgressBar progressBar;
	@FXML
	private BorderPane pane;
	@FXML
	private AnchorPane apane;

	@FXML
	private LineChart<Number, Number> lineChart;

	private ObservableList<XYChart.Data<Number, Number>> data;
	
	private List<XYChart.Data<Number, Number>> benchmarkData = new ArrayList<>();

	private NumberAxis x = new NumberAxis(1, 4, 1);
	private NumberAxis y = new NumberAxis(0,10, 0.1);

	private final static int MAX_THREADS = 2000;

	private final static ExecutorService service = Executors
			.newFixedThreadPool(MAX_THREADS);

	private BinarySearchTree<String> tree = new BinarySearchTree<>();

	@FXML
	public void initialize() {

		data = FXCollections.observableArrayList();
		
		ObservableList<Series<Number, Number>> series = FXCollections
				.observableArrayList(new XYChart.Series<>(data));
		
		x.autoRangingProperty().set(true);
		y.autoRangingProperty().set(true);
		x.setLabel("Threads");
		y.setLabel("Nanoseconds");
		
		lineChart = new LineChart<>(x, y, series);
		lineChart.setTitle("Average addition time according to the number of threads");
		lineChart.setLegendVisible(false);
		pane.setCenter(lineChart);

	}

	@FXML
	private void launchAction() {

		try {
			
			int threadsNumber = getThreadsField();
			int NodesNumber = getNodesField();
			 
			BenchMark bench  = new BenchMark(threadsNumber, NodesNumber);
			setBenchmarkListener(bench);
			(new Thread(bench)).start();
			
			
		} catch (NumberFormatException e) {
			FieldAlert();
		}

	}
	
	private void setBenchmarkListener(BenchMark bench){
		
		bench.progressProperty().addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<? extends Object> observable,
					Object oldValue, Object newValue) {
				
				progressBar.setProgress((double)newValue);
				if( (double)newValue == 1 ){
					drawChart();
					progressBar.setProgress(0);
				}
			}
			
		});
		
	}

	public void drawChart() {
		data.clear();		
		for(Data d : benchmarkData){
			data.add(d);
		}
		
	}

	private int getNodesField() throws NumberFormatException {
		return Integer.parseInt(nodesNumber.getText());
	}

	private int getThreadsField() throws NumberFormatException {
		int threads = Integer.parseInt(threadsNumber.getText());
		if (threads > MAX_THREADS-500){
			throw new  NumberFormatException();
		}else return threads;
	}

	public void FieldAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Invalid Input");
		alert.setHeaderText(null);
		alert.setContentText("invalid input ! \nPlease enter integers...");
		alert.showAndWait();
	}

	private class BenchMark extends Task<Object> {

		int threadsNumber;
		int NodesNumber;

		public BenchMark(int threadsNumber, int NodesNumber) {
			this.threadsNumber = threadsNumber;
			this.NodesNumber = NodesNumber;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javafx.concurrent.Task#call()
		 */
		@Override
		protected Object call() throws Exception {
			benchmarkData.clear();
			
			
			ArrayList<Future<Duration>> futureList = new ArrayList<>();
			
			for (int i = 1; i < threadsNumber+1; i++) {
				
				for (int j = 0; j < i; j++){
					
					Future<Duration> future = service.submit(new BSTAdder(new RandomWordGenerator(
							NodesNumber/i), tree));
					futureList.add(future);
				}
				
				List<Duration> times = new ArrayList<Duration>();
				
				for (Future<Duration> future : futureList) {
					try {
						times.add( future.get() );
					} catch (InterruptedException | ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				updateProgress(i,threadsNumber);

				OptionalDouble avg = times.stream().mapToLong(b -> b.toNanos())
						.average();
				benchmarkData.add(new Data<Number, Number>( i , avg.getAsDouble()));

				//Clear the tree !
				tree = new BinarySearchTree<>();
			}

			return null;
		}

	}

}
