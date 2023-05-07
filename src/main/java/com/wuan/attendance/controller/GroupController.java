package com.wuan.attendance.controller;

import com.wuan.attendance.dto.GroupDTO;
import com.wuan.attendance.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @GetMapping("/{id}")
    public ResponseEntity<GroupDTO> getGroupById(@PathVariable Integer id) {
        GroupDTO groupDTO = groupService.findById(id);
        return ResponseEntity.ok(groupDTO);
    }
}
