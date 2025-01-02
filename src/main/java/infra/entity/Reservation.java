package infra.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString(callSuper = true)
@Entity
public class Reservation extends AuditingFields{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mid", nullable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid", nullable = false)
    private User user;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "people_num", nullable = false)
    private int peopleNum;

    @Column(name = "start_time", nullable = false)
    private int startTime;  // 24시간 형식

    @Column(name = "end_time", nullable = false)
    private int endTime;    // 24시간 형식

    @Column(name = "total_duration", nullable = false)
    private int totalDuration;

    
    protected Reservation() {}
    
    
    private Reservation(Room room, User user, LocalDate date, int peopleNum, int startTime, int endTime,
			int totalDuration) {

		this.room = room;
		this.user = user;
		this.date = date;
		this.peopleNum = peopleNum;
		this.startTime = startTime;
		this.endTime = endTime;
		this.totalDuration = totalDuration;
	}
    
    public static Reservation of(Room room, User user, LocalDate date, int peopleNum, int startTime, int endTime,
			int totalDuration) {
    	return new Reservation(room, user, date, peopleNum, startTime, endTime, totalDuration);
    }


	public Reservation updateField(LocalDate startDate, Integer peopleNum, Integer startTime, Integer endTime,
			Integer totalTime) {
		
		this.date =startDate;
		this.peopleNum = peopleNum;
		this.startTime = startTime;
		this.endTime = endTime;
		this.totalDuration = totalTime;
		
		return this;
		
	}
    
    
    
    
    



}