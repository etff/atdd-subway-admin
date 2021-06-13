package nextstep.subway.section.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.line.accpetance.step.LineAcceptanceStep.지하철_노선_등록되어_있음;
import static nextstep.subway.section.acceptance.step.SectionAcceptanceStep.지하철_구간_삭제_요청;
import static nextstep.subway.section.acceptance.step.SectionAcceptanceStep.지하철_구간_삭제됨;
import static nextstep.subway.section.acceptance.step.SectionAcceptanceStep.지하철_노선에_구간_삭제_실패됨;
import static nextstep.subway.section.acceptance.step.SectionAcceptanceStep.지하철_노선에_구간_생성_실패됨;
import static nextstep.subway.section.acceptance.step.SectionAcceptanceStep.지하철_노선에_구간_중복됨;
import static nextstep.subway.section.acceptance.step.SectionAcceptanceStep.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.section.acceptance.step.SectionAcceptanceStep.지하철_노선에_지하철역_등록되어있음;
import static nextstep.subway.section.acceptance.step.SectionAcceptanceStep.지하철_노선에_지하철역_등록됨;
import static nextstep.subway.station.step.StationAcceptanceStep.지하철_역_등록되어_있음;

@DisplayName("구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 잠실역;
    private StationResponse 선릉역;
    private StationResponse 종로역;
    private StationResponse 서울역;
    private LineResponse 이호선;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = 지하철_역_등록되어_있음("강남역").as(StationResponse.class);
        잠실역 = 지하철_역_등록되어_있음("잠실역").as(StationResponse.class);
        선릉역 = 지하철_역_등록되어_있음("선릉역").as(StationResponse.class);
        종로역 = 지하철_역_등록되어_있음("종로역").as(StationResponse.class);
        서울역 = 지하철_역_등록되어_있음("서울역").as(StationResponse.class);

        이호선 = 지하철_노선_등록되어_있음(
                "2호선", "green", 강남역.getId(), 잠실역.getId(), 10
        ).as(LineResponse.class);
    }


    @DisplayName("노선 등록된 구간 등록한다.")
    @Test
    void addSection() {
        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(이호선.getId(), 강남역.getId(), 선릉역.getId(), 5);

        // then
        지하철_노선에_지하철역_등록됨(response);
    }

    @DisplayName("기존 역 사이에 주어진 길이보다 더 긴 구간 등록시 예외처리")
    @Test
    void addSectionWithLongerDistance() {
        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(이호선.getId(), 강남역.getId(), 선릉역.getId(), 20);

        // then
        지하철_노선에_구간_생성_실패됨(response);
    }

    @DisplayName("기존 역과 동일 구간 등록시 예외처리")
    @Test
    void addSectionWithDuplicateStation() {
        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(이호선.getId(), 강남역.getId(), 잠실역.getId(), 5);

        // then
        지하철_노선에_구간_중복됨(response);
    }

    @DisplayName("기존 노선에 상행, 하행 미포함된 구간 등록시 예외처리")
    @Test
    void addSectionWithNotContainStation() {
        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(이호선.getId(), 종로역.getId(), 서울역.getId(), 5);

        // then
        지하철_노선에_구간_생성_실패됨(response);
    }

    @DisplayName("주어진 구간을 삭제하기")
    @Test
    void deleteSection() {
        // given
        지하철_노선에_지하철역_등록되어있음(이호선.getId(), 강남역.getId(), 선릉역.getId(), 2);

        // when
        ExtractableResponse<Response> response = 지하철_구간_삭제_요청(이호선.getId(), 선릉역.getId());

        // then
        지하철_구간_삭제됨(response);
    }

    @DisplayName("구간 한개 구간만 존재하는 삭제하기")
    @Test
    void deleteWithOnlyExistSection() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_삭제_요청(이호선.getId(), 잠실역.getId());

        // then
        지하철_노선에_구간_삭제_실패됨(response);
    }

}
