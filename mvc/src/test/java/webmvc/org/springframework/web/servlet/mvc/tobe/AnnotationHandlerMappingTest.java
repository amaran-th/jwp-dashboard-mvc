package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() throws NoSuchMethodException, InstantiationException, IllegalAccessException {
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
    }

    @Test
    @DisplayName("요청 메서드가 GET이고 경로가 /get-test이면 해당 값이 @RequestMapping으로 매핑된 핸들러가 호출된다.")
    void getHandler_get() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        given(request.getAttribute("id")).willReturn("gugu");
        given(request.getRequestURI()).willReturn("/get-test");
        given(request.getMethod()).willReturn("GET");

        // when
        final Handler handler = handlerMapping.getHandler(request);
        final ModelAndView modelAndView = handler.handle(request, response);

        // then
        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    @DisplayName("요청 메서드가 POST이고 경로가 /post-test이면 해당 값이 @RequestMapping으로 매핑된 핸들러가 호출된다.")
    void getHandler_post() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        given(request.getAttribute("id")).willReturn("gugu");
        given(request.getRequestURI()).willReturn("/post-test");
        given(request.getMethod()).willReturn("POST");

        // when
        final Handler handler = handlerMapping.getHandler(request);
        final var modelAndView = handler.handle(request, response);

        //then
        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    @DisplayName("요청 메서드, 경로에 부합하는 핸들러가 없을 경우 기본 핸들러가 호출된다.")
    void getHandler_not_found() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        given(request.getAttribute("id")).willReturn("not_found");
        given(request.getRequestURI()).willReturn("/not_found");
        given(request.getMethod()).willReturn("POST");

        // when
        final Handler handler = handlerMapping.getHandler(request);
        final var modelAndView = handler.handle(request, response);

        // then
        assertThat(modelAndView.getView())
                .usingRecursiveComparison()
                .isEqualTo(new JspView(""));
    }
}
