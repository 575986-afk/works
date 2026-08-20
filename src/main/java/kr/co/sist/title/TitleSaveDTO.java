package kr.co.sist.title;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TitleSaveDTO {

    private String no;
    private String name;
    private int priority;
}