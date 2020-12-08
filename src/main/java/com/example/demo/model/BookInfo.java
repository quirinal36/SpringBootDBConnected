package com.example.demo.model;

import java.time.LocalDateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BookInfo {
	int id;
  private String title;   // 도서 제목
  private String contents; // 도서 소개
  private String url;         // 도서 상세 URL
  private String isbn;        // 국제 표준 도서번호
  private LocalDateTime datetime; // 도서 출판날짜 [YYYY]-[MM]-[DD]T[hh]:[mm]:[ss].000+[tz]
  private String[] authors;       // 도서 저자 리스트
  private String publisher;       // 도서 출판사
  private String[] translators;   // 도서 번역자 리스트
  private int price;              // 도서 정가
  private int sale_price;         // 도서 판매가
  private String thumbnail;       // 도서 표지 미리보기 URL
  private String status;          // 도서 판매 상태 정보 (정상, 품절, 절판 등)
  
  public static BookInfo parse(JSONObject input) {
    BookInfo info = new BookInfo();
    if(input.has("authors")) {
      JSONArray authorsArr = input.getJSONArray("authors");
      String []authors = new String[authorsArr.length()];
      for(int i=0; i<authorsArr.length(); i++) {
        authors[i] = authorsArr.getString(i);
      }
      info.setAuthors(authors);
    }
    if(input.has("contents")) {
      info.setContents(input.getString("contents"));
    }
    if(input.has("isbn")) {
      info.setIsbn(input.getString("isbn"));
    }
    if(input.has("price")) {
      info.setPrice(input.getInt("price"));
    }
    if(input.has("publisher")) {
      info.setPublisher(input.getString("publisher"));
    }
    if(input.has("sale_price")) {
      info.setSale_price(input.getInt("sale_price"));
    }
    if(input.has("status")) {
      info.setStatus(input.getString("status"));
    }
    if(input.has("thumbnail")) {
      info.setThumbnail(input.getString("thumbnail"));
    }
    if(input.has("title")) {
      info.setTitle(input.getString("title"));
    }
    if(input.has("url")) {
      info.setUrl(input.getString("url"));
    }
    return info;
  }
}
