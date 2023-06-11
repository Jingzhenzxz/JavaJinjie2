package com.wuan.attendance.controller;

import com.wuan.attendance.dto.GroupDTO;
import com.wuan.attendance.model.Group;
import com.wuan.attendance.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/groups")
@PreAuthorize("hasRole('ADMIN')") // 需要安全框架的支持，例如Spring Security
// 该注解会检查用户的角色，需要将角色信息设置到 Spring Security 的上下文（Context）中。
public class AdminGroupController {
    private final GroupService groupService;

    @Autowired
    public AdminGroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<GroupDTO>> getAllGroups() {
        List<GroupDTO> groups = groupService.findAll();
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Object> getGroupByName(@PathVariable String name) {
        if (name == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("未指定群组");
        }

        GroupDTO groupDTO = groupService.findByName(name);
        return ResponseEntity.ok(groupDTO);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createGroup(@RequestBody GroupDTO groupDTO) {
        Integer groupId = groupDTO.getId();
        if (groupService.findById(groupId) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Group already exists");
        }

        boolean created = groupService.insert(groupDTO);
        if (created) {
            return ResponseEntity.ok(groupDTO);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Create group failed");
        }
    }

    @PutMapping("/update/{name}")
    public ResponseEntity<Object> updateGroup(@PathVariable String name, @RequestBody GroupDTO groupDTO) {
        if (name == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("未指定要更新的群组");
        }

        boolean updated = groupService.update(groupDTO);
        if (updated) {
            return ResponseEntity.status(HttpStatus.CREATED).body(groupDTO);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update group failed");
        }
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<Object> deleteGroup(@PathVariable String name) {
        if (name == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id is not provided");
        }

        if (groupService.findByName(name) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Group does not exist");
        }

        GroupDTO groupDTO = groupService.findByName(name);
        if (groupDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("未找到该群组");
        }

        Integer groupId = groupDTO.getId();
        boolean deleted = groupService.delete(groupId);
        if (deleted) {
            return ResponseEntity.ok("Delete group successful");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Delete group failed");
        }
    }
}
