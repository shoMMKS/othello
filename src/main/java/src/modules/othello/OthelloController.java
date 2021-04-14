package src.modules.othello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/othello")
public class OthelloController {
	OthelloModel othelloModel = new OthelloModel();

	@GetMapping()
	public String otheelo(Model model) {
		model.addAttribute("squares", othelloModel.squares);
		model.addAttribute("blackCount", othelloModel.blackCount);
		model.addAttribute("whiteCount", othelloModel.whiteCount);
		model.addAttribute("isBlack", othelloModel.isBlack);
		if (othelloModel.isFinished) {
			return "redirect:result";
		}
		return "othello/index";
	}

	@GetMapping("result")
	public String result(Model model) {
		model.addAttribute("blackCount", othelloModel.blackCount);
		model.addAttribute("whiteCount", othelloModel.whiteCount);
		model.addAttribute("isBlack", othelloModel.isBlack);
		return "othello/result";
	}

	@PostMapping("{id}")
	public String postOthelloById(@PathVariable("id") int id, Model model) {
		othelloModel.turnChange(id);
		return "redirect:";
	}

	@PostMapping("reset")
	public String reset(Model model) {
		othelloModel = new OthelloModel();
		return "redirect:";
	}

}