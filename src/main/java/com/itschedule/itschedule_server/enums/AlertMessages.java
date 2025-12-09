package com.itschedule.itschedule_server.enums;

import lombok.Getter;

public enum AlertMessages {

    PROJECT_MEMBER_ADD("프로젝트 멤버로 추가 되었습니다.", 0),
    CHANGE_PROJECT_DATE("프로젝트 기간이 변경되었습니다.", 0),
    ISSUE_ADD("이슈 담당자로 지정 되었습니다.", 1),
    CHANGE_ISSUE_DATE("이슈 기간이 변경되었습니다.", 1);

    @Getter
    private final String sentence;
    @Getter
    private final int msgType;

    private AlertMessages(String sentence, int msgType) {
        this.sentence = sentence;
        this.msgType = msgType;
    }


}
