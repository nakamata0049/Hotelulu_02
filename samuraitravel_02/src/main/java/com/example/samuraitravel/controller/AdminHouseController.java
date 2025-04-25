package com.example.samuraitravel.controller;

import java.util.List;

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
	public String index(Model model) {
		List<House> houses = houseRepository.findAll(); //findAll()ですべての民宿データを取得してhousesに格納している

		model.addAttribute("houses", houses);	//index.htmlで"houses"という変数を使ったらhousesの中身を参照する

		return "admin/houses/index";
	}
}
