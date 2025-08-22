### [실습 2-4] 도서 관리 시스템 구현하기 ( 1:1 연관관계 )

[실습2-4] Spring Boot와 JPA(Java Persistence API) 활용
도서 관리 시스템 구현하기 ( 1:1 연관관계 )

1. 엔티티 클래스
   Book.java
   책에 관한 기본 정보 (제목, 저자, ISBN, 가격, 출판일)를 저장합니다.
   @OneToOne 관계를 통해 BookDetail과 연결되어 있습니다.
   mappedBy="book"으로 지정하여 Book이 관계의 주인이 아님을 표시합니다.
   BookDetail.java
   책에 대한 상세 정보 (설명, 언어, 페이지 수, 출판사, 표지 이미지 URL, 에디션)을 저장합니다.
   @OneToOne(fetch = FetchType.LAZY)로 Book 엔티티와 지연 로딩 관계를 설정합니다.
   @JoinColumn(name = "book_id", unique = true)로 외래 키 설정 및 유니크 제약조건을 추가합니다.
   이 엔티티가 관계의 주인입니다 (외래 키를 소유).
   1:1 관계: Book과 BookDetail은 일대일 관계를 가지며, 각 책은 하나의 상세 정보만 가질 수 있습니다.
   지연 로딩: FetchType.LAZY를 사용하여 필요할 때만 연관된 데이터를 로드합니다.
   영속성 컨텍스트: CascadeType.ALL을 사용하여 Book을 저장/삭제할 때 BookDetail도 함께 처리됩니다.
2. 레포지토리 인터페이스
   BookRepository.java
   책을 ID, ISBN, 저자, 제목 등으로 검색하는 메서드를 제공합니다.
   findByIdWithBookDetail과 findByIsbnWithBookDetail 메서드로 Book과 BookDetail을 함께 로드합니다.
   중복 ISBN 확인을 위한 existsByIsbn 메서드를 제공합니다.
   BookDetailRepository.java
   findByBookId로 특정 책의 상세 정보를 조회합니다.
   findByIdWithBook로 상세 정보와 함께 책 정보를 로드합니다.
   findByPublisher로 특정 출판사의 책 상세 정보를 조회합니다.
3. 레포지토리 테스트
   BookRepository 테스트 클래스 Source
   구현하지 않고 위의 링크에서 복사해서 실행만 해보시면 됩니다.
   BookRepositoryTest.java
   책과 책 상세 정보의 생성, 조회 기능을 테스트합니다.
   다양한 검색 조건(ISBN, 저자)에 따른 조회 기능을 검증합니다.
   책과 책 상세 정보 간의 1:1 관계가 올바르게 작동하는지 확인합니다.
4. DTO 클래스
   BookDTO 클래스 Source
   구현하지 않고 위의 링크에서 복사해서 사용하시면 됩니다.
   BookDTO.java
   Request: 책 생성/수정 시 사용하는 DTO입니다.
   BookDetailDTO: 책 상세 정보 요청용 중첩 DTO입니다.
   Response: API 응답용 DTO입니다.
   BookDetailResponse: 책 상세 정보 응답용 중첩 DTO입니다.
   유효성 검사: ISBN 패턴 검사, 가격 음수 방지, 출간일 과거 날짜 제한 등의 검증 규칙이 포함되어 있습니다.
5. 서비스 클래스
   BookService.java
   비즈니스 로직을 처리하는 서비스 계층입니다.
   주요 기능:
   모든 책 조회
   ID 또는 ISBN으로 특정 책 조회
   저자나 제목으로 책 검색
   책 생성/수정/삭제
   ISBN 중복 검사
6. 컨트롤러 클래스
   BookController.java
   REST API 엔드포인트를 제공합니다.
   주요 엔드포인트:
   GET /api/books - 모든 책 조회
   GET /api/books/{id} - ID로 책 조회
   GET /api/books/isbn/{isbn} - ISBN으로 책 조회
   GET /api/books/search/author?author={author} - 저자로 책 검색
   GET /api/books/search/title?title={title} - 제목으로 책 검색
   POST /api/books - 책 생성
   PUT /api/books/{id} - 책 수정
   DELETE /api/books/{id} - 책 삭제
1. 등록 (post)
   http://localhost:8080/api/books
   {
   "title": "Clean Code",
   "author": "Robert C. Martin",
   "isbn": "9780132350884",
   "price": 45,
   "publishDate": "2008-08-01",
   "detailRequest": {
   "description": "A handbook of agile software craftsmanship",
   "language": "English",
   "pageCount": 464,
   "publisher": "Prentice Hall",
   "coverImageUrl": "https://example.com/cleancode.jpg",
   "edition": "1st"
   }
   }

{
"title": "Effective Java",
"author": "Joshua Bloch",
"isbn": "9780134685991",
"price": 55,
"publishDate": "2017-12-27",
"detailRequest": {
"description": "The definitive guide to Java programming language",
"language": "English",
"pageCount": 416,
"publisher": "Addison-Wesley",
"coverImageUrl": "https://example.com/effectivejava.jpg",
"edition": "3rd"
}
}

2. ISBN으로 책 조회 ( GET )
   http://localhost:8080/api/books/isbn/9780132350884

3. 저자로 책 검색
   http://localhost:8080/api/books/search/author?author=Robert


도서 관리 시스템 구현하기 ( 1:1 연관관계 ) - 부분수정(Patch) 기능
주요 개선사항:
1. PATCH 메서드 추가
   PATCH /api/books/{id}: Book의 일부 필드만 수정
   PATCH /api/books/{id}/detail: BookDetail의 일부 필드만 수정
2. 새로운 DTO 클래스
   PatchRequest: Book 부분 수정용 (모든 필드가 Optional)
   BookDetailPatchRequest: BookDetail 부분 수정용
3. 부분 업데이트 로직
   null 체크를 통해 제공된 필드만 업데이트
   제공되지 않은 필드는 기존 값 유지

if (request.getTitle() != null) {
book.setTitle(request.getTitle());
}
4. ISBN 체크 로직 설명
   if (!book.getIsbn().equals(request.getIsbn()) &&
   bookRepository.existsByIsbn(request.getIsbn())) {
   throw new BusinessException(ErrorCode.ISBN_DUPLICATE, request.getIsbn());
   }
   이 로직이 필요한 이유:
   시나리오 1: ISBN을 변경하지 않는 경우 → 체크 불필요
   시나리오 2: 새로운 ISBN으로 변경하는데 이미 다른 책이 사용 중 → 오류 발생해야 함
   시나리오 3: 새로운 고유한 ISBN으로 변경 → 정상 처리


4. 수정 (put) - 전체수정
   http://localhost:8080/api/books/1
   {
   "title": "Clean Code",
   "author": "Robert C. Martin",
   "isbn": "9780132350884",
   "price": 45,
   "publishDate": "2008-08-01",
   "detailRequest": {
   "description": "A handbook of agile software craftsmanship",
   "language": "English",
   "pageCount": 464,
   "publisher": "Prentice Hall",
   "coverImageUrl": "https://example.com/cleancode.jpg",
   "edition": "1st"
   }
   }

4-1. 수정 (patch)
(제목만 수정) PATCH
http://localhost:8080/api/books/1
{
"title": "새로운 제목"
}
// 가격과 언어만 수정 PATCH
http://localhost:8080/api/books/1
{
"price": 15000,
"detailRequest": {
"language": "Korean"
}
}
// BookDetail의 설명만 수정 PATCH
http://localhost:8080/api/books/1/detail
{
"description": "새로운 책 설명"
}
