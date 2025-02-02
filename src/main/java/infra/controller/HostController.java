package infra.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import infra.service.RoomService;
import infra.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * API
 * - host 룸 등록
 * - host 룸 삭제
 * - host 룸 수정
 */
@RequiredArgsConstructor
@RequestMapping("/host")
@Controller
@Slf4j
public class HostController {

	private final RoomService roomService;
	
}
