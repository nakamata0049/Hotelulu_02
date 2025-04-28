package com.example.samuraitravel.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.form.HouseRegisterForm;
import com.example.samuraitravel.repository.HouseRepository;

@Service
public class HouseService {
	private final HouseRepository houseRepository;

	public HouseService(HouseRepository houseRepository) {
		this.houseRepository = houseRepository;
	}

	@Transactional //データベースの操作をひとまとまりにする
	public void create(HouseRegisterForm houseRegisterForm) {
		House house = new House();
		MultipartFile imageFile = houseRegisterForm.getImageFile();

		if (!imageFile.isEmpty()) {
			//パスを指定してファイルを生成し、ファイル名をランダムな名前に変更
			String imageName = imageFile.getOriginalFilename();
			String hashedImageName = generateNewFileName(imageName);
			Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
			copyImageFile(imageFile, filePath);
			house.setImageName(hashedImageName);
		}

		//エンティティに値をセット
		house.setName(houseRegisterForm.getName());
		house.setDescription(houseRegisterForm.getDescription());
		house.setPrice(houseRegisterForm.getPrice());
		house.setCapacity(houseRegisterForm.getCapacity());
		house.setPostalCode(houseRegisterForm.getPostalCode());
		house.setAddress(houseRegisterForm.getAddress());
		house.setPhoneNumber(houseRegisterForm.getPhoneNumber());

		houseRepository.save(house);	//エンティティをデータベースに保存
	}

	//UUIDを使って生成したファイル名を返す
	public String generateNewFileName(String fileName) {
		String[] fileNames = fileName.split("\\.");
		for (int i = 0; i < fileNames.length - 1; i++) {
			fileNames[i] = UUID.randomUUID().toString();	//(ほぼ)重複しない一意のID
		}
		String hashedFileName = String.join(".", fileNames);
		return hashedFileName;
	}

	//画像ファイルを指定したファイルにコピーする
	public void copyImageFile(MultipartFile imageFile, Path filePath) {
		try {
			Files.copy(imageFile.getInputStream(), filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
