package application;

import java.sql.Date;
import java.sql.Time;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Task {
	private StringProperty code;
	private StringProperty title;
	private StringProperty details;
	private StringProperty location;
	private Date start;
	private Date stop;
	private Time startTime;
	private Time endTime;
	private IntegerProperty status;
	
	public Task(String code, String title, String detail, String location, Date start, Date stop, Time startTime,Time stopTime,int status) {
		this.code = new SimpleStringProperty(code);
		this.title = new SimpleStringProperty(title);
		this.details = new SimpleStringProperty(detail);
		this.location = new SimpleStringProperty(location);
		this.start = start;
		this.stop = stop;
		this.startTime = startTime;
		this.endTime = stopTime;
		this.status = new SimpleIntegerProperty(status);
	}

	public final StringProperty codeProperty() {
		return this.code;
	}
	

	public final String getCode() {
		return this.codeProperty().get();
	}
	

	public final void setCode(final String code) {
		this.codeProperty().set(code);
	}
	

	public final StringProperty titleProperty() {
		return this.title;
	}
	

	public final String getTitle() {
		return this.titleProperty().get();
	}
	

	public final void setTitle(final String title) {
		this.titleProperty().set(title);
	}
	

	public final StringProperty detailsProperty() {
		return this.details;
	}
	

	public final String getDetails() {
		return this.detailsProperty().get();
	}
	

	public final void setDetails(final String details) {
		this.detailsProperty().set(details);
	}

	public final Date getStart() {
		return start;
	}

	public final void setStart(Date start) {
		this.start = start;
	}

	public final Date getStop() {
		return stop;
	}

	public final void setStop(Date stop) {
		this.stop = stop;
	}

	public final Time getStartTime() {
		return startTime;
	}

	public final void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public final Time getEndTime() {
		return endTime;
	}

	public final void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public final IntegerProperty statusProperty() {
		return this.status;
	}
	

	public final int getStatus() {
		return this.statusProperty().get();
	}
	

	public final void setStatus(final int status) {
		this.statusProperty().set(status);
	}

	public final StringProperty locationProperty() {
		return this.location;
	}
	

	public final String getLocation() {
		return this.locationProperty().get();
	}
	

	public final void setLocation(final String location) {
		this.locationProperty().set(location);
	}
}
