package com.example.delivery.menu.controller;

import com.example.delivery.menu.dto.RequestDto;
import com.example.delivery.menu.dto.ResponseDto;
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


    @PostMapping
    public ResponseEntity<ResponseDto> createMenu(@RequestBody @Valid RequestDto requestDto) {
        ResponseDto responseDto = menuService.create(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{menuId}")
    public ResponseEntity<ResponseDto> updateMenu(
            @PathVariable Long menuId,
            @RequestBody @Valid RequestDto requestDto) {
        ResponseDto responseDto = menuService.update(menuId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{menuId}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long menuId) {
        menuService.delete(menuId);
        return ResponseEntity.noContent().build();
    }
}
