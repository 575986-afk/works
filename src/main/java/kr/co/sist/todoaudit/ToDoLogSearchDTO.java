package kr.co.sist.todoaudit;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ToDoLogSearchDTO {

    private String startDate;
    private String endDate;
    private String title;
    private String task;
    private String userName;
    private String companyNo;
}