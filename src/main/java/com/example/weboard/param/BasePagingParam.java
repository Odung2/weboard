package com.example.weboard.param;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class BasePagingParam {
    @Schema(description = "현재 페이지 [default: 1, min: 1]")
    @Min(1)
    private long currPage = 1;

    @Schema(description = "페이지 사이즈 [default: 20, min: 2, max: 200]")
    @Min(2)
    @Max(200)
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
