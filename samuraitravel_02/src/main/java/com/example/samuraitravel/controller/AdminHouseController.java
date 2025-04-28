package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.form.HouseRegisterForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.service.HouseService;

@Controller
@RequestMapping("/admin/houses") //https://ドメイン名/admin/housesにアクセスしたときに実行される
public class AdminHouseController {
	private final HouseRepository houseRepository;
	private final HouseService houseService;

	public AdminHouseController(HouseRepository houseRepository, HouseService houseService) {
		this.houseRepository = houseRepository;
		this.houseService = houseService;
	}

	@GetMapping //RequestMappingで記述しているので("/admin/houses")が省略されている
	//public String index(Model, model, Pageable pageable) {
	//public String index(Model model,@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) { //ページネーションの設定を追加
	public String index(Model model,
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
			@RequestParam(name = "keyword", required = false) String keyword) {
		//List<House> houses = houseRepository.findAll(); //findAll()ですべての民宿データを取得してhousesに格納している
		//Page<House> housePage = houseRepository.findAll(pageable); //民宿データをページ情報付きで取得
		Page<House> housePage;

		if (keyword != null && !keyword.isEmpty()) { //keywordパラメータが存在するか
			housePage = houseRepository.findByNameLike("%" + keyword + "%", pageable); //keywordで部分一致検索したデータを取得
		} else {
			housePage = houseRepository.findAll(pageable); //全データ取得
		}

		//model.addAttribute("houses", houses); //index.htmlで"houses"という変数を使ったらhousesの中身を参照する
		model.addAttribute("housePage", housePage); //"housePage"という変数を使ったらhousePageの中身を参照する
		model.addAttribute("keyword", keyword);

		return "admin/houses/index"; //index.htmlを表示する
	}

	@GetMapping("/{id}") //	/admin/houses/{id}がリクエストされたら動く
	public String show(@PathVariable(name = "id") Integer id, Model model) { //@PathVariableでURLの一部("id")を引数(id)にバインドしている
		House house = houseRepository.getReferenceById(id); //URLのidと一致する民宿データを取得

		model.addAttribute("house", house);

		return "/admin/houses/show"; //	show.htmlを表示する
	}

	@GetMapping("/register") //	/admin/houses/registerがリクエストされたら動く
	public String register(Model model) {
		model.addAttribute("houseRegisterForm", new HouseRegisterForm());

		return "admin/houses/register"; //register.htmlを表示する
	}

	@PostMapping("/create") //	/admin/houses/createがリクエストされたら動く
	public String create(@ModelAttribute @Validated HouseRegisterForm houseRegisterForm, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) { //@ModelAttributeによりフォームから送信されたデータをバインドする
		//バリデーションの結果エラーがあったらregister.htmlを返す
		if (bindingResult.hasErrors()) {
			return "admin/houses/register";
		}

		houseService.create(houseRegisterForm);
		redirectAttributes.addFlashAttribute("successMessage", "民宿を登録しました。"); //リダイレクト先にsuccessMessageを渡す

		return "redirect:/admin/houses"; //admin/houses.htmlをsuccessMessageとともに返す
	}
}
