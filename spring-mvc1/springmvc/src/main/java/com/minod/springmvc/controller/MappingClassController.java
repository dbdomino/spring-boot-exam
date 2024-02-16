package com.minod.springmvc.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mapping/users")
public class MappingClassController {
    //회원 관리를 HTTP API로 만든다 생각하고 매핑을 어떻게 하는지 알아보자.
    //(실제 데이터가 넘어가는 부분은 생략하고 URL 매핑만)
    /** 회원 목록 조회:  /users
    * GET /mapping/users
    */
    @GetMapping
    public String users() {
        return "get users";
    }
    /** 회원 등록:       /users
     * POST /mapping/users
     */
    @PostMapping
    public String addUser() {
        return "post user";
    }
    /**   회원 조회:     /users/{userId}
     * GET /mapping/users/{userId}
     */
    @GetMapping("/{userId}")
    public String findUser(@PathVariable String userId) {
        return "get userId=" + userId;
    }
    /**   회원 수정:      /users/{userId}
     * PATCH /mapping/users/{userId}
     */
    @PatchMapping("/{userId}")
    public String updateUser(@PathVariable String userId) {
        return "update userId=" + userId;
    }
    /**  회원 삭제:    /users/{userId}
     * DELETE /mapping/users/{userId}
     */
    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable String userId) {
        return "delete userId=" + userId;
    }
}
