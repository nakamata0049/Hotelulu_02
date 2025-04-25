package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.repository.HouseRepository;

@Controller
@RequestMapping("/admin/houses") //https://ドメイン名/admin/housesにアクセスしたときに実行される
public class AdminHouseController {
	private final HouseRepository houseRepository;

	public AdminHouseController(HouseRepository houseRepository) {
		this.houseRepository = houseRepository;
	}

	@GetMapping //RequestMappingで記述しているので("/admin/houses")が省略されている
	//public String index(Model, model, Pageable pageable) {
	public String index(Model model,
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) { //ページネーションの設定を追加
		//List<House> houses = houseRepository.findAll(); //findAll()ですべての民宿データを取得してhousesに格納している
		Page<House> housePage = houseRepository.findAll(pageable); //民宿データをページ情報付きで取得

		//model.addAttribute("houses", houses); //index.htmlで"houses"という変数を使ったらhousesの中身を参照する
		model.addAttribute("housePage", housePage); //"housePage"という変数を使ったらhousePageの中身を参照する

		return "admin/houses/index";
	}
}
