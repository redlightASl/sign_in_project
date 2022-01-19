package cn.edu.dlut.mail.wuchen2020.signinserver.config;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.util.Pair;

/**
 * 签到系统配置
 * 
 * @author Wu Chen
 */
@ConfigurationProperties(prefix = "signin")
public class SigninProperties {
    private Boolean enableChunkedTransfer = Boolean.FALSE;
    private LocalDate termStartTime = LocalDate.of(2020, 9, 1);
    @SuppressWarnings("unchecked")
    private List<Pair<LocalTime, LocalTime>> lessonTimes = Arrays.asList(new Pair[] {
        Pair.of(LocalTime.of(8,  0),  LocalTime.of(8,  45)),
        Pair.of(LocalTime.of(8,  50), LocalTime.of(9,  35)),
        Pair.of(LocalTime.of(10, 5),  LocalTime.of(10, 50)),
        Pair.of(LocalTime.of(11, 0),  LocalTime.of(11, 45)),
        Pair.of(LocalTime.of(13, 30), LocalTime.of(14, 15)),
        Pair.of(LocalTime.of(14, 20), LocalTime.of(15, 5)),
        Pair.of(LocalTime.of(15, 35), LocalTime.of(16, 20)),
        Pair.of(LocalTime.of(16, 25), LocalTime.of(17, 10)),
        Pair.of(LocalTime.of(18, 0),  LocalTime.of(18, 50)),
        Pair.of(LocalTime.of(18, 55), LocalTime.of(19, 45)),
        Pair.of(LocalTime.of(19, 50), LocalTime.of(20, 40))
    });
    private Integer totalWeekCount = 20;

    public Boolean isChunkedTransferEnabled() {
        return enableChunkedTransfer;
    }

    public void setEnableChunkedTransfer(Boolean enableChunkedTransfer) {
        this.enableChunkedTransfer = enableChunkedTransfer;
    }

    public LocalDate getTermStartTime() {
        return termStartTime;
    }

    public void setTermStartTime(LocalDate termStartTime) {
        this.termStartTime = termStartTime;
    }

    public List<Pair<LocalTime, LocalTime>> getLessonTimes() {
        return lessonTimes;
    }

    public void setLessonTimes(List<Pair<LocalTime, LocalTime>> lessonTimes) {
        this.lessonTimes = lessonTimes;
    }

    public Integer getTotalWeekCount() {
        return totalWeekCount;
    }

    public void setTotalWeekCount(Integer totalWeekCount) {
        this.totalWeekCount = totalWeekCount;
    }
}
