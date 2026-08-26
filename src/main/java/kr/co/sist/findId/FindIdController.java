package kr.co.sist.findId;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FindIdController {

	private final FindIdService fs;

	@GetMapping("/findId")
	public String findId() {
		return "works/login/findId";
	}

	@PostMapping("/findIdProcess")
	@ResponseBody
	public Map<String, Object> findIdProcess(@RequestBody Map<String, String> data) {

		String name = data.get("name");
		String email = data.get("email");

		String userId = fs.selectFindId(name, email);

		if (userId != null) {
			return Map.of("success", true, "userId", userId, "message", "회원님의 아이디를 찾았습니다.");
		}

		return Map.of("success", false, "message", "입력하신 이름과 이메일에 해당하는 회원이 없습니다.");
	}

}
