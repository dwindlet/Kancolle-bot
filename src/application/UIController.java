package application;


import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
//import java.nio.charset.Charset;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;


/**import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;


import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.compression.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;*/

import com.sun.jna.platform.win32.WinDef.HWND;
import org.sikuli.script.*;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

public class UIController{
	@FXML
	private Button handleset;
	@FXML
	private Button updatesetting,clearlog,test,runtest;
	@FXML
	private Button startone, starttwo, startthree;
	@FXML
	private Screen s = new Screen();
	@FXML
	private ArrayList<String> timehour = new ArrayList<String>();
	@FXML
	private ArrayList<String> timeminute = new ArrayList<String>();
	@FXML
	private ArrayList<String> expeditionnum = new ArrayList<String>();
	@FXML
	private ComboBox<String> hourone,hourtwo,hourthree,minuteone,minutetwo,minutethree,secondone,
	secondtwo,secondthree,statusone,statustwo,statusthree,numone,numtwo,numthree;
	@FXML
	private CheckBox fleet2,fleet3,fleet4,savelog,notifysoundcheck,refreshpagecheck,exp3rounds,exp10rounds,exp30rounds,exptokyotrip1rounds,exptokyotrip7rounds;
	@FXML
	private Label assisthandle,handlelabel,workingstatus,countone,counttwo,countthree,notifytimelabel;
	@FXML
	private TextField clickoffsetfield,expoffsetfield,clickoffsetrand,expoffsetrand,waitfortargett,notifytimefield,exp3progress,exp10progress,exp30progress,exptokyo1progress,exptokyo7progress;
	@FXML
	private TextArea logarea;
	@FXML
	private ImageView titleimg;
	@FXML
    private ArrayList<CheckBox> QuestCheckBoxList,QuestAcceptList;
	@SuppressWarnings("rawtypes")
	@FXML
    private ArrayList<ComboBox> SelectedComboBoxList;
	@FXML
	private ArrayList<TextField> SettingTextFieldList, QuestProgressList;
	@FXML 
	private javafx.scene.shape.Rectangle dpirect;
	
	final double rem = javafx.scene.text.Font.getDefault().getSize();
	private SecureRandom sr = new SecureRandom();
	private double timeperscan = 0.5;
	private int fleet2startstatus = 0,fleet3startstatus = 0,fleet4startstatus = 0;
	private String fleet2currentexp = "",fleet3currentexp = "",fleet4currentexp = "";
	private int[] expaccept = new int[5];
	private int page = 1;
	private HWND hwnd = null;
	private HWND hwndclick = null;
	private HWND assisthwnd = null;
	private volatile int statrunning = 0;
	private int firstrunofaday = 0;
	private int questInfoNotGet = 0;
	private int fleet2ExpCountChangable = 1,fleet3ExpCountChangable = 1,fleet4ExpCountChangable = 1;
	private volatile int interrupted = 0;
	private int notifysoundtime = 30;
	private double waitfortargettimeout = 10.0;
	private double clickrand = 3.0;
	private double fixedclickoffset = 0.3;
	private double clickoffsetrandtime = 0.0;
	private double clickoffsettime = ((((clickoffsetrandtime = sr.nextDouble())>clickrand)?clickrand:clickoffsetrandtime)+fixedclickoffset);
	private int exprand = 15,fixedexpoffset = -60;
	private int expoffsettime = (sr.nextInt(exprand+1)+fixedexpoffset);
	private Timeline t1,t2,t3;
	private MediaPlayer mp;
	private BufferedWriter logwriter;
	private Service<Void> service;
	private int EXP[] ={0,900,1800,1200,3000,5400,2400,3600,10800,
			14400,5400,18000,28800,14400,21600,43200,54000,
			2700,18000,21600,7200,8400,10800,14400,30000,
			144000,288000,72000,90000,86400,172800,7200,86400,
			0,0,25200,32400,9900,10500,108000,24600,
			3600,28800,
			//A1,A2...           //B1,B2...
			1500,3300,8100,6600, 2100,31200};
	
	@FXML
    protected void initialize() {
		int changeday = 0;
		int changeweek = 0;
    	for(int i = 0;i<=99;i++){
    		timehour.add(String.valueOf(i));
    	}
    	for(int i = 0;i<=59;i++){
    		timeminute.add(String.valueOf(i));
    	}
    	expeditionnum.add("none");
    	for(int i = 1;i<=42;i++){
    		expeditionnum.add(String.valueOf(i));
    	}
    	expeditionnum.add("A1");
    	expeditionnum.add("A2");
    	expeditionnum.add("A3");
    	expeditionnum.add("A4");
    	expeditionnum.add("B1");
    	expeditionnum.add("B2");
    	
    	hourone.setItems(FXCollections.observableArrayList(timehour));
    	hourtwo.setItems(FXCollections.observableArrayList(timehour));
       	hourthree.setItems(FXCollections.observableArrayList(timehour));
    	minuteone.setItems(FXCollections.observableArrayList(timeminute)); 	
    	minutetwo.setItems(FXCollections.observableArrayList(timeminute));    	
    	minutethree.setItems(FXCollections.observableArrayList(timeminute));   	
    	secondone.setItems(FXCollections.observableArrayList(timeminute));    	
    	secondtwo.setItems(FXCollections.observableArrayList(timeminute));    	
    	secondthree.setItems(FXCollections.observableArrayList(timeminute));    	
    	numone.setItems(FXCollections.observableArrayList(expeditionnum));    	
    	numtwo.setItems(FXCollections.observableArrayList(expeditionnum));    	
    	numthree.setItems(FXCollections.observableArrayList(expeditionnum));    	
    	statusone.setItems(FXCollections.observableArrayList("expeditioning","no expedition"));
    	statustwo.setItems(FXCollections.observableArrayList("expeditioning","no expedition"));
    	statusthree.setItems(FXCollections.observableArrayList("expeditioning","no expedition"));
    	dpirect.setWidth(rem);
    	dpirect.setHeight(rem);
    	for(int i = 0;i<expaccept.length;i++) {
    		expaccept[i] = 0;
    	}
    	
    	for(int i = 0;i<SelectedComboBoxList.size();i++) {
    		@SuppressWarnings("unchecked")
			ComboBox<String> cache = (ComboBox<String>)SelectedComboBoxList.get(i);
			if(i == 9|| i == 10 || i == 11) {
				cache.setOnScroll(r->{
		    		double deltaY = r.getDeltaY();
		    		if (deltaY < 0){
		    			if(cache.getValue().equals("none")) {
		    				
		    			}
		    			else if(cache.getItems().indexOf(cache.getValue()) > 40) {
		    				cache.setValue(cache.getItems().get(cache.getItems().indexOf(cache.getValue())-1));
		    			}
		    			else if(Integer.parseInt(cache.getValue()) > 0) {
		    				if(Integer.parseInt(cache.getValue()) == 1){
		    					 Platform.runLater(new Runnable() {                          
				     					@Override
				     					public void run() {
				     						try{
				     							cache.setValue("none");
				     							}finally{
				     							}
				     					}
				     				});	
		    				}
		    				else {
		    					 Platform.runLater(new Runnable() {                          
				     					@Override
				     					public void run() {
				     						try{
				     							cache.setValue(String.valueOf(Integer.parseInt(cache.getValue())-1));
				     							}finally{
				     							}
				     					}
				     				});	
		    				}
		    				
		    			}
		    		}
		    		else {
		    			if(cache.getValue().equals("none")) {
		    				Platform.runLater(new Runnable() {                          
		     					@Override
		     					public void run() {
		     						try{
		     							cache.setValue(String.valueOf("1"));
		     							}finally{
		     							}
		     					}
		     				});	
		    			}
		    			else if(cache.getItems().indexOf(cache.getValue()) >= 40 && cache.getItems().indexOf(cache.getValue()) < cache.getItems().size()-1) {
		    				Platform.runLater(new Runnable() {                          
		     					@Override
		     					public void run() {
		     						try{
		     							cache.setValue(cache.getItems().get(cache.getItems().indexOf(cache.getValue())+1));
		     							}finally{
		     							}
		     					}
		     				});	
		    				
		    			}
		    			else if(cache.getItems().indexOf(cache.getValue()) < 40 && Integer.parseInt(cache.getValue()) < cache.getItems().size()-1) {
		    				Platform.runLater(new Runnable() {                          
		     					@Override
		     					public void run() {
		     						try{
		     							cache.setValue(String.valueOf(Integer.parseInt(cache.getValue())+1));
		     							}finally{
		     							}
		     					}
		     				});	
		    			}
		    		}
		    	});
			}
			else {
		    	cache.setOnScroll(r->{
		    		double deltaY = r.getDeltaY();
		    		if (deltaY < 0){
		    			if(Integer.parseInt(cache.getValue()) > 0) {
		    				 Platform.runLater(new Runnable() {                          
		     					@Override
		     					public void run() {
		     						try{
		     							cache.setValue(String.valueOf(Integer.parseInt(cache.getValue())-1));
		     							}finally{
		     							}
		     					}
		     				});	
		    			}
		    		}
		    		else {
		    			if(Integer.parseInt(cache.getValue()) < cache.getItems().size()-1) {
		    				Platform.runLater(new Runnable() {                          
		     					@Override
		     					public void run() {
		     						try{
		     							cache.setValue(String.valueOf(Integer.parseInt(cache.getValue())+1));
		     							}finally{
		     							}
		     					}
		     				});	
		    			}
		    		}
		    	});
			}
    	}
    	for(int i = 0;i<SelectedComboBoxList.size();i++) {
    		@SuppressWarnings("unchecked")
			ComboBox<String> cache = (ComboBox<String>)SelectedComboBoxList.get(i);
    		cache.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
          	  if (isNowFocused) {
          		  Platform.runLater(new Runnable() {                          
    					@Override
    					public void run() {
    						try{
    							cache.getEditor().selectAll();
    							}finally{
    							}
    					}
    				});
          	  }
          });
    	}
    	
    	for(int i = 0;i<SettingTextFieldList.size();i++) {
			TextField cache = (TextField)SettingTextFieldList.get(i);
    		cache.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
          	  if (isNowFocused) {
          		  Platform.runLater(new Runnable() {                          
    					@Override
    					public void run() {
    						try{
    							cache.selectAll();
    							}finally{
    							}
    					}
    				});
          	  }
          });
    	}
    	notifysoundcheck.selectedProperty().addListener((observable, oldValue, newValue) -> {
        	if(newValue == true){
        		notifytimelabel.setDisable(false);
        		notifytimefield.setDisable(false);
        	}
        	else{
        		notifytimelabel.setDisable(true);
        		notifytimefield.setDisable(true);
        	}
        });
    	notifytimefield.textProperty().addListener((observable, oldValue, newValue) -> {
        	notifysoundtime = Integer.parseInt(newValue);
        });
        clickoffsetfield.textProperty().addListener((observable, oldValue, newValue) -> {
        	fixedclickoffset = Double.parseDouble(newValue);
        	clickoffsettime = ((((clickoffsetrandtime = sr.nextDouble())>clickrand)?clickrand:clickoffsetrandtime)+fixedclickoffset);
        });
        expoffsetfield.textProperty().addListener((observable, oldValue, newValue) -> {
        	fixedexpoffset = Integer.parseInt(newValue);
        	expoffsettime = (sr.nextInt(exprand+1)+fixedexpoffset);
        });
        clickoffsetrand.textProperty().addListener((observable, oldValue, newValue) -> {
        	clickrand = Double.parseDouble(newValue);
        	clickoffsettime = ((((clickoffsetrandtime = sr.nextDouble())>clickrand)?clickrand:clickoffsetrandtime)+fixedclickoffset);
        });
        expoffsetrand.textProperty().addListener((observable, oldValue, newValue) -> {
        	exprand = Integer.parseInt(newValue);
        	expoffsettime = (sr.nextInt(exprand+1)+fixedexpoffset);
        });
        waitfortargett.textProperty().addListener((observable, oldValue, newValue) -> {
        	waitfortargettimeout = Double.parseDouble(newValue);
        });

    	File missionfile = new File("mission.ini");
    	if(missionfile.exists() == false){
    		try {
    			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("mission.ini"), "UTF-8"));
    			
    			writer.write("Last Updated:" + LocalDateTime.now());
    			writer.newLine();
	    		writer.write("questexp3round=0");
	    		writer.newLine();
	    		writer.write("questexp10round=0");
	    		writer.newLine();
	    		writer.write("questexp30round=0");
	    		writer.newLine();
	    		writer.write("exptokyotrip1rounds=0");
	    		writer.newLine();
	    		writer.write("exptokyotrip7rounds=0");
	    		writer.newLine();
	    		writer.write("questexp3roundaccept=" + expaccept[0]);
	    		writer.newLine();
	    		writer.write("questexp10roundaccept=" + expaccept[1]);
	    		writer.newLine();
	    		writer.write("questexp30roundaccept=" + expaccept[2]);
	    		writer.newLine();
	    		writer.write("exptokyotrip1roundsaccept=" + expaccept[3]);
	    		writer.newLine();
	    		writer.write("exptokyotrip7roundsaccept=" + expaccept[4]);
	    		writer.newLine();

	    		writer.close(); 
	    		for(int i = 0;i<5;i++) {
	    			QuestProgressList.get(i).setText("0");
	    		}
	    		
	    		Platform.runLater(new Runnable() {                          
					@Override
					public void run() {
						LocalDateTime dateTime = LocalDateTime.now();
						try{
							logarea.appendText(String.format("%s: File: Generate initial mission progress file 'mission.ini' success!.\n",datetime(dateTime)
					        ));
							}finally{
								if(savelog.isSelected() == true){
									try {
										logwriter.write(String.format("%s: File: Generate initial mission progress file 'mission.ini' success!.\n",datetime(dateTime)
								        ));
										logwriter.newLine();
										logwriter.flush();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
					}
				});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

    	}
    	else{
    		try {
    			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("mission.ini"), "UTF-8")); 

    			String str = null;
    			str = reader.readLine();
    			LocalDateTime lastupdatedtimelocal = LocalDateTime.parse(str.substring(13, str.length()));
    			ZonedDateTime lastupdatedtime = lastupdatedtimelocal.atZone(ZoneId.systemDefault());
    			lastupdatedtime = lastupdatedtime.withZoneSameInstant(ZoneId.of("GMT+08:00"));
    			ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of("GMT+08:00"));
//    			System.out.println(dateTime.toString());
//    			System.out.println(dateTime.getHour());
    			WeekFields weekFields = WeekFields.ISO;
    			ZonedDateTime lastupdatedyesterday = lastupdatedtime.minusDays(1);
    			int lastupdatedyesterdayweekNumber = lastupdatedyesterday.get(weekFields.weekOfWeekBasedYear());
    			//System.out.println(lastupdatedyesterdayweekNumber);
    			
    			int lastupdatedweekNumber = lastupdatedtime.get(weekFields.weekOfWeekBasedYear());
    			//System.out.println(lastupdatedweekNumber);
    			int weekNumber = dateTime.get(weekFields.weekOfWeekBasedYear());
    			//System.out.println(weekNumber);
    			str = reader.readLine();
    			exp3progress.setText(str.substring(str.lastIndexOf("=")+1, str.length()));
    			str = reader.readLine();
    			exp10progress.setText(str.substring(str.lastIndexOf("=")+1, str.length()));
    			str = reader.readLine();
    			exp30progress.setText(str.substring(str.lastIndexOf("=")+1, str.length()));
    			str = reader.readLine();
    			exptokyo1progress.setText(str.substring(str.lastIndexOf("=")+1, str.length()));
    			str = reader.readLine();
    			exptokyo7progress.setText(str.substring(str.lastIndexOf("=")+1, str.length()));
    			str = reader.readLine();
    			expaccept[0] = Integer.valueOf(str.substring(str.lastIndexOf("=")+1, str.length()));
    			str = reader.readLine();
    			expaccept[1] = Integer.valueOf(str.substring(str.lastIndexOf("=")+1, str.length()));
    			str = reader.readLine();
    			expaccept[2] = Integer.valueOf(str.substring(str.lastIndexOf("=")+1, str.length()));
    			str = reader.readLine();
    			expaccept[3] = Integer.valueOf(str.substring(str.lastIndexOf("=")+1, str.length()));
    			str = reader.readLine();
    			expaccept[4] = Integer.valueOf(str.substring(str.lastIndexOf("=")+1, str.length()));
    			
    			reader.close();
    			if((dateTime.getDayOfYear()==lastupdatedtime.getDayOfYear() && lastupdatedtime.getHour()>=0 && lastupdatedtime.isBefore(ZonedDateTime.of(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), 4, 0, 0, 0, ZoneId.of("GMT+08:00"))) && dateTime.isAfter(ZonedDateTime.of(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), 4, 0, 0, 0, ZoneId.of("GMT+08:00"))))||
    					(dateTime.getDayOfYear()>lastupdatedtime.getDayOfYear() && dateTime.isAfter(ZonedDateTime.of(lastupdatedtime.getYear(), lastupdatedtime.getMonthValue(), lastupdatedtime.getDayOfMonth(), 4, 0, 0, 0, ZoneId.of("GMT+08:00")).plusDays(1)))||
    					(dateTime.getYear()>lastupdatedtime.getYear() && dateTime.isAfter(ZonedDateTime.of(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), 4, 0, 0, 0, ZoneId.of("GMT+08:00"))))) {
    				firstrunofaday = 1;
    				changeday = 1;
    				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("mission.ini"), "UTF-8"));
    				LocalDateTime dateTimelocal = LocalDateTime.now();
        			
        			writer.write("Last Updated:" + dateTimelocal);
        			writer.newLine();
    	    		writer.write("questexp3round=0");
    	    		writer.newLine();
    	    		writer.write("questexp10round=0");
    	    		writer.newLine();
    	    		writer.write("questexp30round=" + exp30progress.getText());
    	    		writer.newLine();
    	    		writer.write("exptokyotrip1rounds=" + exptokyo1progress.getText());
    	    		writer.newLine();
    	    		writer.write("exptokyotrip7rounds=" + exptokyo7progress.getText());
    	    		writer.newLine();
    	    		writer.write("questexp3roundaccept=0");
    	    		writer.newLine();
    	    		writer.write("questexp10roundaccept=0");
    	    		writer.newLine();
    	    		writer.write("questexp30roundaccept=" + expaccept[2]);
    	    		writer.newLine();
    	    		writer.write("exptokyotrip1roundsaccept=" + expaccept[3]);
    	    		writer.newLine();
    	    		writer.write("exptokyotrip7roundsaccept=" + expaccept[4]);
    	    		writer.newLine();

    	    		writer.close();
    	    		exp3progress.setText("0");
    	    		exp10progress.setText("0");
    	    		expaccept[0] = 0;
    	    		expaccept[1] = 0;
    	    		
    			}
    			dateTime = ZonedDateTime.now(ZoneId.of("GMT+08:00"));
    			if((weekNumber==lastupdatedweekNumber && lastupdatedyesterdayweekNumber<weekNumber && dateTime.isAfter(ZonedDateTime.of(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), 4, 0, 0, 0, ZoneId.of("GMT+08:00"))) && lastupdatedtime.getHour()>=0 && lastupdatedtime.getHour()<4)||
    					(weekNumber>lastupdatedweekNumber && dateTime.isAfter(ZonedDateTime.of(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), 4, 0, 0, 0, ZoneId.of("GMT+08:00")))) || 
    					(weekNumber<lastupdatedweekNumber && lastupdatedtime.isBefore(dateTime))) {
    				changeweek = 1;
    				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("mission.ini"), "UTF-8"));
    				LocalDateTime dateTimelocal = LocalDateTime.now();
        			
        			writer.write("Last Updated:" + dateTimelocal);
        			writer.newLine();
    	    		writer.write("questexp3round=" + exp3progress.getText());
    	    		writer.newLine();
    	    		writer.write("questexp10round=" + exp10progress.getText());
    	    		writer.newLine();
    	    		writer.write("questexp30round=0");
    	    		writer.newLine();
    	    		writer.write("exptokyotrip1rounds=0");
    	    		writer.newLine();
    	    		writer.write("exptokyotrip7rounds=0");
    	    		writer.newLine();
    	    		writer.write("questexp3roundaccept=" + expaccept[0]);
    	    		writer.newLine();
    	    		writer.write("questexp10roundaccept=" + expaccept[1]);
    	    		writer.newLine();
    	    		writer.write("questexp30roundaccept=0");
    	    		writer.newLine();
    	    		writer.write("exptokyotrip1roundsaccept=0");
    	    		writer.newLine();
    	    		writer.write("exptokyotrip7roundsaccept=0");
    	    		writer.newLine();

    	    		writer.close();
    	    		exp30progress.setText("0");
    	    		exptokyo1progress.setText("0");
    	    		exptokyo7progress.setText("0");
    	    		expaccept[2] = 0;
    	    		expaccept[3] = 0;
    	    		expaccept[4] = 0;
    			}
    			for(int i = 0;i<expaccept.length;i++) {
    				if(expaccept[i] == 1) {
    					QuestAcceptList.get(i).setSelected(true);
    				}
    				else{
    					QuestAcceptList.get(i).setSelected(false);
    				}
    			}
    			

    			Platform.runLater(new Runnable() {                          
    				@Override
    				public void run() {
    					LocalDateTime dateTime = LocalDateTime.now();
    					try{
    						logarea.appendText(String.format("%s: File: Load from mission progress file 'mission.ini' success!.\n",datetime(dateTime)
    		    	        ));
    						}finally{
								if(savelog.isSelected() == true){
									try {
										logwriter.write(String.format("%s: File: Load from mission progress file 'mission.ini' success!.\n",datetime(dateTime)
			    		    	        ));
										logwriter.newLine();
										logwriter.flush();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
    						}
    				}
    			});
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	for(int i = 0;i<QuestAcceptList.size();i++) {
    		CheckBox cache = (CheckBox)QuestAcceptList.get(i);
    		int numcache = i;
    		cache.selectedProperty().addListener((observable, oldValue, newValue) -> {
            	if(newValue == true){
            		expaccept[numcache] = 1;
            		try {
            			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("mission.ini"), "UTF-8")); 
            			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("missiontemp.ini"), "UTF-8"));
    				  String line;
    				  while ((line = br.readLine()) != null) {
    				    if (line.contains("questexp3roundaccept")){
    					        line = line.replace(line.substring(line.lastIndexOf("=")+1, line.length()),String.valueOf(expaccept[0]));
    					        bw.write(line);
    					        bw.newLine();
    				    }
    				    else if(line.contains("questexp10roundaccept")){
    				    	 line = line.replace(line.substring(line.lastIndexOf("=")+1, line.length()),String.valueOf(expaccept[1]));
    					        bw.write(line);
    					        bw.newLine();
    				    }
    				    else if(line.contains("questexp30roundaccept")){
    				    	 line = line.replace(line.substring(line.lastIndexOf("=")+1, line.length()),String.valueOf(expaccept[2]));
    					        bw.write(line);
    					        bw.newLine();
    				    }
    				    else if(line.contains("exptokyotrip1roundsaccept")){
    				    	 line = line.replace(line.substring(line.lastIndexOf("=")+1, line.length()),String.valueOf(expaccept[3]));
    					        bw.write(line);
    					        bw.newLine();
    				    }
    				    else if(line.contains("exptokyotrip7roundsaccept")){
    				    	 line = line.replace(line.substring(line.lastIndexOf("=")+1, line.length()),String.valueOf(expaccept[4]));
    					        bw.write(line);
    					        bw.newLine();
    				    }
    				    else {
    				    	bw.write(line);
    				    	bw.newLine();
    				    }
    				  }
    				  bw.close();
    				  try {
      				    if(br != null){
      				      br.close();
      				    }
      				  } catch (IOException e) {
      				  }
    				}
    				catch (Exception e) {
    				  return;
    				} finally {
    				}
    				// Once everything is complete, delete old file..
            		File oldFile = new File("mission.ini");
    				oldFile.delete();

    				// And rename tmp file's name to old file name
    				File newFile = new File("missiontemp.ini");
    				newFile.renameTo(oldFile);
            	}
            	else{
            		expaccept[numcache] = 0;
            		try {
            			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("mission.ini"), "UTF-8")); 
            			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("missiontemp.ini"), "UTF-8"));
    				  String line;
    				  while ((line = br.readLine()) != null) {
    				    if (line.contains("questexp3roundaccept")){
    					        line = line.replace(line.substring(line.lastIndexOf("=")+1, line.length()),String.valueOf(expaccept[0]));
    					        bw.write(line);
    					        bw.newLine();
    				    }
    				    else if(line.contains("questexp10roundaccept")){
    				    	 line = line.replace(line.substring(line.lastIndexOf("=")+1, line.length()),String.valueOf(expaccept[1]));
    					        bw.write(line);
    					        bw.newLine();
    				    }
    				    else if(line.contains("questexp30roundaccept")){
    				    	 line = line.replace(line.substring(line.lastIndexOf("=")+1, line.length()),String.valueOf(expaccept[2]));
    					        bw.write(line);
    					        bw.newLine();
    				    }
    				    else if(line.contains("exptokyotrip1roundsaccept")){
    				    	 line = line.replace(line.substring(line.lastIndexOf("=")+1, line.length()),String.valueOf(expaccept[3]));
    					        bw.write(line);
    					        bw.newLine();
    				    }
    				    else if(line.contains("exptokyotrip7roundsaccept")){
    				    	 line = line.replace(line.substring(line.lastIndexOf("=")+1, line.length()),String.valueOf(expaccept[4]));
    					        bw.write(line);
    					        bw.newLine();
    				    }
    				    else {
    				    	bw.write(line);
    				    	bw.newLine();
    				    }
    				  }
    				  bw.close();
    				  try {
      				    if(br != null){
      				      br.close();
      				    }
      				  } catch (IOException e) {
      				  }
    				}
    				catch (Exception e) {
    				  return;
    				} finally {
    				}
    				// Once everything is complete, delete old file..
            		File oldFile = new File("mission.ini");
    				oldFile.delete();

    				// And rename tmp file's name to old file name
    				File newFile = new File("missiontemp.ini");
    				newFile.renameTo(oldFile);
            	}
            });
    	}
        
    	File settingfile = new File("setting.ini");
    	if(settingfile.exists() == false){
    		try {
    			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("setting.ini"), "UTF-8"));

				writer.write("fixedclickoffset=" + fixedclickoffset);
	    		writer.newLine();
				writer.write("clickrand=" + clickrand);
	    		writer.newLine();
				writer.write("fixedexpoffset=" + fixedexpoffset);
	    		writer.newLine();
				writer.write("exprand=" + exprand);
	    		writer.newLine();
	    		writer.write("waitfortargettimeout=" + waitfortargettimeout);
	    		writer.newLine();
	    		writer.write("notifysoundcheck=" + notifysoundcheck.isSelected());
	    		writer.newLine();
	    		writer.write("notifysoundtime=" + notifysoundtime);
	    		writer.newLine();
	    		writer.write("refreshpagecheck=" + refreshpagecheck.isSelected());
	    		writer.newLine();
	    		writer.write("questexp3round=" + exp3rounds.isSelected());
	    		writer.newLine();
	    		writer.write("questexp10round=" + exp10rounds.isSelected());
	    		writer.newLine();
	    		writer.write("questexp30round=" + exp30rounds.isSelected());
	    		writer.newLine();
	    		writer.write("exptokyotrip1rounds=" + exptokyotrip1rounds.isSelected());
	    		writer.newLine();
	    		writer.write("exptokyotrip7rounds=" + exptokyotrip1rounds.isSelected());
	    		writer.newLine();
	    		writer.write("exp1=" + numone.getValue());
	    		writer.newLine();
	    		writer.write("exp2=" + numtwo.getValue());
	    		writer.newLine();
	    		writer.write("exp3=" + numthree.getValue());
	    		writer.newLine();

	    		writer.close(); 
	    		Platform.runLater(new Runnable() {                          
					@Override
					public void run() {
						LocalDateTime dateTime = LocalDateTime.now();
						try{
							logarea.appendText(String.format("%s: File: Generate initial setting file 'setting.ini' success!.\n",datetime(dateTime)
					        ));
							}finally{
								if(savelog.isSelected() == true){
									try {
										logwriter.write(String.format("%s: File: Generate initial setting file 'setting.ini' success!.\n",datetime(dateTime)
								        ));
										logwriter.newLine();
										logwriter.flush();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
					}
				});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

    	}
    	else{
    		try {
    			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("setting.ini"), "UTF-8")); 

    			String str = null;
    			str = reader.readLine();
    			fixedclickoffset = Double.parseDouble(str.substring(str.lastIndexOf("=")+1, str.length()));
    			str = reader.readLine();
    			clickrand = Double.parseDouble(str.substring(str.lastIndexOf("=")+1, str.length()));
    			str = reader.readLine();
    			fixedexpoffset = Integer.parseInt(str.substring(str.lastIndexOf("=")+1, str.length()));
    			str = reader.readLine();
    			exprand = Integer.parseInt(str.substring(str.lastIndexOf("=")+1, str.length()));
    			str = reader.readLine();
    			waitfortargettimeout = Double.parseDouble(str.substring(str.lastIndexOf("=")+1, str.length()));
    			str = reader.readLine();
    			notifysoundcheck.setSelected(Boolean.valueOf(str.substring(str.lastIndexOf("=")+1, str.length())));
    			str = reader.readLine();
    			notifysoundtime = Integer.parseInt(str.substring(str.lastIndexOf("=")+1, str.length()));
    			str = reader.readLine();
    			refreshpagecheck.setSelected(Boolean.valueOf(str.substring(str.lastIndexOf("=")+1, str.length())));
    			str = reader.readLine();
    			exp3rounds.setSelected(Boolean.valueOf(str.substring(str.lastIndexOf("=")+1, str.length())));
    			str = reader.readLine();
    			exp10rounds.setSelected(Boolean.valueOf(str.substring(str.lastIndexOf("=")+1, str.length())));
    			str = reader.readLine();
    			exp30rounds.setSelected(Boolean.valueOf(str.substring(str.lastIndexOf("=")+1, str.length())));
    			str = reader.readLine();
    			exptokyotrip1rounds.setSelected(Boolean.valueOf(str.substring(str.lastIndexOf("=")+1, str.length())));
    			str = reader.readLine();
    			exptokyotrip7rounds.setSelected(Boolean.valueOf(str.substring(str.lastIndexOf("=")+1, str.length())));
    			str = reader.readLine();
    			numone.setValue(str.substring(str.lastIndexOf("=")+1, str.length()));
    			str = reader.readLine();
    			numtwo.setValue(str.substring(str.lastIndexOf("=")+1, str.length()));
    			str = reader.readLine();
    			numthree.setValue(str.substring(str.lastIndexOf("=")+1, str.length()));
    			
    			reader.close();
    			
    			if(changeday == 1) {
    				exp3rounds.setSelected(true);
    				exp10rounds.setSelected(true);
    			}
    			if(changeweek == 1) {
    				exp30rounds.setSelected(true);
    				exptokyotrip1rounds.setSelected(true);
    				exptokyotrip7rounds.setSelected(true);
    			}
    			fleet2currentexp = numone.getValue();
    			fleet3currentexp = numtwo.getValue();
    			fleet4currentexp = numthree.getValue();
    			
    			Platform.runLater(new Runnable() {                          
    				@Override
    				public void run() {
    					LocalDateTime dateTime = LocalDateTime.now();
    					try{
    						logarea.appendText(String.format("%s: File: Load from setting file 'setting.ini' success!.\n",datetime(dateTime)
    		    	        ));
    						}finally{
								if(savelog.isSelected() == true){
									try {
										logwriter.write(String.format("%s: File: Load from setting file 'setting.ini' success!.\n",datetime(dateTime)
			    		    	        ));
										logwriter.newLine();
										logwriter.flush();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
    						}
    				}
    			});
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    	}
    	
    	questInfoNotGet = 1;
    	
    	clickoffsetfield.setText(String.valueOf(fixedclickoffset));
    	clickoffsetrand.setText(String.valueOf(clickrand));
    	expoffsetfield.setText(String.valueOf(fixedexpoffset));
    	expoffsetrand.setText(String.valueOf(exprand));
    	waitfortargett.setText(String.valueOf(waitfortargettimeout));
    	notifytimefield.setText(String.valueOf(notifysoundtime));
    	javafx.scene.image.Image titlebar = new javafx.scene.image.Image(this.getClass().getResource("/res/titlebar.png").toString());
    	titleimg.setImage(titlebar);
    	String path = this.getClass().getResource("/res/Windows Notify System Generic.wav").toString();
        Media media = new Media(path);
        mp = new MediaPlayer(media);
        for(int i = 0;i<QuestProgressList.size();i++) {
			TextField cache = (TextField)QuestProgressList.get(i);
    		cache.textProperty().addListener((observable, oldValue, newValue) -> {
				try {
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("mission.ini"), "UTF-8"));
					
					writer.write("Last Updated:" + LocalDateTime.now());
	    			writer.newLine();
		    		writer.write("questexp3round=" + exp3progress.getText());
		    		writer.newLine();
		    		writer.write("questexp10round=" + exp10progress.getText());
		    		writer.newLine();
		    		writer.write("questexp30round=" + exp30progress.getText());
		    		writer.newLine();
		    		writer.write("exptokyotrip1rounds=" + exptokyo1progress.getText());
		    		writer.newLine();
		    		writer.write("exptokyotrip7rounds=" + exptokyo7progress.getText());
		    		writer.newLine();
		    		writer.write("questexp3roundaccept=" + expaccept[0]);
		    		writer.newLine();
		    		writer.write("questexp10roundaccept=" + expaccept[1]);
		    		writer.newLine();
		    		writer.write("questexp30roundaccept=" + expaccept[2]);
		    		writer.newLine();
		    		writer.write("exptokyotrip1roundsaccept=" + expaccept[3]);
		    		writer.newLine();
		    		writer.write("exptokyotrip7roundsaccept=" + expaccept[4]);
		    		writer.newLine();

		    		writer.close(); 
				} catch (UnsupportedEncodingException | FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			
    			
            });
    	}
        
        savelog.selectedProperty().addListener((observable, oldValue, newValue) -> {
        	if(newValue == true){
        		try {
        			LocalDateTime dateTime = LocalDateTime.now();
					logwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dateTime.getYear() + "-" + dateTime.getMonthValue() + "-" + dateTime.getDayOfMonth()  
	    	        + "_" + dateTime.getHour() + "-" + dateTime.getMinute() + "-" + dateTime.getSecond() + "-log.txt"), "UTF-8"));
					logwriter.write("Date Created: " + datetime(dateTime)
	    	        );
					logwriter.newLine();
					logwriter.flush();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	else{
        		try {
					logwriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        });
        
    }
	private String datetime(LocalDateTime dateTime){
		return dateTime.getYear() + "/" + dateTime.getMonthValue() + "/" + dateTime.getDayOfMonth()  
		+ " " + dateTime.getHour() + ":" + dateTime.getMinute() + ":" + dateTime.getSecond();
	}
	@FXML
	protected void clearlog(ActionEvent e){
		logarea.clear();
	}
	@FXML
	protected void updatesetting(ActionEvent e){
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("setting.ini"), "UTF-8"));

			writer.write("fixedclickoffset=" + fixedclickoffset);
    		writer.newLine();
			writer.write("clickrand=" + clickrand);
    		writer.newLine();
			writer.write("fixedexpoffset=" + fixedexpoffset);
    		writer.newLine();
			writer.write("exprand=" + exprand);
    		writer.newLine();
    		writer.write("waitfortargettimeout=" + waitfortargettimeout);
    		writer.newLine();
    		writer.write("notifysoundcheck=" + notifysoundcheck.isSelected());
    		writer.newLine();
    		writer.write("notifysoundtime=" + notifysoundtime);
    		writer.newLine();
    		writer.write("refreshpagecheck=" + refreshpagecheck.isSelected());
    		writer.newLine();
    		writer.write("questexp3round=" + exp3rounds.isSelected());
    		writer.newLine();
    		writer.write("questexp10round=" + exp10rounds.isSelected());
    		writer.newLine();
    		writer.write("questexp30round=" + exp30rounds.isSelected());
    		writer.newLine();
    		writer.write("exptokyotrip1rounds=" + exptokyotrip1rounds.isSelected());
    		writer.newLine();
    		writer.write("exptokyotrip7rounds=" + exptokyotrip1rounds.isSelected());
    		writer.newLine();
    		writer.write("exp1=" + numone.getValue());
    		writer.newLine();
    		writer.write("exp2=" + numtwo.getValue());
    		writer.newLine();
    		writer.write("exp3=" + numthree.getValue());
    		writer.newLine();


    		writer.close();
    		Platform.runLater(new Runnable() {                          
				@Override
				public void run() {
					LocalDateTime dateTime = LocalDateTime.now();
					try{
						logarea.appendText(String.format("%s: File: Update setting success!.\n",datetime(dateTime)
				        ));
						}finally{
							if(savelog.isSelected() == true){
								try {
									logwriter.write(String.format("%s: File: Update setting success!.\n",datetime(dateTime)
							        ));
									logwriter.newLine();
									logwriter.flush();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							final Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("Success"); 
							alert.setHeaderText(""); 
							alert.setContentText("Generate/Update Setting file success!");
							alert.show();
						}
				}
			});

		} catch (IOException f) {
			// TODO Auto-generated catch block
			f.printStackTrace();
		}
	}
	@FXML
	protected void setAssistHandle(MouseEvent e){
		assisthwnd = CursorGetHandle.main(null);
		String ahs = assisthwnd.toString();
		assisthandle.setText(ahs.substring(9));
	}
	@FXML
	protected void setHandle(MouseEvent e){
		hwnd = CursorGetHandle.main(null);
		hwndclick = hwnd;
		String hs = hwnd.toString();
		handlelabel.setText(hs.substring(9));
		handlelabel.setTextFill(Color.BLACK);
		//int[] rect;
		//rect = GetWindowRect.getRect(hwnd);
		//GameRegion = new Region(rect[0], rect[1], rect[2]-rect[0], rect[3]-rect[1]);
		//System.out.println(GameRegion.getRect());
		


	}
	@FXML
	protected void automaticplay(ActionEvent e){
		try {
			System.out.print(PostMessage.refreshPage(assisthwnd));
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
//		System.out.println(rootGroup.getState());
//		rootGroup.interrupt();
		/*ThreadGroup tgp = rootGroup.getThreadGroup();
		Thread[] threads = new Thread[tgp.activeCount()];
		tgp.enumerate(threads, true );
		for(Thread t:threads) {
			t.interrupt();
		}*/
		/**handlelabel.setText("Finding");
		//ImagePath.add("C:\\Users\\Yamato\\workspace\\SikuliKancolle\\res");
		
		myApp = new App("ElectronicObserver") ;    

		myApp.pause(1);
		
		f.wait("gamestart.png");
		GameRegion.click("gamestart.png");
		handlelabel.setText("Success1");*/
		//PostMessage.clickbypostmessage(hwndclick, 194, 224);
		//System.out.println(f.next().getTarget().toStringShort());
		//PostMessage.clickbypostmessage(hwndclick,118,119);
		/**interrupted = clickact("sorties",sr.nextInt(20)+1,sr.nextInt(20)+1);
		if(interrupted == 1){
			statrunning = 0; return;
		}*/
		//Checkminimized.minimizedcheckandshow(assisthwnd);
		//TryWithHWND.enumchildwin(hwnd);
		/**try {
			checkquest();
		} catch (FindFailed | InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		/**BufferedImage test = CaptureByHandle.capture(hwnd);
		
		javafx.scene.image.Image image = SwingFXUtils.toFXImage(test, null);
		titleimg.setImage(image);*/
		
		/**
		Rectangle sd = new Rectangle(0,0,120,300);
		Region dda = new Region(sd);
		
		Settings.OcrTextSearch = true;
		Settings.OcrTextRead = true;
		Settings.OcrLanguage = "jpn";
		
		TextRecognizer.reset();
		
		
		//String targetstring = TextRecognizer.getInstance().recognize(cacheCapture.getSubimage(707, 241, 80, 20));
		//System.out.println(transcapture.getRegion());
		//System.out.println(targetstring);
		
			//String targetstring = TextRecognizer.getInstance().recognize();
			ScreenImage dd = new ScreenImage(sd,cacheCapture.getSubimage(427, 156, 120, 300));
			
			List<Match> test = TextRecognizer.getInstance().listText(dd,dda );
			for(int i = 1;i<8;i++) {
			logarea.appendText(test.iterator().next().getText()+ "\n");
		}
		*/
		//System.out.println(transcapture.getRegion());
		 
		//exp3rounds.setText(targetstring);
		/**HttpProxyServer server =
			    DefaultHttpProxyServer.bootstrap()
			        .withPort(8080)
			        .withFiltersSource(new HttpFiltersSourceAdapter() {
			            public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
			                return new HttpFiltersAdapter(originalRequest) {
			                    @Override
			                    public HttpResponse clientToProxyRequest(HttpObject httpObject) {
			                        // TODO: implement your filtering here
			                        return null;
			                    }

			                    @Override
			                    public HttpObject serverToProxyResponse(HttpObject httpObject) {
			                        // TODO: implement your filtering here
			                    	
			                    	  if (httpObject instanceof HttpResponse) {
			                    	    HttpResponse httpResponse = (HttpResponse) httpObject;
			                    	    System.out.println(httpResponse.toString());
			                    	    System.out.println();
			                    	  }

			                    	 
			                    	  if (httpObject instanceof HttpContent) {
			                    	    HttpContent httpContent = (HttpContent) httpObject;
			                    	    byte[] resposeBody;
			                    	    //resposeBody = Compressor.decompress(httpContent.content().);
			                    	    
										System.out.println(resposeBody.toString());
			                    	    
			                    	  }
			                    	  return httpObject;
			                    }
			                };
			            }
			        })
			        .start();*/
		
		//System.out.println(findexists("needsupply",2));
//		TryWithHWND ttw = new TryWithHWND();
//		HWND ttr = ttw.test(assisthwnd);
//		hwndclick = ttr;
		//System.out.println(ttr);
		
//		BufferedImage test = CaptureByHandle.capture_printWindow(hwnd);
//		
//		javafx.scene.image.Image image = SwingFXUtils.toFXImage(test, null);
//		titleimg.setImage(image);
		
		

		
		
	}
	@FXML
	protected void startone(ActionEvent e){
		if(handlelabel.getText().compareTo("Waiting") != 0){
			int sleep = 1000;
			Platform.runLater(new Runnable() {                          
				@Override
				public void run() {
					LocalDateTime dateTime = LocalDateTime.now();
					try{
						startone.setDisable(true);
						logarea.appendText(String.format("%s: Status: Start fleet 2 exp.\n",datetime(dateTime)
				        ));
						}finally{
							if(savelog.isSelected() == true){
								try {
									logwriter.write(String.format("%s: Status: Start fleet 2 exp.\n",datetime(dateTime)
							        ));
									logwriter.newLine();
									logwriter.flush();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
				}
			});

		        KeyFrame kf = new KeyFrame(Duration.millis(sleep), new EventHandler<ActionEvent>() {
		            @Override
		            public void handle(ActionEvent event) {
		            	service = new Service<Void>() {
		                    @Override
		                    protected Task<Void> createTask() {
		                        return new Task<Void>() {           
		                            @Override
		                            protected Void call() throws Exception {
		                                //Background work   
		                            	int total = Integer.parseInt(hourone.getValue())*3600 + Integer.parseInt(minuteone.getValue())*60 + Integer.parseInt(secondone.getValue());
		                                final CountDownLatch latch = new CountDownLatch(1);
		                                Platform.runLater(new Runnable() {                          
		                                    @Override
		                                    public void run() {
		                                    	//FX Stuff done here
		                                        try{
		                                        	if(statusone.getValue().equals("expeditioning")){
		                        	            		int total = Integer.parseInt(hourone.getValue())*3600 + Integer.parseInt(minuteone.getValue())*60 + Integer.parseInt(secondone.getValue());
		                        	            		if(total !=0 && Integer.parseInt(secondone.getValue()) == 0 && Integer.parseInt(minuteone.getValue()) !=0){
		                        	            			secondone.setValue(String.valueOf(59));
		                        	            			minuteone.setValue(String.valueOf(Integer.parseInt(minuteone.getValue())-1));
		                        	            		}
		                        	            		else if(total !=0 && Integer.parseInt(secondone.getValue()) == 0 && Integer.parseInt(minuteone.getValue()) == 0 && Integer.parseInt(hourone.getValue()) !=0){
		                        	            			secondone.setValue(String.valueOf(59));
		                        	            			minuteone.setValue(String.valueOf(59));
		                        	            			hourone.setValue(String.valueOf(Integer.parseInt(hourone.getValue())-1));
		                        	            		}
		                        	            		else if(total == 0){
		                        	            				
		                        	            		}
		                        	            		else{
		                        	            			secondone.setValue(String.valueOf(Integer.parseInt(secondone.getValue())-1));
		                        	            		}        
		                                        	}
		                                        	else if(statusone.getValue().equals("no expedition")){
	                        	            			secondone.setValue(String.valueOf(0));
	                        	            			minuteone.setValue(String.valueOf(0));
	                        	            			hourone.setValue(String.valueOf(0));
	                        	            		}	  
		                                            
		                                        }finally{
		                                            latch.countDown();
		                                        }
		                                    }
		                                });
		                                latch.await();                      
		                                //Keep with the background work
		                              
		                                if(statusone.getValue().equals("expeditioning")){
			                                if(total == notifysoundtime && notifysoundcheck.isSelected() == true){
			        	            			//bot.focus();
			        	            	        mp.play();
			        	            	        
			        	            		}
			        	            		if((total == 1 || total == 0)){
			        	            			fleet2startstatus = 1;
			        	            			if(fleet2ExpCountChangable == 1) {
			        	            				fleet2ExpCountChangable = 0;
			        	            				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("mission.ini"), "UTF-8")); 

									    			String str = null;
									    			str = reader.readLine();
									    			reader.close();
									    			LocalDateTime lastupdatedtimelocal = LocalDateTime.parse(str.substring(13, str.length()));
									    			ZonedDateTime lastupdatedtime = lastupdatedtimelocal.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("GMT+08:00"));
									    			ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of("GMT+08:00"));
									    			WeekFields weekFields = WeekFields.ISO;
									    			ZonedDateTime lastupdatedyesterday = lastupdatedtime.minusDays(1);
									    			int lastupdatedyesterdayweekNumber = lastupdatedyesterday.get(weekFields.weekOfWeekBasedYear());
									    			int lastupdatedweekNumber = lastupdatedtime.get(weekFields.weekOfWeekBasedYear());
									    			int weekNumber = dateTime.get(weekFields.weekOfWeekBasedYear());
									    			Platform.runLater(new Runnable() {                          
								    					@Override
								    					public void run() {
								    						try{
								    							if((dateTime.getDayOfYear()==lastupdatedtime.getDayOfYear() && lastupdatedtime.getHour()>=0 && lastupdatedtime.isBefore(ZonedDateTime.of(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), 4, 0, 0, 0, ZoneId.of("GMT+08:00"))) && dateTime.isAfter(ZonedDateTime.of(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), 4, 0, 0, 0, ZoneId.of("GMT+08:00"))))||
								    			    					(dateTime.getDayOfYear()>lastupdatedtime.getDayOfYear() && dateTime.isAfter(ZonedDateTime.of(lastupdatedtime.getYear(), lastupdatedtime.getMonthValue(), lastupdatedtime.getDayOfMonth(), 4, 0, 0, 0, ZoneId.of("GMT+08:00")).plusDays(1)))||
								    			    					(dateTime.getYear()>lastupdatedtime.getYear() && dateTime.isAfter(ZonedDateTime.of(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), 4, 0, 0, 0, ZoneId.of("GMT+08:00"))))) {
												    				firstrunofaday = 1;
												    	    		exp3progress.setText("0");
												    	    		exp10progress.setText("0");
												    	    		exp3rounds.setSelected(true);
												    	    		exp10rounds.setSelected(true);
												    	    		QuestAcceptList.get(0).setSelected(false);
												    	    		QuestAcceptList.get(1).setSelected(false);
												    			}
								    							ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of("GMT+08:00"));
								    							if((weekNumber==lastupdatedweekNumber && lastupdatedyesterdayweekNumber<weekNumber && dateTime.isAfter(ZonedDateTime.of(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), 4, 0, 0, 0, ZoneId.of("GMT+08:00"))) && lastupdatedtime.getHour()>=0 && lastupdatedtime.getHour()<4)||
								    			    					(weekNumber>lastupdatedweekNumber && dateTime.isAfter(ZonedDateTime.of(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), 4, 0, 0, 0, ZoneId.of("GMT+08:00")))) || 
								    			    					(weekNumber<lastupdatedweekNumber && lastupdatedtime.isBefore(dateTime))) {
												    	    		exp30progress.setText("0");
												    	    		exptokyo1progress.setText("0");
												    	    		exptokyo7progress.setText("0");
												    	    		
												    	    		exp30rounds.setSelected(true);
												    	    		exptokyotrip1rounds.setSelected(true);
												    	    		exptokyotrip7rounds.setSelected(true);
												    	    		QuestAcceptList.get(2).setSelected(false);
												    	    		QuestAcceptList.get(3).setSelected(false);
												    	    		QuestAcceptList.get(4).setSelected(false);
												    			}
												    			if(expaccept[0] == 1) {
												    				exp3progress.setText(String.valueOf(Integer.valueOf(exp3progress.getText())+1));
												    			}
												    			if(expaccept[1] == 1) {
												    				exp10progress.setText(String.valueOf(Integer.valueOf(exp10progress.getText())+1));
												    			}
												    			if(expaccept[2] == 1) {
												    				exp30progress.setText(String.valueOf(Integer.valueOf(exp30progress.getText())+1));
												    			}
																if(String.valueOf(fleet2currentexp).equals("37") || String.valueOf(fleet2currentexp).equals("38")) {
																	if(expaccept[3] == 1) {
																		exptokyo1progress.setText(String.valueOf(Integer.valueOf(exptokyo1progress.getText())+1));
																	}
																	if(expaccept[4] == 1) {
																		exptokyo7progress.setText(String.valueOf(Integer.valueOf(exptokyo7progress.getText())+1));
																	}								
																}
								    							}finally{
								    							}
								    					}
								    				});
			        	            			}
			        	            			if(statrunning == 0){
			        	            				startexp();
			        	            				if(interrupted == 1) {
			        	            					Platform.runLater(new Runnable() {                          
			        	            						@Override
			        	            						public void run() {
			        	            							LocalDateTime dateTime = LocalDateTime.now();
			        	            							try{
			        	            								workingstatus.setText("idle");
			        	            								workingstatus.setTextFill(Color.web("#009a00"));
			        	            								logarea.appendText(String.format("%s: Status: interrupted.\n",datetime(dateTime)
			        	            						        ));
			        	            								}finally{
			        	            									if(savelog.isSelected() == true){
			        	            										try {
			        	            											logwriter.write(String.format("%s: Status: interrupted.\n",datetime(dateTime)
			        	            									        ));
			        	            											logwriter.newLine();
			        	            											logwriter.flush();
			        	            										} catch (IOException e) {
			        	            											// TODO Auto-generated catch block
			        	            											e.printStackTrace();
			        	            										}
			        	            									}
			        	            								}
			        	            						}
			        	            					});
			        	            				}
			        	            			}
			        	            			else {
			        	            				if(workingstatus.getText().equals("idle")) {
			        	            					Platform.runLater(new Runnable() {                          
			        	            						@Override
			        	            						public void run() {
			        	            							try{
			        	            								workingstatus.setText("working");
			        	            								workingstatus.setTextFill(Color.RED);
			        	            								}finally{
			        	            								}
			        	            						}
			        	            					});
			        	            				}
			        	            			}
			        	            		}
		                                }
		                                else if(statusone.getValue().equals("no expedition")){
	            	            			fleet2startstatus = 2;
	            	            			if(statrunning == 0){
		        	            				startexp();
		        	            				if(interrupted == 1) {
		        	            					Platform.runLater(new Runnable() {                          
		        	            						@Override
		        	            						public void run() {
		        	            							LocalDateTime dateTime = LocalDateTime.now();
		        	            							try{
		        	            								workingstatus.setText("idle");
		        	            								workingstatus.setTextFill(Color.web("#009a00"));
		        	            								logarea.appendText(String.format("%s: Status: interrupted.\n",datetime(dateTime)
		        	            						        ));
		        	            								}finally{
		        	            									if(savelog.isSelected() == true){
		        	            										try {
		        	            											logwriter.write(String.format("%s: Status: interrupted.\n",datetime(dateTime)
		        	            									        ));
		        	            											logwriter.newLine();
		        	            											logwriter.flush();
		        	            										} catch (IOException e) {
		        	            											// TODO Auto-generated catch block
		        	            											e.printStackTrace();
		        	            										}
		        	            									}
		        	            								}
		        	            						}
		        	            					});
		        	            				}
		        	            			}
	            	            			else {
		        	            				if(workingstatus.getText().equals("idle")) {
		        	            					Platform.runLater(new Runnable() {                          
		        	            						@Override
		        	            						public void run() {
		        	            							try{
		        	            								workingstatus.setText("working");
		        	            								workingstatus.setTextFill(Color.RED);
		        	            								}finally{
		        	            								}
		        	            						}
		        	            					});
		        	            				}
		        	            			}
	            	            		}	  
		                                return null;
		                            }
		                        };
		                    }
		                };
		                service.start();
	
	            		
	            		
		            }
		        });
		        t1 = new Timeline(kf);
		        t1.setCycleCount(Timeline.INDEFINITE);
		        t1.play();
		}
		else{
			final Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning"); 
			alert.setHeaderText(""); 
			alert.setContentText("Please set the handle first!\nDrag the 'Find Area' Button to game's area and then release it.");
			alert.showAndWait();
		}
	        
	        
	}
	@FXML
	protected void stopone(ActionEvent e){
		fleet2startstatus = 0;
		t1.stop();
		Platform.runLater(new Runnable() {                          
			@Override
			public void run() {
				LocalDateTime dateTime = LocalDateTime.now();
				try{
					startone.setDisable(false);
					logarea.appendText(String.format("%s: Status: Stop fleet 2 exp.\n",datetime(dateTime)
			        ));
					}finally{
						if(savelog.isSelected() == true){
							try {
								logwriter.write(String.format("%s: Status: Stop fleet 2 exp.\n",datetime(dateTime)
						        ));
								logwriter.newLine();
								logwriter.flush();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
			}
		});
	}
	@FXML
	protected void starttwo(ActionEvent e){
		if(handlelabel.getText().compareTo("Waiting") != 0){
			int sleep = 1000;
			Platform.runLater(new Runnable() {                          
				@Override
				public void run() {
					LocalDateTime dateTime = LocalDateTime.now();
					try{
						starttwo.setDisable(true);
						logarea.appendText(String.format("%s: Status: Start fleet 3 exp.\n",datetime(dateTime)
				        ));
						}finally{
							if(savelog.isSelected() == true){
								try {
									logwriter.write(String.format("%s: Status: Start fleet 3 exp.\n",datetime(dateTime)
							        ));
									logwriter.newLine();
									logwriter.flush();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
				}
			});

		        KeyFrame kf = new KeyFrame(Duration.millis(sleep), new EventHandler<ActionEvent>() {
		            @Override
		            public void handle(ActionEvent event) {
		            	Service<Void> service = new Service<Void>() {
		                    @Override
		                    protected Task<Void> createTask() {
		                        return new Task<Void>() {           
		                            @Override
		                            protected Void call() throws Exception {
		                                //Background work   
		                            	int total = Integer.parseInt(hourtwo.getValue())*3600 + Integer.parseInt(minutetwo.getValue())*60 + Integer.parseInt(secondtwo.getValue());
		                                final CountDownLatch latch = new CountDownLatch(1);
		                                Platform.runLater(new Runnable() {                          
		                                    @Override
		                                    public void run() {
		                                    	//FX Stuff done here
		                                        try{
		                                        	if(statustwo.getValue().equals("expeditioning")){
		                        	            		int total = Integer.parseInt(hourtwo.getValue())*3600 + Integer.parseInt(minutetwo.getValue())*60 + Integer.parseInt(secondtwo.getValue());
		                        	            		if(total !=0 && Integer.parseInt(secondtwo.getValue()) == 0 && Integer.parseInt(minutetwo.getValue()) !=0){
		                        	            			secondtwo.setValue(String.valueOf(59));
		                        	            			minutetwo.setValue(String.valueOf(Integer.parseInt(minutetwo.getValue())-1));
		                        	            		}
		                        	            		else if(total !=0 && Integer.parseInt(secondtwo.getValue()) == 0 && Integer.parseInt(minutetwo.getValue()) == 0 && Integer.parseInt(hourtwo.getValue()) !=0){
		                        	            			secondtwo.setValue(String.valueOf(59));
		                        	            			minutetwo.setValue(String.valueOf(59));
		                        	            			hourtwo.setValue(String.valueOf(Integer.parseInt(hourtwo.getValue())-1));
		                        	            		}
		                        	            		else if(total == 0){
		                        	            				
		                        	            		}
		                        	            		else{
		                        	            			secondtwo.setValue(String.valueOf(Integer.parseInt(secondtwo.getValue())-1));
		                        	            		}        
		                                        	}
		                                        	else if(statustwo.getValue().equals("no expedition")){
	                        	            			secondtwo.setValue(String.valueOf(0));
	                        	            			minutetwo.setValue(String.valueOf(0));
	                        	            			hourtwo.setValue(String.valueOf(0));
	                        	            		}	  
		                                            
		                                        }finally{
		                                            latch.countDown();
		                                        }
		                                    }
		                                });
		                                latch.await();                      
		                                //Keep with the background work
		                                
		                                if(statustwo.getValue().equals("expeditioning")){
		                                	if(total == notifysoundtime && notifysoundcheck.isSelected() == true){
			        	            			//bot.focus();
			        	            	        mp.play();
			        	            	        
			        	            		}
			        	            		if((total == 1 || total == 0)){
			        	            			fleet3startstatus = 1;
			        	            			if(fleet3ExpCountChangable == 1) {
			        	            				fleet3ExpCountChangable = 0;
			        	            				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("mission.ini"), "UTF-8")); 

									    			String str = null;
									    			str = reader.readLine();
									    			reader.close();
									    			LocalDateTime lastupdatedtimelocal = LocalDateTime.parse(str.substring(13, str.length()));
									    			ZonedDateTime lastupdatedtime = lastupdatedtimelocal.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("GMT+08:00"));
									    			ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of("GMT+08:00"));
									    			WeekFields weekFields = WeekFields.ISO;
									    			ZonedDateTime lastupdatedyesterday = lastupdatedtime.minusDays(1);
									    			int lastupdatedyesterdayweekNumber = lastupdatedyesterday.get(weekFields.weekOfWeekBasedYear());
									    			int lastupdatedweekNumber = lastupdatedtime.get(weekFields.weekOfWeekBasedYear());
									    			int weekNumber = dateTime.get(weekFields.weekOfWeekBasedYear());
									    			Platform.runLater(new Runnable() {                          
								    					@Override
								    					public void run() {
								    						try{
								    							if((dateTime.getDayOfYear()==lastupdatedtime.getDayOfYear() && lastupdatedtime.getHour()>=0 && lastupdatedtime.isBefore(ZonedDateTime.of(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), 4, 0, 0, 0, ZoneId.of("GMT+08:00"))) && dateTime.isAfter(ZonedDateTime.of(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), 4, 0, 0, 0, ZoneId.of("GMT+08:00"))))||
								    			    					(dateTime.getDayOfYear()>lastupdatedtime.getDayOfYear() && dateTime.isAfter(ZonedDateTime.of(lastupdatedtime.getYear(), lastupdatedtime.getMonthValue(), lastupdatedtime.getDayOfMonth(), 4, 0, 0, 0, ZoneId.of("GMT+08:00")).plusDays(1)))||
								    			    					(dateTime.getYear()>lastupdatedtime.getYear() && dateTime.isAfter(ZonedDateTime.of(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), 4, 0, 0, 0, ZoneId.of("GMT+08:00"))))) {
												    				firstrunofaday = 1;
												    	    		exp3progress.setText("0");
												    	    		exp10progress.setText("0");
												    	    		exp3rounds.setSelected(true);
												    	    		exp10rounds.setSelected(true);
												    	    		QuestAcceptList.get(0).setSelected(false);
												    	    		QuestAcceptList.get(1).setSelected(false);
												    			}
								    							ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of("GMT+08:00"));
								    							if((weekNumber==lastupdatedweekNumber && lastupdatedyesterdayweekNumber<weekNumber && dateTime.isAfter(ZonedDateTime.of(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), 4, 0, 0, 0, ZoneId.of("GMT+08:00"))) && lastupdatedtime.getHour()>=0 && lastupdatedtime.getHour()<4)||
								    			    					(weekNumber>lastupdatedweekNumber && dateTime.isAfter(ZonedDateTime.of(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), 4, 0, 0, 0, ZoneId.of("GMT+08:00")))) || 
								    			    					(weekNumber<lastupdatedweekNumber && lastupdatedtime.isBefore(dateTime))) {
												    	    		exp30progress.setText("0");
												    	    		exptokyo1progress.setText("0");
												    	    		exptokyo7progress.setText("0");
												    	    		exp30rounds.setSelected(true);
												    	    		exptokyotrip1rounds.setSelected(true);
												    	    		exptokyotrip7rounds.setSelected(true);
												    	    		QuestAcceptList.get(2).setSelected(false);
												    	    		QuestAcceptList.get(3).setSelected(false);
												    	    		QuestAcceptList.get(4).setSelected(false);
												    			}
												    			if(expaccept[0] == 1) {
												    				exp3progress.setText(String.valueOf(Integer.valueOf(exp3progress.getText())+1));
												    			}
												    			if(expaccept[1] == 1) {
												    				exp10progress.setText(String.valueOf(Integer.valueOf(exp10progress.getText())+1));
												    			}
												    			if(expaccept[2] == 1) {
												    				exp30progress.setText(String.valueOf(Integer.valueOf(exp30progress.getText())+1));
												    			}
																if(String.valueOf(fleet3currentexp).equals("37") || String.valueOf(fleet3currentexp).equals("38")) {
																	if(expaccept[3] == 1) {
																		exptokyo1progress.setText(String.valueOf(Integer.valueOf(exptokyo1progress.getText())+1));
																	}
																	if(expaccept[4] == 1) {
																		exptokyo7progress.setText(String.valueOf(Integer.valueOf(exptokyo7progress.getText())+1));
																	}								
																}
								    							}finally{
								    							}
								    					}
								    				});
			        	            			}
			        	            			if(statrunning == 0){
			        	            				startexp();
			        	            				if(interrupted == 1) {
			        	            					Platform.runLater(new Runnable() {                          
			        	            						@Override
			        	            						public void run() {
			        	            							LocalDateTime dateTime = LocalDateTime.now();
			        	            							try{
			        	            								workingstatus.setText("idle");
			        	            								workingstatus.setTextFill(Color.web("#009a00"));
			        	            								logarea.appendText(String.format("%s: Status: interrupted.\n",datetime(dateTime)
			        	            						        ));
			        	            								}finally{
			        	            									if(savelog.isSelected() == true){
			        	            										try {
			        	            											logwriter.write(String.format("%s: Status: interrupted.\n",datetime(dateTime)
			        	            									        ));
			        	            											logwriter.newLine();
			        	            											logwriter.flush();
			        	            										} catch (IOException e) {
			        	            											// TODO Auto-generated catch block
			        	            											e.printStackTrace();
			        	            										}
			        	            									}
			        	            								}
			        	            						}
			        	            					});
			        	            				}
			        	            			}
		            	            			else {
			        	            				if(workingstatus.getText().equals("idle")) {
			        	            					Platform.runLater(new Runnable() {                          
			        	            						@Override
			        	            						public void run() {
			        	            							try{
			        	            								workingstatus.setText("working");
			        	            								workingstatus.setTextFill(Color.RED);
			        	            								}finally{
			        	            								}
			        	            						}
			        	            					});
			        	            				}
			        	            			}
			        	            		}
		                                }
		                                else if(statustwo.getValue().equals("no expedition")){
	            	            			fleet3startstatus = 2;
	            	            			if(statrunning == 0){
		        	            				startexp();
		        	            				if(interrupted == 1) {
		        	            					Platform.runLater(new Runnable() {                          
		        	            						@Override
		        	            						public void run() {
		        	            							LocalDateTime dateTime = LocalDateTime.now();
		        	            							try{
		        	            								workingstatus.setText("idle");
		        	            								workingstatus.setTextFill(Color.web("#009a00"));
		        	            								logarea.appendText(String.format("%s: Status: interrupted.\n",datetime(dateTime)
		        	            						        ));
		        	            								}finally{
		        	            									if(savelog.isSelected() == true){
		        	            										try {
		        	            											logwriter.write(String.format("%s: Status: interrupted.\n",datetime(dateTime)
		        	            									        ));
		        	            											logwriter.newLine();
		        	            											logwriter.flush();
		        	            										} catch (IOException e) {
		        	            											// TODO Auto-generated catch block
		        	            											e.printStackTrace();
		        	            										}
		        	            									}
		        	            								}
		        	            						}
		        	            					});
		        	            				}
		        	            			}
	            	            			else {
		        	            				if(workingstatus.getText().equals("idle")) {
		        	            					Platform.runLater(new Runnable() {                          
		        	            						@Override
		        	            						public void run() {
		        	            							try{
		        	            								workingstatus.setText("working");
		        	            								workingstatus.setTextFill(Color.RED);
		        	            								}finally{
		        	            								}
		        	            						}
		        	            					});
		        	            				}
		        	            			}
	            	            		}	  
		                                return null;
		                            }
		                        };
		                    }
		                };
		                service.start();
	
	            		
	            		
		            }
		        });
		        t2 = new Timeline(kf);
		        t2.setCycleCount(Timeline.INDEFINITE);
		        t2.play();
		}
		else{
			final Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning"); 
			alert.setHeaderText(""); 
			alert.setContentText("Please set the handle first!\nDrag the 'Find Area' Button to game's area and then release it.");
			alert.showAndWait();
		}
	        
	        
	}
	
	@FXML
	protected void stoptwo(ActionEvent e){
		fleet3startstatus = 0;
		t2.stop();
		Platform.runLater(new Runnable() {                          
			@Override
			public void run() {
				LocalDateTime dateTime = LocalDateTime.now();
				try{
					starttwo.setDisable(false);
					logarea.appendText(String.format("%s: Status: Stop fleet 3 exp.\n",datetime(dateTime)
			        ));
					}finally{
						if(savelog.isSelected() == true){
							try {
								logwriter.write(String.format("%s: Status: Stop fleet 3 exp.\n",datetime(dateTime)
						        ));
								logwriter.newLine();
								logwriter.flush();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
			}
		});
	}
	@FXML
	protected void startthree(ActionEvent e){
		if(handlelabel.getText().compareTo("Waiting") != 0){
			int sleep = 1000;
			Platform.runLater(new Runnable() {                          
				@Override
				public void run() {
					LocalDateTime dateTime = LocalDateTime.now();
					try{
						startthree.setDisable(true);
						logarea.appendText(String.format("%s: Status: Start fleet 4 exp.\n",datetime(dateTime)
				        ));
						}finally{
							if(savelog.isSelected() == true){
								try {
									logwriter.write(String.format("%s: Status: Start fleet 4 exp.\n",datetime(dateTime)
							        ));
									logwriter.newLine();
									logwriter.flush();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
				}
			});
			
		        KeyFrame kf = new KeyFrame(Duration.millis(sleep), new EventHandler<ActionEvent>() {
		            @Override
		            public void handle(ActionEvent event) {
		            	Service<Void> service = new Service<Void>() {
		                    @Override
		                    protected Task<Void> createTask() {
		                        return new Task<Void>() {           
		                            @Override
		                            protected Void call() throws Exception {
		                                //Background work   
		                            	int total = Integer.parseInt(hourthree.getValue())*3600 + Integer.parseInt(minutethree.getValue())*60 + Integer.parseInt(secondthree.getValue());
		                                final CountDownLatch latch = new CountDownLatch(1);
		                                Platform.runLater(new Runnable() {                          
		                                    @Override
		                                    public void run() {
		                                    	//FX Stuff done here
		                                        try{
		                                        	if(statusthree.getValue().equals("expeditioning")){
		                        	            		int total = Integer.parseInt(hourthree.getValue())*3600 + Integer.parseInt(minutethree.getValue())*60 + Integer.parseInt(secondthree.getValue());
		                        	            		if(total !=0 && Integer.parseInt(secondthree.getValue()) == 0 && Integer.parseInt(minutethree.getValue()) !=0){
		                        	            			secondthree.setValue(String.valueOf(59));
		                        	            			minutethree.setValue(String.valueOf(Integer.parseInt(minutethree.getValue())-1));
		                        	            		}
		                        	            		else if(total !=0 && Integer.parseInt(secondthree.getValue()) == 0 && Integer.parseInt(minutethree.getValue()) == 0 && Integer.parseInt(hourthree.getValue()) !=0){
		                        	            			secondthree.setValue(String.valueOf(59));
		                        	            			minutethree.setValue(String.valueOf(59));
		                        	            			hourthree.setValue(String.valueOf(Integer.parseInt(hourthree.getValue())-1));
		                        	            		}
		                        	            		else if(total == 0){
		                        	            				
		                        	            		}
		                        	            		else{
		                        	            			secondthree.setValue(String.valueOf(Integer.parseInt(secondthree.getValue())-1));
		                        	            		}        
		                                        	}
		                                        	else if(statusthree.getValue().equals("no expedition")){
	                        	            			secondthree.setValue(String.valueOf(0));
	                        	            			minutethree.setValue(String.valueOf(0));
	                        	            			hourthree.setValue(String.valueOf(0));
	                        	            		}	  
		                                            
		                                        }finally{
		                                            latch.countDown();
		                                        }
		                                    }
		                                });
		                                latch.await();                      
		                                //Keep with the background work
		                               
		                                if(statusthree.getValue().equals("expeditioning")){
		                                	if(total == notifysoundtime && notifysoundcheck.isSelected() == true){
			        	            			//bot.focus();
			        	            	        mp.play();
			        	            	        
			        	            		}
			        	            		if((total == 1 || total == 0 )){
			        	            			fleet4startstatus = 1;
			        	            			if(fleet4ExpCountChangable == 1) {
			        	            				fleet4ExpCountChangable = 0;
			        	            				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("mission.ini"), "UTF-8")); 

									    			String str = null;
									    			str = reader.readLine();
									    			reader.close();
									    			LocalDateTime lastupdatedtimelocal = LocalDateTime.parse(str.substring(13, str.length()));
									    			ZonedDateTime lastupdatedtime = lastupdatedtimelocal.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("GMT+08:00"));
									    			ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of("GMT+08:00"));
									    			WeekFields weekFields = WeekFields.ISO;
									    			ZonedDateTime lastupdatedyesterday = lastupdatedtime.minusDays(1);
									    			int lastupdatedyesterdayweekNumber = lastupdatedyesterday.get(weekFields.weekOfWeekBasedYear());
									    			int lastupdatedweekNumber = lastupdatedtime.get(weekFields.weekOfWeekBasedYear());
									    			int weekNumber = dateTime.get(weekFields.weekOfWeekBasedYear());
									    			Platform.runLater(new Runnable() {                          
								    					@Override
								    					public void run() {
								    						try{
								    							if((dateTime.getDayOfYear()==lastupdatedtime.getDayOfYear() && lastupdatedtime.getHour()>=0 && lastupdatedtime.isBefore(ZonedDateTime.of(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), 4, 0, 0, 0, ZoneId.of("GMT+08:00"))) && dateTime.isAfter(ZonedDateTime.of(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), 4, 0, 0, 0, ZoneId.of("GMT+08:00"))))||
								    			    					(dateTime.getDayOfYear()>lastupdatedtime.getDayOfYear() && dateTime.isAfter(ZonedDateTime.of(lastupdatedtime.getYear(), lastupdatedtime.getMonthValue(), lastupdatedtime.getDayOfMonth(), 4, 0, 0, 0, ZoneId.of("GMT+08:00")).plusDays(1)))||
								    			    					(dateTime.getYear()>lastupdatedtime.getYear() && dateTime.isAfter(ZonedDateTime.of(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), 4, 0, 0, 0, ZoneId.of("GMT+08:00"))))) {
												    				firstrunofaday = 1;
												    	    		exp3progress.setText("0");
												    	    		exp10progress.setText("0");
												    	    		exp3rounds.setSelected(true);
												    	    		exp10rounds.setSelected(true);
												    	    		QuestAcceptList.get(0).setSelected(false);
												    	    		QuestAcceptList.get(1).setSelected(false);
												    			}
								    							ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of("GMT+08:00"));
								    							if((weekNumber==lastupdatedweekNumber && lastupdatedyesterdayweekNumber<weekNumber && dateTime.isAfter(ZonedDateTime.of(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), 4, 0, 0, 0, ZoneId.of("GMT+08:00"))) && lastupdatedtime.getHour()>=0 && lastupdatedtime.getHour()<4)||
								    			    					(weekNumber>lastupdatedweekNumber && dateTime.isAfter(ZonedDateTime.of(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), 4, 0, 0, 0, ZoneId.of("GMT+08:00")))) || 
								    			    					(weekNumber<lastupdatedweekNumber && lastupdatedtime.isBefore(dateTime))) {
												    	    		exp30progress.setText("0");
												    	    		exptokyo1progress.setText("0");
												    	    		exptokyo7progress.setText("0");
												    	    		exp30rounds.setSelected(true);
												    	    		exptokyotrip1rounds.setSelected(true);
												    	    		exptokyotrip7rounds.setSelected(true);
												    	    		QuestAcceptList.get(2).setSelected(false);
												    	    		QuestAcceptList.get(3).setSelected(false);
												    	    		QuestAcceptList.get(4).setSelected(false);
												    			}
												    			if(expaccept[0] == 1) {
												    				exp3progress.setText(String.valueOf(Integer.valueOf(exp3progress.getText())+1));
												    			}
												    			if(expaccept[1] == 1) {
												    				exp10progress.setText(String.valueOf(Integer.valueOf(exp10progress.getText())+1));
												    			}
												    			if(expaccept[2] == 1) {
												    				exp30progress.setText(String.valueOf(Integer.valueOf(exp30progress.getText())+1));
												    			}
																if(String.valueOf(fleet4currentexp).equals("37") || String.valueOf(fleet4currentexp).equals("38")) {
																	if(expaccept[3] == 1) {
																		exptokyo1progress.setText(String.valueOf(Integer.valueOf(exptokyo1progress.getText())+1));
																	}
																	if(expaccept[4] == 1) {
																		exptokyo7progress.setText(String.valueOf(Integer.valueOf(exptokyo7progress.getText())+1));
																	}								
																}
								    							}finally{
								    							}
								    					}
								    				});
			        	            			}
			        	            			if(statrunning == 0){
			        	            				startexp();
			        	            				if(interrupted == 1) {
			        	            					Platform.runLater(new Runnable() {                          
			        	            						@Override
			        	            						public void run() {
			        	            							LocalDateTime dateTime = LocalDateTime.now();
			        	            							try{
			        	            								workingstatus.setText("idle");
			        	            								workingstatus.setTextFill(Color.web("#009a00"));
			        	            								logarea.appendText(String.format("%s: Status: interrupted.\n",datetime(dateTime)
			        	            						        ));
			        	            								}finally{
			        	            									if(savelog.isSelected() == true){
			        	            										try {
			        	            											logwriter.write(String.format("%s: Status: interrupted.\n",datetime(dateTime)
			        	            									        ));
			        	            											logwriter.newLine();
			        	            											logwriter.flush();
			        	            										} catch (IOException e) {
			        	            											// TODO Auto-generated catch block
			        	            											e.printStackTrace();
			        	            										}
			        	            									}
			        	            								}
			        	            						}
			        	            					});
			        	            				}
			        	            			}
		            	            			else {
			        	            				if(workingstatus.getText().equals("idle")) {
			        	            					Platform.runLater(new Runnable() {                          
			        	            						@Override
			        	            						public void run() {
			        	            							try{
			        	            								workingstatus.setText("working");
			        	            								workingstatus.setTextFill(Color.RED);
			        	            								}finally{
			        	            								}
			        	            						}
			        	            					});
			        	            				}
			        	            			}
			        	            		}
		                                }
		                                else if(statusthree.getValue().equals("no expedition")){
	            	            			fleet4startstatus = 2;
	            	            			if(statrunning == 0){
		        	            				startexp();
		        	            				if(interrupted == 1) {
		        	            					Platform.runLater(new Runnable() {                          
		        	            						@Override
		        	            						public void run() {
		        	            							LocalDateTime dateTime = LocalDateTime.now();
		        	            							try{
		        	            								workingstatus.setText("idle");
		        	            								workingstatus.setTextFill(Color.web("#009a00"));
		        	            								logarea.appendText(String.format("%s: Status: interrupted.\n",datetime(dateTime)
		        	            						        ));
		        	            								}finally{
		        	            									if(savelog.isSelected() == true){
		        	            										try {
		        	            											logwriter.write(String.format("%s: Status: interrupted.\n",datetime(dateTime)
		        	            									        ));
		        	            											logwriter.newLine();
		        	            											logwriter.flush();
		        	            										} catch (IOException e) {
		        	            											// TODO Auto-generated catch block
		        	            											e.printStackTrace();
		        	            										}
		        	            									}
		        	            								}
		        	            						}
		        	            					});
		        	            				}
		        	            			}
	            	            			else {
		        	            				if(workingstatus.getText().equals("idle")) {
		        	            					Platform.runLater(new Runnable() {                          
		        	            						@Override
		        	            						public void run() {
		        	            							try{
		        	            								workingstatus.setText("working");
		        	            								workingstatus.setTextFill(Color.RED);
		        	            								}finally{
		        	            								}
		        	            						}
		        	            					});
		        	            				}
		        	            			}
	            	            		}	  
		                                return null;
		                            }
		                        };
		                    }
		                };
		                service.start();
	
	            		
	            		
		            }
		        });
		        t3 = new Timeline(kf);
		        t3.setCycleCount(Timeline.INDEFINITE);
		        t3.play();
		}
		else{
			final Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning"); 
			alert.setHeaderText(""); 
			alert.setContentText("Please set the handle first!\nDrag the 'Find Area' Button to game's area and then release it.");
			alert.showAndWait();
		}
	        
	        
	}
	
	@FXML
	protected void stopthree(ActionEvent e){
		fleet4startstatus = 0;
		t3.stop();
		Platform.runLater(new Runnable() {                          
			@Override
			public void run() {
				LocalDateTime dateTime = LocalDateTime.now();
				try{
					startthree.setDisable(false);
					logarea.appendText(String.format("%s: Status: Stop fleet 4 exp.\n",datetime(dateTime)
			        ));
					}finally{
						if(savelog.isSelected() == true){
							try {
								logwriter.write(String.format("%s: Status: Stop fleet 4 exp.\n",datetime(dateTime)
						        ));
								logwriter.newLine();
								logwriter.flush();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
			}
		});
	}
	@FXML
	protected void startall(ActionEvent e){
		if(handlelabel.getText().compareTo("Waiting") != 0){
			if(fleet2.isSelected() == true && startone.isDisabled() != true){
				startone(null);
			}
			if(fleet3.isSelected() == true && starttwo.isDisabled() != true){
				starttwo(null);
			}
			if(fleet4.isSelected() == true && startthree.isDisabled() != true){
				startthree(null);
			}
			Platform.runLater(new Runnable() {                          
				@Override
				public void run() {
					LocalDateTime dateTime = LocalDateTime.now();
					try{
						logarea.appendText(String.format("%s: Status: Start all checked exp.\n",datetime(dateTime)
				        ));
						}finally{
							if(savelog.isSelected() == true){
								try {
									logwriter.write(String.format("%s: Status: Start all checked exp.\n",datetime(dateTime)
							        ));
									logwriter.newLine();
									logwriter.flush();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
				}
			});

		}
		else{
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning"); 
			alert.setHeaderText(""); 
			alert.setContentText("Please set the handle first!\nDrag the 'Find Area' Button to game's area and then release it.");
			alert.showAndWait();
		}
	}
	@FXML
	protected void stopall(ActionEvent e){
		if(statrunning == 1){
			Platform.runLater(new Runnable() {                          
				@Override
				public void run() {
					LocalDateTime dateTime = LocalDateTime.now();
					try{					
						logarea.appendText(String.format("%s: Status: interrupted by user.\n",datetime(dateTime)
				        ));
						}finally{
							if(savelog.isSelected() == true){
								try {
									logwriter.write(String.format("%s: Status: interrupted by user.\n",datetime(dateTime)
							        ));
									logwriter.newLine();
									logwriter.flush();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
				}
			});
			interrupted = 1;
		}
		if(fleet2.isSelected() == true && startone.isDisabled() == true){
			stopone(null);
		}
		if(fleet3.isSelected() == true && starttwo.isDisabled() == true){
			stoptwo(null);
		}
		if(fleet4.isSelected() == true && startthree.isDisabled() == true){
			stopthree(null);
		}
		Platform.runLater(new Runnable() {                          
			@Override
			public void run() {
				LocalDateTime dateTime = LocalDateTime.now();
				try{
					workingstatus.setText("idle");
					workingstatus.setTextFill(Color.web("#009a00"));
					
					logarea.appendText(String.format("%s: Status: Stop all checked exp.\n",datetime(dateTime)
			        ));
					}finally{
						if(savelog.isSelected() == true){
							try {
								logwriter.write(String.format("%s: Status: Stop all checked exp.\n",datetime(dateTime)
						        ));
								logwriter.newLine();
								logwriter.flush();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
			}
		});
		
	}
	private boolean findexists(String target,double timeout){
		double waittime = timeout;
		if(assisthandle != null){
			Checkminimized.minimizedcheckandshow(assisthwnd);
		}
		BufferedImage cacheCapture = CaptureByHandle.capture_printWindow(hwnd);
		Rectangle gamepicrect = new Rectangle(cacheCapture.getMinX(),cacheCapture.getMinY(),cacheCapture.getWidth(),cacheCapture.getHeight());
		ScreenImage transcapture = new ScreenImage(gamepicrect,cacheCapture);
		Region picregion = new Region(cacheCapture.getMinX(),cacheCapture.getMinY(),cacheCapture.getWidth(),cacheCapture.getHeight());
		Finder f = new Finder(transcapture,picregion);
		Pattern	targetpattern = new Pattern(this.getClass().getResource("/res/" + target + ".png")).similar((float) 0.90);
		Pattern error = new Pattern(this.getClass().getResource("/res/error.png")).similar((float) 0.90);
		do{
			if(interrupted == 1) {
				return false;
			}
			f.find(targetpattern);
			if(f.hasNext() == true){
				return true;
			}
			f.find(error);
			if(f.hasNext() == true){
				Platform.runLater(new Runnable() {                          
					@Override
					public void run() {
						LocalDateTime dateTime = LocalDateTime.now();
						try{
							logarea.appendText(String.format("%s: ERROR: cat bomb interrupted.\n",datetime(dateTime)
					        ));
							}finally{
								if(savelog.isSelected() == true){
									try {
										logwriter.write(String.format("%s: ERROR: cat bomb interrupted.\n",datetime(dateTime)
								        ));
										logwriter.newLine();
										logwriter.flush();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
					}
				});
				if(assisthandle != null && refreshpagecheck.isSelected()){
					try {
						PostMessage.refreshPage(assisthwnd);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Platform.runLater(new Runnable() {                          
						@Override
						public void run() {
							LocalDateTime dateTime = LocalDateTime.now();
							try{
								logarea.appendText(String.format("%s: STATUS: trying to refresh game page.\n",datetime(dateTime)
						        ));
								}finally{
									if(savelog.isSelected() == true){
										try {
											logwriter.write(String.format("%s: STATUS: trying to refresh game page.\n",datetime(dateTime)
									        ));
											logwriter.newLine();
											logwriter.flush();
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								}
						}
					});
					wait(2.0);
					if(findexists("gamestart",30.0)) {
						wait(3.0+sr.nextDouble()*2);
						try {
							clickact("gamestart",sr.nextInt(200),sr.nextInt(40));
						} catch (FindFailed | InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(findexists("sorties",30.0)) {
							wait(3.0+sr.nextDouble()*2);
						}else {
							stopall(null);
						}
					}else {
						stopall(null);
					}
				}else {
					stopall(null);
				}
				interrupted = 1;
				return false;
			}
			try {
				if(waittime < timeperscan) {
					Thread.sleep((long) (waittime*1000));
				}
				else {
					Thread.sleep((long) (timeperscan*1000));
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			waittime -= timeperscan;
			if(assisthandle != null){
				Checkminimized.minimizedcheckandshow(assisthwnd);
			}
			cacheCapture = CaptureByHandle.capture_printWindow(hwnd);
			gamepicrect = new Rectangle(cacheCapture.getMinX(),cacheCapture.getMinY(),cacheCapture.getWidth(),cacheCapture.getHeight());
			transcapture = new ScreenImage(gamepicrect,cacheCapture);
			picregion = new Region(cacheCapture.getMinX(),cacheCapture.getMinY(),cacheCapture.getWidth(),cacheCapture.getHeight());
			f = new Finder(transcapture,picregion);
		}while(waittime > 0);
		Platform.runLater(new Runnable() {                          
			@Override
			public void run() {
				LocalDateTime dateTime = LocalDateTime.now();
				try{
					logarea.appendText(String.format("%s: Warning: Can't find '%s' target.\n",datetime(dateTime)
			        ,target));
					}finally{
						if(savelog.isSelected() == true){
							try {
								logwriter.write(String.format("%s: Warning: Can't find '%s' target.\n",datetime(dateTime)
						        ,target));
								logwriter.newLine();
								logwriter.flush();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
			}
		});
		return false;
	}
	
	private int expResupply(ComboBox<String> expnumcombobox,String fleet,ComboBox<String> status,Label count,int fleetstatus) throws FindFailed, InterruptedException{
		if(interrupted == 1) {
			return 1;
		}

		if(findexists("gaswarning",0.2)|| findexists("gaswarning2",0.1)||findexists("bulletwarning",0.1) ||findexists("bulletwarning2",0.1)){
			Platform.runLater(new Runnable() {                          
				@Override
				public void run() {
					LocalDateTime dateTime = LocalDateTime.now();
					try{
						logarea.appendText(String.format("%s: Warning: %s need resupply. Resupplying\n",datetime(dateTime)
				        ,fleet));
						}finally{
							if(savelog.isSelected() == true){
								try {
									logwriter.write(String.format("%s: Warning: %s need resupply. Resupplying\n",datetime(dateTime)
							        ,fleet));
									logwriter.newLine();
									logwriter.flush();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
				}
			});
			interrupted = clickact("supplyleft",sr.nextInt(30)+1,sr.nextInt(12)+1);
			if(interrupted == 1){
				return interrupted;
			}
			wait(0.3);
			page = 1;
			do{
				interrupted = clickact(fleet,sr.nextInt(9)+1,sr.nextInt(8)+1);
				if(interrupted == 1){
					return interrupted;
				}
			}while(findexists(fleet + "(pressed)",2) == false);
			if(findexists("needsupply",2)){
				do{
					interrupted = clickact("supplyall",sr.nextInt(8)+1,sr.nextInt(8)+1);
					if(interrupted == 1){
						return interrupted;
					}
					wait(0.3);
				}while(findexists("needsupply",2) == true);
				Platform.runLater(new Runnable() {                          
					@Override
					public void run() {
						LocalDateTime dateTime = LocalDateTime.now();
						try{
							logarea.appendText(String.format("%s: Supply %s.\n",datetime(dateTime)
					        ,fleet));
							}finally{
								if(savelog.isSelected() == true){
									try {
										logwriter.write(String.format("%s: Supply %s.\n",datetime(dateTime)
								        ,fleet));
										logwriter.newLine();
										logwriter.flush();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
					}
				});
				Platform.runLater(new Runnable() {                          
					@Override
					public void run() {
						try{
							status.setValue("no expedition");
							int counter = Integer.parseInt(count.getText());
							count.setText(String.valueOf(++counter));
							}finally{
							}
					}
				});
				fleetstatus = 2;
			}
			else {
				if(findexists("expeditioning",1) == false){
					Platform.runLater(new Runnable() {                          
						@Override
						public void run() {
							try{
								status.setValue("no expedition");
								}finally{
								}
						}
					});
					fleetstatus = 2;
				}
				else {
					/**if(fleet.equals("fleet2")) {
						stopone(null);
					}
					if(fleet.equals("fleet3")) {
						stoptwo(null);
					}
					if(fleet.equals("fleet4")) {
						stopthree(null);
					}*/
				}
			}
			interrupted = clickact("home",sr.nextInt(9)+1,sr.nextInt(28)+1);
			if(interrupted == 1){
				return interrupted;
			}
			/**do{
				if(findexists("expeditionreturn",2)){
					interrupted = clickact("expeditionreturn",-(sr.nextInt(400)+1),sr.nextInt(350)+1);
					if(interrupted == 1){
						statrunning = 0; return interrupted;
					}
					
					interrupted = clickact("next",-(sr.nextInt(600)+1),-(sr.nextInt(350)+1));
					if(interrupted == 1){
						statrunning = 0; return interrupted;
					}
					wait(clickoffsettime);
					PostMessage.clickbypostmessage(hwndclick,sr.nextInt(600)+1 ,sr.nextInt(350)+1);
					//interrupted = clickact("next",-(sr.nextInt(600)+1),-(sr.nextInt(350)+1));
					//if(interrupted == 1){
						//statrunning = 0; return;
					//}
				}
			}while(findexists("expeditionreturn",2));
						
			interrupted = clickact("sorties",sr.nextInt(20)+1,sr.nextInt(20)+1);
			if(interrupted == 1){
				return interrupted;
			}
			
			interrupted = clickact("expedition",sr.nextInt(40)+1,sr.nextInt(40)+1);
			if(interrupted == 1){
				return interrupted;
			}
			
			chooseExp(expnumcombobox);
			
			interrupted = clickact("decided",sr.nextInt(30)+1,sr.nextInt(12)+1);
			if(interrupted == 1){
				return interrupted;
			}*/
			return 1;
		}
		return 0;
		
	}
	private int chooseExp(ComboBox<String> expnumcombobox) throws FindFailed, InterruptedException{
		String expid = "expedition" + expnumcombobox.getValue();
		wait(clickoffsettime);
		Platform.runLater(new Runnable() {                          
			@Override
			public void run() {
				LocalDateTime dateTime = LocalDateTime.now();
				try{
					logarea.appendText(String.format("%s: choose %s\n",datetime(dateTime)
			        ,expid));
					}finally{
						if(savelog.isSelected() == true){
							try {
								logwriter.write(String.format("%s: choose %s\n",datetime(dateTime)
						        ,expid));
								logwriter.newLine();
								logwriter.flush();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
			}
		});
		if(expnumcombobox.getValue() == "none"){
			interrupted = 1;
			if(interrupted == 1){
				return interrupted;
			}
		}
		else if(expnumcombobox.getValue() == "A1" || expnumcombobox.getValue() == "A2" || expnumcombobox.getValue() == "A3" || expnumcombobox.getValue() == "A4"){
			if(page != 1) {
				interrupted = clickact("base",sr.nextInt(20)+1,sr.nextInt(14)+1);
				if(interrupted == 1){
					return interrupted;
				}
				page = 1;
			}
			if(page == 1) {
				do {
					page = 10;
					interrupted = clickactCoordinate("expnext",0,-(sr.nextInt(5)+1),-186,-85);
					if(interrupted == 1){
						return interrupted;
					}
					//wait(clickoffsettime);
				}while(!findexists(expid,2));
			}
		}
		else if(expnumcombobox.getValue() == "B1" || expnumcombobox.getValue() == "B2"){
			if(page != 2) {
				interrupted = clickact("southwestislands",sr.nextInt(20)+1,sr.nextInt(14)+1);
				if(interrupted == 1){
					return interrupted;
				}
				page = 2;
			}
			if(page == 2) {
				do {
					page = 11;
					interrupted = clickactCoordinate("expnext",0,-(sr.nextInt(5)+1),-186,-85);
					if(interrupted == 1){
						return interrupted;
					}
					//wait(clickoffsettime);
				}while(!findexists(expid,2));
			}
		}
		else if(Integer.parseInt(expnumcombobox.getValue())<9 && page == 10){
			interrupted = clickact("southwestislands",sr.nextInt(20)+1,sr.nextInt(14)+1);
			if(interrupted == 1){
				return interrupted;
			}
			
			interrupted = clickact("base",sr.nextInt(20)+1,sr.nextInt(14)+1);
			if(interrupted == 1){
				return interrupted;
			}
			page = 1;
		}
		else if(Integer.parseInt(expnumcombobox.getValue())>=9 && Integer.parseInt(expnumcombobox.getValue())<=16 && page == 11){	
			interrupted = clickact("base",sr.nextInt(20)+1,sr.nextInt(14)+1);
			if(interrupted == 1){
				return interrupted;
			}
			
			interrupted = clickact("southwestislands",sr.nextInt(20)+1,sr.nextInt(14)+1);
			if(interrupted == 1){
				return interrupted;
			}
			page = 2;
		}
		else if(Integer.parseInt(expnumcombobox.getValue())<9 && page != 1){			
			interrupted = clickact("base",sr.nextInt(20)+1,sr.nextInt(14)+1);
			if(interrupted == 1){
				return interrupted;
			}
			page = 1;
		}
		else if(Integer.parseInt(expnumcombobox.getValue())>=9 && Integer.parseInt(expnumcombobox.getValue())<=16 && page != 2){	
			interrupted = clickact("southwestislands",sr.nextInt(20)+1,sr.nextInt(14)+1);
			if(interrupted == 1){
				return interrupted;
			}
			page = 2;
		}
		else if(Integer.parseInt(expnumcombobox.getValue())>=17 && Integer.parseInt(expnumcombobox.getValue())<=24 && page != 3){
			interrupted = clickact("north",sr.nextInt(19)+1,sr.nextInt(13)+1);
			if(interrupted == 1){
				return interrupted;
			}
			page = 3;
		}
		else if(Integer.parseInt(expnumcombobox.getValue())>=25 && Integer.parseInt(expnumcombobox.getValue())<=32 && page != 4){
			interrupted = clickact("west",sr.nextInt(20)+1,sr.nextInt(12)+1);
			if(interrupted == 1){
				return interrupted;
			}
			page = 4;
		}
		else if(Integer.parseInt(expnumcombobox.getValue())>=33 && Integer.parseInt(expnumcombobox.getValue())<=40 && page != 5){
			interrupted = clickact("south",sr.nextInt(16)+1,sr.nextInt(9)+1);
			if(interrupted == 1){
				return interrupted;
			}
			page = 5;
		}
		else if(Integer.parseInt(expnumcombobox.getValue())>40 && page != 6){
			interrupted = clickact("southwest",sr.nextInt(16)+1,sr.nextInt(9)+1);
			if(interrupted == 1){
				return interrupted;
			}
			page = 6;
		}
		interrupted = clickact(expid,sr.nextInt(170)+1,sr.nextInt(10)+1);
		if(interrupted == 1){
			return interrupted;
		}
		return 0;
	}
	private int checkquest() throws FindFailed, InterruptedException{
		firstrunofaday = 0;
		questInfoNotGet = 0;
		boolean exp3notfound = false;
		boolean exp10notfound = false;
		boolean exp30notfound = false;
		boolean exptokyotrip1notfound = false;
		boolean exptokyotrip7notfound = false;
		
		boolean questnotfound[] = {exp3notfound,exp10notfound,exp30notfound,exptokyotrip1notfound,exptokyotrip7notfound};
		String questpages[] = {"questdaily","questweekly"};
		
		for(CheckBox quest:QuestCheckBoxList){
			if(quest.isSelected() == true){
				questnotfound[QuestCheckBoxList.indexOf(quest)] = true;
				
			}
		}
		if(QuestCheckBoxList.get(0).isSelected() == true || QuestCheckBoxList.get(1).isSelected() == true ||QuestCheckBoxList.get(2).isSelected() == true || QuestCheckBoxList.get(3).isSelected() == true || QuestCheckBoxList.get(4).isSelected() == true){
			if(assisthandle != null){
				Checkminimized.minimizedcheckandshow(assisthwnd);
			}
			if(interrupted == 1){
				return 1;
			}
			if(findexists("space2",waitfortargettimeout) == false){
				return 1;
			}
			else{
				if(assisthandle != null){
					Checkminimized.minimizedcheckandshow(assisthwnd);
				}
				BufferedImage cacheCapture = CaptureByHandle.capture_printWindow(hwnd);
				Rectangle gamepicrect = new Rectangle(cacheCapture.getMinX(),cacheCapture.getMinY(),cacheCapture.getWidth(),cacheCapture.getHeight());
				ScreenImage transcapture = new ScreenImage(gamepicrect,cacheCapture);
				Region picregion = new Region(cacheCapture.getMinX(),cacheCapture.getMinY(),cacheCapture.getWidth(),cacheCapture.getHeight());
				Finder f = new Finder(transcapture,picregion);
				Pattern targetpattern = new Pattern(this.getClass().getResource("/res/space2.png")).targetOffset(sr.nextInt(58), sr.nextInt(12)).similar((float) 0.90);
				f.find(targetpattern);
				String targetstring = f.next().getTarget().toStringShort();
				wait(clickoffsettime);
				if(interrupted == 1){
					return 1;
				}
				PostMessage.clickbypostmessage(hwndclick, Integer.parseInt(targetstring.substring(2, targetstring.indexOf(",", 2)))-110 , Integer.parseInt(targetstring.substring(targetstring.indexOf(",", 2)+1, targetstring.length()-1)));
				Platform.runLater(new Runnable() {                          
					@Override
					public void run() {
						LocalDateTime dateTime = LocalDateTime.now();
						try{
							logarea.appendText(String.format("%s: click on (%s)(x: %d,y: %d)\n",datetime(dateTime)
					           ,"quest",Integer.parseInt(targetstring.substring(2, targetstring.indexOf(",", 2))),Integer.parseInt(targetstring.substring(targetstring.indexOf(",", 2)+1, targetstring.length()-1))));
							}finally{
								if(savelog.isSelected() == true){
									try {
										logwriter.write(String.format("%s: click on (%s)(x: %d,y: %d)\n",datetime(dateTime)
								           ,"quest",Integer.parseInt(targetstring.substring(2, targetstring.indexOf(",", 2))),Integer.parseInt(targetstring.substring(targetstring.indexOf(",", 2)+1, targetstring.length()-1))));
										logwriter.newLine();
										logwriter.flush();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
					}
				});
				interrupted = clickact("inquest",-(sr.nextInt(200)+1),sr.nextInt(200)+1);
				if(interrupted == 1){
					return interrupted;
				}
				wait(clickoffsettime);
				for(String qpages:questpages) {
					do {
						interrupted = clickact(qpages,sr.nextInt(70)+1,sr.nextInt(15)+1);
						if(interrupted == 1){
							return interrupted;
						}
						if(findexists(qpages+"(pressed)",10)) {
							wait(2.0);
							break;
						}
					}while(true);
					
					do{
						if(interrupted == 1){
							return 1;
						}
						if(QuestCheckBoxList.get(0).isSelected() && findexists("quest(exp3round)",0.5) == true){
							if(assisthandle != null){
								Checkminimized.minimizedcheckandshow(assisthwnd);
							}
							int questselected = 0;
							int questsuccess = 0;
							cacheCapture = CaptureByHandle.capture_printWindow(hwnd);
							gamepicrect = new Rectangle(cacheCapture.getMinX(),cacheCapture.getMinY(),cacheCapture.getWidth(),cacheCapture.getHeight());
							transcapture = new ScreenImage(gamepicrect,cacheCapture);
							picregion = new Region(cacheCapture.getMinX(),cacheCapture.getMinY(),cacheCapture.getWidth(),cacheCapture.getHeight());
							f = new Finder(transcapture,picregion);
							targetpattern = new Pattern(this.getClass().getResource("/res/quest(exp3round).png")).similar((float) 0.90);
							f.find(targetpattern);
							if(f.hasNext()){
								questnotfound[0] = false;
								questnotfound[1] = false;
								String targetstringexp = f.next().getTarget().toStringShort();
								int checky = Integer.parseInt(targetstringexp.substring(targetstringexp.indexOf(",", 2)+1, targetstringexp.length()-1));
								targetpattern = new Pattern(this.getClass().getResource("/res/questselected.png")).similar((float) 0.90);
								f.findAll(targetpattern);
								if(f.hasNext()){
									while(f.hasNext()){
										expaccept[0] = 1;
										String targetstring2 = f.next().getTarget().toStringShort();
										if(Integer.parseInt(targetstring2.substring(targetstring2.indexOf(",", 2)+1, targetstring2.length()-1))< checky+55 && Integer.parseInt(targetstring2.substring(targetstring2.indexOf(",", 2)+1, targetstring2.length()-1)) > checky){
											questselected = 1;
											Platform.runLater(new Runnable() {                          
												@Override
												public void run() {
													LocalDateTime dateTime = LocalDateTime.now();
													try{
														logarea.appendText(String.format("%s: Status: (%s) is already selected.\n",datetime(dateTime)
												           ,"quest(exp3round)"));
														}finally{
															if(savelog.isSelected() == true){
																try {
																	logwriter.write(String.format("%s: Status: (%s) is already selected.\n",datetime(dateTime)
																	           ,"quest(exp3round)"));
																	logwriter.newLine();
																	logwriter.flush();
																} catch (IOException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}
															}
														}
												}
											});
											break;
										}
									}
								}
								if(questselected == 0){
									targetpattern = new Pattern(this.getClass().getResource("/res/questsuccess.png")).targetOffset(-sr.nextInt(500)+1, -sr.nextInt(25)+1).similar((float) 0.80);
									f.findAll(targetpattern);
									if(f.hasNext()){
										while(f.hasNext()){
											expaccept[0] = 0;
											String targetstring2 = f.next().getTarget().toStringShort();
											if(Integer.parseInt(targetstring2.substring(targetstring2.indexOf(",", 2)+1, targetstring2.length()-1))< checky+55 && Integer.parseInt(targetstring2.substring(targetstring2.indexOf(",", 2)+1, targetstring2.length()-1)) > checky){
												questsuccess = 1;
												Platform.runLater(new Runnable() {                          
													@Override
													public void run() {
														LocalDateTime dateTime = LocalDateTime.now();
														try{
															logarea.appendText(String.format("%s: Status: (%s) is success.\n",datetime(dateTime)
																	,"quest(exp3round)"));
														}finally{
															if(savelog.isSelected() == true){
																try {
																	logwriter.write(String.format("%s: Status: (%s) is success.\n",datetime(dateTime)
																			,"quest(exp3round)"));
																	logwriter.newLine();
																	logwriter.flush();
																} catch (IOException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}
															}
														}
													}
												});
												wait(clickoffsettime);
												PostMessage.clickbypostmessage(hwndclick, Integer.parseInt(targetstringexp.substring(2, targetstringexp.indexOf(",", 2)))+sr.nextInt(500) , Integer.parseInt(targetstringexp.substring(targetstringexp.indexOf(",", 2)+1, targetstringexp.length()-1))+sr.nextInt(45));
												Platform.runLater(new Runnable() {                          
													@Override
													public void run() {
														LocalDateTime dateTime = LocalDateTime.now();
														try{
															logarea.appendText(String.format("%s: click on (%s)\n",datetime(dateTime)
																	,"quest(exp3round)"));
														}finally{
															if(savelog.isSelected() == true){
																try {
																	logwriter.write(String.format("%s: click on (%s)\n",datetime(dateTime)
																			,"quest(exp3round)"));
																	logwriter.newLine();
																	logwriter.flush();
																} catch (IOException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}
															}
														}
													}
												});
												wait(5.0);
												do{
													interrupted = clickact("questreward",sr.nextInt(45),sr.nextInt(10));
													if(interrupted == 1){
														return interrupted;
													}
													wait(3.0);
												}while(findexists("questreward",3));
												Platform.runLater(new Runnable() {                          
													@Override
													public void run() {
														try{
															QuestCheckBoxList.get(0).setSelected(false);
														}finally{
														}
													}
												});
												wait(5.0);
												break;
											}
										}
									}
									if(questsuccess == 0){
										expaccept[0] = 1;
										wait(clickoffsettime);
										PostMessage.clickbypostmessage(hwndclick, Integer.parseInt(targetstringexp.substring(2, targetstringexp.indexOf(",", 2)))+sr.nextInt(500) , Integer.parseInt(targetstringexp.substring(targetstringexp.indexOf(",", 2)+1, targetstringexp.length()-1))+sr.nextInt(45));
										Platform.runLater(new Runnable() {                          
											@Override
											public void run() {
												LocalDateTime dateTime = LocalDateTime.now();
												try{
													logarea.appendText(String.format("%s: Status: (%s) accept.\n",datetime(dateTime)
											           ,"quest(exp3round)"));
													}finally{
														if(savelog.isSelected() == true){
															try {
																logwriter.write(String.format("%s: Status: (%s) accept.\n",datetime(dateTime)
																           ,"quest(exp3round)"));
																logwriter.newLine();
																logwriter.flush();
															} catch (IOException e) {
																// TODO Auto-generated catch block
																e.printStackTrace();
															}
														}
													}
											}
										});
										wait(5.0);
									}
								}
							}
						}
						if(QuestCheckBoxList.get(1).isSelected() && findexists("quest(exp10round)",0.1) == true){
							if(assisthandle != null){
								Checkminimized.minimizedcheckandshow(assisthwnd);
							}
							int questselected = 0;
							int questsuccess = 0;
							cacheCapture = CaptureByHandle.capture_printWindow(hwnd);
							gamepicrect = new Rectangle(cacheCapture.getMinX(),cacheCapture.getMinY(),cacheCapture.getWidth(),cacheCapture.getHeight());
							transcapture = new ScreenImage(gamepicrect,cacheCapture);
							picregion = new Region(cacheCapture.getMinX(),cacheCapture.getMinY(),cacheCapture.getWidth(),cacheCapture.getHeight());
							f = new Finder(transcapture,picregion);
							targetpattern = new Pattern(this.getClass().getResource("/res/quest(exp10round).png")).similar((float) 0.90);
							f.find(targetpattern);
							if(f.hasNext()){
								questnotfound[1] = false;
								String targetstringexp = f.next().getTarget().toStringShort();
								int checky = Integer.parseInt(targetstringexp.substring(targetstringexp.indexOf(",", 2)+1, targetstringexp.length()-1));
								targetpattern = new Pattern(this.getClass().getResource("/res/questselected.png")).similar((float) 0.90);
								f.findAll(targetpattern);
								if(f.hasNext()){
									while(f.hasNext()){
										expaccept[1] = 1;
										String targetstring2 = f.next().getTarget().toStringShort();
										if(Integer.parseInt(targetstring2.substring(targetstring2.indexOf(",", 2)+1, targetstring2.length()-1))< checky+55 && Integer.parseInt(targetstring2.substring(targetstring2.indexOf(",", 2)+1, targetstring2.length()-1)) > checky){
											questselected = 1;
											Platform.runLater(new Runnable() {                          
												@Override
												public void run() {
													LocalDateTime dateTime = LocalDateTime.now();
													try{
														logarea.appendText(String.format("%s: Status: (%s) is already selected.\n",datetime(dateTime)
												           ,"quest(exp10round)"));
														}finally{
															if(savelog.isSelected() == true){
																try {
																	logwriter.write(String.format("%s: Status: (%s) is already selected.\n",datetime(dateTime)
																	           ,"quest(exp10round)"));
																	logwriter.newLine();
																	logwriter.flush();
																} catch (IOException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}
															}
														}
												}
											});
											break;
										}
										
									}
								}
								if(questselected == 0){
									targetpattern = new Pattern(this.getClass().getResource("/res/questsuccess.png")).targetOffset(-sr.nextInt(500)+1, -sr.nextInt(25)+1).similar((float) 0.80);
									f.findAll(targetpattern);
									if(f.hasNext()){
										while(f.hasNext()){
											expaccept[1] = 0;
											String targetstring2 = f.next().getTarget().toStringShort();
											if(Integer.parseInt(targetstring2.substring(targetstring2.indexOf(",", 2)+1, targetstring2.length()-1))< checky+55 && Integer.parseInt(targetstring2.substring(targetstring2.indexOf(",", 2)+1, targetstring2.length()-1)) > checky){
												questsuccess = 1;
												Platform.runLater(new Runnable() {                          
													@Override
													public void run() {
														LocalDateTime dateTime = LocalDateTime.now();
														try{
															logarea.appendText(String.format("%s: Status: (%s) is success.\n",datetime(dateTime)
																	,"quest(exp10round)"));
														}finally{
															if(savelog.isSelected() == true){
																try {
																	logwriter.write(String.format("%s: Status: (%s) is success.\n",datetime(dateTime)
																			,"quest(exp10round)"));
																	logwriter.newLine();
																	logwriter.flush();
																} catch (IOException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}
															}
														}
													}
												});
												wait(clickoffsettime);
												PostMessage.clickbypostmessage(hwndclick, Integer.parseInt(targetstringexp.substring(2, targetstringexp.indexOf(",", 2)))+sr.nextInt(500) , Integer.parseInt(targetstringexp.substring(targetstringexp.indexOf(",", 2)+1, targetstringexp.length()-1))+sr.nextInt(45));
												Platform.runLater(new Runnable() {                          
													@Override
													public void run() {
														LocalDateTime dateTime = LocalDateTime.now();
														try{
															logarea.appendText(String.format("%s: click on (%s)\n",datetime(dateTime)
																	,"quest(exp10round)"));
														}finally{
															if(savelog.isSelected() == true){
																try {
																	logwriter.write(String.format("%s: click on (%s)\n",datetime(dateTime)
																			,"quest(exp10round)"));
																	logwriter.newLine();
																	logwriter.flush();
																} catch (IOException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}
															}
														}
													}
												});
												wait(5.0);
												do{
													interrupted = clickact("questreward",sr.nextInt(45),sr.nextInt(10));
													if(interrupted == 1){
														return interrupted;
													}
													wait(3.0);
												}while(findexists("questreward",3));
												Platform.runLater(new Runnable() {                          
													@Override
													public void run() {
														try{
															QuestCheckBoxList.get(1).setSelected(false);
														}finally{
														}
													}
												});
												wait(5.0);
												break;
											}
										}
									}
									if(questsuccess == 0){
										expaccept[1] = 1;
										wait(clickoffsettime);
										PostMessage.clickbypostmessage(hwndclick, Integer.parseInt(targetstringexp.substring(2, targetstringexp.indexOf(",", 2)))+sr.nextInt(500) , Integer.parseInt(targetstringexp.substring(targetstringexp.indexOf(",", 2)+1, targetstringexp.length()-1))+sr.nextInt(45));
										Platform.runLater(new Runnable() {                          
											@Override
											public void run() {
												LocalDateTime dateTime = LocalDateTime.now();
												try{
													logarea.appendText(String.format("%s: Status: (%s) accept.\n",datetime(dateTime)
											           ,"quest(exp10round)"));
													}finally{
														if(savelog.isSelected() == true){
															try {
																logwriter.write(String.format("%s: Status: (%s) accept.\n",datetime(dateTime)
																           ,"quest(exp10round)"));
																logwriter.newLine();
																logwriter.flush();
															} catch (IOException e) {
																// TODO Auto-generated catch block
																e.printStackTrace();
															}
														}
													}
											}
										});
										wait(5.0);
									}
								}
							}
						}
						if(QuestCheckBoxList.get(2).isSelected() && findexists("quest(exp30round)",0.1) == true){
							if(assisthandle != null){
								Checkminimized.minimizedcheckandshow(assisthwnd);
							}
							int questselected = 0;
							int questsuccess = 0;
							cacheCapture = CaptureByHandle.capture_printWindow(hwnd);
							gamepicrect = new Rectangle(cacheCapture.getMinX(),cacheCapture.getMinY(),cacheCapture.getWidth(),cacheCapture.getHeight());
							transcapture = new ScreenImage(gamepicrect,cacheCapture);
							picregion = new Region(cacheCapture.getMinX(),cacheCapture.getMinY(),cacheCapture.getWidth(),cacheCapture.getHeight());
							f = new Finder(transcapture,picregion);
							targetpattern = new Pattern(this.getClass().getResource("/res/quest(exp30round).png")).similar((float) 0.90);
							f.find(targetpattern);
							if(f.hasNext()){
								questnotfound[2] = false;
								String targetstringexp = f.next().getTarget().toStringShort();
								int checky = Integer.parseInt(targetstringexp.substring(targetstringexp.indexOf(",", 2)+1, targetstringexp.length()-1));
								targetpattern = new Pattern(this.getClass().getResource("/res/questselected.png")).similar((float) 0.90);
								f.findAll(targetpattern);
								if(f.hasNext()){
									while(f.hasNext()){
										expaccept[2] = 1;
										String targetstring2 = f.next().getTarget().toStringShort();
										if(Integer.parseInt(targetstring2.substring(targetstring2.indexOf(",", 2)+1, targetstring2.length()-1))< checky+55 && Integer.parseInt(targetstring2.substring(targetstring2.indexOf(",", 2)+1, targetstring2.length()-1)) > checky){
											questselected = 1;
											Platform.runLater(new Runnable() {                          
												@Override
												public void run() {
													LocalDateTime dateTime = LocalDateTime.now();
													try{
														logarea.appendText(String.format("%s: Status: (%s) is already selected.\n",datetime(dateTime)
												           ,"quest(exp30round)"));
														}finally{
															if(savelog.isSelected() == true){
																try {
																	logwriter.write(String.format("%s: Status: (%s) is already selected.\n",datetime(dateTime)
																	           ,"quest(exp30round)"));
																	logwriter.newLine();
																	logwriter.flush();
																} catch (IOException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}
															}
														}
												}
											});
											break;
										}
									}
								}
								if(questselected == 0){
									targetpattern = new Pattern(this.getClass().getResource("/res/questsuccess.png")).targetOffset(-sr.nextInt(500)+1, -sr.nextInt(25)+1).similar((float) 0.80);
									f.findAll(targetpattern);
									if(f.hasNext()){
										while(f.hasNext()){
											expaccept[2] = 0;
											String targetstring2 = f.next().getTarget().toStringShort();
											if(Integer.parseInt(targetstring2.substring(targetstring2.indexOf(",", 2)+1, targetstring2.length()-1))< checky+55 && Integer.parseInt(targetstring2.substring(targetstring2.indexOf(",", 2)+1, targetstring2.length()-1)) > checky){
												questsuccess = 1;
												Platform.runLater(new Runnable() {                          
													@Override
													public void run() {
														LocalDateTime dateTime = LocalDateTime.now();
														try{
															logarea.appendText(String.format("%s: Status: (%s) is success.\n",datetime(dateTime)
																	,"quest(exp30round)"));
														}finally{
															if(savelog.isSelected() == true){
																try {
																	logwriter.write(String.format("%s: Status: (%s) is success.\n",datetime(dateTime)
																			,"quest(exp30round)"));
																	logwriter.newLine();
																	logwriter.flush();
																} catch (IOException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}
															}
														}
													}
												});
												wait(clickoffsettime);
												PostMessage.clickbypostmessage(hwndclick, Integer.parseInt(targetstringexp.substring(2, targetstringexp.indexOf(",", 2)))+sr.nextInt(500) , Integer.parseInt(targetstringexp.substring(targetstringexp.indexOf(",", 2)+1, targetstringexp.length()-1))+sr.nextInt(45));
												Platform.runLater(new Runnable() {                          
													@Override
													public void run() {
														LocalDateTime dateTime = LocalDateTime.now();
														try{
															logarea.appendText(String.format("%s: click on (%s)\n",datetime(dateTime)
																	,"quest(exp30round)"));
														}finally{
															if(savelog.isSelected() == true){
																try {
																	logwriter.write(String.format("%s: click on (%s)\n",datetime(dateTime)
																			,"quest(exp30round)"));
																	logwriter.newLine();
																	logwriter.flush();
																} catch (IOException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}
															}
														}
													}
												});
												wait(5.0);
												do{
													interrupted = clickact("questreward",sr.nextInt(45),sr.nextInt(10));
													if(interrupted == 1){
														return interrupted;
													}
													wait(3.0);
												}while(findexists("questreward",3));
												Platform.runLater(new Runnable() {                          
													@Override
													public void run() {
														try{
															QuestCheckBoxList.get(2).setSelected(false);
														}finally{
														}
													}
												});
												wait(5.0);
												break;
											}
										}
									}
									if(questsuccess == 0){
										expaccept[2] = 1;
										wait(clickoffsettime);
										PostMessage.clickbypostmessage(hwndclick, Integer.parseInt(targetstringexp.substring(2, targetstringexp.indexOf(",", 2)))+sr.nextInt(500) , Integer.parseInt(targetstringexp.substring(targetstringexp.indexOf(",", 2)+1, targetstringexp.length()-1))+sr.nextInt(45));
										Platform.runLater(new Runnable() {                          
											@Override
											public void run() {
												LocalDateTime dateTime = LocalDateTime.now();
												try{
													logarea.appendText(String.format("%s: Status: (%s) accept.\n",datetime(dateTime)
											           ,"quest(exp30round)"));
													}finally{
														if(savelog.isSelected() == true){
															try {
																logwriter.write(String.format("%s: Status: (%s) accept.\n",datetime(dateTime)
																           ,"quest(exp30round)"));
																logwriter.newLine();
																logwriter.flush();
															} catch (IOException e) {
																// TODO Auto-generated catch block
																e.printStackTrace();
															}
														}
													}
											}
										});
										wait(5.0);
									}
								}
							}
						}
						if(QuestCheckBoxList.get(3).isSelected() && findexists("quest(tokyotrip1round)",0.1) == true){
							if(assisthandle != null){
								Checkminimized.minimizedcheckandshow(assisthwnd);
							}
							int questselected = 0;
							int questsuccess = 0;
							cacheCapture = CaptureByHandle.capture_printWindow(hwnd);
							gamepicrect = new Rectangle(cacheCapture.getMinX(),cacheCapture.getMinY(),cacheCapture.getWidth(),cacheCapture.getHeight());
							transcapture = new ScreenImage(gamepicrect,cacheCapture);
							picregion = new Region(cacheCapture.getMinX(),cacheCapture.getMinY(),cacheCapture.getWidth(),cacheCapture.getHeight());
							f = new Finder(transcapture,picregion);
							targetpattern = new Pattern(this.getClass().getResource("/res/quest(tokyotrip1round).png")).similar((float) 0.90);
							f.find(targetpattern);
							if(f.hasNext()){
								questnotfound[3] = false;
								questnotfound[4] = false;
								String targetstringexp = f.next().getTarget().toStringShort();
								int checky = Integer.parseInt(targetstringexp.substring(targetstringexp.indexOf(",", 2)+1, targetstringexp.length()-1));
								targetpattern = new Pattern(this.getClass().getResource("/res/questselected.png")).similar((float) 0.90);
								f.findAll(targetpattern);
								if(f.hasNext()){
									while(f.hasNext()){
										expaccept[3] = 1;
										String targetstring2 = f.next().getTarget().toStringShort();
										if(Integer.parseInt(targetstring2.substring(targetstring2.indexOf(",", 2)+1, targetstring2.length()-1))< checky+55 && Integer.parseInt(targetstring2.substring(targetstring2.indexOf(",", 2)+1, targetstring2.length()-1)) > checky){
											questselected = 1;
											Platform.runLater(new Runnable() {                          
												@Override
												public void run() {
													LocalDateTime dateTime = LocalDateTime.now();
													try{
														logarea.appendText(String.format("%s: Status: (%s) is already selected.\n",datetime(dateTime)
												           ,"quest(tokyotrip1round)"));
														}finally{
															if(savelog.isSelected() == true){
																try {
																	logwriter.write(String.format("%s: Status: (%s) is already selected.\n",datetime(dateTime)
																	           ,"quest(tokyotrip1round)"));
																	logwriter.newLine();
																	logwriter.flush();
																} catch (IOException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}
															}
														}
												}
											});
											break;
										}
									}
								}
								if(questselected == 0){
									targetpattern = new Pattern(this.getClass().getResource("/res/questsuccess.png")).targetOffset(-sr.nextInt(500)+1, -sr.nextInt(25)+1).similar((float) 0.80);
									f.findAll(targetpattern);
									if(f.hasNext()){
										while(f.hasNext()){
											expaccept[3] = 0;
											String targetstring2 = f.next().getTarget().toStringShort();
											if(Integer.parseInt(targetstring2.substring(targetstring2.indexOf(",", 2)+1, targetstring2.length()-1))< checky+55 && Integer.parseInt(targetstring2.substring(targetstring2.indexOf(",", 2)+1, targetstring2.length()-1)) > checky){
												questsuccess = 1;
												Platform.runLater(new Runnable() {                          
													@Override
													public void run() {
														LocalDateTime dateTime = LocalDateTime.now();
														try{
															logarea.appendText(String.format("%s: Status: (%s) is success.\n",datetime(dateTime)
																	,"quest(tokyotrip1round)"));
														}finally{
															if(savelog.isSelected() == true){
																try {
																	logwriter.write(String.format("%s: Status: (%s) is success.\n",datetime(dateTime)
																			,"quest(tokyotrip1round)"));
																	logwriter.newLine();
																	logwriter.flush();
																} catch (IOException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}
															}
														}
													}
												});
												wait(clickoffsettime);
												PostMessage.clickbypostmessage(hwndclick, Integer.parseInt(targetstringexp.substring(2, targetstringexp.indexOf(",", 2)))+sr.nextInt(500) , Integer.parseInt(targetstringexp.substring(targetstringexp.indexOf(",", 2)+1, targetstringexp.length()-1))+sr.nextInt(45));
												Platform.runLater(new Runnable() {                          
													@Override
													public void run() {
														LocalDateTime dateTime = LocalDateTime.now();
														try{
															logarea.appendText(String.format("%s: click on (%s)\n",datetime(dateTime)
																	,"quest(tokyotrip1round)"));
														}finally{
															if(savelog.isSelected() == true){
																try {
																	logwriter.write(String.format("%s: click on (%s)\n",datetime(dateTime)
																			,"quest(tokyotrip1round)"));
																	logwriter.newLine();
																	logwriter.flush();
																} catch (IOException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}
															}
														}
													}
												});
												wait(5.0);
												do{
													interrupted = clickact("questreward",sr.nextInt(45),sr.nextInt(10));
													if(interrupted == 1){
														return interrupted;
													}
													wait(3.0);
												}while(findexists("questreward",3));
												Platform.runLater(new Runnable() {                          
													@Override
													public void run() {
														try{
															QuestCheckBoxList.get(3).setSelected(false);
														}finally{
														}
													}
												});
												wait(5.0);
												break;
											}
										}
									}
									if(questsuccess == 0){
										expaccept[3] = 1;
										wait(clickoffsettime);
										PostMessage.clickbypostmessage(hwndclick, Integer.parseInt(targetstringexp.substring(2, targetstringexp.indexOf(",", 2)))+sr.nextInt(500) , Integer.parseInt(targetstringexp.substring(targetstringexp.indexOf(",", 2)+1, targetstringexp.length()-1))+sr.nextInt(45));
										Platform.runLater(new Runnable() {                          
											@Override
											public void run() {
												LocalDateTime dateTime = LocalDateTime.now();
												try{
													logarea.appendText(String.format("%s: Status: (%s) accept.\n",datetime(dateTime)
											           ,"quest(tokyotrip1round)"));
													}finally{
														if(savelog.isSelected() == true){
															try {
																logwriter.write(String.format("%s: Status: (%s) accept.\n",datetime(dateTime)
																           ,"quest(tokyotrip1round)"));
																logwriter.newLine();
																logwriter.flush();
															} catch (IOException e) {
																// TODO Auto-generated catch block
																e.printStackTrace();
															}
														}
													}
											}
										});
										wait(5.0);
									}
								}
							}
						}
						if(QuestCheckBoxList.get(4).isSelected() && findexists("quest(tokyotrip7round)",0.1) == true){
							if(assisthandle != null){
								Checkminimized.minimizedcheckandshow(assisthwnd);
							}
							int questselected = 0;
							int questsuccess = 0;
							cacheCapture = CaptureByHandle.capture_printWindow(hwnd);
							gamepicrect = new Rectangle(cacheCapture.getMinX(),cacheCapture.getMinY(),cacheCapture.getWidth(),cacheCapture.getHeight());
							transcapture = new ScreenImage(gamepicrect,cacheCapture);
							picregion = new Region(cacheCapture.getMinX(),cacheCapture.getMinY(),cacheCapture.getWidth(),cacheCapture.getHeight());
							f = new Finder(transcapture,picregion);
							targetpattern = new Pattern(this.getClass().getResource("/res/quest(tokyotrip7round).png")).similar((float) 0.90);
							f.find(targetpattern);
							if(f.hasNext()){
								questnotfound[4] = false;
								String targetstringexp = f.next().getTarget().toStringShort();
								int checky = Integer.parseInt(targetstringexp.substring(targetstringexp.indexOf(",", 2)+1, targetstringexp.length()-1));
								targetpattern = new Pattern(this.getClass().getResource("/res/questselected.png")).similar((float) 0.90);
								f.findAll(targetpattern);
								if(f.hasNext()){
									while(f.hasNext()){
										expaccept[4] = 1;
										String targetstring2 = f.next().getTarget().toStringShort();
										if(Integer.parseInt(targetstring2.substring(targetstring2.indexOf(",", 2)+1, targetstring2.length()-1))< checky+55 && Integer.parseInt(targetstring2.substring(targetstring2.indexOf(",", 2)+1, targetstring2.length()-1)) > checky){
											questselected = 1;
											Platform.runLater(new Runnable() {                          
												@Override
												public void run() {
													LocalDateTime dateTime = LocalDateTime.now();
													try{
														logarea.appendText(String.format("%s: Status: (%s) is already selected.\n",datetime(dateTime)
												           ,"quest(tokyotrip7round)"));
														}finally{
															if(savelog.isSelected() == true){
																try {
																	logwriter.write(String.format("%s: Status: (%s) is already selected.\n",datetime(dateTime)
																	           ,"quest(tokyotrip7round)"));
																	logwriter.newLine();
																	logwriter.flush();
																} catch (IOException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}
															}
														}
												}
											});
											break;
										}
									}
								}
								if(questselected == 0){
									targetpattern = new Pattern(this.getClass().getResource("/res/questsuccess.png")).targetOffset(-sr.nextInt(500)+1, -sr.nextInt(25)+1).similar((float) 0.80);
									f.findAll(targetpattern);
									if(f.hasNext()){
										while(f.hasNext()){
											expaccept[4] = 0;
											String targetstring2 = f.next().getTarget().toStringShort();
											if(Integer.parseInt(targetstring2.substring(targetstring2.indexOf(",", 2)+1, targetstring2.length()-1))< checky+55 && Integer.parseInt(targetstring2.substring(targetstring2.indexOf(",", 2)+1, targetstring2.length()-1)) > checky){
												questsuccess = 1;
												Platform.runLater(new Runnable() {                          
													@Override
													public void run() {
														LocalDateTime dateTime = LocalDateTime.now();
														try{
															logarea.appendText(String.format("%s: Status: (%s) is success.\n",datetime(dateTime)
																	,"quest(tokyotrip7round)"));
														}finally{
															if(savelog.isSelected() == true){
																try {
																	logwriter.write(String.format("%s: Status: (%s) is success.\n",datetime(dateTime)
																			,"quest(tokyotrip7round)"));
																	logwriter.newLine();
																	logwriter.flush();
																} catch (IOException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}
															}
														}
													}
												});
												wait(clickoffsettime);
												PostMessage.clickbypostmessage(hwndclick, Integer.parseInt(targetstringexp.substring(2, targetstringexp.indexOf(",", 2)))+sr.nextInt(500) , Integer.parseInt(targetstringexp.substring(targetstringexp.indexOf(",", 2)+1, targetstringexp.length()-1))+sr.nextInt(45));
												Platform.runLater(new Runnable() {                          
													@Override
													public void run() {
														LocalDateTime dateTime = LocalDateTime.now();
														try{
															logarea.appendText(String.format("%s: click on (%s)\n",datetime(dateTime)
																	,"quest(tokyotrip7round)"));
														}finally{
															if(savelog.isSelected() == true){
																try {
																	logwriter.write(String.format("%s: click on (%s)\n",datetime(dateTime)
																			,"quest(tokyotrip7round)"));
																	logwriter.newLine();
																	logwriter.flush();
																} catch (IOException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}
															}
														}
													}
												});
												wait(5.0);
												do{
													interrupted = clickact("questreward",sr.nextInt(45),sr.nextInt(10));
													if(interrupted == 1){
														return interrupted;
													}
													wait(3.0);
												}while(findexists("questreward",3));
												Platform.runLater(new Runnable() {                          
													@Override
													public void run() {
														try{
															QuestCheckBoxList.get(4).setSelected(false);
														}finally{
														}
													}
												});
												wait(5.0);
												break;
											}
										}
									}
									if(questsuccess == 0){
										expaccept[4] = 1;
										wait(clickoffsettime);
										PostMessage.clickbypostmessage(hwndclick, Integer.parseInt(targetstringexp.substring(2, targetstringexp.indexOf(",", 2)))+sr.nextInt(500) , Integer.parseInt(targetstringexp.substring(targetstringexp.indexOf(",", 2)+1, targetstringexp.length()-1))+sr.nextInt(45));
										Platform.runLater(new Runnable() {                          
											@Override
											public void run() {
												LocalDateTime dateTime = LocalDateTime.now();
												try{
													logarea.appendText(String.format("%s: Status: (%s) accept.\n",datetime(dateTime)
											           ,"quest(tokyotrip7round)"));
													}finally{
														if(savelog.isSelected() == true){
															try {
																logwriter.write(String.format("%s: Status: (%s) accept.\n",datetime(dateTime)
																           ,"quest(tokyotrip7round)"));
																logwriter.newLine();
																logwriter.flush();
															} catch (IOException e) {
																// TODO Auto-generated catch block
																e.printStackTrace();
															}
														}
													}
											}
										});
										wait(5.0);
									}
								}
							}
						}
						if(assisthandle != null){
							Checkminimized.minimizedcheckandshow(assisthwnd);
						}
						if(interrupted == 1){
							return 1;
						}
						
						while(findexists("questreward",2)){
							interrupted = clickact("questreward",sr.nextInt(45),sr.nextInt(10));
							if(interrupted == 1){
								return interrupted;
							}
							wait(3.0);
						}
						
						cacheCapture = CaptureByHandle.capture_printWindow(hwnd);
						gamepicrect = new Rectangle(cacheCapture.getMinX(),cacheCapture.getMinY(),cacheCapture.getWidth(),cacheCapture.getHeight());
						transcapture = new ScreenImage(gamepicrect,cacheCapture);
						picregion = new Region(cacheCapture.getMinX(),cacheCapture.getMinY(),cacheCapture.getWidth(),cacheCapture.getHeight());
						f = new Finder(transcapture,picregion);
						targetpattern = new Pattern(this.getClass().getResource("/res/questnextpage.png")).similar((float) 0.90);
						f.find(targetpattern);
						if(f.hasNext() == true){
							f.find(targetpattern);
							interrupted = clickact("questnextpage",sr.nextInt(10),sr.nextInt(7));
							if(interrupted == 1){
								return interrupted;
							}
							wait(5.0);
						}
						else if(findexists("questnextpage",5)){
							if(assisthandle != null){
								Checkminimized.minimizedcheckandshow(assisthwnd);
							}
							cacheCapture = CaptureByHandle.capture_printWindow(hwnd);
							gamepicrect = new Rectangle(cacheCapture.getMinX(),cacheCapture.getMinY(),cacheCapture.getWidth(),cacheCapture.getHeight());
							transcapture = new ScreenImage(gamepicrect,cacheCapture);
							picregion = new Region(cacheCapture.getMinX(),cacheCapture.getMinY(),cacheCapture.getWidth(),cacheCapture.getHeight());
							f = new Finder(transcapture,picregion);
							targetpattern = new Pattern(this.getClass().getResource("/res/questnextpage.png")).similar((float) 0.90);
							f.find(targetpattern);
							if(f.hasNext() == true){
								f.find(targetpattern);
								interrupted = clickact("questnextpage",sr.nextInt(10),sr.nextInt(7));
								if(interrupted == 1){
									return interrupted;
								}
								wait(5.0);
							}
						}
					}while(f.hasNext());
					
				}
				
				interrupted = clickact("back",sr.nextInt(40)+1,sr.nextInt(13)+1);
				if(interrupted == 1){
					return interrupted;
				}
				for(CheckBox quest:QuestCheckBoxList){
					if(questnotfound[QuestCheckBoxList.indexOf(quest)] == true){
						quest.setSelected(false);
					}
				}
				for(int i = 0;i<expaccept.length;i++) {
					if(expaccept[i] == 1) {
						QuestAcceptList.get(i).setSelected(true);
					}
					else {
						QuestAcceptList.get(i).setSelected(false);
					}
				}
				
				BufferedReader br = null;
				BufferedWriter bw = null;
				try {
					br = new BufferedReader(new InputStreamReader(new FileInputStream("setting.ini"), "UTF-8")); 
					bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("settingtemp.ini"), "UTF-8"));
				  String line;
				  while ((line = br.readLine()) != null) {
				    if (line.contains("questexp3round")){
				    	if(QuestCheckBoxList.get(0).isSelected() == false) {
					        line = line.replace(line.substring(line.lastIndexOf("=")+1, line.length()),"false");
					        bw.write(line);
					        bw.newLine();
				    	}
				    	else {
				    		line = line.replace(line.substring(line.lastIndexOf("=")+1, line.length()),"true");
						    bw.write(line);
						    bw.newLine();
				    	}
				    }
				    else if(line.contains("questexp10round")){
				    	if(QuestCheckBoxList.get(1).isSelected() == false) {
					        line = line.replace(line.substring(line.lastIndexOf("=")+1, line.length()),"false");
					        bw.write(line);
					        bw.newLine();
				    	}
				    	else {
				    		line = line.replace(line.substring(line.lastIndexOf("=")+1, line.length()),"true");
						    bw.write(line);
						    bw.newLine();
				    	}
				    	
				    }
				    else if(line.contains("questexp30round")){
				    	if(QuestCheckBoxList.get(2).isSelected() == false) {
					        line = line.replace(line.substring(line.lastIndexOf("=")+1, line.length()),"false");
					        bw.write(line);
					        bw.newLine();
				    	}
				    	else {
				    		line = line.replace(line.substring(line.lastIndexOf("=")+1, line.length()),"true");
						    bw.write(line);
						    bw.newLine();
				    	}
				    	
				    }
				    else if(line.contains("exptokyotrip1rounds")){
				    	if(QuestCheckBoxList.get(3).isSelected() == false) {
					        line = line.replace(line.substring(line.lastIndexOf("=")+1, line.length()),"false");
					        bw.write(line);
					        bw.newLine();
				    	}
				    	else {
				    		line = line.replace(line.substring(line.lastIndexOf("=")+1, line.length()),"true");
						    bw.write(line);
						    bw.newLine();
				    	}
				    	
				    }
				    else if(line.contains("exptokyotrip7rounds")){
				    	if(QuestCheckBoxList.get(4).isSelected() == false) {
					        line = line.replace(line.substring(line.lastIndexOf("=")+1, line.length()),"false");
					        bw.write(line);
					        bw.newLine();
				    	}
				    	else {
				    		line = line.replace(line.substring(line.lastIndexOf("=")+1, line.length()),"true");
						    bw.write(line);
						    bw.newLine();
				    	}
				    	
				    }
				    else {
				    	bw.write(line);
				    	bw.newLine();
				    }
				  }
				  bw.close();
				}
				catch (Exception e) {
				  return 1;
				} finally {
				  try {
				    if(br != null){
				      br.close();
				    }
				  } catch (IOException e) {
				  }
				}
				// Once everything is complete, delete old file..
				File oldFile = new File("setting.ini");
				oldFile.delete();

				// And rename tmp file's name to old file name
				File newFile = new File("settingtemp.ini");
				newFile.renameTo(oldFile);

				
				try {
					br = new BufferedReader(new InputStreamReader(new FileInputStream("mission.ini"), "UTF-8")); 
					bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("missiontemp.ini"), "UTF-8"));
				  String line;
				  while ((line = br.readLine()) != null) {
				    if (line.contains("questexp3roundaccept")){
					        line = line.replace(line.substring(line.lastIndexOf("=")+1, line.length()),String.valueOf(expaccept[0]));
					        bw.write(line);
					        bw.newLine();
				    }
				    else if(line.contains("questexp10roundaccept")){
				    	 line = line.replace(line.substring(line.lastIndexOf("=")+1, line.length()),String.valueOf(expaccept[1]));
					        bw.write(line);
					        bw.newLine();
				    }
				    else if(line.contains("questexp30roundaccept")){
				    	 line = line.replace(line.substring(line.lastIndexOf("=")+1, line.length()),String.valueOf(expaccept[2]));
					        bw.write(line);
					        bw.newLine();
				    }
				    else if(line.contains("exptokyotrip1roundsaccept")){
				    	 line = line.replace(line.substring(line.lastIndexOf("=")+1, line.length()),String.valueOf(expaccept[3]));
					        bw.write(line);
					        bw.newLine();
				    }
				    else if(line.contains("exptokyotrip7roundsaccept")){
				    	 line = line.replace(line.substring(line.lastIndexOf("=")+1, line.length()),String.valueOf(expaccept[4]));
					        bw.write(line);
					        bw.newLine();
				    }
				    else {
				    	bw.write(line);
				    	bw.newLine();
				    }
				  }
				  bw.close();
				}
				catch (Exception e) {
				  return 1;
				} finally {
				  try {
				    if(br != null){
				      br.close();
				    }
				  } catch (IOException e) {
				  }
				}
				// Once everything is complete, delete old file..
				oldFile = new File("mission.ini");
				oldFile.delete();

				// And rename tmp file's name to old file name
				newFile = new File("missiontemp.ini");
				newFile.renameTo(oldFile);
			}
		}
		return 0;
	}
	private int retry() throws InterruptedException {
		if(interrupted == 1) {
			return 1;
		}
		BufferedImage cacheCapture;
		Rectangle gamepicrect;
		ScreenImage transcapture;
		Region picregion;
		Finder f;
		Platform.runLater(new Runnable() {                          
			@Override
			public void run() {
				LocalDateTime dateTime = LocalDateTime.now();
				try{
					logarea.appendText(datetime(dateTime)
			            + ": Error: Fail to click target pattern. Retrying.\n");
					}finally{
						if(savelog.isSelected() == true){
							try {
								logwriter.write(datetime(dateTime)
						            + ": Error: Fail to click target pattern. Retrying.");
								logwriter.newLine();
								logwriter.flush();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
			}
		});
		if(findexists("next",0.2) == true || (findexists("back",0.1) == false && findexists("repair",0.1) == false && findexists("home",0.1) == false)){
			int randclickx = sr.nextInt(81)+6;
			int randclicky = sr.nextInt(76)+5;
			if(interrupted == 1){
				return 1;
			}
			PostMessage.clickbypostmessage(hwndclick,randclickx ,randclicky);
			Platform.runLater(new Runnable() {                          
				@Override
				public void run() {
					LocalDateTime dateTime = LocalDateTime.now();
					try{
						logarea.appendText(String.format("%s: click on (clicktry)(x: %d,y: %d)\n",datetime(dateTime)
				           ,randclickx,randclicky));
						}finally{
							if(savelog.isSelected() == true){
								try {
									logwriter.write(String.format("%s: click on (clicktry)(x: %d,y: %d)\n",datetime(dateTime)
							           ,randclickx,randclicky));
									logwriter.newLine();
									logwriter.flush();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
				}
			});
			return 1;
		}
		if(findexists("back",0.2) == true){
			if(assisthandle != null){
				Checkminimized.minimizedcheckandshow(assisthwnd);
			}
			cacheCapture = CaptureByHandle.capture_printWindow(hwnd);
			gamepicrect = new Rectangle(cacheCapture.getMinX(),cacheCapture.getMinY(),cacheCapture.getWidth(),cacheCapture.getHeight());
			transcapture = new ScreenImage(gamepicrect,cacheCapture);
			picregion = new Region(cacheCapture.getMinX(),cacheCapture.getMinY(),cacheCapture.getWidth(),cacheCapture.getHeight());
			f = new Finder(transcapture,picregion);
			Pattern back = new Pattern(this.getClass().getResource("/res/back.png")).targetOffset(sr.nextInt(40)+1,sr.nextInt(13)+1).similar((float) 0.90);
			f.find(back);
			String targetstring = f.next().getTarget().toStringShort();
			wait(clickoffsettime);
			if(interrupted == 1){
				return 1;
			}
			PostMessage.clickbypostmessage(hwndclick,Integer.parseInt(targetstring.substring(2, targetstring.indexOf(",", 2))) , Integer.parseInt(targetstring.substring(targetstring.indexOf(",", 2)+1, targetstring.length()-1)));
			Platform.runLater(new Runnable() {                          
				@Override
				public void run() {
					LocalDateTime dateTime = LocalDateTime.now();
					try{
						logarea.appendText(String.format("%s: click on (back)(x: %d,y: %d)\n",datetime(dateTime)
				           ,Integer.parseInt(targetstring.substring(2, targetstring.indexOf(",", 2))),Integer.parseInt(targetstring.substring(targetstring.indexOf(",", 2)+1, targetstring.length()-1))));
						}finally{
							if(savelog.isSelected() == true){
								try {
									logwriter.write(String.format("%s: click on (back)(x: %d,y: %d)\n",datetime(dateTime)
							           ,Integer.parseInt(targetstring.substring(2, targetstring.indexOf(",", 2))),Integer.parseInt(targetstring.substring(targetstring.indexOf(",", 2)+1, targetstring.length()-1))));
									logwriter.newLine();
									logwriter.flush();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
				}
			});
			return 1;
		}
		if(sr.nextInt(2) == 0){
			if(findexists("repair",0.2) == true){
				if(assisthandle != null){
					Checkminimized.minimizedcheckandshow(assisthwnd);
				}
				cacheCapture = CaptureByHandle.capture_printWindow(hwnd);
				gamepicrect = new Rectangle(cacheCapture.getMinX(),cacheCapture.getMinY(),cacheCapture.getWidth(),cacheCapture.getHeight());
				transcapture = new ScreenImage(gamepicrect,cacheCapture);
				picregion = new Region(cacheCapture.getMinX(),cacheCapture.getMinY(),cacheCapture.getWidth(),cacheCapture.getHeight());
				f = new Finder(transcapture,picregion);
				Pattern repair = new Pattern(this.getClass().getResource("/res/repair.png")).targetOffset(sr.nextInt(48)+1,sr.nextInt(13)+1).similar((float) 0.90);
				f.find(repair);
				String targetstring = f.next().getTarget().toStringShort();
				wait(clickoffsettime);
				if(interrupted == 1){
					return 1;
				}
				PostMessage.clickbypostmessage(hwndclick,Integer.parseInt(targetstring.substring(2, targetstring.indexOf(",", 2))) , Integer.parseInt(targetstring.substring(targetstring.indexOf(",", 2)+1, targetstring.length()-1)));
				Platform.runLater(new Runnable() {                          
					@Override
					public void run() {
						LocalDateTime dateTime = LocalDateTime.now();
						try{
							logarea.appendText(String.format("%s: click on (repair)(x: %d,y: %d)\n",datetime(dateTime)
					           ,Integer.parseInt(targetstring.substring(2, targetstring.indexOf(",", 2))),Integer.parseInt(targetstring.substring(targetstring.indexOf(",", 2)+1, targetstring.length()-1))));
							}finally{
								if(savelog.isSelected() == true){
									try {
										logwriter.write(String.format("%s: click on (repair)(x: %d,y: %d)\n",datetime(dateTime)
								           ,Integer.parseInt(targetstring.substring(2, targetstring.indexOf(",", 2))),Integer.parseInt(targetstring.substring(targetstring.indexOf(",", 2)+1, targetstring.length()-1))));
										logwriter.newLine();
										logwriter.flush();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
					}
				});
				if(findexists("inrepaircheck",waitfortargettimeout)){
					if(findexists("home",waitfortargettimeout)){
						if(assisthandle != null){
							Checkminimized.minimizedcheckandshow(assisthwnd);
						}
						cacheCapture = CaptureByHandle.capture_printWindow(hwnd);
						gamepicrect = new Rectangle(cacheCapture.getMinX(),cacheCapture.getMinY(),cacheCapture.getWidth(),cacheCapture.getHeight());
						transcapture = new ScreenImage(gamepicrect,cacheCapture);
						picregion = new Region(cacheCapture.getMinX(),cacheCapture.getMinY(),cacheCapture.getWidth(),cacheCapture.getHeight());
						f = new Finder(transcapture,picregion);
						Pattern home = new Pattern(this.getClass().getResource("/res/home.png")).targetOffset(sr.nextInt(9)+1,sr.nextInt(28)+1).similar((float) 0.90);
						f.find(home);
						String targetstring2 = f.next().getTarget().toStringShort();
						wait(2.0);
						if(interrupted == 1){
							return 1;
						}
						PostMessage.clickbypostmessage(hwndclick,Integer.parseInt(targetstring2.substring(2, targetstring2.indexOf(",", 2))) , Integer.parseInt(targetstring2.substring(targetstring2.indexOf(",", 2)+1, targetstring2.length()-1)));
						Platform.runLater(new Runnable() {                          
							@Override
							public void run() {
								LocalDateTime dateTime = LocalDateTime.now();
								try{
									logarea.appendText(String.format("%s: click on (home)(x: %d,y: %d)\n",datetime(dateTime)
							           ,Integer.parseInt(targetstring2.substring(2, targetstring2.indexOf(",", 2))),Integer.parseInt(targetstring2.substring(targetstring2.indexOf(",", 2)+1, targetstring2.length()-1))));
									}finally{
										if(savelog.isSelected() == true){
											try {
												logwriter.write(String.format("%s: click on (home)(x: %d,y: %d)\n",datetime(dateTime)
										           ,Integer.parseInt(targetstring2.substring(2, targetstring2.indexOf(",", 2))),Integer.parseInt(targetstring2.substring(targetstring2.indexOf(",", 2)+1, targetstring2.length()-1))));
												logwriter.newLine();
												logwriter.flush();
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
									}
							}
						});
					}
				}
			}
			else{
				if(findexists("home",waitfortargettimeout)){
					if(assisthandle != null){
						Checkminimized.minimizedcheckandshow(assisthwnd);
					}
					cacheCapture = CaptureByHandle.capture_printWindow(hwnd);
					gamepicrect = new Rectangle(cacheCapture.getMinX(),cacheCapture.getMinY(),cacheCapture.getWidth(),cacheCapture.getHeight());
					transcapture = new ScreenImage(gamepicrect,cacheCapture);
					picregion = new Region(cacheCapture.getMinX(),cacheCapture.getMinY(),cacheCapture.getWidth(),cacheCapture.getHeight());
					f = new Finder(transcapture,picregion);
					Pattern home = new Pattern(this.getClass().getResource("/res/home.png")).targetOffset(sr.nextInt(9)+1,sr.nextInt(28)+1).similar((float) 0.90);
					f.find(home);
					String targetstring2 = f.next().getTarget().toStringShort();
					wait(3.0);
					if(interrupted == 1){
						return 1;
					}
					PostMessage.clickbypostmessage(hwndclick,Integer.parseInt(targetstring2.substring(2, targetstring2.indexOf(",", 2))) , Integer.parseInt(targetstring2.substring(targetstring2.indexOf(",", 2)+1, targetstring2.length()-1)));
					Platform.runLater(new Runnable() {                          
						@Override
						public void run() {
							LocalDateTime dateTime = LocalDateTime.now();
							try{
								logarea.appendText(String.format("%s: click on (home)(x: %d,y: %d)\n",datetime(dateTime)
							         ,Integer.parseInt(targetstring2.substring(2, targetstring2.indexOf(",", 2))),Integer.parseInt(targetstring2.substring(targetstring2.indexOf(",", 2)+1, targetstring2.length()-1))));
								}finally{
									if(savelog.isSelected() == true){
										try {
											logwriter.write(String.format("%s: click on (home)(x: %d,y: %d)\n",datetime(dateTime)
										         ,Integer.parseInt(targetstring2.substring(2, targetstring2.indexOf(",", 2))),Integer.parseInt(targetstring2.substring(targetstring2.indexOf(",", 2)+1, targetstring2.length()-1))));
											logwriter.newLine();
											logwriter.flush();
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								}
						}
					});
				}
				
			}
			return 1;
		}
		else{
			int randclickx = sr.nextInt(81)+6;
			int randclicky = sr.nextInt(76)+5;
			if(interrupted == 1){
				return 1;
			}
			PostMessage.clickbypostmessage(hwndclick,randclickx ,randclicky);
			Platform.runLater(new Runnable() {                          
				@Override
				public void run() {
					LocalDateTime dateTime = LocalDateTime.now();
					try{
						logarea.appendText(String.format("%s: click on (clicktry)(x: %d,y: %d)\n",datetime(dateTime)
				           ,randclickx,randclicky));
						}finally{
							if(savelog.isSelected() == true){
								try {
									logwriter.write(String.format("%s: click on (clicktry)(x: %d,y: %d)\n",datetime(dateTime)
							           ,randclickx,randclicky));
									logwriter.newLine();
									logwriter.flush();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
				}
			});
			return 1;
		}
	
	}
	
	private void wait(double timeout){
		try{ 
	         Thread.sleep((long)(timeout*1000L)); 
	      } 
	      catch(InterruptedException e){ 
	         e.printStackTrace(); 
	      } 
	} 
	private int clickact(String target,int offsetx,int offsety) throws FindFailed, InterruptedException{
		if(assisthandle != null){
			Checkminimized.minimizedcheckandshow(assisthwnd);
		}
		if(interrupted == 1){
			return 1;
		}
		Pattern targetpattern = new Pattern(this.getClass().getResource("/res/" + target + ".png")).targetOffset(offsetx, offsety).similar((float) 0.90);
		if(findexists(target,waitfortargettimeout) == false){
			interrupted = retry();
			return interrupted;
		}
		else{
			if(assisthandle != null){
				Checkminimized.minimizedcheckandshow(assisthwnd);
			}
			if(interrupted == 1){
				return 1;
			}
			BufferedImage cacheCapture = CaptureByHandle.capture_printWindow(hwnd);
			Rectangle gamepicrect = new Rectangle(cacheCapture.getMinX(),cacheCapture.getMinY(),cacheCapture.getWidth(),cacheCapture.getHeight());
			ScreenImage transcapture = new ScreenImage(gamepicrect,cacheCapture);
			Region picregion = new Region(cacheCapture.getMinX(),cacheCapture.getMinY(),cacheCapture.getWidth(),cacheCapture.getHeight());
			Finder f = new Finder(transcapture,picregion);
			f.find(targetpattern);
			String targetstring = f.next().getTarget().toStringShort();
			wait(clickoffsettime);
			PostMessage.clickbypostmessage(hwndclick, Integer.parseInt(targetstring.substring(2, targetstring.indexOf(",", 2))) , Integer.parseInt(targetstring.substring(targetstring.indexOf(",", 2)+1, targetstring.length()-1)));
			Platform.runLater(new Runnable() {                          
				@Override
				public void run() {
					LocalDateTime dateTime = LocalDateTime.now();
					try{
						logarea.appendText(String.format("%s: click on (%s)(x: %d,y: %d)\n",datetime(dateTime)
				           ,target,Integer.parseInt(targetstring.substring(2, targetstring.indexOf(",", 2))),Integer.parseInt(targetstring.substring(targetstring.indexOf(",", 2)+1, targetstring.length()-1))));
						}finally{
							if(savelog.isSelected() == true){
								try {
									logwriter.write(String.format("%s: click on (%s)(x: %d,y: %d)\n",datetime(dateTime)
							           ,target,Integer.parseInt(targetstring.substring(2, targetstring.indexOf(",", 2))),Integer.parseInt(targetstring.substring(targetstring.indexOf(",", 2)+1, targetstring.length()-1))));
									logwriter.newLine();
									logwriter.flush();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
				}
			});
			if(interrupted == 1){
				return 1;
			}
			return 0;
		}

	}
	private int clickactCoordinate(String target,int offsetx,int offsety,int xchange,int ychange) throws FindFailed, InterruptedException{
		if(assisthandle != null){
			Checkminimized.minimizedcheckandshow(assisthwnd);
		}
		if(interrupted == 1){
			return 1;
		}
		Pattern targetpattern = new Pattern(this.getClass().getResource("/res/" + target + ".png")).targetOffset(offsetx, offsety).similar((float) 0.90);
		if(findexists(target,waitfortargettimeout) == false){
			interrupted = retry();
			return interrupted;
		}
		else{
			if(assisthandle != null){
				Checkminimized.minimizedcheckandshow(assisthwnd);
			}
			if(interrupted == 1){
				return 1;
			}
			BufferedImage cacheCapture = CaptureByHandle.capture_printWindow(hwnd);
			Rectangle gamepicrect = new Rectangle(cacheCapture.getMinX(),cacheCapture.getMinY(),cacheCapture.getWidth(),cacheCapture.getHeight());
			ScreenImage transcapture = new ScreenImage(gamepicrect,cacheCapture);
			Region picregion = new Region(cacheCapture.getMinX(),cacheCapture.getMinY(),cacheCapture.getWidth(),cacheCapture.getHeight());
			Finder f = new Finder(transcapture,picregion);
			f.find(targetpattern);
			String targetstring = f.next().getTarget().toStringShort();
			wait(clickoffsettime);
			PostMessage.clickbypostmessage(hwndclick, Integer.parseInt(targetstring.substring(2, targetstring.indexOf(",", 2)))+xchange , Integer.parseInt(targetstring.substring(targetstring.indexOf(",", 2)+1, targetstring.length()-1))+ychange);
			Platform.runLater(new Runnable() {                          
				@Override
				public void run() {
					LocalDateTime dateTime = LocalDateTime.now();
					try{
						logarea.appendText(String.format("%s: click on (%s)(x: %d,y: %d)\n",datetime(dateTime)
				           ,target,Integer.parseInt(targetstring.substring(2, targetstring.indexOf(",", 2)))+xchange,Integer.parseInt(targetstring.substring(targetstring.indexOf(",", 2)+1, targetstring.length()-1))+ychange));
						}finally{
							if(savelog.isSelected() == true){
								try {
									logwriter.write(String.format("%s: click on (%s)(x: %d,y: %d)\n",datetime(dateTime)
							           ,target,Integer.parseInt(targetstring.substring(2, targetstring.indexOf(",", 2)))+xchange,Integer.parseInt(targetstring.substring(targetstring.indexOf(",", 2)+1, targetstring.length()-1))+ychange));
									logwriter.newLine();
									logwriter.flush();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
				}
			});
			if(interrupted == 1){
				return 1;
			}
			return 0;
		}

	}
	private synchronized void startexp() throws InterruptedException{
		interrupted = 0;
		statrunning = 1;
		page = 1;
		Platform.runLater(new Runnable() {                          
			@Override
			public void run() {
				try{
					workingstatus.setText("working");
					workingstatus.setTextFill(Color.RED);
					}finally{
					}
			}
		});
		if(assisthandle != null){
			Checkminimized.minimizedcheckandshow(assisthwnd);
		}
		if(fleet2startstatus == 0 && fleet3startstatus == 0 && fleet4startstatus == 0){
			Platform.runLater(new Runnable() {                          
				@Override
				public void run() {
					try{
						workingstatus.setText("idle");
						workingstatus.setTextFill(Color.web("#009a00"));
						}finally{
						}
				}
			});
			statrunning = 0; return;
		}

		if(fleet2startstatus != 1 && fleet3startstatus != 1 && fleet4startstatus != 1){
			if(fleet2startstatus == 2 && numone.getValue()=="none"&& startone.isDisabled() == true){
				stopone(null);
			}
			if(fleet3startstatus == 2 && numtwo.getValue()=="none"&& starttwo.isDisabled() == true){
				stoptwo(null);
			}
			if(fleet4startstatus == 2 && numthree.getValue()=="none"&& startthree.isDisabled() == true){
				stopthree(null);
			}
			try {
				//TryWithHWND.main(null);
				do{
					if(findexists("expeditionreturn",2)){
						interrupted = clickact("expeditionreturn",-(sr.nextInt(400)+1),sr.nextInt(350)+1);
						if(interrupted == 1){
							statrunning = 0; return;
						}
						
						interrupted = clickact("next",-(sr.nextInt(600)+1),-(sr.nextInt(350)+1));
						if(interrupted == 1){
							statrunning = 0; return;
						}
						wait(clickoffsettime);
						PostMessage.clickbypostmessage(hwndclick,sr.nextInt(600)+1 ,sr.nextInt(350)+1);
						/**interrupted = clickact("next",-(sr.nextInt(600)+1),-(sr.nextInt(350)+1));
						if(interrupted == 1){
							statrunning = 0; return;
						}*/
					}
				}while(findexists("expeditionreturn",2));
				/*
				if(findexists("sorties",10) == true && findexists("expeditionreturn",2) == true){
					do{
						if(findexists("expeditionreturn",2)){
							interrupted = clickact("expeditionreturn",-(sr.nextInt(400)+1),sr.nextInt(350)+1);
							if(interrupted == 1){
								statrunning = 0; return;
							}
							
							interrupted = clickact("next",-(sr.nextInt(600)+1),-(sr.nextInt(350)+1));
							if(interrupted == 1){
								statrunning = 0; return;
							}
							wait(clickoffsettime);
							PostMessage.clickbypostmessage(hwndclick,sr.nextInt(600)+1 ,sr.nextInt(350)+1);
						}
						else {
							break;
						}
					}while(findexists("expeditionreturn",2));
				}
				else if(findexists("sorties",2) == false){
					interrupted = retry();
					if(interrupted == 1){
						statrunning = 0; return;
					}
				}*/
				interrupted = clickact("sorties",sr.nextInt(20)+1,sr.nextInt(20)+1);
				if(interrupted == 1){
					statrunning = 0; return;
				}
				
				interrupted = clickact("expedition",sr.nextInt(40)+1,sr.nextInt(40)+1);
				if(interrupted == 1){
					statrunning = 0; return;
				}
				
				if(fleet2startstatus == 2 && numone.getValue()!="none"&& startone.isDisabled() == true){
					interrupted = chooseExp(numone);
					if(interrupted == 1){
						statrunning = 0; return;
					}
					interrupted = clickact("decided",sr.nextInt(78)+1,sr.nextInt(14)+1);
					if(interrupted == 1){
						statrunning = 0; return;
					}
					if(findexists("expeditioning",1) == true){
						Platform.runLater(new Runnable() {                          
							@Override
							public void run() {
								LocalDateTime dateTime = LocalDateTime.now();
								try{
									logarea.appendText(String.format("%s: Error: %s is already expeditioning.\n",datetime(dateTime)
							        ,"fleet2"));
									}finally{
										if(savelog.isSelected() == true){
											try {
												logwriter.write(String.format("%s: Error: %s is already expeditioning.\n",datetime(dateTime)
										        ,"fleet2"));
												logwriter.newLine();
												logwriter.flush();
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
									}
							}
						});
						stopone(null);
						interrupted = clickact("home",sr.nextInt(9)+1,sr.nextInt(28)+1);
						if(interrupted == 1){
							statrunning = 0; return;
						}
						statrunning = 0; return;
					}
					/**Pattern space = new Pattern("space.png").targetOffset(sr.nextInt(50)+1,sr.nextInt(50)+1);
					Match spac = GameRegion.exists(space,1);
					GameRegion.mouseMove(spac);*/
					
					interrupted = expResupply(numone,"fleet2",statusone,countone,fleet2startstatus);
					if(interrupted == 1){
						statrunning = 0; return;
					}
					
					interrupted = clickact("expeditionstart",sr.nextInt(10)+1,sr.nextInt(8)+1);
					if(interrupted == 1){
						statrunning = 0; return;
					}
					Platform.runLater(new Runnable() {                          
						@Override
						public void run() {
							LocalDateTime dateTime = LocalDateTime.now();
							try{
								logarea.appendText(String.format("%s: %s expeditioning exp%s\n",datetime(dateTime)
						        ,"fleet2",numone.getValue()));
								}finally{
									if(savelog.isSelected() == true){
										try {
											logwriter.write(String.format("%s: %s expeditioning exp%s\n",datetime(dateTime)
									        ,"fleet2",numone.getValue()));
											logwriter.newLine();
											logwriter.flush();
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								}
						}
					});
					wait(5000L);
					fleet2currentexp = numone.getValue(); 
					
					int totalexptimecache = EXP[numone.getItems().indexOf(numone.getValue())] + expoffsettime;
					String finishhour = String.valueOf(totalexptimecache/3600);
					String finishminute = String.valueOf((totalexptimecache/60)-((totalexptimecache/3600)*60));
					String finishsec = String.valueOf(totalexptimecache-(Integer.parseInt(finishminute)*60)-(Integer.parseInt(finishhour)*3600));
					Platform.runLater(new Runnable() {                          
						@Override
						public void run() {
							try{
								hourone.setValue(finishhour);
								minuteone.setValue(finishminute);
								secondone.setValue(finishsec);
								statusone.setValue("expeditioning");
								}finally{
								}
						}
					});
					fleet2ExpCountChangable = 1;
					fleet2startstatus = 0;
				}
				if(fleet3startstatus == 2 && numtwo.getValue()!="none"&& starttwo.isDisabled() == true){
					interrupted = chooseExp(numtwo);
					if(interrupted == 1){
						statrunning = 0; return;
					}
					interrupted = clickact("decided",sr.nextInt(78)+1,sr.nextInt(14)+1);
					if(interrupted == 1){
						statrunning = 0; return;
					}
					/**Pattern space = new Pattern("space.png").targetOffset(sr.nextInt(50)+1,sr.nextInt(50)+1);
					Match spac = GameRegion.exists(space,1);
					GameRegion.mouseMove(spac);*/
					wait(1.0);
					do{
						interrupted = clickact("fleet3",sr.nextInt(10)+1,sr.nextInt(8)+1);
						if(interrupted == 1){
							statrunning = 0; return;
						}
					}while(findexists("fleet3(pressed)",2) == false);
					
					wait(1.0);
					if(findexists("expeditioning",1) == true){
						Platform.runLater(new Runnable() {                          
							@Override
							public void run() {
								LocalDateTime dateTime = LocalDateTime.now();
								try{
									logarea.appendText(String.format("%s: Error: %s is already expeditioning.\n",datetime(dateTime)
							        ,"fleet3"));
									}finally{
										if(savelog.isSelected() == true){
											try {
												logwriter.write(String.format("%s: Error: %s is already expeditioning.\n",datetime(dateTime)
										        ,"fleet3"));
												logwriter.newLine();
												logwriter.flush();
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
									}
							}
						});
						stoptwo(null);
						interrupted = clickact("home",sr.nextInt(9)+1,sr.nextInt(28)+1);
						if(interrupted == 1){
							statrunning = 0; return;
						}
						statrunning = 0; return;
					}
					
					interrupted = expResupply(numtwo,"fleet3",statustwo,counttwo,fleet3startstatus);
					if(interrupted == 1){
						statrunning = 0; return;
					}
					
					interrupted = clickact("expeditionstart",sr.nextInt(10)+1,sr.nextInt(8)+1);
					if(interrupted == 1){
						statrunning = 0; return;
					}
					Platform.runLater(new Runnable() {                          
						@Override
						public void run() {
							LocalDateTime dateTime = LocalDateTime.now();
							try{
								logarea.appendText(String.format("%s: %s expeditioning exp%s\n",datetime(dateTime)
						        ,"fleet3",numtwo.getValue()));
								}finally{
									if(savelog.isSelected() == true){
										try {
											logwriter.write(String.format("%s: %s expeditioning exp%s\n",datetime(dateTime)
									        ,"fleet3",numtwo.getValue()));
											logwriter.newLine();
											logwriter.flush();
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								}
						}
					});
	
					wait(5000L);
					fleet3currentexp = numtwo.getValue();
					int totalexptimecache = EXP[numtwo.getItems().indexOf(numtwo.getValue())] + expoffsettime;
					String finishhour = String.valueOf(totalexptimecache/3600);
					String finishminute = String.valueOf((totalexptimecache/60)-((totalexptimecache/3600)*60));
					String finishsec = String.valueOf(totalexptimecache-(Integer.parseInt(finishminute)*60)-(Integer.parseInt(finishhour)*3600));
					Platform.runLater(new Runnable() {                          
						@Override
						public void run() {
							try{
								hourtwo.setValue(finishhour);
								minutetwo.setValue(finishminute);
								secondtwo.setValue(finishsec);
								statustwo.setValue("expeditioning");
								}finally{
								}
						}
					});
					fleet3ExpCountChangable = 1;
					fleet3startstatus = 0;
				}
				if(fleet4startstatus == 2 && numthree.getValue()!="none"&& startthree.isDisabled() == true){
					interrupted = chooseExp(numthree);
					if(interrupted == 1){
						statrunning = 0; return;
					}
					interrupted = clickact("decided",sr.nextInt(78)+1,sr.nextInt(14)+1);
					if(interrupted == 1){
						statrunning = 0; return;
					}
					/**Pattern space = new Pattern("space.png").targetOffset(sr.nextInt(50)+1,sr.nextInt(50)+1);
					Match spac = GameRegion.exists(space,1);
					GameRegion.mouseMove(spac);*/
					wait(1.0);
					
					do{
						interrupted = clickact("fleet4",sr.nextInt(10)+1,sr.nextInt(8)+1);
						if(interrupted == 1){
							statrunning = 0; return;
						}
					}while(findexists("fleet4(pressed)",2) == false);
					wait(1.0);
					if(findexists("expeditioning",1) == true){
						Platform.runLater(new Runnable() {                          
							@Override
							public void run() {
								LocalDateTime dateTime = LocalDateTime.now();
								try{
									logarea.appendText(String.format("%s: Error: %s is already expeditioning.\n",datetime(dateTime)
							        ,"fleet4"));
									}finally{
										if(savelog.isSelected() == true){
											try {
												logwriter.write(String.format("%s: Error: %s is already expeditioning.\n",datetime(dateTime)
										        ,"fleet4"));
												logwriter.newLine();
												logwriter.flush();
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
									}
							}
						});
						stopthree(null);
						interrupted = clickact("home",sr.nextInt(9)+1,sr.nextInt(28)+1);
						if(interrupted == 1){
							statrunning = 0; return;
						}
						statrunning = 0; return;
					}
					
					interrupted = expResupply(numthree,"fleet4",statusthree,countthree,fleet4startstatus);
					if(interrupted == 1){
						statrunning = 0; return;
					}
					
					interrupted = clickact("expeditionstart",sr.nextInt(10)+1,sr.nextInt(8)+1);
					if(interrupted == 1){
						statrunning = 0; return;
					}
					Platform.runLater(new Runnable() {                          
						@Override
						public void run() {
							LocalDateTime dateTime = LocalDateTime.now();
							try{
								logarea.appendText(String.format("%s: %s expeditioning exp%s\n",datetime(dateTime)
						        ,"fleet4",numthree.getValue()));
								}finally{
									if(savelog.isSelected() == true){
										try {
											logwriter.write(String.format("%s: %s expeditioning exp%s\n",datetime(dateTime)
									        ,"fleet4",numthree.getValue()));
											logwriter.newLine();
											logwriter.flush();
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								}
						}
					});
					wait(5000L);
					fleet4currentexp = numthree.getValue();
					int totalexptimecache = EXP[numthree.getItems().indexOf(numthree.getValue())] + expoffsettime;
					String finishhour = String.valueOf(totalexptimecache/3600);
					String finishminute = String.valueOf((totalexptimecache/60)-((totalexptimecache/3600)*60));
					String finishsec = String.valueOf(totalexptimecache-(Integer.parseInt(finishminute)*60)-(Integer.parseInt(finishhour)*3600));
					Platform.runLater(new Runnable() {                          
						@Override
						public void run() {
							try{
								hourthree.setValue(finishhour);
								minutethree.setValue(finishminute);
								secondthree.setValue(finishsec);
								statusthree.setValue("expeditioning");
								}finally{
								}
						}
					});
					fleet4ExpCountChangable = 1;
					fleet4startstatus = 0;
				}
				
				interrupted = clickact("home",sr.nextInt(9)+1,sr.nextInt(28)+1);
				if(interrupted == 1){
					statrunning = 0; return;
				}
//				do{
//					if(findexists("expeditionreturn",2)){
//						interrupted = clickact("expeditionreturn",-(sr.nextInt(400)+1),sr.nextInt(350)+1);
//						if(interrupted == 1){
//							statrunning = 0; return;
//						}
//						
//						interrupted = clickact("next",-(sr.nextInt(600)+1),-(sr.nextInt(350)+1));
//						if(interrupted == 1){
//							statrunning = 0; return;
//						}
//						wait(clickoffsettime);
//						PostMessage.clickbypostmessage(hwndclick,sr.nextInt(600)+1 ,sr.nextInt(350)+1);
//						/**interrupted = clickact("next",-(sr.nextInt(600)+1),-(sr.nextInt(350)+1));
//						if(interrupted == 1){
//							statrunning = 0; return;
//						}*/
//					}
//				}while(findexists("expeditionreturn",2));
				
				do{
					if(findexists("sorties",10)){
						break;
					}
					else{
						interrupted = clickact("home",sr.nextInt(9)+1,sr.nextInt(28)+1);
						if(interrupted == 1){
							statrunning = 0; return;
						}
						do{
							if(findexists("expeditionreturn",2)){
								interrupted = clickact("expeditionreturn",-(sr.nextInt(400)+1),sr.nextInt(350)+1);
								if(interrupted == 1){
									statrunning = 0; return;
								}
								
								interrupted = clickact("next",-(sr.nextInt(600)+1),-(sr.nextInt(350)+1));
								if(interrupted == 1){
									statrunning = 0; return;
								}
								wait(clickoffsettime);
								PostMessage.clickbypostmessage(hwndclick,sr.nextInt(600)+1 ,sr.nextInt(350)+1);
								/**interrupted = clickact("next",-(sr.nextInt(600)+1),-(sr.nextInt(350)+1));
								if(interrupted == 1){
									statrunning = 0; return;
								}*/
							}
							else {
								break;
							}
						}while(findexists("expeditionreturn",2));
					}
				}while(findexists("sorties",10) == false);
				page = 1; 
				/**Pattern space2 = new Pattern("space2.png").targetOffset(sr.nextInt(50)+1,sr.nextInt(14)+1);
				Match spac2 = GameRegion.exists(space2,1);
				GameRegion.mouseMove(spac2);*/
				
			} catch (FindFailed e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(fleet2startstatus == 1||fleet3startstatus == 1||fleet4startstatus == 1){
			try {
				//TryWithHWND.main(null);
				do{
					if(findexists("expeditionreturn",2)){
						interrupted = clickact("expeditionreturn",-(sr.nextInt(400)+1),sr.nextInt(350)+1);
						if(interrupted == 1){
							statrunning = 0; return;
						}
						//wait("next.png",30);
						interrupted = clickact("next",-(sr.nextInt(600)+1),-(sr.nextInt(350)+1));
						if(interrupted == 1){
							statrunning = 0; return;
						}
						wait(clickoffsettime);
						PostMessage.clickbypostmessage(hwndclick,sr.nextInt(600)+1 ,sr.nextInt(350)+1);
						/**interrupted = clickact("next",-(sr.nextInt(600)+1),-(sr.nextInt(350)+1));
						if(interrupted == 1){
							statrunning = 0; return;
						}*/
					}
					else{
						if(findexists("sorties",10) == true && findexists("expeditionreturn",2) == true){
							do{
								if(findexists("expeditionreturn",2)){
									interrupted = clickact("expeditionreturn",-(sr.nextInt(400)+1),sr.nextInt(350)+1);
									if(interrupted == 1){
										statrunning = 0; return;
									}
									
									interrupted = clickact("next",-(sr.nextInt(600)+1),-(sr.nextInt(350)+1));
									if(interrupted == 1){
										statrunning = 0; return;
									}
									wait(clickoffsettime);
									PostMessage.clickbypostmessage(hwndclick,sr.nextInt(600)+1 ,sr.nextInt(350)+1);
									/**interrupted = clickact("next",-(sr.nextInt(600)+1),-(sr.nextInt(350)+1));
									if(interrupted == 1){
										statrunning = 0; return;
									}*/
								}
								else {
									break;
								}
							}while(findexists("expeditionreturn",2));
							break;
						}
						else if(findexists("sorties",2) == false){
							interrupted = retry();
							if(interrupted == 1){
								statrunning = 0; return;
							}
						}
						interrupted = clickact("sorties",sr.nextInt(20)+1,sr.nextInt(20)+1);
						if(interrupted == 1){
							statrunning = 0; return;
						}
							            						
						interrupted = clickact("home",sr.nextInt(9)+1,sr.nextInt(28)+1);
						if(interrupted == 1){
							statrunning = 0; return;
						}
	
						if(findexists("expeditionreturn",2)){
							interrupted = clickact("expeditionreturn",-(sr.nextInt(400)+1),sr.nextInt(350)+1);
							if(interrupted == 1){
								statrunning = 0; return;
							}
							
							//wait("next.png",30);
							interrupted = clickact("next",-(sr.nextInt(600)+1),-(sr.nextInt(350)+1));
							if(interrupted == 1){
								statrunning = 0; return;
							}
							wait(clickoffsettime);
							PostMessage.clickbypostmessage(hwndclick,sr.nextInt(600)+1 ,sr.nextInt(350)+1);
							/**interrupted = clickact("next",-(sr.nextInt(600)+1),-(sr.nextInt(350)+1));
							if(interrupted == 1){
								statrunning = 0; return;
							}*/
						}
					}
				}while(findexists("expeditionreturn",2));
					interrupted = clickact("supply",sr.nextInt(25)+1,sr.nextInt(25)+1);
					if(interrupted == 1){
						statrunning = 0; return;
					}
					if(fleet2startstatus == 1){
						do{
							interrupted = clickact("fleet2",sr.nextInt(9)+1,sr.nextInt(8)+1);
							if(interrupted == 1){
								statrunning = 0; return;
							}
						}while(findexists("fleet2(pressed)",2) == false);
						
						if(findexists("needsupply",2)){
							do{
								interrupted = clickact("supplyall",sr.nextInt(8)+1,sr.nextInt(8)+1);
								if(interrupted == 1){
									statrunning = 0; return;
								}
								wait(0.3);
							}while(findexists("needsupply",2) == true);
							Platform.runLater(new Runnable() {                          
								@Override
								public void run() {
									LocalDateTime dateTime = LocalDateTime.now();
									try{
										logarea.appendText(String.format("%s: Supply %s.\n",datetime(dateTime)
								        ,"fleet2"));
										}finally{
											if(savelog.isSelected() == true){
												try {
													logwriter.write(String.format("%s: Supply %s.\n",datetime(dateTime)
											        ,"fleet2"));
													logwriter.newLine();
													logwriter.flush();
												} catch (IOException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
											}
										}
								}
							});
							Platform.runLater(new Runnable() {                          
								@Override
								public void run() {
									try{
										statusone.setValue("no expedition");
										int count1 = Integer.parseInt(countone.getText());
										countone.setText(String.valueOf(++count1));
										}finally{
										}
								}
							});
							fleet2startstatus = 2;
						}
						else {
							if(findexists("expeditioning",1) == false){
								Platform.runLater(new Runnable() {                          
									@Override
									public void run() {
										try{
											statusone.setValue("no expedition");
											}finally{
											}
									}
								});
								fleet2startstatus = 2;
							}
							else {
								//stopone(null);
							}
						}
					}
					if(fleet3startstatus == 1){
						do{
						    interrupted = clickact("fleet3",sr.nextInt(9)+1,sr.nextInt(8)+1);
							if(interrupted == 1){
								statrunning = 0; return;
							}
						}while(findexists("fleet3(pressed)",2) == false);
						
						if(findexists("needsupply",2)){
							do{
								interrupted = clickact("supplyall",sr.nextInt(8)+1,sr.nextInt(8)+1);
								if(interrupted == 1){
									statrunning = 0; return;
								}
								wait(0.3);
							}while(findexists("needsupply",2) == true);
							Platform.runLater(new Runnable() {                          
								@Override
								public void run() {
									LocalDateTime dateTime = LocalDateTime.now();
									try{
										logarea.appendText(String.format("%s: Supply %s.\n",datetime(dateTime)
								        ,"fleet3"));
										}finally{
											if(savelog.isSelected() == true){
												try {
													logwriter.write(String.format("%s: Supply %s.\n",datetime(dateTime)
											        ,"fleet3"));
													logwriter.newLine();
													logwriter.flush();
												} catch (IOException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
											}
										}
								}
							});
							Platform.runLater(new Runnable() {                          
								@Override
								public void run() {
									try{
										statustwo.setValue("no expedition");
										int count2 = Integer.parseInt(counttwo.getText());
										counttwo.setText(String.valueOf(++count2));
										}finally{
										}
								}
							});
							fleet3startstatus = 2;
						}
						else {
							if(findexists("expeditioning",1) == false){
								Platform.runLater(new Runnable() {                          
									@Override
									public void run() {
										try{
											statustwo.setValue("no expedition");
											}finally{
											}
									}
								});
								fleet3startstatus = 2;
							}
							else {
								//stoptwo(null);
							}
						}
					}
					if(fleet4startstatus == 1){
						do{
						    interrupted = clickact("fleet4",sr.nextInt(9)+1,sr.nextInt(8)+1);
							if(interrupted == 1){
								statrunning = 0; return;
							}
						}while(findexists("fleet4(pressed)",2) == false);
						
						if(findexists("needsupply",2)){
							do{
								interrupted = clickact("supplyall",sr.nextInt(8)+1,sr.nextInt(8)+1);
								if(interrupted == 1){
									statrunning = 0; return;
								}
								wait(0.3);
							}while(findexists("needsupply",2) == true);
							Platform.runLater(new Runnable() {                          
								@Override
								public void run() {
									LocalDateTime dateTime = LocalDateTime.now();
									try{
										logarea.appendText(String.format("%s: Supply %s.\n",datetime(dateTime)
								        ,"fleet4"));
										}finally{
											if(savelog.isSelected() == true){
												try {
													logwriter.write(String.format("%s: Supply %s.\n",datetime(dateTime)
											        ,"fleet4"));
													logwriter.newLine();
													logwriter.flush();
												} catch (IOException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
											}
										}
								}
							});
							Platform.runLater(new Runnable() {                          
								@Override
								public void run() {
									try{
										statusthree.setValue("no expedition");
										int count3 = Integer.parseInt(countthree.getText());
										countthree.setText(String.valueOf(++count3));
										}finally{
										}
								}
							});
							fleet4startstatus = 2;
						}
						else {
							if(findexists("expeditioning",1) == false){
								Platform.runLater(new Runnable() {                          
									@Override
									public void run() {
										try{
											statusthree.setValue("no expedition");
											}finally{
											}
									}
								});
								fleet4startstatus = 2;
							}
							else {
								//stopthree(null);
							}
						}
						
					}
					
					interrupted = clickact("home",sr.nextInt(9)+1,sr.nextInt(28)+1);
					if(interrupted == 1){
						statrunning = 0; return;
					}
					do{
						if(findexists("expeditionreturn",2)){
							interrupted = clickact("expeditionreturn",-(sr.nextInt(400)+1),sr.nextInt(350)+1);
							if(interrupted == 1){
								statrunning = 0; return;
							}
							
							interrupted = clickact("next",-(sr.nextInt(600)+1),-(sr.nextInt(350)+1));
							if(interrupted == 1){
								statrunning = 0; return;
							}
							wait(clickoffsettime);
							PostMessage.clickbypostmessage(hwndclick,sr.nextInt(600)+1 ,sr.nextInt(350)+1);
							/**interrupted = clickact("next",-(sr.nextInt(600)+1),-(sr.nextInt(350)+1));
							if(interrupted == 1){
								statrunning = 0; return;
							}*/
						}
					}while(findexists("expeditionreturn",2));
					
					if(fleet2startstatus == 2 && numone.getValue()=="none"&& startone.isDisabled() == true){
						fleet2currentexp = "";
						Platform.runLater(new Runnable() {                          
							@Override
							public void run() {
								LocalDateTime dateTime = LocalDateTime.now();
								try{
									logarea.appendText(String.format("%s: Warning: %s no choose exp.\n",datetime(dateTime)
							        ,"fleet2"));
									}finally{
										if(savelog.isSelected() == true){
											try {
												logwriter.write(String.format("%s: Warning: %s no choose exp.\n",datetime(dateTime)
										        ,"fleet2"));
												logwriter.newLine();
												logwriter.flush();
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
									}
							}
						});
						stopone(null);
						
					}
					if(fleet3startstatus == 2 && numtwo.getValue()=="none"&& starttwo.isDisabled() == true){
						fleet3currentexp = "";
						Platform.runLater(new Runnable() {                          
							@Override
							public void run() {
								LocalDateTime dateTime = LocalDateTime.now();
								try{
									logarea.appendText(String.format("%s: Warning: %s no choose exp.\n",datetime(dateTime)
							        ,"fleet3"));
									}finally{
										if(savelog.isSelected() == true){
											try {
												logwriter.write(String.format("%s: Warning: %s no choose exp.\n",datetime(dateTime)
										        ,"fleet3"));
												logwriter.newLine();
												logwriter.flush();
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
									}
							}
						});
						stoptwo(null);
						
					}
					if(fleet4startstatus == 2 && numthree.getValue()=="none"&& startthree.isDisabled() == true){
						fleet4currentexp = "";
						Platform.runLater(new Runnable() {                          
							@Override
							public void run() {
								LocalDateTime dateTime = LocalDateTime.now();
								try{
									logarea.appendText(String.format("%s: Warning: %s no choose exp.\n",datetime(dateTime)
							        ,"fleet4"));
									}finally{
										if(savelog.isSelected() == true){
											try {
												logwriter.write(String.format("%s: Warning: %s no choose exp.\n",datetime(dateTime)
										        ,"fleet4"));
												logwriter.newLine();
												logwriter.flush();
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
									}
							}
						});
						stopthree(null);
						
					}
					if((fleet2startstatus == 2 && startone.isDisabled() == true)
						|| (fleet3startstatus == 2 && starttwo.isDisabled() == true)
						|| (fleet4startstatus == 2 && startthree.isDisabled() == true)) {
						
					}else {
						statrunning = 0; 
						Platform.runLater(new Runnable() {                          
							@Override
							public void run() {
								try{
									workingstatus.setText("idle");
									workingstatus.setTextFill(Color.web("#009a00"));
									}finally{
									}
							}
						});
						return;
					}
					
					
					if(startone.isDisabled() == true || starttwo.isDisabled() == true || startthree.isDisabled() == true) {
		
						interrupted = clickact("sorties",sr.nextInt(20)+1,sr.nextInt(20)+1);
						if(interrupted == 1){
							statrunning = 0; return;
						}
			
						interrupted = clickact("expedition",sr.nextInt(40)+1,sr.nextInt(40)+1);
						if(interrupted == 1){
							statrunning = 0; return;
						}
						
						if(fleet2startstatus == 2 && numone.getValue() !="none" && startone.isDisabled() == true){
							interrupted = chooseExp(numone);
							if(interrupted == 1){
								statrunning = 0; return;
							}
							interrupted = clickact("decided",sr.nextInt(78)+1,sr.nextInt(14)+1);
							if(interrupted == 1){
								statrunning = 0; return;
							}
							/**Pattern space = new Pattern("space.png").targetOffset(sr.nextInt(50)+1,sr.nextInt(50)+1);
							Match spac = GameRegion.exists(space,1);
							GameRegion.mouseMove(spac);*/
							if(findexists("expeditioning",1) == true){
								Platform.runLater(new Runnable() {                          
									@Override
									public void run() {
										LocalDateTime dateTime = LocalDateTime.now();
										try{
											logarea.appendText(String.format("%s: Error: %s is already expeditioning.\n",datetime(dateTime)
									        ,"fleet2"));
											}finally{
												if(savelog.isSelected() == true){
													try {
														logwriter.write(String.format("%s: Error: %s is already expeditioning.\n",datetime(dateTime)
												        ,"fleet2"));
														logwriter.newLine();
														logwriter.flush();
													} catch (IOException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													}
												}
											}
									}
								});
								stopone(null);
								interrupted = clickact("home",sr.nextInt(9)+1,sr.nextInt(28)+1);
								if(interrupted == 1){
									statrunning = 0; return;
								}
								statrunning = 0; 
								Platform.runLater(new Runnable() {                          
									@Override
									public void run() {
										try{
											workingstatus.setText("idle");
											workingstatus.setTextFill(Color.web("#009a00"));
											}finally{
											}
									}
								});
								return;
							}
							
							interrupted = expResupply(numone,"fleet2",statusone,countone,fleet2startstatus);
							if(interrupted == 1){
								statrunning = 0; return;
							}
							
							interrupted = clickact("expeditionstart",sr.nextInt(10)+1,sr.nextInt(8)+1);
							if(interrupted == 1){
								statrunning = 0; return;
							}
							Platform.runLater(new Runnable() {                          
								@Override
								public void run() {
									LocalDateTime dateTime = LocalDateTime.now();
									try{
										logarea.appendText(String.format("%s: %s expeditioning exp%s\n",datetime(dateTime)
								        ,"fleet2",numone.getValue()));
										}finally{
											if(savelog.isSelected() == true){
												try {
													logwriter.write(String.format("%s: %s expeditioning exp%s\n",datetime(dateTime)
											        ,"fleet2",numone.getValue()));
													logwriter.newLine();
													logwriter.flush();
												} catch (IOException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
											}
										}
								}
							});
							wait(5000L);
							fleet2currentexp = numone.getValue();
							int totalexptimecache = EXP[numone.getItems().indexOf(numone.getValue())] + expoffsettime;
							String finishhour = String.valueOf(totalexptimecache/3600);
							String finishminute = String.valueOf((totalexptimecache/60)-((totalexptimecache/3600)*60));
							String finishsec = String.valueOf(totalexptimecache-(Integer.parseInt(finishminute)*60)-(Integer.parseInt(finishhour)*3600));
							Platform.runLater(new Runnable() {                          
								@Override
								public void run() {
									try{
										hourone.setValue(finishhour);
										minuteone.setValue(finishminute);
										secondone.setValue(finishsec);
										statusone.setValue("expeditioning");
										}finally{
										}
								}
							});
							fleet2ExpCountChangable = 1;
							fleet2startstatus = 0;
						}
						if(fleet3startstatus == 2 && numtwo.getValue()!="none"&& starttwo.isDisabled() == true){
							interrupted = chooseExp(numtwo);
							if(interrupted == 1){
								statrunning = 0; return;
							}
							interrupted = clickact("decided",sr.nextInt(78)+1,sr.nextInt(14)+1);
							if(interrupted == 1){
								statrunning = 0; return;
							}
							/**Pattern space = new Pattern("space.png").targetOffset(sr.nextInt(50)+1,sr.nextInt(50)+1);
							Match spac = GameRegion.exists(space,1);
							GameRegion.mouseMove(spac);*/
							wait(1.0);
							do{
								interrupted = clickact("fleet3",sr.nextInt(10)+1,sr.nextInt(8)+1);
								if(interrupted == 1){
									statrunning = 0; return;
								}
							}while(findexists("fleet3(pressed)",2) == false);
							wait(1.0);
							if(findexists("expeditioning",1) == true){
								Platform.runLater(new Runnable() {                          
									@Override
									public void run() {
										LocalDateTime dateTime = LocalDateTime.now();
										try{
											logarea.appendText(String.format("%s: Error: %s is already expeditioning.\n",datetime(dateTime)
									        ,"fleet3"));
											}finally{
												if(savelog.isSelected() == true){
													try {
														logwriter.write(String.format("%s: Error: %s is already expeditioning.\n",datetime(dateTime)
												        ,"fleet3"));
														logwriter.newLine();
														logwriter.flush();
													} catch (IOException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													}
												}
											}
									}
								});
								stoptwo(null);
								interrupted = clickact("home",sr.nextInt(9)+1,sr.nextInt(28)+1);
								if(interrupted == 1){
									statrunning = 0; return;
								}
								statrunning = 0; 
								Platform.runLater(new Runnable() {                          
									@Override
									public void run() {
										try{
											workingstatus.setText("idle");
											workingstatus.setTextFill(Color.web("#009a00"));
											}finally{
											}
									}
								});
								return;
							}
							
							interrupted = expResupply(numtwo,"fleet3",statustwo,counttwo,fleet3startstatus);
							if(interrupted == 1){
								statrunning = 0; return;
							}
							
							interrupted = clickact("expeditionstart",sr.nextInt(10)+1,sr.nextInt(8)+1);
							if(interrupted == 1){
								statrunning = 0; return;
							}
							Platform.runLater(new Runnable() {                          
								@Override
								public void run() {
									LocalDateTime dateTime = LocalDateTime.now();
									try{
										logarea.appendText(String.format("%s: %s expeditioning exp%s\n",datetime(dateTime)
								        ,"fleet3",numtwo.getValue()));
										}finally{
											if(savelog.isSelected() == true){
												try {
													logwriter.write(String.format("%s: %s expeditioning exp%s\n",datetime(dateTime)
											        ,"fleet3",numtwo.getValue()));
													logwriter.newLine();
													logwriter.flush();
												} catch (IOException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
											}
										}
								}
							});
							wait(5000L);
							fleet3currentexp = numtwo.getValue();
							int totalexptimecache = EXP[numtwo.getItems().indexOf(numtwo.getValue())] + expoffsettime;
							String finishhour = String.valueOf(totalexptimecache/3600);
							String finishminute = String.valueOf((totalexptimecache/60)-((totalexptimecache/3600)*60));
							String finishsec = String.valueOf(totalexptimecache-(Integer.parseInt(finishminute)*60)-(Integer.parseInt(finishhour)*3600));
							Platform.runLater(new Runnable() {                          
								@Override
								public void run() {
									try{
										hourtwo.setValue(finishhour);
										minutetwo.setValue(finishminute);
										secondtwo.setValue(finishsec);
										statustwo.setValue("expeditioning");
										}finally{
										}
								}
							});
							fleet3ExpCountChangable = 1;
							fleet3startstatus = 0;
						}
						if(fleet4startstatus == 2 && numthree.getValue()!="none" && startthree.isDisabled() == true){
							interrupted = chooseExp(numthree);
							if(interrupted == 1){
								statrunning = 0; return;
							}
							interrupted = clickact("decided",sr.nextInt(78)+1,sr.nextInt(14)+1);
							if(interrupted == 1){
								statrunning = 0; return;
							}
							/**Pattern space = new Pattern("space.png").targetOffset(sr.nextInt(50)+1,sr.nextInt(50)+1);
							Match spac = GameRegion.exists(space,1);
							GameRegion.mouseMove(spac);*/
							wait(1.0);
							do{
								interrupted = clickact("fleet4",sr.nextInt(10)+1,sr.nextInt(8)+1);
								if(interrupted == 1){
									statrunning = 0; return;
								}
							}while(findexists("fleet4(pressed)",2) == false);
							wait(1.0);
							if(findexists("expeditioning",1) == true){
								Platform.runLater(new Runnable() {                          
									@Override
									public void run() {
										LocalDateTime dateTime = LocalDateTime.now();
										try{
											logarea.appendText(String.format("%s: Error: %s is already expeditioning.\n",datetime(dateTime)
									        ,"fleet4"));
											}finally{
												if(savelog.isSelected() == true){
													try {
														logwriter.write(String.format("%s: Error: %s is already expeditioning.\n",datetime(dateTime)
												        ,"fleet4"));
														logwriter.newLine();
														logwriter.flush();
													} catch (IOException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													}
												}
											}
									}
								});
								stopthree(null);
								interrupted = clickact("home",sr.nextInt(9)+1,sr.nextInt(28)+1);
								if(interrupted == 1){
									statrunning = 0; return;
								}
								statrunning = 0; 
								Platform.runLater(new Runnable() {                          
									@Override
									public void run() {
										try{
											workingstatus.setText("idle");
											workingstatus.setTextFill(Color.web("#009a00"));
											}finally{
											}
									}
								});
								return;
							}
							
							interrupted = expResupply(numthree,"fleet4",statusthree,countthree,fleet4startstatus);
							if(interrupted == 1){
								statrunning = 0; return;
							}
							
							interrupted = clickact("expeditionstart",sr.nextInt(10)+1,sr.nextInt(8)+1);
							if(interrupted == 1){
								statrunning = 0; return;
							}
							Platform.runLater(new Runnable() {                          
								@Override
								public void run() {
									LocalDateTime dateTime = LocalDateTime.now();
									try{
										logarea.appendText(String.format("%s: %s expeditioning exp%s\n",datetime(dateTime)
								        ,"fleet4",numthree.getValue()));
										}finally{
											if(savelog.isSelected() == true){
												try {
													logwriter.write(String.format("%s: %s expeditioning exp%s\n",datetime(dateTime)
											        ,"fleet4",numthree.getValue()));
													logwriter.newLine();
													logwriter.flush();
												} catch (IOException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
											}
										}
								}
							});
							wait(5000L);
							fleet4currentexp = numthree.getValue();
							int totalexptimecache = EXP[numthree.getItems().indexOf(numthree.getValue())] + expoffsettime;
							String finishhour = String.valueOf(totalexptimecache/3600);
							String finishminute = String.valueOf((totalexptimecache/60)-((totalexptimecache/3600)*60));
							String finishsec = String.valueOf(totalexptimecache-(Integer.parseInt(finishminute)*60)-(Integer.parseInt(finishhour)*3600));
							Platform.runLater(new Runnable() {                          
								@Override
								public void run() {
									try{
										hourthree.setValue(finishhour);
										minutethree.setValue(finishminute);
										secondthree.setValue(finishsec);
										statusthree.setValue("expeditioning");
										}finally{
										}
								}
							});
							fleet4ExpCountChangable = 1;
							fleet4startstatus = 0;
						}
						
						interrupted = clickact("home",sr.nextInt(9)+1,sr.nextInt(28)+1);
						if(interrupted == 1){
							statrunning = 0; return;
						}
//						do{
//							if(findexists("expeditionreturn",2)){
//								interrupted = clickact("expeditionreturn",-(sr.nextInt(400)+1),sr.nextInt(350)+1);
//								if(interrupted == 1){
//									statrunning = 0; return;
//								}
//								
//								interrupted = clickact("next",-(sr.nextInt(600)+1),-(sr.nextInt(350)+1));
//								if(interrupted == 1){
//									statrunning = 0; return;
//								}
//								wait(clickoffsettime);
//								PostMessage.clickbypostmessage(hwndclick,sr.nextInt(600)+1 ,sr.nextInt(350)+1);
//								/**interrupted = clickact("next",-(sr.nextInt(600)+1),-(sr.nextInt(350)+1));
//								if(interrupted == 1){
//									statrunning = 0; return;
//								}*/
//							}
//						}while(findexists("expeditionreturn",2));
						do{
							if(findexists("sorties",10)){
								break;
							}
							else{
								interrupted = clickact("home",sr.nextInt(9)+1,sr.nextInt(28)+1);
								if(interrupted == 1){
									statrunning = 0; return;
								}
								do{
									if(findexists("expeditionreturn",2)){
										interrupted = clickact("expeditionreturn",-(sr.nextInt(400)+1),sr.nextInt(350)+1);
										if(interrupted == 1){
											statrunning = 0; return;
										}
										
										interrupted = clickact("next",-(sr.nextInt(600)+1),-(sr.nextInt(350)+1));
										if(interrupted == 1){
											statrunning = 0; return;
										}
										wait(clickoffsettime);
										PostMessage.clickbypostmessage(hwndclick,sr.nextInt(600)+1 ,sr.nextInt(350)+1);
										/**interrupted = clickact("next",-(sr.nextInt(600)+1),-(sr.nextInt(350)+1));
										if(interrupted == 1){
											statrunning = 0; return;
										}*/
									}
									else {
										break;
									}
								}while(findexists("expeditionreturn",2));
							}
						}while(findexists("sorties",10) == false);
						page = 1;
						/**Pattern space2 = new Pattern("space2.png").targetOffset(sr.nextInt(50)+1,sr.nextInt(14)+1);
						Match spac2 = GameRegion.exists(space2,1);
						GameRegion.mouseMove(spac2);*/
				}
			} catch (FindFailed e) {
				// TODO Auto-generated catch block
				e.printStackTrace();	
			}
		}
		try {
			if((QuestCheckBoxList.get(0).isSelected() == true && Integer.valueOf(QuestProgressList.get(0).getText())>=3)||
					(QuestCheckBoxList.get(1).isSelected() == true && Integer.valueOf(QuestProgressList.get(1).getText())>=10)||
					(QuestCheckBoxList.get(2).isSelected() == true && Integer.valueOf(QuestProgressList.get(2).getText())>=30)||
					(QuestCheckBoxList.get(3).isSelected() == true && Integer.valueOf(QuestProgressList.get(3).getText())>=1)||
					(QuestCheckBoxList.get(4).isSelected() == true && Integer.valueOf(QuestProgressList.get(4).getText())>=6)||
					firstrunofaday == 1 || questInfoNotGet == 1) {
				interrupted = checkquest();
				if(interrupted == 1){
					statrunning = 0; return;
				}
			}
		} catch (FindFailed e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Platform.runLater(new Runnable() {                          
			@Override
			public void run() {
				try{
					workingstatus.setText("idle");
					workingstatus.setTextFill(Color.web("#009a00"));
					}finally{
					}
			}
		});
		statrunning = 0;
		
	}
	      
}