package com.wuan.attendance.controller;

import com.wuan.attendance.dto.GroupDTO;
import com.wuan.attendance.service.GroupService;
import com.wuan.attendance.service.UserGroupService;
import com.wuan.attendance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/user/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;
    private UserService userService;
    private UserGroupService userGroupService;
    private HttpServletRequest httpServletRequest;

    @GetMapping
    public ResponseEntity<Object> getMyGroups() {
        Integer userId = (Integer) httpServletRequest.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        GroupDTO groups = groupService.findById(userId);
        return ResponseEntity.ok(groups);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateMyGroup(@RequestBody GroupDTO groupDTO) {
        boolean updated = groupService.update(groupDTO);
        if (updated) {
            return ResponseEntity.status(HttpStatus.CREATED).body(groupDTO);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Update group failed");
        }
    }

    @DeleteMapping("/delete/{groupId}")
    public ResponseEntity<Object> deleteMyGroup(@PathVariable Integer groupId) {
        Integer userId = (Integer) httpServletRequest.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        boolean deleted = userGroupService.deleteSomeGroupsOfUser(userId, Stream.of(groupId).collect(Collectors.toList()));
        if (deleted) {
            return ResponseEntity.ok().body("Delete group successfully");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Delete group failed");
        }
    }
}
