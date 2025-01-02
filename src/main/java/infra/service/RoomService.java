package infra.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import infra.common.constant.SearchType;
import infra.dto.RoomDto;
import infra.entity.Room;
import infra.repository.RoomRepository;
import infra.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoomService {
	
	private final RoomRepository roomRepository;
	private final UserRepository userRepository;
	

	@Transactional
    public void registerRoom() {
		

	}
	
	public ResponseEntity<String> updateRoom(Long rid, RoomDto roomDto) {

		// ID로 방을 찾음
        Optional<Room> optionalRoom = roomRepository.findById(rid);

        if (optionalRoom.isPresent()) {
            Room room = optionalRoom.get();
            // DTO의 데이터를 엔티티에 반영
            room.setName(roomDto.getName());
            room.setMaxPeople(roomDto.getMaxPeople());
            room.setPrice(roomDto.getPrice());
            room.setAddr1(roomDto.getAddr1());
            room.setAddr2(roomDto.getAddr2());
            room.setAddr3(roomDto.getAddr3());
            room.setContent(roomDto.getContent());

            // 엔티티 저장
            roomRepository.save(room);
            return ResponseEntity.ok("스터디룸 수정이 성공적으로 되었습니다");
        } else {
            return ResponseEntity.ok("스터디룸 수정이 실패했습니다");
        }
    }
	
	
	// 방 삭제 메서드
    public ResponseEntity<String> deleteRoom(Long rid) {
        try {
            // Optional로 Room 객체를 찾음
            Room room = roomRepository.findById(rid)
                .orElseThrow(() -> new EntityNotFoundException("Room with ID " + rid + " not found."));

            // 해당 Room 삭제
            roomRepository.delete(room);

            return ResponseEntity.ok(" 스터디룸" + rid + "이 삭제되었습니다 .");
        } catch (EntityNotFoundException e) {
            // 해당 방이 없으면 404 오류 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            // 예기치 않은 에러가 발생하면 500 오류 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

   
	public Page<RoomDto> searchRooms(Pageable pageable) {
		
		
		return null;
	}

	public Page<RoomDto> searchRoomsWithTypeAndValue(SearchType searchType, String searchValue, Pageable pageable) {
		if(searchValue == null || searchValue.isBlank()) {
			
		}
		
	
		
		switch (searchType) {
	
			case REGION: {
				
				break;
			}
			case MAX_PEOPLE: {
				
				break;
			}
			case NAME: {

				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + searchType);
		}
		
		return null;
		
		
	}
	
	public RoomDto getRoom (Long mid) {
		return  RoomDto.from(roomRepository.findById(mid).orElseThrow());
    }

	
	
}
