package com.opengalk.server.控制层;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@CrossOrigin
@RestController
@RequestMapping("/studentManagement")
@PreAuthorize("hasAuthority('1')")
public class StudentManagementController {


}
