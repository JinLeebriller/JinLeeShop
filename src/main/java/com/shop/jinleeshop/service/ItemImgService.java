package com.shop.jinleeshop.service;

import com.shop.jinleeshop.config.WebMvcConfig;
import com.shop.jinleeshop.entity.ItemImg;
import com.shop.jinleeshop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

    // application.properties에 등록한 itemImgLocation 값을 불러와서 itemImgLocation 변수에 넣어준다.
    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        // 파일 업로드
        if(!StringUtils.isEmpty(oriImgName)) {
            // 사용자가 상품의 이미지를 등록했다면 저장할 경로, 파일명, 파일의 바이트 배열을 파라미터로 uploadFile 메서드 호출
            // 호출 결과 로컬에 저장된 파일의 이름을 imgName 변수에 저장
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            /*
            저장한 상품 이미지를 불러올 경로를 설정.

            외부 리소스를 불러오는 urlPatterns로 WebMvcConfig 클래스에서 "/images/**"를 설정해주었다.
            또한 application.properties에서 설정한 uploadPath 프로퍼티 경로에서 "shopImg/" 아래 item 폴더에 이미지를 저장하므로
            상품 이미지를 불러오는 경로로 "images/item/"을 붙여준다.
            */
            imgUrl = "/images/item/" + imgName;

            // 상품 이미지 정보 저장
            /*
                imgName : 실제 로컬에 저장딘 상품 이미지 파일의 이름
                oriImgName : 업로드했던 상품 이미지 파일의 원래 이름
                imgUrl : 업로드 결과 로컬에 저장된 상품 이미지 파일을 불러오는 경로
            */
            itemImg.updateItemImg(oriImgName, imgName, imgUrl);
            itemImgRepository.save(itemImg);
        }
    }

}
