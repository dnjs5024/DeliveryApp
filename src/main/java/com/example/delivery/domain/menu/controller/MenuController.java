package com.example.delivery.domain.menu.controller;

import com.example.delivery.common.exception.enums.SuccessCode;
import com.example.delivery.common.response.ApiResponseDto;
import com.example.delivery.domain.user.dto.SessionUserDto;
import com.example.delivery.domain.menu.dto.RequestDto;
import com.example.delivery.domain.menu.dto.ResponseDto;
import com.example.delivery.domain.menu.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;


    @PostMapping
    public ResponseEntity<ApiResponseDto<ResponseDto>> createMenu(
            @SessionAttribute("loginUser") SessionUserDto sessionUserDto,
            @RequestBody @Valid RequestDto requestDto) {
        // 이곳에서 세션을 받아와야해요
        ResponseDto responseDto = menuService.create(sessionUserDto.getId(), requestDto);
        return ResponseEntity.status(
                SuccessCode.MENU_CREATED.getHttpStatus()).body(
                ApiResponseDto.success(SuccessCode.MENU_CREATED, responseDto)
        );
    }

    @PutMapping("/{menuId}")
    public ResponseEntity<ApiResponseDto<ResponseDto>> updateMenu(
            // 여기도 추가
            @SessionAttribute("loginUser") SessionUserDto sessionUserDto,
            @PathVariable Long menuId,
            @RequestBody @Valid RequestDto requestDto) {
        ResponseDto responseDto = menuService.update(sessionUserDto.getId(), menuId, requestDto);
        return ResponseEntity.status(SuccessCode.MENU_UPDATED.getHttpStatus()).body(
                ApiResponseDto.success(SuccessCode.MENU_UPDATED, responseDto)
        );
    }

    @DeleteMapping("/{menuId}")
    public ResponseEntity<Void> deleteMenu(
            @SessionAttribute("loginUser") SessionUserDto sessionUserDto,
            @PathVariable Long menuId) {
        menuService.delete(sessionUserDto.getId(), menuId);
        return ResponseEntity.noContent().build();
    }
}
