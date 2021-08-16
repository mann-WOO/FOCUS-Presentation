package com.example.demo.api.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.api.request.RoomRegisterPostReq;
<<<<<<< HEAD
import com.example.demo.api.request.RoomUpdateReq;
import com.example.demo.db.entity.Rooms;
=======
import com.example.demo.api.request.RoomUpdatePostReq;
>>>>>>> 80e3e7f841b5fa3d56613e6185fa8c16fbc31774
import com.example.demo.api.response.BaseResponseBody;
import com.example.demo.api.response.ParticipantGetRes;
import com.example.demo.api.response.RoomGetRes;
import com.example.demo.api.service.ParticipantService;
import com.example.demo.api.service.RoomService;
import com.example.demo.db.entity.Rooms;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "유저 API", tags = { "Room" })
@RestController
//@CrossOrigin(origins={"http://i5a107.p.ssafy.io:8446/"})
@RequestMapping("/rooms")
public class RoomController {
	@Autowired
	RoomService roomService;

	@Autowired
	ParticipantService participantService;

	private final Logger log = LoggerFactory.getLogger(RoomController.class);

	@PostMapping("/create")
	@ApiOperation(value = "방생성")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm A", timezone = "Asia/Seoul")
	@ApiResponses({ @ApiResponse(code = 200, message = "성공"), @ApiResponse(code = 401, message = "인증 실패"),
			@ApiResponse(code = 404, message = "사용자 없음"), @ApiResponse(code = 500, message = "서버 오류") })
	public ResponseEntity<? extends BaseResponseBody> register(
<<<<<<< HEAD
			@RequestBody @ApiParam(value="방정보", required = true) RoomRegisterPostReq registerInfo) {
		try{
			System.out.println("[createroom] register: registerInfo: "+registerInfo);
			log.info("[register] room register info: {}", registerInfo);
			Rooms room = roomService.createRoom(registerInfo);
			log.info("[register] room : {}", room);
			return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
		}catch (Exception e){
			e.printStackTrace();
		}
		return ResponseEntity.status(500).body(BaseResponseBody.of(500, "fail"));
	}
	
	@PutMapping("/update/{roomId}")
	@ApiOperation(value = "방업데이트") 
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 401, message = "인증 실패"),
        @ApiResponse(code = 404, message = "사용자 없음"),
        @ApiResponse(code = 500, message = "서버 오류")
    })
	public ResponseEntity<? extends BaseResponseBody> update(
			@RequestBody @ApiParam(value="방업데이트", required = true) RoomUpdateReq roomInfo) {
		try{
			log.info("[update] roomUpdateReq: {}", roomInfo);
			Rooms room = roomService.updateRoom(roomInfo);
			log.info("[update] room: {}", room);
			return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
		}catch(Exception e){
			e.printStackTrace();
		}
		return ResponseEntity.status(500).body(BaseResponseBody.of(500, "fail"));
	}
	
	@DeleteMapping("/delete/{roomId}")
	@ApiOperation(value = "방삭제") 
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 401, message = "인증 실패"),
        @ApiResponse(code = 404, message = "사용자 없음"),
        @ApiResponse(code = 500, message = "서버 오류")
    })
=======
			@RequestBody @ApiParam(value = "방정보", required = true) RoomRegisterPostReq registerInfo) {
		System.out.println("[createroom] register: registerInfo: " + registerInfo);
		log.info("[register] room register info: {}", registerInfo);
		Rooms room = roomService.createRoom(registerInfo);
		log.info("[register] room : {}", room);
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
	}

	@PostMapping("/updateroom/{roomId}")
	@ApiOperation(value = "방업데이트")
	@ApiResponses({ @ApiResponse(code = 200, message = "성공"), @ApiResponse(code = 401, message = "인증 실패"),
			@ApiResponse(code = 404, message = "사용자 없음"), @ApiResponse(code = 500, message = "서버 오류") })
	public ResponseEntity<? extends BaseResponseBody> update(
			@RequestBody @ApiParam(value = "방업데이트", required = true) RoomUpdatePostReq registerInfo) {
		System.out.println(registerInfo.getUser_id());
		Rooms room = roomService.updateRoom(registerInfo);

		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
	}

	@PostMapping("/deleteroom/{roomId}")
	@ApiOperation(value = "방삭제")
	@ApiResponses({ @ApiResponse(code = 200, message = "성공"), @ApiResponse(code = 401, message = "인증 실패"),
			@ApiResponse(code = 404, message = "사용자 없음"), @ApiResponse(code = 500, message = "서버 오류") })
>>>>>>> 80e3e7f841b5fa3d56613e6185fa8c16fbc31774
	public ResponseEntity<? extends BaseResponseBody> deleteRoom(@PathVariable("roomId") int roomId) {

		// 임의로 리턴된 User 인스턴스. 현재 코드는 회원 가입 성공 여부만 판단하기 때문에 굳이 Insert 된 유저 정보를 응답하지 않음.
		Rooms room = roomService.getRoom(roomId);
		roomService.deleteRoom(roomId);

		if (room == null) {
			return ResponseEntity.status(500).body(BaseResponseBody.of(500, "fail"));
		}
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "성공"));
	}

	@GetMapping("/")
	@ApiOperation(value = "전체 방 보기")
	@ApiResponses({ @ApiResponse(code = 200, message = "성공"), @ApiResponse(code = 401, message = "인증 실패"),
			@ApiResponse(code = 404, message = "사용자 없음"), @ApiResponse(code = 409, message = "이미 존재하는 유저"),
			@ApiResponse(code = 500, message = "서버 오류") })
	public ResponseEntity<List<RoomGetRes>> showRooms() {

		List<RoomGetRes> rooms = roomService.findAll();
		for (RoomGetRes item : rooms) {
			item.setParticipants(participantService.getParticipantByRoomId(item.getRoom_id()));
		}
		return new ResponseEntity<List<RoomGetRes>>(rooms, HttpStatus.OK);
	}

	@GetMapping("/user/{userId}")
	@ApiOperation(value = "유저별 전체 방 보기")
	@ApiResponses({ @ApiResponse(code = 200, message = "성공"), @ApiResponse(code = 401, message = "인증 실패"),
			@ApiResponse(code = 404, message = "사용자 없음"), @ApiResponse(code = 409, message = "이미 존재하는 유저"),
			@ApiResponse(code = 500, message = "서버 오류") })
	public ResponseEntity<List<RoomGetRes>> showRoomsbyuser(@PathVariable("userId") int userId) {

		List<RoomGetRes> rooms = roomService.findbyuser(userId);
		for (RoomGetRes item : rooms) {
			item.setParticipants(participantService.getParticipantByRoomId(item.getRoom_id()));
		}
		return new ResponseEntity<List<RoomGetRes>>(rooms, HttpStatus.OK);
	}
<<<<<<< HEAD
	
	@GetMapping("/{roomId}")
	@ApiOperation(value = "방하나보기")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공"),
			@ApiResponse(code = 401, message = "인증 실패"),
			@ApiResponse(code = 404, message = "사용자 없음"),
			@ApiResponse(code = 409, message = "이미 존재하는 유저"),
			@ApiResponse(code = 500, message = "서버 오류")
	})
	public ResponseEntity<RoomGetRes> showRoomone(@PathVariable("roomId") int roomId) {
		Rooms room = roomService.getRoom(roomId);
		List<ParticipantGetRes> participants=participantService.getParticipantByRoomId(roomId);
		RoomGetRes roomget = new RoomGetRes(room.getName(),room.getDescription(), room.getStartTime().toLocalDateTime(),room.getUsers().getUserId(),room.getUsers().getName(), room.getRoomId());
		if(room.getEndTime()==null){
			roomget.setEndTime(null);
		}else{
=======

	@GetMapping("/{roomId}")
	@ApiOperation(value = "방하나보기")
	@ApiResponses({ @ApiResponse(code = 200, message = "성공"), @ApiResponse(code = 401, message = "인증 실패"),
			@ApiResponse(code = 404, message = "사용자 없음"), @ApiResponse(code = 409, message = "이미 존재하는 유저"),
			@ApiResponse(code = 500, message = "서버 오류") })
	public ResponseEntity<RoomGetRes> showRoomone(@PathVariable("roomId") int roomId) {
		Rooms room = roomService.getRoom(roomId);
		List<ParticipantGetRes> participants = participantService.getParticipantByRoomId(roomId);
		RoomGetRes roomget = new RoomGetRes(room.getName(), room.getDescription(),
				room.getStartTime().toLocalDateTime(), room.getUsers().getUserId(), room.getRoomId());
		if (room.getEndTime() == null) {
			roomget.setEndTime(null);
		} else {
>>>>>>> 80e3e7f841b5fa3d56613e6185fa8c16fbc31774
			roomget.setEndTime(room.getEndTime().toLocalDateTime());
		}
		roomget.setParticipants(participants);

<<<<<<< HEAD
	        return new ResponseEntity<RoomGetRes>(roomget,HttpStatus.OK);
	    }
=======
		return new ResponseEntity<RoomGetRes>(roomget, HttpStatus.OK);
	}
>>>>>>> 80e3e7f841b5fa3d56613e6185fa8c16fbc31774
}
