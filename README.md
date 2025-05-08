# 웹사이트 개발 로드맵

## 1. 사전 준비
1. 요구사항 수집  
   - **기본 기능**  
     1. 영상 업로드 (커버곡·자작곡)  
     2. 영상 스트리밍 (HLS/DASH)  
     3. 회원관리 (소셜 로그인, 프로필, 팔로우/구독)  
     4. 검색·추천 (태그/키워드, 인기·최신 정렬)  
     5. 좋아요·댓글 기능  
     6. 댓글 유해 단어 필터링  
     7. 주간·월간 좋아요 TOP5 사이드바 제공  
     8. 주간·월간 TOP5 보상(기프티콘 등)  
   - **UI/UX 요구사항**  
     1. 유튜브와 유사한 친숙한 UI/UX 제공  
     2. 썸네일에 마우스 오버 시 하이라이트 영상 자동 재생  
   - **비기능 요구사항**  
     - 유지보수성 및 운영 가능성  
     - 시스템 확장성  
     - 개발 생산성  
     - 인프라·운영 비용 고려  
2. 기술 스택 및 아키텍처 확정  
   - React(JSX), Node.js, Spring Boot, MySQL, Docker, Kubernetes  
   - 마이크로서비스 vs 모놀리식 결정  
3. 개발 환경 구성  
   - Git 리포지토리 생성 (GitHub)  
   - 코드 스타일/룰 설정 (ESLint, Prettier, Checkstyle)  
   - 로컬 Docker Compose 파일 작성 (MySQL, Redis, MinIO 등)  

## 2. 백엔드 설계 및 구현
1. 데이터베이스 설계  
   - ERD 작성 (users, videos, follows, comments, likes 등)  
   - MySQL 스키마 생성 스크립트  
2. Spring Boot 서비스 구조  
   - 모듈 분리: UserService, VideoService, MetadataService  
   - 공통 모듈: 인증(JWT), 예외처리, 로깅(AOP)  
3. REST API 설계  
   - OpenAPI(Swagger) 명세 작성  
   - 엔드포인트 정의 (회원가입/로그인, 업로드, 메타데이터 CRUD 등)  
4. 구현 & 테스트  
   - Controller → Service → Repository 계층별 코드 작성  
   - 단위 테스트(JUnit) 및 통합 테스트 작성  
   - CI 파이프라인에 테스트 자동화 적용  

## 3. 영상 처리 파이프라인
1. 파일 업로드 처리  
   - Express + Multer 설정 (Node.js)  
   - 대용량 업로드 최적화 (Chunked Upload)  
2. 트랜스코딩 & 스트리밍 준비  
   - FFmpeg 설치 및 스크립트 작성  
   - HLS/DASH 세그먼트 생성  
3. 오브젝트 스토리지 연동  
   - MinIO/S3 버킷 생성 및 권한 설정  
   - 업로드된 세그먼트 업로드 API 개발  
4. CDN 연동  
   - CloudFront/Nginx 설정  
   - CORS, 캐싱 정책 최적화  

## 4. 프론트엔드 설계 및 구현
1. 프로젝트 초기화  
   - Vite + React 템플릿 생성  
   - 폴더 구조 설계 (components, pages, services, hooks 등)  
2. 인증 흐름 구현  
   - 로그인/회원가입 폼  
   - JWT 토큰 저장 (localStorage) 및 인터셉터 설정  
3. 주요 화면 개발  
   - 홈 피드 (VideoList, VideoCard)  
   - 업로드 페이지 (UploadForm, ProgressBar)  
   - 비디오 플레이어 (video.js 또는 HTML5 video)  
   - 프로필 & 팔로우 기능  
4. 상태 관리  
   - Recoil/Redux 적용  
   - 전역 상태(인증, 피드, 검색결과) 관리  

## 5. 검색·추천 엔진
1. 키워드 검색  
   - MySQL 인덱스 설계 (FULLTEXT, B-Tree)  
   - 백엔드 API 및 프론트 연동  
2. 인기·최신 정렬  
   - 조회수, 좋아요 집계 로직  
   - 페이징 및 무한 스크롤 구현  
3. 개인화 추천(옵션)  
   - 기본 알고리즘 설계 (Collaborative Filtering 등)  
   - 별도 마이크로서비스로 분리  

## 6. CI/CD 구축
1. Dockerfile 작성  
   - 각 서비스별 베이스 이미지 설정  
2. GitHub Actions 파이프라인  
   - 빌드 → 테스트 → 이미지 빌드 & 푸시  
3. Kubernetes 매니페스트  
   - Deployment, Service, Ingress 작성  
   - ConfigMap/Secret 관리  
   - HPA(자동 스케일링) 설정  
4. 배포 자동화  
   - ArgoCD/GitOps 연동  

## 7. 모니터링·로깅·운영
1. 로깅  
   - ELK/EFK 스택 구성  
   - 로그 레벨·포맷 표준화  
2. 모니터링  
   - Prometheus + Grafana 대시보드  
   - 주요 지표(응답시간, 에러율, 리소스 사용량) 시각화  
3. 알림  
   - Slack/Email 알림 연동 (Prometheus Alertmanager)  
4. 장애 대응 프로세스 문서화  

## 8. 테스트 및 배포
1. 스테이징 환경 배포  
   - 전체 E2E 테스트 (Cypress/Selenium)  
   - 성능 테스트(JMeter)  
2. 프로덕션 롤아웃  
   - 블루/그린 또는 카나리 배포 전략 적용  
3. 운영 매뉴얼 작성  
   - 장애 대응, 백업·복구 절차  
   - 정기 점검 체크리스트  
