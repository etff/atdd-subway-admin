package nextstep.subway.line.accpetance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.line.accpetance.step.LineAcceptanceStep.지하철_노선_등록되어_있음;
import static nextstep.subway.line.accpetance.step.LineAcceptanceStep.지하철_노선_목록_응답됨;
import static nextstep.subway.line.accpetance.step.LineAcceptanceStep.지하철_노선_목록_조회_요청;
import static nextstep.subway.line.accpetance.step.LineAcceptanceStep.지하철_노선_목록_포함됨;
import static nextstep.subway.line.accpetance.step.LineAcceptanceStep.지하철_노선_삭제됨;
import static nextstep.subway.line.accpetance.step.LineAcceptanceStep.지하철_노선_생성_실패됨;
import static nextstep.subway.line.accpetance.step.LineAcceptanceStep.지하철_노선_생성_요청;
import static nextstep.subway.line.accpetance.step.LineAcceptanceStep.지하철_노선_생성됨;
import static nextstep.subway.line.accpetance.step.LineAcceptanceStep.지하철_노선_수정_요청;
import static nextstep.subway.line.accpetance.step.LineAcceptanceStep.지하철_노선_수정됨;
import static nextstep.subway.line.accpetance.step.LineAcceptanceStep.지하철_노선_응답됨;
import static nextstep.subway.line.accpetance.step.LineAcceptanceStep.지하철_노선_제거_요청;
import static nextstep.subway.line.accpetance.step.LineAcceptanceStep.지하철_노선_조회_요청;
import static nextstep.subway.station.step.StationAcceptanceStep.지하철_역_등록되어_있음;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    public void setUp() {
        super.setUp();
        지하철_역_등록되어_있음("강남역");
        지하철_역_등록되어_있음("역삼역");
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(
                "2호선", "green", 1L, 2L, 10
        );

        // then
        지하철_노선_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createLine2() {
        // given
        지하철_노선_등록되어_있음("2호선", "green", 1L, 2L, 10);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(
                "2호선", "green", 1L, 2L, 10
        );

        // then
        지하철_노선_생성_실패됨(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        ExtractableResponse<Response> createResponse1 = 지하철_노선_등록되어_있음(
                "2호선", "green", 1L, 2L, 10
        );
        ExtractableResponse<Response> createResponse2 = 지하철_노선_등록되어_있음(
                "3호선", "orange", 1L, 2L, 10
        );

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        지하철_노선_목록_응답됨(response);
        지하철_노선_목록_포함됨(createResponse1, createResponse2, response);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_등록되어_있음(
                "3호선", "orange", 1L, 2L, 10
        );

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(createResponse);

        // then
        지하철_노선_응답됨(response);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_등록되어_있음(
                "3호선", "orange", 1L, 2L, 10
        );

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(createResponse);

        // then
        지하철_노선_수정됨(response);
    }


    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_등록되어_있음(
                "2호선", "green", 1L, 2L, 10
        );

        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(createResponse);

        // then
        지하철_노선_삭제됨(response);
    }
}
