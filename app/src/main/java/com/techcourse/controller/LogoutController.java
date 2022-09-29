package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller
public class LogoutController {

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(final HttpServletRequest req, final HttpServletResponse res) {
        final var session = req.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);
        String path = "redirect:/";

        return new ModelAndView(new JspView(path));
    }
}
