package chapter6.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import chapter6.beans.Message;
import chapter6.logging.InitApplication;
import chapter6.service.MessageService;

@WebServlet(urlPatterns = { "/edit" })
public class EditServlet extends HttpServlet {

	//ロガーインスタンスの生成
	Logger log = Logger.getLogger("twitter");

	//デフォルトコンストラクタ
	public EditServlet() {
		InitApplication application = InitApplication.getInstance();
		application.init();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException {

		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
		" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		String message = request.getParameter("message_id");
		Message editMessage = null;

		try {

			if (message != null && !message.isEmpty()) {
				int id = Integer.parseInt(message);
				editMessage = new MessageService().select(id);
			}

		}catch(NumberFormatException e) {

		}
			if(editMessage == null) {
			List<String> errorMessages = new ArrayList<String>();
			errorMessages.add("不正なパラメータが入力されました");
			request.getSession().setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./");
			return;
		}

		request.setAttribute("editMessage", editMessage);
		request.getRequestDispatcher("edit.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException {

		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
		" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		String message  = request.getParameter("message_id");
		String text = request.getParameter("text");
		List<String> errorMessages = new ArrayList<String>();

		if (message == null || message.isEmpty()) {
			errorMessages.add("不正なパラメータが入力されました");
			request.getSession().setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./");
			return;
		}
		int id = Integer.parseInt(message);

		if (!isValid(text, errorMessages)) {
			Message editMessage = new Message();
			editMessage.setId(id);
			editMessage.setText(text);

	        request.setAttribute("errorMessages", errorMessages);
	        request.setAttribute("editMessage", editMessage);
	        request.getRequestDispatcher("edit.jsp").forward(request, response);
	        return;
		}

		new MessageService().update(id, text);
		response.sendRedirect("./");
	}

	private boolean isValid(String text, List<String> errorMessages) {

		  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
	        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

	        if (StringUtils.isBlank(text)) {
	            errorMessages.add("メッセージを入力してください");
	        } else if (140 < text.length()) {
	            errorMessages.add("140文字以下で入力してください");
	        }

	        if (errorMessages.size() != 0) {
	            return false;
	        }
	        return true;
	    }

}