package com.example.weboard.param;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class BasePagingParam {
    @Schema(description = "현재 페이지 [default: 1, min: 1]")
    @Min(value = 1, message = "페이지는 1부터 가능합니다.")
    private long currPage = 1;

    @Schema(description = "페이지 사이즈 [default: 20, min: 2, max: 200]")
    @Min(value = 2, message = "페이지 사이즈는 2에서 200 사이의 값만 가능합니다.")
    @Max(value = 200, message = "페이지 사이즈는 2에서 200 사이의 값만 가능합니다.")
    private long pageSize = 5;

    /**
     * paging의 offset 계산해서 리턴
     * @return
     */
    @JsonIgnore
    public long getOffset() {
        return (currPage -1) * pageSize;
    }
}
