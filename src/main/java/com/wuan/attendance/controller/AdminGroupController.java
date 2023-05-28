package com.wuan.attendance.controller;

import com.wuan.attendance.dto.GroupDTO;
import com.wuan.attendance.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/admin/groups")
@PreAuthorize("hasRole('ADMIN')")
public class AdminGroupController {

    @Autowired
    private GroupService groupService;
    private HttpServletRequest httpServletRequest;

    @GetMapping
    public ResponseEntity<List<GroupDTO>> getAllGroups() {
        List<GroupDTO> groups = groupService.findAll();
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getGroupById(@PathVariable Integer id) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("未指定群组");
        }

        GroupDTO groupDTO = groupService.findById(id);
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

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateGroup(@PathVariable Integer id, @RequestBody GroupDTO groupDTO) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("未指定要更新的群组");
        }

        boolean updated = groupService.update(groupDTO);
        if (updated) {
            return ResponseEntity.status(HttpStatus.CREATED).body(groupDTO);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update group failed");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteGroup(@PathVariable Integer id) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id is not provided");
        }

        if (groupService.findById(id) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Group does not exist");
        }

        boolean deleted = groupService.delete(id);
        if (deleted) {
            return ResponseEntity.ok("Delete group successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Delete group failed");
        }
    }
}