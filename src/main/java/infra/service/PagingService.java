package infra.service;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

@Service
public class PagingService {

	// 화면당 보여줄 페이지의 개수
	private final static int PAGE_LENGTH = 10;
	
	
	// 화면에서 start, end 페이지를 정의
	public List<Integer> getPagingNumbers(int pageNumber, int totalPages) {
		int startPage = Math.max((pageNumber - PAGE_LENGTH / 2),0);
		int endPage = Math.min(startPage + PAGE_LENGTH,totalPages);
		
		return IntStream.range(startPage, endPage).boxed().toList();
		
	}
	
	public int getPageLength() {
		return PAGE_LENGTH;
	}

}
