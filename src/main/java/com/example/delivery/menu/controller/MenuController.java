package com.example.delivery.menu.controller;

import com.example.delivery.menu.dto.MenuRequestDto;
import com.example.delivery.menu.dto.MenuResponseDto;
import com.example.delivery.menu.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    // 메뉴 생성
    @PostMapping
    public ResponseEntity<MenuResponseDto> createMenu(@RequestBody @Valid MenuRequestDto requestDto) {
        MenuResponseDto responseDto = menuService.createMenu(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 메뉴 수정
    @PutMapping("/{menuId}")
    public ResponseEntity<MenuResponseDto> updateMenu(
            @PathVariable Long menuId,
            @RequestBody @Valid MenuRequestDto requestDto
    ) {
        MenuResponseDto responseDto = menuService.updateMenu(menuId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 메뉴 삭제
    @DeleteMapping("/{menuId}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long menuId) {
        menuService.deleteMenu(menuId);
        return ResponseEntity.noContent().build();
    }
}

