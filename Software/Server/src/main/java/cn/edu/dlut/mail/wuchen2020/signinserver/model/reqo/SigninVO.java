package cn.edu.dlut.mail.wuchen2020.signinserver.model.reqo;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "签到信息")
public class SigninVO {
    @NotNull
    @Size(min = 32, max = 32)
    @Schema(description = "人脸指纹", example = "8399d88e3293cc89cacc1d735af12810")
    private String fingerprint;
    @NotEmpty
    @Schema(description = "签到位置", example = "B102")
    private String location;
    @NotNull
    @Schema(description = "签到时间", example = "1618405164222")
    private Long timestamp;
    
    public SigninVO() {}
    
    public SigninVO(String fingerprint, String location, long timestamp) {
        this.fingerprint = fingerprint;
        this.location = location;
        this.timestamp = timestamp;
    }

    public String getFingerprint() {
        return fingerprint;
    }
    
    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
