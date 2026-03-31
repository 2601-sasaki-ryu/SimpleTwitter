package chapter6.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(urlPatterns = { "/setting", "/edit" })
public class LoginFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	throws IOException, ServletException {

		HttpServletRequest loginRequest = (HttpServletRequest) request;
		HttpServletResponse loginResponse = (HttpServletResponse) response;
		HttpSession session = loginRequest.getSession();

		List<String> errorMessages = new ArrayList<String>();

		Object loginUser = session.getAttribute("loginUser");

		if (loginUser != null) {
			chain.doFilter(request, response);
		} else {
			errorMessages.add("ログインしてください");
			session.setAttribute("errorMessages", errorMessages);

			loginResponse.sendRedirect("login");
		}
	}

	public void init(FilterConfig config) throws ServletException {
	}

    public void destroy() {
    }

}
